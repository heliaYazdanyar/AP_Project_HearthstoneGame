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
    public void getGameRequest(ClientHandler clientHandler,boolean deckReader){
        System.out.println("got game request from: "+clientHandler.getUsername());
        if(deckReader){
            boolean flag=false;
            if(waitingList.size()>0){
                for(int i=0;i<waitingList.size();i++){
                    if(waitingList.get(i).isDeckReader()){
                        startNewGame(clientHandler, waitingList.get(i),true);
                        waitingList.remove(i);
                        flag=true;
                    }
                }
                if(!flag) waitingList.add(clientHandler);
            }else waitingList.add(clientHandler);

        }
        else {
            if (waitingList.size() > 0) {
                startNewGame(clientHandler, waitingList.get(0),false);
                waitingList.remove(0);
            } else waitingList.add(clientHandler);
        }
    }

    public void cancelGameRequest(ClientHandler clientHandler){
        waitingList.remove(clientHandler);
    }

    public void startNewGame(ClientHandler A,ClientHandler B,boolean deckReader){
        System.out.println("in start method");
        SecureRandom r=new SecureRandom();
        String gameName="game"+r.nextInt()+"*";
        APlayers.put(gameName,A);
        BPlayers.put(gameName,B);

        if(A.isDeckReader()) {
            NewGame forA = new NewGame(gameName, B.getUsername(), B.getHeroName(), 0,true);
            A.send("NewGame:" + forA.getJson());
            NewGame forB = new NewGame(gameName, A.getUsername(), A.getHeroName(), 1,true);
            B.send("NewGame:" + forB.getJson());
        }
        else{
            NewGame forA = new NewGame(gameName, B.getUsername(), B.getHeroName(), 0,false);
            A.send("NewGame:" + forA.getJson());
            NewGame forB = new NewGame(gameName, A.getUsername(), A.getHeroName(), 1,false);
            B.send("NewGame:" + forB.getJson());
        }

    }

    public void endGame(String gamName){
        APlayers.remove(gamName);
        BPlayers.remove(gamName);
    }


    public void sendMsgToOpponent(ClientHandler from,String gameName,String msg){
        System.out.println("sending to opponent:" + msg);
        if(APlayers.get(gameName)==from){
            BPlayers.get(gameName).send(msg);
        }
        else {
            APlayers.get(gameName).send(msg);
        }

    }

}
