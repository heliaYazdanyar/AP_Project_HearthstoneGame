package Models;

import Out.Collections;
import Out.MainFrame;
import Out.Status;
import com.google.gson.Gson;
import gamePlayers.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static Util.Logger.logger;

public class Deck extends JPanel implements ActionListener,Decks{

    private Player currentPlayer;
    private String name;
    private Hero deckHero;
    private List<String> deckList;
    private List<String> cards=new ArrayList<>();
    public DeckInfo deckInfo;
    public boolean isSelectedForPlaying;
    private GridBagConstraints gbc=new GridBagConstraints();
    private JLabel nameLabel;
    private JLabel heroLabel;
    private JLabel[] cardsButtons=new JLabel[15];
    private JButton delete;
    private JButton close;
    private JButton select;


    private MouseListener listener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int n=cards.size();
            for (int i=0;i<n;i++){
                final int t=i;
                if(e.getSource()==cardsButtons[t]){
                    String removing=cards.get(t);
                    removeCard(removing);
                    update();
                }
            }

        }
    };

    public Deck(Player player, String name){
        this.currentPlayer=player;
        this.deckInfo=getDeckInfo(name);

        initDeckFromFile(name);
        initPanel();

    }

    private void initPanel(){
        initDeckFromFile(name);
        this.setSize(100,900);

        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=0;
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.orange);

        nameLabel=new JLabel(name,SwingConstants.CENTER);
        nameLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
        nameLabel.setForeground(Color.black);
        nameLabel.setBackground(Color.GRAY);
        nameLabel.setOpaque(true);

        gbc.gridy=0; this.add(nameLabel,gbc);

        heroLabel=new JLabel(getHeroName(),SwingConstants.CENTER);
        heroLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
        heroLabel.setForeground(Color.black);
        heroLabel.setBackground(Color.lightGray);
        heroLabel.setOpaque(true);
        gbc.gridy=1; this.add(heroLabel,gbc);

        for(int i=0;i<cards.size();i++){
            gbc.gridy++;
            cardsButtons[i]=new JLabel(cards.get(i));
            cardsButtons[i].addMouseListener(listener);
            cardsButtons[i].setFont(new Font("Courier New", Font.ITALIC, 15));
            cardsButtons[i].setForeground(Color.black);
            cardsButtons[i].setBackground(Color.yellow);
            cardsButtons[i].setOpaque(true);
            this.add(cardsButtons[i],gbc);

        }
        if(cards.size()<15){
            for (int i=cards.size();i<15;i++){
                gbc.gridy++;
                cardsButtons[i]=new JLabel("+");
//                cardsButtons[i].addMouseListener(listener);
                this.add(cardsButtons[i],gbc);
            }
        }

        select=new JButton("Select Deck");
        select.addActionListener(this);
        gbc.gridy++; this.add(select,gbc);

        delete=new JButton("Delete Deck");
        delete.addActionListener(this);
        gbc.gridy++; this.add(delete,gbc);

        close=new JButton("Close");

        gbc.gridy++; this.add(close,gbc);

        initListeners();

    }
    public void initListeners(){
        nameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String str = JOptionPane.showInputDialog("Enter New Name:");
                if(changeNamePossible(str)){
                    changeName(str);
                    update();
                }
                else{
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),"you already have a deck with this name!",
                            "ERROR",JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        heroLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String h = JOptionPane.showInputDialog("Enter Hero Name:");
                if(h.equalsIgnoreCase("Mage")|| h.equalsIgnoreCase("Warlock")||
                        h.equalsIgnoreCase("Priest")||h.equalsIgnoreCase("Hunter")||h.equalsIgnoreCase("Rogue")) {
                    if (changeHeroPossible()) {
                        changeHero(h);
                        update();
                    }
                    else{
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),"remove hero cards first!",
                                "ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),"There is no such hero!",
                            "ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cards.size()<15){
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),"You should add 15 cards at least!",
                            "ERROR",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    logger(currentPlayer.getUsername(),"change_Deck_name",Deck.this.getName()+"changed to "+name);
                    Collections.getInstance().unSelect();
                    Collections.getInstance().deckPanel.cardLayout1.show(Collections.getInstance().deckPanel.allDecks, "all");
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Deck.this.deleteDeck();
                Collections.getInstance().update();
            }
        });

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cards.size() < 15)
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), "You should add 15 cards at least!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                else {
                    logger(currentPlayer.getUsername(),"select_deck",Deck.this.getName());
                    currentPlayer.setCurrentDeck(Deck.this);
                    Collections.getInstance().setPlayingDeck(Deck.this);
                }
            }
        });

    }
    private void update(){
        //update name
        nameLabel.setText(name);
        //update heroName
        heroLabel.setText(getHeroName());
        //cards buttons
        initDeckFromFile(name);
        initPanel();
        Collections.getInstance().update();
        Status.getInstance().update();
    }

    public String getName(){
        return name;
    }
    public String getHeroName(){
        return deckHero.getName();
    }
    public List<Card> getDeckCards(){
        List<String> st=getDeckFile(this.name);
        List<Card> result=new ArrayList<>();
        st.remove(0);
        for (String name:
             st) {
            result.add(Card.getCard(name));
        }

        return result;
    }


    private List<String> getDeckFile(String name){
        List<String> deck=new ArrayList<>();
        try{
            deck= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+name+".txt"));
        }catch (Exception e){e.printStackTrace();}
        return deck;
    }
    private void initDeckFromFile(String name){
        this.deckList=getDeckFile(name);
        this.name=name;
        this.deckHero=new Hero(deckList.get(0));
        removeEmptyLinesFromFile();
        List<String> list=new ArrayList<>();
        list.addAll(deckList);
        list.remove(0);
        this.cards=list;
    }
    private void removeEmptyLinesFromFile(){
        List<String> lines=new ArrayList<>();
        try {
           lines=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+
                    this.name+".txt"));
        }catch (Exception e){e.printStackTrace();}
        lines.removeIf(String::isEmpty);
        try{
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+
                    this.name+".txt"),lines);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean addPossible(Card card){
        initDeckFromFile(name);
        if(deckList.size()<16 && currentPlayer.getMyCardsNames().contains(card.getName())){
            if(card.getClassName().equals("Neutral")) return true;
            else if(deckHero==null) return true;
            else if(checkCardHero(card)) return true;
            else return false;
        }
        else return false;
    }
    private boolean changeHeroPossible(){
        List<String> cards=new ArrayList<>();
        List<String> neutral;
        try{
            cards=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "allCards"+File.separator+"Lists"+File.separator+"allCards.txt"));
            neutral=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+
                    File.separator+"allCards"+File.separator+"Lists"+File.separator+"Neutral.txt"));
            cards.removeAll(neutral);
        }catch (Exception e){ e.printStackTrace();}

        for (String card:
                cards) {
            if(deckList.contains(card)){
                return false;
            }
        }
        return true;
    }
    private boolean changeNamePossible(String name){
        List<String> list=new ArrayList<>();
        try{
            list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+
                    File.separator+"users"+File.separator+currentPlayer.getUsername()+ File.separator+"ListOfDecks.txt"));
        }catch (IOException e){ e.printStackTrace(); }

        for (String st:
                list) {
            if(st.equalsIgnoreCase(name)) return false;
        }
        if(name.equalsIgnoreCase("")) return false;
        return true;
    }
    private boolean checkCardHero(Card card){
        if(card.getClassName().equals(deckHero.getName())){
            return true;
        }
        else return false;
    }

    public void addCard(Card card){
        logger(currentPlayer.getUsername(),"add_card_toDeck",card.getName());
        removeEmptyLinesFromFile();
        deckList.add(card.getName());
        try {
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+
                    this.name+".txt"),("\n"+card.getName()).getBytes(), StandardOpenOption.APPEND);
        }catch (Exception e){e.printStackTrace();}
        Collections.getInstance().update();
    }
    private void removeCard(String cardName){
        logger(currentPlayer.getUsername(),"remove_card_fromDeck",cardName);
        cards.remove(cardName);
        deckList.remove(cardName);
        try {
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+
                    File.separator+"Decks"+File.separator+this.name+".txt"),deckList);
        }catch (Exception e){e.printStackTrace();}
    }
    private void changeHero(String heroName){
        logger(currentPlayer.getUsername(),"change_hero_Deck",deckHero.getName()+"->"+heroName);

        //apply to status file
        this.deckInfo.changeHero(heroName,currentPlayer.getUsername());

        this.deckHero=new Hero(heroName);
        List<String> list=new ArrayList<>();
        list.add(heroName);
        deckList.remove(0);
        list.addAll(deckList);
        deckList.removeAll(deckList);
        deckList.addAll(list);
        try{
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"users"+File.separator+currentPlayer.getUsername()+File.separator+
                    "Decks"+File.separator+this.name+".txt"),deckList);
        }catch(IOException e){e.printStackTrace();}
        Status.getInstance().update();
    }
    private void changeName(String name){
        logger(currentPlayer.getUsername(),"change_Deck_name",this.getName()+"changed to "+name);

        //status file change
        deckInfo.setName(name,currentPlayer.getUsername());
        String preName=this.name;
        try{
            List<String> list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"users"+File.separator+currentPlayer.getUsername()+File.separator+"ListOfDecks.txt"));
            list.remove((Object)preName);
            this.name=name;
            list.add(name);
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+ currentPlayer.getUsername()+File.separator+"ListOfDecks.txt"),list);
        }catch (IOException e){ e.printStackTrace(); }

        try{
            List<String> deckFile=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+
                    File.separator+preName+".txt"));
            Files.createFile(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+name+".txt"));
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+"users"+
                    File.separator+ currentPlayer.getUsername()+File.separator+"Decks"+File.separator+name+".txt"),deckFile);
            Files.delete(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+"users"+
                    File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+preName+".txt"));

        }catch(IOException e){ e.printStackTrace();}

        currentPlayer.update();
        Collections.getInstance().update();
        Status.getInstance().update();

    }

    private void deleteDeck(){
        logger(currentPlayer.getUsername(),"delete_deck",this.getName());
        if(isSelectedForPlaying){
            currentPlayer.ifDeckSelected=false;
            Collections.getInstance().unSelectPlayingDeck(this);
        }
        try{
            Files.delete(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+this.name+".txt"));
        }catch (IOException e){ e.printStackTrace(); }
        try{
            List<String> list=Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+
                    "resources"+File.separator+"users"+File.separator+currentPlayer.getUsername()+File.separator+
                    "ListOfDecks.txt"));
            list.remove(this.name);
            Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+ File.separator+"ListOfDecks.txt"),list);
        }catch (IOException e){
            e.printStackTrace();
        }
        Collections.getInstance().currentDeckCnt=-1;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
//        if(e.getSource()==delete){
//            this.deleteDeck();
//            Collections.getInstance().update();
//        }
//        if(e.getSource()==close){
//            if(cards.size()<15){
//                JOptionPane.showMessageDialog(MainFrame.getInstance(),"You should add 15 cards at least!",
//                        "ERROR",JOptionPane.ERROR_MESSAGE);
//            }
//            else {
//                logger(currentPlayer.getUsername(),"change_Deck_name",this.getName()+"changed to "+name);
//                Collections.getInstance().unSelect();
//                Collections.getInstance().deckPanel.cardLayout1.show(Collections.getInstance().deckPanel.allDecks, "all");
//            }
//        }
//        if(e.getSource()==select) {
//            if (cards.size() < 15)
//                JOptionPane.showMessageDialog(MainFrame.getInstance(), "You should add 15 cards at least!",
//                        "ERROR", JOptionPane.ERROR_MESSAGE);
//            else {
//                logger(currentPlayer.getUsername(),"select_deck",this.getName());
//                currentPlayer.setCurrentDeck(this);
//                Collections.getInstance().setPlayingDeck(this);
//            }
//        }
    }

    public class DeckInfo{
        String name;
        String heroName;
        int wins;
        int numberOfGames;
        String[] cards;
        int[] cardsCnt;

        public void addWins(String user){
            wins++;
            save(user);
        }
        public void addNumberOfGames(String username){
            this.numberOfGames++;
            save(username);
        }
        public void changeHero(String heroName,String username){
            this.heroName=heroName;
            save(username);
        }
        public void setName(String name,String user){
            List<String> list=new ArrayList<>();
            try {
                list = Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"
                        +File.separator+"users"+File.separator+user+File.separator+"Decks"+File.separator+
                        "status"+File.separator+this.name+".txt"));
            }catch (Exception e){
                e.printStackTrace();}
            try{
                Files.createFile(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+
                        File.separator+"users"+File.separator+user+File.separator+"Decks"+File.separator+
                        "status"+File.separator+name+".txt"));
                Files.write(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                        "users"+File.separator+user+File.separator+"Decks"+File.separator+"status"+File.separator+name+".txt"),list.get(0).getBytes());
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                Files.delete(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                        "users"+File.separator+user+File.separator+"Decks"+File.separator+"status"+File.separator+this.name+".txt"));
            }catch (IOException e){e.printStackTrace();}
            this.name=name;
            save(user);
        }

        public int getNumberOfGames(){ return numberOfGames;}
        public int getWins(){return wins;}

        public int getAverageMana(Player player){
            Deck thisDeck=new Deck(player,this.name);

            List<String> names=new ArrayList<>();
            names.addAll(thisDeck.getDeckFile(name));
            names.remove(0);
            int n=names.size();
            int sum=0;
            for (String name:
                    names) {
                Card card=Card.getCard(name);
                sum+=card.getManaCost();
            }
            if(n==0) return 1;
            return (sum/n);
        }

        private Path getThisPath(String user){
            Path p=Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+user+File.separator+"Decks"+File.separator+"status"+File.separator+this.name+".txt");
            return p;
        }
        void save(String user){

            String st="{name:'"+name+"',heroName:'"+heroName+"','wins':"+wins+",numberOfAllGames:"+numberOfGames+",'cards':[";
            for(int i=0;i<14;i++){
               st=st+"'"+cards[i]+"',";
            }
            st=st+"'"+cards[14]+"'],'cardsCnt':[";
            for(int i=0;i<14;i++){
                st=st+"'"+cardsCnt[i]+"',";
            }
            st=st+"'"+cardsCnt[14]+"']}";

            try{
                Files.write(getThisPath(user),st.getBytes());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private DeckInfo getDeckInfo(String name){
        List<String> list=new ArrayList<>();

        try{
            list =Files.readAllLines(Paths.get(System.getProperty("user.dir")+File.separator+"resources"+File.separator+
                    "users"+File.separator+currentPlayer.getUsername()+File.separator+"Decks"+File.separator+"status"+
                    File.separator+name+".txt"));

        }catch (IOException e){
            e.printStackTrace();
        }

        Gson gson= new Gson();
        DeckInfo Info=gson.fromJson(list.get(0), DeckInfo.class);
        return Info;
    }

}
