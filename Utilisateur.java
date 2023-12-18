package projetjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

class MonException extends Exception {
    public MonException(String s) {
        super(s);
    }
}

public class Utilisateur {
    private String login1, pwd1;
    private String role;
    private int id_ut;

    public int getid_ut() {
        return id_ut;
    }

    public void setid_ut(int id_ut) {
        this.id_ut = id_ut;
    }

    public String getrole() {
        return role;
    }

    public void setrole(String role) {
        this.role = role;
    }

    public String getlogin() {
        return login1;
    }

    public void setlogin(String login1) {
        this.login1 = login1;
    }

    public String getpwd() {
        return pwd1;
    }

    public void setpwd(String pwd1) {
        this.pwd1 = pwd1;
    }

    public Utilisateur(String login1, String pwd1) {
        this.login1 = login1;
        this.pwd1 = pwd1;
    }

    public void authentifier() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
            String requete = "SELECT * FROM utilisateur WHERE login=? AND pwd=?";
            try (PreparedStatement stmt = con.prepareStatement(requete)) {
                stmt.setString(1, getlogin());
                stmt.setString(2, getpwd());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");
                        this.role = role;
                        int id_ut = rs.getInt("id_utilisateur");
                        this.id_ut = id_ut;

                        if ("etudiant".equals(role) || "enseignant".equals(role)) {
                            System.out.println("Connexion r�ussite en tant que " + role + "....");

                        } else {
                            System.out.println("Connexion r�ussite en tant que " + role + "....");

                        }
                    } else {
                        throw new MonException("L'utilisateur n'est pas reconnu");
                    }
                }
            } finally {
                con.close();
            }

        } catch (MonException e) {
            System.out.println("Connexion �chou�e: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void consulteremp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
            String requete1 = "select id_livre,date_emprunt,date_retour,statut from emprunt where id_utilisateur =?";
            PreparedStatement stmt1 = con1.prepareStatement(requete1);
            stmt1.setInt(1, getid_ut());

            ResultSet rs1 = stmt1.executeQuery();

            while (rs1.next())
                System.out.println(rs1.getInt(1) + "  " + rs1.getTimestamp(2) + "  " + rs1.getTimestamp(3) + " "
                        + rs1.getString(4));

            con1.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void gererrapport() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
            String requete2 = "select id_livre,COUNT(id_livre) as nb_emprunts from emprunt GROUP BY id_livre ORDER BY nb_emprunts DESC";
            PreparedStatement stmt2 = con2.prepareStatement(requete2);
            ResultSet rs2 = stmt2.executeQuery();
            System.out.println("les livres les plus plus emprunt�s :");
            while (rs2.next()) {

                int idLivre = rs2.getInt("id_livre");
                int nbEmprunts = rs2.getInt("nb_emprunts");
                System.out.println(" idLivre " + idLivre + " : " + nbEmprunts);
            }
            String requete3 = "select id_utilisateur,COUNT(id_emprunt) as nb_emprunts from emprunt GROUP BY id_utilisateur ORDER BY nb_emprunts DESC";
            PreparedStatement stmt3 = con2.prepareStatement(requete3);
            ResultSet rs3 = stmt3.executeQuery();
            System.out.println("les utilisateurs les plus assidus :");
            while (rs3.next()) {

                int idUtilisateur = rs3.getInt("id_utilisateur");
                int nbEmprunts = rs3.getInt("nb_emprunts");
                System.out.println("idUtilisateur " + idUtilisateur + " : " + nbEmprunts + "emprunt(s)");
            }
            con2.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void affmail() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con10 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
            String requete400 = "select message from utilisateur where login=? ";
            PreparedStatement stmt450 = con10.prepareStatement(requete400);
            stmt450.setString(1, getlogin());
            ResultSet rs465 = stmt450.executeQuery();
            if (rs465.next()) {
                System.out.println("message re�u :");
                System.out.println(rs465.getString(1));
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void envoimail() {
        System.out.println("donner le mail du destinataire:");
        Scanner ma = new Scanner(System.in);
        String log = ma.nextLine();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");
            String requete4 = "select * from utilisateur where login=? ";
            PreparedStatement stmt4 = con3.prepareStatement(requete4);
            stmt4.setString(1, log);

            ResultSet rs4 = stmt4.executeQuery();
            if (rs4.next()) {
                Scanner e = new Scanner(System.in);
                System.out.println("saisir l'objet du mail :");
                String s = e.nextLine();
                System.out.println("saisir le message du mail");
                String r = e.nextLine();
                String requete5 = "update utilisateur set message=? where login=? ";
                PreparedStatement stmt5 = con3.prepareStatement(requete5);
                stmt5.setString(1, r);
                stmt5.setString(2, log);
                int mes = stmt5.executeUpdate();
                if (mes > 0) {
                    System.out.println("Message envoy�e avec succ�es !");
                } else {
                    System.out.println("Echec d'envoi du message !!!");
                }

            } else {
                System.out.println("v�rifier le mail de destinataire !!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
