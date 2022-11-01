package se.ahmed.api.core.tollfee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Builder
@EqualsAndHashCode
@Data
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaxRules {
    @NotEmpty(message = "tollFreeVehicle must not be empty")
    @JsonProperty("tollFreeVehicle")
    private List<String> tollFreeVehicle;
    @JsonProperty("useTollFreeDay")
    private boolean useTollFreeDay;
}
