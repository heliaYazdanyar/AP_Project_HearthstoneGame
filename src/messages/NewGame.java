package messages;

import com.google.gson.Gson;

public class NewGame {

    private String gameName;
    private String enemyUsername;
    private String enemyHeroName;
    private int enemyCups;

    public NewGame(String gameName,String enemyUsername,String enemyHeroName){
        this.gameName=gameName;
        this.enemyUsername=enemyUsername;
        this.enemyHeroName=enemyHeroName;
        this.enemyCups=0;
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


    public String getJson(){
        String result="{'gameName':'"+this.gameName+"','enemyUsername':'"+this.enemyUsername+"','enemyHeroName':'"
                +this.enemyHeroName+"','enemyCups':"+this.enemyCups+"}";
        return result;
    }
    public static NewGame getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,NewGame.class);
    }

}
