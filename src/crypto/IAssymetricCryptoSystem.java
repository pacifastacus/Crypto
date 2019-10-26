/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author palkovics
 */
public interface IAssymetricCryptoSystem {
	/**
	 * Generate a public and secret key, which contained in a KeyPair object
	 * @param confidency when prime numbers involved, 
	 * @return a KeyPair object which holds the public and the secret key
	 */
    public KeyPair keyGen(int confidency);
    
    /**
     * Elementary encoding function used on a number type message
     * @param num the number to encode
     * @param publicKey
     * @return the encoded number
     * @throws InvalidKeyException if wrong type of key used 
     */
    public BigInteger encode(BigInteger num, PublicKey publicKey) throws InvalidKeyException;
    
    /**
     * Elementary decoding function used on a number which is encrypted
     * @param code the encrypted number
     * @param secretKey
     * @return the decrypted number
     * @throws InvalidKeyException if wrong type of key used
     */
    public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException;
    
    /**
     * Encrypt a message. The function should encrypt arbitrary long message
     * @param message clear text message
     * @param publicKey
     * @return the encrypted message. An array of bytes
     * @throws InvalidKeyException 
     */
    public byte[] crypt(String message, PublicKey publicKey) throws InvalidKeyException;
    
    /**
     * Decrypt a message. The function should decrypt arbitrary long encrypted message
     * @param code array of bytes, which is the encrypted message
     * @param secretKey
     * @return the decrypted message in clear text
     * @throws InvalidKeyException
     */
    public String decrypt(byte[] code, PrivateKey secretKey) throws InvalidKeyException;
}
