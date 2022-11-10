package TreeFTPTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import TreeFTP.ParcoursProfondeurFTP;

public class ParcoursProfondeurFTPTest {
	@Test
	public void main() throws IOException {
		FTPSessionMock ftpMock = new FTPSessionMock("ftp.ubuntu.com");
		ParcoursProfondeurFTP parcours = new ParcoursProfondeurFTP(ftpMock);
		String answer = parcours.GetArborescence(10).toString();
		assertEquals(answer, "[, Fichier1, Fichier2, Fichier3, [Dossier1, Fichier1, Fichier2, Fichier3], Fichier4, Fichier5, [Dossier2, Fichier1, [Dossier3, Fichier1, Fichier2, Fichier3, Fichier4, Fichier5, Fichier6, Fichier7, Fichier8, Fichier9, Fichier10], Fichier2]]");
		answer = parcours.GetArborescence(2).toString();
		assertEquals(answer, "[, Fichier1, Fichier2, Fichier3, [Dossier1, Fichier1, Fichier2, Fichier3], Fichier4, Fichier5, [Dossier2, Fichier1, [Dossier3, Fichier1, Fichier2, Fichier3, Fichier4, Fichier5, Fichier6, Fichier7, Fichier8, Fichier9, Fichier10], Fichier2]]");
		answer = parcours.GetArborescence(1).toString();
		assertEquals(answer, "[, Fichier1, Fichier2, Fichier3, [Dossier1, Fichier1, Fichier2, Fichier3], Fichier4, Fichier5, [Dossier2, Fichier1, [Dossier3], Fichier2]]");
		answer = parcours.GetArborescence(0).toString();
		assertEquals(answer, "[, Fichier1, Fichier2, Fichier3, [Dossier1], Fichier4, Fichier5, [Dossier2]]");
	}

}