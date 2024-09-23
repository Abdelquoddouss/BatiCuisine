package Repository.Interface;

import Entity.Labor;
import Entity.Material;

import java.util.List;

public interface MaterialRepsitoryInter {
    void creerMaterial(Material material);

    List<Material> findAllMaterialsByProject(int projectId);
}
