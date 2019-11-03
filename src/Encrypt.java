
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import crypto.*;
/**
 * @author Palkovics Dénes
 *
 */
public final class Encrypt {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RSA rsa = new RSA();
		
		//BufferedReader key = new BufferedReader(new FileReader(file));
		switch (args.length) {
		//Help
		case 0:
			System.out.println("Használat:");
			System.out.println("Encrypt <PK_fájlnév>");
			System.out.println("Encrypt <PK_fájlnév> <üzenet>");
			System.out.println("Encrypt <PK_fájlnév> <üzenetfájl>");
			return;
		//Read string from command line
		case 1:
			try(BufferedWriter secret = new BufferedWriter(new FileWriter("secret.bin"));
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
				System.out.println("üzenet:");
				rsa.encrypt(br.readLine(), publicKey)
				System.out.println("Üzenet titkosítva! (secret.bin)");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		//Read string from file
		case 2:
			
			break;
		default:
			System.out.println("Túl sok argumentum!");
			break;
		}

	}

}
