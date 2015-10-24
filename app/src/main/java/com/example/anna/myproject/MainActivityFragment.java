package com.example.anna.myproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    LoginButton fbLoginBtn;
    CallbackManager callbackManager;
    FacebookCallback <LoginResult> callbackInfo; //informs whether the login was successful or not(error,cancelled)?

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        callbackManager = CallbackManager.Factory.create();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.image_slider_fragment, container, false);

        //Properties you can customize includes
        // LoginBehavior, DefaultAudience,
        // ToolTipPopup.Style and
        // permissions on the LoginButton.



        fbLoginBtn = (LoginButton) view.findViewById(R.id.facebook_login_button);

        //configure the button and request for permissions
        fbLoginBtn.setReadPermissions("user_friends");

        //If you use the LoginButton in a fragment, you need to set the fragment on the button
        fbLoginBtn.setFragment(this);
        // Other app specific specialization

        callbackInfo = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken token= loginResult.getAccessToken();

                //access the profile of the user
                Profile profile = Profile.getCurrentProfile();
                //to display the user name
                profile.getName();

                //so when you login the the username is displayed.but when we rotate the phone the username disappears.
                //to make it not happen, we must display the username in the Fragment's onResume()
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        };


        fbLoginBtn.registerCallback(callbackManager, callbackInfo);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //show username.
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, requestCode, data);
    }
}
