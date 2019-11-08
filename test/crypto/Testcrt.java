package crypto;

import java.math.BigInteger;

final class Testcrt {

	public static void main(String[] args) {
		BigInteger a = BigInteger.valueOf(42);
		BigInteger p = BigInteger.valueOf(11);
		BigInteger q = BigInteger.valueOf(13);
		BigInteger[] primes = {p, q};
		
		BigInteger[] c = Algorithm.preCrt(p, q);
		for (BigInteger e : c) {
			System.out.println(e);
		}
		System.out.println(Algorithm.crt(a, BigInteger.valueOf(103), primes, c));
	}

}
