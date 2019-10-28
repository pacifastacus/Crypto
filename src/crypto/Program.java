package crypto;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.Queue;

/**
 * A basic text encryptor application
 * @author palkovics
 *
 */
public final class Program {
	private static final String CMD_GEN = "generate";
	private static final String CMD_ENC = "encrypt";
	private static final String CMD_DEC = "decrypt";
	private static final String CMD_LOAD= "load";
	private static final String CMD_QUIT = "quit";
	private RSA rsa;
	private RSAKey key;
	private Console console;
	
	public Program() {
		this.console = System.console();
		this.rsa = new RSA();
	}
	
	private void printGreeting() {
		System.out.println("RSA rendszerű üzenet titkosító.\n "
				+ "Az alkalmazaás használható titkosításra és visszafejtésre egyaránt.\n"
				+ "A kulcsokat a program a 'PK.key' és 'SK.key' fájlokban tárolja\n"
				+ "Készítette: Palkovics Dénes\n\n");
	}
	
	/**
	 * Generate a key pair and stores them in the <code>RSA_SK.key</code> and the <code>RSA_PK.key</code>
	 * files.
	 */
	private void generateKeys(){
		KeyPair keys;
		int size = 128;
		int confidency = 3;
		System.out.println("Kulcsgenerálás:");
		try(
			ObjectOutputStream out_sk = new ObjectOutputStream(new FileOutputStream("RSA_SK.key"));
			ObjectOutputStream out_pk = new ObjectOutputStream(new FileOutputStream("RSA_PK.key"))
		) {
			try {
				int read = Integer.parseInt(
						console.readLine("Adja meg a generáladnó kulcsok méretét bitben (%d):", size));
				if(read >= 0) size=read;
			} catch (NumberFormatException e) {	}
			System.out.println("size=" + size);
			keys = rsa.keyGen(confidency, size);
			
			out_pk.writeObject(keys.getPublic());
			out_sk.writeObject(keys.getPrivate());
		} catch (FileNotFoundException e) {
			System.err.println("HIBA: nem nyitható meg írásra a kulcsfájlok valamelyike!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("HIBA: kimeneti hiba!");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param filename
	 * @param rsakey
	 */
	private void loadKey(String filename){
		try (ObjectInputStream in_key = 
				new ObjectInputStream(new FileInputStream(filename))){
			key = (RSAKey)in_key.readObject();
		} catch (FileNotFoundException e) {
			System.err.println("HIBA: A fájl nem található!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("HIBA: A fájl nem tartalmaz kulcsot!");
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.err.println("HIBA: A fájlban nem RSA kulcs van!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("HIBA: bemeneti hiba!");
			e.printStackTrace();
		}
	}
	
	private void encMode() {
		EncLoop:
		while(true) {
			System.out.println("-------Titkosító üzemmód-------\n\n"
					+ CMD_LOAD+ " - kulcs betöltése fájlból\n"
					+ CMD_ENC + " - Szöveges fájl titkosítása\n"
					+ CMD_QUIT +" - visszalépés\n");
			switch(console.readLine()) {
			case CMD_LOAD:
				String keyfname = "RSA_PK.key"; 
				String read = console.readLine("Adja meg a publikus kulcs fájlnevét (%s):",keyfname);
				if(!read.isBlank()) keyfname = read;
				loadKey(keyfname);
				break;
			case CMD_ENC:
				//Ellenőrizzük, hogy publikus rsa kulcs lett-e beolvasva;
				System.out.println("kulcs typusa:"+key.getClass());
				
				String textfname = console.readLine("Adja meg a titkosítandó szöveges fájlt");
				try(BufferedReader infile =
						new BufferedReader(new FileReader(textfname));
					ObjectOutputStream outfile = 
						new ObjectOutputStream(new FileOutputStream(textfname + ".crypt"))) {
					outfile.writeObject(rsa.encrypt(infile.readLine(), (PublicKey)key));
				} catch (FileNotFoundException e) {
					System.err.println("HIBA: a fájl nem található!");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					System.err.println("HIBA: A betöltött kulcs nem rsa kulcs");
					e.printStackTrace();
				}
				System.out.println(textfname + " fájl titkosítva!");
				break;
			case CMD_QUIT:	break EncLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue EncLoop;
			}
		}
		
		
		
	}
	/**
	 * Runs the main application
	 * @param args - the commandline arguments not yet implemented
	 */
	public static void main(String[] args) {
		Program prg = new Program();
		prg.printGreeting();
		MainLoop:
		while(true) {
			System.out.println("A továbblépéshez gépelje be az alábbiak valamelyikét:\n\n"
					+ CMD_GEN +" - Új kulcspár generálása. Létrehoz egy 'PK.key' és 'SK.key' kulcspárt\n"
					+ CMD_ENC +" - titkosító üzemmód.\n"
					+ CMD_DEC +" - visszafejtő üzemmód\n"
					+ CMD_QUIT+" - kilépés a programból\n");
			switch (prg.console.readLine()) {
			case CMD_GEN:	prg.generateKeys(); break; 
			case CMD_ENC:	prg.encMode(); break;
			case CMD_DEC:	break;
			case CMD_QUIT:	break MainLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue MainLoop;
			}
		}
		
	}

}
