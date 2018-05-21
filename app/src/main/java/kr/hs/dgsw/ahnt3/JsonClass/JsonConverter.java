package kr.hs.dgsw.ahnt3.JsonClass;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

public class JsonConverter {
    public Object ConvertObjectToJson(String Json, int type){
        Object resultJson=null;
        try {
            JSONObject jsonObject = new JSONObject(Json);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            switch (type){
                case 1:
                    ResponseLoginJson tempLogin;
                    tempLogin   =   mapper.reader().forType(ResponseLoginJson.class).readValue(jsonObject.toString());
                    resultJson  =   (Object)tempLogin;
                    break;
                case 2:
                    ResponseOutJson tempOut;
                    tempOut     =   mapper.reader().forType(ResponseOutJson.class).readValue(jsonObject.toString());
                    resultJson  =   (Object)tempOut;
                    break;
            }
            return resultJson;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
