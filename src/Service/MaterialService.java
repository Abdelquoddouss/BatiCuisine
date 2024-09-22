package Service;

import Entity.Material;
import Repository.Interface.MaterialRepsitoryInter;
import Repository.MaterialRepository;

public class MaterialService implements Service.Interface.MaterialServiceInter {

    private MaterialRepsitoryInter materialRepository;

    public MaterialService(MaterialRepsitoryInter materialRepository) {
        this.materialRepository = materialRepository;
    }

    public void addMaterial(Material material) {
        materialRepository.creerMaterial(material);
    }

}
