package messages;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListMessage {
    private int authToken;
    private String gameName;
    private String title;
    private String listInString;

    public ListMessage(int authToken, String gameName, String title,List<String> list){
        this.authToken=authToken;
        this.gameName=gameName;
        this.title=title;

        this.listInString=covertListToString(list);

    }

    private String covertListToString(List<String> list){
        String result="";
        if(list.size()>0) {
            for (int i = 0; i < list.size() - 1; i++) {
                result = result + list.get(i) + ",";
            }
            result = result + list.get(list.size() - 1);
        }
        return result;
    }

    public int getAuthToken() {
        return authToken;
    }
    public String getGameName(){
        return gameName;
    }
    public String getTitle(){
        return title;
    }
    public String getListInString() {
        return listInString;
    }

    public List<String> getList(){
        String[] array=listInString.split(",");
        List<String> result=new ArrayList<>();
        for(int i=0;i<array.length;i++)
            result.add(array[i]);
        return result;
    }

    public String getJson(){
        String result="{'authToken':"+this.authToken+",'gameName':'"+gameName+"','title':'"+title+"','listInString':'"+listInString+"'}";
        return result;
    }
    public static ListMessage getFromJson(String json){
        Gson gson= new Gson();
        return gson.fromJson(json,ListMessage.class);
    }


}
