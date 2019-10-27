/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/** 
 * RSA crypto system
 * Implements the RSA crypto system
 *
 * @author Palkovics Dénes
 */
public class RSA implements IAssymetricCryptoSystem<BigInteger>{
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
        
        return new KeyPair((PublicKey)new RSA_PK(n,e), (PrivateKey)new RSA_SK(p,q,d));
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
        }while(!Algorithm.euclid(e, phy).equals(BigInteger.ONE) || e.equals(pk.getExponent()));
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
        
        return new KeyPair((PublicKey)new RSA_PK(primes[0].multiply(primes[1]),e), 
        		(PrivateKey)new RSA_SK(primes[0],primes[1],d));
	}
	
	@Override
	public BigInteger encode(BigInteger num, PublicKey publicKey) throws InvalidKeyException, ArithmeticException{
		if(!publicKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'publicKey' is not an RSA key");
		}
		//Casting publickey to RSA_PK
		RSA_PK PK = (RSA_PK)publicKey;
		if(num.compareTo(PK.getModulus())>=0 || num.compareTo(BigInteger.ZERO)<0) {
			throw new ArithmeticException("Number to encode is out of range( [0,modulus[ )!");
		}
		return Algorithm.quickPow(num, PK.getExponent(), PK.getModulus());
		//return num.modPow(PK.getPublicExponent(), PK.getModulus());
	}
	
	@Override 
	public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException {
		if(!secretKey.getAlgorithm().equals("RSA"))
		{
			throw new InvalidKeyException("The provided 'secretKey' is not an RSA key");
		}
		RSA_SK SK = (RSA_SK)secretKey;
		return Algorithm.quickPow(code, SK.getExponent(), SK.getModulus());
		//return code.modPow(SK.getPrivateExponent(), SK.getModulus());
	}

	@Override
	public Queue<BigInteger> crypt(String message, PublicKey publicKey) throws InvalidKeyException {
		byte[] bytesOfMessage = message.getBytes(StandardCharsets.UTF_8);
		Queue<BigInteger> blocks = new LinkedList<BigInteger>();
		for(int i = 0; i<Math.ceil((double)bytesOfMessage.length/MOD_SIZE ) ; i++)
			try {
				BigInteger c = new BigInteger(1,Arrays.copyOfRange(bytesOfMessage, i*MOD_SIZE, (i+1)*MOD_SIZE));
				blocks.add(encode(c, publicKey));
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.err.println("Out of array in encoding process!");
			}
		
		return blocks;
	}

	@Override
	public String decrypt(Queue<BigInteger> code, PrivateKey secretKey) throws InvalidKeyException {
		Queue<Byte> message = new LinkedList<Byte>();
		byte[] bytes;
		//decoding the blocks
		for (BigInteger block : code) {
			bytes = decode(block, secretKey).toByteArray();
			
			//boxing bytes to Byte type
			for(byte b : bytes) {
				if(b == 0) continue;	//
				message.add(b);
			}
		}
		byte[] tmp = new byte[message.size()];
		int i = 0;
		for(Byte b: message.toArray(new Byte[0])) {
			tmp[i++] = b.byteValue();
		}
		
		
		return new String(tmp,StandardCharsets.UTF_8);
	}
}
