package Service.Interface;

import Entity.Material;

import java.util.List;

public interface MaterialServiceInter {
    void addMaterial(Material material);
    List<Material> findAllMaterialsByProject(int projectId);

}
