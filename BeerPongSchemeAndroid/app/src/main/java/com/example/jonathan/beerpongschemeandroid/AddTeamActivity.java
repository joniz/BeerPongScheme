package com.example.jonathan.beerpongschemeandroid;

import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddTeamActivity extends AppCompatActivity {
    private DatabaseAccess db = new DatabaseAccess();
    private static final String TAG_RETAINED_FRAGMENT = "selectTeamRetainedFragment";
    private static final String TAG_TEAM_NAME = "teamName";
    private static final String TAG_MEMBER1 = "member1";
    private static final String TAG_MEMBER2 = "member2";

    EditText editText;
    EditText member1;
    EditText member2;
    String enteredTitle;
    String name1;
    String name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

    }
    public void addTeam(View view){
        editText = findViewById(R.id.teamname);
        member1 = findViewById(R.id.teammember1);
        member2 = findViewById(R.id.teammember2);
        enteredTitle = editText.getText().toString();
        name1 = member1.getText().toString();
        name2 = member2.getText().toString();

        if (checkInput(name1, name2,enteredTitle)) {
            Team team = new Team();
            team.setName(enteredTitle);
            team.setMembers(name1);
            team.setMembers(name2);
            db.storeTeam(AccessToken.getCurrentAccessToken().getUserId(), team);
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_message_add_team)
                    .setMessage(R.string.error_add_teams)
                    .setCancelable(false)
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            }
                    ).show();
            member1.getText().clear();
            member2.getText().clear();
        }


    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        enteredTitle = savedInstanceState.getString(TAG_TEAM_NAME);
        name1 = savedInstanceState.getString(TAG_MEMBER1);
        name2 = savedInstanceState.getString(TAG_MEMBER2);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putString(TAG_TEAM_NAME, enteredTitle);
        savedInstanceState.putString(TAG_MEMBER1, name1);
        savedInstanceState.putString(TAG_MEMBER2, name2);
        // etc.
    }
    private Boolean checkInput(String name1, String name2, String title){
        if(!name1.isEmpty() && !name2.isEmpty() && !title.isEmpty()){
            return true;
        }else{
            return false;
        }


    }






}

