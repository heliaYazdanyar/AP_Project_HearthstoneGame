package Out;

import Models.Card;
import Models.Deck;
import Models.Hero;
import Util.ImageLoader;
import Util.SoundPlayer;
import gamePlayers.Player;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Util.Logger.logger;

public class Game extends JPanel {
    private Player currentPlayer;
    private SoundPlayer sound;
    private Deck currentDeck;
    private Hero currentHero;
    private List<Card> deckCards=new ArrayList<>();


    int width=1000;
    int height=770;

    private int level;
    private int currentMana;
    private  int handCnt=0;
    private int groundCnt=0;
    private boolean weoponExist=false;

    private CardLayout cardLayout=new CardLayout();
    private JPanel mainCardPanel;
    private InfoPassive passiveCardPanel;
    private JPanel mainPanel;
    private JScrollPane eventsScroll;
    private Events events;
    private JScrollPane cardsScroll;
    private JPanel cards;
    private JPanel userPanel;
    private PlayGround playGround;
    private JPanel manaPanel;
    private JLabel heroHP;
    private JLabel deckNumber;
    private JLabel endTurn;
    private JLabel quit;
    private JLabel[] handCardLabels;
    private List<Card> handCards;
    private JLabel[] groundCardsLabels;
    private List<Card> groundCards;
    private JLabel heroPowerLabel;
    private JLabel weoponLabel;


    private MouseListener listener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if(e.getSource()==endTurn){
                nextTurn();
            }
            if(e.getSource()==quit){
                MainFrame.getInstance().setPanel("MainMenu");
            }
            for(int i=0;i<handCnt;i++){
                final int t=i;
                if(e.getSource()==handCardLabels[t]){
                    if(addToGroundPossible(handCards.get(t))){
                        //use the card
                        addToGround(handCards.get(t));
                        //lessen mana
                        update();
                    }
                    else{
                        JOptionPane.showMessageDialog(Game.this,"Not Possible!",
                                "Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
    };


    class PlayGround extends JPanel{

        void initGroundCards(){
            this.setLayout(new GridLayout(1,7));
            for(int i=0;i<groundCnt;i++){
                groundCardsLabels[i]=new JLabel(ImageLoader.getInstance().loadIcon(groundCards.get(i).getName(),"png",120,190));
                this.add(groundCardsLabels[i]);
            }
            mainPanel.add(playGround,BorderLayout.CENTER);
            Game.this.mainCardPanel.add(mainPanel,BorderLayout.CENTER);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("playGround2","jpg",500,500);
            g.drawImage(background,0,0,getWidth(),getHeight(),null);
        }

    }
    class Events extends JPanel{
        private String path=System.getProperty("user.dir")+ File.separator+"resources"+File.separator+"users"+File.separator
                +currentPlayer.getUsername()+File.separator+"Events"+File.separator+"number_"+
                currentPlayer.getPlayersInfo().getAllGames()+".txt";
        private List<JLabel> eventLabels=new ArrayList<>();
        private int eventCnt=0;


        private MouseListener listener1 = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int i=0;i<eventCnt;i++){
                    final int t=i;
                    if(e.getSource()== eventLabels.get(t)){
                        JOptionPane.showMessageDialog(Game.this,getEvent(t),
                                "Event",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        };

        Events(){
            this.setSize(95,600);
            this.setLayout(new GridLayout(15,2));
            this.setBackground(Color.red);

            initFile();
        }

        void initFile(){
            try{
                Files.createFile(Paths.get(path));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        String getEvent(int i){
            List<String> list=new ArrayList<>();
            try{
                list= Files.readAllLines(Paths.get(path));
            }catch (IOException e){
                e.printStackTrace();
            }
            return list.get(i);
        }

        void saveEvent(String action){
            try{
                Files.write(Paths.get(path),(action+"\n").getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e){
                e.printStackTrace();
            }

            eventCnt++;
            eventLabels.add(new JLabel(eventCnt+""));
            eventLabels.get(eventCnt-1).setForeground(Color.black);
            eventLabels.get(eventCnt-1).setBorder(new LineBorder(Color.black, 5));
            eventLabels.get(eventCnt-1).setBackground(Color.red);
            eventLabels.get(eventCnt-1).setOpaque(true);
            eventLabels.get(eventCnt-1).addMouseListener(listener1);

        }

        void initPanel(){
            for(int i=0;i<eventLabels.size();i++){
                this.add(eventLabels.get(i));
            }
        }
    }
    class InfoPassive extends JPanel{
        boolean twiceDraw=false;
        boolean freePower=false;
        boolean warriors=false;
        boolean manaJump=false;
        boolean zombie=false;

        private List<String> passiveNames;
        private JLabel[] passiveLabels=new JLabel[3];
        private String[] passive=new String[3];

        private MouseListener PListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int i=0;i<3;i++){
                    final int t=i;
                    if(e.getSource()==passiveLabels[t]){
                        switch (passive[t]){
                            case"Zombie":
                                zombie=true;
                                break;
                            case"FreePower":
                                freePower=true;
                                break;
                            case"ManaJump":
                                manaJump=true;
                                break;
                            case"Warriors":
                                warriors=true;
                                break;
                            case"TwiceDraw":
                                twiceDraw=true;
                                break;
                        }
                        Game.this.update();

                        Game.this.currentDeck.deckInfo.addNumberOfGames(currentPlayer.getUsername());
                        cardLayout.show(Game.this,"main");
                    }
                }
            }
        };

        InfoPassive(){
            initPassives();
            this.setLayout(new GridLayout(1,3));
            initPanel();
        }

        private void initPanel(){
            for(int i=0;i<3;i++){
                String st=getRandomPassive();
                passive[i]=st;
                passiveLabels[i]=new JLabel(ImageLoader.getInstance().loadIcon(st,"png",200,300));
                passiveLabels[i].addMouseListener(PListener);
                this.add(passiveLabels[i]);
            }
        }
        private void initPassives(){
            passiveNames=new ArrayList<>();
            passiveNames.add("TwiceDraw");
            passiveNames.add("Warriors");
            passiveNames.add("FreePower");
            passiveNames.add("ManaJump");
            passiveNames.add("Zombie");
        }
        private  String getRandomPassive(){
            Random r=new Random();
             return passiveNames.get(r.nextInt(5));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("passive","jpg",600,600);
            g.drawImage(background,0,0,getWidth(),getHeight(),null);
        }
    }


    public Game(Player player, Deck deck){
        this.setLayout(cardLayout);
        mainCardPanel=new JPanel();
        passiveCardPanel=new InfoPassive();
        this.add("passive",passiveCardPanel);
        this.currentDeck=deck;
        this.currentPlayer=player;
        currentPlayer.getPlayersInfo().addToAllGames(player.getUsername());
        this.events=new Events();
        this.currentHero=new Hero(deck.getHeroName());
        mainCardPanel.setLayout(new BorderLayout());
        mainPanel=new JPanel(); mainPanel.setLayout(new BorderLayout());
        handCardLabels=new JLabel[15];
        groundCardsLabels=new JLabel[15];
        level=1;
        initCards();
        initGamePanel();
        initEastPanel();
        addToHand(); addToHand(); addToHand();
        initUserPanel();
        this.add("main",mainCardPanel);
        cardLayout.show(this,"passive");
    }

    private void update(){
        if(passiveCardPanel.manaJump){
            level=2;
            setMana(level);
        }

        events.initPanel();
        initGamePanel();
        initUserPanel();
    }

    private void initEastPanel(){
        JPanel eastPanel=new JPanel();
        eastPanel.setSize(150,600);
        eastPanel.setBackground(Color.darkGray);
        eastPanel.setBorder(new LineBorder(Color.red, 5));
        eastPanel.setLayout(new BorderLayout());

        JLabel enemyFace=new JLabel(ImageLoader.getInstance().loadIcon("enemy","jpg",180,180));
        enemyFace.setBackground(Color.darkGray);
        Border b0 = new LineBorder(Color.BLACK, 10);
        enemyFace.setBorder(b0);

        JLabel heroFace=new JLabel(ImageLoader.getInstance().loadIcon(currentDeck.getHeroName(),"jpg",180,180));
        heroFace.setBackground(Color.darkGray);
        Border b = new LineBorder(Color.BLACK, 10);
        heroFace.setBorder(b);
//        eastPanel.add(heroFace,BorderLayout.SOUTH);

        JPanel innerEastPanel=new JPanel();
        innerEastPanel.setBackground(Color.darkGray);
        innerEastPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridx=0; gbc.gridy=0;

        JPanel hpPanel=new JPanel();
        hpPanel.setLayout(new GridLayout(1,2));
        hpPanel.setBackground(Color.red);
        hpPanel.add(new JLabel(ImageLoader.getInstance().loadIcon("hpIcon","png",20,20)));
        heroHP=new JLabel(currentHero.getHP()+"");
        heroHP.setFont(new Font("Courier New", Font.ITALIC, 13));
        heroHP.setForeground(Color.white);
        heroHP.setBackground(Color.red);
        heroHP.setOpaque(true);
        hpPanel.add(heroHP);

        manaPanel=new JPanel();
        Border b1 = new LineBorder(Color.blue, 5);
        manaPanel.setBorder(b1);
        manaPanel.setLayout(new GridLayout(1,10));
        manaPanel.setBackground(Color.lightGray);
        setMana(level);

        deckNumber=new JLabel(deckCards.size()+" Cards Left",SwingConstants.CENTER);
        deckNumber.setFont(new Font("Courier New", Font.ITALIC, 18));
        deckNumber.setForeground(Color.blue);
        deckNumber.setBackground(Color.darkGray);
        deckNumber.setOpaque(true);


        endTurn=new JLabel("End Turn",SwingConstants.CENTER);
        endTurn.setFont(new Font("Courier New", Font.ITALIC, 18));
        endTurn.setForeground(Color.black);
        endTurn.setBackground(Color.pink);
        endTurn.setOpaque(true); endTurn.addMouseListener(listener);

        quit=new JLabel("Quit",SwingConstants.CENTER);
        quit.setFont(new Font("Courier New", Font.ITALIC, 18));
        quit.setForeground(Color.red);
        quit.setBackground(Color.darkGray);
        quit.setOpaque(true); quit.addMouseListener(listener);

        innerEastPanel.add(enemyFace,gbc);
        gbc.gridy++; innerEastPanel.add(quit,gbc);
        gbc.gridy++; innerEastPanel.add(endTurn,gbc);
        gbc.gridy++; innerEastPanel.add(deckNumber,gbc);
        gbc.gridy++; innerEastPanel.add(manaPanel,gbc);
        gbc.gridy++; innerEastPanel.add(hpPanel,gbc);
        gbc.gridy++; innerEastPanel.add(heroFace,gbc);


        eastPanel.add(innerEastPanel,BorderLayout.CENTER);
        mainPanel.add(eastPanel,BorderLayout.EAST);

    }
    private void initGamePanel(){
        if(playGround!=null) mainPanel.remove(playGround);
        if(events!=null) mainPanel.remove(events);
//        eventsScroll=new JScrollPane(events,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(events,BorderLayout.WEST);
        playGround=new PlayGround();
        playGround.initGroundCards();

    }
    private void initUserPanel(){
        if(userPanel!=null) this.mainCardPanel.remove(userPanel);
        userPanel=new JPanel();
        userPanel.removeAll();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(new LineBorder(Color.red, 5));

        String hPower="";
        if(passiveCardPanel.zombie) hPower="Zombie-"+currentHero.getHeroPower();
        else if(passiveCardPanel.freePower) hPower="FreePower-"+currentHero.getHeroPower();
        else hPower=currentHero.getHeroPower();
        heroPowerLabel=new JLabel(ImageLoader.getInstance().loadIcon(hPower,"png",130,190));
        heroPowerLabel.setBackground(Color.red);
        heroPowerLabel.setOpaque(true);
        heroPowerLabel.addMouseListener(listener);
        userPanel.add(heroPowerLabel,BorderLayout.EAST);

        cards=new JPanel();
        cards.removeAll();
        cards.setLayout(new GridLayout(1,16));
        cards.setBackground(Color.darkGray);
        cards.setSize(600,200);

        for(int i=0;i<handCnt;i++){
            if(handCardLabels[i]!=null) {
                handCardLabels[i].removeMouseListener(listener);
            }
            handCardLabels[i]=new JLabel(ImageLoader.getInstance().loadIcon(handCards.get(i).getName(),"png",120,190));
            handCardLabels[i].addMouseListener(listener);
            cards.add(handCardLabels[i]);
        }
        cardsScroll=new JScrollPane(cards,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        userPanel.add(cardsScroll,BorderLayout.CENTER);

        if(weoponExist){
            userPanel.add(weoponLabel,BorderLayout.WEST);
        }

        mainCardPanel.add(userPanel,BorderLayout.SOUTH);
    }
    private void initCards(){
        groundCards=new ArrayList<>(15);
        handCards=new ArrayList<>(15);
        for (Card c:
                currentDeck.getDeckCards()) {
            deckCards.add(c);
        }

    }

    private void setMana(int cnt){
        currentMana=cnt;
        manaPanel.removeAll();
        ImageIcon manaIcon= ImageLoader.getInstance().loadIcon("Mana","png",20,20);
        if(cnt<10){
            for (int i=0;i<cnt;i++) {
                manaPanel.add(new JLabel(manaIcon));
            }
            for(int i=cnt;i<10;i++){
                manaPanel.add(new JLabel(""));
            }
        }
        else{
            for(int i=0;i<10;i++){
                manaPanel.add(new JLabel(manaIcon));
            }
        }

    }

    private Card getRandomCard(){
        Random r=new Random();
        int i=r.nextInt(deckCards.size());
        Card card=deckCards.get(i);
        deckCards.remove(i);
        return card;
    }
    private void addToHand(){
        if(handCnt<12) {
            handCards.add(getRandomCard());
            handCnt++;
        }
        else{
            ImageIcon icon= ImageLoader.getInstance().loadIcon(getRandomCard().getName(),"png",80,130);
            JOptionPane.showMessageDialog(
                    null,
                    "You cant have more than 12 card in your hand",
                    "can not do that", JOptionPane.INFORMATION_MESSAGE,
                    icon);
        }
    }
    private void removeFromHand(Card card){
        handCards.remove(card);
        handCnt--;
    }

    private boolean addToGroundPossible(Card card){
        if(groundCnt<7) {
            boolean ownCard = false;
            for (Card c :
                    handCards) {
                if (c.getName().equals(card.getName())) {
                    ownCard = true;
                }
            }
            if (currentMana >= card.getManaCost() && ownCard) return true;

            return false;
        }
        else return false;
    }
    private void addToGround(Card card){
        removeFromHand(card);
        events.saveEvent(card.getName()+"_was_played_by_"+currentHero.getName());
        //show in ground or sth
        if(card.getType().equals("Minion")){
            logger(currentPlayer.getUsername(),"card_played","minion:"+card.getName());
            groundCards.add(card);
            groundCnt++;
        }
        else if(card.getType().equals("Spell")){
            logger(currentPlayer.getUsername(),"card_played","spell:"+card.getName());
            //animation
        }
        else{
            logger(currentPlayer.getUsername(),"card_played","weopon:"+card.getName());
            weoponExist=true;
            weoponLabel=new JLabel(ImageLoader.getInstance().loadIcon(card.getName(),"png",120,180));
            weoponLabel.setBackground(Color.red);
            weoponLabel.setOpaque(true);
        }
        //cost mana
        setMana(currentMana-card.getManaCost());
    }

    private void nextTurn(){
        logger(currentPlayer.getUsername(),"click_button","end_turn");
        //mana plus
        level++;
        setMana(level);
        //card new
        if(passiveCardPanel.twiceDraw) addToHand();
        addToHand();
        initUserPanel();
        //update dek number
        deckNumber.setText(deckCards.size()+" Cards Left");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("playGround","jpg",900,900);
        g.drawImage(background,0,0,width,height,null);

    }
}
