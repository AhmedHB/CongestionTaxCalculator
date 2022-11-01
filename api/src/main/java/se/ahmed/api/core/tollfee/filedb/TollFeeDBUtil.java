package se.ahmed.api.core.tollfee.filedb;

import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeDB;

import java.util.List;
import java.util.Optional;

public class TollFeeDBUtil {
    private String jsonDB;
    private TollFeeDB tollFeeDB;

    public TollFeeDBUtil(String json){
        tollFeeDB  = TollFeeDBFactory.getTollFeeDBFactory(json);
    }

    public TollFee findByCity(String city){
        List<TollFee> tollFeeList = tollFeeDB.getTollFeeList();

        TollFee tollFee = tollFeeList.stream()
                .filter(x -> x.getCity().equalsIgnoreCase(city))
                .findFirst()
                .orElse(null);

        return tollFee;
    }

    public List<TollFee> findAll(){
        return tollFeeDB.getTollFeeList();
    }
}
