package se.ahmed.api.core.tollfeecalculator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import se.ahmed.api.core.tollfee.TollFee;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class TollFeeTax {
    @NotNull
    @JsonProperty("vehicle")
    private String vehicle;
    @NotEmpty(message = "Input dates list cannot be empty.")
    @JsonProperty("dates")
    private List<String> dates;
    @NotNull
    @JsonProperty("tollFee")
    private TollFee tollFee;
}
