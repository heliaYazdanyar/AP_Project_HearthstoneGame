package client;

import messages.GameMessage;

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

            else if(message.startsWith("NewGame:")){
                String gameJson=message.substring(8);
                gameClient.startGame(gameJson);
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
            }


        }

    }
}
