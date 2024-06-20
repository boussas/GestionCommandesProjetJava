import java.io.Serializable;

public class Fournisseur implements Serializable {
    private String nom;
    private String prenom;
    private String raisonSociale;
    private String domaineActivite;
    private boolean estSociete;

    public Fournisseur(String nom, String prenom,
     String raisonSociale, String domaineActivite, boolean estSociete) {
        this.nom = nom;
        this.prenom = prenom;
        this.raisonSociale = raisonSociale;
        this.domaineActivite = domaineActivite;
        this.estSociete = estSociete;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getDomaineActivite() {
        return domaineActivite;
    }

    public boolean isEstSociete() {
        return estSociete;
    }
}