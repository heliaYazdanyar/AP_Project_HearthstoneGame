package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDeck implements Decks{

    private Hero hero;
    private List<String> cardsNames;
    private List<Card> cards;

    public RandomDeck(){
        cardsNames=new ArrayList<>();
        cards=new ArrayList<>();

        Random r=new Random();
        initFromFile(r.nextInt(3));
        initCards();

    }

    private void initFromFile(int rand){
        try {
            List<String> list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+
                    "resources"+File.separator+"randomDecks"+File.separator+"deck_"+rand+".txt"));
            hero=Hero.getHero(list.get(0));
            list.remove(0);
            cardsNames.addAll(list);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void initCards(){
        for(int i=0;i<cardsNames.size();i++){
            cards.add(Card.getCard(cardsNames.get(i)));
        }
    }

    @Override
    public String getHeroName() {
        return hero.getName();
    }
    public List<Card> getDeckCards() {
        return cards;
    }




}
