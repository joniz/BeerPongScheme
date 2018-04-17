package com.example.jonathan.beerpongschemeandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 2017-10-31.
 */

public class tab_teams_fragment extends Fragment {

    private List list = new ArrayList<>();
    private View currView;
    private DatabaseAccess db = new DatabaseAccess();
    private RecyclerView recList;
    private tab_teams_retained_fragment retained_fragment;
    private static final String TAG_RETAINED_FRAGMENT = "teamsRetainedFragment";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){

        View view = inflater.inflate(R.layout.fragment_tab_teams, container, false);
        recList = view.findViewById(R.id.teamlistview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(AccessToken.getCurrentAccessToken() != null) {
            inflater.inflate(R.menu.actionbar, menu);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        FragmentManager fm = getFragmentManager();
        retained_fragment = (tab_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if(retained_fragment.getList() == null) {

            if (AccessToken.getCurrentAccessToken() != null) {
                db.getTeams(AccessToken.getCurrentAccessToken().getUserId(), new DatabaseAccess.getTeamData() {
                    @Override
                    public void onSuccess(List<Team> data) {

                        list = data;
                        retained_fragment.setList(list);
                        CustomTeamsRecyclerAdapter adapter = new CustomTeamsRecyclerAdapter(list);
                        recList.setAdapter(adapter);

                    }

                    @Override
                    public void onFailed(DatabaseError databaseError) {

                    }
                });
            }
        }else{
            CustomTeamsRecyclerAdapter adapter = new CustomTeamsRecyclerAdapter(retained_fragment.getList());
            recList.setAdapter(adapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!getActivity().isChangingConfigurations()){
            FragmentManager fm = getFragmentManager();
            if(retained_fragment != null) {
                retained_fragment = (tab_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
                retained_fragment.setList(null);
            }
        }
    }
}
