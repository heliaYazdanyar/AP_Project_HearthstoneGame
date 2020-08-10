package messages;

import com.google.gson.Gson;

public class UseHeroPower {
    private int authToken;



    public String getJson(){
        String result="{}";
        return result;
    }
    public static UseHeroPower getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,UseHeroPower.class);
    }

}
