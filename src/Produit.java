import java.io.Serializable;

public class Produit implements Serializable {
    private String libelle;
    private double prixTTC;
    private int stock;

    public Produit(String libelle, double prixTTC, int stock) {
        this.libelle = libelle;
        this.prixTTC = prixTTC;
        this.stock = stock;
    }

    public String getLibelle() {
        return libelle;
    }

    public double getPrixTTC() {
        return prixTTC;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean decrementStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }

    public boolean isAvailable(int quantity) {
        return stock >= quantity;
    }
}
