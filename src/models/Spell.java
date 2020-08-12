package models;

public class Spell extends Card {

    private String name;
    private String type;
    private String rarity;
    private int manaCost;
    private String className;
    private int price;

    private boolean drawCard;
    private boolean manaBurn;
    private boolean summon;
    private boolean dealDamage;
    private boolean discover;
    private boolean changeMinion;
    private boolean divineShield;

    private String summonWhat;
    private String drawCardExp;
    private String discoverExp;
    private int damageCnt;
    private String damageCondition;
    private String changeMinionExp;


    @Override
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getRarity(){
        return rarity;
    }
    public String getClassName(){
        return className;
    }
    public int getManaCost(){
        return manaCost;
    }
    public int getPrice(){
        return price;
    }

    @Override
    public String getThisCardsJson() {
        String json="{'name':'"+name+"','type':'Spell','rarity':'"+rarity+"','manaCost':"+manaCost+",'className':'"+
                className+"','price':"+price+",'drawCard':"+drawCard+",'manaBurn':"+manaBurn+",'summon':"+summon+
                ",'dealDamage':"+dealDamage+",'discover':"+discover+",'changeMinion':"+changeMinion+
                ",'divineShield':"+divineShield+",'summonWhat':'','drawCardExp':'','discoverExp':'','damageCnt':"+damageCnt+",'damageCondition':'','changeMinionExp':''}";
        return json;
    }


    public boolean isDrawCard(){ return drawCard;}
    public boolean isManaBurn(){ return manaBurn;}
    public boolean summonCards(){ return summon;}
    public boolean dealsDamage(){ return  dealDamage;}
    public boolean isDiscover(){return discover;}
    public boolean changesMinion(){return changeMinion;}
    public boolean isDivineShield(){return divineShield;}

    public String getSummonWhat(){return summonWhat;}
    public String getChangeMinionExp() { return changeMinionExp; }
    public String getDamageCondition() { return damageCondition; }
    public String getDiscoverExp() { return discoverExp; }
    public int getDamageCnt() { return damageCnt; }
    public String getDrawCardExp() { return drawCardExp; }

    public void attack(MyCharacter enemy){

    }


    public void applySpell(){
        if(drawCard){

        }
        if(manaBurn){

        }
    }
}
