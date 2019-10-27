package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;

public class TestRSAOnNumbers {

	public static void main(String[] args) throws InvalidKeyException {
		RSA rsa = new RSA(64);
		KeyPair keys = rsa.keyGen(3);
		RSA_PK pk = (RSA_PK)keys.getPublic();
		RSA_SK sk = (RSA_SK)keys.getPrivate();
		
		
		BigInteger m = new BigInteger("Foobar".getBytes(StandardCharsets.UTF_8));
		//BigInteger m = new BigInteger("-100");
		BigInteger c;
		System.out.println("Nyílvános kulcs"+ pk);
		System.out.println("titkos kulcs"+ sk);
		System.out.println("Modulus mérete:" + rsa.getModSize());
		//Kódolás
		c = rsa.encode(m, keys.getPublic());
		System.out.println("m ="+m +" ->" + new String(m.toByteArray()) + " " + m.bitLength() +
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
