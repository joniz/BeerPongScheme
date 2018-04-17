package com.example.jonathan.beerpongschemeandroid;

import android.provider.ContactsContract;
import android.util.Log;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jonathan on 2017-11-15.
 */

public class DatabaseAccess {
    private DatabaseReference mDatabase;
    private boolean userExist;
    private FirebaseAuth mAuth;
    private static final String TAG_USERS = "Users";
    private static final String TAG_NAME = "Name";
    private static final String TAG_USERSTEAMS = "usersTeams/";
    private static final String TAG_UUID = "uuid";
    private static final String TAG_SMALLNAME = "name";
    private static final String TAG_MEMBERS = "members";
    private static final String TAG_TOTNUMBEROFGAMES = "totNumberOfGames";
    private static final String TAG_LOSTGAMES = "lostGames";
    private static final String TAG_WONGAMES = "wonGames";
    private static final String TAG_REGULARUSERSTEAMS = "usersTeams";
    private static final String TAG_DASH = "/";



    public void storeTeam(String userId,Team team) {
        mDatabase = FirebaseDatabase.getInstance().getReference(TAG_USERSTEAMS + userId + TAG_DASH + team.getUUID());
        mDatabase.setValue(team);
    }
    public void updateStatistics(ArrayList<Team> teams, String userId){
        for(int i = 0; i < teams.size(); i++) {
            mDatabase = FirebaseDatabase.getInstance().getReference(TAG_USERSTEAMS + userId + TAG_DASH + teams.get(i).getUUID());
            mDatabase.setValue(teams.get(i));
        }
    }

    public void storeUser(User user){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(TAG_USERS).child(String.valueOf(user.getId())).child(TAG_NAME).setValue(user.getUsersName());

    }
    public void checkIfUserExist(final String id, final databaseCheckUserComplete returnCallback){
        mDatabase = FirebaseDatabase.getInstance().getReference(TAG_USERS);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    userExist = true;
                }else{
                    userExist = false;

                }
                returnCallback.onActionDone(userExist);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    public void getTeams(final String userId, final getTeamData callback){
        mDatabase = FirebaseDatabase.getInstance().getReference(TAG_USERSTEAMS + userId);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Team> teams = new ArrayList<Team>();
                if(dataSnapshot.exists()){
                    for (DataSnapshot shot : dataSnapshot.getChildren()) {

                        Team team = new Team();
                        team.setId(shot.child(TAG_UUID).getValue().toString());
                        team.setName(shot.child(TAG_SMALLNAME).getValue().toString());
                        for(DataSnapshot member : shot.child(TAG_MEMBERS).getChildren()){
                            team.setMembers(member.getValue().toString());
                        }
                        team.setTotNumberOfGames(Integer.valueOf(shot.child(TAG_TOTNUMBEROFGAMES).getValue().toString()));
                        team.setLostGames(Integer.valueOf(shot.child(TAG_LOSTGAMES).getValue().toString()));
                        team.setWonGames(Integer.valueOf(shot.child(TAG_WONGAMES).getValue().toString()));


                        teams.add(team);
                    }
                }else{
                    return;
                }
                callback.onSuccess(teams);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void deleteTeam(String team, String userId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(TAG_REGULARUSERSTEAMS).child(userId).child(team).removeValue();
    }

    public void getNumOfUserTeams(String userId, final getNumofTeams callback){
        mDatabase = FirebaseDatabase.getInstance().getReference(TAG_USERSTEAMS  + userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer count = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    count++;
                }
                callback.onSucces(count);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface getNumofTeams
    {
        void onSucces(Integer count);
    }



    public interface databaseCheckUserComplete
    {
        void onActionDone(boolean succeeded);
    }
    public interface getTeamData {
        void onSuccess(List<Team> data);
        void onFailed(DatabaseError databaseError);
    }
    public interface getTeams
    {
        void onSuccess(List<String> data);
        void onFailed(DatabaseError databaseError);
    }



}
