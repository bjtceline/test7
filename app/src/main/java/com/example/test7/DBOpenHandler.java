package com.example.test7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHandler extends SQLiteOpenHelper {
    /**
     * @param context 当前应用上下文
     */
    public DBOpenHandler(Context context) {
        super(context, "myAndroid.db", null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_createTable = "CREATE TABLE person (personid integer primary key autoincrement, name)";
        db.execSQL(sql_createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "ALTER TABLE person ADD phone";
        db.execSQL(sql);
        db.execSQL("ALTER TABLE person ADD amount integer");
    }
}