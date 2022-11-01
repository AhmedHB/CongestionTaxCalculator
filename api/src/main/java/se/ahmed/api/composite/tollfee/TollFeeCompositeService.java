package se.ahmed.api.composite.tollfee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.ahmed.api.composite.tollfee.request.TollFeeRequest;
import se.ahmed.api.composite.tollfee.request.TollFeeTaxRequest;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;

import javax.validation.Valid;

@Api(value = "TollFeeCompositeService", description = "REST API for composite toll fee information.")
public interface TollFeeCompositeService {

    @ApiOperation(
            tags = "getTollFee",
            value = "${api.tollfee-composite.get-composite-tollfee.description}",
            notes = "${api.tollfee-composite.get-composite-tollfee.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @PostMapping("/tollfee-composite/fee")
    TollFeeResponse getTollFee(@Valid  @RequestBody TollFeeRequest request);

    @ApiOperation(
            tags = "getTollFeeTax",
            value = "${api.tollfee-composite.get-composite-tollfeetax.description}",
            notes = "${api.tollfee-composite.get-composite-tollfeetax.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @PostMapping("/tollfee-composite/tax")
    TollFeeTaxResponse getTax(@Valid  @RequestBody TollFeeTaxRequest request);

}
