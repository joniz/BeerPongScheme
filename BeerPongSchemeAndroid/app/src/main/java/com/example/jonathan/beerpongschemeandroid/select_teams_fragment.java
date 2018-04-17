package com.example.jonathan.beerpongschemeandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 2017-11-27.
 */

public class select_teams_fragment extends Fragment {

    private List list;
    private DatabaseAccess db = new DatabaseAccess();
    private RecyclerView recList;
    private FloatingActionButton floatAction;
    CustomChooseTeamsRecyclerAdapter adapter;
    List<Team> selected;
    private static final String TAG_RETAINED_FRAGMENT = "selectTeamRetainedFragment";
    private select_teams_retained_fragment retained_fragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){

        View view = inflater.inflate(R.layout.fragment_choose_teams, container, false);
        recList = view.findViewById(R.id.chooseteamlist);
        floatAction = view.findViewById(R.id.floatbutton);

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        getActionBar().setTitle(R.string.select_teams);

        recList.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && floatAction.isShown())
                    floatAction.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    floatAction.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getFragmentManager();
        retained_fragment = (select_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (retained_fragment.getList() == null) {
            // add the fragment
            retained_fragment = new select_teams_retained_fragment();
            if (AccessToken.getCurrentAccessToken() != null) {

                db.getTeams(AccessToken.getCurrentAccessToken().getUserId(), new DatabaseAccess.getTeamData() {
                    @Override
                    public void onSuccess(List<Team> data) {
                        list = data;
                        retained_fragment.setList(data);
                        CustomChooseTeamsRecyclerAdapter adapter = new CustomChooseTeamsRecyclerAdapter(retained_fragment.getList());
                        recList.setAdapter(adapter);
                    }
                    @Override
                    public void onFailed(DatabaseError databaseError) {

                    }
                });
            }
            fm.beginTransaction().add(retained_fragment, TAG_RETAINED_FRAGMENT).commit();
        }else {
            retained_fragment = (select_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
            CustomChooseTeamsRecyclerAdapter adapter = new CustomChooseTeamsRecyclerAdapter(retained_fragment.getList());
            recList.setAdapter(adapter);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    private ActionBar getActionBar() {
        return ((selectTeamActivity) getActivity()).getSupportActionBar();
    }
    public List<Team> getSelectedTeams(){
        adapter = new CustomChooseTeamsRecyclerAdapter();
        selected = adapter.getSelectedTeams();
        return selected;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        selectTeamActivity.switchList.clear();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStop() {
        super.onStop();
        if(!getActivity().isChangingConfigurations()){
            FragmentManager fm = getFragmentManager();
            retained_fragment = (select_teams_retained_fragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
            retained_fragment.setList(null);
        }
    }

}
