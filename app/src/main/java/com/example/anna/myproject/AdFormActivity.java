package com.example.anna.myproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class AdFormActivity extends ActionBarActivity implements View.OnClickListener{

    static final int REQUEST_CODE_CAMERA = 1;
    static final int REQUEST_CODE_SELECT_IMAGE = 0;
    ImageView camera_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_form);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ad_form, menu);

        ImageButton cameraBtn = (ImageButton)findViewById(R.id.camera_button);
        cameraBtn.setOnClickListener(this);

        Button uploadBtn = (Button)findViewById(R.id.upload_button);
        uploadBtn.setOnClickListener(this);

        camera_image= (ImageView)findViewById(R.id.camera_image);
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
        if(v.getId() == R.id.camera_button)
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
        else if(v.getId() == R.id.upload_button)
        {
            //to upload an image
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);

        }
        else if(v.getId() == R.id.post_ad_button)
        {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            camera_image.setImageBitmap(imageBitmap);
        }
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //set the ImageView with the selected image
            camera_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            //if there is an error: unable to decode bitmap(permission not granted), use this-
            //<"uses-permission android:name=”android.permission.WRITE_EXTERNAL_STORAGE" />
            //in manifest file

        }
    }
}
