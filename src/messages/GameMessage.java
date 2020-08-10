package messages;

import com.google.gson.Gson;

public class GameMessage {
    private int authToken;

    private String gameName;
    private String subject;
    private String explanation;

    public GameMessage(int authToken,String gameName,String subject,String explanation){
        this.authToken=authToken;
        this.gameName=gameName;
        this.subject=subject;
        this.explanation=explanation;
    }

    public int getAuthToken(){
        return authToken;
    }
    public String getGameName(){
        return gameName;
    }
    public String getSubject(){
        return subject;
    }
    public String getExplanation(){
        return explanation;
    }



    public String getJson(){
        String result="{'authToken':"+this.authToken+",'subject':'"+this.subject+"','explanation':'"+explanation+"'}";
        return result;
    }
    public static GameMessage getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,GameMessage.class);
    }


}
