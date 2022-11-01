package se.ahmed.api.core.tollfee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
@Builder
@Getter
@EqualsAndHashCode
@Data
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TollFee {
    private int tollfeeId;
    @NotEmpty(message = "city must not be empty")
    @JsonProperty("city")
    private String city;
    @NotEmpty(message = "tollFeeTimes must not be empty")
    @JsonProperty("tollFeeTimes")
    private List<TollFeeTime> tollFeeTimes;
    @NotEmpty(message = "taxRules must not be empty")
    @JsonProperty("taxRules")
    private TaxRules taxRules;
}
