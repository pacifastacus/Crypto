package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class TestStringConversion {

	public static void main(String[] args) {
		String message = "Foobar őfelsége";
		byte[] t = message.getBytes(StandardCharsets.UTF_8);

		BigInteger m = new BigInteger(t);
		System.out.println(m);
		String s = new String(t, StandardCharsets.UTF_8);
		System.out.println(s);
	}

}
