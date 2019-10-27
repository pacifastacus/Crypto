package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.Queue;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public class Main {
	public static void main(String[] args) {
		RSA rsa = new RSA(Integer.parseInt(args[0]));
		KeyPair keys = rsa.keyGen(3); 
		String message = args[1];
		Queue<BigInteger> code;
		System.out.println("\nüzenet:\n"+message);
		try {
			code = rsa.crypt(message, keys.getPublic());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPublic did not provided RSA key!");
			e.printStackTrace();
			return;
		}
		System.out.println("\ntitkos üzenet:\n"+code);
		String decryptedMessage;
		try {
			decryptedMessage = rsa.decrypt(code, keys.getPrivate());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPrivate did not provided RSA key!");
			e.printStackTrace();
			decryptedMessage = "NONE";
		}
		
		
		System.out.println("\nVisszafejtett üzenet:\n"+decryptedMessage);
	}
}
