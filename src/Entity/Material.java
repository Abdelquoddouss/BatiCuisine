package Entity;

public class Material extends Composants {

    private int id ;
    private int quantite;
    private double coutUnitaire ;
    private double coutTransport ;
    private double coefficientQualite ;
    private Project project;


    public Material( String nom, String typeComposant, double tauxTva, int id, int quantite, double coutUnitaire, double coutTransport, double coefficientQualite) {
        super( nom, typeComposant, tauxTva);
        this.id = id;
        this.quantite = quantite;
        this.coutUnitaire = coutUnitaire;
        this.coutTransport = coutTransport;
        this.coefficientQualite = coefficientQualite;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public void setCoutUnitaire(double coutUnitaire) {
        this.coutUnitaire = coutUnitaire;
    }

    public double getCoutTransport() {
        return coutTransport;
    }

    public void setCoutTransport(double coutTransport) {
        this.coutTransport = coutTransport;
    }

    public double getCoefficientQualite() {
        return coefficientQualite;
    }

    public void setCoefficientQualite(double coefficientQualite) {
        this.coefficientQualite = coefficientQualite;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Double calculateTotalCost() {
        Double costBeforeTax = (coutUnitaire * quantite * coefficientQualite) + coutTransport;
        Double costWithTax = costBeforeTax * (1 + (getTauxTva() / 100));
        return costWithTax;
    }

}
