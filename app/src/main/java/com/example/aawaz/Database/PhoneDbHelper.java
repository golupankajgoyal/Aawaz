package com.example.aawaz.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




public class PhoneDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "phoneNos.db";

    /**
     * Database version, If you need to change the Database schema, then you must increase the DATABASE_VERSION
     */
    private static final int DATABASE_VERSION = 2;
    private static final String SQL_CREATE_ITEM_TABLE = " CREATE TABLE " + PhoneContract.ItemEntry.TABLE_NAME + " ( "
            + PhoneContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PhoneContract.ItemEntry.COLUMN_PHONE_NO
            + " TEXT NOT NULL " + " );";

    private static final String SQL_CREATE_INFO_TABLE = " CREATE TABLE " + PhoneContract.ItemEntry.LOCAL_TABLE_NAME + " ( "
            + PhoneContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PhoneContract.ItemEntry.COLUMN_USER_ID
            + " TEXT NOT NULL, " +  PhoneContract.ItemEntry.COLUMN_CONTACT_NO
            + " TEXT NOT NULL, " +  PhoneContract.ItemEntry.COLUMN_RELATIVE_1
            + " TEXT NOT NULL, " + PhoneContract.ItemEntry.COLUMN_RELATIVE_2
            + " TEXT NOT NULL " + " );";

    public PhoneDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_INFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
