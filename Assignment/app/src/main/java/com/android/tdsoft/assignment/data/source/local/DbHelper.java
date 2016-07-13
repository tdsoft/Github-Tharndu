package com.android.tdsoft.assignment.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.tdsoft.assignment.data.Material;
import com.android.tdsoft.assignment.data.MaterialProperty;

/**
 * Created by Admin on 5/26/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "inventory.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Material.CREATE);
        db.execSQL(MaterialProperty.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Material.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MaterialProperty.TABLE_NAME);
        onCreate(db);
    }
}
