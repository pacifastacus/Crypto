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
 * Implements the RSA cryptosystem
 *
 * @author palkovics
 */
public class RSA implements IAssymetricCryptoSystem{
    private final int SIZE;	//size of primes in bits
    private int MOD_SIZE = 0;	//byte size of modulo ( n )
    
    public RSA(int size) {
    	this.SIZE = size;
    }
    /**
     * Generate a prime number, which primarilty tested with Algorithm.isPrime()
     * @param confidency
     * @return a propably prime number
     */
	private BigInteger prime(int confidency) {
    	BigInteger p;
    	System.err.print("Searching prime");
    	do{
            p = new BigInteger(SIZE, new Random());
            p = p.setBit(0).setBit(SIZE);
            System.err.print(".");
        }while(!Algorithm.isPrime(p,confidency));
    	System.err.println("DONE");
    	return p;
	}
	@Override
	public KeyPair keyGen(int confidency) {
		BigInteger p,
				   q,
				   n,
				   e,
				   d,
				   phyN;
		//Choose p, q big primes and n=p*q
        p = prime(confidency);
        q = prime(confidency);
        n = p.multiply(q);
        MOD_SIZE = n.bitLength()/8;
        System.err.println("primes found!");
        //Calculate Euler's phy function phy(n)=(p-1)*(q-1)
        //Because of p and q odds, we can subtract 1 from them by resetting the last bit
        phyN = p.clearBit(0).multiply(q.clearBit(0));
        
        //Choose e: 3<e<phy(n) and gcd(e,phy(n))=1
        //Because gcd(e,phy(n)=1 and phy(n) an even number, e must be odd 
        do {
        	e = new BigInteger(SIZE, new Random());
        	//if e is even subtract one from it (optimization)
        	e.clearBit(0);
        }while(!Algorithm.euclid(e, phyN).equals(BigInteger.ONE));
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
	
	/**
	 * Key generation resuing p and q primes which contained by sk. This means that only the exponent of the key changed
	 * @param keys private key
	 * @return a new keypair
	 */
	public KeyPair reKeyGen(KeyPair keys) {
		RSA_PK pk = (RSA_PK)keys.getPublic();
		RSA_SK sk = (RSA_SK)keys.getPrivate();
		BigInteger[] primes = sk.getPrimes();
		BigInteger e,
				   d;
		//Calculate Euler's phy function phy(n)=(p-1)*(q-1)
        //Because of p and q odds, we can subtract 1 from them by resetting the last bit
		BigInteger phy = primes[0].clearBit(0).multiply(primes[1].clearBit(0));
		
		//Choose e: 3<e<phy(n) and gcd(e,phy(n))=1
        //Because gcd(e,phy(n)=1 and phy(n) an even number, e must be odd 
        do {
        	e = new BigInteger(SIZE, new Random());
        	//if e is even subtract one from it (optimization)
        	e.clearBit(0);
        }while(!Algorithm.euclid(e, phy).equals(BigInteger.ONE) || e.equals(pk.getPublicExponent()));
        System.err.println("encryptor exponent found!");
        
        //calculate d
        BigInteger[] dxy = Algorithm.extEuclid(e, phy);
        if(dxy[1].compareTo(BigInteger.ONE)<0) {
        	d = dxy[1].add(phy);
        }else if(dxy[1].compareTo(phy)>0){
        	d = dxy[1].subtract(phy);
        }else {
        	d = dxy[1];
        }
        System.err.println("decryptor exponent calculated!");
        
        return new KeyPair(new RSA_PK(primes[0].multiply(primes[1]),e), 
        		new RSA_SK(primes[0],primes[1],d));
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
		//return num.modPow(PK.getPublicExponent(), PK.getModulus());
	}
	
	@Override
	public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException {
		if(!secretKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'secretKey' is not an RSA key");
		}
		RSA_SK SK = (RSA_SK)secretKey;
		return Algorithm.quickPow(code, SK.getPrivateExponent(), SK.getModulus());
		//return code.modPow(SK.getPrivateExponent(), SK.getModulus());
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
		byte[] code = new byte[blocks.toArray().length*(MOD_SIZE+1)];
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
