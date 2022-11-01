package se.ahmed.microservices.core.tollfee.services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeEntity;


@Mapper(componentModel = "spring")
public interface TollFeeMapper {

    TollFee entityToApi(TollFeeEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    TollFeeEntity apiToEntity(TollFee api);
}
