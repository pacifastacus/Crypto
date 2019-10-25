package crypto;

import java.math.BigInteger;

public final class TestMillerRabin {

	public static void main(String[] args) {
		System.out.println(Prime.millerRabin(BigInteger.valueOf(561)));

	}

}
