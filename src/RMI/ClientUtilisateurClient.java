package RMI;

import java.rmi.Naming;
import java.util.Scanner;

import metier.GestionStation;

public class ClientUtilisateurClient {

	public static void main(String[] args) throws Exception {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/MaGestionStation");
		Scanner sc = new Scanner(System.in);
		Boolean exit = false;
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int val;
		String nom;
		String prenom;
		String telephone;
		int nbre;
		//menu
		while(!exit)
		{
			System.out.println("------------ Bienvenue ------------");
			System.out.println("--1) Créer compte Client");
			System.out.println("--2) Emprunter un vélo");
			System.out.println("--3) Ramener un vélo");
			System.out.println("--4) Quitter");
			System.out.println("------------ Fin ------------");
			
			val = sc.nextInt();
			
			switch(val)
			{
			case 1:
				System.out.println("Veuillez entrer votre nom : ");
				nom = sc.next();
				System.out.println("Veuillez entrer votre prenom : ");
				prenom = sc.next();
				System.out.println("Veuillez entrer votre telephone : ");
				telephone = sc.next();
				System.out.println("Veuillez entrer votre nom : ");
				nom = sc.next();
				System.out.println("Veuillez entrer la valeur : ");
				nbre = sc.nextInt();
				proxy.creerCompte(cpte, nbre);
				break;
			case 2:
				System.out.println("Veuillez entrer le nom du compte : ");
				cpte = sc.next();
				System.out.println("Veuillez entrer la valeur : ");
				nbre = sc.nextInt();
				proxy.ajouter(cpte, nbre);
				System.out.println("Position du compte de " + cpte + " : "+proxy.position(cpte));
				break;
			case 3:
				System.out.println("Veuillez entrer le nom du compte : ");
				cpte = sc.next();
				System.out.println("Veuillez entrer la valeur : ");
				nbre = sc.nextInt();
				proxy.retirer(cpte, nbre);
				System.out.println("Position du compte de " + cpte + " : "+proxy.position(cpte));
				break;
			case 4:
				exit = true;
				break;
			}
		}
	}

}
