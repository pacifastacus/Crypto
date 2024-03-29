package crypto;

import java.math.BigInteger;
import java.util.Random;

/**
 * 
 * @author Palkovics Dénes
 *
 */
final class TestQuickPow {

	public static void main(String[] args) {
		Random rnd = new Random();
		int num = Math.abs(rnd.nextInt());
		int exp = Math.abs(rnd.nextInt());
		int mod = Math.abs(rnd.nextInt());
		System.out.println("num:\t" + num
				+ "\nexp:\t" + exp
				+ "\nmod:\t" + mod);
		System.out.println(Algorithm.
				quickPow(BigInteger.valueOf(num), BigInteger.valueOf(exp),BigInteger.valueOf(mod)));
		System.out.println(BigInteger.valueOf(num).
				modPow(BigInteger.valueOf(exp), BigInteger.valueOf(mod)));
	}

}
