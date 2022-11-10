package TreeFTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import FTP.FTPSession;

/**
 * Cette classe gère le parcours en profondeur de l'arborescence d'un serveur FTP
 *
 * @author Cédric Bevilacqua
 */
public class ParcoursProfondeurFTP {
	
	private FTPSession connecteur;
	
	/**
	 * Initialise les attributs
	 *
	 * @author Cédric Bevilacqua
	 * @param connecteur : Instance de connecteur FTP sur lequel on pourra parcourir l'arborescence du serveur
	 */
	public ParcoursProfondeurFTP(FTPSession connecteur) {
		this.connecteur = connecteur;
	}
	
	/**
	 * Génère l'arborescence du serveur et la renvoit
	 *
	 * @author Cédric Bevilacqua
	 * @param profondeur : Indication de la limite jusqu'à quel niveau on va parcourir l'arborescence du serveur
	 * @return Arborescence du serveur, liste composée d'autres listes ou de chaines de caractère
	 * @throws Exception liée au seveur
	 * @see Arborescence
	 */
	public List<Object> GetArborescence(int profondeur) throws IOException {
		return Arborescence("", 0, profondeur);
	}
	
	/**
	 * Méthode récursive de génération de l'arborescence à partir du serveur et d'une profondeur demandée
	 *
	 * @author Cédric Bevilacqua
	 * @param upName : Nom du dossier parent dans lequel nous sommes
	 * @param compteur : Compteur de la profondeur, incrémenté à chaque récursion de manière à s'arrêter à un certain niveau
	 * @return L'arborescence du serveur
	 * @throws Exception liée à la connexion FTP
	 */
	private List<Object> Arborescence(String upName, int compteur, int profondeur) throws IOException {
		List<Object> arborescence = new ArrayList<Object>();
		arborescence.add(upName); //Nom du dossier
		if(compteur > profondeur) { //Vérification de la profondeur maximale
			return arborescence;
		}
		List<String> listedFiles = connecteur.ListFile();
		for(String element : listedFiles) {
			if(IsDirectory(element)) { //Gestion d'un nouveau dossier suivi d'un appel récursif
				Boolean abandon = false;
				try {
					connecteur.Forward(ExtractName(element.toString()));
				} catch (Exception e) {
					abandon = true; //Gestion d'une erreur 550 catchée depuis le FTPSession
				}
				if(!abandon) {
					arborescence.add(Arborescence(ExtractName(element.toString()), compteur + 1, profondeur));
					connecteur.Backward();
				}
			} else { //Gestion d'un fichier
				arborescence.add(ExtractName(element.toString()));
			}
		}
		return arborescence;
	}
	
	/**
	 * Prend un élément avec toutes ses informations et ses droits et vérifie si il est un dossier
	 *
	 * @author Cédric Bevilacqua
	 * @param element : Nom complet d'une ligne renvoyée par le serveur FTP sur un fichier avec toutes ses informations
	 * @return Si oui ou non cet élément est un dossier
	 */
	private Boolean IsDirectory(String element) {
		if(element.charAt(0) == 'd') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Récupère le nom d'un fichier ou d'un dossier à partir d'une ligne entière renvoyée par le serveur FTP
	 *
	 * @author Cédric Bevilacqua
	 * @param element : Nom complet d'une ligne renvoyée par le serveur FTP sur un fichier avec toutes ses informations
	 * @return Nom du fichier
	 */
	private String ExtractName(String element) {
		return element.substring(56);
	}
	
}
