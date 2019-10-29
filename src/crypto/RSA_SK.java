package crypto;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * RSA Private key
 * @author Palkovics Dénes
 *
 */
public final class RSA_SK extends RSAKey implements PrivateKey, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4202698738488598476L;
	/**
	 * p and q prime numbers which the two common divisor of the modulus
	 */
	private final BigInteger[] primes;	// primes=[p, q]
	/**
	 * two multipliers used for accelerate the modular exponentiation in decoding
	 */
	private final BigInteger[] c;	// c=[cp, cq]
	
	/**
	 * 
	 * @param p prime
	 * @param q prime
	 * @param d exponent
	 */
	public RSA_SK(BigInteger p, BigInteger q, BigInteger d) {
		super(p.multiply(q), d);
		this.primes = new BigInteger[2];
		primes[0] = p;
		primes[1] = q;
		this.c = Algorithm.preCrt(p, q);
	}

	/**
	 * Return an array that contains p and q primes
	 * @return p and q
	 */
	public BigInteger[] getPrimes() {
		return primes;
	}
	
	/**
	 * Return the multipliers that helps accelerate the decoding process.
	 * They are used by <code>Algorithm.crt</code> 
	 * @return C_p and C_q
	 */
	public BigInteger[] getCrtMultipliers(){
		return c;
	}
}
