package com.example.jonathan.beerpongschemeandroid;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 2017-11-10.
 */

public class tab_account_fragment extends Fragment {

    private ImageView image;
    private TextView user;
    private TextView numberOfTeams;
    private DatabaseAccess db = new DatabaseAccess();
    private FacebookButtonBase fbButton;
    private TextView loggedoutbox;
    private static final String TAB_SPACECOLON = ": ";
    private static final String TAG_RETAINEDTEAMSCOUNTFRAGMENT = "teamsCountRetainedFragment";
    private static final String TAG_TEAMCOUNT = "count";
    private static final String TAG_ACCOUNTFRAGMENT = "accountFragment";
    private AccessTokenTracker loginTracker;
    private int teamCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_tab_account, container, false);
        loggedoutbox = view.findViewById(R.id.loggedouttextview);
        user = view.findViewById(R.id.user);
        numberOfTeams = view.findViewById(R.id.numberofteams);
        image = (ImageView) view.findViewById(R.id.accountimage);
        fbButton = view.findViewById(R.id.facebook_button);
        loggedoutbox.setVisibility(View.GONE);
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            teamCount = savedInstanceState.getInt(TAG_TEAMCOUNT);
        }
        if (AccessToken.getCurrentAccessToken() != null) {
            Picasso.with(getContext())
                    .load(Profile.getCurrentProfile().getProfilePictureUri(500, 500))
                    .resize(500, 500)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .centerInside()
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (image != null) {
                                Bitmap imageBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                imageDrawable.setCircular(true);
                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                image.setImageDrawable(imageDrawable);
                            }
                        }

                        @Override
                        public void onError() {
                            Picasso.with(getActivity())
                                    .load(Profile.getCurrentProfile().getProfilePictureUri(500, 500))
                                    .resize(500, 500)
                                    .centerInside()
                                    .into(image, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            if (image != null) {
                                                Bitmap imageBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                                                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                                imageDrawable.setCircular(true);
                                                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                                image.setImageDrawable(imageDrawable);
                                            }
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });

                        }
                    });
            user.setText(Profile.getCurrentProfile().getName());
            if (savedInstanceState == null) {
                db.getNumOfUserTeams(Profile.getCurrentProfile().getId(), new DatabaseAccess.getNumofTeams() {
                    @Override
                    public void onSucces(Integer count) {
                        teamCount = count;
                        if (numberOfTeams != null) {
                            numberOfTeams.setText(getString(R.string.teams_added) + TAB_SPACECOLON + String.valueOf(count));
                        }
                    }
                });
            } else {
                numberOfTeams.setText(getString(R.string.teams_added) + TAB_SPACECOLON + String.valueOf(savedInstanceState.getInt(TAG_TEAMCOUNT)));
            }

        } else {
            user.setVisibility(View.GONE);
            numberOfTeams.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            fbButton.setVisibility(View.GONE);
            loggedoutbox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        image = null;
        numberOfTeams = null;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt(TAG_TEAMCOUNT, teamCount);
        // etc.
    }

    private void tracker() {
        loginTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                if (newAccessToken == null) {
                    tab_account_fragment accountFragment;
                    accountFragment = (tab_account_fragment) getFragmentManager().findFragmentByTag(TAG_ACCOUNTFRAGMENT);
                    if(accountFragment == null){
                        accountFragment = new tab_account_fragment();
                    }
                    FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                    fragTransaction.detach(accountFragment);
                    fragTransaction.attach(accountFragment);
                    fragTransaction.commit();


                }
            }
        };


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        loginTracker.stopTracking();
    }
}
