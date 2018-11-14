package com.example.alik.reminder.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminder.db";
    private static final int DATABASE_VERSION = 1;

    //tables name
    public static final String TABLE_USER = "user";
    public static final String TABLE_REMIND = "remind";
    public static final String TABLE_ALARM_CODE = "alarm_code";

    public static final String USER_ID = "id";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_PHONE_NUMBER = "phone_number";

    public static final String REMIND_ID = "id";
    public static final String REMIND_USER_ID = "user_id";
    public static final String REMIND_LABEL = "label";
    public static final String REMIND_KIND = "kind";
    public static final String REMIND_STATUS = "status";
    public static final String REMIND_TIME = "time";

    public static final String ALARM_ID = "alarm_id";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ( " +
                        USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        USER_FIRST_NAME + " TEXT NOT NULL , " +
                        USER_LAST_NAME + " TEXT NOT NULL , " +
                        USER_PHONE_NUMBER + " TEXT NOT NULL )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REMIND + " ( " +
                REMIND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                REMIND_USER_ID + " INTEGER NOT NULL , " +
                REMIND_LABEL + " TEXT NOT NULL , " +
                REMIND_TIME + " TEXT NOT NULL , " +
                REMIND_KIND + " INTEGER NOT NULL , " +
                REMIND_STATUS + " INTEGER NOT NULL )");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ALARM_CODE + " ( " +
                ALARM_ID + " INTEGER NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
