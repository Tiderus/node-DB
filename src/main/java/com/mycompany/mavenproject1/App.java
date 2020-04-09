/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author krzysztof.staporek
 */
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 import java.io.IOException;
import java.util.List;
import java.util.Map;
    
    import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

    // NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
    //       instead of the above import use the following:
	// import org.nanohttpd.NanoHTTPD;
    
    public class App extends NanoHTTPD {
    public static int port = 6969;
    
    private Node root = new Node("root");
    private Node db = new Node("db");
    private Node users = new Node("users");
        public App(int port) throws IOException {
            super(port);
             LoadDataBase();
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            System.out.println("\nRunning! Point your browsers to http://localhost:6969/ \n");
        }
    
        public static void main(String[] args) {
            try {

                port = Integer.parseInt(System.getProperty("http.port","6969"));
                new App(port);
            } catch (IOException ioe) {
                System.err.println("Couldn't start server:\n" + ioe);
            }
        }
    
        
        @Override
        public Response serve(IHTTPSession session) {
              String msg = "";

            Method method = session.getMethod();
            String temp = session.getQueryParameterString();
            Map<String, String> params = new HashMap<String,String>();
            Map<String,String> post_params = new HashMap<String,String>();
            try {
            session.parseBody(post_params);
            post_params = session.getParms();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ResponseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
            if(temp!=null) {
                String[] netemp = temp.split("&");
                for (int i = 0; i < netemp.length; i++) {
                    String[] n = netemp[i].split("=");
                    params.put(n[0], n[1]);
                }
            }

            String uri = session.getUri();
            System.out.println(method+" "+uri);
            System.out.println(params+" "+post_params);
            System.out.println(session.getParameters());
            
            Iterator it = session.getHeaders().keySet().iterator();
            while(it.hasNext()){
            String s = (String)it.next();
            System.out.println(s+":"+session.getHeaders().get(s));
            }
            String dbname="";
            Node currentdb = null;
            if(session.getHeaders().containsKey("db"))
            dbname = session.getHeaders().get("db");
            
            System.out.println(dbname);
             if(db.nodes.containsKey(dbname)){
            currentdb=(Node)db.nodes.get(dbname);
            }else{
                    Response rep = newFixedLengthResponse(Status.UNAUTHORIZED,"text/html","No db to connect");
           rep.addHeader("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT");
            rep.addHeader("Access-Control-Allow-Origin",  "*");
            rep.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Pass, User,Db");
            return rep;
             }
            uri = dbname+uri;
            System.out.println("Node path "+uri);
            String user ="";
            if(session.getHeaders().containsKey("user"))
            user = session.getHeaders().get("user");
            
            if(!users.nodes.containsKey(user)){
                    Response rep = newFixedLengthResponse(Status.UNAUTHORIZED,"text/html","Invaled user");
           rep.addHeader("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT");
            rep.addHeader("Access-Control-Allow-Origin",  "*");
            rep.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Pass, User,Db");
            return rep;
            
            }
            
            System.out.println(user);
            if(method.equals(Method.GET)){
                int dept = 0;
                if(params.containsKey("depth")){
                dept = Integer.parseInt(params.get("depth"));
                }
               
            msg = currentdb.printNode(uri,user, dept);
            }
            
            if(method.equals(Method.DELETE)){
            
             if(params.containsKey("action")){
                if(params.get("action").equalsIgnoreCase("drop")){
                currentdb.delete(uri, user);
                msg = "Drop "+uri;
                }
                if(params.get("action").equalsIgnoreCase("trim")){
                currentdb.trim(uri, user);
                msg = "Trim "+uri;
                }
                }
                SaveDataBase();
            }
            
            if(method.equals(Method.POST)){
            if(currentdb.putValue(uri,user, post_params)){
            SaveDataBase();
            msg = "Post Succesed";
            }else{
            msg = "Post false";
            }
            
            }
            System.out.println(msg);
         if(msg.isEmpty()){
            Response rep = newFixedLengthResponse(Status.BAD_REQUEST,"text/html","Bad request. Path not found");
           rep.addHeader("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT");
            rep.addHeader("Access-Control-Allow-Origin",  "*");
            rep.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Pass, User,Db");
            return rep;
         
         }
         System.out.println(msg);
            Response rep = newFixedLengthResponse(Status.OK,"text/html",msg);
           rep.addHeader("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT");
            rep.addHeader("Access-Control-Allow-Origin",  "*");
            rep.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Pass, User,Db");
            return rep;
        }

public void SaveDataBase(){
    
File file = new File(".","DB.json");
try{
if(!file.exists()){
   
        file.createNewFile();
    
}

FileOutputStream out = new FileOutputStream(file);

out.write(root.printNode("root/","root", -2).getBytes());
out.flush();
out.close();
} catch (IOException ex) {
        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public void LoadDataBase(){
File file = new File(".","DB.json");
try{
if(!file.exists()){
   setupDB();
        file.createNewFile();
    FileOutputStream out = new FileOutputStream(file);

out.write(root.printNode("root","root", -2).getBytes());
out.flush();
out.close();


}

BufferedReader buf = new BufferedReader(new FileReader(file.getAbsolutePath()));
String s="";
String text="";
while((s=buf.readLine())!=null){
text+=s;
}
buf.close();
JsonElement parser = new JsonParser().parse(text);
parseStep(parser.getAsJsonObject(),root);

db=root.findNode("root/db", "root");
users = root.findNode("root/users", "root");
} catch (IOException ex) {
        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
    }

}

public void setupDB(){
       root.premisions.put("*", new Premmision("*",false,false,true));
       Node newdb = new Node("apk");
       newdb.premisions.put("*", new Premmision("*",false,false,true));
       db.nodes.put("apk", newdb);
         db.premisions.put("*", new Premmision("*",false,false,true));
        root.nodes.put("db", db);
        root.nodes.put("users", users);
}

public Node parseStep(JsonObject root,Node node){

    node.id = root.get("Id").getAsString();
    
    JsonArray nodes = root.getAsJsonArray("Nodes");
    
    for(int i=0;i<nodes.size();i++){
        JsonObject obj = nodes.get(i).getAsJsonObject();
    node.nodes.put(obj.get("Id").getAsString(),parseStep(obj,new Node(obj.get("Id").getAsString())) );
    }
    JsonArray values = root.getAsJsonArray("Values");
    for(int i=0;i<values.size();i++){
    node.values.put(values.get(i).getAsJsonObject().get("Id").getAsString()+".var", new Value(values.get(i).getAsJsonObject().get("Id").getAsString(),values.get(i).getAsJsonObject().get("Value").getAsString()));
    }
    JsonArray prems = root.getAsJsonArray("Premmision");
    if(prems!=null)
     for(int i=0;i<prems.size();i++){
         JsonObject obj = prems.get(i).getAsJsonObject();
    node.premisions.put(obj.get("User").getAsString(), new Premmision(obj.get("User").getAsString(),obj.get("Read").getAsBoolean(),obj.get("Write").getAsBoolean(),obj.get("Execute").getAsBoolean()));
    }
    return node;
}
    
    }