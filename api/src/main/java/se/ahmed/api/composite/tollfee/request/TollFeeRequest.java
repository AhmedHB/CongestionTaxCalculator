package se.ahmed.api.composite.tollfee.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import se.ahmed.api.composite.tollfee.validation.ValidDate;
import se.ahmed.api.composite.tollfee.validation.ValidVehicle;

import javax.validation.constraints.NotEmpty;

@Builder
@EqualsAndHashCode
@Data
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TollFeeRequest {
    @NotEmpty(message = "city must not be empty")
    @JsonProperty("city")
    @ApiModelProperty(notes = "City name", example = "gothenburg", required = true)
    private String city;
    @ValidVehicle
    @NotEmpty(message = "vehicle must not be empty")
    @JsonProperty("vehicle")
    @ApiModelProperty(notes = "Vehicle type", example = "car", required = true)
    private String vehicle;
    @NotEmpty(message = "date must not be empty")
    @ValidDate
    @JsonProperty("date")
    @ApiModelProperty(notes = "Date", example = "2013-06-13 08:00:00", required = true)
    private String date;
}
