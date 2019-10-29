import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.Queue;

import crypto.*;
/**
 * 
 * @author Palkovics Dénes
 *
 */
public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int size = 64;
		System.out.format("RSA kulcsok mérete:");
		try {
			size = Integer.parseInt(br.readLine());
			
		}catch (NumberFormatException e) {
			System.err.println("Nem szám! "+ e);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		RSA rsa = new RSA();
		KeyPair keys = rsa.keyGen(3,size);
		
		while(true) {
			String file;
			System.out.format("adja meg az text fájlt:");
			if((file = br.readLine()) == null)
				return;
			
			BufferedReader fr = new BufferedReader(new FileReader(file));
			StringBuilder strb = new StringBuilder();
			String line;
			while((line = fr.readLine())!=null) {
				strb.append(line).append("\n");
			}
			
			Queue<BigInteger> code;
			System.out.println("\nüzenet-----------------------------------\n"
					+strb.toString());
			try {
				code = rsa.encrypt(strb.toString(), keys.getPublic());
			} catch (InvalidKeyException e) {
				System.err.println("keys.getPublic did not provided RSA key!");
				e.printStackTrace();
				return;
			}
			System.out.println("\ntitkos üzenet-----------------------------\n");
			for (BigInteger e : code) {
				System.out.println(e);
			}
			System.out.println();
			String decryptedMessage;
			try {
				decryptedMessage = rsa.decrypt(code, keys.getPrivate());
			} catch (InvalidKeyException e) {
				System.err.println("keys.getPrivate did not provided RSA key!");
				e.printStackTrace();
				decryptedMessage = "NONE";
			}
			
			
			System.out.println("\nVisszafejtett üzenet-----------------------\n"
					+decryptedMessage);
			System.out.print("\nAkar új fájlt titkosítani? ");
			if(br.readLine() != "igen")
				break;				
		}
	}
}
