# BEVILACQUA-TreeFTP

Affichage de l'arborescence d'un serveur FTP
Cédric Bevilacqua
24/01/22

## Introduction

Ce programme se connecte à un serveur FTP indiqué en premier paramètre, puis récupère l'arborescence du serveur.

Cela peut être tout l'arborescence où simplement une partie avec une profondeur limité qui peut être spécifiée en deuxième paramètre. Cette arborescence est stockée dans une structure de donnée qui est une liste qui peut contenir d'autres listes (dossiers) ou des chaines de caractère String (fichier). Le premier élément de chaque liste correspond au titre du dossier.

Enfin, le programme transforme cette arborescence dans une représentation d'arbre textuelle (tree) ou dans un Json si la caractère j est rajouté en 3ème paramètre. Cette représentation est ensuite affichée dans la console.

## Architecture

Le programme est réparti en deux parties qui sont deux packages distinct : FTP et TreeFTP.

Le package FTP s'occupe de tout ce qui concerne la connexion FTP. Elle contient une classe FTPSession qui sera la classe principale de ce package car c'est elle qui sera instanciée et s'occupera de gérer le dialogue avec le serveur. Elle contient des méthodes simples comme lister les objets du répertoire, avancer dans un répertoire ou revenir en arrière.

Cette classe gère donc les deux connexions FTP : la connexion des données et la connexion des commandes. Ces deux connexions sont matérialisées par deux objets CommandeConnector et DataConnector qui sont contenu dans FTPSession qui se chargera de les instancier et de les appeler. Ces deux objets contiennent les deux connexions et dérivent toutes les deux d'une même classe abstraite Connector qui gère une connexion avec une Socket et des méthodes simples pour démarrer ou arrêter la connexion.

Ces deux classes implémentent des interfaces. Une première interface contient une méthode pour lire le contenu retourné et l'autre pour écrire du contenu au serveur. La classe gérant les commandes implémente les deux interfaces tandis que celle qui gère la data n'implémente que la lecture des données car elle n'aura à priori jamais besoin d'écrire.

Concernant l'autre partie du programme, elle contient toutes des classes indépendantes qui serviront au traitement des données. Tout d'abord, la classe Main qui s'exécute au démarrage du programme et gère les arguments et l'instanciation des différentes classes et l'affichage de leur résultat.

Deux autres classes sont ensuite disponibles, une par fonction. La première s'occupe de générer une arborescence en parcourant le contenu d'un serveur FTP. Elle prend lors de l'instance une FTPSession et l'utilise pour générer une arborescence.

La dernière classe va cette fois récupérer cette arborescence afin d'en générer une représentation textuelle au choix, sous la forme d'un arbre ou d'un json. La classe main s'occupera ensuite d'afficher sa réponse au travers d'accesseurs.

Concernant les erreurs, les problèmes de connexion sont systématiquement remontées jusqu'au main qui va les gérer en conséquence. Seul l'entrée dans un dossier lors du parcours au profondeur peut générer une exception lorsque le serveur coupe la connexion à cause d'une manoeuvre interdite et envoi une erreur 550. Dans ce cas, l'exception va être récupérée et le dossier s'affichera mais son contenu sera ignoré, le parcours continuera ensuite sur le dossier suivant et la classe FTPSession s'occupera en toute transparence de reconnecter la session et de revenir au chemin actuel qui a été mémorisé afin de poursuivre le parcours sans problème.

## Code Samples

Cette méthode gère la parcours en profondeur du serveur FTP afin de générer l'arborescence qui correspond tout en respectant l'éventuelle contrainte de profondeur. Il s'agit d'une méthode appelée récursivement à chaque dossier rencontré pour générer une liste de fichiers et de sous dossiers.

```java
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
```

Ces deux méthodes permettent de générer une représentation sous forme d'arbre à partir d'une arborescence sous la forme de listes. Chaque élément de la liste est parcouru et affichée sous la forme d'un arbre généré par la deuxième méthode à partir d'une prondeur qui lui est indiqué selon le niveau de récursion de la méthode.

```java
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
	
  private String getLine(String name, int profondeur) {
    String line = "";
    for(int boucle = 0; boucle < profondeur; boucle++) {
      line += "|  ";
    }
    line += "|___" + name;
    line += "\n";
    return line;
  }
```

Cette méthode génère une représentation en Json à partir d'une arborescence. Chaque élément est parcouru et la méthode est appelée récursivement pour chaque sous dossier afin de générer les sous ensembles Json.

```java
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
```

Cette classe gère la connexion au canal de commandes du serveur FTP, elle est contenue dans le FTPSession qui l'utilise pour communiquer avec le serveur. Elle implémente deux interfaces permettant de lire et écrire ainsi qu'un connecteur qui contiendra toute la partie intialisation de la connexion.

```java
public class CommandeConnector extends Connector implements ConnectReader, ConnectWriter {
	
  private InputStream in;
  private InputStreamReader inReader;
  private BufferedReader reader;
  private OutputStream out;
  private PrintWriter print;
	
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
	
  public String read() throws IOException {...}

  public void write(String message) {...}
	
  public void Disconnect() throws IOException {
    write("QUIT");
    super.Disconnect();
  }
}
```

Ces deux méthodes font partie de la classe FTPSession et gèrent l'avance dans un dossier. L'erreur 500 est gérée par la première méthode et une méthode de reconnexion du canal de données est systématiquement appelée qui va également reconnecter le canal de commandes et le ramener à l'emplacement précédent à l'aide d'une pile qui contient les dernières commandes utilisées pour revenir dans un état cohérent.

```java
	public void Forward(String directoryName) throws Exception {
		commandes.write("CWD " + directoryName);
		String answer = commandes.read();
		if(answer.charAt(0) == '5' && answer.charAt(1) == '5' && answer.charAt(2) == '0') {
			throw new Exception(answer);
		}
		chemin.add(directoryName);
		ReconnectDataConnector();
	}
	
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
```