package Repository;

import Entity.Labor;
import Entity.Material;
import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Repository.Interface.ProjectRepositoryInter;
import Repository.Interface.UserRepositoryInter;
import Service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository implements ProjectRepositoryInter {

    private Connection connection;
    private UserRepositoryInter userRepository;

    public ProjectRepository(Connection connection,UserRepositoryInter userRepository) {
        this.connection = connection;
        this.userRepository =  userRepository;
    }


@Override
    public Project createProject(Project project) {
        String sql = "INSERT INTO projects (nomproject, margebeneficiaire, coutotal, etatproject, client_id) VALUES (?, ?, ?, ?::etatproject, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, project.getNomProject());
            preparedStatement.setDouble(2, project.getMargeBeneficiaire());
            preparedStatement.setDouble(3, project.getCouTotal());
            preparedStatement.setString(4, project.getEtatProject().name());
            preparedStatement.setInt(5, project.getUser().getId());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                project.setId(rs.getInt(1));
            }

            System.out.println("Le projet a été inséré avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion du projet : " + e.getMessage());
            e.printStackTrace();
        }
        return project;
    }


@Override
    public Project getProjectById(int id) {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Project mapRowToProject(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("nomproject");
        double profitMargin = rs.getDouble("margebeneficiaire");
        double totalCost = rs.getDouble("coutotal");
        EtatProject status = EtatProject.valueOf(rs.getString("etatproject"));
        int userId = rs.getInt("client_id");

        // Use injected userService to get the User object

        User user = userRepository.findById(userId);
        return new Project(id,name, profitMargin, totalCost, status, user);
    }


@Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Directly create Project objects using ResultSet data
                int id = rs.getInt("id");
                String name = rs.getString("nomproject");
                double profitMargin = rs.getDouble("margebeneficiaire");
                double totalCost = rs.getDouble("coutotal");
                EtatProject status = EtatProject.valueOf(rs.getString("etatproject"));
                int userId = rs.getInt("client_id");

                User user = userRepository.findById(userId); // Assuming userRepository is available

                // Create and add the Project object to the list
                projects.add(new Project(id, name, profitMargin, totalCost, status, user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

@Override
    public void updateProject(Project project) {
        String query = "UPDATE projects SET nomproject = ?, margebeneficiaire = ?, coutotal = ?, etatproject = ?, client_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, project.getNomProject());
            ps.setDouble(2, project.getMargeBeneficiaire());

            // Handle null value for total cost
            if (project.getCouTotal() == 0) {
                ps.setNull(3, Types.DOUBLE);
            } else {
                ps.setDouble(3, project.getCouTotal());
            }

            // Handle enum value for status
            ps.setObject(4, project.getEtatProject().name());

            ps.setInt(5, project.getUser().getId());
            ps.setInt(6, project.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

@Override
    public void deleteProject(int id) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

@Override
    public List<Project> getProjectsByUserId(int userId) {
        List<Project> projects = new ArrayList<>();
    User user = userRepository.findById(userId);
        String query = "SELECT * FROM projects WHERE client_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Créer un nouvel objet Project directement à partir des résultats
                    ResultSet rss=rs;
                    int id = rs.getInt("id");
                    String name = rs.getString("nomproject");
                    double profitMargin = rs.getDouble("margebeneficiaire");
                    double totalCost = rs.getDouble("coutotal");
                    EtatProject status = EtatProject.valueOf(rs.getString("etatproject"));
                    int clientId = rs.getInt("client_id");

                    // Récupérer l'utilisateur associé

//                    System.out.println(user.toString());

                    // Créer le projet
                    Project project = new Project(id, name, profitMargin, totalCost, status, user);
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
    public void updateMarginAndTotalCost_Project(int id, double marginProfit, double totalCost) {
        String sql = "UPDATE projects SET margebeneficiaire =? , coutotal = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, marginProfit);
            preparedStatement.setDouble(2, totalCost);
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Project updated successfully");
            } else {
                System.out.println("Update failed, project not found");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE projects SET etatproject = ?::etatproject  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }
}



