package client;

import messages.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Transmitter extends Thread{

    private InputStream inputStream;
    private PrintStream printStream;

    private GameClient gameClient;


    Transmitter(InputStream inputStream, PrintStream printStream,GameClient client){
        this.gameClient=client;

        this.printStream=printStream;
        this.inputStream=inputStream;

    }




    @Override
    public void run() {
        Scanner scanner = new Scanner(inputStream);
        while (!isInterrupted()) {
            String message = scanner.next();
            System.out.println("Transmitter recieved: "+message);

            if(message.startsWith("authToken:")){
                String token=message.substring(10);
                gameClient.setToken(Integer.parseInt(token));
            }
            else if(message.startsWith("onlines:")){
                gameClient.setOnlineNames(message);
            }
            else if(message.startsWith("offlines:")){
                gameClient.setOfflineNames(message);
            }

            else if(message.startsWith("NewGame:")){
                String gameJson=message.substring(8);
                NewGame newGame=NewGame.getFromJson(gameJson);
                gameClient.startGame(gameJson,newGame.isDeckReader());
            }

            else if(message.startsWith("GameMessage:")){
                String json=message.substring(12);
                GameMessage gameMessage=GameMessage.getFromJson(json);
                if(gameMessage.getSubject().equalsIgnoreCase("gameViewInfo")){
                    gameClient.administer.setInformation(gameMessage.getExplanation(),false);
                }
                else if(gameMessage.getSubject().equals("youWin")){
                    //cups++
                    //gameview show wining

                }
                else if(gameMessage.getSubject().equals("youLost")){
                    //cups--
                    //gameview show losng

                }
                else if(gameMessage.getSubject().equals("endTurn")){
                    System.out.println("new turn n transmitter");
                    gameClient.gameView.newTurn();
                }
            }

            else if(message.startsWith("Attack:")){
                String json=message.substring(7);
                Attack attack=Attack.getFromJson(json);
                gameClient.gameView.arrangeAttack(attack);
            }

            else if(message.startsWith("PlayCard:")){
                String json=message.substring(9);
                int index=json.indexOf('-');
                String type=json.substring(0,index);

                System.out.println("type: "+type);
                System.out.println("Json: "+json.substring(index+1));

                gameClient.enemyPlayCard(type,json.substring(index+1));
            }

            else if(message.startsWith("ListMessage:")) {
                String json=message.substring(12);
                ListMessage listMessage=ListMessage.getFromJson(json);
                if(listMessage.getTitle().equals("enemyDeck")){
                    gameClient.administer.updateEnemyDeckList(listMessage.getList());
                }
                else if(listMessage.getTitle().equals("handCards")){
                    gameClient.administer.updateEnemyHand(listMessage.getList());
                }
            }
            else if(message.startsWith("SendingVictim:")) {
                String json = message.substring(14);
                SendingVictim sendingVictim=SendingVictim.getFromJson(json);
                gameClient.administer.setVictimOwner(gameClient.getPracticePlayer());
                if(sendingVictim.getVictimType().equals("Minion"))
                    gameClient.administer.setVictim(gameClient.administer.friend_CardsOnGround.get(sendingVictim.getIndexOfMinionVictim()));
                else
                    gameClient.administer.setVictim(gameClient.getPracticePlayer().getHero());

            }
            else if(message.startsWith("GameChatMessage:")){
                String json=message.substring(16);
                GameChatMessage gameChatMessage=GameChatMessage.getFromJson(json);
                gameClient.receiveGameChatMsg(gameChatMessage.getMessage());
            }
            else if(message.startsWith("UseHeroPower:")){
                gameClient.administer.callHeroPower(gameClient.getEnemy());
                gameClient.administer.setInformation("Enemy using HeroPower",false);
            }



        }

    }
}
