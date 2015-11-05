package com.example.anna.myproject.sell;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.anna.myproject.home.CategoriesActivity;
import com.example.anna.myproject.R;
import com.example.anna.myproject.sqlite.AdDetailsModel;
import com.example.anna.myproject.sqlite.DatabaseOpenHelper;

import java.io.ByteArrayOutputStream;


public class PostSuccessfulActvity extends ActionBarActivity implements View.OnClickListener    {




    DatabaseOpenHelper db;
    long row_id;

    String category;
    Boolean postSuccess ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_successful_actvity);
        Button returnBtn =(Button)findViewById(R.id.button_return);
        Button show = (Button) findViewById(R.id.button_show_ad);
        returnBtn.setOnClickListener(this);
        show.setOnClickListener(this);

       Intent intent = getIntent();
        db = new DatabaseOpenHelper(getApplicationContext());
        row_id = intent.getLongExtra("EXTRA_TEXT", db.getRowCount());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_successful_actvity, menu);

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
        if(v.getId() == R.id.button_return)
        {
            Intent intent = new Intent(this, CategoriesActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.button_show_ad)
        {

            Intent intent = new Intent(this, ShowAdActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, row_id);
            startActivity(intent);
        }

    }




}
