/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author krzysztof.staporek
 */
public class Node implements Printable{
    String id;
    HashMap<String,Printable> nodes;
    HashMap<String,Printable> values;
    HashMap<String,Printable> premisions;
    Node parent;
    
    public Node(String n){
    
    id=n;
    nodes = new HashMap<>();
    values = new HashMap<>();
    premisions = new HashMap<>();
    
    
    }
    @Override
    public String print(String user,int dept){
        dept--;
    String msg="";
    System.out.println("Id "+id+" "+dept);
    boolean acces = user.equals("root");
    if(premisions.containsKey(user))
        if(((Premmision)premisions.get(user)).read)
            acces=true;
     if(premisions.containsKey("*"))
    if(((Premmision)premisions.get("*")).read)
        acces=true;
  
    if(acces){
    msg +="{\"Id\":\""+id+"\",";
    msg+="\"Nodes\":[";
    if(dept!=-2){
    msg+=enumPrint(nodes,user,dept);
    }
    msg+="],\"Values\":[";
    if(dept!=-2){
        msg+= enumPrint(values,user,dept);
    }
    msg+="]";
    
    if(user.equals("root")){
    msg+=",\"Premmision\":[";
    msg+=enumPrint(premisions,user,dept);
    
    msg+="]";
    }
    msg+="}";
    }
        return msg;
    }
    
    
    public String enumPrint(HashMap<String,Printable> value,String user,int dept){
     String msg="";
        Set set = value.keySet();
    Iterator it = set.iterator();
    while(it.hasNext()){
        String next = (String) it.next();
        System.out.println(next);
        msg+=value.get(next).print(user,dept);
        if(it.hasNext()){
        msg+=",";
        }
    }
    
    return msg;
    }
    
    
    public Node findNode(String path,String user){
     String[] slice = path.split("/");
     System.out.println("Find node slice "+path+" "+slice.length);
   if(slice.length>1){
   String newPath = "";
   for(int i=1;i<slice.length;i++){
   newPath +=slice[i]+"/";
   }
   System.out.println("New Path:"+newPath);
   System.out.println("Slice table:");
   for(int i=0;i<slice.length;i++){
       System.out.println("Slice["+i+"]: "+slice[i]);
   }
  boolean acces = user.equals("root");
    if(premisions.containsKey(user))
        if(((Premmision)premisions.get(user)).execute)
            acces=true;
    if(premisions.containsKey("*"))
    if(((Premmision)premisions.get("*")).execute)
        acces=true;
    
    if(acces){ 
        if(!nodes.containsKey(slice[1]) && !slice[1].contains(".var")){
       Node nod = new Node(slice[1]);
       nod.parent = this;
       if(!user.equals("root"))
       nod.premisions.put(user, new Premmision(user,true,true,true));
        nodes.put(slice[1], nod);
       
   }}
   if(nodes.containsKey(slice[1])){
       System.out.println(((Node)nodes.get(slice[1])).id);
   return ((Node)nodes.get(slice[1])).findNode(newPath,user);
   }else{
      if(id.equals(newPath.replace("/", ""))){
       System.out.println(id);
   return this;
   }
   }
   
   
   }else{
   if(id.equals(slice[0])){
       System.out.println(id);
   return this;
   }
   }
   return this;
    }
    
    
    public void delete(String path,String user){
    
    Node nod = findNode(path,user);
        System.out.println("Delete:"+nod.id);
    
    if(nod!=null){
      boolean acces = user.equals("root");
    if(nod.premisions.containsKey(user))
        if(((Premmision)nod.premisions.get(user)).write)
            acces=true;
     if(nod.premisions.containsKey("*"))
    if(((Premmision)nod.premisions.get("*")).write)
        acces=true;
  
    if(acces){
        System.out.println(nod.id+" "+path);
        if(path.contains(".var")){
            String[] slice = path.split("/");
            String name = slice[slice.length-1];
            System.out.println(nod.id+"->"+name);
        nod.values.remove(name);
        
        }
        else{
            if(nod.parent!=null){
    nod.parent.nodes.remove(nod.id);
            }else{
            nod.nodes.clear();
            nod.values.clear();
            }
    }}
    }
    
    }
    
    public void trim(String path,String user){
     Node nod = findNode(path,user);
     System.out.println("Trim:"+nod.id);
    if(nod!=null){
      boolean acces = user.equals("root");
    if(nod.premisions.containsKey(user))
        if(((Premmision)nod.premisions.get(user)).write)
            acces=true;
     if(nod.premisions.containsKey("*"))
    if(((Premmision)nod.premisions.get("*")).write)
        acces=true;
  
    if(acces){
         System.out.println(nod.id+" "+path);
        if(path.contains(".var")){
            String[] slice = path.split("/");
            String name = slice[slice.length-1];
            System.out.println(nod.id+"->"+name);
            if(nod.values.containsKey(name)){
            nod.values.replace(name, new Value(name.replace(".var", ""),""));
            }
        }else{
        nod.values.clear();
        }
        
    }}
    }
    
  
    
    public boolean putValue(String path,String user,Map<String,String> params){
    Node nod = findNode(path,user);
    
    if(nod!=null){
     boolean acces = user.equals("root");
    if(nod.premisions.containsKey(user))
        if(((Premmision)nod.premisions.get(user)).write)
            acces=true;
     if(nod.premisions.containsKey("*"))
    if(((Premmision)nod.premisions.get("*")).write)
        acces=true;
  
    if(acces){
    Iterator<String> it = params.keySet().iterator();
    while(it.hasNext()){
    String key = it.next();
    if(nod.values.containsKey(key+".var")){
        System.out.println("Replase "+key+".var "+params.get(key));
    nod.values.replace(key+".var", new Value(key,params.get(key)));
    return true;
    }else{
        
        System.out.println("Put "+key+".var "+params.get(key));
    nod.values.put(key+".var", new Value(key,params.get(key)));
    return true;
    }
    }
            }
        
    }
    return false;
    }

    @Override
    public String printNode(String path,String user, int depth) {
   String msg="";
   String[] slice = path.split("/");
       System.out.println("Print node slice "+path+" "+slice.length +" "+slice[0]);

   if(slice.length>1){
   String newPath = "";
   for(int i=1;i<slice.length;i++){
   newPath +=slice[i]+"/";
   }
   if(nodes.containsKey(slice[1]))
   return ((Node)nodes.get(slice[1])).printNode(newPath,user,depth);
   if(values.containsKey(slice[1]))
       return values.get(slice[1]).print(user,depth);
   }else{
   if(id.equals(slice[0])){
   return print(user,depth);
   }else
   if(nodes.containsKey(slice[0])){
   return nodes.get(slice[0]).print(user,depth);
   }else{
   if(values.containsKey(slice[0])){
   return values.get(slice[0]).print(user,depth);
   }
   }
   }
   return msg;
    
    }
}
