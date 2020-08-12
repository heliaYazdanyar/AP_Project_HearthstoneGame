package messages;

import com.google.gson.Gson;

public class GameChatMessage {
    private int authToken;
    private String gameName;
    private String message;

    public GameChatMessage(int authToken, String gameName, String message){
        this.authToken=authToken;
        this.gameName=gameName;
        setMessage(message);
    }

    public int getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public String getMessage() {
        for(int i=0;i<message.length();i++){
            if(message.charAt(i)=='*'){
                String first=message.substring(0,i);
                message=first+" "+message.substring(i+1);
            }
        }
        return message;
    }


    private void setMessage(String message){
        for(int i=0;i<message.length();i++){
            if(message.charAt(i)==' '){
                String first=message.substring(0,i);
                message=first+"*"+message.substring(i+1);
            }
        }
        this.message=message;
    }

    public String getJson(){
        String result="{'authToken':"+this.authToken+",'gameName':'"+gameName+"','message':'"+message+"'}";
        return result;
    }
    public static GameChatMessage getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json, GameChatMessage.class);
    }
}
