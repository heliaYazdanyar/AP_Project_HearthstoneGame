package server;

import java.util.ArrayList;
import java.util.List;

public class OnlineThread extends Thread {

    List<String> onlineNames;
    List<String> offlineNames;

    OnlineThread(){
        onlineNames=new ArrayList<>();
        offlineNames=new ArrayList<>();

    }

    @Override
    public void run() {



    }
}
