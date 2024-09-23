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


    public double calculateMaterialAfterVatRate(Material material) {
        double costBeforeVat = (material.getCoutUnitaire() * material.getQuantite() * material.getCoefficientQualite())+material.getCoutTransport();
        return costBeforeVat+(costBeforeVat*material.getTauxTva()/100);
    }

    public double calculateMaterialBeforeVatRate(Material material) {
        return (material.getCoutUnitaire()* material.getQuantite() * material.getCoefficientQualite()) + material.getCoutTransport();
    }


}
