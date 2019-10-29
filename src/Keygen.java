import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import crypto.*;

/**
 * @author Palkovics Dénes
 *
 */
public final class Keygen {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		RSA rsa = new RSA();
		KeyPair keys;
		try(
				ObjectOutputStream out_sk = new ObjectOutputStream(new FileOutputStream("RSA_SK.key"));
				ObjectOutputStream out_pk = new ObjectOutputStream(new FileOutputStream("RSA_PK.key"))
			) {
			keys = rsa.keyGen(3, size);
			
			out_pk.writeObject(keys.getPublic());
			out_sk.writeObject(keys.getPrivate());
		} catch (FileNotFoundException e) {
			System.err.println("HIBA: nem nyitható meg írásra a kulcsfájlok valamelyike! " + e);			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
