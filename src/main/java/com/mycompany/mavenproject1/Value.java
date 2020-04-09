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
public class Value implements Printable{
    String id;
    String value;
    
    public Value(String n,String v){
    id=n;
    value=v;
    }
    @Override
    public String print(String user,int dept){
    return "{\"Id\":\""+id+"\",\"Value\":\""+value+"\"}";
    }

    @Override
    public String printNode(String path,String user, int depth) {
       String msg="";
       if(path.endsWith(id)){
       msg += print(user,depth);
       }
       return msg;
    }
}
