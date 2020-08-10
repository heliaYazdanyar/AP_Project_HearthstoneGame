package server;

import messages.Attack;
import messages.GameMessage;
import messages.InfoGiverMsg;
import messages.Request;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket socket;
    private GameServer gameServer;

    private String clientUsername;
    private int authToken;

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
                    System.out.println(this.authToken+"my authtoken");
                    System.out.println(request.getAuthToken());
                    if(request.getAuthToken()==this.authToken){
                        if(request.getRequest().startsWith("heroIs:")){
                            setHero(request.getRequest().substring(7));
                        }
                        else if(request.getRequest().equals("gameRequest")){
                            gameServer.getGameRequest(this);
                            System.out.println("heyyy");
                        }
                        else if(request.getRequest().equals("cancelGameRequest")){
                            gameServer.cancelGameRequest(this);
                        }
                    }
                    else{
                        gameServer.wrongToken(this);
                    }
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
                        //code--here


                    }
                    else gameServer.wrongToken(this);

                }
                else if(message.startsWith("Attack:")){
                    String json=message.substring(7);
                    Attack attack=Attack.getFromJson(json);
                    if(attack.getAuthToken()==this.authToken){


                    }
                    else gameServer.wrongToken(this);
                }
                else if(message.startsWith("InfoGiver:")){
                    String json=message.substring(10);
                    InfoGiverMsg infoGiverMsg=InfoGiverMsg.getFromJson(json);
                    if(infoGiverMsg.getAuthToken()==this.authToken){
                        //code-here
                    }
                    else gameServer.wrongToken(this);
                }



            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //in game methods

    public void setPlayer(){

    }
    public String getPlayer(){
        return null;
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

    public void methods(){

    }



}
