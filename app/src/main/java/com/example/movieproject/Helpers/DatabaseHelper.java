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

import com.example.movieproject.Classes.Movie;
import com.example.movieproject.Classes.User;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MovieProject.db";
    private static final String USERS_TABLE = "Users";
    private static final String IMAGES_TABLE = "Images";
    private static final String FAVOURITES_TABLE = "Favourites";
    private static final String NOWPLAYING_TABLE = "NowPlaying";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (NAME TEXT PRIMARY KEY, EMAIL TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + IMAGES_TABLE + " (NAME TEXT, IMAGEDATA BLOB)");
        db.execSQL("CREATE TABLE " + FAVOURITES_TABLE + " (NAME TEXT, ID INTEGER, MOVIE_TITLE TEXT, MOVIE_DATE TEXT, OVERVIEW TEXT, IMAGE_PATH TEXT, VOTE_AVERAGE REAL, PRIMARY KEY(NAME,ID)) ");
        db.execSQL("CREATE TABLE " + NOWPLAYING_TABLE + " (ID INTEGER PRIMARY KEY, MOVIE_TITLE TEXT, MOVIE_DATE TEXT, OVERVIEW TEXT, IMAGE_PATH TEXT, VOTE_AVERAGE REAL) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOWPLAYING_TABLE);
        onCreate(db);
    }

    public void addFavouriteMovie(String user, Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user);
        contentValues.put("id",movie.getId());
        contentValues.put("movie_title",movie.getTitle());
        contentValues.put("movie_date",movie.getReleaseDate());
        contentValues.put("overview",movie.getOverview());
        contentValues.put("image_path",movie.getPosterPath());
        contentValues.put("vote_average",movie.getVoteAverage());

        db.insert(FAVOURITES_TABLE, null, contentValues);
    }

    public List<Movie> getFavourites(String user){
        List<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FAVOURITES_TABLE + " WHERE NAME= '" + user + "'", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(1);
                String title = cursor.getString(2);
                String date = cursor.getString(3);
                String overview = cursor.getString(4);
                String image = cursor.getString(5);
                double vote = cursor.getDouble(6);

                movies.add(new Movie(title,id,date,overview,image,vote));

                cursor.moveToNext();
            }

        }
        return movies;
    }

    public boolean checkIfInFavourites(String user, Movie movie){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FAVOURITES_TABLE + " WHERE NAME = '" + user + "' AND ID = '" + movie.getId() + "'",null);
        return cursor.getCount() > 0;
    }

    public void deleteFromFavourites(String user, Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVOURITES_TABLE, "NAME = '" + user + "' AND ID = '" + movie.getId() + "'",null);
    }

    public void insertUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.getUsername());
        contentValues.put("email",user.getEmail());
        contentValues.put("password",user.getPassword());

        db.insert(USERS_TABLE, null, contentValues);
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

    public void insertImage(String username, byte[] imageData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",username);
        contentValues.put("imagedata",imageData);

        if (checkIfHasImage(username)){
            db.update(IMAGES_TABLE,contentValues, "name = " + "'" + username + "'", null);
        } else {
            db.insert(IMAGES_TABLE, null, contentValues);
        }
    }

    public boolean checkIfHasImage(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + IMAGES_TABLE + " WHERE NAME= '" + username + "'", null);

        cursor.moveToFirst();
        return cursor.getInt(0) > 0;

    }

    public Bitmap getImage(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        int position = 1, size = getImageSize(username);

        byte[] imageBytes = new byte[size];
        ByteBuffer buffer = ByteBuffer.wrap(imageBytes);

        while (size > 0){
            Cursor cursor = db.rawQuery("SELECT substr(imagedata, " + position + "," + 1000000 + ") FROM " + IMAGES_TABLE + " WHERE NAME= '" + username + "'", null);
            if (cursor.moveToFirst()){
                //concat results
                buffer.put(cursor.getBlob(0));
            }
            size = size - 1000000;
            position = position + 1000000;
        }

        //convert byte array to bitmap
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    }

    public int getImageSize(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT length(imagedata) FROM " + IMAGES_TABLE + " WHERE NAME= '" + username + "'", null);
        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    public void changePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);
        db.update(USERS_TABLE,contentValues,"NAME = ?", new String[]{username});
    }

    public void deleteNowPlaying(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ NOWPLAYING_TABLE);
    }

    public void addNowPlayingMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id",movie.getId());
        contentValues.put("movie_title",movie.getTitle());
        contentValues.put("movie_date",movie.getReleaseDate());
        contentValues.put("overview",movie.getOverview());
        contentValues.put("image_path",movie.getPosterPath());
        contentValues.put("vote_average",movie.getVoteAverage());

        db.insert(NOWPLAYING_TABLE,null,contentValues);
    }

    public List<Movie> getNowPlaying(){
        List<Movie> movies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOWPLAYING_TABLE, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String overview = cursor.getString(3);
                String image = cursor.getString(4);
                double vote = cursor.getDouble(5);

                movies.add(new Movie(title,id,date,overview,image,vote));

                cursor.moveToNext();
            }

        }
        return movies;
    }
}
