package crypto;

import java.math.BigInteger;

class RSAKey extends Key{
	private final BigInteger modulus;
	private final BigInteger exponent;
	
	public RSAKey(BigInteger n, BigInteger k) {
		this.modulus = n;
		this.exponent = k; 
	}

	public BigInteger getModulus() {
		return modulus;
	}

	public BigInteger getExponent() {
		return exponent;
	}
	
	
}
