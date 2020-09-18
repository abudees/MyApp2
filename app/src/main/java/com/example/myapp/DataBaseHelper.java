package com.example.myapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cardsdatabase";
    private static final String TABLE_NAME = "CARDSTABLE";
    private static final String UID = "_id";
    private static final String NAME = "Name";
    private static final String CARD = "Card";
    private static final String CODE = "Code";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " (" + UID
            + " integer primary key autoincrement, " + NAME
            + " text not null, " + CARD + " ext not null, " + CODE
            + " text not null);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    private Context context;

    public DataBaseHelper(Context context) {

        super(context, DataBaseHelper.DATABASE_NAME, null, DataBaseHelper.DATABASE_VERSION);
        this.context = context;
        Toast toast = Toast.makeText(context, "constructor called", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast toast = Toast.makeText(context, "onCreate called", Toast.LENGTH_LONG);
            toast.show();

        } catch (SQLException e) {
            Toast toast = Toast.makeText(context, "" + e, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
