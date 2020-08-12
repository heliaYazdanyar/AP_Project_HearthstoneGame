package game.Out;

import util.DeckReader;
import client.GameClient;
import gamePlayers.Player;
import gamePlayers.PracticePlayer;

import javax.swing.*;
import java.awt.*;

public class PanelHandler extends JPanel {
    private GameClient client;


    private PracticePlayer friendPlayer;
    private PracticePlayer enemyPlayer;

    private final CardLayout cardLayout=new CardLayout();

    private GameView gameView;
    private StartGamePanel startGamePanel;
    private PassiveChooser passiveChooser;
    private ChooseCard chooseCardFriend;
    private ChooseCard chooseCardEnemy;
    private Waiting waitingPanel;

    public int width= (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();;
    public int height=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();;

    public PanelHandler(GameClient client){
        this.setLayout(cardLayout);

        this.client=client;

        waitingPanel=new Waiting(client,this);
        this.add("waiting",waitingPanel);

        startGamePanel=new StartGamePanel(client,this);
        this.add("startGame",startGamePanel);
        cardLayout.show(this,"startGame");
    }

    public void initPassivePanel(boolean online){
        passiveChooser=new PassiveChooser(friendPlayer,this,online);
        this.add("Passives",passiveChooser);
    }

    public void addPracticeWithDeckReader(Player player){
        DeckReader deckReader=DeckReader.getDeckReader();

        this.friendPlayer=new PracticePlayer(player.getUsername(),false,deckReader);
        this.enemyPlayer=new PracticePlayer("kingTogwaggle",true,deckReader);

        chooseCardFriend=new ChooseCard(friendPlayer,this,"friend");
        this.add("ChooseCardFriend",chooseCardFriend);
        chooseCardEnemy=new ChooseCard(enemyPlayer,this,"Enemy");
        this.add("ChooseCardEnemy",chooseCardEnemy);

        gameView=new GameView(friendPlayer,enemyPlayer,true);
        this.add("GameView",gameView);
        initPassivePanel(false);
    }
    public void addPracticeWithBot(Player player){

        initPassivePanel(false);
    }
    public void addPracticeWithRandomEnemy(Player player){
        this.friendPlayer=new PracticePlayer(player);
        this.enemyPlayer=new PracticePlayer("kingTogwaggle",0);

        chooseCardFriend=new ChooseCard(friendPlayer,this,"Friend");
        this.add("ChooseCardFriend",chooseCardFriend);
        chooseCardEnemy=new ChooseCard(enemyPlayer,this,"Enemy");
        this.add("ChooseCardEnemy",chooseCardEnemy);

        gameView=new GameView( friendPlayer,enemyPlayer);
        this.add("GameView",gameView);
        initPassivePanel(false);
    }

    public void addPracticeOnline(){
        this.friendPlayer=client.getPracticePlayer();

        chooseCardFriend=new ChooseCard(friendPlayer,this,"friend");
        this.add("ChooseCardFriend",chooseCardFriend);

        initPassivePanel(true);

        gameView=new GameView(this.client,this.client.getEnemy(),"");
        client.setGameView(gameView);

        this.add("GameView",gameView);
        cardLayout.show(this,"Passives");

    }
    public void addPracticeWithDeckReader_Online(){
        this.friendPlayer=client.getPracticePlayer();

        chooseCardFriend=new ChooseCard(friendPlayer,this,"friend");
        this.add("ChooseCardFriend",chooseCardFriend);

        initPassivePanel(true);

        gameView=new GameView(this.client,this.client.getEnemy(),"");
        client.setGameView(gameView);

        this.add("GameView",gameView);
        cardLayout.show(this,"Passives");

    }

    public void setPanel(String panelName){
        if(panelName.equalsIgnoreCase("GameView")) {
            this.setPreferredSize(new Dimension(width,height));
        }
        else this.setPreferredSize(new Dimension(900,850));
        cardLayout.show(this,panelName);
    }


}
