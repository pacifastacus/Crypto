package crypto;

import java.math.BigInteger;
import java.util.Random;

final class Prime {
	/**
	 * 
	 * @param n
	 * @return false if n is maybe prime, true if n is surely a composite
	 */
	static boolean millerRabin(BigInteger n, long withness){
        BigInteger a = BigInteger.valueOf(withness);
        BigInteger d = n.subtract(BigInteger.ONE);
        long s = 0;
        long r = 1;
        //Bontsuk fel n-1-t a^s és d szorzataira
        while(d.mod(a).equals(BigInteger.ZERO)) {
        	d = d.divide(a);
        	s++;
        }
        //Első feltétel:  a^d = 1 mod n, akkor n prím
        if(a.modPow(d, n).equals(BigInteger.ONE))
        	return false;
        System.err.println("First test failed! - " +
        	a.modPow(d, n));
        //Második feltétel: van-e olyan r, hogy a^(2^r*d) = -1 mod n
        BigInteger exp = new BigInteger("2").multiply(d);
        BigInteger tmp;
        for(r = 1; r<s; r++){
        	tmp = a.modPow(exp, n);
        	if(tmp.equals(BigInteger.ONE.negate()) ||
        		tmp.equals(n.subtract(BigInteger.ONE))){
        		return false;
        	};
        	System.err.println("Second test failed! - " +
            		a.modPow(exp, n));
        	exp = exp.multiply(BigInteger.valueOf(2));
        }
        return true;
    }
        
	static boolean isPrime(BigInteger num, int trials) {
		boolean result;
		for(int i = 0; i < trials; i++) {
			long w = new Random().nextLong();
			result = millerRabin(num, w)
		}
		
		return true;
	}
}
