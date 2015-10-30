package com.example.anna.myproject;


import android.content.Intent;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;


public class CategoriesActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    Toolbar toolbar;
    SlidingTabLayout tabs;

    MyCustomPagerAdapter customFragmentAdapter;
    ViewPager mViewPager;

    private static final String LOG_TAG = CategoriesActivity.class.getSimpleName();
    String typeOfAccnt = null;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);


        //Don't forget to call to FacebookSdk.sdkInitialize(getApplicationContext());
        // before you ask any methods in Facebook SDK.
        FacebookSdk.sdkInitialize(getApplicationContext());

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        setContentView(R.layout.activity_categories);

        Log.v(LOG_TAG, "welcome anamika ");

        /*
             hook the ViewPager layout to a PagerAdapter which will populate our ViewPager with pages
         */
        customFragmentAdapter = new MyCustomPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(customFragmentAdapter);

        /* Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
         it's PagerAdapter set.
         */
        //Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
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


        Intent intent = getIntent();
        String user = intent.getStringExtra("EXTRA_TEXT_1");
        typeOfAccnt = intent.getStringExtra("EXTRA_TEXT_2");

        if (id == R.id.action_sign_out) {

            if (typeOfAccnt.equals("Facebook")) {
                // FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();

                //return to the sign in page
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            } else if (typeOfAccnt.equals("Google")) {
                // Clear the default account so that GoogleApiClient will not automatically
                // connect in the future.
                //if (mGoogleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.disconnect();

                System.out.println("logout successfull");

                //return to sign in page
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "on resume called....");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "on stop called....");
        mGoogleApiClient.disconnect();


    }

    @Override
    public void onClick(View v) {

    }
}
