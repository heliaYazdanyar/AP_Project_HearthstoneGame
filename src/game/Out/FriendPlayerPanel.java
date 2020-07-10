package game.Out;

import Models.Card;
import Out.MainFrame;
import Util.ImageLoader;
import Util.SoundPlayer;
import gamePlayers.PracticePlayer;
import logic.Administer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FriendPlayerPanel extends JPanel {

    private SoundPlayer soundPlayer;
    private PracticePlayer player;
    private GameView gameView;

    private JLabel hpLabel;
    private JLabel manaLabel;
    private JLabel weaponLabel;
    private JLabel deckLabel;
    private JLabel heroPowerLabel;
    private HandCards handCards;
    private JScrollPane scrollPane;
    private JPanel hpAndManaPanel;

    int width=1100;
    int height=140;


    FriendPlayerPanel(PracticePlayer player,GameView gameView){
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.player=player;
        this.gameView=gameView;

        hpAndManaPanel=new JPanel();
        hpAndManaPanel.setLayout(new BoxLayout(hpAndManaPanel,BoxLayout.Y_AXIS));
        update();
    }

    class HandCards extends JPanel{

        private int width=500;
        private int height=180;
        private List<JLabel> cardLabels;
        private List<Card> handCards;

        private Thread cardsThread=new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                        hpLabel.setText(""+player.getHero().getHP());
                        if(player.isMyTurn() && player.needsUpdate()) {
                            HandCards.this.update();
                            player.changeNeedsUpdate(false);
                            manaLabel.setText(""+player.getMana());
                            deckLabel.setText(player.getdeckCnt()+"");
                            FriendPlayerPanel.this.repaint();
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        HandCards() {
            this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
            this.setSize(width,height);

            cardLabels=new ArrayList<>();
            handCards=new ArrayList<>();

            initPanel();
            cardsThread.start();
        }

        private void initPanel(){
            this.removeAll();
            cardLabels.removeAll(cardLabels);
            handCards=FriendPlayerPanel.this.player.getHandCards();

            for(int i=0;i<handCards.size();i++){
                cardLabels.add(new JLabel(ImageLoader.getInstance().loadIcon(handCards.get(i).getName(),"jpeg",140,140)));
                final int t=i;
                cardLabels.get(t).addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Card card=handCards.get(t);
                        if(player.isMyTurn() ) {
                            if(player.getMana()>= card.getManaCost()) {
                                gameView.events.addEvent(card.getName() + " was played.", player);
                                gameView.events.updateEvents();
                                gameView.setMovingCad(handCards.get(t));
                                removeCard(card);
                                player.playCard(card);
                                if(card.getType().equalsIgnoreCase("Weapon")) FriendPlayerPanel.this.update();
                            }else{
                                JOptionPane.showMessageDialog(MainFrame.getInstance(),"you dont have enough MANA:(",
                                        "ERROR",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        gameView.infoGiver.initCardInfo(handCards.get(t));
                    }

                });
                this.add(cardLabels.get(i));
            }
        }

        private void addCard(Card card){
            player.addToHand(card);
            handCards.add(card);
            update();
        }

        private void removeCard(Card card){
            cardLabels.remove(handCards.indexOf(card));
            handCards.remove(card);
            update();
        }

        private void update(){
            initPanel();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
                    HandCards.this.width*10,HandCards.this.height*10);
            g.drawImage(background,0,0,null);
        }

    }


    private void initDeckLabel(){
        deckLabel=new JLabel(ImageLoader.getInstance().loadIcon("deck","png",50,140));
        deckLabel.setText(player.getdeckCnt()+"");
        deckLabel.setHorizontalTextPosition(JLabel.CENTER);
        deckLabel.setVerticalTextPosition(JLabel.CENTER);
        deckLabel.setForeground(Color.BLACK);
        deckLabel.setFont(new Font("Courier New", Font.BOLD, 18));
        deckLabel.setOpaque(true);
    }
    private void initHandCards(){
        if(handCards==null)handCards=new HandCards();
        else handCards.update();
        scrollPane=new JScrollPane(handCards,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    private void initManaPanel(){
        manaLabel=new JLabel(ImageLoader.getInstance().loadIcon("mana","png",65,65),SwingUtilities.CENTER);
        manaLabel.setText(player.getMana()+"");
        manaLabel.setHorizontalTextPosition(JLabel.CENTER);
        manaLabel.setVerticalTextPosition(JLabel.CENTER);
        manaLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        manaLabel.setForeground(Color.BLUE);
        manaLabel.setOpaque(true);
    }
    public void initWeapon(){
        if(Administer.getInstance().hasWeapon(player)){
            weaponLabel=new JLabel(ImageLoader.getInstance().loadIcon(player.getWeapon().getName(),"jpeg",120,140));
            weaponLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(player.isMyTurn()){
                        if(Administer.getInstance().isAttackerChosen()){
                            Administer.getInstance().setInformation("Choose target");
                        }
                        else {
                            Administer.getInstance().setInformation("Weapon chosen");
                            Administer.getInstance().setIsAttackerChosen(true);
                            Administer.getInstance().setAttackerIsWeapon(true);
                            Administer.getInstance().setAttackerOwner(player);
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    gameView.infoGiver.initWeaponInfo(player.getWeapon());
                }
            });

        }
        else{
            weaponLabel=new JLabel(ImageLoader.getInstance().loadIcon("emptyWeapon","jpg",120,140));
            weaponLabel.setBackground(Color.getHSBColor(106, 114, 124 ));
        }
    }
    public void initHeroPower(){
        heroPowerLabel=new JLabel(ImageLoader.getInstance().loadIcon(player.getHero().getHeroPowerName(),
                "jpeg",120,140));
        heroPowerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(player.isMyTurn()) {
                    if(Administer.getInstance().canCallHeroPower(player)) {
                        Administer.getInstance().callHeroPower(player);
                        Administer.getInstance().setInformation("HeroPower chosen");
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                gameView.infoGiver.initHeroPowerInfo(player.getHero().getHeroPowerName());
            }

        });
    }
    public void initHpPanel(){
        hpLabel=new JLabel(ImageLoader.getInstance().loadIcon("hpIcon","png",65,65),SwingUtilities.CENTER);
        hpLabel.setText(player.getHero().getHP()+"");
        hpLabel.setHorizontalTextPosition(JLabel.CENTER);
        hpLabel.setVerticalTextPosition(JLabel.CENTER);
        hpLabel.setForeground(Color.BLACK);
        hpLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        hpLabel.setOpaque(true);
    }


    private void addComponents(){
        this.removeAll();
        this.add(weaponLabel);
        this.add(scrollPane);
        this.add(deckLabel);

        hpAndManaPanel.removeAll();
        hpAndManaPanel.add(manaLabel);
        hpAndManaPanel.add(hpLabel);
        this.add(hpAndManaPanel);
        this.add(heroPowerLabel);
    }

    public void update(){
        initDeckLabel();
        initManaPanel();
        initHandCards();
        initWeapon();
        initHeroPower();
        initHpPanel();

        addComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage background= ImageLoader.getInstance().loadImage("mainMenuBackground","jpg",
                width,height);
        g.drawImage(background,0,0,null);


    }
}
