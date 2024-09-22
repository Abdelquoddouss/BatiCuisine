package Service;

import Entity.Project;
import Repository.Interface.ProjectRepositoryInter;
import Repository.ProjectRepository;

import java.util.List;

public class ProjectService implements Service.Interface.ProjectServiceInter {

    private final ProjectRepositoryInter projectRepository;

    public ProjectService(ProjectRepositoryInter projectRepository) {
        this.projectRepository = projectRepository;
    }

@Override
    public void addProject(Project project) {
        projectRepository.createProject(project);
    }
@Override
    public Project getProjectById(int id) {
        return projectRepository.getProjectById(id);
    }
@Override
    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }
@Override
    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }
@Override
    public void deleteProject(int id) {
        projectRepository.deleteProject(id);
    }
@Override
    public List<Project> getProjectsByUserId(int userId) {
        return projectRepository.getProjectsByUserId(userId);
    }


}
