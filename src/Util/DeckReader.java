package Util;

import Models.Card;
import Models.QuestAndReward;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeckReader {

    private String[] enemy;
    private String[] friend;

    public List<Card> getEnemyDeck(){
        List<Card> enemyDeck=new ArrayList<>();
        for(int i=0;i<enemy.length;i++){
            if(enemy[i].contains("->")){
                int index=enemy[i].indexOf('-');
                String quest=enemy[i].substring(0,index);
                String reward=enemy[i].substring(index+9);

                QuestAndReward qr=(QuestAndReward) Card.getCard(quest);
                qr.setReward(reward);

                enemyDeck.add(qr);

            }
            else enemyDeck.add(Card.getCard(enemy[i]));
        }

        return enemyDeck;
    }
    public List<Card> getFriendDeck(){
        List<Card> friendDeck=new ArrayList<>();
        for(int i=0;i<friend.length;i++){
            if(friend[i].contains("->")){
                int index=friend[i].indexOf('-');
                String quest=friend[i].substring(0,index);
                String reward=friend[i].substring(index+9);

                QuestAndReward qr=(QuestAndReward) Card.getCard(quest);
                qr.setReward(reward);

                friendDeck.add(qr);

            }
            else friendDeck.add(Card.getCard(friend[i]));
        }

        return friendDeck;
    }



    public static String getFromJson(){
        List<String> t =null;
        try {
            Path f = Paths.get( System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "deckReaders"+File.separator+"deckReader_0.txt");
            t= Files.readAllLines(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t.get(0);
    }
    public static DeckReader getDeckReader(){
        Gson gson= new Gson();
        DeckReader dr=gson.fromJson(getFromJson(), DeckReader.class);
        return dr;
    }

}