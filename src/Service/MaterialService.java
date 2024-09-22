package Service;

import Entity.Material;
import Repository.MaterialRepository;

public class MaterialService {

    private MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public void addMaterial(Material material) {
        materialRepository.addMaterial(material);
    }

}
