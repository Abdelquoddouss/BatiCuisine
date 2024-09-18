package Entity;

public class Composants {

    private int id;
    private String nom ;
    private String typeComposant;
    private double tauxTva;

    public Composants() {
    }

    public Composants( String nom, String typeComposant, double tauxTva) {

        this.nom = nom;
        this.typeComposant = typeComposant;
        this.tauxTva = tauxTva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeComposant() {
        return typeComposant;
    }

    public void setTypeComposant(String typeComposant) {
        this.typeComposant = typeComposant;
    }

    public double getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(double tauxTva) {
        this.tauxTva = tauxTva;
    }


}
