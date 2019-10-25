/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Random;

/** RSA crypto system
 * Apply the RSA cryptosystem
 *
 * @author palkovics
 */
public class RSA implements IAssymetricCryptoSystem{
    
    private boolean millerRabin(BigInteger p){
        return p.isProbablePrime(10);
    }
    
	@Override
	public KeyPair keyGen(int size, int confidency) {
		BigInteger p,
				   q,
				   n,
				   e,
				   d,
				   phyN;
		//Choose p, q big primes and n=p*q
		//TODO p és q millerRabin teszttel kell meghatározni!
        do{
            p = new BigInteger(size, new Random());
        }while(Prime.isPrime(p,4));
        do {
        	q = new BigInteger(size, new Random());
        }while(Prime.isPrime(q,4)); 
        n = p.multiply(q);
        System.err.println("primes found!");
        
        //Calculate Euler's phy function phy(n)=(p-1)*(q-1)
        //Because of p and q odds, we can subtract 1 from them by flipping the last bit
        phyN = p.flipBit(0).multiply(q.flipBit(0));
        
        //Choose e: 3<e<phy(n) and gcd(e,phy(n))=1
        //Because gcd(e,phy(n)=1 and phy(n) an even number, e must be odd 
        do {
        	e = new BigInteger(size, new Random());
        	//if e is even subtract one from it (optimization)
        	if(!e.testBit(0)) {
        		e.flipBit(0);
        	}
        }while(Euclidean.euclid(e, phyN).equals(BigInteger.ONE));
        System.err.println("encryptor exponent found!");
        
        //calculate d
        BigInteger[] dxy = Euclidean.extEuclid(e, phyN);
        if(dxy[1].compareTo(BigInteger.ONE)<0) {
        	d = dxy[1].add(phyN);
        }else if(dxy[1].compareTo(phyN)>0){
        	d = dxy[1].subtract(phyN);
        }else {
        	d = dxy[1];
        }
        System.err.println("decryptor exponent calculated!");
        
        return new KeyPair(new RSA_PK(n,e), new RSA_SK(p,q,d));
	}

	@Override
	public byte[] encode(String message, PublicKey publicKey) throws InvalidKeyException{
		if(!publicKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'publicKey' is not an RSA key");
		}
		
		String[] messageblocks = cuttingMessage(message, publicKey.getModulus().bitCount());
		BigInteger m, c;
		byte[] code = new byte[publicKey.getModulus().bitCount()];
		for(int i = 0; i < messageblocks.length; i++) {
//			m = new BigInteger(messageblocks[i].getBytes(StandardCharsets.UTF_8));
			m = new BigInteger(messageblocks[i].getBytes());
			c = m.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
			
//			code = code.concat(new String(c.toByteArray(),StandardCharsets.UTF_8));
			code = code.concat(new String(c.toByteArray()));
		}
		return code;
	}
	
	@Override
	public String decode(String code, PrivateKey secretKey) {
		//TODO
		return null;
	}


	public static String[] cuttingMessage(String message, int size) {
		int numberOfBlocks = (int)Math.ceil((double)message.length()/size);
		String[] retVal = new String[numberOfBlocks];
		
		for(int i = 0; i < numberOfBlocks; i++) {
			try {
				retVal[i] = message.substring(size*i, size*(i+1));
			}
			catch (IndexOutOfBoundsException e) {
				String fill = "";
				while(fill.length() < size-1) {
					fill = fill.concat("*");
				}
				retVal[i] = message.substring(size*i).concat(fill);
			}
		}
		return retVal;
		
	}
}
