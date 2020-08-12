package messages;

import com.google.gson.Gson;

public class UseHeroPower {
    private int authToken;
    private String gameName;

    public UseHeroPower(int authToken,String gameName){
        this.authToken=authToken;
        this.gameName=gameName;
    }

    public int getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public String getJson(){
        String result="{'authToken':"+authToken+"'gameName':'"+gameName+"'}";
        return result;
    }
    public static UseHeroPower getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,UseHeroPower.class);
    }

}
