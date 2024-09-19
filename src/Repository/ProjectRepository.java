package Repository;

import Entity.Project;
import Entity.User;
import Entity.enums.EtatProject;
import Service.UserService;

import java.sql.*;

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
    }


}
