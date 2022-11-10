package FTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe gère toute la connexion à un serveur FTP et permet d'y réaliser des commandes de bases
 *
 * @author Cédric Bevilacqua
 */
public class FTPSession {

	private DataConnector donnees;
	private CommandeConnector commandes;
	private String adresse;
	private int port;
	private List<String> chemin = new ArrayList<String>();
	
	/**
	 * Initialisation des attributs, le port est défini automatiquement
	 *
	 * @author Cédric Bevilacqua
	 * @param adresse : Adresse de connexion du serveur FTP
	 */
	public FTPSession(String adresse) {
		this.adresse = adresse;
		this.port = 21;
	}
	
	/**
	 * Initialisation de la connexion au serveur FTP
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur de connexion
	 */
	public void Connect() throws IOException {
		//Connexion au canal de commandes
		commandes = new CommandeConnector(adresse, port);
		commandes.read(); //Attendu : 220 FTP server (vsftpd)
		commandes.write("AUTH TLS"); //Vu sur FileZilla
		commandes.read(); //Attendu : 530 Please login with USER and PASS.
		commandes.write("AUTH SSL"); //Vu sur FileZilla
		commandes.read(); //Attendu : 530 Please login with USER and PASS.
		commandes.write("USER anonymous"); //Connexion sans identifiant
		commandes.read(); //Attendu : 331 Please specify the password.
		commandes.write("PASS"); //Connexion sans mot de passe
		commandes.read(); //Attendu : 230 Login successful.
		commandes.write("TYPE I"); //Vu sur FileZilla
		commandes.read(); //Attendu : 200 Switching to Binary mode.
		
		//Connexion au canal de données
		ReconnectDataConnector();
	}
	
	/**
	 * Arrêt de la connexion
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur de connexion
	 */
	public void Disconnect() throws IOException {
		commandes.Disconnect();
		donnees.Disconnect();
	}
	
	/**
	 * Avance dans l'arborescence
	 *
	 * @author Cédric Bevilacqua
	 * @param directoryName : Nom du dossier vers lequel aller
	 * @throws Exception d'erreur de connexion ou de fin brutale de la connexion par le serveur
	 */
	public void Forward(String directoryName) throws Exception {
		commandes.write("CWD " + directoryName);
		String answer = commandes.read();
		if(answer.charAt(0) == '5' && answer.charAt(1) == '5' && answer.charAt(2) == '0') {
			throw new Exception(answer);
		}
		chemin.add(directoryName);
		ReconnectDataConnector();
	}
	
	/**
	 * Revient en arrière dans l'arborescence et rouvre une connexion de données
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur de connexion
	 */
	public void Backward() throws IOException {
		commandes.write("CDUP");
		commandes.read();
		chemin.remove(chemin.size() - 1);
		ReconnectDataConnector();
	}
	
	/**
	 * Liste tous les fichiers présents dans le répertoire
	 *
	 * @author Cédric Bevilacqua
	 * @return Liste des fichiers et dossiers visibles à l'adresse où le serveur se trouve
	 * @throws Erreur de connexion
	 */
	public List<String> ListFile() throws IOException {
		commandes.write("LIST");
		List<String> fileList = new ArrayList<String>();
		String gettedList  = "";
	    while(gettedList != null) {
	    	gettedList = donnees.read();
	    	if(gettedList != null) {
	    		fileList.add(gettedList);
	    	}
	    }
	    commandes.read();
	    commandes.read();
	    return fileList;
	}
	
	/**
	 * Reconnecte la connexion aux données ainsi que la connexion aux commanes si elle a été fermée
	 *
	 * @author Cédric Bevilacqua
	 * @throws Erreur de connexion
	 */
	private void ReconnectDataConnector() throws IOException {
		if(donnees != null) {
			donnees.Disconnect();
		}
		commandes.write("PASV"); //Passage en mode passif pour contourner les restrictions pare-feu
		String connectInfos = commandes.read(); //Attendu : 227 Entering Passive Mode (XXX,XXX,XXX,XXX,XXX,XXX)
		//Détection d'une erreur dans la connexion des commandes
		if(connectInfos == null) {
			//Reconnexion au serveur
			commandes.Disconnect();
			Connect();
			//Reprise sur le chemin où on était
			for(String dossier : chemin) {
				commandes.write("CWD " + dossier);
				commandes.read();
			}
			ReconnectDataConnector();
			return;
		}
		//Récupération de l'IP et du port
		int retrait = 1;
		while(connectInfos.charAt(connectInfos.length() - retrait) != ')') {
			retrait++;
		}
		connectInfos = connectInfos.substring(27, connectInfos.length() - retrait);
		String[] extractedConnectInfos = connectInfos.split(",");
		String ip = extractedConnectInfos[0] + '.';
		ip += extractedConnectInfos[1] + '.';
		ip += extractedConnectInfos[2] + '.';
		ip += extractedConnectInfos[3];
		int port = Integer.valueOf(extractedConnectInfos[4]) * 256 + Integer.valueOf(extractedConnectInfos[5]);
		//Reconnexion
		donnees = new DataConnector(ip, port);
	}
	
}
