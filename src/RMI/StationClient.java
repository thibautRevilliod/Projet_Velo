package RMI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import metier.GestionStation;

public class StationClient {
	public static GestionStation proxyGS;
	public static String valeurChoix;
	//public static Scanner sc = new Scanner(System.in);
	public static BufferedReader entree = new BufferedReader(new InputStreamReader(System.in)); 
	
	public static void menuPrincipal() throws IOException{
		System.out.println("------------ Bienvenue ------------");
		System.out.println("--1) Créer compte");
		System.out.println("--2) S'identifier");
		System.out.println("------------ Fin ------------");
		valeurChoix = entree.readLine();
	}
	
	public static void menuCreerCompte() throws IOException{
		String tab[];
		String mdp;
		int identifiantUtilisateur = 0;
		
		System.out.println("Veuillez choisir le type de compte : ");
		System.out.println("--1) Client");
		System.out.println("--2) Operateur");
		System.out.println("--3) Administrateur");
		System.out.println("--4) Quitter");
		valeurChoix = entree.readLine();
			
		switch(valeurChoix)
		{
		case "1":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], "Client");
			messageUtilisateurCree(identifiantUtilisateur, mdp);	
			break;
		case "2":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], "Operateur");
			messageUtilisateurCree(identifiantUtilisateur, mdp);	
			break;
		case "3":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], "Administrateur");
			messageUtilisateurCree(identifiantUtilisateur, mdp);
			break;
		case "4":
			menuPrincipal();
		}	
	}
	
	public static void messageUtilisateurCree(int pidentifiantUtilisateur, String pmdp){
		System.out.println("Utilisateur créé");
		System.out.println("  Identifiant : " + pidentifiantUtilisateur);
		System.out.println("  Mot de passe : " + pmdp);	
	}
	
	public static String[] demandeInfoCreeUtilisateur() throws IOException{	
		String result[] = new String[6];
		
		System.out.println("Veuillez entrer votre nom : ");
		result[0] = entree.readLine();
		System.out.println("Veuillez entrer votre prenom : ");
		result[1] = entree.readLine();
		System.out.println("Veuillez entrer votre telephone : ");
		result[2] = entree.readLine();
		System.out.println("Veuillez entrer votre email : ");
		result[3] = entree.readLine();
		System.out.println("Veuillez entrer votre adresse postale : ");
		result[4] = entree.readLine();
		System.out.println("Veuillez entrer votre mot de passe : ");
		result[5] = entree.readLine();
		
		return result;
	}
	
	public static void menuIdentification() throws NumberFormatException, IOException
	{
		int identifiant;
		String mdp;
		System.out.println("Veuillez entrer votre identifiant : ");
		identifiant = Integer.valueOf(entree.readLine());
		System.out.println("Veuillez entrer votre mot de passe : ");
		mdp = entree.readLine();
		// rôle ! ! ! !
		boolean reponsOk = proxyGS.estUtilisateurIdentifie(identifiant, mdp);
		if(reponsOk)
		{
			System.out.println("Connexion réussie");
		}else
		{
			System.out.println("Connexion refusée");
			
		}
	}

	public static void main(String[] args) throws Exception {
		proxyGS = (GestionStation) Naming.lookup("rmi://localhost:1099/MaGestionStation");
//		Boolean exit = false;
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//menu
//		while(!exit)
//		{
			menuPrincipal();
			
			switch(valeurChoix)
			{
				case "1":
					menuCreerCompte();
				case "2":
					menuIdentification();
					
			}
//		}
	}
}
