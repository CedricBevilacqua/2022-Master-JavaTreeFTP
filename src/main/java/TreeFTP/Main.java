package TreeFTP;

import java.io.IOException;
import java.util.List;

import FTP.FTPSession;

public class Main {
	public static void main (String[] args) throws IllegalArgumentException {
		//Vérification des paramètres
		if(args.length < 1) {
			System.out.println("Arguments incorrectes");
			throw new IllegalArgumentException();
		}
		//Connexion FTP
		FTPSession ftp = new FTPSession(args[0]);
		try {
			ftp.Connect();
		} catch (IOException e) {
			System.out.println("Problème de connexion au serveur");
			e.printStackTrace();
			return;
		}
		//Parcours
		ParcoursProfondeurFTP parcours = new  ParcoursProfondeurFTP(ftp);
		List<Object> arborescenceList = null;
		int profondeur = 1000000;
		if(args.length >= 2) {
			profondeur = Integer.parseInt(args[1]);
		}
		try {
			arborescenceList = parcours.GetArborescence(profondeur);
		} catch (NumberFormatException e) {
			System.out.println("L'argument indiquant le nombre de recursions est incorrecte");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problème de connexion au serveur");
			e.printStackTrace();
			return;
		}
		try {
			ftp.Disconnect(); //Déconnexion
		} catch (IOException e) { }
		//Affichage de l'arbre
		AfficheurTree afficheur = new AfficheurTree(arborescenceList);
		if(args.length >= 3 && args[2].charAt(0) == 'j') {
			String json = afficheur.getJson();
			System.out.println(json);
		} else {
			String tree = afficheur.getTree();
			System.out.println(tree);
		}
	}
}
