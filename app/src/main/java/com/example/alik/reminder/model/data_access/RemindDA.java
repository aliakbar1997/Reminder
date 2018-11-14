package com.example.alik.reminder.model.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alik.reminder.model.MyDatabaseHelper;
import com.example.alik.reminder.model.table_object.RemindModel;

import java.util.ArrayList;
import java.util.List;

public class RemindDA {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;
    private String[] remindColumns = {MyDatabaseHelper.REMIND_ID};
    private String[] userColumns1 = {MyDatabaseHelper.REMIND_LABEL, MyDatabaseHelper.REMIND_TIME, MyDatabaseHelper.REMIND_KIND, MyDatabaseHelper.REMIND_STATUS};

    public RemindDA(Context context) {
        this.sqLiteOpenHelper = new MyDatabaseHelper(context);
    }

    public void open() {
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void addAlarm(RemindModel remindModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.REMIND_USER_ID, remindModel.getUser_id());
        contentValues.put(MyDatabaseHelper.REMIND_KIND, remindModel.getKind());
        contentValues.put(MyDatabaseHelper.REMIND_STATUS, remindModel.getStatus());
        contentValues.put(MyDatabaseHelper.REMIND_TIME, remindModel.getTime());
        contentValues.put(MyDatabaseHelper.REMIND_LABEL, remindModel.getLabel());

        db.insert(MyDatabaseHelper.TABLE_REMIND, null, contentValues);
    }

    public int deleteRemindById(int userId){
        return db.delete(MyDatabaseHelper.TABLE_REMIND, MyDatabaseHelper.REMIND_USER_ID + " =?", new String[]{String.valueOf(userId)});
    }

    public int getLastId() {
        int id = 0;

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_REMIND, remindColumns, null, null, null, null, null);
        if (cursor.moveToLast()) {
            id = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.REMIND_ID));
        }

        cursor.close();
        return id;
    }

    public void updateStatusById(int reminderKey, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.REMIND_STATUS, status);

        db.update(MyDatabaseHelper.TABLE_REMIND, contentValues,
                MyDatabaseHelper.REMIND_ID + " =?", new String[]{String.valueOf(reminderKey)});
    }

    public List<RemindModel> getInfoByUserId(int userId) {

        List<RemindModel> remindModels = new ArrayList<>();

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_REMIND, userColumns1, MyDatabaseHelper.REMIND_USER_ID + " =?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()){
            do {
                RemindModel remindModel = new RemindModel();

                remindModel.setLabel(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.REMIND_LABEL)));
                remindModel.setTime(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.REMIND_TIME)));
                remindModel.setKind(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.REMIND_KIND)));
                remindModel.setStatus(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.REMIND_STATUS)));

                remindModels.add(remindModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return remindModels;

    }
}
