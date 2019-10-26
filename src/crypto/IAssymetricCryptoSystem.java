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
    public KeyPair keyGen(int size, int confidency);
    public BigInteger encode(BigInteger num, PublicKey publicKey) throws InvalidKeyException;
    public BigInteger decode(BigInteger code, PrivateKey secretKey) throws InvalidKeyException;
    public byte[] crypt(String message, PublicKey publicKey) throws InvalidKeyException;
    public String decrypt(byte[] code, PrivateKey secretKey) throws InvalidKeyException;
}
