from decimal import Decimal, localcontext

def solution(s):
    with localcontext() as ctx:#change context for this part of the code only.
        ctx.prec = 1000
        solution = beatty_seq(int(s), Decimal(2).sqrt())
    return solution
    
def beatty_seq(s, alpha):
    n = int(s)
    if n == 0: 
        return "0"
    n_prime = int((alpha - 1) * n)
    sum_n = n * (n + 1) / 2
    sum_n_prime = n_prime * (n_prime + 1) / 2
    result = n * n_prime + sum_n - sum_n_prime - int(beatty_seq(str(n_prime), alpha))
    return str(result)
    
print(solution("5"))#19.
print(solution("77"))#4208.
print(solution("55109"))#2147496038.
print(solution("23223423"))#381362049543566.
print(solution("86032128652"))#5233670046269023958676
print(solution("86975757568698689689689757857858656456342542313435468576870970979687858598677678578564536321234567890"))#5349108936528018775311711221684838745555627973886608082483172195366322556543980967825690098844361050956029699760724588747414393462546584533649411083945437343242751648755796153201952590266229889173689597