package messages;

import com.google.gson.Gson;

public class Request {
     int authToken;
     String request;


     public Request(int authToken,String request){
         this.authToken=authToken;
         this.request=request;

     }

     public String getJson(){
         String result="{'authToken':"+this.authToken+",'request':'"+this.request+"'}";
         return result;
     } 



     public int getAuthToken(){
         return this.authToken;
     }
     public String getRequest(){
         return this.request;
     }


     public static Request getFromJson(String json){
         Gson gson= new Gson();
         return gson.fromJson(json,Request.class);
     }

}
