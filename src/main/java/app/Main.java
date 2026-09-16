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
 * Liste 1 wird per SQL bereits sortiert von der Datenbank geholt,
 * Liste 2 wird unsortiert geholt und anschließend mit Java sortiert.
 *
 * Tabellen- und Spaltennamen müssen ggf. an das tatsächliche
 * Schema der Testdatenbank angepasst werden.
 */
public class Main {

    private static final String TABELLE = "KUNDE";
    private static final String SPALTE_NAME = "NAME";
    private static final String SPALTE_ADRESSE = "ADRESSE";

    public static void main(String[] args) {
        Database db = new Database();

        try {
            db.open();

            System.out.println("Liste 1: von der Datenbank sortiert (ORDER BY " + SPALTE_NAME + ")");
            System.out.println("------------------------------------------------------------");
            String sqlSortiert = "SELECT " + SPALTE_NAME + ", " + SPALTE_ADRESSE
                    + " FROM " + TABELLE + " ORDER BY " + SPALTE_NAME;
            List<Kunde> liste1 = ladeKunden(db, sqlSortiert);
            gibKundenlisteAus(liste1);

            System.out.println();
            System.out.println("Liste 2: unsortiert von der Datenbank, mit Java sortiert");
            System.out.println("------------------------------------------------------------");
            String sqlUnsortiert = "SELECT " + SPALTE_NAME + ", " + SPALTE_ADRESSE + " FROM " + TABELLE;
            List<Kunde> liste2 = ladeKunden(db, sqlUnsortiert);
            liste2.sort(Comparator.comparing(Kunde::getName));
            gibKundenlisteAus(liste2);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private static List<Kunde> ladeKunden(Database db, String sql) throws SQLException {
        List<Kunde> kunden = new ArrayList<>();

        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                kunden.add(new Kunde(rs.getString(SPALTE_NAME), rs.getString(SPALTE_ADRESSE)));
            }
        }

        return kunden;
    }

    private static void gibKundenlisteAus(List<Kunde> kunden) {
        System.out.printf("%-30s %-40s%n", "Name", "Adresse");
        for (Kunde kunde : kunden) {
            System.out.printf("%-30s %-40s%n", kunde.getName(), kunde.getAdresse());
        }
    }
}
