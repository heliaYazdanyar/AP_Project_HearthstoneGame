package messages;

import com.google.gson.Gson;

public class SummonMinion {
    private int authToken;



    public String getJson(){
        String result="{}";
        return result;
    }
    public static SummonMinion getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,SummonMinion.class);
    }
}
