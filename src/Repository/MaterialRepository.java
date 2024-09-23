package Repository;

import Entity.Material;
import Entity.Project;
import Repository.Interface.MaterialRepsitoryInter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialRepository implements MaterialRepsitoryInter {

    private Connection connection;

    public MaterialRepository(Connection connection) {
        this.connection = connection;
    }

    public void creerMaterial(Material material) {
        String query = "INSERT INTO materials (nom, typeComposant, tauxTva, quantite, coutUnitaire, coutTransport, coefficientQualite,project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, material.getNom());
            ps.setString(2, material.getTypeComposant());
            ps.setDouble(3, material.getTauxTva());
            ps.setInt(4, material.getQuantite());
            ps.setDouble(5, material.getCoutUnitaire());
            ps.setDouble(6, material.getCoutTransport());
            ps.setDouble(7, material.getCoefficientQualite());
            ps.setInt(8, material.getProject().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les matériaux associés à un projet
    public List<Material> findAllMaterialsByProject(int projectId) {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT m.id, m.coutUnitaire, m.quantite, m.coutTransport, m.coefficientQualite, " +
                "m.nom, m.taux_tva, m.project_id, m.typeComposant " +
                "FROM materials m WHERE m.project_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, projectId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getInt("project_id"));

                Material material = new Material(
                        resultSet.getString("nom"),
                        resultSet.getString("typeComposant"),
                        resultSet.getDouble("taux_tva"),
                        resultSet.getInt("id"),
                        resultSet.getInt("quantite"),
                        resultSet.getDouble("coutUnitaire"),
                        resultSet.getDouble("coutTransport"),
                        resultSet.getDouble("coefficientQualite")
                );

                material.setProject(project);
                materials.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error finding materials by project ID: " + e.getMessage());
        }

        return materials;
    }

}
