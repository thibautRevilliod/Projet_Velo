package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GestionStationJDBC {
Connection conn;
	
	public GestionStationJDBC() {
		try {
		    Class.forName("org.h2.Driver");
		    conn = DriverManager.getConnection("jdbc:h2:"+"BDD"+";IGNORECASE=TRUE", "sa", "");
		    // on cree un objet Statement qui va permettre l'execution des requetes
	        Statement s = conn.createStatement();
	
	        System.out.println("test");
	        
//			ResultSet rs = s.executeQuery("select * from UTILISATEUR");
//	        if (rs.next()) {
//	        	double solde = rs.get("solde");
//	        	Date date = rs.getTimestamp("dateDerniereOperation");
//	        	Position p = new Position(solde);
//	        	p.setDerniereOperation(date);
//	        	return p;
//	        } else {
//	        	return null;
//	        }

		} catch(Exception e) {
			// il y a eu une erreur
			e.printStackTrace();
		}
	}
	
//	
}
