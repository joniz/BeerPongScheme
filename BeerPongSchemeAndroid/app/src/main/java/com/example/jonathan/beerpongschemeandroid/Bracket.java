package com.example.jonathan.beerpongschemeandroid;

import android.os.Parcel;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonathan on 2017-11-20.
 */

public class Bracket {
    private ArrayList<Team> allTeamsStartingList = new ArrayList<>();
    private ArrayList<ArrayList<Team>> bracketList = new ArrayList<>();

    public Bracket(){}
    public Bracket(ArrayList<Team> teams){
        this.allTeamsStartingList = teams;
    }
    public ArrayList<Team> matchMakeTeams(){

        ArrayList<Team> roundList = new ArrayList<>();


        for(int i = 0; i < allTeamsStartingList.size(); i++) {
            roundList.add(allTeamsStartingList.get(i));
        }
        Collections.shuffle(roundList);
            int size = roundList.size();
        int j = 0;
        for(int i = 0; i < numOfneccesaryTeams()-size ; i++){

            Team team = new Team(Parcel.obtain());
            roundList.add(j, team);
            j+=2;


        }

        bracketList.add(roundList);
        return roundList;
    }
    private int getStartTeams(){
        return this.allTeamsStartingList.size();
    }
    private Double numOfneccesaryTeams(){
        return Math.pow(2, Math.ceil(Math.log(getStartTeams()) / Math.log(2)));
    }









}
