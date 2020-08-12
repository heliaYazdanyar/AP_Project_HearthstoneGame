package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static String getPort(){
        List<String> result=null;
        try{
            result= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator+
                    "server.txt"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return result.get(0);
    }

    public static List<String> loadAllPlayers(){
        List<String> users=new ArrayList<>();
        try{
            users=Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+"resources"+File.separator+"users.txt"));
        }catch (IOException e){
            e.printStackTrace();
        }
        List<String> names=new ArrayList<>();
        for (String st:
             users) {
            if(st.length()>0) {
                int index = 0;
                for (int i = 0; i < st.length(); i++) {
                    if (st.charAt(i) == ' ') {
                        index = i;
                        break;
                    }
                }
                names.add(st.substring(0, index));
                System.out.println("config loaded name:" + st.substring(0, index));
            }
        }
        return names;
    }

}
