package Service.Interface;

import Entity.Labor;

import java.util.List;

public interface LaborServiceInter {
    void addLabor(Labor labor);
     List<Labor> findAllLaborsByProject(int projectId);
    double calculateLaborAfterVatRate(Labor labor);
    double calculateLaborBeforeVatRate(Labor labor);

}
