package Entity;

import java.util.List;

public class User {

   private int id ;
   private String nom ;
   private String address ;
   private String telephone ;
    private boolean estProfessional ;
  private List<Project> Projects ;

    public User() {
    }

    public User(int id, String nom, String address, String telephone, boolean estProfessional, List<Project> Projects) {
        this.id = id;
        this.nom = nom;
        this.address = address;
        this.telephone = telephone;
        this.estProfessional = estProfessional;
        this.Projects = Projects;


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isEstProfessional() {
        return estProfessional;
    }

    public void setEstProfessional(boolean estProfessional) {
        this.estProfessional = estProfessional;
    }

    public List<Project> getProjects() {
        return Projects;
    }

    public void setProjects(List<Project> Projects) {
        this.Projects = Projects;
    }




}
