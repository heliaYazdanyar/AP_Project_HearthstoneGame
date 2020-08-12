package messages;

import com.google.gson.Gson;

public class SendingVictim {
    private int authToken;
    private String gameName;

    private String victimType;
    private int indexOfMinionVictim;

    public SendingVictim(int authToken,String gameName,String victimType,int indexOfMinionVictim){
        this.authToken=authToken;
        this.gameName=gameName;
        this.victimType=victimType;
        this.indexOfMinionVictim=indexOfMinionVictim;
    }

    public int getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public int getIndexOfMinionVictim() {
        return indexOfMinionVictim;
    }

    public String getVictimType() {
        return victimType;
    }

    public String getJson(){
        String result="{'authToken':"+this.authToken+",'gameName':'"+gameName+"','victimType':'"+victimType+"','indexOfMinionVictim':"+indexOfMinionVictim +"}";
        return result;
    }
    public static SendingVictim getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,SendingVictim.class);
    }

}
