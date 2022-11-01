package se.ahmed.microservices.core.tollfee.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TollFeeRepository  extends CrudRepository<TollFeeEntity, String> {
    Optional<TollFeeEntity> findByCity(String city);
}
