package com.example.anna.myproject.sell;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anna.myproject.home.CategoriesActivity;
import com.example.anna.myproject.R;
import com.example.anna.myproject.sqlite.AdDetailsModel;
import com.example.anna.myproject.sqlite.DatabaseOpenHelper;


public class ShowAdActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String LOG_TAG = "ShowAdActivity";

    DatabaseOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ad);

        Intent intent = getIntent();
        db = new DatabaseOpenHelper(getApplicationContext());
        //set the default value for the intent using getCount()
       // Log.v(LOG_TAG, "row count = "+ db.getRowCount());
        long id = intent.getLongExtra("EXTRA_TEXT", db.getRowCount());

        //Log.v(LOG_TAG, "row id = "+ id);
        showAdDetails(id);

       Button back = (Button)findViewById(R.id.back_button);
        back.setOnClickListener(this);


    }

    private void showAdDetails(long id) {

        DatabaseOpenHelper db = new DatabaseOpenHelper(getApplicationContext());

        AdDetailsModel ad = db.getAd(id);

        String title = ad.getTitle();
        String category = ad.getCategory();
        String description = ad.getDescription();
        String name = ad.getName();
        String location = ad.getLocation();
        String phone = ad.getPhone();
        //(long)String ph = String.valueOf(phone);
        int price = ad.getPrice();
        byte[] image = ad.getImage();
        Bitmap photo = BitmapFactory.decodeByteArray(image, 0, image.length);
        if(photo == null)
        {
            Log.v(LOG_TAG, "byte image unable to decode...");
        }

        TextView displayTitle = (TextView)findViewById(R.id.title_textView);
        displayTitle.setText(title);
        TextView displayCategory = (TextView)findViewById(R.id.category_textView);
        displayCategory.setText(category);
        TextView displayDescription = (TextView)findViewById(R.id.textView_description);
        displayDescription.setText(description);
        TextView displayName = (TextView)findViewById(R.id.name_textView);
        displayName.setText(name);
        TextView displayLocation = (TextView)findViewById(R.id.textView_location);
        displayLocation.setText(location);
        TextView displayPhone = (TextView)findViewById(R.id.textView_phone);
        displayPhone.setText(phone);
        TextView displayPrice = (TextView)findViewById(R.id.textView_price2);
        displayPrice.setText(Integer.toString(price));
        ImageView displayImage = (ImageView)findViewById(R.id.showImage_View);
        displayImage.setImageBitmap(photo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_ad, menu);
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
    public void onClick(View v) {

        if(v.getId() == R.id.back_button)
        {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        }


    }
}
