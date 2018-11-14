package com.example.alik.reminder.model.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alik.reminder.model.MyDatabaseHelper;

public class AlarmCodeDA {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;
    private String[] remindColumns = {MyDatabaseHelper.ALARM_ID};

    public AlarmCodeDA(Context context) {
        this.sqLiteOpenHelper = new MyDatabaseHelper(context);
    }

    public void open() {
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }


    public void addId(int remindPrimary) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.ALARM_ID, remindPrimary);

        db.insert(MyDatabaseHelper.TABLE_ALARM_CODE, null, contentValues);
    }

    public int getLastId() {
        int id = 0;

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_ALARM_CODE, remindColumns, null, null, null, null, null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ALARM_ID));
        }

        cursor.close();
        return id;
    }
}
