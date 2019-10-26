package crypto;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

class RSAKey implements java.security.Key, java.security.interfaces.RSAKey{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7256926749067436228L;
	protected final BigInteger modulus;
	protected final BigInteger exponent;
	
	public RSAKey(BigInteger mod, BigInteger exp) {
		this.modulus = mod;
		this.exponent = exp; 
	}

	public BigInteger getModulus() {
		return modulus;
	}

	@Override
	public String getAlgorithm() {
		return "RSA";
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public AlgorithmParameterSpec  getParams() {
		return null;
	}

	public String toString() {
		return "[mod="+modulus+", exp="+exponent+"]";
	}
	
}
