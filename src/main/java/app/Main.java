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

    // Basis-SQL für beide Listen: verknüpft PERSON (Name, Vorname) mit
    // ADRESSE (Straße, PLZ, Ort) über den gemeinsamen Schlüssel PERSON_ID.
    // Da eine Person in ADRESSE mehrere Zeilen haben kann (ADRESSE_ART ist
    // Teil des Primärschlüssels, z. B. Rechnungsadresse/Lieferadresse),
    // kann ein Kunde hier mehrfach auftauchen - das erfüllt genau die
    // Anforderung "jeden Kunden mindestens einmal auflisten".
    private static final String SQL_BASE =
            "SELECT p.NAME, p.VORNAME, a.STRASSE, a.PLZ, a.ORT "
                    + "FROM PERSON p JOIN ADRESSE a ON p.PERSON_ID = a.PERSON_ID";

    public static void main(String[] args) throws SQLException {
        // Database kapselt die JDBC-Verbindung (siehe db/Database.java).
        // Erst ein Objekt erzeugen, dann die Verbindung öffnen.
        Database db = new Database();
        db.open();

        // --- Liste 1: Sortierung übernimmt die Datenbank ---
        // Wir hängen "ORDER BY p.NAME" an die Basis-Query an, sodass die
        // Ergebnisse bereits in sortierter Reihenfolge zurückkommen.
        // Die Methode abfragen() führt das SQL aus und baut daraus eine
        // Liste, die dann nur noch ausgegeben werden muss.
        System.out.println("Liste 1: von der Datenbank sortiert");
        ausgeben(abfragen(db, SQL_BASE + " ORDER BY p.NAME"));

        // --- Liste 2: Sortierung übernimmt Java ---
        // Hier wird bewusst OHNE ORDER BY abgefragt, die Reihenfolge der
        // Zeilen ist also von der Datenbank her nicht garantiert sortiert.
        // liste2.sort(...) sortiert die Liste danach in Java selbst:
        // Comparator.comparing(zeile -> zeile[0]) sagt "vergleiche die
        // Einträge anhand von Index 0 im String-Array", also anhand des
        // Namens (siehe abfragen(): kunde[0] = Name, kunde[1] = Adresse).
        System.out.println("\nListe 2: unsortiert geholt, mit Java sortiert");
        List<String[]> liste2 = abfragen(db, SQL_BASE);
        liste2.sort(Comparator.comparing(zeile -> zeile[0]));
        ausgeben(liste2);

        // Verbindung am Ende wieder schließen, um keine Ressourcen offen
        // zu lassen.
        db.close();
    }

    /**
     * Führt die übergebene SQL-Abfrage aus und wandelt jede Ergebniszeile
     * in ein String-Array {Name, Adresse} um.
     *
     * @param db  die geöffnete Datenbankverbindung
     * @param sql die auszuführende SELECT-Abfrage
     * @return Liste von Kunden als {Name, Adresse}-Arrays
     */
    private static List<String[]> abfragen(Database db, String sql) throws SQLException {
        List<String[]> kunden = new ArrayList<>();

        // try-with-resources: PreparedStatement und ResultSet werden am
        // Ende des try-Blocks automatisch geschlossen, auch wenn eine
        // Exception auftritt - man muss also kein manuelles finally mit
        // close() schreiben.
        try (PreparedStatement stmt = db.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // rs.next() rückt Zeile für Zeile durch das Ergebnis und gibt
            // false zurück, sobald keine weitere Zeile mehr da ist.
            while (rs.next()) {
                // Name aus zwei Spalten der PERSON-Tabelle zusammenbauen.
                String name = rs.getString("NAME") + ", " + rs.getString("VORNAME");
                // Adresse aus drei Spalten der ADRESSE-Tabelle zusammenbauen.
                String adresse = rs.getString("STRASSE") + ", " + rs.getString("PLZ") + " " + rs.getString("ORT");
                // Als kleines Array {Name, Adresse} in die Ergebnisliste
                // aufnehmen - das ist die einfachste Struktur für "ein
                // Kunde = zwei Textfelder", ohne dafür eine eigene Klasse
                // anlegen zu müssen.
                kunden.add(new String[] { name, adresse });
            }
        }

        return kunden;
    }

    /**
     * Gibt eine Kundenliste tabellarisch auf der Konsole aus.
     *
     * @param kunden Liste von {Name, Adresse}-Arrays
     */
    private static void ausgeben(List<String[]> kunden) {
        for (String[] kunde : kunden) {
            // %-30s: Name linksbündig auf 30 Zeichen aufgefüllt, damit die
            // Adresse in jeder Zeile an derselben Position beginnt (Tabellenoptik).
            // %n erzeugt einen plattformunabhängigen Zeilenumbruch.
            System.out.printf("%-30s %s%n", kunde[0], kunde[1]);
        }
    }
}
