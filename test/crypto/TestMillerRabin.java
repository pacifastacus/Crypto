package crypto;

import java.math.BigInteger;
import java.util.Random;

public final class TestMillerRabin {

	public static void main(String[] args) {
		//System.out.println(Prime.millerRabin(BigInteger.valueOf(12738),7));
		BigInteger num = new BigInteger(10, new Random());
		System.out.println("test if "+num+" is prime");
		System.out.println(Prime.isPrime(num, 4));
	}

}
