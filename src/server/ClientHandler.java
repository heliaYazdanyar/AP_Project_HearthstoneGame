package server;

import messages.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket socket;
    private GameServer gameServer;

    private String clientUsername;
    private int authToken;
    private String gameName="";
    private boolean deckReader=false;

    private PrintStream printer;


    //in game fields
    private String heroName;
    private int cups;



    ClientHandler(Socket socket, GameServer gameServer){
        this.socket=socket;
        this.gameServer=gameServer;


    }

    public void setClientUsername(String username){
        this.clientUsername=username;
    }
    public String getUsername(){
        return clientUsername;
    }

    private void setAuthToken(int authToken){
        this.authToken=authToken;
    }
    private int getAuthToken(){
        return authToken;
    }

    public void send(String message){
        System.out.println("sending message with client : "+message);
        printer.println(message);
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            printer = new PrintStream(socket.getOutputStream());


            while (true) {
                String message= scanner.nextLine();
                System.out.println("client handler recieved: "+message);

                if(message.startsWith("username:")){
                    String un=message.substring(9);
                    setClientUsername(un);
                }

                else if(message.equals("login")){
                    gameServer.addNewClient(this);
                    send("authToken:"+gameServer.getToken(this));
                    this.authToken=gameServer.getToken(this);
                }

                else if(message.startsWith("Request:")){
                    String json=message.substring(8);
                    Request request=Request.getFromJson(json);

                    if(request.getAuthToken()==this.authToken){
                        if(request.getRequest().startsWith("heroIs:")){
                            setHero(request.getRequest().substring(7));
                        }
                        else if(request.getRequest().equals("gameRequest")){
                            gameServer.getGameRequest(this,false);
                        }
                        else if(request.getRequest().equals("gameWithDeckReader")){
                            deckReader=true;
                            gameServer.getGameRequest(this,true);
                        }
                        else if(request.getRequest().equals("cancelGameRequest")){
                            gameServer.cancelGameRequest(this);
                        }
                    }

                    else
                        gameServer.wrongToken(this);

                }

                else if(message.startsWith("GameMessage:")){
                    String json=message.substring(12);
                    GameMessage gameMessage=GameMessage.getFromJson(json);
                    if(gameMessage.getAuthToken()==this.authToken){
                        if(gameMessage.getSubject().equalsIgnoreCase("gameViewInfo")){
                            gameServer.sendMsgToOpponent(this,gameMessage.getGameName(),message);
                        }
                        else if(gameMessage.getSubject().equals("youWin")){
                            gameServer.sendMsgToOpponent(this,gameMessage.getGameName(),message);
                            gameServer.endGame(gameMessage.getGameName());
                        }
                        else if(gameMessage.getSubject().equals("youLost")){
                            gameServer.sendMsgToOpponent(this,gameMessage.getGameName(),message);
                            gameServer.endGame(gameMessage.getGameName());
                        }
                        else if(gameMessage.getSubject().equals("endTurn")){
                            gameServer.sendMsgToOpponent(this,gameMessage.getGameName(),message);
                        }
                        //code--here


                    }
                    else gameServer.wrongToken(this);

                }

                else if(message.startsWith("Attack:")){
                    String json=message.substring(7);
                    Attack attack=Attack.getFromJson(json);
                    if(attack.getAuthToken()==this.authToken)
                        gameServer.sendMsgToOpponent(this,attack.getGameName(),message);
                    else
                        gameServer.wrongToken(this);
                }

                else if(message.startsWith("InfoGiver:")){
                    String json=message.substring(10);
                    InfoGiverMsg infoGiverMsg=InfoGiverMsg.getFromJson(json);
                    if(infoGiverMsg.getAuthToken()==this.authToken){
                        //code-here
                    }
                    else gameServer.wrongToken(this);
                }

                else if(message.startsWith("PlayCard:")){
                    gameServer.sendMsgToOpponent(this,gameName,message);
                }
                else if(message.startsWith("ListMessage:")){
                    String json=message.substring(12);
                    ListMessage listMessage=ListMessage.getFromJson(json);
                    if(listMessage.getAuthToken()==authToken){
                        gameServer.sendMsgToOpponent(this,listMessage.getGameName(),message);

                        if(!gameName.equals(listMessage.getGameName())) gameName=listMessage.getGameName();
                    }
                    else gameServer.wrongToken(this);
                }
                else if(message.startsWith("SendingVictim:")){
                    String json=message.substring(14);
                    SendingVictim sendingVictim=SendingVictim.getFromJson(json);
                    if(sendingVictim.getAuthToken()==authToken){
                        gameServer.sendMsgToOpponent(this,sendingVictim.getGameName(),message);
                    }
                    else gameServer.wrongToken(this);
                }
                else if(message.startsWith("GameChatMessage:")){
                    String json=message.substring(16);
                    GameChatMessage gameChatMessage=GameChatMessage.getFromJson(json);
                    if(gameChatMessage.getAuthToken()==authToken){
                        gameServer.sendMsgToOpponent(this,gameChatMessage.getGameName(),message);
                    }
                    else gameServer.wrongToken(this);
                }



            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //in game methods

    public boolean isDeckReader() {
        return deckReader;
    }

    private void setHero(String heroName){
        this.heroName=heroName;
    }
    public String getHeroName(){
        return heroName;
    }

    private void setCups(int cups){
        this.cups=cups;
    }
    private int getCups(){
        return cups;
    }

    public void gameViewInformation(String information){

    }



}
