package Service;

import Entity.Labor;
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

}
