package Entity;

import Entity.enums.EtatProject;

public class Project {


    private int id ;
    private String NomProject ;
    private double margeBeneficiaire ;
    private double couTotal ;
    private EtatProject etatProject ;
    private User user ;

    public Project() {
    }

    public Project(String NomProject, double margeBeneficiaire, double couTotal, EtatProject etatProject, User user) {
        this.NomProject = NomProject;
        this.margeBeneficiaire = margeBeneficiaire;
        this.couTotal = couTotal;
        this.etatProject = etatProject;
        this.user = user;
    }

    public Project(int id, String name, double profitMargin, double totalCost, EtatProject status, User user) {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomProject() {
        return NomProject;
    }

    public void setNomProject(String nomProject) {
        NomProject = nomProject;
    }

    public double getMargeBeneficiaire() {
        return margeBeneficiaire;
    }

    public void setMargeBeneficiaire(double margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public double getCouTotal() {
        return couTotal;
    }

    public void setCouTotal(double couTotal) {
        this.couTotal = couTotal;
    }

    public EtatProject getEtatProject() {
        return etatProject;
    }

    public void setEtatProject(EtatProject etatProject) {
        this.etatProject = etatProject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
