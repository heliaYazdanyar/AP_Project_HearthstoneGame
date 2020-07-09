package Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    public static void createNewLog(String username,String password){
        List<String> header = new ArrayList<>();
        header.add("USER:" + username);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        header.add("CREATED_ADD:" + dtf.format(now));
        header.add("PASSWORD:" + password);
        try {
            String cwd=System.getProperty("user.dir");
            Files.createFile(Paths.get(cwd + File.separator+"resources"+File.separator
                    +"logs"+File.separator+username+"-"+"Log.txt" ));
            Path log = Paths.get(cwd + File.separator+"resources"+File.separator+
                    "logs"+File.separator +username+"-"+"Log.txt" );
            Files.write(log, (header.get(0) + "\n" + header.get(1) + "\n" + header.get(2)+"\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) { e.printStackTrace(); }
    }
    public static void logger(String username,String event, String Discription){
        List<String> txt=new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String text=event+":"+dtf.format(now)+">>"+Discription;
        txt.add("\n"+text);
        writeInTxtFile("logs"+File.separator+username+"-Log",txt);
    }
    private static void writeInTxtFile(String fileName,List<String> text){
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+File.separator+"resources"+File.separator+fileName+".txt");
            Files.write(p,text, StandardOpenOption.APPEND);
        }catch(IOException w){ w.printStackTrace(); }
    }

}
