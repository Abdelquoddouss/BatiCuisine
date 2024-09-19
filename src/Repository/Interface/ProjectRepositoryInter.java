package Repository.Interface;

import Entity.Project;

import java.util.List;

public interface ProjectRepositoryInter {
    void createProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProjects();
    void updateProject(Project project);
    void deleteProject(int id);

}
