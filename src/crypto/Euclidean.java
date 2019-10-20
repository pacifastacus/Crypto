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
public class Euclidean {
    
    public static BigInteger euclid(BigInteger a, BigInteger b){
        BigInteger r;
        a = a.abs();
        b = b.abs();
        while(!b.equals(BigInteger.ZERO)){
            r = a.mod(b);
            a = b;
            b = r;
        }
                
        return a;
    }
    
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b){
        BigInteger[] dxy = new BigInteger[3]; //gcd(a,b), X, Y
        BigInteger[] qnr = new BigInteger[2];
        BigInteger[] x = new BigInteger[2];
        BigInteger[] y = new BigInteger[2];
        BigInteger tmp;
        x[0] = new BigInteger("1");
        x[1] = new BigInteger("0");
        y[0] = new BigInteger("0");
        y[1] = new BigInteger("1");
        boolean counter = false;
        
        a = a.abs();
        b = b.abs();
        while(!b.equals(BigInteger.ZERO)){
            counter = !counter;    
            qnr = a.divideAndRemainder(b);
            a = b;
            b = qnr[1];
            tmp = new BigInteger(x[1].multiply(qnr[0]).add(x[0]).toString());
            x[0] = x[1];
            x[1] = tmp;
            tmp = new BigInteger(y[1].multiply(qnr[0]).add(y[0]).toString());
            y[0] = y[1];
            y[1] = tmp;
        }
        dxy[0] = a;
        dxy[1] = counter ? x[0].negate() : x[0];
        dxy[2] = counter ? y[0] : y[0].negate();
        return dxy;
    }
}
