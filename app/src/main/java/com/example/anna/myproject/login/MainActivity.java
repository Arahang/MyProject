package com.example.anna.myproject.login;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.anna.myproject.R;
import com.example.anna.myproject.home.CategoriesActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
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
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;


public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
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

    MyPagerAdapter pagerAdapter;
    ViewPager mViewPager;



    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    LoginButton fbLoginBtn;
    CallbackManager callbackManager;//informs whether the login was successful or not(error,cancelled)?
    FacebookCallback<LoginResult> callbackInfo = new FacebookCallback<LoginResult>() {
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
                Log.e(LOG_TAG, "exception: " + String.valueOf(e));

            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //google sign in
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();


        setContentView(R.layout.login_main);
        
        /*
             hook the ViewPager layout to a PagerAdapter which will populate our ViewPager with pages
         */
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

         final LayerDrawable background = (LayerDrawable) mViewPager.getBackground();
        background.getDrawable(0).setAlpha(0); // this is the lowest drawable
        background.getDrawable(1).setAlpha(0);
        background.getDrawable(2).setAlpha(0);
        background.getDrawable(3).setAlpha(0);
        background.getDrawable(4).setAlpha(255); // this is the upper one

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {

                int index = (Integer) view.getTag();
                Drawable currentDrawableInLayerDrawable;
                currentDrawableInLayerDrawable = background.getDrawable(index);


                if (position <= -1 || position >= 1) {
                    currentDrawableInLayerDrawable.setAlpha(0);
                } else if (position == 0) {
                    currentDrawableInLayerDrawable.setAlpha(255);
                } else {
                    currentDrawableInLayerDrawable.setAlpha((int) (255 - Math.abs(position * 255)));
                }

            }
        });




        PageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);





        //Properties you can customize includes
        // LoginBehavior, DefaultAudience,
        // ToolTipPopup.Style and
        // permissions on the LoginButton.
        fbLoginBtn = (LoginButton) findViewById(R.id.facebook_login_button);

        //configure the button and request for permissions
        fbLoginBtn.setReadPermissions("user_friends");

        //If you use the LoginButton in a fragment, you need to set the fragment on the button
        //fbLoginBtn.setFragment(this);
        // Other app specific specialization

        fbLoginBtn.registerCallback(callbackManager, callbackInfo);


        // Large sign-in
        SignInButton gmLoginBtn= ((SignInButton)findViewById(R.id.google_login_button));
        gmLoginBtn.setSize(SignInButton.SIZE_WIDE);
        //set onclick listener on the sign in button
        gmLoginBtn.setOnClickListener(this);

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
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
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
                int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

                if (resultCode != ConnectionResult.SUCCESS) {
                    if (apiAvailability.isUserResolvableError(resultCode)) {
                        apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
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
                        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

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

    // [START on_sign_in_clicked] google sign in
    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

    }
    // [END on_sign_in_clicked]



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "on resume called");

        //show facebook username even when the device orientation is changed
        Profile profile = Profile.getCurrentProfile();
        displayProfile(profile);

        //after sign out if user again wants to login
        mGoogleApiClient.connect();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "fragment on stop called");
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.google_login_button) {
            onSignInClicked();
        }

    }


    //display profile for facebook
    public void displayProfile(Profile profile)
    {
        if (profile !=null)
        {
            //welcomeText.setText(" home welcome " + profile.getName());


            String name_Account = "Facebook";
            Intent intent = new Intent(this,CategoriesActivity.class);
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

            Intent intent = new Intent(this, CategoriesActivity.class);

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


}
