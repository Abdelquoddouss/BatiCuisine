package Entity;

public class Labor  extends Composants {

    private int id;
    private double tauxHoraire;
    private double heuresTravail;
    private double productuvuteOuvrier;
    private Project project;


    public Labor() {
    }

    public Labor(String nom, String typeComposant, double tauxTva,double heuresTravail, int id, double tauxHoraire, double productuvuteOuvrier) {
        super(nom, typeComposant, tauxTva);
        this.heuresTravail = heuresTravail;
        this.id = id;
        this.tauxHoraire = tauxHoraire;
        this.productuvuteOuvrier = productuvuteOuvrier;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public double getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(double tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public double getHeuresTravail() {
        return heuresTravail;
    }

    public void setHeuresTravail(double heuresTravail) {
        this.heuresTravail = heuresTravail;
    }

    public double getProductuvuteOuvrier() {
        return productuvuteOuvrier;
    }

    public void setProductuvuteOuvrier(double productuvuteOuvrier) {
        this.productuvuteOuvrier = productuvuteOuvrier;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Double calculateTotalCost() {
        Double costBeforeTax =  tauxHoraire * heuresTravail * productuvuteOuvrier;
        Double costWithTax = costBeforeTax * (1 + (getTauxTva() / 100));
        return costWithTax;
    }
}
