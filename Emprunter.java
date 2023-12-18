package projetjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

class Monexception2 extends Exception {
    public Monexception2(String s) {
        super(s);
    }
}

class Monexception3 extends Exception {
    public Monexception3(String s) {
        super(s);
    }
}

public class Emprunter {

    private int id_l, id_ut;

    public int getid_l() {
        return id_l;
    }

    public void setid_l(int id_l) {
        this.id_l = id_l;
    }

    public int getid_ut() {
        return id_ut;
    }

    public void setid_ut(int id_ut) {
        this.id_ut = id_ut;
    }

    public Emprunter(int id_ut, int id_l) {
        this.id_ut = id_ut;
        this.id_l = id_l;
    }

    public void emprunter() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "");

            System.out.println("Choisi un choix : 1/Emprunter un livre ou  2/Retourner un livre");
            Scanner s = new Scanner(System.in);
            int e1 = s.nextInt();

            if (e1 == 1) {

                String selectEmpruntQuery = "SELECT * FROM emprunt WHERE id_livre = ? AND (statut = 'en cours' OR statut = 'en retard')";
                try (PreparedStatement stmt = con.prepareStatement(selectEmpruntQuery)) {
                    stmt.setInt(1, id_l);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        throw new Monexception2("Ce livre est emprunt�, tu peux le r�server");
                    } else {
                        System.out.println("Traitement d'emprunt....");
                        String selectLivreQuery = "SELECT * FROM livre  WHERE id_livre = ?";
                        try (PreparedStatement stmt2 = con.prepareStatement(selectLivreQuery)) {
                            stmt2.setInt(1, id_l);
                            ResultSet rs1 = stmt2.executeQuery();

                            if (rs1.next()) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new Date());
                                calendar.add(Calendar.DATE, 15);
                                Date dateRetour = calendar.getTime();

                                String insertEmpruntQuery = "INSERT INTO emprunt (id_utilisateur, id_livre, date_emprunt, date_retour, statut) VALUES (?, ?, ?, ?, ?)";
                                try (PreparedStatement stmt1 = con.prepareStatement(insertEmpruntQuery)) {
                                    stmt1.setInt(1, getid_ut());
                                    stmt1.setInt(2, getid_l());
                                    stmt1.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                                    stmt1.setTimestamp(4, new Timestamp(dateRetour.getTime()));
                                    stmt1.setString(5, "En cours");

                                    int rowsAffected = stmt1.executeUpdate();

                                    if (rowsAffected > 0) {
                                        System.out.println("Emprunt r�ussi !");

                                        String updateLivreQuery = "UPDATE livre SET disponibilite = 'non' WHERE id_livre = ?";
                                        try (PreparedStatement stmt7 = con.prepareStatement(updateLivreQuery)) {
                                            stmt7.setInt(1, getid_l());
                                            stmt7.executeUpdate();
                                        }
                                    } else {
                                        System.out.println("�chec de l'emprunt.");
                                    }
                                }
                            } else {

                                throw new Monexception3("Ce livre n'existe pas, impossible de l'emprunter !!");
                            }
                        }
                    }
                }
            } else {

                String updateEmpruntQuery = "UPDATE emprunt SET statut ='termin�e' WHERE id_utilisateur = ? AND id_livre = ?";
                try (PreparedStatement stmt6 = con.prepareStatement(updateEmpruntQuery)) {
                    stmt6.setInt(1, getid_ut());
                    stmt6.setInt(2, getid_l());
                    int rowsAffected1 = stmt6.executeUpdate();
                    System.out.println("livre retourn�e avec succ�es ....");
                }
            }

            con.close();
        } catch (Monexception2 e) {
            System.out.println("Erreur d'emprunt: " + e.getMessage());
        } catch (Monexception3 e) {
            System.out.println("Erreur d'emprunt : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
