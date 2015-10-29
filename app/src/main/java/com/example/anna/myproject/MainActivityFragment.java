package com.example.anna.myproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {

    //for google sign in
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 1;
    /* Client used to interact with Google APIs. */
    GoogleApiClient mGoogleApiClient;

    // when your Activity receives an onConnectionFailed callback, your Activity should wait for the user to click the sign-in button
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private static final int RESULT_OK = 1;



    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    TextView welcomeText;
    LoginButton fbLoginBtn;
    CallbackManager callbackManager;//informs whether the login was successful or not(error,cancelled)?
    FacebookCallback <LoginResult> callbackInfo = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken token= loginResult.getAccessToken();

            //access the profile of the user
            Profile profile = Profile.getCurrentProfile();
            //to display the user name
            displayProfile(profile);

            //so when you login the the username is displayed.but when we rotate the phone the username disappears.
            //to make it not happen, we must display the username in the Fragment's onResume()
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            if(e !=null){
                Log.e(LOG_TAG, "exception: "+ String.valueOf(e));

            }


        }
    };



    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //google sign in
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.login_fragment, container, false);

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

        fbLoginBtn.registerCallback(callbackManager, callbackInfo);



        // Large sign-in
        SignInButton gmLoginBtn= ((SignInButton)view.findViewById(R.id.google_login_button));
        gmLoginBtn.setSize(SignInButton.SIZE_WIDE);
        //set onclick listener on the sign in button
        gmLoginBtn.setOnClickListener(this);

        return view;
    }


    //display profile for facebook
    public void displayProfile(Profile profile)
    {
        if (profile !=null)
        {
            //welcomeText.setText(" home welcome " + profile.getName());


            String name_Account = "Facebook";
            Intent intent = new Intent(getActivity(), CategoriesActivity.class);
            intent.putExtra("EXTRA_TEXT_1", profile.getName());
            intent.putExtra("EXTRA_TEXT_2", name_Account);

            startActivity(intent);

            //so when you login the the username is displayed.but when we rotate the phone the username disappears.
            //to make it not happen, we must display the username in the Fragment's onResume()
        }

    }

    //display profile and intent for google sign in
    public void displayProfile()
    {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {

                Intent intent = new Intent(getActivity(), CategoriesActivity.class);

                String username = currentPerson.getDisplayName();
                intent.putExtra("EXTRA_TEXT_1", username);
                String name_Account = "Google";
                intent.putExtra("EXTRA_TEXT_2", name_Account);
                startActivity(intent);
            }
            else
            {
                Log.v(LOG_TAG, "Current person is null");
            }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //callback for facebook login
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //reset the state of the flags when control returns to your Activity in onActivityResult for google
        Log.d(LOG_TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.google_login_button) {
            onSignInClicked();
        }

    }


    // GoogleApiClient.ConnectionCallbacks methods
    @Override
    public void onConnected(Bundle bundle) {
        //when google sign in is successful
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(LOG_TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        displayProfile();

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    //GoogleApiClient.OnConnectionFailedListener method
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(LOG_TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
            else
            {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());

                if (resultCode != ConnectionResult.SUCCESS) {
                    if (apiAvailability.isUserResolvableError(resultCode)) {
                        apiAvailability.getErrorDialog(getActivity(), resultCode, RC_SIGN_IN,
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        mShouldResolve = false;

                                        // If getCurrentPerson returns null there is generally some error with the
                                        // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                                        Log.w(LOG_TAG, "sign in error ");

                                    }
                                }).show();
                    }
                    else
                    {
                        Log.w(LOG_TAG, "Google Play Services Error:" + connectionResult);
                        String errorString = apiAvailability.getErrorString(resultCode);
                        Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();

                        mShouldResolve = false;

                    }
                }

            }
        }
        else
        {
            // Show the signed-out UI
            //showSignedOutUI();
        }
    }

    // [START on_sign_in_clicked]
    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        welcomeText.setText("signing in....");
    }
    // [END on_sign_in_clicked]


    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "on resume called");

        //show facebook username even when the device orientation is changed
        Profile profile = Profile.getCurrentProfile();
        displayProfile(profile);

        //after sign out if user again wants to login
        mGoogleApiClient.connect();

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG,"fragment on stop called");
        mGoogleApiClient.disconnect();
    }


}

