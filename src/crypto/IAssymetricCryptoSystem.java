/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.Queue;

/**
 *
 * @author palkovics
 */
public interface IAssymetricCryptoSystem<T>{
	
	/**
	 * Generate a public and secret key, which contained in a KeyPair object
	 * @param confidency when prime numbers involved. The higher number means greater propability that the primes are actually primes
	 * @param size  the size of the key (dependes on the key type)
	 * @return a KeyPair object which holds the public and the secret key
	 */
    public KeyPair keyGen(int confidency, int size);
    
    /**
     * Elementary encoding function used on a number type message
     * @param num the number to encode
     * @param publicKey
     * @return the encoded number
     * @throws InvalidKeyException thrown if wrong key type used 
     */
    public BigInteger encode(BigInteger num, PublicKey publicKey) throws InvalidKeyException;
    
    /**
     * Elementary decoding function used on a number which is encrypted
     * @param code the encrypted number
     * @param secretKey
     * @return the decrypted number
     * @throws InvalidKeyException thrown if wrong key type used
     */
    public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException;
    
    /**
     * Encrypt a message. The function should encrypt arbitrary long message
     * @param <T> The encoded blocks type
     * @param message clear text message
     * @param publicKey
     * @return the encrypted message. A Queue of encrypted blocks
     * @throws InvalidKeyException thrown if wrong key type used
     */
    public Queue<T> encrypt(String message, PublicKey publicKey) throws InvalidKeyException;
    
    /**
     * Decrypt a message. The function should decrypt arbitrary long encrypted message
     * @param code Queue of <T> blocks, which is the encrypted message
     * @param secretKey
     * @return the decrypted message in clear text
     * @throws InvalidKeyException thrown if wrong key type used
     */
    public String decrypt(Queue<T> code, PrivateKey secretKey) throws InvalidKeyException;
}