package com.example.hometask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dab.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "Contacts";

    public static final String KEY_ID = "ID";
    public static final String KEY_SURNAME = "SURNAME";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_PATRONYMIC = "PATRONYMIC";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_DATA = "DATA";

    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, SURNAME TEXT, NAME TEXT, PATRONYMIC TEXT, POSITION TEXT, DATA TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
