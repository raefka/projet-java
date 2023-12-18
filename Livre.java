package projetjava;

import java.sql.*;

class Monexception1 extends Exception {
	public Monexception1(String s) {
		super(s);
	}
}

public class Livre {
	private String titre;

	public String gettitre() {
		return titre;
	}

	public void settitre(String titre) {
		this.titre = titre;
	}

	public Livre(String titre) {
		this.titre = titre;
	}

	public Livre() {

	};

	public void afficher() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
			String requete1 = "select * from livre";
			Statement stmt1 = con1.createStatement();

			ResultSet rs1 = stmt1.executeQuery(requete1);
			int i = 1;
			while (rs1.next()) {
				System.out.println("Livre : " + i);
				System.out.println("id_liv :" + rs1.getInt(1) + " Titre :   " + rs1.getString(2) + " Auteur :   "
						+ rs1.getString(3) + " Genre :  " + rs1.getString(4) + " Disponibilite :  " + rs1.getString(5));
				i++;
			}
			con1.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void afficher(String titre) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
			String requete = "select * from livre where titre = ?";
			PreparedStatement stmt = con.prepareStatement(requete);
			stmt.setString(1, gettitre());

			ResultSet rs = stmt.executeQuery();

			while (rs.next())
				System.out.println("id_liv :" + rs.getInt(1) + "Titre :  " + rs.getString(2) + " Auteur : "
						+ rs.getString(3) + " Genre : " + rs.getString(4) + " Disponibilite : " + rs.getString(5));

			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void rechercher(String titre) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
			String requete2 = "select * from livre where titre=? ";
			PreparedStatement stmt2 = con2.prepareStatement(requete2);
			stmt2.setString(1, gettitre());

			ResultSet rs4 = stmt2.executeQuery();

			if (rs4.next()) {
				System.out.println("ce livre : " + gettitre() + " existe dans notre biblioth�que !!!");
			} else {
				throw new Monexception1("Ce livre n'existe pas dans notre biblioth�que !!!!");

			}

			con2.close();

		} catch (Monexception1 e) {
			System.out.println("errrrrrr : " + e.getMessage());
		}

		catch (Exception e) {
			System.out.println(e);
		}
	}

}
