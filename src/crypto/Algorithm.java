package crypto;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public class Algorithm {
	
	/**
	 * Quadratic modular quick-exponentiation
	 * @param num - base
	 * @param exp - exponent
	 * @param mod - modulo
	 * @return n^e mod m
	 */
	static BigInteger quickPow(BigInteger num, BigInteger exp, BigInteger mod) {
		BigInteger result = BigInteger.ONE;
		BigInteger tmp = num.mod(mod);
		for(int i = 0; i<exp.bitLength();i++) {
			if(exp.testBit(i))
				result = result.multiply(tmp);
			tmp = tmp.multiply(tmp).mod(mod);
		}
		
		return result.mod(mod);
	}
	
	/**
	 * Quadratic modular quick-exponentiation
	 * @param num - base 
	 * @param exp - exponent
	 * @param mod - modulo
	 * @return n^e mod m
	 */
	static long quickPow(int num, int exp, int mod) {
		return quickPow(BigInteger.valueOf(num), 
				BigInteger.valueOf(exp), 
				BigInteger.valueOf(mod)).longValue();
	}
	
	/**
	 * Euclidean algorithm
	 * @param a
	 * @param b
	 * @return gcd(a,b)
	 */
	public static BigInteger euclid(BigInteger a, BigInteger b){
        BigInteger r;
        a = a.abs();
        b = b.abs();
        while(!b.equals(BigInteger.ZERO)){
            r = a.mod(b);
            a = b;
            b = r;
        }
                
        return a;
    }
    
	/**
	 * Extended euclidean algorithm
	 * Calulates gcd(a,b) and solves the aX + bY = gcd(a,b) equation for X and Y 
	 * @param a
	 * @param b
	 * @return Array of BigIntegers:{gcd(a,b),X,Y}
	 */
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b){
        BigInteger[] dxy = new BigInteger[3]; //gcd(a,b), X, Y
        BigInteger[] qnr = new BigInteger[2];
        BigInteger[] x = new BigInteger[2];
        BigInteger[] y = new BigInteger[2];
        BigInteger tmp;
        x[0] = new BigInteger("1");
        x[1] = new BigInteger("0");
        y[0] = new BigInteger("0");
        y[1] = new BigInteger("1");
        boolean counter = false;
        
        a = a.abs();
        b = b.abs();
        while(!b.equals(BigInteger.ZERO)){
            counter = !counter;    
            qnr = a.divideAndRemainder(b);
            a = b;
            b = qnr[1];
            tmp = new BigInteger(x[1].multiply(qnr[0]).add(x[0]).toString());
            x[0] = x[1];
            x[1] = tmp;
            tmp = new BigInteger(y[1].multiply(qnr[0]).add(y[0]).toString());
            y[0] = y[1];
            y[1] = tmp;
        }
        dxy[0] = a;
        dxy[1] = counter ? x[0].negate() : x[0];
        dxy[2] = counter ? y[0] : y[0].negate();
        return dxy;
    }
        
	/**
     * Test is num is a prime, using the Miller-Rabin test.
     * @param num is the tested number
     * @param trials is the number of tests; the certainity level 
     * @return true, if num is probably a prime
     */
	static boolean isPrime(BigInteger num, int trials) {
		BigInteger d = num.subtract(BigInteger.ONE);
		BigInteger a;
		BigInteger x;
		Random rnd = new Random();		
		int r = 0;
		
		//short-cut for little primes
		try{
			int p = num.intValueExact();
			if(p <= 1)
				return false;
			int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 
					37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 
					89, 97, 101};
			
			if(Arrays.binarySearch(primes, p)>=0)
				return true;
		}
		catch(ArithmeticException e) {
			;
		}
	
		//calculate r,d so num = 2^r * d + 1
		while(d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
        	d = d.divide(BigInteger.valueOf(2));
        	r++;
        }
		witness:
		for(int i = 0; i < trials; i++) {
			//pick a witness between [2,n-2]
			do {
				a = new BigInteger(8, rnd);
			}while(a.compareTo(num.subtract(BigInteger.ONE)) >= 0 ||
					a.compareTo(BigInteger.ONE) <= 0);
			x = quickPow(a, d, num);
			if( x.equals(BigInteger.ONE) || 
					x.equals(num.subtract(BigInteger.ONE)) || 
					x.equals(BigInteger.ONE.negate()) ) 
			{	//test first round failed
				continue witness;
			}
			for(int j = 0; j<r-1; j++) {
				x = quickPow(x, BigInteger.valueOf(2), num);
				if(x.equals(num.subtract(BigInteger.ONE)) || 
						x.equals(BigInteger.ONE.negate()) ) 
				{ //test Nth round failed
					continue witness;
				}
			}
			//All test in that trial passed (one trial passed)
			return false;
		}
        //All trials failed
		return true;
	}
	
	static BigInteger[] preCrt(BigInteger p, BigInteger q) {
		BigInteger[] c = new BigInteger[2];
		BigInteger[] eea = extEuclid(p, q);
		c[0] = p.multiply(eea[1]);
		c[1] = q.multiply(eea[2]);
		return c;
	}
	
	static BigInteger crt(BigInteger x, BigInteger e, BigInteger[] primes, BigInteger[] c) {
		BigInteger xp, xq;
		//xp = quickPow(x, e, primes[0]);
		xp = x.modPow(e.mod(primes[0].subtract(BigInteger.ONE)), primes[0]);
		//xq = quickPow(x, e, primes[1]);
		xq = x.modPow(e.mod(primes[1].subtract(BigInteger.ONE)), primes[1]);
		BigInteger retVal = xq.multiply(c[0]).add(xp.multiply(c[1]));

		return retVal.mod(primes[0].multiply(primes[1]));
	}
}
