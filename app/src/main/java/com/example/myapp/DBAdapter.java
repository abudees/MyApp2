package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {


    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context ctx)
    {
        this.c=ctx;
        helper=new DBHelper(c);
    }

    //OPEN DB
    public DBAdapter openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    //CLOSE
    public void close()
    {
        try
        {
            helper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    //INSERT DATA TO DB
    public long add(String name,String pos)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Contants.NAME,name);
            cv.put(Contants.POSITION, pos);

            return db.insert(Contants.TB_NAME,Contants.ROW_ID,cv);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    //RETRIEVE ALL PLAYERS
    public Cursor getAllPlayers()
    {
        String[] columns={Contants.ROW_ID,Contants.NAME,Contants.POSITION};

        return db.query(Contants.TB_NAME,columns,null,null,null,null,null);
    }

    //UPDATE
    public long UPDATE(int id,String name,String pos)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Contants.NAME,name);
            cv.put(Contants.POSITION, pos);

            return db.update(Contants.TB_NAME,cv,Contants.ROW_ID+" =?",new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    //DELETE
    public long Delete(int id)
    {
        try
        {

            return db.delete(Contants.TB_NAME,Contants.ROW_ID+" =?",new String[]{String.valueOf(id)});

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

}
