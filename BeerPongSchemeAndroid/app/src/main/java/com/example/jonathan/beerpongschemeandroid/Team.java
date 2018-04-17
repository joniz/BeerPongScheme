package com.example.jonathan.beerpongschemeandroid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jonathan on 2017-11-15.
 */

public class Team implements Parcelable {

    private String name;
    private List<String> members = new ArrayList<>();
    private int totNumberOfGames;
    private int lostGames;
    private int wonGames;
    private String id;


    public Team(){
        this.totNumberOfGames = 0;
        this.lostGames = 0;
        this.wonGames = 0;
        this.id = UUID.randomUUID().toString();

    }

    public Team(Parcel in) {
        this.name = in.readString();
        this.members = in.createStringArrayList();
        this.totNumberOfGames = in.readInt();
        this.lostGames = in.readInt();
        this.wonGames = in.readInt();
        this.id = in.readString();
    }
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getUUID(){
        return id;
    }

    public  Team(String teamName){
        this.name = teamName;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setWonGames(int wonGames ){
        this.wonGames = wonGames;
    }
    public int getWonGames(){
        return wonGames;
    }
    public int getTotNumberOfGames(){
        return totNumberOfGames;
    }
    public void setTotNumberOfGames(int totNumberOfGames){
        this.totNumberOfGames = totNumberOfGames;
    }
    public int getLostGames(){
        return lostGames;
    }
    public void setLostGames(int lostGames){
        this.lostGames = lostGames;
    }
    public List<String> getMembers(){
        return members;
    }
    public void setMembers(String member){
        this.members.add(member);
    }
    public void setId(String id){
        this.id = id;
    }
    public void incrementWonGames(){
        this.wonGames += 1;
    }
    public void incrementLostGames(){
        this.lostGames += 1;
    }
    public void incrementTotNumberOfGames(){
        this.totNumberOfGames += 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeStringList(members);
        parcel.writeInt(totNumberOfGames);
        parcel.writeInt(lostGames);
        parcel.writeInt(wonGames);
        parcel.writeString(id);
    }
}
