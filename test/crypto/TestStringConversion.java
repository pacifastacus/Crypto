package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Stack;

public class TestStringConversion {

	public static void main(String[] args) {
		LinkedList<byte[]> blocks = RSA.cuttingMessage("Árvíztűrő tükörfúrógép", 4);
		for (byte[] bytes : blocks) {
			BigInteger m = new BigInteger(bytes);
			System.out.print("message: [");
			for (byte b : bytes) {
				System.out.format("%X ",b);
			}
			System.out.println("]");
			System.out.println("length:"+m.bitLength()+" ("+m+")");
		}
	}

}
