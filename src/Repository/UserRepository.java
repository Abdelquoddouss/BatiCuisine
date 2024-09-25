package Repository;

import Entity.User;
import Repository.Interface.UserRepositoryInter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserRepositoryInter {

    private  Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }


    public void create(User user) {
        try {
            String sql = "INSERT INTO clients (nom, address, telephone, estProfessional) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getAddress());
            preparedStatement.setString(3, user.getTelephone());
            preparedStatement.setBoolean(4, user.isEstProfessional());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public User findById(int id) {
        User user = null;
        try {
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String address = resultSet.getString("address");
                String telephone = resultSet.getString("telephone");
                boolean estProfessional = resultSet.getBoolean("estProfessional");
                user = new User(id, nom, address, telephone, estProfessional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM clients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nom");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("telephone");
                boolean isProfessional = resultSet.getBoolean("estProfessional");
                users.add(new User(id, name, address, phone, isProfessional));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void update(User user) {
        try {
            String sql = "UPDATE clients SET nom = ?, address = ?, telephone = ?, estProfessional = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getNom());
            statement.setString(2, user.getAddress());
            statement.setString(3, user.getTelephone());
            statement.setBoolean(4, user.isEstProfessional());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByName(String nom) {
        User user = null;
        try {
            String sql = "SELECT * FROM clients WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nom);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String address = resultSet.getString("address");
                String telephone = resultSet.getString("telephone");
                boolean estProfessional = resultSet.getBoolean("estProfessional");
                user = new User(id, nom, address, telephone, estProfessional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


}
