package Service;

import Entity.Labor;
import Entity.Material;
import Repository.Interface.LaborRepositoryInter;
import Service.Interface.LaborServiceInter;

public class LaborService implements LaborServiceInter {
    private LaborRepositoryInter laborRepository;

    public LaborService(LaborRepositoryInter laborRepository) {
        this.laborRepository = laborRepository;
    }

    @Override
    public void addLabor(Labor labor) {
        laborRepository.addLabor(labor);
    }


    public double calculateLaborAfterVatRate(Labor labor) {
        double costBeforeVat = calculateLaborBeforeVatRate(labor);
        return costBeforeVat+(costBeforeVat*labor.getTauxTva()/100);
    }


    public double calculateLaborBeforeVatRate(Labor labor) {
        return labor.getHeuresTravail() *labor.getTauxHoraire() * labor.getProductuvuteOuvrier();
    }

}
