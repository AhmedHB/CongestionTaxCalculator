package se.ahmed.microservices.core.tollfee;

import org.junit.Test;
import org.mapstruct.factory.Mappers;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeEntity;
import se.ahmed.microservices.core.tollfee.services.TollFeeMapper;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTests {

    private TollFeeMapper mapper = Mappers.getMapper(TollFeeMapper.class);

    @Test
    public void mapperTests() {

        assertNotNull(mapper);

        TollFee api = TollFee.builder()
                .city("stockholm")
                .tollFeeTimes(new ArrayList<TollFeeTime>(){{add(new TollFeeTime("06:00", "06:29", 8));}})
                .taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{add("car");add("Motorcycle");}}).build())
                .build();

        TollFeeEntity entity = mapper.apiToEntity(api);

        assertEquals(api.getCity(), entity.getCity());
        assertEquals(api.getTollFeeTimes().size(), entity.getTollFeeTimes().size());

        TollFee api2 = mapper.entityToApi(entity);

        assertEquals(api.getCity(), api2.getCity());
        assertEquals(api.getTollFeeTimes().size(), api2.getTollFeeTimes().size());
    }
}
