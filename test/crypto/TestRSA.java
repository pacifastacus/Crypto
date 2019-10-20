package crypto;

public class TestRSA {
	
	public static void main(String[] args) {
		RSA cryptoSystem = new RSA();
		RSAKeyPair keys = (RSAKeyPair)cryptoSystem.keyGen(12, 10); 
//		String message = "Árvíztűrő tükörfúrógép. Ezt a mondatot a karakterkészelt tesztelésére használjuk. Így bizonyosodhatunk meg affelől, hogy a különleges magyar karakterek is helyesen megjelennek";
		String message = "Foobar zarathustra";
		String code;
		System.out.println("üzenet:\n"+message);
		code = cryptoSystem.encode(message, keys.getPk());
		System.out.println("titkos üzenet:\n"+code);
		System.out.println("Visszafejtett üzenet:\n"+
				cryptoSystem.decode(code, keys.getSk())
		);
	}

}
