package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public class TestRSA {
	
	public static void main(String[] args) {
		RSA rsa = new RSA(8*8);
		KeyPair keys = rsa.keyGen(10); 
		System.err.format("A modulo nagysága %d bájt\n",rsa.getModSize());
		String message = " Árvíztűrő tükörfúrógép. Ezt a mondatot a karakterkészelt tesztelésére használjuk. Így bizonyosodhatunk meg affelől, hogy a különleges magyar karakterek is helyesen megjelennek";
//		String message = "Foobar";
//		String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ";
		Queue<BigInteger> code;
		System.out.println("üzenet:"+message);
		byte[] bytesOfMessage = message.getBytes(StandardCharsets.UTF_8);
		for(byte b : bytesOfMessage) {
			System.out.print(b + " ");
		}
		System.out.println();
		try {
			code = rsa.crypt(message, keys.getPublic());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPublic did not provided RSA key!");
			e.printStackTrace();
			//TODO change code to decodable cyphertext
			code = new LinkedList<BigInteger>();
			code.add(BigInteger.valueOf(1337));
		}
		System.out.println("titkos üzenet:\n"+code);
		System.out.println();
		String decryptedMessage;
		try {
			decryptedMessage = rsa.decrypt(code, keys.getPrivate());
		} catch (InvalidKeyException e) {
			System.err.println("keys.getPrivate did not provided RSA key!");
			e.printStackTrace();
			decryptedMessage = "NONE";
		}
		
		
		System.out.println("Visszafejtett üzenet:"+decryptedMessage);
		bytesOfMessage = decryptedMessage.getBytes(StandardCharsets.UTF_8);
		for(byte b : bytesOfMessage) {
			System.out.print(b + " ");
		}
		System.out.println();
	}

}
