package game.Out;

import Util.DeckReader;
import gamePlayers.Player;
import gamePlayers.PracticePlayer;

import javax.swing.*;
import java.awt.*;

public class PanelHandler extends JPanel {
    private PracticePlayer friendPlayer;
    private PracticePlayer enemyPlayer;

    private final CardLayout cardLayout=new CardLayout();

    private GameView gameView;
    private StartGamePanel startGamePanel;
    private PassiveChooser passiveChooser;
    private ChooseCard chooseCardFriend;
    private ChooseCard chooseCardEnemy;

    public int width=1400;
    public int height=800;

    public PanelHandler(Player player){
        this.setLayout(cardLayout);

        startGamePanel=new StartGamePanel(player,this);
        this.add("startGame",startGamePanel);
        cardLayout.show(this,"startGame");
    }

    public void initPassivePanel(){
        passiveChooser=new PassiveChooser(friendPlayer,this);
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
        initPassivePanel();
    }
    public void addPracticeWithBot(Player player){

        initPassivePanel();
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
        initPassivePanel();
    }


    public void setPanel(String panelName){
        if(panelName.equalsIgnoreCase("GameView")) {
            this.setPreferredSize(new Dimension(width,height));
        }
        else this.setPreferredSize(new Dimension(900,850));
        cardLayout.show(this,panelName);
    }


}
