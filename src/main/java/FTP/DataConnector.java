package FTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Cette classe hérite d'un connecteur et implémente la faculté de lire. 
 * Elle permet de manipuler le canal de données en FTP.
 *
 * @author Cédric Bevilacqua
 */
public class DataConnector extends Connector implements ConnectReader {
	
	private InputStream in;
	private InputStreamReader inReader;
	private BufferedReader reader;

	/**
	 * Démarre la connexion et instancie tous les éléments nécessaires à la lecture
	 *
	 * @author Cédric Bevilacqua
	 * @param adresse : Adresse du serveur sur lequel établir la connexion
	 * @param port : Numéro de port sur lequel établir la connexion
	 * @throws Erreur de connexion
	 */
	public DataConnector(String adresse, int port) throws IOException {
		super(adresse, port);
		//Lecture
		this.in = this.connexion.getInputStream();
		this.inReader = new InputStreamReader(this.in);
		this.reader = new BufferedReader(inReader);
	}

	/**
	 * Méthode de lecture implémenté
	 *
	 * @author Cédric Bevilacqua
	 * @return Chaine de caractère réponse du serveur FTP
	 * @throws Erreur de connexion
	 */
	public String read() throws IOException {
		String fromFTP = this.reader.readLine();
		return fromFTP;
	}
	
}
