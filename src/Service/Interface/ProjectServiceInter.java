package Service.Interface;

import Entity.Labor;
import Entity.Material;
import Entity.Project;

import java.util.List;

public interface ProjectServiceInter {
    Project addProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProjects();
    void updateProject(Project project);
    void deleteProject(int id);
    List<Project> getProjectsByUserId(int userId);
    void updateMarginAndTotalCost_Project(int projectId,double marginProfit, double totalCost);
}
