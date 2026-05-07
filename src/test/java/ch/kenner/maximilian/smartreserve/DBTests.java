package ch.kenner.maximilian.smartreserve;


import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.model.service.ServiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class DBTests {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void insertVehicle() {
        Service serviceObj = new Service();
        serviceObj.setName("Haircut");
        serviceObj.setDescription("Haircut");
        serviceObj.setDurationSeconds(180L);
        serviceObj.setAfterServiceBreakDurationSeconds(2L);
        serviceObj = serviceRepository.save(serviceObj);
        Assertions.assertNotNull(serviceObj.getId());
        Service serviceObj2 = new Service();
        serviceObj2.setName("Medium Haircut");
        serviceObj2.setDescription("Medium length haircut");
        serviceObj2.setDurationSeconds(180L);
        serviceObj2.setAfterServiceBreakDurationSeconds(2L);
        serviceObj2 = serviceRepository.save(serviceObj2);
        Assertions.assertNotNull(serviceObj2.getId());
    }
}
