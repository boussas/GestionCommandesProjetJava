import java.io.Serializable;
import java.util.*;

class Commande implements Serializable {
    private int numero;
    private Client client;
    private Fournisseur fournisseur;
    private HashMap<Produit, Integer> produits;
    private Date dateCommande;
    private Date dateLivraison;

    public Commande(int numero, Client client, Date dateLivraison) {
        this.numero = numero;
        this.client = client;
        this.produits = new HashMap<>();
        this.dateCommande = new Date();
        this.dateLivraison = dateLivraison;
    }
    public Commande(int numero, Client client) {
        this.numero = numero;
        this.client = client;
        this.produits = new HashMap<>();
        this.dateCommande = new Date();
    }

    public void ajouterProduit(Produit produit, int quantite) {
        this.produits.put(produit, this.produits.getOrDefault(produit, 0) + quantite);
    }

    public int getNumero() {
        return numero;
    }

    public Client getClient() {
        return client;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public List<String> getProduitsListe() {
        List<String> produitsListe = new ArrayList<>();
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            produitsListe.add(entry.getKey().getLibelle() + " x" + entry.getValue());
        }
        return produitsListe;
    }

    public int getQuantiteTotale() {
        int total = 0;
        for (int quantite : produits.values()) {
            total += quantite;
        }
        return total;
    }

    public double getPrixTotal() {
        double total = 0.0;
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            total += entry.getKey().getPrixTTC() * entry.getValue();
        }
        return total;
    }
    public Date getDateLivraison() {
        return this.dateLivraison;
    }
}
