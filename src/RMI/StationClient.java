package RMI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Scanner;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import metier.CarteAcces.Role;
import metier.GestionStation;
import metier.Position;
import metier.Station;
import metier.Velo;

public class StationClient {
	public static GestionStation proxyGS;
	public static String valeurChoix;
	public static int idStation;
	public static BufferedReader entree = new BufferedReader(new InputStreamReader(System.in)); 
	
	
	public static void menuPrincipal() throws IOException, InterruptedException{
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("------------ Bienvenue ------------");
		System.out.println("--1) Cr�er compte");
		System.out.println("--2) S'identifier");
		System.out.println("-----------------------------------");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				menuCreerCompte();
				menuPrincipal();
				break;
			case "2":
				menuIdentification();
				break;
			default :
				menuPrincipal();
		}
	}
	
	public static void menuCreerCompte() throws IOException, InterruptedException{
		String tab[];
		int identifiantUtilisateur = 0;
		
		System.out.println("--------- Creation compte ----------");
		System.out.println("Veuillez choisir le type de compte : ");
		System.out.println("--1) Client");
		System.out.println("--2) Operateur");
		System.out.println("--3) Administrateur");
		System.out.println("--4) Quitter");
		System.out.println("----");
		valeurChoix = entree.readLine();
			
		switch(valeurChoix)
		{
		case "1":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerClient(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
			messageUtilisateurCree(identifiantUtilisateur, tab[5]);	
			break;
		case "2":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerOperateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
			messageUtilisateurCree(identifiantUtilisateur, tab[5]);	
			break;
		case "3":
			tab = demandeInfoCreeUtilisateur();
			identifiantUtilisateur = proxyGS.creerAdministrateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
			messageUtilisateurCree(identifiantUtilisateur, tab[5]);
			break;
		case "4":
			menuPrincipal();
		}	
	}
	
	public static void messageUtilisateurCree(int pidentifiantUtilisateur, String pmdp) throws InterruptedException{
		System.out.println("-------------------------------------");
		System.out.println("Utilisateur cr��");
		System.out.println("  Identifiant : " + pidentifiantUtilisateur);
		System.out.println("  Mot de passe : " + pmdp);	
		System.out.println("-------------------------------------");
		pause(10);
	}
	
	public static String[] demandeInfoCreeUtilisateur() throws IOException{	
		String result[] = new String[6];
		
		System.out.println("Veuillez entrer votre nom : ");
		result[0] = entree.readLine();
		System.out.println("Veuillez entrer votre prenom : ");
		result[1] = entree.readLine();
		System.out.println("Veuillez entrer votre telephone : ");
		result[3] = entree.readLine();
		System.out.println("Veuillez entrer votre email : ");
		result[4] = entree.readLine();
		System.out.println("Veuillez entrer votre adresse postale : ");
		result[5] = entree.readLine();
		System.out.println("Veuillez entrer votre mot de passe : ");
		result[2] = entree.readLine();
		
		return result;
	}
	
	public static void menuIdentification() throws NumberFormatException, IOException, InterruptedException
	{
		int identifiant;
		String mdp;
		String role[];
		String choixRole;
		String currentRole;
		
		System.out.println("----------- Identification ----------");
		System.out.println("Veuillez entrer votre identifiant : ");
		identifiant = Integer.valueOf(entree.readLine());
		System.out.println("Veuillez entrer votre mot de passe : ");
		mdp = entree.readLine();
		System.out.println("-------------------------------------");
		
		boolean reponsOk = proxyGS.estUtilisateurIdentifie(identifiant, mdp);
		
		if(reponsOk)
		{
			System.out.println("Connexion r�ussie");
			role = proxyGS.getRoleUtilisateur(identifiant); // retourne un tableau de string de role
			if(role.length > 1){
				System.out.println("Veuillez selectionner votre role  : ");
				for(int i = 0; i < role.length; i++){
					System.out.println("--" + i + ") " + role[i]);
				}
				System.out.println("----");
				choixRole = entree.readLine();
				currentRole = role[Integer.parseInt(choixRole)];
			}else{
				currentRole = role[0];
			}
			
			switch(currentRole)
			{
				case "Administrateur":
					menuAdministrateur(identifiant, mdp);
					break;
				case "Operateur":
					menuOperateur(identifiant, mdp);
					break;
				case "Client":
					menuClient(identifiant, mdp);
					break;
			}
			
			mdp = entree.readLine();
			pause(3);
		}else
		{
			System.out.println("Connexion refus�e");
			pause(3);
			menuPrincipal();
		}
	}
	
	private static void menuAdministrateur(int identifiant, String mdp) throws IOException, InterruptedException {
		// TODO Auto-generated method stub	
		int idVelo;
		int veloADeposer;
		
		System.out.println("--------- Menu Administrateur ----------");
		System.out.println("Que voulez-vous faire ?");
		System.out.println("--1) Mettre un v�lo en atelier de r�paration");
		System.out.println("--2) D�poser un v�lo de l'atelier de r�paration");
		System.out.println("--3) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				System.out.println("Veuillez entrer l'id du v�lo � retirer pour l'atelier :");
				idVelo = Integer.parseInt(entree.readLine());
				// retourne l'id du v�lo sinon rien (null ??)
				// mettre � jour le statut du v�lo en r�paration
				veloADeposer = proxyGS.emprunterVeloAdministrateur(identifiant, idVelo, idStation);
				if(veloADeposer == -1){ // ou null � voir
					System.out.println("Erreur 10600 : Le v�lo n'est pas disponible.");
				}else
				{
					System.out.println("Veuillez prendre le v�lo.");
				}
				System.out.print("D�connection");
				pause(5);
				menuPrincipal();
				break;
			case "2":
				System.out.println("Veuillez entrer l'id du v�lo r�par� � d�poser :");
				idVelo = Integer.parseInt(entree.readLine());
				// retourne si place disponible l'id v�lo
				// sinon retourne le nom de la station la plus proche, sa latitude et sa longitude
				// mettre � jour le statut du v�lo disponible
				veloADeposer = proxyGS.deposerVeloAdministrateur(identifiant, idVelo, idStation);
				if (veloADeposer == -1){
					Station stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de place disponible dans cette station.");
					System.out.println("Veuillez aller � la station " + stationLaPlusProche.getIdStation() + " qui dispose de place : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
				}else
				{
					System.out.println("Veuillez d�poser le v�lo " + veloADeposer);
				}
				System.out.print("D�connection");
				pause(5);
				menuPrincipal();
				break;
			case "3":
				System.out.print("D�connection");
				pause(3);
				menuPrincipal();
				break;
			default :
				menuAdministrateur(identifiant, mdp);
		}
	}
	
	private static void menuOperateur(int identifiant, String mdp) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int[] lesIdVelo;
		
		System.out.println("--------- Menu Operateur ----------");
		System.out.println("Que voulez-vous faire ?");
		System.out.println("--1) G�rer les notifications");
		System.out.println("--2) Emprunter des V�los");
		System.out.println("--3) D�poser des v�los");
		System.out.println("--4) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				menuOperateurNotification(identifiant, mdp);
				break;
			case "2":
				// retourne les ids de v�lo si disponible
				// sinon => la notification � plant�
				// ne pas oublier de stocker la date et heure de l'emprunt !
				lesIdVelo = proxyGS.emprunterVeloOperateur(identifiant, idStation);
				if (lesIdVelo.length == 0){ // ou null � voir
					System.out.println("Erreur 10200 : Station source d�finit par la notification incorrecte.");
					System.out.print("D�connection");
					pause(10);
					menuPrincipal();
				}else{
					System.out.println("Liste des v�los � retirer :");
					for(int i = 0; i < lesIdVelo.length; i++){
						System.out.println("  " + lesIdVelo[i]);
						pause(3);
					}
					System.out.print("D�connection");
					pause(5);
					menuPrincipal();
				}
				break;
			case "3":
				// retourne les ids de v�lo � d�poser si place disponible !
				// sinon => la notification � plant� 
				//la m�thode deposerVeloOperateur doit retourner aucun idV�lo si pas place
				// Il faut stocker la liste des v�lo emprunt�s par personne � un instant T
				lesIdVelo = proxyGS.deposerVeloOperateur(identifiant, idStation);
				if(lesIdVelo.length == 0){
					System.out.println("Erreur 10400 : Station destination d�finit par la notification incorrecte.");
					System.out.print("D�connection");
					pause(10);
					menuPrincipal();
				}else{
					System.out.println("Veuillez d�poser les v�los : ");
					for(int i = 0; i < lesIdVelo.length; i++){
						System.out.println("  " + lesIdVelo[i]);
						pause(3);
					}
					System.out.print("D�connection");
					pause(5);
					menuPrincipal();
				}
				break;
			case "4":
				System.out.print("D�connection");
				pause(3);
				menuPrincipal();
				break;
			default :
				menuOperateur(identifiant, mdp);
		}
	}
	
	private static void menuOperateurNotification(int identifiant, String mdp) {
		boolean notification = false;
		
		System.out.println("En attente de notifications...");
		while(!notification){
			//retourne false s'il y pas de notification et true s'il y en a.
			notification = estnotificationStation();
			pause(3);
		}
		
		// return [0] = nbreVelos; [1] = stationSatur�e; [2] = stationP�nurie;
		String detailNotification[] = detailNotificationStation();
		
		System.out.println("Veuillez transf�rer " + detailNotification[0] + " v�los de la station satur�e " + detailNotification[1] + " � la station en p�nurie " + detailNotification[2]);
		System.out.println("Veuillez valider par 'ok' d�s que l'action est termin�e : ");
		String actionOK = entree.readLine();
		while (!actionOK.equals("oui"))
		{
			System.out.println("Veuillez valider par 'ok' d�s que l'action est termin�e : ");
			String actionOK = entree.readLine();
		}
		proxyGS.notificationOK();
		menuOperateurNotification(identifiant,mdp);
	}

	public static void menuClient(int pidUtilisateur, String pmdpUtilisateur) throws IOException, InterruptedException{
		int idVeloEmprunteClient;
		int idVeloDeposeClient;
		Station stationLaPlusProche;
		
		System.out.println("--------- Menu Client ----------");
		System.out.println("Que voulez-vous faire ?");
		System.out.println("--1) Emprunter un v�lo");
		System.out.println("--2) Ramener un V�lo");
		System.out.println("--3) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				// retourne l'id du v�lo si disponible sinon retourne le nom de la station, 
				// la latitude, et la longitude avec des v�los disponible
				// ne pas oublier de stocker la date et heure de l'emprunt !
				idVeloEmprunteClient = proxyGS.emprunterVeloClient(pidUtilisateur, idStation);
				if (idVeloEmprunteClient != -1){
					System.out.println("Veuillez prendre le v�lo " + idVeloEmprunteClient);
					System.out.print("D�connection");
					pause(10);
					menuPrincipal();
				}else{
					stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de v�lo disponible dans cette station.");
					System.out.println("Veuillez aller � la station " + stationLaPlusProche.getIdStation() + " qui dispose de v�lo : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("D�connection");
					pause(15);
					menuPrincipal();
				}
				break;
			case "2":
				// retourne l'id du v�lo s'il y a de la place dans la station, sinon retourne le nom 
				//de la station, la longitude, et la latitude de la plus proche station qui a 
				//des places
				// on doit pouvoir r�cup�rer d'un id utilisateur le v�lo qu'il a emprunt�.
				idVeloDeposeClient = proxyGS.ramenerVeloClient(pidUtilisateur, idStation);
				if (idVeloDeposeClient.length != 1){
					System.out.println("Vous pouvez d�poser le v�lo " + result[0]);
					// a d�velopper c�ter gestionStation ! (optionnel)
						System.out.println("Voulez-vous un re�u ? ");
						System.out.println("--1) Oui");
						System.out.println("--2) Non");
						System.out.println("----");
						valeurChoix = entree.readLine();
						if(valeurChoix=="1"){
							//sous forme [0] = Jours; [1] = Heures; [2] = minutes; [3] = Prix
							int recuDureePrix[] = dureePrixEmpruntVeloClient(pidUtilisateur, idStation); 
							System.out.println("Votre re�u : ");
							System.out.println("  Duree : " + recuDureePrix[0] + " Jours " + recuDureePrix[1] + " Heures " + recuDureePrix[2] + "Minutes");
							System.out.println("  Prix : " + recuDureePrix[3]);
						}
					System.out.println("Merci d'avoir utilis� les services de VeloRMI.");
					System.out.print("D�connection");
					pause(10);
					menuPrincipal();
				}else{
					stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de place disponible dans cette station.");
					System.out.println("Veuillez aller � la station " + stationLaPlusProche.getIdStation() + " qui dispose de place : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("D�connection");
					pause(15);
					menuPrincipal();
				}
				break;
			case "3":
				System.out.print("D�connection");
				pause(3);
				menuPrincipal();
				break;
			default :
				menuClient(pidUtilisateur, pmdpUtilisateur);
		}
	}

	public static void pause(int n) throws InterruptedException{
		for(int i = 1; i<n+1; i++){
			Thread.sleep(1000);
			System.out.print(".");
		}
		System.out.println("");	
	}

	private static void initialisationInstances() throws RemoteException {
		// TODO Auto-generated method stub
		
		//creation Client
		String tab[] = new String[6];
		tab[0] = "nomClient";
		tab[1] = "prenomClient";
		tab[2] = "mdpClient";
		tab[3] = "telClient";
		tab[4] = "adrClient@email.com";
		tab[5] = "adrClient";
		
		proxyGS.creerClient(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
		
		//creation Operateur
		tab = new String[6];
		tab[0] = "nomOperateur";
		tab[1] = "prenomOperateur";
		tab[2] = "mdpOperateur";
		tab[3] = "telOperateur";
		tab[4] = "adrOperateur@email.com";
		tab[5] = "adrOperateur";
		
		proxyGS.creerClient(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
		
		//creation Administrateur
		tab = new String[6];
		tab[0] = "nomAdministrateur";
		tab[1] = "prenomAdministrateur";
		tab[2] = "mdpAdministrateur";
		tab[3] = "telAdministrateur";
		tab[4] = "adrAdministrateur@email.com";
		tab[5] = "adrAdministrateur";
		
		proxyGS.creerClient(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5]);
	}
	
	public static void main(String[] args) throws Exception {
		proxyGS = (GestionStation) Naming.lookup("rmi://localhost:1099/MaGestionStation");
			
		//creation de la station
		String nomStation = "Station1";
		double longitude = 1.2222;
		double latitude = 5.4444;
		int capacite = 30;
		idStation = proxyGS.creerStation(nomStation, longitude, latitude, capacite);
		
		//creation de v�los 
		//(pas d'id en param�tre du constructeur de v�lo car g�n�r� automatiquemnt)
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		
		// creation d'instances utilisateurs pour les tests
		initialisationInstances();
		
		menuPrincipal();
	}

	
}
