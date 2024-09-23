package Entity;

import java.time.LocalDate;

public class Devi {
    private int id;
    private double montantEstime;
    private LocalDate dateEmission;
    private boolean accepte;
    private LocalDate dateValidate;
    private int project_id;

    public Devi() {
    }

    public Devi(int id, double montantEstime, LocalDate dateEmission, boolean accepte, LocalDate dateValidate, int project_id) {
        this.id = id;
        this.montantEstime = montantEstime;
        this.dateEmission = dateEmission;
        this.accepte = accepte;
        this.dateValidate = dateValidate;
        this.project_id = project_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontantEstime() {
        return montantEstime;
    }

    public void setMontantEstime(double montantEstime) {
        this.montantEstime = montantEstime;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public boolean isAccepte() {
        return accepte;
    }

    public void setAccepte(boolean accepte) {
        this.accepte = accepte;
    }

    public LocalDate getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(LocalDate dateValidate) {
        this.dateValidate = dateValidate;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }



}
