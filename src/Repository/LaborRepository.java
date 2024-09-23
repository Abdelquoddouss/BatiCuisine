package Repository;

import Entity.Labor;
import Repository.Interface.LaborRepositoryInter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
