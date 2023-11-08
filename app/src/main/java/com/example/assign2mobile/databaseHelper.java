package com.example.assign2mobile;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class databaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LocationFinder.db";
    public static final String KEY_TABLE = "locationTable";
    public static final String KEY_ID = "id";
    public static final String KEY_ADDR = "address";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LONG = "longitude";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists " + KEY_TABLE);
        onCreate(db);
    }

//    Functions to add, update and delete locations on the database

    public boolean addAddr(addrObj location){
        SQLiteDatabase db = this.getWritableDatabase();

//        get the values from model to be put in the db
        ContentValues values = new ContentValues();
        values.put(KEY_ADDR, location.getAddress());
        values.put(KEY_LAT, location.getLatitude());
        values.put(KEY_LONG, location.getLongitude());

//        get insertion status
        long addStatus = db.insert(KEY_TABLE, null, values);

//        return true or false based on the success of the query
        if (addStatus == -1){
            return false;
        }
        return true;
    }

    public boolean addAddrs(List<addrObj> addrObjList){
        SQLiteDatabase db = this.getWritableDatabase();
        long addStatus = 0;
//        get the values from model to be put in the db
        for (addrObj e : addrObjList) {
            ContentValues values = new ContentValues();
            values.put(KEY_ADDR, e.getAddress());
            values.put(KEY_LAT, e.getLatitude());
            values.put(KEY_LONG, e.getLongitude());

//        get insertion status
            addStatus = db.insert(KEY_TABLE, null, values);
        }
//        return true or false based on the success of the query
        if (addStatus == -1){
            return false;
        }
        return true;
    }

    public boolean updateAddr(addrObj location){
        SQLiteDatabase db = this.getWritableDatabase();

//        get the values from model to be put in the db
        ContentValues values = new ContentValues();
        values.put(KEY_ADDR, location.getAddress());
        values.put(KEY_LAT, location.getLatitude());
        values.put(KEY_LONG, location.getLongitude());

//        get update status
        long updateStatus = db.update(KEY_TABLE, values, KEY_ID + "=?", new String[] {Integer.toString(location.getId())});

//        return true or false based on the success of the query
        if (updateStatus == -1){
            return false;
        }
        return true;
    }

    public boolean deleteAddr(addrObj location){
        SQLiteDatabase db = this.getWritableDatabase();

        long deleteStatus = db.delete(KEY_TABLE, KEY_ID + "=?", new String[] {Integer.toString(location.getId())});

//        return true or false based on the success of the query
        if (deleteStatus == -1){
            return false;
        }
        return true;
    }

    //    get all locations in the database to be displayed
    public ArrayList<addrObj> getAlladdr(){
        ArrayList<addrObj> locations = new ArrayList<>();

//        make a query to get from the database
        String query = "select * from " + KEY_TABLE+" ORDER BY "+KEY_ID+" ASC";
        SQLiteDatabase db = this.getReadableDatabase();

//        iterate over each entry and add the location to the arraylist
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String address = cursor.getString(1);
                double latitude = cursor.getDouble(2);
                double longtitude = cursor.getDouble(3);


                addrObj loc = new addrObj(id, address, latitude, longtitude);
                locations.add(loc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locations;
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+KEY_TABLE);
        onCreate(db);

    }
}
