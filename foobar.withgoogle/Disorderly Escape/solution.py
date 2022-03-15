from fractions import Fraction

def solution(w, h, s):
    term_list = cycle_index_nm(w, h)
    result = 0
    for term in term_list:
        result += substitute(term, s)
    return str(result)
    
def cycle_index_nm(n, m):
    """Compute the cycle index of the permutation from S_n x S_m - Z(M_(n,m))"""
    index_a = cycle_index(n)
    index_b = cycle_index(m)
    #print(index_a)
    #print(index_b)
    term_list = []
    for term_a in index_a:
        for term_b in index_b:
            p_q_list = []
            for a in term_a[1]:
                len_a = a[0]
                inst_a = a[1]
                for b in term_b[1]:
                    len_b = b[0]
                    inst_b = b[1]
                    new_p = lcm(len_a, len_b)
                    new_q = (len_a * len_b * inst_a * inst_b) / new_p
                    p_q_list.append([new_p, new_q])
            new_term = [term_a[0] * term_b[0], p_q_list]
            #combine terms.
            handled = False
            if len(p_q_list) <= 1:
                new_term = [term_a[0] * term_b[0], p_q_list]
                handled = True
            if not handled:
                new_term = [term_a[0] * term_b[0], combine_p_q(p_q_list)]
            term_list.append(new_term)
    #term_list = minimize_term_list(term_list)
    #print(term_list)
    return term_list
    
def cycle_index(n):
    """Compute the cycle index of the symmetric group S_n - Z(S_n)"""
    if n == 0:
        return [[Fraction(1), []]]
    term_list = []
    for l in range(1, n + 1):
        term_list = add(term_list, multiply(l, cycle_index(n - l)))
    return expand(Fraction(1, n), term_list)
    
def expand(fraction, term_list):
    """Multiply the given fraction to every term in term_list"""
    for term in term_list:
        term[0] *= fraction
    return term_list
    
def combine_p_q(p_q_list):
    """Given a term without the fraction, if there are multiple similar subscript, add their power together to get a compact term"""
    for i in range(len(p_q_list) - 1):
        for j in range(i + 1, len(p_q_list)):
            if p_q_list[i][0] == p_q_list[j][0] and p_q_list[i][1] != 0:
                p_q_list[i][1] += p_q_list[j][1]
                #mark this term.
                p_q_list[j][1] = 0
    return [p_q for p_q in p_q_list if p_q[1] != 0]
    
def add(term_list_a, term_list_b):
    """Compute the cycle index of 2 given term lists"""
    term_list = term_list_a + term_list_b
    return term_list if len(term_list) <= 1 else minimize(term_list)
    
def minimize(term_list):
    """Given a term list, add similar terms together to get a compact version of the list"""
    for i in range(len(term_list) - 1):
        for j in range(i + 1, len(term_list)):
            if (set([(a[0], a[1]) for a in term_list[i][1]]) == set([(b[0], b[1]) for b in term_list[j][1]])):
                term_list[i][0] += term_list[j][0]
                #mark this term.
                term_list[j][0] = Fraction(0)
    return [term for term in term_list if term[0] != Fraction(0)]
    
def multiply(p, term_list):
    """Multiple a_p to a term list"""
    for term in term_list:
        included = False
        for a in term[1]:
            if a[0] == p:
                included = True
                a[1] += 1
                break
        if not included:
            term[1].append([p, 1])
    return term_list
    
def substitute(term, value):
    """Substitute real value into a term in the computed formula"""
    term_total = term[0]
    for a in term[1]:
        term_total *= int(value) ** int(a[1])
    return term_total
    
def lcm(a, b):
    """Compute the least common multiple of 2 numbers"""
    return abs(a * b) / gcd(a, b)
    
def gcd(a, b):
    """Compute the greatest common divisor of 2 numbers"""
    return a if b == 0 else gcd(b, a % b)

#print(solution(2,2,2))    
#print(solution(2,3,4))
#print(solution(3,4,4))
#print(solution(8,8,2))
print(solution(12,12,20))

#print(pet_cycle_index(5))
#print(pet_cycle_index_nm(3,4))
#print(pet_cycle_index(12))
#print(pet_cycle_index_nm(8,8))