package com.example.movieproject.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.movieproject.Classes.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MovieProject.db";
    private static final String USERS_TABLE = "Users";
    private static final String IMAGES_TABLE = "Images";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (NAME TEXT PRIMARY KEY, EMAIL TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + IMAGES_TABLE + " (NAME TEXT PRIMARY KEY, IMAGEDATA BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE);
    }

    public void deleteImages(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE);
    }

    public void insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.getUsername());
        contentValues.put("email",user.getEmail());
        contentValues.put("password",user.getPassword());

        db.insert(USERS_TABLE, null, contentValues);
    }

    public void insertImage(String username, byte[] imageData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",username);
        contentValues.put("imagedata",imageData);

        db.insert(IMAGES_TABLE, null, contentValues);
    }

    public boolean checkIfRegistered(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE NAME = " + "'" + user.getUsername() + "' AND EMAIL = '" + user.getEmail() + "'", null);
        return cursor.getCount() > 0;
    }

    public boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE EMAIL = '" + email + "' AND PASSWORD = '" + password + "'", null);
        return cursor.getCount() > 0;
    }

    public String getUsername(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM " + USERS_TABLE + " WHERE EMAIL = '" + email + "'",null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();

        return null;
    }

    public Bitmap getImage(String username){
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT imagedata FROM " + IMAGES_TABLE + " WHERE NAME= '" + username + "'", null);
  //      if (cursor.moveToFirst())
        {
            //byte[] image = cursor.getBlob(cursor.getColumnIndex("imagedata"));
            //Log.d("movies", String.valueOf(image.length));
            //return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
     //   cursor.close();
        return null;
    }
}
