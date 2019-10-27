package crypto;

/**
 * 
 * @author Palkovics Dénes
 *
 */
interface Key {
	
	/**
	 * Return a string that tells which kind of cryptosystem this key is used 
	 * @return the name of the cryptosystem
	 */
	public String getAlgorithm();
}
