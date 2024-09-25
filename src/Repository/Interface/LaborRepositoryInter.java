package Repository.Interface;

import Entity.Labor;

import java.util.List;

public interface LaborRepositoryInter {
    void addLabor(Labor labor);
    List<Labor> findAllLaborsByProject(int projectId);

}
