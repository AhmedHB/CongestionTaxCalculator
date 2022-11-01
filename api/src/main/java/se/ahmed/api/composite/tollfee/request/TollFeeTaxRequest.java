package se.ahmed.api.composite.tollfee.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import se.ahmed.api.composite.tollfee.validation.ValidDateList;
import se.ahmed.api.composite.tollfee.validation.ValidVehicle;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Builder
@EqualsAndHashCode
@Data
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TollFeeTaxRequest {
    @NotEmpty(message = "city must not be empty")
    @JsonProperty("city")
    @ApiModelProperty(notes = "City name", example = "gothenburg", required = true)
    private String city;
    @ValidVehicle
    @NotEmpty(message = "vehicle must not be empty")
    @JsonProperty("vehicle")
    @ApiModelProperty(notes = "Vehicle type", example = "car", required = true)
    private String vehicle;
    @ValidDateList
    @NotEmpty(message = "dates list must not be empty")
    @JsonProperty("dates")
    @ApiModelProperty(notes = "Dates", example = "[\"2013-01-14 07:30:00\", \"2013-01-14 08:00:00\"]", required = true)
    private List<String> dates;
}
