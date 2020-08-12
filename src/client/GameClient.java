package client;

import models.Card;
import Out.MainFrame;
import game.Out.GameView;
import gamePlayers.OnlineEnemy;
import gamePlayers.Player;
import gamePlayers.PracticePlayer;
import logic.Administer;
import messages.*;
import util.DeckReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class GameClient extends Thread{
    private String username;
    private Player player;

    private Socket socket;
    private int authToken;

    private InputStream socketInputStream ;
    private PrintStream socketPrinter;

    private Transmitter transmitter;

    //in game fields
    public Administer administer;
    private boolean inGame;
    private String gameName;
    private PracticePlayer practicePlayer;
    public GameView gameView;
    private OnlineEnemy enemy;
    private int turnNumber;

    public GameClient(String serverIP,int serverPort,String username) throws IOException {
        this.socket=new Socket(serverIP,serverPort);
        this.username=username;
        this.player=new Player(username);

        System.out.println("Connected to Server at: " + serverIP + ":" + serverPort);

        this.socketInputStream = socket.getInputStream();
        this.socketPrinter = new PrintStream(socket.getOutputStream());


        this.inGame=false;

        sendString("username:"+username);
        sendString("login");
    }

    public void setToken(int authToken){
        this.authToken=authToken;
    }

    public String getUsername(){
        return username;
    }

    //sending methods
    public void sendRequest(String request){
        Request r=new Request(this.authToken,request);

        socketPrinter.println("Request:"+r.getJson());
    }
    public void sendString(String message){
        socketPrinter.println(message);
    }
    public void sendCard(String type,String cardJson){
        SendingCard sendingCard=new SendingCard(this.authToken,gameName,type,cardJson);
        socketPrinter.println("SendingCard:"+sendingCard.getJson());
    }
    public void sendInfoGiver(String first,String second,String third){
        InfoGiverMsg infoGiverMsg=new InfoGiverMsg(authToken,first,second,third);
        socketPrinter.println("InfoGiver:"+infoGiverMsg.getJson());
    }

    public void sendList(String title, List<String> list){
        ListMessage listMessage=new ListMessage(authToken,gameName,title,list);
        socketPrinter.println("ListMessage:"+listMessage.getJson());
    }

    public void sendGameMessage(String subject,String explanation){
        GameMessage gameMessage=new GameMessage(authToken,gameName,subject,explanation);

        socketPrinter.println("GameMessage:"+gameMessage.getJson());
    }


    public Player getPlayer(){
        return player;
    }

    @Override
    public void run() {
        transmitter=new Transmitter(socketInputStream,socketPrinter,this);

        transmitter.start();
        while (isStillAlive()) {
            try {
                Thread.sleep(100);

            } catch (InterruptedException ignore) { }
        }

        transmitter.interrupt();

    }

    public boolean isStillAlive(){
        return (socket.isConnected() && transmitter.isAlive());
    }

    public void requestForGame(){
        //sending hero that we are playing with
        sendRequest("heroIs:"+player.getHeroName());

        sendRequest("gameRequest");
    }
    public void requestForDeckReaderGame(){
        sendRequest("gameWithDeckReader");
    }
    public void cancelRequestForGame(){
        sendRequest("cancelGameRequest");
    }

    //in game methods
    public void startGame(String newGameJson,boolean deckReader){
        NewGame newGame=NewGame.getFromJson(newGameJson);
        this.turnNumber=newGame.getTurnNumber();
        this.gameName=newGame.getGameName();


        if(deckReader)
           setEnemy(newGame.getEnemyUsername(),"Mage");
        else
            setEnemy(newGame.getEnemyUsername(),newGame.getEnemyHeroName());


        if(!deckReader) {
            setPracticePlayer(false, null);
            MainFrame.getInstance().getPanelHandler().addPracticeOnline();
        }
        else{
            if(turnNumber==0) practicePlayer=new PracticePlayer(player.getUsername(),false, DeckReader.getDeckReader());
            else practicePlayer=new PracticePlayer(player.getUsername(),true,DeckReader.getDeckReader());

            MainFrame.getInstance().getPanelHandler().addPracticeWithDeckReader_Online();
        }
    }

    public void setPracticePlayer(boolean deckReader,PracticePlayer player1){
        System.out.println("practice player was setted for client");
        if(deckReader)
            this.practicePlayer=player1;
        else
            this.practicePlayer=new PracticePlayer(this.player);
    }
    public PracticePlayer getPracticePlayer(){
        return practicePlayer;
    }

    public void setGameView(GameView gameView){
        this.gameView=gameView;
        this.administer= gameView.getAdminister();
    }

    public void setEnemy(String username,String heroName){
        enemy=new OnlineEnemy(username,heroName);

    }
    public OnlineEnemy getEnemy(){
        return enemy;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void enemyPlayCard(String type,String cardJson){
        Card card=Card.getCardFromJson(type,cardJson);

        gameView.events.addEvent(card.getName() + " was played.", enemy);
        gameView.events.updateEvents();
        gameView.setMovingCad(card);

        enemy.playCard(card);

        if(card.getType().equalsIgnoreCase("Weapon")) gameView.enemyPanel.update();

    }
    public void sendPlayCard(Card card){
        String message="PlayCard:"+card.getType()+"-"+card.getThisCardsJson();
        socketPrinter.println(message);
    }

    public void sendVictim(String type,int indexOfMinion){
        SendingVictim sendingVictim=new SendingVictim(authToken,gameName,type,indexOfMinion);
        socketPrinter.println("SendingVictim:"+sendingVictim.getJson());
    }
    public void sendAttack(String kindOfAttacker,int indexOfAttackerMinion
            ,int damage,String kindOfTarget,int indexOfTargetMinion){
        Attack attack=new Attack(authToken,gameName,kindOfAttacker,indexOfAttackerMinion,damage,kindOfTarget,indexOfTargetMinion);

        socketPrinter.println("Attack:"+attack.getJson());
    }

    public void sendGameChatMsg(String message){
        GameChatMessage gameChatMessage=new GameChatMessage(authToken,gameName,message);
        socketPrinter.println("GameChatMessage:"+gameChatMessage.getJson());
    }
    public void receiveGameChatMsg(String message){
        gameView.chatRoom.sendText(enemy.getUsername(),message);
    }





}
