/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

/**
 *
 * @author palkovics
 */
public interface ICryptoSystem {
    public KeyPair keyGen(int size, int confidency);
    public String encode(String message, Key publicKey);
    public String decode(String code, Key secretKey);
    public static String[] cuttingMessage(String message, int size) {
    	return new String[0];
    }
}
