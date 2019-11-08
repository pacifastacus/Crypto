package crypto;

import java.math.BigInteger;
import java.util.Random;

public final class DHexchange {
	static BigInteger p;
	static BigInteger g;
	static BigInteger a, b;
	static int bitsize = 64;
	
	public static void main(String[] args) {
		Random rnd = new Random(); 
		//search p
		do {
			p = new BigInteger(bitsize, rnd);
		}while(!Algorithm.isPrime(p, 3));
		System.out.println("prime found");
		//search g
		g = BigInteger.ONE;
		boolean isGood;
		do{
			g = new BigInteger(bitsize, rnd);
			System.out.println(g);
			isGood = true;
			for(long i = 0; i < p.longValueExact(); i++) {
				if(Algorithm.quickPow(g, BigInteger.valueOf(i), p).equals(BigInteger.ONE)) {
					isGood = false;
					break;
				}
			}
		}while(isGood);
		System.out.println("g="+g);
		//choose a and b
		do {
			a = new BigInteger(bitsize, rnd);
		}while(a.compareTo(p.subtract(BigInteger.valueOf(2))) > 0 );
		System.out.println("a="+a);
		do {
			b = new BigInteger(bitsize, rnd);
		}while(b.compareTo(p.subtract(BigInteger.valueOf(2))) > 0 );
		System.out.println("b="+b);
		
		//Calculate A and B
		BigInteger A = Algorithm.quickPow(g, a, p);
		BigInteger B = Algorithm.quickPow(g, b, p);
		System.out.println("A="+A+" B="+B);
		
		//Calculate Ka and Kb
		BigInteger Ka = Algorithm.quickPow(B, a, p);
		BigInteger Kb = Algorithm.quickPow(A, b, p);
		System.out.println("Ka="+Ka);
		System.out.println("Kb="+Kb);
		System.out.println(Ka.equals(Kb));
		
		
	}
}
