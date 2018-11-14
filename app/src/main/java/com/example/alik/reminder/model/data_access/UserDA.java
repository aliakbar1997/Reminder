package com.example.alik.reminder.model.data_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.alik.reminder.model.MyDatabaseHelper;
import com.example.alik.reminder.model.table_object.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDA {

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db;
    private String[] userColumns = {MyDatabaseHelper.USER_ID, MyDatabaseHelper.USER_FIRST_NAME, MyDatabaseHelper.USER_LAST_NAME};
    private String[] userColumns1 = {MyDatabaseHelper.USER_ID, MyDatabaseHelper.USER_FIRST_NAME, MyDatabaseHelper.USER_LAST_NAME, MyDatabaseHelper.USER_PHONE_NUMBER};

    public UserDA(Context context) {
        this.sqLiteOpenHelper = new MyDatabaseHelper(context);
    }

    public void open() {
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void addUser(UserModel user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.USER_FIRST_NAME, user.getFirstName());
        contentValues.put(MyDatabaseHelper.USER_LAST_NAME, user.getLastName());
        contentValues.put(MyDatabaseHelper.USER_PHONE_NUMBER, user.getPhoneNumber());

        db.insert(MyDatabaseHelper.TABLE_USER, null, contentValues);
    }

    public List<UserModel> getAllUser() {
        List<UserModel> users = new ArrayList<>();

        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USER, userColumns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                user.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.USER_ID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_LAST_NAME)));

                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return users;
    }

    public int deleteUserById(int id){
        return db.delete(MyDatabaseHelper.TABLE_USER, MyDatabaseHelper.USER_ID + " =?", new String[]{String.valueOf(id)});
    }

    public int updateUserById(UserModel user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabaseHelper.USER_FIRST_NAME, user.getFirstName());
        contentValues.put(MyDatabaseHelper.USER_LAST_NAME, user.getLastName());
        contentValues.put(MyDatabaseHelper.USER_PHONE_NUMBER, user.getPhoneNumber());

        return db.update(MyDatabaseHelper.TABLE_USER, contentValues, MyDatabaseHelper.USER_ID + " =?", new String[]{String.valueOf(user.getId())});
    }

    public UserModel getUserById(int id){
        UserModel user = new UserModel();
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USER, userColumns1, MyDatabaseHelper.USER_ID + " =?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            user.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.USER_ID)));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_LAST_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_PHONE_NUMBER)));
        }

        cursor.close();
        return user;
    }

    public UserModel getUserByName(String firstName, String lastName){
        UserModel user = new UserModel();
        Cursor cursor = db.query(MyDatabaseHelper.TABLE_USER, userColumns1,
                MyDatabaseHelper.USER_FIRST_NAME + " = ? AND " + MyDatabaseHelper.USER_LAST_NAME + " = ?",
                new String[]{firstName, lastName}, null, null, null);

        if (cursor.moveToFirst()){
            user.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.USER_ID)));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_LAST_NAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.USER_PHONE_NUMBER)));
        }

        cursor.close();
        return user;
    }
}
