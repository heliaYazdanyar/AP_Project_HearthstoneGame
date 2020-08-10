package Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
}
