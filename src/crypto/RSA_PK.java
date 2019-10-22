package crypto;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

class RSA_PK extends RSAKey implements RSAPublicKey {

	public RSA_PK(BigInteger mod, BigInteger exp) {
		super(mod, exp);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -156275816726906066L;

	@Override
	public BigInteger getPublicExponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
