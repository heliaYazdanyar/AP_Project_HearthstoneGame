package gamePlayers;

import models.Card;
import models.Deck;
import models.Hero;
import Out.MainFrame;
import Out.Status;
import Out.Store;
import com.google.gson.Gson;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import static util.Logger.createNewLog;
import static util.Logger.logger;

public class Player {
    private String username;
    private static String password;
    private List<String> myCards;
    private gamePlayers.Player.Information playersInfo;

    public List<Deck> allDecks=new ArrayList<>();
    public boolean ifDeckSelected=false;
    static Deck currentDeck;

    public Player(String username){
        this.username=username;
        myCards=this.getMyCardsNames();
        playersInfo=getInfoFromJson();
        initDecks();
    }

    private void initDecks(){
        allDecks.removeAll(allDecks);
        List<String> list=getDecksList();
        for (String st:
                list) {
            allDecks.add(new Deck(this,st));
        }
    }

    public String getUsername(){
        return this.username;
    }
    public void setCurrentDeck(Deck deck){
        ifDeckSelected=true;
        this.currentDeck=deck;
    }
    public Deck getCurrentDeck(){
        return currentDeck;
    }

    public String getHeroName() {
        return currentDeck.getHeroName();
    }
    public Hero getHero() {
        return Hero.getHero(getHeroName());
    }



    public static class Information{
        private int cups;
        private int diamonds;
        private String currentDeck;
        private double winsOfAllGames;
        private int wins;
        private int loses;
        private int gamesWithThisHand;
        private String heroName;
        private int allGames;

        public Path getPath(String username){
            return Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+"users"
                    +File.separator+username+File.separator+"Information.txt");
        }

        public int getDiamonds(){
            return this.diamonds;
        }
        public void addDiamonds(int price,String user){
            this.diamonds=this.diamonds+price;
            saveInfo(user);
        }
        public void giveDiamonds(int price,String user){
            this.diamonds=diamonds-price;
            saveInfo(user);
        }

        public int getCups(){
            return cups;
        }
        public void addCup(){
            cups++;
        }
        public void loseCup(){
            cups--;
        }

        public void win(String user){
            this.wins++;
            saveInfo(user);
        }
        public void lose(String username){
            this.loses++;
            saveInfo(username);
        }
        public void addToAllGames(String user){
            allGames++;
            saveInfo(user);
        }

        public int getAllGames(){
            return allGames;
        }

        public void saveInfo(String user){
            String result="{"+"'cups':"+cups+",'diamonds':"+diamonds+",'nameOfHandCard':'"+currentDeck+"','winsOfAllGames':"+wins+",'loses':"+loses
                    +",'gamesWithThisHand':"+gamesWithThisHand+",'heroName':'"+heroName+"','allGames':"+allGames+"}";
            try{
                Files.write(getPath(user),result.getBytes());
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }
    private String getInfoJson(){
        List<String> t =new ArrayList<>();
        try {
            Path f = Paths.get( System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "Users"+File.separator+username+File.separator+"Information.txt");
            t= Files.readAllLines(f);
        } catch (IOException e) { e.printStackTrace(); }

        return t.get(0);
    }
    private gamePlayers.Player.Information getInfoFromJson(){
        Gson gson= new Gson();
        gamePlayers.Player.Information Info=gson.fromJson(getInfoJson(), gamePlayers.Player.Information.class);
        return Info;
    }

    public gamePlayers.Player.Information getPlayersInfo(){
        this.playersInfo=getInfoFromJson();
        return playersInfo;
    }


    public void update(){
        myCards=this.getMyCardsNames();
        initDecks();

    }

    public List<String> getMyCardsNames(){
        List<String> result=new ArrayList<>();
        try{
            Path p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"myCards.txt");
            result=Files.readAllLines(p);
        }catch (IOException e){ e.printStackTrace();}
        return result;
    }

    public void sellCard(Card card){
        logger(this.getUsername(),"sell_card",card.getName());
        boolean possible=true;
        String deckname="";
        for (Deck deck:
                allDecks) {
            if(deck.getDeckCards().contains(card.getName())){
                possible=false;
                deckname=deck.getName();
                break;
            }
        }
        if(possible) {
            if (myCards.contains(card.getName())) {
                List<String> cards = new ArrayList();
                try {
                    Path p = Paths.get(System.getProperty("user.dir") +File.separator+"resources"+File.separator+
                            "users"+File.separator + this.getUsername() + File.separator+"myCards.txt");
                    cards = Files.readAllLines(p);
                    cards.remove(card.getName());
                    this.playersInfo.addDiamonds(card.getPrice(),this.getUsername());
                    Files.write(p, cards);
                    this.update();
                    Store.getInstance().update("CardsToSell");
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), "Card successfully Sold =)",
                            "Massage", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), "You Don't have this card :(",
                        "Oops!", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Remove this cards from -"+deckname+"- frist!",
                    "Oops!", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void buyCard(Card card){
        logger(this.getUsername(),"buy_card",card.getName());
        if(playersInfo.getDiamonds()>=card.getPrice()){
            List<String> cards;
            try{
                Path p=Paths.get(System.getProperty("user.dir") +File.separator+"resources"+File.separator+
                        "users"+File.separator+ this.getUsername() +File.separator+"myCards.txt");
                cards=Files.readAllLines(p);
                cards.add(card.getName());
                Files.write(p,cards);
                this.playersInfo.giveDiamonds(card.getPrice(),this.getUsername());
                this.update();
                Store.getInstance().update("CardsToBuy");
                JOptionPane.showMessageDialog(MainFrame.getInstance(),"Card successfully bought =)",
                        "Massage",JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){e.printStackTrace();}

        }
        else{
            JOptionPane.showMessageDialog(MainFrame.getInstance(),"You Don't enough Diamonds :(",
                    "Oops!",JOptionPane.ERROR_MESSAGE);
        }

    }


    public boolean newDeckPossible(String name,String heronName){
        List<String> names=new ArrayList<>();
        try{
            names=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"ListOfDecks.txt"));
        }catch (IOException e){
            e.printStackTrace();
        }
        if(names.contains(name)){
            JOptionPane.showMessageDialog(MainFrame.getInstance(),"You already have a deck with this name!",
                    "ERROR",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else{
            return true;
        }
    }
    public void newDeck(String name,String heronName){
        try{
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+ File.separator+"ListOfDecks.txt"),(name+"\n").getBytes(),StandardOpenOption.APPEND);
            Files.createFile(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"Decks"+File.separator+name+".txt"));
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"Decks"+File.separator+name+".txt"),heronName.getBytes());
            Files.createFile(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"Decks"+File.separator+"status"+File.separator+
                    name+".txt"));
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+"Decks"+File.separator+"status"+File.separator+
                    name+".txt"),("{name:'"+name+"',heroName:'"+heronName+
                    "',wins:0,numberOfAllGames:0,cards:['','','','','','','','','','','','','','',''],"+
                    "cardsCnt:[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}").getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        initDecks();
        Status.getInstance().update();
    }

    public List<String> getDecksList(){
        List<String> result=new ArrayList<>();
        try{
            result=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+this.getUsername()+File.separator+ "ListOfDecks.txt"));
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    private static void createPlayersFiles(String username,String password){
        //create ownDirectory
        String cwd = System.getProperty("user.dir");
        try {
            //directory
            Path p= Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+username);
            Files.createDirectory(p);

            //decks directory
            p= Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+username+File.separator+"Decks");
            Files.createDirectory(p);

            //list of Decks
            p= Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+username+File.separator+"ListOfDecks.txt");
            Files.createFile(p);

            //directory for statusDeck
            p= Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+username+
                    File.separator+"Decks"+File.separator+"status");
            Files.createDirectory(p);


            //owned Cards txtFile
            Files.createFile(Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+
                    username+File.separator+"myCards.txt"));
            Files.copy(Paths.get(cwd+File.separator+"resources"+File.separator+"allCards"+File.separator+
                            "Lists"+File.separator+"FirstCollection.txt"),
                    Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+
                            username+File.separator+"myCards.txt"), StandardCopyOption.REPLACE_EXISTING);

            //Information Json
            String info="{'cups':0,'diamonds':50,'nameOfHandCard':'','winsOfAllGames':0,'wins':0,'loses':0,'gamesWithThisHand':0,'heroName':'','allGames':0}";
            p=Paths.get(cwd+File.separator+"resources"+File.separator+"users"+File.separator+
                    username+File.separator+"Information.txt");
            Files.createFile(p);
            Files.write(p,info.getBytes());

            //directory of events
            Files.createDirectory(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+username+File.separator+"Events"));

        } catch (IOException e) { e.printStackTrace(); }
    }
    public static boolean signIn(String username,String password){
        List<String> users=readTxtFile("users");
        boolean possible=true;
        for (String name:
                users) {
            int m=0;
            for(int i=0;i<name.length();i++){
                if(name.charAt(i)==' ') {
                    m=i;
                    break;
                }
            }
            if(name.substring(0,m).equals(username)){
                possible=false;
                break;
            }
        }
        if(possible){
            List<String> text=new ArrayList<>(); text.add("\n"+username+" "+password);
            //save name
            writeInTxtFile("users",text);

            createPlayersFiles(username,password);

            //creat Log
            createNewLog(username,password);

        }
        return possible;
    }
    public static boolean logIn(String username, String password){
        List<String> users=readTxtFile("users");
        for (String st:
                users) {
            if(st.equals(username+" "+password)){
                logger(username,"logIn","player logged in.");
                return true;
            }
        }
        return false;
    }

    private static void writeInTxtFile(String fileName,List<String> text){
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+File.separator+"resources"+File.separator+fileName+".txt");
            Files.write(p,text, StandardOpenOption.APPEND);
        }catch(IOException w){ w.printStackTrace(); }
    }
    private static List<String> readTxtFile(String filePath){
        List<String> result=new ArrayList<>();
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+File.separator+"resources"+File.separator+filePath+".txt");
            result=Files.readAllLines(p);
        }catch(IOException w){ w.printStackTrace(); }
        return result;
    }
}

