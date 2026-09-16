package app;

/**
 * Repräsentiert einen Kunden mit Name und Adresse.
 */
public class Kunde {

    private final String name;
    private final String adresse;

    public Kunde(String name, String adresse) {
        this.name = name;
        this.adresse = adresse;
    }

    public String getName() {
        return name;
    }

    public String getAdresse() {
        return adresse;
    }
}
