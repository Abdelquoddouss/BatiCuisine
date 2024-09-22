package Repository;

import Entity.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MaterialRepository {

    private Connection connection;

    public MaterialRepository(Connection connection) {
        this.connection = connection;
    }

    public void addMaterial(Material material) {
        String query = "INSERT INTO materials (nom, typeComposant, tauxTva, quantite, coutUnitaire, coutTransport, coefficientQualite) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, material.getNom());
            ps.setString(2, material.getTypeComposant());
            ps.setDouble(3, material.getTauxTva());
            ps.setInt(4, material.getQuantite());
            ps.setDouble(5, material.getCoutUnitaire());
            ps.setDouble(6, material.getCoutTransport());
            ps.setDouble(7, material.getCoefficientQualite());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
