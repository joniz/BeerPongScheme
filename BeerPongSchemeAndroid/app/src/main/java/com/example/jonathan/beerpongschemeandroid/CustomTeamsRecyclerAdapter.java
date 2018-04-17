package com.example.jonathan.beerpongschemeandroid;

/**
 * Created by jonathan on 2017-11-08.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.database.DatabaseError;

public class CustomTeamsRecyclerAdapter extends RecyclerView.Adapter<CustomTeamsRecyclerAdapter.ViewHolder> {
    public static List<Team> values = new ArrayList<>();
    private static final String TAG_LINEBREAK  = "\n";
    private static final String TAG_COLONSPACE = ": ";
    private static final String TAG_SEPARATOR = " | ";

    private DatabaseAccess db = new DatabaseAccess();
    public class ViewHolder extends RecyclerView.ViewHolder {



        // each data item is just a string in this case
        private TextView txtHeader;
        private TextView txtFooter;
        private TextView teamTitle;
        private ImageButton deleteButton;
            public View layout;
        private TextView members;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.teamtitle);
            txtFooter = (TextView) v.findViewById(R.id.txtName);
            teamTitle = (TextView) v.findViewById(R.id.teamtitle);
            members = v.findViewById(R.id.members);

            deleteButton = (ImageButton) v.findViewById(R.id.deletebutton);

            v.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    Snackbar snackbar = Snackbar.make(v, v.getContext().getString(R.string.totalgames) + TAG_COLONSPACE + values.get(getAdapterPosition()).getTotNumberOfGames() + TAG_LINEBREAK
                                    + v.getContext().getString(R.string.wongames)+ TAG_COLONSPACE + values.get(getAdapterPosition()).getWonGames() + TAG_LINEBREAK
                                    + v.getContext().getString(R.string.lostgames) + TAG_COLONSPACE + values.get(getAdapterPosition()).getLostGames(),
                            Snackbar.LENGTH_LONG).setDuration(Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView tv = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setMaxLines(3);
                    snackbar.show();
                }

            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.deleteteam)
                            .setMessage(R.string.deleteteamtext)
                            .setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int whichButton){
                                            db.deleteTeam(values.get(getAdapterPosition()).getUUID(), AccessToken.getCurrentAccessToken().getUserId());
                                            remove(getAdapterPosition());
                                        }
                                    }
                            ).setNegativeButton(
                            android.R.string.no,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            }
                    ).show();
                }
            });
        }
    }
    public void add(int position) {
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if(position != -1) {
            values.remove(position);
            notifyItemRemoved(position);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomTeamsRecyclerAdapter(List<Team> data) {
        values = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomTeamsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.teams_layout, parent, false);
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
        }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();

    }
}
