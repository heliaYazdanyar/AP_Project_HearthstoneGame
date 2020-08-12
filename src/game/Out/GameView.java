package game.Out;

import models.*;
import Out.MainFrame;
import util.DeckReader;
import util.ImageLoader;
import util.SoundPlayer;
import client.GameClient;
import gamePlayers.InGamePlayer;
import gamePlayers.OnlineEnemy;
import gamePlayers.PlayerBot;
import gamePlayers.PracticePlayer;
import logic.Administer;
import messages.Attack;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel {
    private SoundPlayer soundPlayer;

    public int turn=0;
    boolean online;
    boolean myTurn;

    private Administer administer;

    public ChatRoom chatRoom;

    public GameView.PlayGround playGround;
    public GameView.Events events;
    private GameView.Tools tools;
    public InfoGiver infoGiver;

    private PracticePlayer friendPlayer;
    private InGamePlayer enemyPlayer;

    private FriendPlayerPanel myPanel;
    public PlayerPanel enemyPanel;

    private Card cardPopUp;
    private JPanel container=new JPanel();

    private boolean cardIsMoving;
    private BufferedImage movingCard;
    private Thread animatingCards=new Thread(){
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10);
                    if (cardIsMoving) {
                        moveCard(100, 500);
                        playGround.repaint();
                    }
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }
        }
    };
    private int xMove;
    private int yMove;



    //inner classes
    public class InfoGiver extends JPanel{

        int width=250;
        int height=800;

        public JLabel infoGiverLabel;
        public JLabel logLabel;

        InfoGiver(){
            this.setLayout(new BorderLayout());

            infoGiverLabel=new JLabel("Let's Win This Thing!",SwingConstants.CENTER);
            infoGiverLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
            infoGiverLabel.setForeground(Color.pink);
            infoGiverLabel.setBackground(Color.BLACK);
            infoGiverLabel.setHorizontalTextPosition(JLabel.CENTER);
            infoGiverLabel.setVerticalTextPosition(JLabel.CENTER);
            infoGiverLabel.setOpaque(true);

            if(administer.online) {
                chatRoom = new ChatRoom(administer.getClient());
                chatRoom.addMember(enemyPlayer.getUsername());
            }

            logLabel=new JLabel("Let's Win This Thing!",SwingConstants.CENTER);
            logLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
            logLabel.setForeground(Color.GREEN);
            logLabel.setBackground(Color.BLACK);
            logLabel.setHorizontalTextPosition(JLabel.CENTER);
            logLabel.setVerticalTextPosition(JLabel.CENTER);
            logLabel.setOpaque(true);

            infoGiverLabel.setPreferredSize(new Dimension(300,150));
            logLabel.setPreferredSize(new Dimension(300,200));
            this.add(infoGiverLabel,BorderLayout.SOUTH);
            if(administer.online)
                this.add(chatRoom,BorderLayout.CENTER);
            this.add(logLabel,BorderLayout.NORTH);

        }

        public void initCardInfo(Card card){
            if(card.getType().equalsIgnoreCase("Minion"))
                initMinionInfo((Minion)card);
            else if(card.getType().equalsIgnoreCase("Spell"))
                initSpellInfo((Spell)card);
            else if(card.getType().equalsIgnoreCase("Weapon")){
                initWeaponInfo((Weapon)card);
            }
            else{
                initQusetAndReward((QuestAndReward)card);
            }
        }

        public void initSpellInfo(Spell spell){
            String st="<html>*"+spell.getName()+"<br>"+"$MANA:"+spell.getManaCost()+"</html>";
            infoGiverLabel.setText(st);
        }
        public void initMinionInfo(Minion minion){
            String st="<html>*"+minion.getName()+"*<br>"+
                    "$MANA:"+minion.getManaCost()+"$"+
                    "<br>"+"*HP:"+minion.getHP()+"*** Attack:"+minion.getAttack()+"</html>";
            infoGiverLabel.setText(st);

        }
        public void initWeaponInfo(Weapon weapon){
            String st="<html>*"+weapon.getName()+"<br>"+
                    "$MANA:"+weapon.getManaCost()+"$<br>"+
                    "*ATTACK:"+weapon.getAttack()+"*<br>"
                    +"*SHIELD:"+weapon.getShield()+"*</html>";
            infoGiverLabel.setText(st);
        }
        public void initQusetAndReward(QuestAndReward qar){
        }
        public void initHeroPowerInfo(String name){
            String st="";
            if(name.equalsIgnoreCase("Caltropes")){
                st="<html>*"+name+"*<br>"+
                        "$MANA:"+2+"$"+
                        "<br>"+"Passive"+"</html>";
            }
            else if(name.equalsIgnoreCase("FireBlast")){
                st="<html>*"+name+"*<br>"+
                        "$MANA:"+2+"$"+
                        "<br>"+"deal 1 damage"+"</html>";
            }
            else if(name.equalsIgnoreCase("StealMaster")){
                st="<html>*"+name+"*<br>"+
                        "$MANA:"+3+"$"+
                        "<br>"+"Passive"+"</html>";
            }
            else if(name.equalsIgnoreCase("Heal")){

            }
            else{

            }
            infoGiverLabel.setText(st);
        }

        public void logAction(String action1,String action2,String action3){
            String st="<html>*"+action1+"<br>"+action2+"<br>"+action3+"*</html>";

            logLabel.setText(st);

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("InfoGiverBG","jpg",
                    InfoGiver.this.height,InfoGiver.this.width);
            g.drawImage(background,0,0,null);
        }
    }
    public class PlayGround extends JPanel{

        private int width=1100;
        private int height=600;

        private int x1=0,y1=235;
        private int x2=0,y2=250;
        private JLabel attacker;
        private JLabel victim;

        List<JLabel> friendMinions=new ArrayList<>();
        List<JLabel> enemyMinions=new ArrayList<>();

        private boolean isCardPopUp=false;
        private int xPopUp=120, yPopUp=240;

        private Thread grounRender= new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(4000);
                    PlayGround.this.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        PlayGround(){
            this.setLayout(new GridLayout(2,7));
            grounRender.start();
        }

        public void initFriendCard(){
            if(friendPlayer.getGroundCards()!=null) {
                friendMinions.removeAll(friendMinions);
                for (int i=0;i<administer.friend_CardsOnGround.size();i++) {
                    final int t=i;
                    JLabel label = new JLabel(ImageLoader.getInstance().loadIcon(
                            administer.friend_CardsOnGround.get(i).getName(), "jpeg", 140, 140));
                    //taunt notice
                    if((administer.friend_CardsOnGround.get(i)).isTaunt()) {
                        Border b = new LineBorder(Color.RED, 10);
                        label.setBorder(b);
                        label.setOpaque(true);
                    }

                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            isCardPopUp=false;
                            if(!online) {
                                if (administer.isAttackerChosen() && !friendPlayer.isMyTurn()) {
                                    administer.setInformation("victim chosen", false);
                                    victim = label;

                                    administer.setVictimOwner(friendPlayer);
                                    administer.setVictim(administer.friend_CardsOnGround.get(t));
                                    x2 = e.getXOnScreen();

                                    playGround.update();
                                } else if (friendPlayer.isMyTurn()) {
                                    administer.setAttackerIsWeapon(false);
                                    administer.setAttackerOwner(friendPlayer);
                                    administer.setAttacker(administer.friend_CardsOnGround.get(t));
                                    x1 = e.getXOnScreen();

                                    administer.setAttackerIndex(t);

                                    administer.setInformation("Attacker chosen", false);
                                }
                            }
                            else{
                                if(friendPlayer.isMyTurn()){
                                    administer.setAttackerIsWeapon(false);
                                    administer.setAttackerOwner(friendPlayer);
                                    administer.setAttacker(administer.friend_CardsOnGround.get(t));
                                    x1 = e.getXOnScreen();

                                    administer.setAttackerIndex(t);

                                    administer.setInformation("Attacker chosen", false);
                                }
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            infoGiver.initCardInfo(administer.friend_CardsOnGround.get(t));
                        }

                    });

                    friendMinions.add(label);
                }
            }
        }
        public void initEnemyCards(){

            if(enemyPlayer.getGroundCards()!=null){
            enemyMinions.removeAll(enemyMinions);
            for (int i=0;i<administer.enemy_CardsOnGround.size();i++){
                final int t=i;
                    JLabel label = new JLabel(ImageLoader.getInstance().loadIcon(
                            administer.enemy_CardsOnGround.get(i).getName(), "jpeg", 140, 140));
                    //taunt notice
                   if(administer.enemy_CardsOnGround.get(i).isTaunt()) {
                       Border b = new LineBorder(Color.RED, 10);
                       label.setBorder(b);
                       label.setOpaque(true);
                   }

                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            isCardPopUp=false;
                            if(!online) {
                                if (administer.isAttackerChosen() && friendPlayer.isMyTurn()) {
                                    administer.setInformation("Victim Chosen", false);
                                    victim = label;

                                    administer.setVictimOwner(enemyPlayer);
                                    administer.setVictim(administer.enemy_CardsOnGround.get(t));
                                    x2 = e.getXOnScreen();
                                    playGround.update();
                                } else if (!friendPlayer.isMyTurn()) {
                                    administer.setAttackerIsWeapon(false);
                                    administer.setAttackerOwner(enemyPlayer);
                                    administer.setAttacker(administer.enemy_CardsOnGround.get(t));
                                    x1 = e.getXOnScreen();
                                    administer.setInformation("Attacker Chosen", false);
                                    attacker = label;
                                }
                            }
                            else{
                                if(friendPlayer.isMyTurn() && administer.isAttackerChosen()){
                                    administer.setInformation("Victim Chosen", false);
                                    victim = label;


                                    if(!administer.isAttackerIsSpell()) {
                                        administer.setVictimOwner(enemyPlayer);
                                        administer.setVictim(administer.enemy_CardsOnGround.get(t));
                                        administer.getClient().sendAttack(administer.getAttacker().getType(), administer.getAttackerIndex(),
                                                administer.getAttacker().getAttack(), administer.getVictim().getType(), t);
                                    }
                                    else {
                                        administer.getClient().sendVictim("Minion",t);
                                        administer.setVictimOwner(enemyPlayer);
                                        administer.setVictim(administer.enemy_CardsOnGround.get(t));
                                    }

                                    x2 = e.getXOnScreen();
                                    playGround.update();
                                }
                            }

                        }
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            infoGiver.initCardInfo(administer.enemy_CardsOnGround.get(t));
                        }


                    });
                    enemyMinions.add(label);
                }
            }
        }

        public void addLabels(){
            for(int i=0;i<7;i++){
                if(i<enemyMinions.size()) this.add(enemyMinions.get(i));
                else this.add(new JLabel());
            }

            for(int i=0;i<7;i++){
                if(i<friendMinions.size()) this.add(friendMinions.get(i));

                else this.add(new JLabel());
            }

        }

        public void update(){
            PlayGround.this.removeAll();
            initEnemyCards();
            initFriendCard();
            addLabels();
            PlayGround.this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("paper-background","jpg",
                    PlayGround.this.width,PlayGround.this.height);
            g.drawImage(background,0,0,getWidth(),getHeight(),null);

            if(cardIsMoving) {
                g.drawImage(movingCard,xMove,yMove,null);
            }
            if(administer.isHPloss()){
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("-HP", x1, y1);
                g.drawString("-HP",x2,y2);

                administer.setHPloss(false);
            }

            if(isCardPopUp){
                String st="*"+ cardPopUp.getName()+"\n"+"$MANA:"+cardPopUp.getManaCost()+
                        "\n"+"*HP:"+((Minion)cardPopUp).getHP()+" *Attack:"+((Minion)cardPopUp).getAttack()+"";
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.GRAY);
                g.drawString(st,xPopUp,yPopUp);
            }
        }

    }
    public class Events extends JPanel{
        private String path;
        private java.util.List<JLabel> eventLabels=new ArrayList<>();
        private int eventCnt=0;
        private int rows;

        private int width=80;
        private int height=500;

        Events(){
            this.setSize(95,600);
            rows=10;
            this.setLayout(new GridLayout(rows,1));
            this.setBackground(Color.darkGray);
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setOpaque(true);

            initFile();
        }

        private void initFile(){
            //handling number of events
            int n=0;
            try{
                java.util.List<String> list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+
                        "resources"+File.separator+"events"+File.separator +"cnt.txt"));
                n=Integer.parseInt(list.get(0));
                Files.write(Paths.get(System.getProperty("user.dir")+ File.separator+
                        "resources"+File.separator+"events"+File.separator +"cnt.txt"),(n+1+"").getBytes());
            }catch (IOException e){ e.printStackTrace();}

            //this game event file
            this.path=System.getProperty("user.dir")+ File.separator+"resources"+File.separator+"events"+File.separator
                    +"number_"+n+".txt";
            try{
                Files.createFile(Paths.get(path));
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        public void updateEvents(){
            Events.this.removeAll();
            for(int i=0;i<eventLabels.size();i++){
                this.add(eventLabels.get(i));
            }
        }

        public void addEvent(String action, InGamePlayer attacker){
            String thisEvent="player: "+attacker.getUsername()+"->"+action;
            saveEvent(thisEvent);

            //add to event panel
            eventCnt++;
            eventLabels.add(new JLabel("  "+eventCnt+"  ",SwingUtilities.CENTER));
            eventLabels.get(eventCnt-1).setForeground(Color.black);
            eventLabels.get(eventCnt-1).setBorder(new LineBorder(Color.black, 5));
            eventLabels.get(eventCnt-1).setBackground(Color.YELLOW);
            eventLabels.get(eventCnt-1).setOpaque(true);
            eventLabels.get(eventCnt-1).addMouseListener(listener1);

        }

        private void saveEvent(String event){
            try{
                Files.write(Paths.get(path),(event+"\n").getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        private String getEvent(int i){
            List<String> list=new ArrayList<>();
            try{
                list= Files.readAllLines(Paths.get(path));
            }catch (IOException e){
                e.printStackTrace();
            }
            return list.get(i);
        }

        private void showEvent(int i){
            JOptionPane.showMessageDialog(GameView.this,getEvent(i),
                    "Event",JOptionPane.INFORMATION_MESSAGE);
        }

        private MouseListener listener1 = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for(int i=0;i<eventCnt;i++){
                    final int t=i;
                    if(e.getSource()== eventLabels.get(t)){
                        showEvent(t);
                    }
                }

            }
        };
    }
    class Tools extends JPanel{
        private int width=200;
        private int height=600;

        private int cntTime;
        private JLabel countDown;
        private Thread timer=new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (cntTime <= 0) {
                        turn = (turn + 1) % 2;
                        administer.newTurn(turn);
                        cntTime = 60;
                    }
                    if (cntTime <= 40) countDown.setText("!!!!  " + cntTime + "  !!!!");
                    else countDown.setText("     " + cntTime + "     ");
                    cntTime--;
                    information.setText(administer.getInformation());
                }
            }
        };

        private JButton endTurn=new JButton("END TURN");
        private JButton mainMenu=new JButton("MAIN MENU");
        private JButton quitGame=new JButton("QUIT");

        private JLabel friendHero;
        private JLabel enemyHero;

        private JLabel information;
        private GridBagConstraints gbc=new GridBagConstraints();

        Tools(){
            this.setLayout(new GridBagLayout());
            this.setBackground(Color.BLACK);

            countDown=new JLabel(0+"",SwingConstants.CENTER);
            countDown.setFont(new Font("Courier New", Font.ITALIC, 20));
            countDown.setForeground(Color.RED);
            countDown.setBackground(Color.BLACK);
            countDown.setHorizontalTextPosition(JLabel.CENTER);
            countDown.setVerticalTextPosition(JLabel.CENTER);
            countDown.setOpaque(true);

            information=new JLabel("",SwingConstants.CENTER);
            information.setFont(new Font("Courier New", Font.ITALIC, 20));
            information.setForeground(Color.GREEN);
            information.setBackground(Color.BLACK);
            information.setHorizontalTextPosition(JLabel.CENTER);
            information.setVerticalTextPosition(JLabel.CENTER);
            information.setOpaque(true);


            endTurn.addActionListener(e -> {
                if(friendPlayer.isMyTurn()) {
                    turn = (turn + 1) % 2;
                    administer.newTurn(turn);
                    if (administer.online)
                        administer.getClient().sendGameMessage("endTurn", "");

                    cntTime = 60;
                }
            });
            endTurn.setFont(new Font("Courier New", Font.ITALIC, 18));
            endTurn.setForeground(Color.PINK);
            endTurn.setOpaque(true);

            mainMenu.addActionListener(e -> MainFrame.getInstance().setPanel("MainMenu"));
            mainMenu.setForeground(Color.DARK_GRAY);
            mainMenu.setOpaque(true);

            quitGame.addActionListener(e -> {

            });
            quitGame.setForeground(Color.DARK_GRAY);
            quitGame.setOpaque(true);

            System.out.println("initing her labels");
            friendHero=new JLabel(ImageLoader.getInstance().loadIcon(friendPlayer.getHeroName(),"jpeg",200,200));
            enemyHero=new JLabel(ImageLoader.getInstance().loadIcon(enemyPlayer.getHeroName(),"jpeg",200,200));


            friendHero.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!friendPlayer.isMyTurn()){
                        if(administer.isAttackerChosen()){
                            administer.setInformation("Target chosen",false);
                            administer.setVictimOwner(friendPlayer);
                            administer.setVictim(friendPlayer.getHero());
                        }
                        else{
                            if(!online)
                            administer.setInformation("choose attacker",false);
                        }
                    }
                    else{
                        if(administer.isAttackerChosen()){
                            administer.setInformation("choose target",false);
                        }
                    }
                }
            });
            enemyHero.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(friendPlayer.isMyTurn()){
                        if(administer.isAttackerChosen()){
                            administer.setInformation("Target chosen",false);

                            if(online) {
                                if(!(administer.isAttackerIsSpell())) {
                                    administer.setVictimOwner(enemyPlayer);
                                    administer.setVictim(enemyPlayer.getHero());
                                    administer.getClient().sendAttack(administer.getAttacker().getType(), administer.getAttackerIndex(),
                                            administer.getAttacker().getAttack(), administer.getVictim().getType(), 0);
                                }
                                else {
                                    administer.getClient().sendVictim("Hero",0);
                                    administer.setVictimOwner(enemyPlayer);
                                    administer.setVictim(enemyPlayer.getHero());
                                }
                            }

                        }
                        else{
                            administer.setInformation("choose attacker",false);
                        }

                    }
                    else{
                        if(administer.isAttackerChosen()){
                            administer.setInformation("choose target",false);
                        }
                    }
                }
            });

            administer.newTurn(0);
            cntTime=60;
            timer.start();

            initTools();
        }

        private void initTools(){
            gbc.weightx=1;
            gbc.fill=GridBagConstraints.HORIZONTAL;
            gbc.gridx=0;

            gbc.gridy=0;
            this.add(enemyHero,gbc);
            gbc.gridy=1;
            this.add(mainMenu,gbc);
            gbc.gridy=2;
            this.add(quitGame,gbc);
            gbc.gridy=3;
            this.add(information,gbc);
            gbc.gridy=4;
            this.add(countDown,gbc);
            gbc.gridy=5;
            this.add(endTurn,gbc);
            gbc.gridy=6;
            this.add(friendHero,gbc);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
//            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
//                    Tools.this.width,Tools.this.height);
//            g.drawImage(background,0,0,null);
        }
    }


    //practice with yourself
    public GameView(PracticePlayer friendPlayer,PracticePlayer enemyPlayer){
        this.friendPlayer=friendPlayer;
        this.enemyPlayer=enemyPlayer;

        administer=new Administer(this.friendPlayer,this.enemyPlayer,this,false);
        friendPlayer.setAdminister(administer);
        enemyPlayer.setAdminister(administer);

        initMainPanels();
        initOfflineGamePanels();
        addPanels();
        animatingCards.start();
        this.online=false;
    }

    //with deckReader
    public GameView(PracticePlayer friendPlayer,PracticePlayer enemyPlayer,boolean deckReader){
        this.friendPlayer=friendPlayer;
        this.enemyPlayer=enemyPlayer;

        administer=new Administer(this.friendPlayer,this.enemyPlayer,this,true);
        this.friendPlayer.setAdminister(administer);
        this.enemyPlayer.setAdminister(administer);

        initMainPanels();
        initOfflineGamePanels();
        addPanels();
        animatingCards.start();

        this.online=false;
    }

    //with bot
    public GameView(PracticePlayer friendPlayer, PlayerBot playerBot){

    }


    //online
    public GameView(GameClient friendClient, OnlineEnemy enemyPlayer, String online){
        this.enemyPlayer=enemyPlayer;
        this.friendPlayer=friendClient.getPracticePlayer();

        administer=new Administer(friendClient,enemyPlayer,this,"",friendClient.getTurnNumber(),false);
        friendPlayer.setAdminister(administer);
        this.enemyPlayer.setAdminister(administer);

        initMainPanels();
        initOnlineGamePanels();
        addPanels();
        animatingCards.start();

        this.online=true;
    }


    public Administer getAdminister(){
        return administer;
    }
    public void setMyTurn(boolean t){
        myTurn=t;
    }

    private void initOnlineGamePanels(){
       this.myPanel=new FriendPlayerPanel(friendPlayer,this);

       this.enemyPanel=new EnemyPlayerPanel(enemyPlayer,this);
    }

    private void initOfflineGamePanels(){
        this.myPanel=new FriendPlayerPanel(friendPlayer,this);
        myPanel.setSize(myPanel.width,myPanel.height);

        this.enemyPanel=new FriendPlayerPanel((PracticePlayer)enemyPlayer,this);
//        enemyPanel.setSize(enemyPanel.width,enemyPanel.height);
    }

    private void initMainPanels(){
        this.playGround=new GameView.PlayGround();
        playGround.setSize(playGround.width,playGround.height);

        container.setLayout(new BorderLayout());
        this.events=new GameView.Events();
        events.setSize(events.width,events.height);
        this.infoGiver=new InfoGiver();
        infoGiver.setPreferredSize(new Dimension(infoGiver.width,infoGiver.height));

        tools=new Tools();
    }

    private void addPanels(){
        this.setLayout(new BorderLayout());
        playGround.setSize(playGround.width,playGround.height);
        container.add(playGround,BorderLayout.CENTER);
        container.add(myPanel,BorderLayout.SOUTH);
        container.add((JPanel)enemyPanel,BorderLayout.NORTH);

        tools.setPreferredSize(new Dimension(tools.width,tools.height));
        container.add(events,BorderLayout.WEST);
        container.add(tools,BorderLayout.EAST);

        this.add(infoGiver,BorderLayout.WEST);
        this.add(container,BorderLayout.CENTER);

    }

    private void initFromDeckReader(){
        DeckReader dr=DeckReader.getDeckReader();
    }

    public void setMovingCad(Card card){
        this.movingCard=ImageLoader.getInstance().loadImage(card.getName(),"jpeg",140,140);
        setCardIsMoving(true);
    }
    public void setCardIsMoving(boolean t){
        if(t) {
            xMove = 600;
            yMove = 200;
        }
        this.cardIsMoving=t;
        if(!t){
            repaint();
            playGround.update();
        }
    }
    public void moveCard(int x,int y){
        xMove--;
        if(xMove==x) {
            playGround.update();
            setCardIsMoving(false);
        }
    }

    public void newTurn(){
        turn=(turn+1)%2;
        administer.newTurn(turn);

        tools.cntTime=60;
    }
    public void arrangeAttack(Attack attack){
        administer.setIsAttackerChosen(true);

        administer.setAttackerOwner(enemyPlayer);
        administer.setVictimOwner(friendPlayer);
        if(administer.isAttackerIsSpell()){

        }
        else if(attack.getKindOfAttacker().equalsIgnoreCase("Weapon"))
        administer.setAttackerIsWeapon(true);
        else{
            administer.setAttacker(administer.enemy_CardsOnGround.get(attack.getIndexOfAttackerMinion()));
        }

        if(attack.getKindOfTarget().equalsIgnoreCase("Hero"))
            administer.setVictim(friendPlayer.getHero());
        else
            administer.setVictim(administer.friend_CardsOnGround.get(attack.getIndexOfTargetMinion()));

    }


}
