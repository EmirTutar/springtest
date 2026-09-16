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
- `src/main/java/app/Main.java` – Hauptprogramm mit den beiden Kundenlisten.
  Verknüpft `PERSON` (NAME, VORNAME) und `ADRESSE` (STRASSE, PLZ, ORT) über
  `PERSON_ID`.
- `src/main/java/app/SchemaExplorer.java` – Hilfsprogramm zum Erkunden von
  Tabellen-/Spaltennamen der Testdatenbank (nicht Teil der eigentlichen
  Aufgabe).

## Anpassungen vor dem Ausführen

In `Database.java`: `username`, `password` und `verbinder` (JDBC-URL) mit den
echten Zugangsdaten der Testdatenbank ersetzen.

## Bauen

```
mvn package
```

Das erzeugt `target/kundenliste.jar` (Main-Class: `app.Main`) inklusive
Oracle-JDBC-Treiber als Abhängigkeit.
