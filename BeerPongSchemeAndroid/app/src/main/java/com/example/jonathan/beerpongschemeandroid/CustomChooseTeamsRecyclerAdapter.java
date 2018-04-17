package com.example.jonathan.beerpongschemeandroid;

/**
 * Created by jonathan on 2017-11-08.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.database.DatabaseError;

public class CustomChooseTeamsRecyclerAdapter extends RecyclerView.Adapter<CustomChooseTeamsRecyclerAdapter.ViewHolder> {

    public static List<Team> values = new ArrayList<>();
    public static List<Boolean> areTeamsSelected =  new ArrayList<>();
    private static final String TAG_SEPARATOR = " | ";

    private DatabaseAccess db = new DatabaseAccess();
    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView txtFooter;
        public View layout;
        private TextView members;
        private SwitchCompat switchbutton;






        public ViewHolder(View v) {
            super(v);
            layout = v;
            switchbutton = v.findViewById(R.id.switchteam);
            txtFooter = v.findViewById(R.id.txtName);
            members = v.findViewById(R.id.members_choose);


            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            switchbutton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    areTeamsSelected.set(getAdapterPosition(), !areTeamsSelected.get(getAdapterPosition()));
                }
            });

        }
    }

    public List<Team> getSelectedTeams(){
        List<Team> selectedTeams = new ArrayList();
        for(int i = 0; i < areTeamsSelected.size(); i++){
            if(areTeamsSelected.get(i)){
                selectedTeams.add(values.get(i));
            }
        }
        return selectedTeams;
    }

    public void add(int position) {
        notifyItemInserted(position);
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomChooseTeamsRecyclerAdapter(List<Team> data) {

        values = data;
        for (Team team : data){
            areTeamsSelected.add(false);

        }

    }
    public CustomChooseTeamsRecyclerAdapter(){}

    // Create new views (invoked by the layout manager)
    @Override
    public CustomChooseTeamsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.choose_teams_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //final String name = values.get(position);

        holder.txtFooter.setText(values.get(position).getName());
        StringBuilder sb = new StringBuilder();
        for (String s : values.get(position).getMembers())
        {
            sb.append(s);
            sb.append(TAG_SEPARATOR);

        }
        holder.members.setText(sb.toString());
        holder.switchbutton.setChecked(areTeamsSelected.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
