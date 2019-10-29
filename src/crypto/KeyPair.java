package crypto;

/**
 * 
 * @author Palkovics Dénes
 *
 */
public class KeyPair {
	private PrivateKey privateKey;
    private PublicKey publicKey;
    
    public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
    
    public PublicKey getPublic() {
        return publicKey;
    }
    
    public PrivateKey getPrivate() {
        return privateKey;
    }
}
