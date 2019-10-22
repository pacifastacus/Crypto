package crypto;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class RSA_SK extends RSAKey implements RSAPrivateKey {

	public RSA_SK(BigInteger n, BigInteger k) {
		super(n, k);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 985194391416510895L;

	@Override
	public BigInteger getPrivateExponent() {
		return exponent;
	}

}
