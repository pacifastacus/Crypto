package crypto;

import java.math.BigInteger;

/**
 * RSA Public key
 * @author palkovics
 *
 */
final class RSA_PK extends RSAKey implements PublicKey{


	public RSA_PK(BigInteger mod, BigInteger exp) {
		super(mod, exp);
	}
}
