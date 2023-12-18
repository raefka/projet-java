package projetjava;
import java.util.*;



import java.util.Scanner;

public class Principale {

    public static void main(String[] args) {
        Scanner e = new Scanner(System.in);
        System.out.println("-- Bienvenue dans notre bibliothèque en ligne -- \n");
        System.out.println("- veuillez entrer votre login : ");
        String s = e.nextLine();
        System.out.println("-- veuillez entrer votre pwd : ");
        String m = e.nextLine();

        Utilisateur p = new Utilisateur(s, m);
        p.authentifier();

        if (p.getrole().equals("etudiant") || p.getrole().equals("enseignant")) {
            int choix;

            do {
                System.out.println("-----------------------");
                System.out.println("1-Consulter catalogue");
                System.out.println("2-Rechercher un livre");
                System.out.println("3-Afficher les détails d'un livre");
                System.out.println("4-Gestion des emprunts et retours de livres");
                System.out.println("5-Réservation en ligne de livres indisponibles ");
                System.out.println("6-Consultation de l'historique des emprunts");
                System.out.println("7-Consultation des messages réçus");
                System.out.println("8-Déconnexion");
                System.out.println("------------------------");
                System.out.println("-Entrer votre choix :");
                choix = e.nextInt();
                e.nextLine(); 

                switch (choix) {
                    case 1:
                        Livre catalogue = new Livre();
                        catalogue.afficher();
                        break;
                    case 2:
                        System.out.println("-Entrer le titre du livre à chercher :");
                        String titre = e.nextLine();
                        Livre rechercheLivre = new Livre(titre);
                        rechercheLivre.rechercher(titre);
                        break;
                    case 3:
                        System.out.println("-Entrer le livre a voir :");
                        String titre1 = e.nextLine();
                        Livre detailLivre = new Livre(titre1);
                        detailLivre.afficher(titre1);
                        break;
                    case 4:
                    	System.out.println("-Entrer id du livre :");
                        int id_l = e.nextInt();
                        Emprunter emp = new Emprunter((p.getid_ut()),id_l);
                        emp.emprunter();
                        break;
                    case 5:
                    	System.out.println("-Entrer id du livre :");
                        int id_l1 = e.nextInt();
                        Reservation res = new Reservation((p.getid_ut()),id_l1);
                        res.reserver();
                        break;
                    case 6:
                    	p.consulteremp();
                    	break;
                    case 7:
                    	p.affmail();
                    	break;
                    case 8:
                        System.out.println("Au revoir!");
                        try {
        					return;
        					}
        					finally {
        						Principale.main(args);
        					}
                        
                    default:
                        System.out.println("Choix invalide");
                }

            } while (choix != 8); 
        }else {
        	int choix1;
        	do {
                System.out.println("-----------------------");
                System.out.println("1-Gerer rapport des personnes et livres");
                System.out.println("2-Envoi mails aux etud/ens");
                System.out.println("3-Déconnexion");
                System.out.println("-Entrer votre choix :");
                choix1 = e.nextInt();
                e.nextLine();
                switch (choix1) {
                case 1:
                    p.gererrapport();
                    break;
                case 2:
                    p.envoimail();
                    break;
                case 3:
                	System.out.println("Au revoir!");
                    try {
    					return;
    					}
    					finally {
    						Principale.main(args);
    					}
                default:
                    System.out.println("Choix invalide");
                    }
        	}while(choix1 != 3);
        }
    }
}
