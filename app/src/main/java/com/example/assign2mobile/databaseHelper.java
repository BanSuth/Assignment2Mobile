package com.example.assign2mobile;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class databaseHelper extends SQLiteOpenHelper { //database helper function to create database
    public static final String DATABASE_NAME = "LocationFinder.db";
    public static final String KEY_TABLE = "locationTable";
    public static final String KEY_ID = "id"; //defining all the keys and SQL variables to make things easier
    public static final String KEY_ADDR = "address";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    } //implementing required function for SQLiteOpenHelper

    @Override
    public void onCreate(SQLiteDatabase db) { //on create function to create the address table
        db.execSQL(
                "create table " + KEY_TABLE + " (" +
                        KEY_ID + " integer primary key autoincrement, " +
                        KEY_ADDR + " text not null, " +
                        KEY_LAT + " real, " +
                        KEY_LONG + " real" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) { //implementing required onUpgrade function. Not used as only one version of DB.
        db.execSQL("drop table if exists " + KEY_TABLE);
        onCreate(db);
    }

//    Functions to add, update and delete locations on the database

    public boolean addAddr(addrObj location){
        SQLiteDatabase db = this.getWritableDatabase();

        //get the values from the address model to be put in the db
        ContentValues values = new ContentValues();
        values.put(KEY_ADDR, location.getAddress());
        values.put(KEY_LAT, location.getLatitude());
        values.put(KEY_LONG, location.getLongitude());

        // get insertion status
        long addStatus = db.insert(KEY_TABLE, null, values);

        // return true or false based on the success of the query
        return addStatus != -1;
    }

    public boolean addAddrs(List<addrObj> addrObjList){
        SQLiteDatabase db = this.getWritableDatabase();
        long addStatus = 0;
        //get the values from address model list and  put all in to the db
        for (addrObj e : addrObjList) {
            ContentValues values = new ContentValues();
            values.put(KEY_ADDR, e.getAddress());
            values.put(KEY_LAT, e.getLatitude());
            values.put(KEY_LONG, e.getLongitude());

        //get insertion status
            addStatus = db.insert(KEY_TABLE, null, values);
        }
        //return true or false based on the success of the query
        if (addStatus == -1){
            return false;
        }
        return true;
    }

    public boolean updateAddr(addrObj location){ //function to update an existing address
        SQLiteDatabase db = this.getWritableDatabase();

        // get the values from address model to be put in the db to update the entry
        ContentValues values = new ContentValues();
        values.put(KEY_ADDR, location.getAddress());
        values.put(KEY_LAT, location.getLatitude());
        values.put(KEY_LONG, location.getLongitude());

        // get update status and query to update entry
        long updateStatus = db.update(KEY_TABLE, values, KEY_ID + "=?", new String[] {Integer.toString(location.getId())});

        // return true or false based on the success of the query
        if (updateStatus == -1){
            return false;
        }
        return true;
    }

    public boolean deleteAddr(addrObj location){ //function to delete an address
        SQLiteDatabase db = this.getWritableDatabase();

        //query to delete the address from the DB
        long deleteStatus = db.delete(KEY_TABLE, KEY_ID + "=?", new String[] {Integer.toString(location.getId())});

        // return true or false based on the success of the query
        if (deleteStatus == -1){
            return false;
        }
        return true;
    }


    public ArrayList<addrObj> getAlladdr(){ //get all addresses in the database
        ArrayList<addrObj> locations = new ArrayList<>();

        // make a query to get all addresses from the database
        String query = "select * from " + KEY_TABLE+" ORDER BY "+KEY_ID+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        // iterate over each entry and add the location to the arraylist
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String address = cursor.getString(1);
                double latitude = cursor.getDouble(2); //getting result from cursor
                double longtitude = cursor.getDouble(3);


                addrObj loc = new addrObj(id, address, latitude, longtitude); //creating new address object and add to address list
                locations.add(loc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locations;
    }

    public void deleteTable(){ //function to delete table and remake. Used only during initial startup or for debug.
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+KEY_TABLE);
        onCreate(db);

    }
}
