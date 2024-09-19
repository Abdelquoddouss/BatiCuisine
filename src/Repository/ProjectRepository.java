package Repository;

import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    private Connection connection;
    private UserService userService;

    public ProjectRepository(Connection connection) {
        this.connection = connection;
        this.userService = userService;
    }

    public void createProject(Project project) {
        String sql = "INSERT INTO projects (nomproject, margebeneficiaire, coutotal, etatproject, client_id) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getNomProject());
            preparedStatement.setDouble(2, project.getMargeBeneficiaire());

            if(project.getCouTotal() == 0) {
                preparedStatement.setNull(3, java.sql.Types.DOUBLE);
            } else {
                preparedStatement.setDouble(3, project.getCouTotal());
            }
            preparedStatement.setObject(4, project.getEtatProject().name());
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

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

        User user = userService.getUserById(userId);
        return new Project(id, name, profitMargin, totalCost, status, user);
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM projects";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

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

    public void deleteProject(int id) {
        String query = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}



