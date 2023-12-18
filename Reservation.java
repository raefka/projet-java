package projetjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

class Monexception4 extends Exception {
    public Monexception4(String s) {
        super(s);
    }
}

class Monexception8 extends Exception {
    public Monexception8(String s) {
        super(s);
    }
}

public class Reservation {
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

    public Reservation(int id_ut, int id_l) {
        this.id_ut = id_ut;
        this.id_l = id_l;
    }

    public void reserver() {
        try {

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava", "root", "")) {
                String requete9 = "select * from reservation where id_livre = ? and statut = 'En attente'";
                try (PreparedStatement stmt8 = con.prepareStatement(requete9)) {
                    stmt8.setInt(1, id_l);
                    ResultSet rs8 = stmt8.executeQuery();
                    if (rs8.next()) {
                        throw new Monexception8("Ce livre est d�j� r�serv�.");
                    } else {
                        try (Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetjava",
                                "root", "")) {
                            String requete = "select * from livre where id_livre = ? and disponibilite = 'oui'";
                            try (PreparedStatement stmt = con1.prepareStatement(requete)) {
                                stmt.setInt(1, id_l);
                                ResultSet rs = stmt.executeQuery();
                                if (rs.next()) {
                                    System.out.println("Ce livre peut �tre emprunt� sans r�servation.");
                                } else {
                                    System.out.println("Traitement de r�servation....");
                                    String requete2 = "SELECT * FROM livre  WHERE id_livre =?";
                                    try (PreparedStatement stmt2 = con1.prepareStatement(requete2)) {
                                        stmt2.setInt(1, id_l);
                                        try (ResultSet rs1 = stmt2.executeQuery()) {
                                            if (rs1.next()) {
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(new Date());
                                                String requete4 = "INSERT INTO reservation (id_utilisateur, id_livre, date_reservation, statut) VALUES (?, ?, ?, ?)";
                                                try (PreparedStatement stmt1 = con1.prepareStatement(requete4)) {
                                                    stmt1.setInt(1, getid_ut());
                                                    stmt1.setInt(2, getid_l());
                                                    stmt1.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                                                    Scanner ar = new Scanner(System.in);
                                                    System.out.println(
                                                            "Choisir entre 1: r�server / 2: confirmer la r�servation pour l'emprunter / 3: annuler la r�servation");
                                                    int scan = ar.nextInt();
                                                    if (scan == 1) {
                                                        stmt1.setString(4, "En attente");
                                                    } else if (scan == 2) {
                                                        String requete5 = "update reservation set statut='Confirm�e' where id_utilisateur=? and id_livre=?";
                                                        PreparedStatement stmt12 = con1.prepareStatement(requete5);
                                                        stmt12.setInt(1, getid_ut());
                                                        stmt12.setInt(2, getid_l());
                                                        int conf = stmt12.executeUpdate();
                                                    } else if (scan == 3) {
                                                        String requete6 = "update reservation set statut='Annul�e' where id_utilisateur=? and id_livre=?";
                                                        PreparedStatement stmt13 = con1.prepareStatement(requete6);
                                                        stmt13.setInt(1, getid_ut());
                                                        stmt13.setInt(2, getid_l());
                                                        int annu = stmt13.executeUpdate();

                                                    }

                                                    int rowsAffected = stmt1.executeUpdate();

                                                    if (rowsAffected > 0) {
                                                        System.out.println("r�servation r�ussite !");
                                                    } else {
                                                        System.out.println("�chec de la r�servation.");
                                                    }
                                                }
                                            } else {
                                                throw new Monexception4(
                                                        "Ce livre n'existe pas, impossible de le r�server");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Monexception8 | Monexception4 e) {
            System.out.println("Erreur de r�servation : " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
