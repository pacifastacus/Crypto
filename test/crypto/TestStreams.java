package crypto;


import java.math.BigInteger;
import java.util.Random;

public final class TestStreams {
	public static void main(String[] args) {
		BigInteger num = new BigInteger(1024, new Random());

		System.out.println(num);
		System.out.println("bits: " + num.bitCount());
		System.out.println("length: " + num.bitLength());
		byte[] bytes = num.toByteArray();
		System.out.println(bytes.length);
		System.out.print("[");
		for (byte b : bytes) {
			System.out.format("%X ",b);
		}
		System.out.println("]");
	}
}
