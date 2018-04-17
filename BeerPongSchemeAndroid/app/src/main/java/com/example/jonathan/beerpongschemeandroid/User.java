package com.example.jonathan.beerpongschemeandroid;

import java.util.List;

/**
 * Created by jonathan on 2017-11-15.
 */

public class User {
    private String name;
    private String id;
    private List<String> teams;

    public void User(){

    }
    public void User(String name, String id, List<String> teams){
        this.name = name;
        this.id = id;
        this.teams = teams;
    }
    public String getUsersName(){
        return name;
    }

    public String getId(){
        return id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setId(String id){
        this.id = id;
    }


}
