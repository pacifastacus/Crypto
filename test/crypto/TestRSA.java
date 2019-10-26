package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;

public class TestRSA {
	
	public static void main(String[] args) {
		RSA rsa = new RSA();
		KeyPair keys = rsa.keyGen(32, 10); 
		System.err.format("A modulo nagysága %d bájt\n",rsa.getModSize());
//		String message = "Árvíztűrő tükörfúrógép. Ezt a mondatot a karakterkészelt tesztelésére használjuk. Így bizonyosodhatunk meg affelől, hogy a különleges magyar karakterek is helyesen megjelennek";
		String message = "Foobar zarathustra";
		byte[] code;
		System.out.println("üzenet:\n"+message);
		try {
			code = rsa.crypt(message, keys.getPublic());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPublic did not provided RSA key!");
			e.printStackTrace();
			//TODO change code to decodable cyphertext
			code = "foobar".getBytes();
		}
		System.out.println("titkos üzenet:\n"+code);
		String decryptedMessage;
		try {
			decryptedMessage = rsa.decrypt(code, keys.getPrivate());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPrivate did not provided RSA key!");
			e.printStackTrace();
			decryptedMessage = "NONE";
		}
		
		
		System.out.println("Visszafejtett üzenet:\n"+
				decryptedMessage);
	}

}
