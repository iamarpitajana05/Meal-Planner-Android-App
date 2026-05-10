package com.example.meal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "meal.db";
    private static final String TABLE_USERS = "mealusers";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public static final String TABLENAME = "meal_planner";
    public static final String COL_ID = "id";
    public static final String COL_WEEK = "week_no";
    public static final String COL_DAY = "day_of_week";
    public static final String COL_BREAKFAST = "breakfast";
    public static final String COL_LUNCH = "lunch";
    public static final String COL_DINNER = "dinner";

    public static final int VER = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // User table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT)");

        // Meal Planner table
        db.execSQL("CREATE TABLE " + TABLENAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_WEEK + " INTEGER NOT NULL, " +
                COL_DAY + " TEXT NOT NULL, " +
                COL_BREAKFAST + " TEXT, " +
                COL_LUNCH + " TEXT, " +
                COL_DINNER + " TEXT, " +
                "UNIQUE (" + COL_WEEK + ", " + COL_DAY + ")" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    // --- USER AUTHENTICATION METHODS ---

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // --- MEAL PLANNER DATA METHODS ---

    // Insert new meal
    public long insertMeal(int week, String day, String breakfast, String lunch, String dinner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEEK, week);
        values.put(COL_DAY, day);
        values.put(COL_BREAKFAST, breakfast);
        values.put(COL_LUNCH, lunch);
        values.put(COL_DINNER, dinner);
        return db.insert(TABLENAME, null, values);
    }

    // Update existing meal
    public boolean updateMeal(int id, int week, String day, String breakfast, String lunch, String dinner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_WEEK, week);
        values.put(COL_DAY, day);
        values.put(COL_BREAKFAST, breakfast);
        values.put(COL_LUNCH, lunch);
        values.put(COL_DINNER, dinner);

        int result = db.update(TABLENAME, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Delete a meal
    public void deleteMeal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLENAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Fetch all meals for DisplayData Activity
    public Cursor getAllMeals() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLENAME + " ORDER BY " + COL_WEEK + " DESC", null);
    }
}