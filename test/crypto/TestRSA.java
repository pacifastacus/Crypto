package crypto;

import java.security.InvalidKeyException;
import java.security.KeyPair;

public class TestRSA {
	
	public static void main(String[] args) {
		RSA rsa = new RSA();
		KeyPair keys = rsa.keyGen(12, 10); 
//		String message = "Árvíztűrő tükörfúrógép. Ezt a mondatot a karakterkészelt tesztelésére használjuk. Így bizonyosodhatunk meg affelől, hogy a különleges magyar karakterek is helyesen megjelennek";
		String message = "Foobar zarathustra";
		String code;
		System.out.println("üzenet:\n"+message);
		try {
			code = rsa.encode(message, keys.getPublic());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPublic did not provided RSA key!");
			e.printStackTrace();
		} finally {
			//TODO change code to decodable cyphertext
			code = "foobar";
		}
		System.out.println("titkos üzenet:\n"+code);
		System.out.println("Visszafejtett üzenet:\n"+
				rsa.decode(code, keys.getPrivate()));
	}

}
