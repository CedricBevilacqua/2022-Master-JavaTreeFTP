package TreeFTP;

import java.util.List;

/**
 * Classe de gestion de l'affichage permettant de passer d'une représentation arborescente d'une liste de liste et de chaines 
 * de caractère à une représentation textuelle en arbre ou Json
 *
 * @author Cédric Bevilacqua
 */
public class AfficheurTree {
	
	private List<Object> arborescence;
	private String tree;
	private String json;

	/**
	 * Met à jour les attributs et démarre la génération de l'arbre et du json associé à l'entrée
	 *
	 * @author Cédric Bevilacqua
	 * @param arborescence : Arborescence des fichiers et dossiers sous la forme d'une liste contenant 
	 * d'autres listes ou des String à partir duquel on va générer les représentations
	 */
	public AfficheurTree(List<Object> arborescence) {
		this.arborescence = arborescence;
		this.tree = "";
		calcTree(this.arborescence, 0);
		calcJson(this.arborescence);
		json = json.substring(5, json.length() - 1);
	}
	
	/**
	 * Accesseur permettant d'obtenir la représentation arborescente sous la forme d'arbre après qu'elle ait été générée
	 *
	 * @author Cédric Bevilacqua
	 * @return Arbre textuel généré
	 */
	public String getTree() {
		return tree;
	}
	
	/**
	 * Accesseur permettant d'obtenir la représentation arborescente sous la forme d'un JSON après qu'elle ait été générée
	 *
	 * @author Cédric Bevilacqua
	 * @return Le JSON généré au format texte
	 */
	public String getJson() {
		return json;
	}
	
	/**
	 * Méthode de calcul de l'arbre, passage de la représentation arborescente en liste à une représentation textuelle sous 
	 * la forme d'un arbre
	 *
	 * @author Cédric Bevilacqua
	 * @param arborescence : Liste de liste et de chaines de caractère représentant l'arborescence d'un système de fichiers
	 * @param profondeur : Niveau de profondeur de la méthode, incrémenté à chaque recursion pour pouvoir 
	 * mettre à jour le nombre de branches de l'arbre à chaque ligne
	 */
	@SuppressWarnings("unchecked")
	private void calcTree(List<Object> arborescence, int profondeur) {
		Boolean firstPass = true;
		for(Object element : arborescence) {
			if(!firstPass) {
				if(element instanceof String) {
					this.tree += getLine(element.toString(), profondeur);
				} else if(element instanceof List<?>) {
					this.tree += getLine(((List<Object>)element).toArray()[0].toString(), profondeur);
					calcTree((List<Object>)element, profondeur + 1);
				}
			} else {
				firstPass = false;
			}
		}
	}
	
	/**
	 * Génère une ligne de l'arbre textuel à partir d'un nom et d'une profondeur à afficher
	 *
	 * @author Cédric Bevilacqua
	 * @param name : Nom du fichier à indiquer sur cette ligne de l'arbre
	 * @param profondeur : Nombre de branches à afficher
	 * @return Nouvelle ligne de l'arbre à rajouter
	 * @see calcTree
	 */
	private String getLine(String name, int profondeur) {
		String line = "";
		for(int boucle = 0; boucle < profondeur; boucle++) {
			line += "|  ";
		}
		line += "|___" + name;
		line += "\n";
		return line;
	}
	
	/**
	 * Méthode de calcul de l'arbre, passage de la représentation arborescente en liste à une représentation textuelle sous 
	 * la forme d'un Json
	 *
	 * @author Cédric Bevilacqua
	 * @param arborescence : Liste de liste et de chaines de caractère représentant l'arborescence d'un système de fichiers
	 */
	@SuppressWarnings("unchecked")
	private void calcJson(List<Object> arborescence) {
		Boolean firstPass = true;
		for(Object element : arborescence) {
			if(!firstPass) {
				if(element instanceof String) {
					json += element.toString() + ',';
				} else if(element instanceof List<?>) {
					calcJson((List<Object>)element);
				}
			} else {
				json += element + ":{";
				firstPass = false;
			}
		}
		if(json.charAt(json.length()-1) == ',') {
			json = json.substring(0, json.length() - 1);
		}
		json += "},";
	}
	
}
