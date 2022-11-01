package se.ahmed.microservices.core.tollfee.persistence;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFeeTime;

import java.util.List;

@Data
@Document(collection="tollfees")
@CompoundIndex(name = "tollfee-city-id", unique = true, def = "{'tollfeeId': 1, 'city' : 1}")
public class TollFeeEntity {
    @Id
    private String id;

    @Version
    private Integer version;

    private int tollfeeId;
    private String city;
    private List<TollFeeTime> tollFeeTimes;
    private TaxRules taxRules;
}
