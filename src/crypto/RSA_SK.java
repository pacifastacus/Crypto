package crypto;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 
 * @author Palkovics Dénes
 *
 */
final class RSA_SK extends RSAKey implements PrivateKey, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4202698738488598476L;
	private final BigInteger p;
	private final BigInteger q;
	
	public RSA_SK(BigInteger p, BigInteger q, BigInteger k) {
		super(p.multiply(q), k);
		this.p = p;
		this.q = q;
	}

	public BigInteger[] getPrimes() {
		BigInteger[] retval = new BigInteger[2];
		retval[0] = p;
		retval[1] = q;
		return retval;
	}
}
