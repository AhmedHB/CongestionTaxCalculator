package se.ahmed.microservices.composite.tollfee.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeService;
import se.ahmed.api.core.tollfeecalculator.TollFeeCalculatorService;
import se.ahmed.api.core.tollfeecalculator.TollFeeTax;
import se.ahmed.util.exceptions.InvalidInputException;
import se.ahmed.util.exceptions.NotFoundException;
import se.ahmed.util.http.HttpErrorInfo;

import java.io.IOException;

@Component
public class TollFeeCompositeIntegration implements TollFeeService, TollFeeCalculatorService {

    private static final Logger LOG = LoggerFactory.getLogger(TollFeeCompositeIntegration.class);

    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    private final String tollFeeServiceUrl;

    private final String tollFeeCalculationServiceUrl;

    @Autowired
    public TollFeeCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,

            @Value("${app.tollfee-service.host}") String tollFeeServiceHost,
            @Value("${app.tollfee-service.port}") int    tollFeeServicePort,

            @Value("${app.tollfeecalculator-service.host}") String tollFeeCalculationServiceHost,
            @Value("${app.tollfeecalculator-service.port}") int    tollFeeCalculationServicePort

    ){
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        tollFeeServiceUrl = "http://" + tollFeeServiceHost + ":" + tollFeeServicePort + "/tollfee/";
        tollFeeCalculationServiceUrl = "http://" + tollFeeCalculationServiceHost + ":" + tollFeeCalculationServicePort + "/tollfeecalculation/";
    }

    @Override
    public TollFee getTollFee(String city) {

        try{
            String url = tollFeeServiceUrl + city;
            LOG.debug("Will call getTollFee API on URL: {}", url);

            TollFee response = restTemplate.getForObject(url, TollFee.class);
            return response;

        }catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY :
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }
    }

    @Override
    public TollFeeResponse getTollFeeCalculation(TollFeeTax request) {
        try{
            String url = tollFeeCalculationServiceUrl + "fee";
            LOG.debug("Will call getTollFeeCalculation API on URL: {}", url);

            TollFeeResponse response = restTemplate.postForObject(url, request, TollFeeResponse.class);
            return response;


        }catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY :
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }
    }

    @Override
    public TollFeeTaxResponse getTollFeeTaxCalculation(TollFeeTax request) {
        try{
            String url = tollFeeCalculationServiceUrl + "tax";
            LOG.debug("Will call getTollFeeCalculation API on URL: {}", url);

            TollFeeTaxResponse response = restTemplate.postForObject(url, request, TollFeeTaxResponse.class);
            return response;
        }catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY :
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
