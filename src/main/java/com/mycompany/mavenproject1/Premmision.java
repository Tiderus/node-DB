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
public class Premmision implements Printable{
    String User;
    boolean read;
    boolean write;
    boolean execute;
    
    public Premmision(String user){
    User = user;
    read = true;
    write = true;
    execute = true;
    }
    
    public Premmision(String user,boolean r,boolean w,boolean e){
    User = user;
    read = r;
    write = w;
    execute = e;
    }

    @Override
    public String print(String user,int dept) {
     return "{\"User\":\""+User+"\",\"Read\":"+read+",\"Write\":"+write+",\"Execute\":"+execute+"}";
     }

    @Override
    public String printNode(String path,String user, int depth) {
           String msg="";
       if(path.endsWith(User)){
       msg += print(user,depth);
       }
       return msg;
    }
}
