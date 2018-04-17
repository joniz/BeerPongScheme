package com.example.jonathan.beerpongschemeandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 2017-12-14.
 */

public class tab_teams_retained_fragment extends Fragment {

    private List<Team> teamList;


    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public void setList(List<Team> teams) {
        teamList = new ArrayList<>();
        teamList = teams;
    }
    public List<Team> getList(){
        return teamList;
    }


}
