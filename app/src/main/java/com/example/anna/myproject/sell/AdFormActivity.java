package com.example.anna.myproject.sell;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.anna.myproject.R;
import com.example.anna.myproject.sqlite.AdDetailsModel;
import com.example.anna.myproject.sqlite.DatabaseOpenHelper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class AdFormActivity extends ActionBarActivity implements View.OnClickListener {

    static final int REQUEST_CODE_CAMERA = 1;
    static final int REQUEST_CODE_SELECT_IMAGE = 0;
    private static final String LOG_TAG = "AdFormActivity";
    public String imagePath;
    ImageView camera_image_view;
    DatabaseOpenHelper db;
    static int id = 1;
    int price;
    String phone;
    String category;
    Boolean postSuccess ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form);

        ImageButton cameraBtn = (ImageButton)findViewById(R.id.button_camera);
        cameraBtn.setOnClickListener(this);

        Button uploadBtn = (Button)findViewById(R.id.button_upload);
        uploadBtn.setOnClickListener(this);

        Button postAdBtn = (Button)findViewById(R.id.button_post_ad);
        postAdBtn.setOnClickListener(this);

        camera_image_view = (ImageView)findViewById(R.id.imageView_camera_image);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ad_form, menu);

        /*
        code for getting spinner data
         */
        final Spinner spinner= (Spinner)findViewById(R.id.spinner_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        if(v.getId() == R.id.button_camera)
        {
            //to capture an image
            Intent intent =  new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            //if you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            //there this code is added
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }

        }
        if(v.getId() == R.id.button_upload)
        {
            //to upload an image
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);

        }
        if(v.getId() == R.id.button_post_ad) {

                submitDbAd();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for capture image
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
           // Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //camera_image_view.setImageBitmap(imageBitmap);

            //for saving the captured image path in the database
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();
            imagePath = cursor.getString(column_index_data);
            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath );
            camera_image_view.setImageBitmap(bitmapImage);
            cursor.close();

        }
        //for select image
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //this imagePath is stored in database
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            //set the ImageView with the selected image
            camera_image_view.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            //if there is an error: unable to decode bitmap(permission not granted), use this-
            //<"uses-permission android:name=”android.permission.WRITE_EXTERNAL_STORAGE" />
            //in manifest file

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    public void submitDbAd()
    {
        EditText title = (EditText)findViewById(R.id.editText_Title);
        EditText description = (EditText)findViewById(R.id.editText_description);
        EditText location = (EditText)findViewById(R.id.editText_location);
        EditText name = (EditText)findViewById(R.id.editText_name);

        EditText priceEt = (EditText)findViewById(R.id.editText_price);
        String priceStr = priceEt.getText().toString();
        try{
            if(priceEt.getText() != null){
                price = Integer.parseInt(priceStr);
            }
        }
        catch(NumberFormatException e)
        {
            Log.v(LOG_TAG, " price number cant be parsed") ;
        }

        EditText phoneEt = (EditText)findViewById(R.id.editText_phone);
        String phoneStr = phoneEt.getText().toString();
        /*try{
            if(phoneEt.getText() != null){
                //phone = Long.parseLong(phoneStr);
            }
        }
        catch(NumberFormatException e)
        {
            Log.v(LOG_TAG, " phone number cant be parsed") ;
        }
        */

        byte[] AdImage = null;

        //get imagePath from the image picker and save it in AdDetails object -
        //to save it in db
        try {
            FileInputStream fileinputStream = new FileInputStream(imagePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileinputStream);
            AdImage = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(AdImage);
            fileinputStream.close();


        } catch (IOException e) {

            Log.v(LOG_TAG, "error while file reading .... = "+ e);
        }


        AdDetailsModel ad = new AdDetailsModel (title.getText().toString(),
                                                    category.toString(),
                                                    description.getText().toString(),
                                                    name.getText().toString(),
                                                    phoneStr,
                                                    location.getText().toString(),
                                                    price,
                                                    AdImage);

        db = new DatabaseOpenHelper(getApplicationContext());

        long row = db.createAd(ad, id);
        db.closeDB();

        if(row == -1)
        {
            Log.v(LOG_TAG, "error while inserting a row....");



        }
        else
        {
            Log.v(LOG_TAG, "Row _id = " + row + " is inserted");
            Intent intent = new Intent(this,PostSuccessfulActvity.class);
            intent.putExtra(Intent.EXTRA_TEXT, row);
            startActivity(intent);

            id++;

        }

    }

}
