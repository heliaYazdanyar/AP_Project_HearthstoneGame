package Models;

public class QuestAndReward extends Card{

    private String name;
    private String type;
    private String rarity;
    private int manaCost;
    private String className;
    private int price;

    private String quest;
    private boolean questSpendMana;
    private int manaSpend;
    private String reward;

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

}
