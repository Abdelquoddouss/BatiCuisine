package Service.Interface;

import Entity.Project;

import java.util.List;

public interface ProjectServiceInter {
    Project addProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProjects();
    void updateProject(Project project);
    void deleteProject(int id);
    List<Project> getProjectsByUserId(int userId);
}
