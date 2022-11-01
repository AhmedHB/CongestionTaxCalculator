package se.ahmed.microservices.core.tollfee.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RestController;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeService;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeEntity;
import se.ahmed.microservices.core.tollfee.persistence.TollFeeRepository;
import se.ahmed.util.exceptions.NotFoundException;
import se.ahmed.util.http.ServiceUtil;

@RestController
public class TollFeeServiceImpl implements TollFeeService {
    private static final Logger LOG = LoggerFactory.getLogger(TollFeeServiceImpl.class);

    private final TollFeeRepository repository;

    private final TollFeeMapper mapper;

    private final ServiceUtil serviceUtil;

    @Autowired
    public TollFeeServiceImpl(TollFeeRepository repository, TollFeeMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public TollFee getTollFee(String city) {
        TollFeeEntity entity = repository.findByCity(city)
                .orElseThrow(() -> new NotFoundException("No tollfees found for city: "+ city));

        TollFee tollFeeResponse = mapper.entityToApi(entity);
        LOG.debug("getTollFee: tollFeeResponse: {}/{}", tollFeeResponse, serviceUtil.getServiceAddress());

        return tollFeeResponse;
    }
}
