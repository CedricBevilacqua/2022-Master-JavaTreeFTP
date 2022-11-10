package FTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Cette classe hérite d'un connecteur et implémente la faculté de lire et d'écrire. 
 * Elle permet de manipuler le canal de commandes en FTP.
 *
 * @author Cédric Bevilacqua
 */
public class CommandeConnector extends Connector implements ConnectReader, ConnectWriter {
	
	private InputStream in;
	private InputStreamReader inReader;
	private BufferedReader reader;
	private OutputStream out;
	private PrintWriter print;
	
	/**
	 * Démarre la connexion et instancie tous les éléments nécessaires à la lecture et l'écriture
	 *
	 * @author Cédric Bevilacqua
	 * @param adresse : Adresse du serveur sur lequel établir la connexion
	 * @param port : Numéro de port sur lequel établir la connexion
	 * @throws Erreur de connexion
	 */
	public CommandeConnector(String adresse, int port) throws IOException {
		super(adresse, port);
		//Lecture
		this.in = this.connexion.getInputStream();
		this.inReader = new InputStreamReader(this.in);
		this.reader = new BufferedReader(inReader);
		//Ecriture
		this.out = this.connexion.getOutputStream();
		this.print = new PrintWriter(this.out, true);
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

	/**
	 * Méthode d'écriture implémenté
	 *
	 * @author Cédric Bevilacqua
	 * @param message : Message chaine de caractères à envoyer
	 */
	public void write(String message) {
		this.print.println(message);
	}
	
	/**
	 * Déconnecte la connexion
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur au niveau de la Socket
	 */
	public void Disconnect() throws IOException {
		write("QUIT");
		super.Disconnect();
	}
	
}
