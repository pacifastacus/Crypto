package crypto;

import java.math.BigInteger;
import java.util.Random;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public final class TestMillerRabin {

	public static void main(String[] args) {
		//System.out.println(Prime.millerRabin(BigInteger.valueOf(12738),7));
		BigInteger num = new BigInteger(128, new Random());
		BigInteger a = new BigInteger("100");
		num.setBit(0);
		System.out.println(a);
		a = a.setBit(0).setBit(3);;
		System.out.println(a);
		System.out.println("test if "+num+" is prime");
		System.out.println(Algorithm.isPrime(num, 4));
		System.out.println(num.isProbablePrime(4));
	}

}
