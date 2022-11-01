package se.ahmed.api.core.tollfee.filedb;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.ahmed.api.core.tollfee.TollFeeDB;

public class TollFeeDBFactory {

    public static TollFeeDB getTollFeeDBFactory(String json){
        TollFeeDB tollFeeDB = new TollFeeDB();
        try {
            ObjectMapper mapper = new ObjectMapper();
            tollFeeDB = mapper.readValue(json, TollFeeDB.class);
        }catch(Exception e){
        }
        return tollFeeDB;
    }

}
