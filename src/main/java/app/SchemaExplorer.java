package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Database;

/**
 * Kleines Hilfsprogramm, um herauszufinden, wie die Kunden-Tabelle
 * (bzw. View) in der Testdatenbank wirklich heißt und welche Spalten
 * sie hat. Einfach ausführen und die Konsolenausgabe prüfen.
 */
public class SchemaExplorer {

    public static void main(String[] args) {
        Database db = new Database();

        try {
            db.open();

            System.out.println("=== Tabellen im eigenen Schema (USER_TABLES) ===");
            printSpalte(db, "SELECT table_name FROM user_tables", "TABLE_NAME");

            System.out.println();
            System.out.println("=== Tabellen mit 'KUND' im Namen (ALL_TABLES, inkl. fremder Schemas) ===");
            printZweiSpalten(db, "SELECT owner, table_name FROM all_tables WHERE table_name LIKE '%KUND%'",
                    "OWNER", "TABLE_NAME");

            System.out.println();
            System.out.println("=== Views mit 'KUND' im Namen (ALL_VIEWS) ===");
            printZweiSpalten(db, "SELECT owner, view_name FROM all_views WHERE view_name LIKE '%KUND%'",
                    "OWNER", "VIEW_NAME");

            System.out.println();
            System.out.println("=== Synonyme mit 'KUND' im Namen (ALL_SYNONYMS) ===");
            printDreiSpalten(db,
                    "SELECT synonym_name, table_owner, table_name FROM all_synonyms WHERE synonym_name LIKE '%KUND%'",
                    "SYNONYM_NAME", "TABLE_OWNER", "TABLE_NAME");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private static void printSpalte(Database db, String sql, String spalte) throws SQLException {
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            boolean leer = true;
            while (rs.next()) {
                System.out.println(rs.getString(spalte));
                leer = false;
            }
            if (leer) {
                System.out.println("(keine Treffer)");
            }
        }
    }

    private static void printZweiSpalten(Database db, String sql, String spalte1, String spalte2) throws SQLException {
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            boolean leer = true;
            while (rs.next()) {
                System.out.printf("%-20s %-20s%n", rs.getString(spalte1), rs.getString(spalte2));
                leer = false;
            }
            if (leer) {
                System.out.println("(keine Treffer)");
            }
        }
    }

    private static void printDreiSpalten(Database db, String sql, String spalte1, String spalte2, String spalte3)
            throws SQLException {
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            boolean leer = true;
            while (rs.next()) {
                System.out.printf("%-20s %-20s %-20s%n",
                        rs.getString(spalte1), rs.getString(spalte2), rs.getString(spalte3));
                leer = false;
            }
            if (leer) {
                System.out.println("(keine Treffer)");
            }
        }
    }
}
