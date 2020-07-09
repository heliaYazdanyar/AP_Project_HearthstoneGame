package Models;

public class Weapon extends Card{

    private String name;
    private String type;
    private String rarity;
    private int manaCost;
    private String className;
    private int attack;
    private int shield;
    private int price;


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

    public int getAttack(){
        return attack;
    }
    public int getShield(){
        return shield;
    }
    public void setShield(int shield){this.shield=shield;}
    public void setAttack(int a){
        this.attack=a;
    }
    public void lessenShield(){
        this.shield--;
    }



}
