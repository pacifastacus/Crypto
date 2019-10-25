package crypto;

import java.math.BigInteger;

final class Modular {
	static BigInteger quickPow(BigInteger num, BigInteger exp, BigInteger mod) {
		BigInteger result = BigInteger.ONE;
		BigInteger tmp = num.mod(mod);
		for(int i = 0; i<exp.bitLength();i++) {
			if(exp.testBit(i))
				result = result.multiply(tmp);
			tmp = tmp.multiply(tmp).mod(mod);
		}
		
		return result.mod(mod);
	}
	static long quickPow(int num, int exp, int mod) {
		return quickPow(BigInteger.valueOf(num), 
				BigInteger.valueOf(exp), 
				BigInteger.valueOf(mod)).longValue();
	}
}
