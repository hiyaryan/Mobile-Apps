package edu.asu.bsse.rmenese1.androidapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Places Contract (PlacesContract.java)
 * This class provides the schema for the SQLite Database and helper methods.
 *
 * Copyright 2021 Ryan Meneses. The SER423 Instructional Team and Arizona State University
 * have the right to build and evaluate this software package for the purposes of grading
 * and program assessment.
 *
 * @author Ryan Meneses     mailto: rmenese1@asu.edu
 * @version 1.0
 * @since November 21, 2021
 */
public final class PlacesContract {
    private static final String SQL_CREATE_PLACES =
            "CREATE TABLE " + PlacesEntry.TABLE_NAME + " ("
            + PlacesEntry._ID + " INTEGER PRIMARY KEY,"
            + PlacesEntry.COLUMN_NAME_NAME + " TEXT,"
            + PlacesEntry.COLUMN_NAME_DESCRIPTION + " TEXT,"
            + PlacesEntry.COLUMN_NAME_CATEGORY + " TEXT,"
            + PlacesEntry.COLUMN_NAME_ADDRESS_TITLE + " TEXT,"
            + PlacesEntry.COLUMN_NAME_ADDRESS_STREET + " TEXT,"
            + PlacesEntry.COLUMN_NAME_ELEVATION + " INTEGER,"
            + PlacesEntry.COLUMN_NAME_LATITUDE + " INTEGER,"
            + PlacesEntry.COLUMN_NAME_LONGITUDE + " INTEGER)";

    private static final String SQL_DELETE_PLACES =
            "DROP TABLE IF EXISTS " + PlacesEntry.TABLE_NAME;

    private PlacesContract() {}

    public static class PlacesEntry implements BaseColumns {
        public static final String TABLE_NAME = "place";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_ADDRESS_TITLE = "address_title";
        public static final String COLUMN_NAME_ADDRESS_STREET = "address_street";
        public static final String COLUMN_NAME_ELEVATION = "elevation";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    public static class PlacesDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Places.db";

        public PlacesDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_PLACES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_PLACES);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
