import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Solution {
    public static String solution(int w, int h, int s) {
        Fraction result = new Fraction(BigInteger.ONE, BigInteger.ONE);
        List<Term> indexNM = cycleIndexNM(w, h);
        for (Term term : indexNM) {
            //System.out.println(result);
            result = result.add(substitute(s, term));
        }
        return result.getNumerator().divide(result.getDenominator()).subtract(BigInteger.ONE).toString();
    }
    
    public static List<Term> cycleIndexNM(int n, int m) {
        List<Term> indexA = cycleIndex(n);
        List<Term> indexB = cycleIndex(m);
        
        List<Term> termList = new ArrayList<Term>();
        
        int lenA, lenB, instA, instB;
        int newP, newQ;
        Term newTerm;
        
        for (Term termA : indexA) {
            for (Term termB : indexB) {
                newTerm = new Term(termA.getFraction().multiply(termB.getFraction()));
                for (int i = 0; i < termA.getSizePQ(); i++) {
                    lenA = termA.getPair(i).getP();
                    instA = termA.getPair(i).getQ();
                    for (int j = 0; j < termB.getSizePQ(); j++) {
                        lenB = termB.getPair(j).getP();
                        instB = termB.getPair(j).getQ();
                        newP = lcm(lenA, lenB);
                        newQ = (lenA * lenB * instA * instB) / newP;
                        newTerm.addPair(newP, newQ);
                    }
                }
                termList.add(newTerm);
            }
        }
        /*for (Term term : termList) {
            System.out.print(term.getFraction().getNumerator() + "/" + term.getFraction().getDenominator() + " (");
            for (int i = 0; i < term.getSizePQ(); i++) {
                System.out.print("(" + term.getPair(i).getP() + "," + term.getPair(i).getQ() + ")");
            }
            System.out.print(") ");
        }
        System.out.println("\n" + termList.size());*/
        return termList;
    }
    
    public static List<Term> cycleIndex(int n) {
        List<Term> termList = new ArrayList<Term>();
        if (n == 0) {
            //.
            termList.add(new Term(new Fraction(BigInteger.ONE, BigInteger.ONE)));
            return termList;
        }
        for (int i = 1; i <= n; i++) {
            termList = addAndMinimize(termList, multiply(i, cycleIndex(n - i)));
        }
        return expand(new Fraction(BigInteger.ONE, BigInteger.valueOf(n)), termList);
    }
    
    public static List<Term> addAndMinimize(List<Term> termListA, List<Term> termListB) {
        termListA.addAll(termListB);
        return termListA.size() <= 1 ? termListA : minimize(termListA);
    }
    
    public static List<Term> minimize(List<Term> termList) {
        for (int i = 0; i < termList.size() - 1; i++) {
            for (int j = i + 1; j < termList.size(); j++) {
                if (sameVar(termList.get(i), termList.get(j))) {
                    termList.get(i).setFraction(termList.get(i).getFraction().add(termList.get(j).getFraction()));
                    //mark this term.
                    termList.get(j).setFraction(new Fraction(BigInteger.ZERO, BigInteger.ONE));
                }
            }
        }
        termList.removeIf(x -> x.getFraction().getNumerator() == BigInteger.ZERO);
        return termList;
    }
    
    public static boolean sameVar(Term termA, Term termB) {
        Set<Pair> setA = new HashSet<Pair>();
        Set<Pair> setB = new HashSet<Pair>();
        for (int i = 0; i < termA.getSizePQ(); i++) {
            setA.add(termA.getPair(i));
        }
        for (int i = 0; i < termB.getSizePQ(); i++) {
            setB.add(termB.getPair(i));
        }
        return setA.equals(setB);
    } 
    
    public static List<Term> multiply(int p, List<Term> termList) {
        boolean included;
        for (Term term : termList) {
            included = false;
            for (int i = 0; i < term.getSizePQ(); i++) {
                if (term.getPair(i).getP() == p) {
                    term.getPair(i).setQ(term.getPair(i).getQ() + 1);
                    included = true;
                    break;
                }
            }
            if (!included) {
                term.addPair(p, 1);
            }
        }
        return termList;
    }
    
    public static List<Term> expand(Fraction f, List<Term> termList) {
        //multiply the fractions of every term with f.
        for (Term a : termList) {
            a.setFraction(a.getFraction().multiply(f));
        }
        return termList;
    }
    
    public static Fraction substitute(int s, Term term) {
        Fraction termTotal = new Fraction(term.getFraction().getNumerator(), term.getFraction().getDenominator());
        for (int i = 0; i < term.getSizePQ(); i++) {
            termTotal = termTotal.multiply(BigInteger.valueOf(s).pow(term.getPair(i).getQ()));
        }
        return termTotal;
    }
    
    public static int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);
    }
    
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    private static class Term {
        private Fraction fraction;
        private List<Pair> pq;
        private int sizePQ;
        
        public Term(Fraction fraction) {
            this.fraction = fraction;
            this.pq = new ArrayList<Pair>();
            this.sizePQ = 0;
        }
        
        //getters.
        public Fraction getFraction() {return this.fraction;}
        public Pair getPair(int index) {return this.pq.get(index);}
        public int getSizePQ() {return this.sizePQ;}
        
        //setters.
        public void setFraction(Fraction f) {this.fraction = f;}
        public void addPair(int p, int q) {
            this.pq.add(new Pair(p, q));
            this.sizePQ++;
        }
        public void setPair(int index, int p, int q) {
            pq.set(index, new Pair(p, q));
        }
    }
    
    private static class Fraction {
        private BigInteger numerator, denominator;
        
        public Fraction(BigInteger numerator, BigInteger denominator) {
            BigInteger gcd = numerator.gcd(denominator);
            this.numerator = numerator.divide(gcd);
            this.denominator = denominator.divide(gcd);
        }
        
        //getters.
        public BigInteger getNumerator() {return this.numerator;}
        public BigInteger getDenominator() {return this.denominator;}
        
        //setters.
        public void setNumerator(BigInteger numer) {this.numerator = numer;}
        public void setDenominator(BigInteger denom) {this.denominator = denom;}
        
        public Fraction multiply(Fraction f) {
            BigInteger numer = this.numerator.multiply(f.numerator);
            BigInteger denom = this.denominator.multiply(f.denominator);
            return new Fraction(numer, denom);
        }
        
        public Fraction multiply(BigInteger b) {
            BigInteger numer = this.numerator.multiply(b);
            BigInteger denom = this.denominator;
            return new Fraction(numer, denom);
        }
        
        public Fraction add(Fraction f) {
            BigInteger numer = this.numerator.multiply(f.denominator).add(f.numerator.multiply(this.denominator));
            BigInteger denom = this.denominator.multiply(f.denominator);
            return new Fraction(numer, denom);
        }
    }
    
    private static class Pair {
        private int p, q;
        
        public Pair(int p, int q) {
            this.p = p;
            this.q = q;
        }
        
        //getters.
        public int getP() {return this.p;}
        public int getQ() {return this.q;}
        
        //setters.
        public void setP(int p) {this.p = p;}
        public void setQ(int q) {this.q = q;}
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {return true;}
            
            if (obj == null) {return false;}
            
            if (obj.getClass() != this.getClass()) {return false;}
            
            final Pair other = (Pair) obj;
            return this.p == other.p && this.q == other.q;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 1;
            hash = prime * hash + p;
            hash = prime * hash + q;
            return hash;
        }
    }
    
    public static void main(String args[]) {
      //System.out.println(solution(2,2,2));
      //System.out.println(solution(2,3,4));
      //System.out.println(solution(3,4,4));
      //System.out.println(solution(8,8,2));
      //System.out.println(solution(12,12,2));
      System.out.println(solution(12,12,20));
    }
}