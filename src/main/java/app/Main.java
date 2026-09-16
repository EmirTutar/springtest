package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import db.Database;

/**
 * Gibt nacheinander zwei Kundenlisten (Name, Adresse) aus:
 * Liste 1 kommt bereits sortiert aus der Datenbank (ORDER BY),
 * Liste 2 kommt unsortiert und wird mit Java sortiert.
 */
public class Main {

    private static final String SQL_BASE =
            "SELECT p.NAME, p.VORNAME, a.STRASSE, a.PLZ, a.ORT "
                    + "FROM PERSON p JOIN ADRESSE a ON p.PERSON_ID = a.PERSON_ID";

    public static void main(String[] args) throws SQLException {
        Database db = new Database();
        db.open();

        System.out.println("Liste 1: von der Datenbank sortiert");
        ausgeben(abfragen(db, SQL_BASE + " ORDER BY p.NAME"));

        System.out.println("\nListe 2: unsortiert geholt, mit Java sortiert");
        List<String[]> liste2 = abfragen(db, SQL_BASE);
        liste2.sort(Comparator.comparing(zeile -> zeile[0]));
        ausgeben(liste2);

        db.close();
    }

    private static List<String[]> abfragen(Database db, String sql) throws SQLException {
        List<String[]> kunden = new ArrayList<>();

        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("NAME") + ", " + rs.getString("VORNAME");
                String adresse = rs.getString("STRASSE") + ", " + rs.getString("PLZ") + " " + rs.getString("ORT");
                kunden.add(new String[] { name, adresse });
            }
        }

        return kunden;
    }

    private static void ausgeben(List<String[]> kunden) {
        for (String[] kunde : kunden) {
            System.out.printf("%-30s %s%n", kunde[0], kunde[1]);
        }
    }
}
