package crypto;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 
 * @author Palkovics Dénes
 *
 */
abstract class RSAKey implements Key, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3701611899032162123L;
	protected final BigInteger modulus;
	protected final BigInteger exponent;
	
	/**
	 * Generate a key so that it used in modular exponentioation like c = m^exp (mod)
	 * 
	 * @param mod - modulus of the key. It is a multiplication of two primes (n = p*q)
	 * @param exp - epxonent of the key (
	 */
	public RSAKey(BigInteger mod, BigInteger exp) {
		this.modulus = mod;
		this.exponent = exp; 
	}
	
	/**
	 * retrieve the modulus of the key
	 * @return modulus
	 */
	public BigInteger getModulus() {
		return modulus;
	}
	
	/**
	 * Retrieve the exponent of the key
	 * @return exponent
	 */
	public BigInteger getExponent() {
		return exponent;
	}
	

	@Override
	public String getAlgorithm() {
		return "RSA";
	}
	

	@Override
	public String toString() {
		return "[mod="+modulus+", exp="+exponent+"]";
	}
	
}
