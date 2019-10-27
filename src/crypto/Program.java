package crypto;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * A basic text encryptor application
 * @author palkovics
 *
 */
public final class Program {
	private static final String CMD_GEN = "generate";
	private static final String CMD_ENC = "encrypt";
	private static final String CMD_DEC = "decrypt";
	private static final String CMD_QUIT = "quit";
	private RSA rsa;
	
	private Console console;
	
	public Program() {
		this.console = System.console();
		this.rsa = new RSA(128);
	}
	public void printGreeting() {
		System.out.println("RSA rendszerű üzenet titkosító.\n "
				+ "Az alkalmazaás használható titkosításra és visszafejtésre egyaránt.\n"
				+ "A kulcsokat a program a 'PK.key' és 'SK.key' fájlokban tárolja\n"
				+ "Készítette: Palkovics Dénes\n\n");
	}
	public void encrypt() {
		InputStreamReader reader;
		char[] cbuf = new char[(int) (0.9*rsa.getModSize())];
		console.format("Írd be a titkosítandó üzenet elérési útvonalát:");
		File file = new File(console.readLine());
		BufferedReader in = new BufferedReader(new FileReader(file));
		while (in.read(cbuf) >= 0) {
			
			rsa.encode(new BigInteger(Arrays.copy))			
		}
		
		rsa.crypt(message, publicKey);
	}
	
	/**
	 * Runs the main application
	 * @param args - the commandline arguments not yet implemented
	 */
	public static void main(String[] args) {
		Program prg = new Program();
		char cmd;
		
		
		prg.printGreeting();
		MainLoop:
		while(true) {
			System.out.println("A továbblépéshez gépelje be az alábbiak valamelyikét:\n\n"
					+ CMD_GEN +" - Új kulcspár generálása. Létrehoz egy 'PK.key' és 'SK.key' kulcspárt\n"
					+ CMD_ENC +" - titkosító üzemmód.\n"
					+ CMD_DEC +" - visszafejtő üzemmód\n"
					+ CMD_QUIT+" - kilépés a programból\n");
			switch (prg.console.readLine()) {
			case CMD_GEN:	break; 
			case CMD_ENC:	break;
			case CMD_DEC:	break;
			case CMD_QUIT:	break MainLoop;
			default: System.out.println("Ismeretlen utasítás!"); continue MainLoop;
			}
		}
		
	}

}
