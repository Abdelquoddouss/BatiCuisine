package Repository;

import Entity.Labor;
import Entity.Project;
import Repository.Interface.LaborRepositoryInter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaborRepository implements LaborRepositoryInter {

    private Connection connection;

    public LaborRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addLabor(Labor labor) {
        String sql = "INSERT INTO Labor (nom, typeComposant, tauxTva, tauxHoraire, heuresTravail, productuviteOuvrier, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, labor.getNom());
            stmt.setString(2, labor.getTypeComposant());
            stmt.setDouble(3, labor.getTauxTva());
            stmt.setDouble(4, labor.getTauxHoraire());
            stmt.setDouble(5, labor.getHeuresTravail());
            stmt.setDouble(6, labor.getProductuvuteOuvrier());
            stmt.setInt(7, labor.getProject().getId());
            System.out.println(labor.getProject().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour récupérer tous les travaux associés à un projet
    public List<Labor> findAllLaborsByProject(int projectId) {
        List<Labor> workforces = new ArrayList<>();
        String sql = "SELECT l.id, l.tauxHoraire, l.heuresTravail, l.productuvuteOuvrier, " +
                "l.nom, l.taux_tva, l.project_id, l.type_composant " +
                "FROM labors l WHERE l.project_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getInt("project_id"));

                Labor labor = new Labor(
                        resultSet.getString("nom"),
                        resultSet.getString("type_composant"),
                        resultSet.getDouble("taux_tva"),
                        resultSet.getDouble("heuresTravail"),
                        resultSet.getInt("id"),
                        resultSet.getDouble("tauxHoraire"),
                        resultSet.getDouble("productuvuteOuvrier")
                );

                labor.setProject(project);
                workforces.add(labor);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des travaux par ID de projet : " + e.getMessage());
        }

        return workforces;
    }


}
