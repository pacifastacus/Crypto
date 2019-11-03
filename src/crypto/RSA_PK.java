package crypto;

import java.math.BigInteger;

/**
 * RSA Public key
 * @author palkovics
 *
 */
public final class RSA_PK extends RSAKey implements PublicKey{


	public RSA_PK(BigInteger mod, BigInteger e) {
		super(mod, e);
	}
}
