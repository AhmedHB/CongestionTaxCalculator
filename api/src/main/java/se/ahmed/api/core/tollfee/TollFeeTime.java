package se.ahmed.api.core.tollfee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@Data
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TollFeeTime {
    @NotEmpty(message = "startTime must not be empty")
    @JsonProperty("startTime")
    private String startTime;
    @NotEmpty(message = "endTime must not be empty")
    @JsonProperty("endTime")
    private String endTime;
    @NotNull(message = "fee must not be null")
    @JsonProperty("fee")
    private Integer fee;
}
