package messages;

import com.google.gson.Gson;

public class AddWeapon {
    private int authToken;



    public String getJson(){
        String result="{}";
        return result;
    }
    public static AddWeapon getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,AddWeapon.class);
    }
}
