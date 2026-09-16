# Kundenliste

Java-Programm für die Aufgabe: Ausgabe von zwei Kundenlisten (Name, Adresse)
aus der Testdatenbank.

- **Liste 1**: Wird per SQL bereits sortiert (`ORDER BY NAME`) abgefragt und
  einfach tabellarisch ausgegeben.
- **Liste 2**: Wird unsortiert per SQL abgefragt und anschließend mit Java
  (`Comparator` / `List.sort`) nach Name sortiert.

## Struktur

- `src/main/java/db/Database.java` – gegebene Klasse zur Kommunikation mit
  der Oracle-Testdatenbank (Verbindung öffnen/schließen, Prozeduren/Funktionen
  ausführen, `PreparedStatement` erzeugen).
- `src/main/java/app/Kunde.java` – einfache Datenklasse für Name + Adresse.
- `src/main/java/app/Main.java` – Hauptprogramm mit den beiden Kundenlisten.

## Anpassungen vor dem Ausführen

Da mir der Zugriff auf die reale Testdatenbank fehlt, bitte vor der Ausführung
anpassen:

1. In `Database.java`: `username`, `password` und `verbinder` (JDBC-URL) mit
   den echten Zugangsdaten der Testdatenbank ersetzen.
2. In `Main.java`: `TABELLE`, `SPALTE_NAME` und `SPALTE_ADRESSE` an das
   tatsächliche Tabellen-/Spaltenschema der Kunden-Tabelle anpassen, falls es
   von `KUNDE` / `NAME` / `ADRESSE` abweicht.

## Bauen

```
mvn package
```

Das erzeugt `target/kundenliste.jar` (Main-Class: `app.Main`) inklusive
Oracle-JDBC-Treiber als Abhängigkeit.
