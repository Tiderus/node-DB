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
public class User {
    String name;
    String password;
    boolean admin;
    
    public User(String n,String p){
    name=n;
    password=p;
    }
    
    public String print(){
      return "{\"User\":\""+name+"\",\"Password\":\""+password+"\",\"Admin\":"+admin+"}";
  
    }
}
