package client;

import Models.Card;
import Out.MainFrame;
import game.Out.GameView;
import gamePlayers.OnlineEnemy;
import gamePlayers.Player;
import gamePlayers.PracticePlayer;
import logic.Administer;
import messages.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

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

    public void sendRequest(String request){
        Request r=new Request(this.authToken,request);

        socketPrinter.println("Request:"+r.getJson());
    }
    public void sendMove(){

    }
    public void sendString(String message){
        socketPrinter.println(message);
    }
    public void sendCard(String type,String cardJson){
        SendingCard sendingCard=new SendingCard(this.authToken,type,cardJson);
        socketPrinter.println("SendingCard:"+sendingCard.getJson());
    }
    public void sendInfoGiver(String first,String second,String third){
        InfoGiverMsg infoGiverMsg=new InfoGiverMsg(authToken,first,second,third);
        socketPrinter.println("InfoGiver:"+infoGiverMsg.getJson());
    }

    public void sendGameMessage(String subject,String explanation){
        GameMessage gameMessage=new GameMessage(authToken,gameName,subject,explanation);

        socketPrinter.println("GameMessage:"+gameMessage.getJson());
    }
    public void sendAttack(boolean hero,boolean dead,int damage,String gameViewInfo,String nameOfMinion,int indexOfMinion){
        Attack attack=new Attack(authToken,hero,dead,damage,gameViewInfo,nameOfMinion,indexOfMinion);

        socketPrinter.println("Attack:"+attack.getJson());
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
    public void cancelRequestForGame(){
        sendRequest("cancelGameRequest");
    }

    //in game methods
    public void startGame(String newGameJson){
        NewGame newGame=NewGame.getFromJson(newGameJson);

        this.gameName=newGame.getGameName();
        setEnemy(newGame.getEnemyUsername(),newGame.getEnemyHeroName());

        setPracticePlayer();

        MainFrame.getInstance().getPanelHandler().addPracticeOnline();
    }

    public void setPracticePlayer(){
        System.out.println("practice player was setted for client");
        this.practicePlayer=new PracticePlayer(player);
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

    public void enemyPlayCard(String message){
        String json=message.substring(9);
        SendingCard sendingCard=SendingCard.getFromJson(json);
        Card card=Card.getCardFromJson(sendingCard.getType(),sendingCard.getCardJson());
        gameView.events.addEvent(card.getName() + " was played.", enemy);
        gameView.events.updateEvents();
        gameView.setMovingCad(card);

        enemy.playCard(card);

        if(card.getType().equalsIgnoreCase("Weapon")) gameView.enemyPanel.update();

        if(card.getType().equalsIgnoreCase("Spell")){

        }
        else if(card.getType().equalsIgnoreCase("Minion")){

        }
        else if(card.getType().equalsIgnoreCase("Weapon")){

        }
        else{

        }

    }
    public void sendPlayCard(Card card){
        SendingCard sendingCard=new SendingCard(authToken,card.getType(),card.getThisCardsJson());
        String message="PlayCard:"+sendingCard.getJson();
        socketPrinter.println(message);
    }


}
