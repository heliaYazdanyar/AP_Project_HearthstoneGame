package server;

import util.Config;
import java.io.IOException;

public class ServerMain {


    public static void main(String[] args) throws IOException {
        int port= Integer.parseInt(Config.getPort());

        new GameServer(port).start();

    }
}
