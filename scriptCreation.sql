DROP TABLE Utilisateur;
CREATE TABLE Utilisateur
	( 
		idUtilisateur int,
		nom Varchar(30),
		prenom Varchar(30),
		telephone Varchar(10),
		constraint pk_Utilisateur primary key(idUtilisateur)
	)

Insert into Utilisateur(idUtilisateur,nom,prenom,telephone) VALUES (1, 'saless', 'fabien', '0123456789');