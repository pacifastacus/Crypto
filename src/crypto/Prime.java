package crypto;

import java.math.BigInteger;
import java.util.Random;

final class Prime {
	/**
	 * The Miller-Rabin compositeness test
	 * @param n is the tested number
	 * @param witness is the witness for the compositeness of n (can't be higher than n)
	 * @return true if n is surely a composite, else n is MAYBE prime
	 */
	static boolean millerRabin(BigInteger n, int witness){
        BigInteger a = BigInteger.valueOf(witness);
        BigInteger d = n.subtract(BigInteger.ONE);
        long s = 0;
        long r = 1;
        
        //make sure w is positive and odd
        witness = Math.abs(witness);
        if(witness % 2 == 0)
			witness++;
        
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
    /**
     * Test is num is a prime, using the Miller-Rabin test.
     * The certainity for num is a prime number is 1/4^trials.
     * @param num is the tested number
     * @param trials the certainity level 
     * @return true, if num is probably a prime
     */
	static boolean isPrime(BigInteger num, int trials) {
		boolean result;
		for(int i = 0; i < trials; i++) {
			int w = Math.abs(new Random().nextInt(num.intValue()));
			System.err.println("witness is: "+w);
			result = millerRabin(num, w);
			if(result)
				return false;
		}
		
		return true;
	}
}
