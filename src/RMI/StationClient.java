package RMI;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

import metier.GestionStation;

public class StationClient {
	public static GestionStation proxyGS;
	public static int valeurChoix;
	public static Scanner sc = new Scanner(System.in);
	
	public static void menuPrincipal(){
		System.out.println("------------ Bienvenue ------------");
		System.out.println("--1) Créer compte");
		System.out.println("--2) S'identifier");
		System.out.println("------------ Fin ------------");
		valeurChoix = sc.nextInt();
	}
	
	public static void menuCreerCompte() throws RemoteException{
		String nom;
		String prenom;
		String telephone;
		String adresseMail;
		String adressePostale;
		String mdp;
		int identifiantUtilisateur = 0;
		
		System.out.println("Veuillez choisir le type de compte : ");
		System.out.println("--1) Client");
		System.out.println("--2) Operateur");
		System.out.println("--3) Administrateur");
		System.out.println("--4) Quitter");
		valeurChoix = sc.nextInt();
		
		System.out.println("Veuillez entrer votre nom : ");
		nom = sc.next();
		System.out.println("Veuillez entrer votre prenom : ");
		prenom = sc.nextLine();
		System.out.println("Veuillez entrer votre telephone : ");
		telephone = sc.nextLine();
		System.out.println("Veuillez entrer votre email : ");
		adresseMail = sc.nextLine();
		System.out.println("Veuillez entrer votre adresse postale : ");
		adressePostale = sc.nextLine();
		System.out.println("Veuillez entrer votre mot de passe : ");
		mdp = sc.nextLine();
		
		switch(valeurChoix)
		{
		case 1:
			identifiantUtilisateur = proxyGS.creerClient(nom, prenom, mdp, telephone, adresseMail, adressePostale);
			System.out.println("Utilisateur créé");
			System.out.println("  Identifiant : " + identifiantUtilisateur);
			System.out.println("  Mot de passe : " + mdp);	
			break;
		case 2:
			identifiantUtilisateur = proxyGS.creerOperateur(nom, prenom, mdp, telephone, adresseMail, adressePostale);
			System.out.println("Utilisateur créé");
			System.out.println("  Identifiant : " + identifiantUtilisateur);
			System.out.println("  Mot de passe : " + mdp);	
			break;
		case 3:
			identifiantUtilisateur = proxyGS.creerAdministrateur(nom, prenom, mdp, telephone, adresseMail, adressePostale);
			System.out.println("Utilisateur créé");
			System.out.println("  Identifiant : " + identifiantUtilisateur);
			System.out.println("  Mot de passe : " + mdp);	
			break;
		case 4:
			break;
		}	
	}
	
	public static void menuIdentification() throws RemoteException
	{
		int identifiant;
		String mdp;
		System.out.println("Veuillez entrer votre identifiant : ");
		identifiant = sc.nextInt();
		System.out.println("Veuillez entrer votre mot de passe : ");
		mdp = sc.next();
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
		Boolean exit = false;
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int nbre;
		//menu
		while(!exit)
		{
			menuPrincipal();
			
			switch(valeurChoix)
			{
				case 1:
					menuCreerCompte();
				case 2:
					menuIdentification();
//					System.out.println("Veuillez entrer votre nom : ");
//					nom = sc.next();
//					System.out.println("Veuillez entrer votre prenom : ");
//					prenom = sc.next();
			}
			
			System.out.println("--2) Emprunter un vélo");
			System.out.println("--3) Ramener un vélo");
			System.out.println("--4) Quitter");
			
			
			
			
//			switch(val)
//			{
//			case 1:
//				System.out.println("Veuillez entrer votre nom : ");
//				nom = sc.next();
//				System.out.println("Veuillez entrer votre prenom : ");
//				prenom = sc.next();
//				System.out.println("Veuillez entrer votre telephone : ");
//				telephone = sc.next();
//				System.out.println("Veuillez entrer votre email : ");
//				adresseMail = sc.next();
//				System.out.println("Veuillez entrer votre adresse postale : ");
//				adressePostale = sc.next();
//				proxyGS.creerUtilisateur(nom, prenom, telephone, adresseMail, ;
//				break;
//			case 2:
//				System.out.println("Veuillez entrer le nom du compte : ");
//				cpte = sc.next();
//				System.out.println("Veuillez entrer la valeur : ");
//				nbre = sc.nextInt();
//				proxy.ajouter(cpte, nbre);
//				System.out.println("Position du compte de " + cpte + " : "+proxy.position(cpte));
//				break;
//			case 3:
//				System.out.println("Veuillez entrer le nom du compte : ");
//				cpte = sc.next();
//				System.out.println("Veuillez entrer la valeur : ");
//				nbre = sc.nextInt();
//				proxy.retirer(cpte, nbre);
//				System.out.println("Position du compte de " + cpte + " : "+proxy.position(cpte));
//				break;
//			case 4:
//				exit = true;
//				break;
//			}
		}
	}
}
