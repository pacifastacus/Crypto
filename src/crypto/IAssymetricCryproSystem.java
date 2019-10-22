/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author palkovics
 */
public interface IAssymetricCryproSystem {
    public KeyPair keyGen(int size, int confidency);
    public String encode(String message, PublicKey publicKey) throws InvalidKeyException;
    public String decode(String code, PrivateKey secretKey) throws InvalidKeyException;
    public static String[] cuttingMessage(String message, int size) {
    	return new String[0];
    }
}
