package com.example.jonathan.beerpongschemeandroid;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    tab_home_fragment homeFragment = null;
    tab_teams_fragment teamFragment = null;
    tab_play_fragment playFragment = null;
    tab_account_fragment accountFragment = null;
    private static final String TAG_RETAINEDTEAMSFRAGMENT = "teamsRetainedFragment";
    private static final String TAG_HOMEFRAGMENT = "homeFragment";
    private static final String TAG_TEAMFRAGMENT = "teamFragment";
    private static final String TAG_ACCOUNTFRAGMENT = "accountFragment";
    private static final String TAG_COUNT = "count";
    private tab_teams_retained_fragment teamsRetainedFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadHomeFragment();
                    return true;
                case R.id.navigation_teams:
                    loadTeamFragment();
                    return true;
                case R.id.navigation_account:

                    loadAccountFragment();
                    return true;
            }
            return false;
        }


    };

    private void loadHomeFragment(){

        getSupportActionBar().setTitle(R.string.title_home);
        if(getFragmentManager().findFragmentByTag(TAG_HOMEFRAGMENT) == null) {
            homeFragment = new tab_home_fragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, homeFragment, TAG_HOMEFRAGMENT);

        ft.commit();


    }
    private void loadTeamFragment(){
        getSupportActionBar().setTitle(R.string.teams);
        teamFragment = (tab_teams_fragment) getSupportFragmentManager().findFragmentByTag(TAG_TEAMFRAGMENT);
        if(teamFragment == null) {
            teamFragment = new tab_teams_fragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, teamFragment, TAG_TEAMFRAGMENT);
        ft.commit();
    }
    private void loadAccountFragment(){
        getSupportActionBar().setTitle(R.string.account);
        accountFragment = (tab_account_fragment) getSupportFragmentManager().findFragmentByTag(TAG_ACCOUNTFRAGMENT);
        if(accountFragment == null){
            accountFragment = new tab_account_fragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, accountFragment, TAG_ACCOUNTFRAGMENT);
        ft.commit();
    }
    public void startPlayActivity(View v){
        Intent intent = new Intent(this, selectTeamActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        teamsRetainedFragment = (tab_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINEDTEAMSFRAGMENT);
        // create the fragment and data the first time
        if (teamsRetainedFragment == null) {
            // add the fragment
            teamsRetainedFragment = new tab_teams_retained_fragment();
            fm.beginTransaction().add(teamsRetainedFragment, TAG_RETAINEDTEAMSFRAGMENT).commit();
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null){
            loadHomeFragment();
        }
    }
    public void addTeamActivity(MenuItem item){
        Intent intent = new Intent(this, AddTeamActivity.class);
        startActivity(intent);
    }

}
