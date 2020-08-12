package messages;

import com.google.gson.Gson;

public class NewGame {

    private String gameName;
    private String enemyUsername;
    private String enemyHeroName;
    private int enemyCups;
    private int turnNumber;
    private boolean deckReader;

    public NewGame(String gameName,String enemyUsername,String enemyHeroName,int turnNumber,boolean deckReader){
        this.gameName=gameName;
        this.enemyUsername=enemyUsername;
        this.enemyHeroName=enemyHeroName;
        this.enemyCups=0;
        this.turnNumber=turnNumber;
        this.deckReader=deckReader;
    }

    public String getGameName() {
        return gameName;
    }
    public String getEnemyUsername(){
        return enemyUsername;
    }
    public  String getEnemyHeroName(){
        return enemyHeroName;
    }
    public int getTurnNumber(){
        return turnNumber;
    }
    public boolean isDeckReader() {
        return deckReader;
    }

    public String getJson(){
        String result="{'gameName':'"+this.gameName+"','enemyUsername':'"+this.enemyUsername+"','enemyHeroName':'"
                +this.enemyHeroName+"','enemyCups':"+this.enemyCups+",'turnNumber':"+turnNumber+",'deckReader':"+deckReader+"}";
        return result;
    }
    public static NewGame getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,NewGame.class);
    }

}
