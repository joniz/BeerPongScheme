package com.example.jonathan.beerpongschemeandroid;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jonathan on 2017-12-04.
 */

public class PlayActivity extends AppCompatActivity {

    TextView teamlabel1;
    TextView teamlabel2;
    private ArrayList<Team> startingTeams;
    private ArrayList<Team> teams;
    private ArrayList<Team> winList = new ArrayList<>();
    private Integer index;
    private Integer round;
    private TextView roundLabel;
    private DatabaseAccess db = new DatabaseAccess();
    private TextView hostName;
    private ImageView hostImage;
    private Button teamButton1;
    private Button teamButton2;
    private static final String TAG_TEAMLIST = "teamList";
    private static final String TAG_SPACE = " ";
    private static final String TAG_SPACECOLON = ": ";
    private static final String TAG_INDEX = "index";
    private static final String TAG_ROUND = "round";
    private static final String TAG_TEAMS = "teams";
    private static final String TAG_WINLIST = "winList";
    private static final String TAG_STARTINGTEAMS = "startingTeams";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bracket_layout);
        getSupportActionBar().setTitle(getString(R.string.tournament));
        startingTeams = getIntent().getParcelableArrayListExtra(TAG_TEAMLIST);
        startTournament();
        getMatch();

    }

    private void startTournament(){
        teamButton1 = findViewById(R.id.team1);
        teamButton2 = findViewById(R.id.team2);

        teamlabel1 = findViewById(R.id.lablabel1);
        teamlabel2 = findViewById(R.id.laglabel2);
        roundLabel = findViewById(R.id.roundlabel);
        hostImage = findViewById(R.id.hostimage);
        hostName = findViewById(R.id.hostname);



        index = 0;
        round = 1;
        Bracket bracket = new Bracket(startingTeams);
        teams = bracket.matchMakeTeams();

        Picasso.with(this)
                .load(Profile.getCurrentProfile().getProfilePictureUri(100, 100))
                .resize(100, 100)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .centerInside()
                .into(hostImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) hostImage.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        hostImage.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        Picasso.with(getApplicationContext())
                        .load(Profile.getCurrentProfile().getProfilePictureUri(200, 200))
                        .resize(200,200)
                        .centerInside()
                        .into(hostImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) hostImage.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                hostImage.setImageDrawable(imageDrawable);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
        hostName.setText(getString(R.string.administrator_string) + TAG_SPACECOLON + Profile.getCurrentProfile().getFirstName());
    }
    public void teamselected(View view) {
        if (view.getId() == R.id.team1) {
            winList.add(teams.get(index));
            teams.get(index).incrementWonGames();
            teams.get(index+1).incrementLostGames();

        }else{
            winList.add(teams.get(index+1));
            teams.get(index+1).incrementWonGames();
            teams.get(index).incrementLostGames();

            }
        teams.get(index+1).incrementTotNumberOfGames();
        teams.get(index).incrementTotNumberOfGames();

        index+=2;
        getMatch();
    }

    private void getMatch(){
        ArrayList<Team> temp = new ArrayList<>();
        for(int i = index; i < teams.size()-1; i+=2){
            temp.add(teams.get(index));
            temp.add(teams.get(index+1));
            if(checkTeams(temp).size() != 2){
                winList.add(checkTeams(temp).get(0));
                index+=2;

            }else{
                break;
            }
            temp.clear();
        }

        if(index > teams.size()-1){
            teams.clear();
            teams.addAll(winList);
            winList.clear();
            index = 0;
            round += 1;
        }
        if(teams.size() == 1){
            teamButton1.setEnabled(false);
            teamButton2.setEnabled(false);
            roundLabel.setText(teams.get(0).getName() + TAG_SPACE + getString(R.string.winner));
            updateStatistics(startingTeams);


        }
        updateText();

    }

    private ArrayList<Team> checkTeams(ArrayList<Team> teams) {
        ArrayList<Team> tempList = new ArrayList<>();

        if (teams.get(0).getName() == null && teams.get(1).getName() == null) {
            tempList.add(teams.get(0));
            return tempList;
        } else if (teams.get(0).getName() != null && teams.get(1).getName() == null) {
            tempList.add(teams.get(0));
            return tempList;
        } else if (teams.get(0).getName() == null && teams.get(1).getName() != null) {
            tempList.add(teams.get(1));
            return tempList;
        } else {
            return teams;

        }

    }
    private void updateStatistics(ArrayList<Team> teams){
        db.updateStatistics(teams, Profile.getCurrentProfile().getId());
    }
    @Override
    public void onBackPressed() {
        if (teams.size() != 1) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exiting_tournament)
                    .setMessage(R.string.tournament_warning)
                    .setCancelable(false)
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    finish();
                                }
                            }
                    ).setNegativeButton(
                    android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }
            ).show();
        }else{
            finish();
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt(TAG_INDEX);
        round = savedInstanceState.getInt(TAG_ROUND);
        teams = savedInstanceState.getParcelableArrayList(TAG_TEAMS);
        winList = savedInstanceState.getParcelableArrayList(TAG_WINLIST);
        startingTeams = savedInstanceState.getParcelableArrayList(TAG_STARTINGTEAMS);
        if(teams.size() == 1){
            teamButton1.setEnabled(false);
            teamButton2.setEnabled(false);
            teamlabel1.setVisibility(View.INVISIBLE);
            teamlabel2.setVisibility(View.INVISIBLE);
            roundLabel.setText(teams.get(0).getName() + TAG_SPACE + getString(R.string.winner));

        }else {
            updateText();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        //savedInstanceState.putInt("count", count);
        savedInstanceState.putInt(TAG_INDEX, index);
        savedInstanceState.putInt(TAG_ROUND, round);
        savedInstanceState.putParcelableArrayList(TAG_TEAMS, teams);
        savedInstanceState.putParcelableArrayList(TAG_WINLIST, winList);
        savedInstanceState.putParcelableArrayList(TAG_STARTINGTEAMS, startingTeams);

    }
    private void updateText(){
        if(teams.size() != 1) {
            teamlabel1.setText(teams.get(index).getName());
            teamlabel2.setText(teams.get(index + 1).getName());
            roundLabel.setText(getString(R.string.round) + TAG_SPACECOLON + String.valueOf(round));
        }
    }





}
