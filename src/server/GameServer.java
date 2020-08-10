package server;

import messages.NewGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameServer extends Thread{

    private ServerSocket serverSocket;

    private List<ClientHandler> clients;
    private HashMap<String,Integer> authTokens;

//    private HashMap<String, OnlineAdminister> runningGames;
    private HashMap<String ,ClientHandler> APlayers;
    private HashMap<String ,ClientHandler> BPlayers;
    private List<ClientHandler> waitingList;

//    private Thread matchingPlayers=new Thread();


    GameServer(int serverPort) throws IOException {
        clients=new ArrayList<>();
        authTokens=new HashMap<>();
        waitingList=new ArrayList<>();
//        runningGames=new HashMap<>();
        APlayers=new HashMap<>();
        BPlayers=new HashMap<>();

        this.serverSocket=new ServerSocket(serverPort);


    }

    public void addNewClient(ClientHandler client){
        SecureRandom r=new SecureRandom();
        int authToken=r.nextInt();
        authTokens.put(client.getUsername(),authToken);
    }


    public int getToken(ClientHandler clientHandler){
        return authTokens.get(clientHandler.getUsername());
    }

    public void wrongToken(ClientHandler clientHandler){

    }


    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                Socket socket=serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(socket,this);
                clients.add(clientHandler);

                System.out.println("Client at: " + socket.getRemoteSocketAddress().toString() + " is connected.");

                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //matching players for game
    public void getGameRequest(ClientHandler clientHandler){
        System.out.println("got game request from: "+clientHandler.getUsername());

        if(waitingList.size()>0){
            System.out.println("game starts");
            startNewGame(clientHandler,waitingList.get(0));
            waitingList.remove(0);
        }
        else waitingList.add(clientHandler);
    }

    public void cancelGameRequest(ClientHandler clientHandler){
        waitingList.remove(clientHandler);
    }

    public void startNewGame(ClientHandler A,ClientHandler B){
        System.out.println("in start method");
        SecureRandom r=new SecureRandom();
        String gameName="game"+r.nextInt()+"*";
        APlayers.put(gameName,A);
        BPlayers.put(gameName,B);
//        runningGames.put(gameName,new OnlineAdminister(A,B,gameName));

        NewGame forA=new NewGame(gameName,B.getUsername(),B.getHeroName());
        A.send("NewGame:"+forA.getJson());
        NewGame forB=new NewGame(gameName,A.getUsername(),A.getHeroName());
        B.send("NewGame:"+forB.getJson());

    }

    public String getPlayerA(String gameName){
        return APlayers.get(gameName).getPlayer();
    }
    public String getPlayerB(String gameName){
        return BPlayers.get(gameName).getPlayer();
    }

    public void AUseMethod(String gameName){

    }
    public void BUseMethod(String gameName){

    }

    public void endGame(String gamName){
        APlayers.remove(gamName);
        BPlayers.remove(gamName);
    }

    public void sendMsgToOpponent(ClientHandler from,String gameName,String msg){
        if(APlayers.get(gameName).getUsername()==from.getUsername()){
            BPlayers.get(gameName).send(msg);
        }
        else {
            APlayers.get(gameName).send(msg);
        }

    }

}
