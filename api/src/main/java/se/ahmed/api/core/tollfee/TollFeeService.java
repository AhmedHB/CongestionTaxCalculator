package se.ahmed.api.core.tollfee;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface TollFeeService {

    @GetMapping(
            value    = "/tollfee/{city}",
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    TollFee getTollFee(@PathVariable String city);

}
