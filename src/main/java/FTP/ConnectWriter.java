package FTP;

/**
 * Cette interface implémente une méthode permettant d'écrire sur les communications du serveur
 *
 * @author Cédric Bevilacqua
 */
public interface ConnectWriter {
	/**
	 * Envoi un message au serveur FTP sous la forme d'une chaine de caractères
	 *
	 * @author Cédric Bevilacqua
	 * @param message : Chaine de caractère texte à envoyer au serveur FTP
	 */
	public void write(String message);
}
