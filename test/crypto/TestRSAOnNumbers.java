package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.util.Random;

public class TestRSAOnNumbers {

	public static void main(String[] args) throws InvalidKeyException {
		RSA rsa = new RSA(32);
		KeyPair keys = rsa.keyGen(3);
		RSA_PK pk = (RSA_PK)keys.getPublic();
		RSA_SK sk = (RSA_SK)keys.getPrivate();
		
		
		BigInteger m = new BigInteger(8,new Random());
		BigInteger c;
		System.out.println("Nyílvános kulcs"+ pk);
		System.out.println("titkos kulcs"+ sk);
		//Kódolás
		c = rsa.encode(m, keys.getPublic());
		System.out.println("m ="+m +
				"\nc ="+c);
		
		//Dekódolás
		m = rsa.decode(c, keys.getPrivate());
		System.out.println("After decoding");
		System.out.println("m ="+m );
		
		//Új kulcsok készítése
		keys = rsa.reKeyGen(keys);
		pk = (RSA_PK)keys.getPublic();     
		sk = (RSA_SK)keys.getPrivate();    
		System.out.println("Nyílvános kulcs"+ pk);
		System.out.println("titkos kulcs"+ sk);
		c = rsa.encode(m, keys.getPublic());
		System.out.println("m ="+m +
				"\nc ="+c);
		m = rsa.decode(c, keys.getPrivate());
		System.out.println("After decoding");
		System.out.println("m ="+m );
	}

}
