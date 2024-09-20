package Entity.enums;

public enum EtatProject {
    EN_COURS("en cours"),
    TERMINE("Terminer"),
    ANNULE("Annuler");

    private final String value;

    EtatProject(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
