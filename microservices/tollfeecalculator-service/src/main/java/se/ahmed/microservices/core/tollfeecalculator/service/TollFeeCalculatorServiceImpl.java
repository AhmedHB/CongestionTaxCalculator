package se.ahmed.microservices.core.tollfeecalculator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfeecalculator.TollFeeCalculatorService;
import se.ahmed.api.core.tollfeecalculator.TollFeeTax;
import se.ahmed.api.core.tollfeecalculator.calculation.CongestionTaxCalculator;
import se.ahmed.api.core.tollfeecalculator.vehicle.Vehicle;
import se.ahmed.api.core.tollfeecalculator.vehicle.VehicleFactory;
import se.ahmed.util.http.ServiceUtil;

import java.util.List;

@RestController
public class TollFeeCalculatorServiceImpl implements TollFeeCalculatorService {
    private static final Logger LOG = LoggerFactory.getLogger(TollFeeCalculatorServiceImpl.class);

    private final ServiceUtil serviceUtil;

    @Autowired
    public TollFeeCalculatorServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public TollFeeResponse getTollFeeCalculation(TollFeeTax request) throws ResponseStatusException{
        Vehicle vehicle = VehicleFactory.getVehile(request.getVehicle());
        List<String> dates = request.getDates();
        TollFee tollFee = request.getTollFee();

        int tollfeeResult = CongestionTaxCalculator.GetTollFee(dates.get(0), vehicle, tollFee);

        LOG.debug("getTollFeeCalculation: tollfeeResult: {}/{}", tollfeeResult, serviceUtil.getServiceAddress());

        TollFeeResponse tollFeeResponse = new TollFeeResponse();
        tollFeeResponse.setFee(tollfeeResult);
        return tollFeeResponse;
    }

    @Override
    public TollFeeTaxResponse getTollFeeTaxCalculation(TollFeeTax request) throws ResponseStatusException {
        Vehicle vehicle = VehicleFactory.getVehile(request.getVehicle());
        List<String> dates = request.getDates();
        TollFee tollFee = request.getTollFee();

        int tollFeeTaxResult = CongestionTaxCalculator.GetTollFeeTax(dates, vehicle, tollFee);
        LOG.debug("getTollFeeTaxCalculation: tollfeeResult: {}/{}", tollFeeTaxResult, serviceUtil.getServiceAddress());

        TollFeeTaxResponse tollFeeTaxResponse = new TollFeeTaxResponse();
        tollFeeTaxResponse.setTax(tollFeeTaxResult);
        return tollFeeTaxResponse;
    }
}
