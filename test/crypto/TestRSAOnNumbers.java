package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public class TestRSAOnNumbers {

	public static void main(String[] args) throws InvalidKeyException {
		RSA rsa = new RSA();
		KeyPair keys = rsa.keyGen(3,3*8);
		RSA_PK pk = (RSA_PK)keys.getPublic();
		RSA_SK sk = (RSA_SK)keys.getPrivate();
		
		
		BigInteger m = new BigInteger("Foobar".getBytes(StandardCharsets.UTF_8));
		//BigInteger m = new BigInteger("32");
		BigInteger c;
		System.out.println("Nyílvános kulcs"+ pk);
		System.out.println("titkos kulcs"+ sk);
		System.out.println("Prímek:");
		System.out.println("p="+sk.getPrimes()[0]);
		System.out.println("q="+sk.getPrimes()[1]);
		System.out.println("Modulus mérete:" + pk.getModulus().bitLength());
		//Kódolás
		c = rsa.encode(m, keys.getPublic());
		System.out.println("m ="+m +" ->" + new String(m.toByteArray()) + " " + m.bitLength()/8 +
				"\nc ="+c +" ->" + " " + c.bitLength()/8);
		
		//Dekódolás
		m = rsa.decode(c, keys.getPrivate());
		System.out.println("After decoding");
		System.out.println("m ="+m +" ->" + new String(m.toByteArray()) + " " + m.bitLength()/8);
		
		//Új kulcsok készítése
//		keys = rsa.reKeyGen(keys);
//		pk = (RSA_PK)keys.getPublic();     
//		sk = (RSA_SK)keys.getPrivate();    
//		System.out.println("Nyílvános kulcs"+ pbytesk);
//		System.out.println("titkos kulcs"+ sk);
//		c = rsa.encode(m, keys.getPublic());
//		System.out.println("m ="+m +
//				"\nc ="+c);
//		m = rsa.decode(c, keys.getPrivate());
//		System.out.println("After decoding");
//		System.out.println("m ="+m );
	}

}
