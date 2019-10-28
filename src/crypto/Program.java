package crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
	private static final String CMD_QUIT= "quit";
	private static final String CMD_BACK= "back";
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
			System.err.println("HIBA: nem nyitható meg írásra a kulcsfájlok valamelyike! " + e);
		} catch (IOException e) {
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
			System.err.println("HIBA: A fájl nem található! " + e);
		} catch (ClassNotFoundException e) {
			System.err.println("HIBA: A fájl nem tartalmaz kulcsot! " + e);
		} catch (ClassCastException e) {
			System.err.println("HIBA: A fájlban nem RSA kulcs van! " + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private void encMode() {
		System.out.println("-------Titkosító üzemmód-------\n");
		String cmd;
		EncLoop:
		while(true) {
			System.out.println("\n-------------------\n"
					+ CMD_LOAD+ " - kulcs betöltése fájlból\n"
					+ CMD_ENC + " - Szöveges fájl titkosítása\n"
					+ CMD_BACK+" - visszalépés\n");
			cmd = console.readLine(">");
			if(cmd == null) {
				return;
			}
			switch(cmd) {
			case CMD_LOAD:
				String keyfname = "RSA_PK.key"; 
				String read = console.readLine("Adja meg a publikus kulcs fájlnevét (%s):",keyfname);
				if(!read.isBlank()) keyfname = read;
				loadKey(keyfname);
				break;
			case CMD_ENC:
				//Ellenőrizzük, hogy van-e kulcs betöltve és az publikus rsa kulcs-e
				if(key == null) {
					System.out.println("Nincs nyílvános kulcs betöltve");
					break;
				}
				if(!(key instanceof RSA_PK)) {
					System.out.println("A betöltött kulcs nem RSA nyílvános kulcs");
					break;
				}
				
				String textfname = console.readLine("Adja meg a titkosítandó szöveges fájlt:");
				try(BufferedReader infile =
						new BufferedReader(new FileReader(textfname));
					ObjectOutputStream outfile = 
						new ObjectOutputStream(new FileOutputStream(textfname.split("\\.")[0] + ".crypt"))) {
					StringBuffer strBuff = new StringBuffer();
					String line = null;
					while((line =infile.readLine())!=null){
						strBuff.append(line).append("\n");
					}
					outfile.writeObject(rsa.encrypt(strBuff.toString(), (PublicKey)key));
					System.out.println(textfname + " fájl titkosítva!\n");
				} catch (FileNotFoundException e) {
					System.err.println("HIBA: a fájl nem található!" + e);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					System.err.println("HIBA: A betöltött kulcs nem rsa kulcs "+ e);
				}
				break;
			case CMD_BACK:	break EncLoop;
			case CMD_QUIT: break EncLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue EncLoop;
			}
		}
	}
		
	private void decMode() {
		System.out.println("-------Visszafejtő üzemmód-------\n");
		DecLoop:
		while(true) {
			System.out.println("\n-------------------\n"
					+ CMD_LOAD+ " - kulcs betöltése fájlból\n"
					+ CMD_DEC + " - titkosított fájl visszafejtése\n"
					+ CMD_BACK +" - visszalépés\n");
			switch(console.readLine()) {
			case CMD_LOAD:
				String keyfname = "RSA_SK.key"; 
				String read = console.readLine("Adja meg a titkos kulcs fájlnevét (%s):",keyfname);
				if(!read.isBlank()) keyfname = read;
				loadKey(keyfname);
				break;
			case CMD_DEC:
				//Ellenőrizzük, hogy van-e kulcs betöltve és az publikus rsa kulcs-e
				if(key == null) {
					System.out.println("Nincs nyílvános kulcs betöltve");
					break;
				}
				if(!(key instanceof RSA_SK)) {
					System.out.println("A betöltött kulcs nem RSA titkos kulcs");
					break;
				}
				
				String codefname = console.readLine("Adja meg a titkosítandó szöveges fájlt:");
				try(BufferedWriter outfile =
						new BufferedWriter(new FileWriter(codefname.split("\\.")[0] + ".txt"));
					ObjectInputStream infile = 
						new ObjectInputStream(new FileInputStream(codefname))) {
					
					Object readObject = infile.readObject();
					if(!(readObject instanceof Queue<?>))
						throw new ClassNotFoundException("readObject is not a Queue");
					outfile.write(rsa.decrypt((Queue<BigInteger>)readObject, (PrivateKey)key));
					System.out.println(codefname + " fájl visszafejtve!\n");
				} catch (FileNotFoundException e) {
					System.err.println("HIBA: a fájl nem található! "+e);
				} catch (InvalidKeyException e) {
					System.err.println("HIBA: A betöltött kulcs nem rsa kulcs! "+e);
				} catch (ClassNotFoundException e) {
					System.err.println("HIBA: A beolvasott fájl nem szabványos! "+e);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case CMD_BACK:	break DecLoop;
			case CMD_QUIT: break DecLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue DecLoop;
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
		String cmd;
		MainLoop:
		while(true) {
			System.out.println("A továbblépéshez gépelje be az alábbiak valamelyikét:\n\n"
					+ CMD_GEN +" - Új kulcspár generálása. Létrehoz egy 'PK.key' és 'SK.key' kulcspárt\n"
					+ CMD_ENC +" - titkosító üzemmód.\n"
					+ CMD_DEC +" - visszafejtő üzemmód\n"
					+ CMD_QUIT+" - kilépés a programból\n");
			
			cmd = prg.console.readLine(">");
			if(cmd == null) {
				return;
			}
			switch (cmd) {
			case CMD_GEN:	prg.generateKeys(); break; 
			case CMD_ENC:	prg.encMode(); break;
			case CMD_DEC:	prg.decMode(); break;
			case CMD_QUIT:	break MainLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue MainLoop;
			}
		}
		
	}

}
