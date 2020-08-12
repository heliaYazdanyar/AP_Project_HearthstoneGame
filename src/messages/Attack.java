package messages;

import com.google.gson.Gson;

public class Attack {
    private int authToken;
    private String gameName;

    //attacker
    private String kindOfAttacker;
    private int indexOfAttackerMinion;
    private int damage;


    //target
    private String kindOfTarget;
    private int indexOfTargetMinion;




    public Attack(int authToken,String gameName,String kindOfAttacker,int indexOfAttackerMinion
            ,int damage,String kindOfTarget,int indexOfTargetMinion){
        this.authToken=authToken;
        this.gameName=gameName;

        this.kindOfAttacker=kindOfAttacker;
        this.indexOfAttackerMinion=indexOfAttackerMinion;
        this.damage=damage;

        this.kindOfTarget=kindOfTarget;
        this.indexOfTargetMinion=indexOfTargetMinion;


    }

    public int getAuthToken() {
        return authToken;
    }
    public String getGameName(){
        return gameName;
    }

    public int getDamage() {
        return damage;
    }

    public String getKindOfAttacker(){
        return kindOfAttacker;
    }
    public int getIndexOfAttackerMinion() {
        return indexOfAttackerMinion;
    }

    public String getKindOfTarget() {
        return kindOfTarget;
    }

    public int getIndexOfTargetMinion() {
        return indexOfTargetMinion;
    }

    public String getJson(){
        String result="{'authToken':"+this.authToken+",'gameName':'"+gameName+"','kindOfAttacker':'"+kindOfAttacker+"','indexOfAttackerMinion':"+indexOfAttackerMinion+
                ",'damage':"+damage+",'kindOfTarget':'"+kindOfTarget+"','indexOfTargetMinion':"+indexOfTargetMinion
                +"}";
        return result;
    }
    public static Attack getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,Attack.class);
    }
}
