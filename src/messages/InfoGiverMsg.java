package messages;

import com.google.gson.Gson;

public class InfoGiverMsg {
    private int authToken;
    private String first;
    private String second;
    private String third;

    public InfoGiverMsg(int authToken,String first,String second,String third){
        this.authToken=authToken;
        this.first=first;
        this.second=second;
        this.third=third;
    }


    public int getAuthToken() {
        return authToken;
    }
    public String getFirst(){
        return first;
    }
    public String getSecond() {
        return second;
    }
    public String getThird(){
        return third;
    }

    public String getJson(){
        String result="{'authToken':"+authToken+",'first':"+first+",'second':'"+second+"','third':"+third+"}";
        return result;
    }
    public static InfoGiverMsg getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,InfoGiverMsg.class);
    }
}
