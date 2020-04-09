/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author krzysztof.staporek
 */
public class DB {
    String name;
    HashMap<String,User> users;
    
    public DB(String n,String p){
    name=n;
    users = new HashMap<>();
    User su = new User("root",p);
    su.admin=true;
    users.put("root",su );
    }
    
    public String print(){
    String msg="";
      msg +="{\"Name\":\""+name+"\",";
    msg+="\"Users\":[";
    
    msg+=enumPrint(users);
    
      msg+="]}";
    return msg;
    }
    
     public String enumPrint(HashMap<String,User> value){
     String msg="";
        Set set = value.keySet();
    Iterator it = set.iterator();
    while(it.hasNext()){
        String next = (String) it.next();
        msg+=value.get(next).print();
    }
    
    return msg;
    }
}
