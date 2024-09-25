package Repository;

import Entity.Devi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviRepository {

    public Connection connection;

    public DeviRepository(Connection connection) {
        this.connection = this.connection;
    }


    public Devi save(Devi devis) {
        String query = "INSERT INTO devis (montantestime, dateemission,datevalidate, project_id) VALUES (?,?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, devis.getMontantEstime());
            preparedStatement.setDate(2, Date.valueOf(devis.getDateEmission()));
            preparedStatement.setDate(3, Date.valueOf(devis.getDateValidate()));
            preparedStatement.setInt(4, devis.getProject_id());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    devis.setId(id);
                    System.out.println("Devis was successfully saved with ID " + id);
                } else {
                    throw new SQLException("Creating quote failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return devis;
    }
    public boolean updateDevisStatus(int id) {
        String sql = "UPDATE devis SET accepte = true WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if(result == 1) {
                return true;
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return false;
    }


    public List<Devi> findAll() {
        List<Devi> devisList = new ArrayList<>();
        String query = "SELECT * FROM devis";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Devi devis = new Devi();
                devis.setId(resultSet.getInt("id"));
                devis.setMontantEstime(resultSet.getDouble("montantestime"));
                devis.setDateEmission(resultSet.getDate("dateemission").toLocalDate());
                devis.setDateValidate(resultSet.getDate("datevalidate").toLocalDate());
                devis.setProject_id(resultSet.getInt("project_id"));
                devis.setAccepte(resultSet.getBoolean("accepte"));

                devisList.add(devis);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all devis: " + e.getMessage());
        }
        return devisList;
    }
}
