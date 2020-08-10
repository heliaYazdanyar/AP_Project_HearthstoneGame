package messages;

import com.google.gson.Gson;

public class SendingCard {

    private int authToken;
    private String type;
    private String cardJson;

    public SendingCard(int authToken,String type,String cardJson){
        this.authToken=authToken;
        this.type=type;
        this.cardJson=cardJson;
    }
    public int getAuthToken(){
        return authToken;
    }
    public String getType(){
        return type;
    }
    public String getCardJson(){
        return cardJson;
    }

    public String getJson(){
        String result="{'authToken':"+authToken+",'type':"+type+",'cardJson':'"+cardJson+"'}";
        return result;
    }
    public static SendingCard getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,SendingCard.class);
    }


}
