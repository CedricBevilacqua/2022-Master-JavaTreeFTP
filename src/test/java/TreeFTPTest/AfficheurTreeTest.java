package TreeFTPTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import TreeFTP.AfficheurTree;

public class AfficheurTreeTest {
	@Test
	public void main() {
		//Création de l'élément de test
		List<Object> arborescence = new ArrayList<Object>();
		arborescence.add(""); //Nom de base vide
		arborescence.add("Fichier1"); //Fichier 1 racine
		arborescence.add("Fichier2"); //Fichier 2 racine
		arborescence.add("Fichier3"); //Fichier 3 racine
		List<Object> dossier1 = new ArrayList<Object>();
		dossier1.add("Dossier1"); //Nom dossier
		List<Object> dossier2 = new ArrayList<Object>();
		dossier2.add("Dossier2"); //Nom sous dossier
		dossier2.add("Fichier1"); //Fichier 1 sous dossier
		dossier2.add("Fichier2"); //Fichier 2 sous dossier
		dossier2.add("Fichier3"); //Fichier 3 sous dossier
		dossier1.add(dossier2);
		dossier1.add("Fichier1"); //Fichier 1 dans dossier
		dossier1.add("Fichier2"); //Fichier 2 dans dossier
		arborescence.add(dossier1);
		arborescence.add("Fichier4"); //Fichier 4 racine
		arborescence.add("Fichier5"); //Fichier 5 racine
		
		//Création de l'élément réponse
		String tree = "";
		tree += "|___Fichier1" + "\n";
		tree += "|___Fichier2" + "\n";
		tree += "|___Fichier3" + "\n";
		tree += "|___Dossier1" + "\n";
		tree += "|  |___Dossier2" + "\n";
		tree += "|  |  |___Fichier1" + "\n";
		tree += "|  |  |___Fichier2" + "\n";
		tree += "|  |  |___Fichier3" + "\n";
		tree += "|  |___Fichier1" + "\n";
		tree += "|  |___Fichier2" + "\n";
		tree += "|___Fichier4" + "\n";
		tree += "|___Fichier5" + "\n";
		String jsonDossier2 = "Dossier2:{Fichier1,Fichier2,Fichier3}";
		String jsonDossier1 = "Dossier1:{" + jsonDossier2 + ",Fichier1,Fichier2}";
		String json = "{Fichier1,Fichier2,Fichier3," + jsonDossier1 + ",Fichier4,Fichier5}";

		//Test de l'élément
		AfficheurTree testAfficheur = new AfficheurTree(arborescence);
		String answer = testAfficheur.getTree();
		assertEquals(answer, tree); //Tree
		answer = testAfficheur.getJson();
		assertEquals(answer, json); //Json
	}

}
