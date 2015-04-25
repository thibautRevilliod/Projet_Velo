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
		System.out.println("--1) Créer compte");
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
		System.out.println("Utilisateur créé");
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
			System.out.println("Connexion réussie");
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
			System.out.println("Connexion refusée");
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
		System.out.println("--1) Mettre un vélo en atelier de réparation");
		System.out.println("--2) Déposer un vélo de l'atelier de réparation");
		System.out.println("--3) Se déconnecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				System.out.println("Veuillez entrer l'id du vélo à retirer pour l'atelier :");
				idVelo = Integer.parseInt(entree.readLine());
				// retourne l'id du vélo sinon rien (null ??)
				// mettre à jour le statut du vélo en réparation
				veloADeposer = proxyGS.emprunterVeloAdministrateur(identifiant, idVelo, idStation);
				if(veloADeposer == -1){ // ou null à voir
					System.out.println("Erreur 10600 : Le vélo n'est pas disponible.");
				}else
				{
					System.out.println("Veuillez prendre le vélo.");
				}
				System.out.print("Déconnection");
				pause(5);
				menuPrincipal();
				break;
			case "2":
				System.out.println("Veuillez entrer l'id du vélo réparé à déposer :");
				idVelo = Integer.parseInt(entree.readLine());
				// retourne si place disponible l'id vélo
				// sinon retourne le nom de la station la plus proche, sa latitude et sa longitude
				// mettre à jour le statut du vélo disponible
				veloADeposer = proxyGS.deposerVeloAdministrateur(identifiant, idVelo, idStation);
				if (veloADeposer == -1){
					Station stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de place disponible dans cette station.");
					System.out.println("Veuillez aller à la station " + stationLaPlusProche.getIdStation() + " qui dispose de place : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
				}else
				{
					System.out.println("Veuillez déposer le vélo " + veloADeposer);
				}
				System.out.print("Déconnection");
				pause(5);
				menuPrincipal();
				break;
			case "3":
				System.out.print("Déconnection");
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
		System.out.println("--1) Gérer les notifications");
		System.out.println("--2) Emprunter des Vélos");
		System.out.println("--3) Déposer des vélos");
		System.out.println("--4) Se déconnecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				menuOperateurNotification(identifiant, mdp);
				break;
			case "2":
				// retourne les ids de vélo si disponible
				// sinon => la notification à planté
				// ne pas oublier de stocker la date et heure de l'emprunt !
				lesIdVelo = proxyGS.emprunterVeloOperateur(identifiant, idStation);
				if (lesIdVelo.length == 0){ // ou null à voir
					System.out.println("Erreur 10200 : Station source définit par la notification incorrecte.");
					System.out.print("Déconnection");
					pause(10);
					menuPrincipal();
				}else{
					System.out.println("Liste des vélos à retirer :");
					for(int i = 0; i < lesIdVelo.length; i++){
						System.out.println("  " + lesIdVelo[i]);
						pause(3);
					}
					System.out.print("Déconnection");
					pause(5);
					menuPrincipal();
				}
				break;
			case "3":
				// retourne les ids de vélo à déposer si place disponible !
				// sinon => la notification à planté 
				//la méthode deposerVeloOperateur doit retourner aucun idVélo si pas place
				// Il faut stocker la liste des vélo empruntés par personne à un instant T
				lesIdVelo = proxyGS.deposerVeloOperateur(identifiant, idStation);
				if(lesIdVelo.length == 0){
					System.out.println("Erreur 10400 : Station destination définit par la notification incorrecte.");
					System.out.print("Déconnection");
					pause(10);
					menuPrincipal();
				}else{
					System.out.println("Veuillez déposer les vélos : ");
					for(int i = 0; i < lesIdVelo.length; i++){
						System.out.println("  " + lesIdVelo[i]);
						pause(3);
					}
					System.out.print("Déconnection");
					pause(5);
					menuPrincipal();
				}
				break;
			case "4":
				System.out.print("Déconnection");
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
		
		// return [0] = nbreVelos; [1] = stationSaturée; [2] = stationPénurie;
		String detailNotification[] = detailNotificationStation();
		
		System.out.println("Veuillez transférer " + detailNotification[0] + " vélos de la station saturée " + detailNotification[1] + " à la station en pénurie " + detailNotification[2]);
		System.out.println("Veuillez valider par 'ok' dès que l'action est terminée : ");
		String actionOK = entree.readLine();
		while (!actionOK.equals("oui"))
		{
			System.out.println("Veuillez valider par 'ok' dès que l'action est terminée : ");
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
		System.out.println("--1) Emprunter un vélo");
		System.out.println("--2) Ramener un Vélo");
		System.out.println("--3) Se déconnecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				// retourne l'id du vélo si disponible sinon retourne le nom de la station, 
				// la latitude, et la longitude avec des vélos disponible
				// ne pas oublier de stocker la date et heure de l'emprunt !
				idVeloEmprunteClient = proxyGS.emprunterVeloClient(pidUtilisateur, idStation);
				if (idVeloEmprunteClient != -1){
					System.out.println("Veuillez prendre le vélo " + idVeloEmprunteClient);
					System.out.print("Déconnection");
					pause(10);
					menuPrincipal();
				}else{
					stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de vélo disponible dans cette station.");
					System.out.println("Veuillez aller à la station " + stationLaPlusProche.getIdStation() + " qui dispose de vélo : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("Déconnection");
					pause(15);
					menuPrincipal();
				}
				break;
			case "2":
				// retourne l'id du vélo s'il y a de la place dans la station, sinon retourne le nom 
				//de la station, la longitude, et la latitude de la plus proche station qui a 
				//des places
				// on doit pouvoir récupérer d'un id utilisateur le vélo qu'il a emprunté.
				idVeloDeposeClient = proxyGS.ramenerVeloClient(pidUtilisateur, idStation);
				if (idVeloDeposeClient.length != 1){
					System.out.println("Vous pouvez déposer le vélo " + result[0]);
					// a développer côter gestionStation ! (optionnel)
						System.out.println("Voulez-vous un reçu ? ");
						System.out.println("--1) Oui");
						System.out.println("--2) Non");
						System.out.println("----");
						valeurChoix = entree.readLine();
						if(valeurChoix=="1"){
							//sous forme [0] = Jours; [1] = Heures; [2] = minutes; [3] = Prix
							int recuDureePrix[] = dureePrixEmpruntVeloClient(pidUtilisateur, idStation); 
							System.out.println("Votre reçu : ");
							System.out.println("  Duree : " + recuDureePrix[0] + " Jours " + recuDureePrix[1] + " Heures " + recuDureePrix[2] + "Minutes");
							System.out.println("  Prix : " + recuDureePrix[3]);
						}
					System.out.println("Merci d'avoir utilisé les services de VeloRMI.");
					System.out.print("Déconnection");
					pause(10);
					menuPrincipal();
				}else{
					stationLaPlusProche = proxyGS.chercherStationLaPlusProche(Station.getStation(idStation));
					System.out.println("Plus de place disponible dans cette station.");
					System.out.println("Veuillez aller à la station " + stationLaPlusProche.getIdStation() + " qui dispose de place : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("Déconnection");
					pause(15);
					menuPrincipal();
				}
				break;
			case "3":
				System.out.print("Déconnection");
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
		
		//creation de vélos 
		//(pas d'id en paramètre du constructeur de vélo car généré automatiquemnt)
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		proxyGS.ajouterVeloStation(new Velo(), Station.getStation(idStation));
		
		// creation d'instances utilisateurs pour les tests
		initialisationInstances();
		
		menuPrincipal();
	}

	
}
