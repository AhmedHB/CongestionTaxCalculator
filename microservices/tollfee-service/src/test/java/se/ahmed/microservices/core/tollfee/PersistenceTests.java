package se.ahmed.microservices.core.tollfee;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeEntity;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeRepository;
import se.ahmed.microservices.core.tollfee.services.TollFeeMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PersistenceTests {
    private TollFeeMapper mapper = Mappers.getMapper(TollFeeMapper.class);
    @Autowired
    private TollFeeRepository repository;

    private TollFeeEntity savedEntity;

    @Before
   	public void setupDb() {
   		repository.deleteAll();

        TollFee api = TollFee.builder()
                .city("alingsås")
                .tollFeeTimes(new ArrayList<TollFeeTime>(){{add(new TollFeeTime("06:00", "06:29", 8));}})
                .taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{add("car");add("Motorcycle");}}).build())
                .build();

        TollFeeEntity entity = mapper.apiToEntity(api);

        savedEntity = repository.save(entity);

        assertEqualsTollFee(entity, savedEntity);
    }


    @Test
   	public void create() {

        TollFee api = TollFee.builder()
                .city("avesta")
                .tollFeeTimes(new ArrayList<TollFeeTime>(){{add(new TollFeeTime("06:00", "06:29", 8));}})
                .taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{add("car");add("Motorcycle");}}).build())
                .build();

        TollFeeEntity newEntity = mapper.apiToEntity(api);

        repository.save(newEntity);

        TollFeeEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertEqualsTollFee(newEntity, foundEntity);

        assertEquals(2, repository.count());
    }

    @Test
   	public void update() {
        savedEntity.setCity("malmö");
        repository.save(savedEntity);

        TollFeeEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("malmö", foundEntity.getCity());
    }

    @Test
   	public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
   	public void getByProductId() {
        Optional<TollFeeEntity> entity = repository.findByCity(savedEntity.getCity());

        assertTrue(entity.isPresent());
        assertEqualsTollFee(savedEntity, entity.get());
    }

    @Test(expected = DuplicateKeyException.class)
   	public void duplicateError() {
        TollFee api = TollFee.builder()
                .city(savedEntity.getCity())
                .tollFeeTimes(new ArrayList<TollFeeTime>(){{add(new TollFeeTime("06:00", "06:29", 8));}})
                .taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{add("car");add("Motorcycle");}}).build())
                .build();
        TollFeeEntity newEntity = mapper.apiToEntity(api);
        repository.save(newEntity);
    }

    @Test
   	public void optimisticLockError() {

        // Store the saved entity in two separate entity objects
        TollFeeEntity entity1 = repository.findById(savedEntity.getId()).get();
        TollFeeEntity entity2 = repository.findById(savedEntity.getId()).get();

        // Update the entity using the first entity object
        entity1.setCity("upsala");
        repository.save(entity1);

        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        try {
            entity2.setCity("luleå");
            repository.save(entity2);

            fail("Expected an OptimisticLockingFailureException");
        } catch (OptimisticLockingFailureException e) {}

        // Get the updated entity from the database and verify its new sate
        TollFeeEntity updatedEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (int)updatedEntity.getVersion());
        assertEquals("upsala", updatedEntity.getCity());
    }

    private void assertEqualsTollFee(TollFeeEntity expectedEntity, TollFeeEntity actualEntity) {
        assertEquals(expectedEntity.getId(),               actualEntity.getId());
        assertEquals(expectedEntity.getVersion(),          actualEntity.getVersion());
        assertEquals(expectedEntity.getTollfeeId(),        actualEntity.getTollfeeId());
        assertEquals(expectedEntity.getCity(),        actualEntity.getCity());
        assertEquals(expectedEntity.getTollFeeTimes().size(), actualEntity.getTollFeeTimes().size());
    }
}
