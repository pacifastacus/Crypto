package crypto;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class RSA_SK extends RSAKey implements RSAPrivateKey {
	private final BigInteger p;
	private final BigInteger q;
	public RSA_SK(BigInteger p, BigInteger q, BigInteger k) {
		super(p.multiply(q), k);
		this.p = p;
		this.q = q;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 985194391416510895L;

	@Override
	public BigInteger getPrivateExponent() {
		return exponent;
	}
	public BigInteger[] getPrimes() {
		BigInteger[] retval = new BigInteger[2];
		retval[0] = p;
		retval[1] = q;
		return retval;
	}
}
