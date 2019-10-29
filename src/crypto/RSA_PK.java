package crypto;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * RSA Public key
 * @author palkovics
 *
 */
final class RSA_PK extends RSAKey implements PublicKey, Serializable{


<<<<<<< HEAD
	/**
	 * 
	 */
	private static final long serialVersionUID = -1688305390324939707L;

	public RSA_PK(BigInteger mod, BigInteger exp) {
		super(mod, exp);
=======
	public RSA_PK(BigInteger mod, BigInteger e) {
		super(mod, e);
>>>>>>> refs/heads/master
	}
}
