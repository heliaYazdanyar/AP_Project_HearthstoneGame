package Models;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Hero implements MyCharacter {

    private String name;
    private int hp;
    private String heroPower;

    public Hero(String name){
        initFromJson(name);

    }


    private void initFromJson(String name){
        //getting file
        List<String> t=new ArrayList<>();
        try{
            t=Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "Heroes"+File.separator+name+".txt"));
        }catch (IOException e){
            e.printStackTrace();
        }
        Gson gson= new Gson();
        Hero hero=gson.fromJson(t.get(0),Hero.class);
        this.name=hero.getName();
        this.heroPower=hero.getHeroPower();
        this.hp=hero.getHP();
    }

    public static String getHeroJson(String heroName){
        List<String> t =null;
        try{
            String cwd1 = System.getProperty("user.dir");
            Path p= Paths.get(cwd1+File.separator+"resources"+File.separator+"Heroes"+File.separator+heroName+".txt");
            t= Files.readAllLines(p);
        }catch (IOException e){
            e.printStackTrace();
        }
        return t.get(0);
    }
    public static Hero getHero(String heroName){
        Gson gson= new Gson();
        Hero h=gson.fromJson(getHeroJson(heroName),Hero.class);
        return h;
    }

    @Override
    public String getName(){return name;}
    public String getType() {
        return "Hero";
    }
    public int getAttack() {
        return 0;
    }
    public int getHP(){return hp;}
    public void setHP(int hp){
        this.hp=hp;
    }

    public String getHeroPower(){ return heroPower; }
    public int getHeroPowerCost(){
        switch (heroPower){
            case"FrireBlast":
                return 2;
            case "StealMaster":
                return 3;
            case "Lifetab":
                return 2;
            case "Caltropes":
                return 0;
            case "Heal":
                return 2;
        }
        return 0;
    }

}
