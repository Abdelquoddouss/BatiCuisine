package Service;

import Entity.Devi;
import Repository.DeviRepository;

public class DeviService {
    public DeviRepository deviRepository;

    public DeviService(DeviRepository deviRepository) {
        this.deviRepository = deviRepository;
    }

    public void save(Devi devi) {
        deviRepository.save(devi);
    }

    public boolean updateDevisStatus(int id) {
        return deviRepository.updateDevisStatus(id);

    }


}
