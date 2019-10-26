/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.math.BigInteger;

/**
 *
 * @author hallgato
 */
public class TestEA {
    public static void main(String[] args) {
        BigInteger a = new BigInteger("280");
        BigInteger b = new BigInteger("32");
        BigInteger ea = Algorithm.euclid(a, b);
        System.err.println("EA finished");
        BigInteger[] eea = Algorithm.extEuclid(a, b);
        System.err.println("EEA finished");
        boolean ea_corr = ea.equals(a.gcd(b));
        boolean eea1_corr = eea[0].equals(a.gcd(b));
        BigInteger x = a.multiply(eea[1]);
        BigInteger y = b.multiply(eea[2]);
        boolean eea2_corr = a.gcd(b).equals(x.add(y));
       
        System.out.println("euclid: " + ea + " " + ea_corr);
        System.out.println("extended euclid:\n"
                + eea[0] + " " + eea1_corr + "\n"
                + eea[1] + " " + eea2_corr + "\n"
                + eea[2] + " " + eea2_corr + "\n"
        );
        System.err.println("Test BigInteger operator precedence");
        x = new BigInteger("10");
        System.out.println("x*-1+10="+x.multiply(new BigInteger("-1")).add(new BigInteger("10")));
        System.out.println("x+-1*10="+x.add(new BigInteger("-1")).multiply(new BigInteger("10")));
        
        System.err.println("Test BigInteger.flipBit() function");
        x = new BigInteger("21");
        System.out.println("x="+x);
        System.out.println("x.flipBit(0):"+x.flipBit(0));
    }
}
