package Models;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Card {

    private static List<String> spells=new ArrayList<>();
    private static List<String> minions=new ArrayList<>();
    private static List<String> weapons=new ArrayList<>();
    private static List<String> questAndRewards=new ArrayList<>();


    public abstract String getName();
    public abstract String getType();
    public abstract String getRarity();
    public abstract String getClassName();
    public abstract int getManaCost();
    public abstract int getPrice();


    public static List<String> getList(String nameOfList){
        if(nameOfList.equalsIgnoreCase("spells")) return spells;
        else if(nameOfList.equalsIgnoreCase("minions")) return minions;
        else if(nameOfList.equalsIgnoreCase("weapons")) return weapons;
        else return questAndRewards;
    }

    private static void loadLists(){
        try{
            Path p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"spells.txt");
            spells=Files.readAllLines(p);
            p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"minions.txt");
            minions=Files.readAllLines(p);
            p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"weapons.txt");
            weapons=Files.readAllLines(p);
            p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"questAndRewards.txt");
            questAndRewards=Files.readAllLines(p);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static String getCardJson(String nameOfCard) {
        String type="";
        if(spells.contains(nameOfCard))
            type="spells";
        if(minions.contains(nameOfCard))
            type="minions";
        if(weapons.contains(nameOfCard))
            type="weapons";
        if(questAndRewards.contains(nameOfCard))
            type="questAndRewards";
        List<String> t =null;
        try {
            Path f = Paths.get( System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "allCards"+File.separator+type+File.separator+nameOfCard + ".txt");
            t= Files.readAllLines(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t.get(0);
    }
    public static Card getCard(String nameOfCard){
        loadLists();
        Gson gson= new Gson();
        Card card;
        if(spells.contains(nameOfCard)) {
            card = gson.fromJson(getCardJson(nameOfCard), Spell.class);
            return card;
        }
        if(minions.contains(nameOfCard)){
            card=gson.fromJson(getCardJson(nameOfCard),Minion.class);
            return card;
        }
        if(weapons.contains(nameOfCard)) {
            card=gson.fromJson(getCardJson(nameOfCard),Weapon.class);
            return card;
        }
        if(questAndRewards.contains(nameOfCard)) {
            card=gson.fromJson(getCardJson(nameOfCard),QuestAndReward.class);
            return card;
        }
        return null;

    }

}




