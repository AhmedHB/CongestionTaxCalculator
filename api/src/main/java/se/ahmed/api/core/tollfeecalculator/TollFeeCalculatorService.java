package se.ahmed.api.core.tollfeecalculator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;

import javax.validation.Valid;

public interface TollFeeCalculatorService {
    @PostMapping("/tollfeecalculation/fee")
    TollFeeResponse getTollFeeCalculation(@Valid @RequestBody TollFeeTax request);

    @PostMapping("/tollfeecalculation/tax")
    TollFeeTaxResponse getTollFeeTaxCalculation(@Valid @RequestBody TollFeeTax request);
}
