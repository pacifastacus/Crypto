package crypto;

import java.math.BigInteger;
import java.util.Random;

final class TestQuickPow {

	public static void main(String[] args) {
		Random rnd = new Random();
		int num = Math.abs(rnd.nextInt());
		int exp = Math.abs(rnd.nextInt());
		int mod = Math.abs(rnd.nextInt());
		System.out.println("num:\t" + num
				+ "\nexp:\t" + exp
				+ "\nmod:\t" + mod);
		System.out.println(Modular.quickPow(num, exp, mod));
		System.out.println(new BigInteger(Integer.toString(num)).
				modPow(BigInteger.valueOf(exp),BigInteger.valueOf(mod)));
	}

}
