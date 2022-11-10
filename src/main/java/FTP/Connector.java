package FTP;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe abstraite reprenant le système de connexion avec une socket sur un serveur
 *
 * @author Cédric Bevilacqua
 */
public abstract class Connector {
	
	protected Socket connexion;
	private String adresse;
	private int port;
	
	/**
	 * Initialise les attributs et démarre la connexion
	 *
	 * @author Cédric Bevilacqua
	 * @param adresse : Adresse du serveur sur lequel établir la connexion
	 * @param port : Numéro de port sur lequel établir la connexion
	 * @throws Problème de connexion
	 */
	public Connector(String adresse, int port) throws UnknownHostException, IOException {
		this.adresse = adresse;
		this.port = port;
		Connect();
	}
	
	/**
	 * Démarre la connexion
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur lié à la connexion sur la socket
	 */
	public void Connect() throws UnknownHostException, IOException {
		if(connexion != null) { Disconnect(); }
		connexion = new Socket(adresse, port);
	}
	
	/**
	 * Stoppe la connexion et libère la mémoire
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur lié à la socket
	 */
	public void Disconnect() throws IOException {
		if(connexion != null && connexion.isConnected()) {
			connexion.close();
			connexion = null;
		}
	}
	
}
