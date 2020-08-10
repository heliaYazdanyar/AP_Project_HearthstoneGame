package messages;

import com.google.gson.Gson;

public class UseSpell {
    private int authToken;



    public String getJson(){
        String result="{}";
        return result;
    }
    public static UseSpell getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,UseSpell.class);
    }
}
