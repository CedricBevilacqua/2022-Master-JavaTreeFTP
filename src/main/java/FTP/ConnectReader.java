package FTP;

import java.io.IOException;

/**
 * Cette interface implémente une méthode permettant de lire sur les communications du serveur
 *
 * @author Cédric Bevilacqua
 */
public interface ConnectReader {
	/**
	 * Lit un message reçu du serveur FTP sous la forme d'une chaine de caractères
	 *
	 * @author Cédric Bevilacqua
	 * @return Renvoit la chaine de caractère reçue par le serveur et disponible dans le buffer
	 * @throws Erreur de connexion
	 */
	public String read() throws IOException;	
}
