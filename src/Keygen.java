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
		if(args.length == 0) {
			System.out.println("Használat:");
			System.out.println("Encrypt <kulcsméret>");
			System.out.println("Encrypt <kulcsméret> <SK fájlnév> <PK fájlnév>");
			return;
		}
		int size = Integer.parseInt(args[0]);
		RSA rsa = new RSA();
		KeyPair keys;
		final String ext = "key";
		String PK_filename = "RSA_PK";
		String SK_filename = "RSA_SK";
		if(args.length == 3) {
			SK_filename = args[1] + "." + ext;
			SK_filename = args[2] + "." + ext;
		}
		try(
				ObjectOutputStream out_sk = new ObjectOutputStream(new FileOutputStream(SK_filename+"."+ext));
				ObjectOutputStream out_pk = new ObjectOutputStream(new FileOutputStream(PK_filename+"."+ext))
			) {
			keys = rsa.keyGen(3, size);
			
			out_pk.writeObject(keys.getPublic());
			out_sk.writeObject(keys.getPrivate());
		} catch (FileNotFoundException e) {
			System.err.println("HIBA: nem nyitható meg írásra a kulcsfájlok valamelyike! " + e);			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(SK_filename + "." + ext + " és " + 
				PK_filename + "." + ext + " " + size + " bites RSA kulcsok létrehozva!");
	}

}
