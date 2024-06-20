import java.io.Serializable;

public class Client implements Serializable {
    private String nom;
    private String prenom;
    private int cin;

    public Client(String nom, String prenom, int cin) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getCin() {
        return cin;
    }
}