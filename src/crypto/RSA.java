/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/** RSA crypto system
 * Apply the RSA cryptosystem
 *
 * @author palkovics
 */
public class RSA implements IAssymetricCryptoSystem{
    private int MOD_SIZE = 0;	//byte size of modulo ( n ) 
	@Override
	public KeyPair keyGen(int size, int confidency) {
		BigInteger p,
				   q,
				   n,
				   e,
				   d,
				   phyN;
		//Choose p, q big primes and n=p*q
        do{
            p = new BigInteger(size, new Random());
        }while(Algorithm.isPrime(p,confidency));
        do {
        	q = new BigInteger(size, new Random());
        }while(Algorithm.isPrime(q,confidency)); 
        n = p.multiply(q);
        MOD_SIZE = n.bitLength()/8;
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
        }while(Algorithm.euclid(e, phyN).equals(BigInteger.ONE));
        System.err.println("encryptor exponent found!");
        
        //calculate d
        BigInteger[] dxy = Algorithm.extEuclid(e, phyN);
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
	
	public int getModSize() {
		return MOD_SIZE;
	}
	@Override
	public BigInteger encode(BigInteger num, PublicKey publicKey) throws InvalidKeyException{
		if(!publicKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'publicKey' is not an RSA key");
		}
		//Casting publickey to RSA_PK
		RSA_PK PK = (RSA_PK)publicKey;
		return Algorithm.quickPow(num, PK.getPublicExponent(), PK.getModulus());
	}
	
	@Override
	public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException {
		if(!secretKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'publicKey' is not an RSA key");
		}
		RSA_SK SK = (RSA_SK)secretKey;
		return Algorithm.quickPow(code, SK.getPrivateExponent(), SK.getModulus());
	}

	@Override
	public byte[] crypt(String message, PublicKey publicKey) throws InvalidKeyException {
		LinkedList<byte[]> blocks = new LinkedList<byte[]>();
		byte[] bytesOfMessage = message.getBytes(StandardCharsets.UTF_8);
		
		//If the message is short enough we must encode it once
//		if(message.length()<MOD_SIZE-1) {
//			BigInteger m = new BigInteger(bytesOfMessage);
//			return encode(m,publicKey).toByteArray();
//		}
		
		//Cut message to blocks which value less than the size of modulo
		for(int i = 0; i<Math.ceil((double)message.length()/(MOD_SIZE-1));i++) {
			try {
				blocks.add(Arrays.copyOfRange(bytesOfMessage, i*(MOD_SIZE-1), (i+1)*(MOD_SIZE-1)));
			}catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("JAJJLACI");
			}
			
		}
		System.err.println("blocks number:"+blocks.size());
		byte[] code = new byte[blocks.toArray().length*MOD_SIZE+1];
		int i = 0;
		for (byte[] block : blocks) {
			BigInteger m = new BigInteger(block);
			block = encode(m, publicKey).toByteArray();
			
			for (byte b : block) {
				code[i++] = b;
			}
		}
		System.err.println("code lenght:"+code.length);
		return code;
	}

	@Override
	public String decrypt(byte[] code, PrivateKey secretKey) throws InvalidKeyException {
		LinkedList<byte[]> blocks = new LinkedList<byte[]>();
		System.err.println("code length:"+code.length);
		
		for(int i = 0; i<Math.ceil((double)code.length/MOD_SIZE-1); i++) {
			blocks.push(Arrays.copyOfRange(code, i*(MOD_SIZE-1), (i+1)*(MOD_SIZE-1)));
		}
		String message = ""; 
		for (byte[] block : blocks) {
			BigInteger c = new BigInteger(block);
			block = decode(c, secretKey).toByteArray();	
			message.concat(new String(block));
		}
		
		System.err.println("blocks number=" +blocks.size());
		return message;
	}
	
	public static LinkedList<byte[]> cuttingMessage(String message, int size){
//		int numberOfBlocks = (int)Math.ceil((double)message.getBytes().length/size);
//		byte[][] retVal = new byte[size][numberOfBlocks];
		LinkedList<byte[]> retVal = new LinkedList<byte[]>();
		byte[] m = message.getBytes();
		
		for(int i = 0; i<size; i++){
			try {
				retVal.push(Arrays.copyOfRange(m, i*size, (i+1)*size));
			}catch (ArrayIndexOutOfBoundsException e) {
				System.err.println("JAJJLACI");
			}
		}
		
		return retVal;
	}
}
