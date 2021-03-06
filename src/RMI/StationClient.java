package RMI;
//TODO supprimer commentaire it.remove
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import metier.CarteAcces.Role;
import metier.Administrateur;
import metier.Client;
import metier.GestionStation;
import metier.GestionStationNotifImpl;
import metier.Notifications;
import metier.Operateur;
import metier.Station;
import metier.Utilisateur;
import metier.Velo;
import metier.Velo.Etat;
//TODO g�rer retour depos v�lo en r�paration pour admin 
public class StationClient {
	private static final int TEMPS_PAUSE = 2;
	private static GestionStation proxyGS;
	private static String valeurChoix;
	private static int idStation;
	private static boolean stationMaitre;
	private static BufferedReader entree = new BufferedReader(new InputStreamReader(System.in)); 
	
	public static void enTeteMenu() throws IOException, InterruptedException{
		if(!stationMaitre){
			System.out.println("*Nombre de velo disponible : "+ 
					(proxyGS.getVelosLibresStation(idStation).length-1)
					+" ");
		}
	}
	
	public static void menuPrincipal() throws IOException, InterruptedException {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		if(!stationMaitre){
			System.out.println("------------ Bienvenue dans la station Cliente n�"+ idStation +" ------------");
		}else{
			System.out.println("------------ Bienvenue dans la station Ma�tre ------------");
		}
		
		enTeteMenu();
		
			System.out.println("--1) Cr�er compte");
			System.out.println("--2) S'identifier");
			System.out.println("--3) D�poser un v�lo");
			System.out.println("-----------------------------------");
			valeurChoix = entree.readLine();
			
			switch(valeurChoix)
			{
				case "1":
					menuCreerComptePrincipal();
					menuPrincipal();
					break;
				case "2":
					menuIdentification();
					break;
				case "3":
					menuDeposerVelo();
					menuPrincipal();
					break;
				default :
					menuPrincipal();
			}

	}
	
	private static void menuDeposerVelo() throws IOException, InterruptedException {
		int stationDepot;
		int idUtilisateur = 0;
		Station stationLaPlusProche;
		boolean verificationFormat = true;
		
		// retourne l'id du v�lo s'il y a de la place dans la station, sinon retourne le nom 
		//de la station, la longitude, et la latitude de la plus proche station qui a 
		//des places
		do{
			verificationFormat = true;
			try{
				System.out.println("Veuillez entrer votre identifiant (action d�poser le v�lo dans la borne) : ");
				idUtilisateur = Integer.parseInt(entree.readLine());
			}catch(Exception e){
				System.out.println("Saisie incorrecte");
				verificationFormat = false;
			}
		}while(!verificationFormat);
		
		if(!proxyGS.hasCompteUtilisateur(idUtilisateur))
		{
			System.out.println("Cet identifiant est incorrect.");
			pause(TEMPS_PAUSE);
			menuPrincipal();
		}
		else
		{				
			if(!proxyGS.hasUtilisateurEmprunteVelos(idUtilisateur))
			{
				System.out.println("Vous n'avez pas emprunt� de v�lo.");
				pause(TEMPS_PAUSE);
				menuPrincipal();
			}else
			{
				stationDepot = proxyGS.deposerVelos(idUtilisateur, idStation);
				if ((stationDepot  == idStation)){
					System.out.println("Vous pouvez d�poser le(s) v�lo(s) ");
					// a d�velopper c�ter gestionStation ! (optionnel)
						System.out.println("Voulez-vous un re�u ? ");
						System.out.println("--1) Oui");
						System.out.println("--2) Non");
						System.out.println("----");
						valeurChoix = entree.readLine();
						if(valeurChoix=="1"){
							//sous forme [0] = Jours; [1] = Heures; [2] = minutes; [3] = Prix
							int recuDureePrix[] = proxyGS.dureePrixEmpruntVeloClient(idUtilisateur, idStation); 
							System.out.println("Votre re�u : ");
							System.out.println("  Duree : " + recuDureePrix[0] + " Jours " + recuDureePrix[1] + " Heures " + recuDureePrix[2] + "Minutes");
							System.out.println("  Prix : " + recuDureePrix[3]);
						}
					System.out.println("Merci d'avoir utilis� les services de VeloRMI.");
					System.out.print("D�connexion");
					pause(TEMPS_PAUSE);
				}else if(stationDepot > 0)
				{
					stationLaPlusProche = proxyGS.getStation(stationDepot);
					System.out.println("Plus de place disponible dans cette station.");
					System.out.println("Veuillez aller � la station " + stationDepot + " qui dispose de place : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());
					System.out.println("D�connexion");
					pause(TEMPS_PAUSE);
				}
				else
				{
					System.out.println("Il n'y a pas de station disponible avec suffisamment de v�los.");
					System.out.println("D�connexion");
					pause(TEMPS_PAUSE);
				}
			}
		}
	}

	public static void menuCreerComptePrincipal() throws IOException, InterruptedException{
		String tab[];
		int identifiantUtilisateur = 0;
		boolean verificationFormat;
		String mdp;
		Integer identifiant = null;
		boolean reponsOk;
		
		System.out.println("--------- Creation compte ----------");
		System.out.println("Avez-vous d�j� un compte administrateur ou op�rateur ? (o/n)");
		valeurChoix = entree.readLine();
		
		switch (valeurChoix) {
			case "o":
				do{
					verificationFormat = true;
					try{
						System.out.println("Veuillez entrer votre identifiant : ");
						identifiant = Integer.valueOf(entree.readLine());
					}catch(Exception e){
						System.out.println("Saisie incorrecte");
						verificationFormat = false;
					}
				}while(!verificationFormat);
				
				System.out.println("Veuillez entrer votre mot de passe : ");
				mdp = entree.readLine();
				System.out.println("---");
				
				reponsOk = proxyGS.estUtilisateurIdentifie(identifiant, mdp);
				
				if(reponsOk)
				{
					proxyGS.ajouterRoleUtilisateur(identifiant, Role.Client);
					System.out.println("R�le ajout�");
					pause(TEMPS_PAUSE);
				}
				
				menuPrincipal();
				break;
			case "n":
				tab = demandeInfoCreeUtilisateur();	
				tab[6] = "Client";
				identifiantUtilisateur = proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], tab[6]);
				messageUtilisateurCree(identifiantUtilisateur, tab[2]);	
				break;
			default:
				menuCreerComptePrincipal();
				
		}
	}
	
	public static void menuCreerCompteAdministrateur() throws IOException, InterruptedException {
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
		case "1": case "2": case "3":
			tab = demandeInfoCreeUtilisateur();	
			switch (valeurChoix) {
			case "1":
				tab[6] = "Client";
				break;
			case "2":
				tab[6] = "Operateur";
				break;
			case "3":
				tab[6] = "Administrateur";
				break;
			}
			identifiantUtilisateur = proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], tab[6]);
			messageUtilisateurCree(identifiantUtilisateur, tab[5]);	
			break;
		case "4":
			menuPrincipal();
			break;
		default:
			menuPrincipal();
			break;
		}
	}
	
	public static void messageUtilisateurCree(int pidentifiantUtilisateur, String pmdp) throws InterruptedException{
		System.out.println("-------------------------------------");
		System.out.println("Utilisateur cr��");
		System.out.println("  Identifiant : " + pidentifiantUtilisateur);
		System.out.println("  Mot de passe : " + pmdp);	
		System.out.println("-------------------------------------");
		pause(TEMPS_PAUSE);
	}
	
	public static String[] demandeInfoCreeUtilisateur() throws IOException{	
		String result[] = new String[7];
		
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
	
	public static void menuIdentification() throws IOException, InterruptedException
	{
		int identifiant = 0;
		String mdp;
		String role[];
		String choixRole;
		String currentRole = null;

		boolean verificationFormat = true;
		
		System.out.println("----------- Identification ----------");
		
		do{
			verificationFormat = true;
			try{
				System.out.println("Veuillez entrer votre identifiant : ");
				identifiant = Integer.valueOf(entree.readLine());
			}catch(Exception e){
				System.out.println("Saisie incorrecte");
				verificationFormat = false;
			}
		}while(!verificationFormat);
		
		System.out.println("Veuillez entrer votre mot de passe : ");
		mdp = entree.readLine();
		System.out.println("-------------------------------------");
		
		boolean reponsOk = proxyGS.estUtilisateurIdentifie(identifiant, mdp);
		
		if(reponsOk)
		{
			System.out.println("Connexion r�ussie");
			role = proxyGS.getRoleUtilisateur(identifiant); // retourne un tableau de string de role
			if(role.length > 1){
				do{
					verificationFormat = true;
					try{
						System.out.println("Veuillez selectionner votre role  : ");
						for(int i = 0; i < role.length; i++){
							System.out.println("--" + i + ") " + role[i]);
						}
						System.out.println("----");
						choixRole = entree.readLine();
						currentRole = role[Integer.parseInt(choixRole)];
					}catch(Exception e){
						System.out.println("Saisie incorrecte");
						verificationFormat = false;
					}
				}while(!verificationFormat);
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
			pause(TEMPS_PAUSE);
		}else
		{
			System.out.println("Connexion refus�e");
			pause(TEMPS_PAUSE);
			menuPrincipal();
		}
	}
	
	private static void menuAdministrateur(int identifiant, String mdp) throws IOException, InterruptedException {
		int idVelo = 0;
		int[] resultats;
		int idStationVelo = 0;
		boolean verificationFormat = true;
		int idUtilisateur = 0;
		
		System.out.println("--------- Menu Administrateur ----------");
		enTeteMenu();
		System.out.println("Que voulez-vous faire ?");
		System.out.println("--1) Enregistrer un nouveau v�lo");
		System.out.println("--2) Mettre un v�lo en atelier de r�paration");
		System.out.println("--3) Cr�er compte (Administrateur / Op�rateur / Client)");
		System.out.println("--4) D�poser un v�lo de l'atelier de r�paration");
		System.out.println("--5) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				//creation de v�los 
				//(pas d'id en param�tre du constructeur de v�lo car g�n�r� automatiquemnt)
				
				do{
					verificationFormat = true;
					try{
						System.out.print("Veuillez entrer l'id de la station o� vous souhaitez cr�er le v�lo : ");
						idStationVelo = Integer.valueOf(entree.readLine());
					}catch(Exception e){
						System.out.println("Saisie incorrecte");
						verificationFormat  = false;
					}
				}while(!verificationFormat);
				
				boolean resultat = proxyGS.ajouterVeloStation(idStationVelo);
				if(!resultat)
				{
					System.out.println("Plus de place disponible");
					Station stationLaPlusProche = proxyGS.getStation(idStation);
					System.out.println("Veuillez aller � la station " + stationLaPlusProche.getIdStation() + " qui dispose de v�lo : ");
					System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
					System.out.println("  longitude : " + stationLaPlusProche.getPosition().getLongitude());
					System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());
					System.out.println("D�connexion");
					pause(TEMPS_PAUSE);
				}
				menuAdministrateur(identifiant, mdp);
				break;	
			case "2":		
				int[] lesVelos = proxyGS.getVelosLibresStation(idStation);
				if(lesVelos.length != 0)
				{
					System.out.println("***** Les v�los de la station *****");
					for(int i = 0; i < lesVelos.length-1; i++ )
					{
						System.out.println("**V�lo n� " + lesVelos[i]);
					}
					System.out.println("***************");
				
					do{
						verificationFormat = true;
						try{
							System.out.println("Veuillez entrer l'id du v�lo � retirer pour l'atelier :");
							idVelo = Integer.valueOf(entree.readLine());
						}catch(Exception e){
							System.out.println("Saisie incorrecte");
							verificationFormat  = false;
						}
					}while(!verificationFormat);
					
					resultats = proxyGS.emprunterVelos(identifiant, idStation, idVelo);
					switch(resultats[1])
					{
						case 0 :
							System.out.println("Veuillez prendre le v�lo.");
							break;
						case -1 :
							System.err.println("Erreur 10600 : Utilisateur ou Station inexistants.");
							break;
						case -2 :
							System.err.println("Erreur 10601 : Utilisateur non administrateur.");
							break;
						case -3 :
							System.err.println("Erreur 10602 : Le v�lo n'est pas disponible.");
							break;
					}
				}else
				{
					System.out.println("Aucun v�lo de disponible dans cette station");
				}
				pause(TEMPS_PAUSE);
				menuAdministrateur(identifiant, mdp);
				break;
			case "3":
				System.out.println("--------- Menu creer Compte ----------");
				System.out.println("--1) Ajout de r�le d'un compte existant");
				System.out.println("--2) Cr�er un nouveau compte");
				System.out.println("--3) Quitter");
				System.out.println("----");
				valeurChoix = entree.readLine();
				switch(valeurChoix)
				{
					case "1":
						do{
							verificationFormat = true;
							try{
								System.out.println("Veuillez renseigner l'identifiant de la personne :");
								idUtilisateur = Integer.parseInt(entree.readLine());
							}catch(Exception e){
								System.out.println("Saisie incorrecte");
								verificationFormat = false;
							}
						}while(!verificationFormat);
						
						System.out.println("Veuillez choisir le type de r�le : ");
						System.out.println("--1) Client");
						System.out.println("--2) Operateur");
						System.out.println("--3) Administrateur");
						System.out.println("----");
						valeurChoix = entree.readLine();
						Role typeRole = null;
						switch(valeurChoix)
						{
							case "1":
								typeRole = Role.Client;
								break;
							case "2":
								typeRole = Role.Operateur;
								break;
							case "3":
								typeRole = Role.Administrateur;
								break;
							default:
								menuAdministrateur(identifiant, mdp);
								break;
						}
						
						proxyGS.ajouterRoleUtilisateur(idUtilisateur, typeRole);
						System.out.println("R�le ajout�");
						menuAdministrateur(identifiant, mdp);
						break;
					case "2":
						menuCreerCompteAdministrateur();
						menuAdministrateur(identifiant, mdp);
						break;
					case "3":
						menuAdministrateur(identifiant, mdp);
						break;
					default:
						menuAdministrateur(identifiant, mdp);
						break;
				}
				break;
			case "5":
				System.out.print("D�connexion");
				pause(TEMPS_PAUSE);
				menuPrincipal();
				break;
			case "4":			
				if(!proxyGS.hasUtilisateurVeloEnReparation(identifiant))
				{
					System.out.println("Vous n'avez pas emprunt� de v�lo en r�paration.");
				}else
				{
					do{
						verificationFormat = true;
						try{
							System.out.println("***** Les v�los que vous avez amen�s en r�paration *****");
							ArrayList<Integer> lesVelosEnReparationAdmin = proxyGS.getIdsVelosEtat(identifiant, Etat.EnReparation);
							for(Integer i : lesVelosEnReparationAdmin)
							{
								System.out.println("**V�lo n� " + i);
							}
							System.out.println("***************");
							System.out.println("Veuillez entrer l'id du v�lo � d�poser provenant de l'atelier :");
							idVelo = Integer.valueOf(entree.readLine());
							if(!lesVelosEnReparationAdmin.contains(idVelo))
							{
								throw new Exception("V�lo non emprunt�");
							}
						}catch(Exception e){
							System.out.println("Saisie incorrecte");
							verificationFormat  = false;
						}
					}while(!verificationFormat);
					int stationDepot;
					stationDepot = proxyGS.deposerVelos(identifiant, idStation, idVelo);
					if ((stationDepot  == idStation)){
						System.out.println("Vous pouvez d�poser le(s) v�lo(s) ");
						// a d�velopper c�ter gestionStation ! (optionnel)
						System.out.println("Merci d'avoir utilis� les services de VeloRMI.");
					}else{
						Station stationLaPlusProche = proxyGS.getStation(stationDepot);
						System.out.println("Plus de place disponible dans cette station.");
						System.out.println("Veuillez aller � la station " + stationDepot + " qui dispose de place : ");
						System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
						System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
						System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());
					}
				}
				pause(TEMPS_PAUSE);
				menuAdministrateur(identifiant, mdp);
				break;
			default :
				menuAdministrateur(identifiant, mdp);
		}
	}
	
	private static void menuOperateur(int identifiant, String mdp) throws IOException, InterruptedException {
		int[] lesIdVelo;
		int nbVelos = 0;
		int stationDepot;
		int resultEmprunterVelos;
		boolean verificationFormat = true;

		System.out.println("--------- Menu Operateur ----------");
		enTeteMenu();
		System.out.println("Que voulez-vous faire ?");
		if(stationMaitre)
		{
			System.out.println("--1) G�rer les notifications");
			
		}
		System.out.println("--2) Emprunter des V�los");
		System.out.println("--3) D�poser des v�los");
		//System.out.println("--4) Mettre un v�lo en atelier de r�paration");
		//System.out.println("--5) D�poser un v�lo de l'atelier de r�paration");
		System.out.println("--6) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				if(stationMaitre)
				{
					menuOperateurNotification(identifiant, mdp);
				}else{
					menuOperateur(identifiant, mdp);
				}
				break;
			case "2":
				//test si l'op�rateur n'a pas d�j� emprunt� des v�los
				if(proxyGS.hasUtilisateurEmprunteVelos(identifiant))
				{
					System.out.println("Vous avez d�j� emprunt� un v�lo.");
					pause(TEMPS_PAUSE);
					menuOperateur(identifiant, mdp);
				}else
				{
					do{
						verificationFormat = true;
						try{
							System.out.println("Veuillez entrer le nombre de v�los � retirer :");
							nbVelos = Integer.parseInt(entree.readLine());
						}catch(Exception e){
							System.out.println("Saisie incorrecte");
							verificationFormat  = false;
						}
					}while(!verificationFormat);
					
	
					// retourne les ids de v�lo si disponible
					// sinon => la notification � plant�
					// ne pas oublier de stocker la date et heure de l'emprunt !
					
					resultEmprunterVelos = proxyGS.isEmprunterVelosPossible(identifiant, idStation, nbVelos, Role.Operateur);
					
					
					switch (resultEmprunterVelos) {
					
					case -1:
						System.err.println("Erreur 10600 : Utilisateur inexistant.");
						break;
					case -2:
						System.err.println("Erreur 10601 : L'utilisateur n'a pas la carte d'acc�s necessaire.");
						break;
					case -3:
						System.err.println("Erreur 10602 : Station inexistante.");
						break;
					case -4:
						System.err.println("Erreur 10603 : Le nombre de v�los demand�s est sup�rieur � la capacit� de la station.");
						break;
					case -6:
						System.err.println("Erreur 10604 : Aucune station proche trouv�e avec assez de v�los.");
						break;
						

					case 0:
					default:
						lesIdVelo = proxyGS.emprunterVelos(identifiant, idStation, nbVelos, Role.Operateur);
						stationDepot = lesIdVelo[nbVelos];
						if(stationDepot == idStation)
						{
							System.out.println("Liste des v�los � retirer :");
							for(int i = 0; i < nbVelos; i++)
							{
								System.out.println("  " + lesIdVelo[i]);
								pause(TEMPS_PAUSE);
							}
						}
						else
						{
							Station stationLaPlusProche = proxyGS.getStation(stationDepot);
							System.out.println("Plus de place disponible dans cette station.");
							System.out.println("Veuillez aller � la station " + stationDepot + " qui dispose de place : ");
							System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
							System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
							System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());

						}
						break;
					}
					System.out.print("D�connexion");
					pause(TEMPS_PAUSE);
					menuPrincipal();
				}
				break;
			case "3":
				if(!proxyGS.hasUtilisateurEmprunteVelos(identifiant))
				{
					System.out.println("Vous n'avez pas emprunt� de v�lo.");
					pause(TEMPS_PAUSE);
					menuPrincipal();
				}else
				{
					// retourne les ids de v�lo � d�poser si place disponible !
					// sinon => la notification � plant� 
					//la m�thode deposerVeloOperateur doit retourner aucun idV�lo si pas place
					// Il faut stocker la liste des v�lo emprunt�s par personne � un instant T
					stationDepot = proxyGS.deposerVelos(identifiant, idStation);
					if(stationDepot != idStation){
						System.out.println("Erreur 10400 : Station destination d�finit par la notification incorrecte.");
						System.out.print("D�connexion");
						pause(TEMPS_PAUSE);
						menuPrincipal();
					}else{
						System.out.println("Veuillez d�poser les v�los");
						System.out.print("D�connexion");
						pause(TEMPS_PAUSE);
						menuPrincipal();
					}
				}
				break;
			
			/*case "4":
			 	do{
					verificationFormat = true;
					try{
						System.out.println("Veuillez entrer l'id du v�lo � retirer pour l'atelier :");
						idVelo = Integer.parseInt(entree.readLine());
					}catch(Exception e){
						System.out.println("Saisie incorrecte");
						verificationFormat  = false;
					}
				}while(!verificationFormat);
				// retourne l'id du v�lo sinon rien (null ??)
				// mettre � jour le statut du v�lo en r�paration
				resultats = proxyGS.emprunterVelos(identifiant, idStation, idVelo);
				switch(resultats[1])
				{
					case 0 :
						System.out.println("Veuillez prendre le v�lo.");
						break;
					case -1 :
						System.err.println("Erreur 10600 : Utilisateur ou Station inexistants.");
						break;
					case -2 :
						System.err.println("Erreur 10601 : Utilisateur non administrateur.");
						break;
					case -3 :
						System.err.println("Erreur 10602 : Le v�lo n'est pas disponible.");
						break;
				}
				System.out.print("D�connexion");
				pause(TEMPS_PAUSE);
				menuPrincipal();
				break;
				*/
			/*case "5":
				do{
					verificationFormat = true;
					try{
						System.out.println("Veuillez entrer l'id du v�lo r�par� � d�poser :");
						idVelo = Integer.parseInt(entree.readLine());
					}catch(Exception e){
						System.out.println("Saisie incorrecte");
						verificationFormat  = false;
					}
				}while(!verificationFormat);
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
					System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());
				}else
				{
					System.out.println("Veuillez d�poser le v�lo " + veloADeposer);
				}
				System.out.print("D�connection");
				pause(TEMPS_PAUSE);
				menuPrincipal();
				break;
			*/
			case "6":
				System.out.print("D�connexion");
				pause(TEMPS_PAUSE);
				menuPrincipal();
				break;
			default :
				menuOperateur(identifiant, mdp);
		}
	}
	
	private static void menuOperateurNotification(int identifiant, String mdp) throws IOException, InterruptedException {
		boolean continuer = true;

		System.out.println("En attente de notifications...");
		Notifications notification = proxyGS.getNotification();
		pause(TEMPS_PAUSE);
		while((notification == null) && (continuer))
		{
			System.out.println("Voulez-vous continuer � attendre des notifications ? (o/n)");
			String rep = entree.readLine();
			if(rep.equals("n"))
			{
				continuer = false;
			}else
			{
				System.out.println("En attente de notifications...");
				//retourne s'il y des notifications.
				notification = proxyGS.getNotification();
				pause(TEMPS_PAUSE);
			}
		}
		
		if(notification != null)
		{
			if(notification.getTypeNotification().toString().equals("Penurie"))
			{
				System.out.println("La station n�" + notification.getStation() + " est en " + notification.getTypeNotification().toString() + ".");
			}
			else
			{
				System.out.println("La station n�" + notification.getStation() + " est " + notification.getTypeNotification().toString() + ".");
			}
//			String detailNotification[] = proxyGS.getNotification().detailNotificationStation(notification);
//			
//			System.out.println("Veuillez transf�rer " + detailNotification[0] + " v�los de la station satur�e " + detailNotification[1] + " � la station en p�nurie " + detailNotification[2]);
//			int nombreVeloTransferes = Integer.parseInt(detailNotification[0]);
//			Station stationSaturee = proxyGS.getStation(Integer.parseInt(detailNotification[1]));
//			Station stationEnPenurie = proxyGS.getStation(Integer.parseInt(detailNotification[2]));
//			
//			int[] listeIdsVeloATRansferer = stationSaturee.getVelosLibresStation(nombreVeloTransferes);
//
//			for(int i=0; i<listeIdsVeloATRansferer.length;i++)
//			{
//				Velo velosATransferer = proxyGS.getVelo(listeIdsVeloATRansferer[i]);
//				proxyGS.transfererVelo(velosATransferer,stationSaturee.getIdStation(), stationEnPenurie.getIdStation());
//			}
			
			System.out.println("Veuillez valider par 'ok' d�s que l'action est termin�e : ");
			String actionOK = entree.readLine();
			while (!actionOK.equals("ok"))
			{
				System.out.println("Veuillez valider par 'ok' d�s que l'action est termin�e : ");
				actionOK = entree.readLine();
			}
			proxyGS.supprimerNotification();
		}
		
		menuOperateur(identifiant,mdp);
	}

	public static void menuClient(int pidUtilisateur, String pmdpUtilisateur) throws IOException, InterruptedException {
		int[] idVeloEmprunteClient;
		int stationDepot;
		int resultEmprunterVelos;
		Station stationLaPlusProche;
		
		System.out.println("--------- Menu Client ----------");
		enTeteMenu();
		System.out.println("Que voulez-vous faire ?");
		System.out.println("--1) Emprunter un v�lo");
		System.out.println("--2) Se d�connecter");
		System.out.println("----");
		valeurChoix = entree.readLine();
		
		switch(valeurChoix)
		{
			case "1":
				//test si l'op�rateur n'a pas d�j� emprunt� des v�los
				if(proxyGS.hasUtilisateurEmprunteVelos(pidUtilisateur))
				{
					System.out.println("Vous avez d�j� emprunt� un v�lo.");
					pause(TEMPS_PAUSE);
					menuClient(pidUtilisateur, pmdpUtilisateur);
				}else
				{
					// retourne l'id du v�lo si disponible sinon retourne le nom de la station, 
					// la latitude, et la longitude avec des v�los disponible
					// ne pas oublier de stocker la date et heure de l'emprunt !
					
					
					resultEmprunterVelos = proxyGS.isEmprunterVelosPossible(pidUtilisateur, idStation, 1, Role.Client);
					
					
					switch (resultEmprunterVelos) {
					
						case -1:
							System.err.println("Erreur 10600 : Utilisateur inexistant.");
							break;
						case -2:
							System.err.println("Erreur 10601 : Utilisateur non op�rateur.");
							break;
						case -3:
							System.err.println("Erreur 10602 : Station inexistante.");
							break;
						case -4:
							System.err.println("Erreur 10603 : Le nombre de v�los demand�s est sup�rieur � la capacit� de la station.");
							break;
						case -6:
							System.err.println("Erreur 10604 : Aucune station proche trouv�e avec assez de v�los.");
//						case -5:
//							System.err.println("Erreur 10604 : La station ne dispose plus de v�los libres.");
//							break;
						case 0:
						default:
							idVeloEmprunteClient = proxyGS.emprunterVelos(pidUtilisateur, idStation, 1, Role.Client);
							stationDepot = idVeloEmprunteClient[1];;
							if(stationDepot == idStation)
							{
								System.out.println("V�lo � retirer : " + idVeloEmprunteClient[0]);
							}
							else
							{
								stationLaPlusProche = proxyGS.getStation(stationDepot);
								System.out.println("Plus de place disponible dans cette station.");
								System.out.println("Veuillez aller � la station " + stationDepot + " qui dispose de place : ");
								System.out.println("  Latitude : " + stationLaPlusProche.getPosition().getLatitude());
								System.out.println("  Longitude : " + stationLaPlusProche.getPosition().getLongitude());
								System.out.println("  Nombre de v�los : " + stationLaPlusProche.getNombreVelosLibres());
								System.out.println("D�connexion");
							}
							pause(TEMPS_PAUSE);
							menuPrincipal();
							break;
					}
				}
				break;
			case "2":
				System.out.print("D�connexion");
				pause(TEMPS_PAUSE);
				menuPrincipal();
				break;
			default :
				menuClient(pidUtilisateur, pmdpUtilisateur);
		}
	}

	public static void pause(int n) throws InterruptedException{
		for(int i = 1; i<n+1; i++){
			Thread.sleep(100);
			System.out.print(".");
		}
		System.out.println("");	
	}

	private static void initialisationInstances() throws RemoteException {
		
		//creation Client
		String tab[] = new String[7];
		tab[0] = "nomClient";
		tab[1] = "prenomClient";
		tab[2] = "mdpClient";
		tab[3] = "telClient";
		tab[4] = "adrClient@email.com";
		tab[5] = "adrClient";
		tab[6] = "Client";
		proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], tab[6]);
		
		//creation Operateur
		tab = new String[7];
		tab[0] = "nomOperateur";
		tab[1] = "prenomOperateur";
		tab[2] = "mdpOperateur";
		tab[3] = "telOperateur";
		tab[4] = "adrOperateur@email.com";
		tab[5] = "adrOperateur";
		tab[6] = "Operateur";
		proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], tab[6]);
		
		//creation Administrateur
		tab = new String[7];
		tab[0] = "nomAdministrateur";
		tab[1] = "prenomAdministrateur";
		tab[2] = "mdpAdministrateur";
		tab[3] = "telAdministrateur";
		tab[4] = "adrAdministrateur@email.com";
		tab[5] = "adrAdministrateur";
		tab[6] = "Administrateur";
		proxyGS.creerUtilisateur(tab[0], tab[1], tab[2], tab[3], tab[4], tab[5], tab[6]);
	}
	
	public static void main(String[] args) throws Exception {
		proxyGS = (GestionStation) Naming.lookup("rmi://localhost:1099/MaGestionStation");
			
		//creation de la station
		int numStation = (int) (Math.random()*100000);
		String nomStation = "Station" + numStation;
		double longitude = Math.random()*10;
		double latitude = Math.random()*10;
		int capacite = 30;
		
		
		idStation = proxyGS.creerStation(nomStation, longitude, latitude, capacite);
		
		//m�thode pour v�rifier s'il y a une station ma�tre
		stationMaitre = proxyGS.gestionStationHasMaitre(idStation);

		//creation de v�los 
		//(pas d'id en param�tre du constructeur de v�lo car g�n�r� automatiquemnt)
		if(!stationMaitre){
			for(int i = 0; i < capacite-5; i++)
				proxyGS.ajouterVeloStationInitialisation(idStation);
		}else{
			// creation d'instances utilisateurs pour les tests
			initialisationInstances();
		}
		
		menuPrincipal();
	}

	
}
