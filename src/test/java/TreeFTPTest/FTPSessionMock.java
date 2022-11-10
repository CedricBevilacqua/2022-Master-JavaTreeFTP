package TreeFTPTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import FTP.FTPSession;

public class FTPSessionMock extends FTPSession {
	
	private List<String> racine;
	private List<String> dossier1;
	private List<String> dossier2;
	private List<String> dossier3;
	private List<String> actual;
	
	public FTPSessionMock(String adresse) {
		super(adresse);
		//Création de l'élément de test
		racine = new ArrayList<String>();
		racine.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier1");
		racine.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier2");
		racine.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier3");
		racine.add("drwxrwxr-x    5 997      997          4096 Jan 10 17:26 Dossier1");
		racine.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier4");
		racine.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier5");
		racine.add("drwxrwxr-x    5 997      997          4096 Jan 10 17:26 Dossier2");
		dossier1 = new ArrayList<String>();
		dossier1.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier1");
		dossier1.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier2");
		dossier1.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier3");
		dossier2 = new ArrayList<String>();
		dossier2.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier1");
		dossier2.add("drwxrwxr-x    5 997      997          4096 Jan 10 17:26 Dossier3");
		dossier2.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier2");
		dossier3 = new ArrayList<String>();
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier1");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier2");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier3");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier4");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier5");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier6");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier7");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier8");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier9");
		dossier3.add("lrwxrwxr-x    5 997      997          4096 Jan 10 17:26 Fichier10");
		actual = racine;
	}
	
	public void Connect() { }
	
	public void Disconnect() { }
	
	public void Forward(String directoryName) {
		if(actual.indexOf("drwxrwxr-x    5 997      997          4096 Jan 10 17:26 " + directoryName) >= 0) {
			if(directoryName.length() == 8 && directoryName.charAt(7) == '1') {
				actual = dossier1;
			} else if(directoryName.length() == 8 && directoryName.charAt(7) == '2') {
				actual = dossier2;
			} else if(directoryName.length() == 8 && directoryName.charAt(7) == '3') {
				actual = dossier3;
			}
		}
	}
	
	public void Backward() {
		if(actual.equals(dossier1)) {
			actual = racine;
		} else if(actual.equals(dossier2)) {
			actual = racine;
		} else if(actual.equals(dossier3)) {
			actual = dossier2;
		}
	}
	
	public List<String> ListFile() {
	    return actual;
	}
	
}
