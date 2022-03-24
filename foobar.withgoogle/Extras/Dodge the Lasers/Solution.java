import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
//import java.math.RoundingMode;

public class Solution {
    public static String solution(String s) {
        //precision must be at least 101, 
        //since s can be up to 101 digits. 
        final int PRECISION = 1000;
        final BigDecimal TWO = new BigDecimal("2");
        //represent sqrt(2) up to PRECISION number of decimal places.
        BigDecimal alpha = TWO.sqrt(new MathContext(PRECISION));
        //beatty sequence with r = sqrt(2).
        return beattySeq(s, alpha);
    }
    
    public static String beattySeq(String s, BigDecimal alpha) {
        BigInteger n = new BigInteger(s);
        if (n.equals(BigInteger.ZERO)) {return "0";}//base case.
        
        //n' = floor((alpha - 1) * n).
        //NOTE: no need for .setScale(0, RoundingMode.FLOOR);
        //as toBigInteger() discarded the fractional part.
        BigInteger nP = alpha.subtract(BigDecimal.ONE).multiply(new BigDecimal(n)).toBigInteger();
        
        //S(alpha, n) = nn' + (n(n + 1) / 2) - (n'(n' + 1) / 2) - S(alpha, n').
        //for 1 < alpha < 2. 
        BigInteger sumN = n.multiply(n.add(BigInteger.ONE)).divide(BigInteger.TWO);
        BigInteger sumNP = nP.multiply(nP.add(BigInteger.ONE)).divide(BigInteger.TWO);
        BigInteger result = n.multiply(nP).add(sumN).subtract(sumNP).subtract(new BigInteger(beattySeq(nP.toString(), alpha)));
        
        return result.toString();
    }
    
    public static void main(String args[]) {
        //String n = "5";//19.
        //String n = "77";//4208.
        //String n = "55109";//2147496038.
        //String n = "23223423";//381362049543566.
        String n = "86032128652";//5233670046269023958676. 
        
        System.out.println(solution(n));
    }
}