package models;

public class Minion extends Card implements MyCharacter{

    private String name;
    private String type;
    private String rarity;
    private int manaCost;
    private String className;
    private int HP;
    private int attack;
    private int price;

    private boolean taunt;
    private boolean battlecry;
    private boolean lifeSteal;
    private boolean rush;
    private boolean permanentCause;

    private String battlecryExplain;
    private String permanentCauseExplain;

    private int finalHP;

    @Override
    public String getName(){
        if (name==null) System.out.println("vaaa");
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
    public int getHP(){
        return HP;
    }
    public int getAttack(){
        return attack;
    }
    public int getPrice(){
        return price;
    }

    @Override
    public String getThisCardsJson() {
        String json="{'name':'"+name+"','type':'Minion','rarity':'"+rarity+"','className':'"+className+
                "','manaCost':"+manaCost+",'HP':"+HP+",'attack':"+attack+",'finalHP':"+finalHP+
                ",'taunt':"+taunt+",'battlecry':"+battlecry+",'lifeSteal':"+lifeSteal+
                ",'rush':"+rush+",'permanentCause':"+permanentCause+",'battlecryExplain':'','permanentCauseExplain':'','price':"+price+"}";
        return json;
    }

    public void setHP(int hp){
        if(hp>finalHP) this.HP=finalHP;
        else this.HP=hp;
    }
    public int getFinalHP(){
        return finalHP;
    }

    public String getBattlecryExplain(){
        return battlecryExplain;
    }
    public String getPermanentCauseExplain(){
        return permanentCauseExplain;
    }


    public void setAttack(int attack){
        this.attack=attack;
    }
    public void setTaunt(boolean b){
        this.taunt=b;
    }
    public boolean isTaunt(){return taunt; }
    public boolean isBattlecry() {
        return battlecry;
    }
    public boolean isLifeSteal() {
        return lifeSteal;
    }
    public boolean isPermanentCause() {
        return permanentCause;
    }
    public boolean isRush() {
        return rush;
    }


    public void summon(){
        if(battlecry){

        }
        if(taunt){

        }
        if(permanentCause){

        }
    }







}
