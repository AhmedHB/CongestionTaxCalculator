package se.ahmed.microservices.composite.tollfee.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import se.ahmed.api.composite.tollfee.TollFeeCompositeService;
import se.ahmed.api.composite.tollfee.request.TollFeeRequest;
import se.ahmed.api.composite.tollfee.request.TollFeeTaxRequest;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfeecalculator.TollFeeTax;
import se.ahmed.util.http.ServiceUtil;

import java.util.ArrayList;

@RestController
public class TollFeeCompositeServiceImpl implements TollFeeCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(TollFeeCompositeServiceImpl.class);
    private final ServiceUtil serviceUtil;
    private TollFeeCompositeIntegration integration;

    @Autowired
    public TollFeeCompositeServiceImpl(ServiceUtil serviceUtil, TollFeeCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public TollFeeResponse getTollFee(TollFeeRequest request) {
        TollFee tollFeeResponse = integration.getTollFee(request.getCity());

        TollFeeTax tft = TollFeeTax.builder()
                .vehicle(request.getVehicle())
                .dates(new ArrayList<String>(){{add(request.getDate());}})
                .tollFee(tollFeeResponse)
                .build();

        TollFeeResponse tollFeeCalculation = integration.getTollFeeCalculation(tft);
        LOG.debug("getTollFee: tollFeeCalculation: {}/{}", tollFeeCalculation, serviceUtil.getServiceAddress());

        return tollFeeCalculation;
    }

    @Override
    public TollFeeTaxResponse getTax(TollFeeTaxRequest request) {
        TollFee tollFeeResponse = integration.getTollFee(request.getCity());

        TollFeeTax tft = TollFeeTax.builder()
                .vehicle(request.getVehicle())
                .dates(request.getDates())
                .tollFee(tollFeeResponse)
                .build();

        TollFeeTaxResponse tollFeeTaxCalculation = integration.getTollFeeTaxCalculation(tft);
        LOG.debug("getTax: tollFeeTaxCalculation: {}/{}", tollFeeTaxCalculation, serviceUtil.getServiceAddress());

        return tollFeeTaxCalculation;
    }
}
