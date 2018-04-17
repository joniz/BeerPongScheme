package com.example.jonathan.beerpongschemeandroid;



import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class selectTeamActivity extends AppCompatActivity {

    private select_teams_fragment selectFragment;
    public static ArrayList<Team> switchList = new ArrayList<>();
    private DatabaseAccess db = new DatabaseAccess();
    private static final String TAG_RETAINED_FRAGMENT = "selectTeamRetainedFragment";
    private static final String TAG_SELECTFRAGMENT = "selectFragment";
    private static final String TAG_TEAMLIST = "teamList";
    private select_teams_retained_fragment selectTeamsRetainedFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        FragmentManager fm = getSupportFragmentManager();
        selectTeamsRetainedFragment = (select_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (selectTeamsRetainedFragment == null) {
            // add the fragment
            selectTeamsRetainedFragment = new select_teams_retained_fragment();
            fm.beginTransaction().add(selectTeamsRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
        }
        loadSelectTeamFragment();
    }
    private void loadSelectTeamFragment(){
        selectFragment = (select_teams_fragment) getSupportFragmentManager().findFragmentByTag(TAG_SELECTFRAGMENT);
        if(selectFragment ==  null){
            selectFragment = new select_teams_fragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.select_team, selectFragment, TAG_SELECTFRAGMENT);
        ft.commit();
    }

    public void teamsSelected(View view){
        select_teams_fragment tab = new select_teams_fragment();
        if(tab.getSelectedTeams().size() > 1) {
            ArrayList<Team> newList = new ArrayList<>();

            Intent intent = new Intent(this, PlayActivity.class);
            newList.addAll(tab.getSelectedTeams());
            intent.putParcelableArrayListExtra(TAG_TEAMLIST, newList);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error)
                    .setMessage(R.string.select_two_teams)
                    .setCancelable(false)
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            }
                    ).show();
        }
    }
    @Override
    public void onPause() {
        // perform other onPause related actions
        super.onPause();
        // this means that this activity will not be recreated now, user is leaving it
        // or the activity is otherwise finishing
        if(isFinishing()) {
            FragmentManager fm = getSupportFragmentManager();
            // we will not need this fragment anymore, this may also be a good place to signal
            // to the retained fragment object to perform its own cleanup.
            fm.beginTransaction().remove(selectTeamsRetainedFragment).commit();
        }
    }

}













