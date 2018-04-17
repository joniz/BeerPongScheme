package com.example.jonathan.beerpongschemeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewGroupCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by jonathan on 2017-10-31.
 */

public class tab_home_fragment extends Fragment {
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private AccessTokenTracker loginTracker;
    private DatabaseAccess db = new DatabaseAccess();
    private static final String TAB_EMAIL = "email";
    private static final String TAB_PUBLICPROFILE = "public_profile";

    LoginButton loginButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){



            View view = inflater.inflate(R.layout.fragment_play, container, false);


                loginButton = (LoginButton) view.findViewById(R.id.login_button);
                loginButton.setReadPermissions(TAB_EMAIL, TAB_PUBLICPROFILE);
                // If using in a fragment
                loginButton.setFragment(this);
                // Other app specific specialization

                // Callback registration
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        loginButton.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), R.string.logincancel,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        throw exception;
                    }

                });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();


        loginTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                if(newAccessToken == null){
                    signOut();
                    loginButton.setVisibility(View.VISIBLE);

                }else{
                loginButton.setVisibility(View.GONE);
                }
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    private void handleFacebookAccessToken(final AccessToken token) {


            final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db.checkIfUserExist(Profile.getCurrentProfile().getId(), new DatabaseAccess.databaseCheckUserComplete() {
                                    @Override
                                    public void onActionDone(boolean succeeded) {
                                        if(!succeeded){
                                            User user = new User();
                                            user.setName(mAuth.getCurrentUser().getDisplayName());
                                            user.setId(Profile.getCurrentProfile().getId());
                                            db.storeUser(user);

                                        }
                                    }
                                });
                            }else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), R.string.errorauth,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }


                    });
        }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        loginTracker.stopTracking();
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}







