package com.example.anna.myproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna on 30-10-2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = "DatabaseOpenHelper";
    AdDetailsModel ad = new AdDetailsModel();

    //In order to upgrade the Database in Android
    // you should increment the DATABASE_VERSION by one,
    // in order for the SQLOpenHelper to know that it must called the onUpgrade method.
    private static final int DATABASE_VERSION = 21;
    public static final String DATABASE_NAME = "ShoppingData.db" ;
    public static final String TABLE_NAME = "AdDetails" ;
    public static final String COLUMN_ID = "_id" ;
    public static final String COLUMN_TITLE = "Title" ;
    public static final String COLUMN_CATEGORY = "Category" ;
    public static final String COLUMN_DESCRIPTION = "Description" ;
    public static final String COLUMN_LOCATION = "Location" ;
    public static final String COLUMN_NAME = "Name" ;
    public static final String COLUMN_PHOTO = "Photos" ;
    public static final String COLUMN_PHONE = "Phone_number" ;
    public static final String COLUMN_PRICE = "Price" ;



    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_CATEGORY + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_PRICE + " INTEGER, "
            + COLUMN_NAME +  " TEXT, "
            + COLUMN_PHOTO + " BLOB, "
            + COLUMN_PHONE +  " TEXT ); ";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

    /*
    * Creating a Ad
    */

    public long createAd(AdDetailsModel ad, int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(id == 1)
        {
            values.put(COLUMN_ID, id);
        }
        values.put(COLUMN_TITLE, ad.getTitle());
        values.put(COLUMN_CATEGORY, ad.getCategory());
        values.put(COLUMN_DESCRIPTION, ad.getDescription());
        values.put(COLUMN_LOCATION, ad.getLocation());
        values.put(COLUMN_PRICE, ad.getPrice());
        values.put(COLUMN_NAME, ad.getName());
        values.put(COLUMN_PHOTO, ad.getImage());
        values.put(COLUMN_PHONE, ad.getPhone());



        // insert row
        //returns the row ID of the newly inserted row, or -1 if an error occurred
        long ad_id = db.insert(TABLE_NAME, null, values);

        if(ad_id == -1)
        {
            //Log.v(LOG_TAG, "error while inserting a row....");
        }
        else
            Log.v(LOG_TAG, "row " + ad_id + " inserted successfully....");



        return ad_id;
    }


    /*
    * get single Ad
    */
    public AdDetailsModel getAd(long Ad_id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + Ad_id;

        Log.e(LOG_TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        //check the no of rows and whether move to first is true
        c.moveToFirst();
        Boolean move = c.moveToFirst();
        //Log.v(LOG_TAG, "cursor count is = " + c.getCount());
        //Log.v(LOG_TAG, "cursor is " + c + " or move to first is" + move);


        if (c != null && c.moveToFirst()) {


            ad.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            ad.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
            ad.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
            ad.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
            ad.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            ad.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
            ad.setPhone(c.getString(c.getColumnIndex(COLUMN_PHONE)));
            ad.setPrice(c.getInt(c.getColumnIndex(COLUMN_PRICE)));
            ad.setImage(c.getBlob(c.getColumnIndex(COLUMN_PHOTO)));
        }

            c.close();

            db.close();



        return ad;
    }


    /*
    * getting all Ads
    * */
    public List<AdDetailsModel> getAllAd() {
        List<AdDetailsModel> adsList = new ArrayList<AdDetailsModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Log.e(LOG_TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                AdDetailsModel ad = new AdDetailsModel();
                ad.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
                ad.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
                ad.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
                ad.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
                ad.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
                ad.setLocation(c.getString(c.getColumnIndex(COLUMN_LOCATION)));
                ad.setPhone(c.getString(c.getColumnIndex(COLUMN_PHONE)));
                ad.setPrice(c.getInt(c.getColumnIndex(COLUMN_PRICE)));
                ad.setImage(c.getBlob(c.getColumnIndex(COLUMN_PHOTO)));

                // adding to AD list
                adsList.add(ad);
            } while (c.moveToNext());
        }

        return adsList;
    }

    //get no of rows count
    public int getRowCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount();

    }



    /*
    * Updating a Ad
    */
    public int updateAd(AdDetailsModel ad) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, ad.getTitle());
        values.put(COLUMN_DESCRIPTION, ad.getDescription());
        values.put(COLUMN_NAME, ad.getName());
        values.put(COLUMN_PHOTO, ad.getImage());
        values.put(COLUMN_PHONE, ad.getPhone());
        values.put(COLUMN_PRICE, ad.getPrice());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(ad.getId()) });
    }

    /*
    * Deleting a AD
    */
    public void deleteAd(long Ad_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(Ad_id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}
