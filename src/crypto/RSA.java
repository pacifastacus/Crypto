/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author palkovics
 */
public class RSA implements ICryptoSystem{
    
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
        p = new BigInteger(size, confidency, new Random());
        q = new BigInteger(size, confidency, new Random());
        n = p.multiply(q);
        System.err.println("primes found!");
        
        //Calculate Euler's phy function phy(n)=(p-1)*(q-1)
//        phyN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        phyN = p.flipBit(0).multiply(q.flipBit(0));
        
        //Choose e: 3<e<phy(n) and gcd(e,phy(n))=1
        do {
        	e = new BigInteger(size, new Random());
        	//if e is even subtract one from it (optimization)
//        	if(!e.testBit(0)) {
//        		e.flipBit(0);
//        	}
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
        
        //Assemble the Keys
        return new RSAKeyPair(n, e, d);
	}

	@Override
	public String encode(String message, Key publicKey) {
		RSAKey castedKey = (RSAKey) publicKey;
		String[] messageblocks = cuttingMessage(message, castedKey.getModulus().bitCount());
		BigInteger m, c;
		String code = "";
		for(int i = 0; i < messageblocks.length; i++) {
//			m = new BigInteger(messageblocks[i].getBytes(StandardCharsets.UTF_8));
			m = new BigInteger(messageblocks[i].getBytes());
			c = m.modPow(castedKey.getExponent(), castedKey.getModulus());
			
//			code = code.concat(new String(c.toByteArray(),StandardCharsets.UTF_8));
			code = code.concat(new String(c.toByteArray()));
		}
		return code;
	}

	@Override
	public String decode(String code, Key secretKey) {
		return encode(code, secretKey);
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
