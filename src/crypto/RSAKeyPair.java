/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;

/**
 * Private Key class for RSA
 * @author palkovics
 */
public class RSAKeyPair extends KeyPair{
//	private class PK extends RSAkey{
//		BigInteger n;
//		BigInteger e;
//		public PK(BigInteger n, BigInteger e) {
//			this.n = n;
//			this.e = e;
//		}
//	}
//	private class SK extends RSAkey{
//		BigInteger n;
//		BigInteger d;
//		public SK(BigInteger n, BigInteger d) {
//			this.n = n;
//			this.d = d;
//		}
//	}
    private final RSAKey pk;
    private final RSAKey sk;
    public RSAKeyPair(BigInteger n, BigInteger e, BigInteger d){
    	sk = new RSAKey(n,d);
    	pk = new RSAKey(n,e);
    }
	public RSAKey getPk() {
		return pk;
	}
	public RSAKey getSk() {
		return sk;
	}
    
    
}
