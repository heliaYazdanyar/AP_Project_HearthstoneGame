package messages;

import com.google.gson.Gson;

public class Attack {
    private int authToken;

    private boolean hero;
    private boolean dead;
    private int damage;
    private String gameViewInfo;
    private int indexOfMinion;
    private String nameOfMinion;



    public Attack(int authToken,boolean hero,boolean dead,int damage,String gameViewInfo,String nameOfMinion,int indexOfMinion){
        this.authToken=authToken;
        this.hero=hero;
        this.dead=dead;
        this.gameViewInfo=gameViewInfo;
        this.indexOfMinion=indexOfMinion;
        this.nameOfMinion=nameOfMinion;

    }

    public int getAuthToken() {
        return authToken;
    }
    public boolean isHero() {
        return hero;
    }
    public boolean isDead() {
        return dead;
    }
    public int getDamage() {
        return damage;
    }
    public int getIndexOfMinion() {
        return indexOfMinion;
    }
    public String getGameViewInfo() {
        return gameViewInfo;
    }
    public String getNameOfMinion() {
        return nameOfMinion;
    }

    public String getJson(){
        String result="{}";
        return result;
    }
    public static Attack getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,Attack.class);
    }
}
