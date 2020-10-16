package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "CartDB";
    private static final String TABLE_CART = "CartDB";
    private static final String COLUMN_PID = "productId";
    private static final String COLUMN_QTY = "qtySelected";


    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART
                + "(" + COLUMN_PID + " INTEGER PRIMARY KEY ,"
                + COLUMN_QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("SELECT  " + TABLE_CART);
        onCreate(db);
    }

    ArrayList<Integer> listAll() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> listpIDs = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int pId = cursor.getInt((1));
                int qty = cursor.getInt((2));

                listpIDs.add(id);
                listpIDs.add(pId);
                listpIDs.add(qty);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return listpIDs;
    }


    ArrayList<Integer> listPID() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                int pId = cursor.getInt((1));



                storeCart.add(new Integer( pId));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }



    int getQty(int pID) {
        String sql = "select "+ COLUMN_QTY + " from " + TABLE_CART + " where " + COLUMN_PID + " = "+ pID;
        SQLiteDatabase db = this.getReadableDatabase();
        int productQty ;
        productQty = 0;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                 productQty = cursor.getInt((2));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return productQty;
    }

    boolean checkProduct(int pID) {
        String sql = "select  *  from " + TABLE_CART + " where " + COLUMN_PID + " = " + pID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            cursor.close();
            return false;
        }    }




    void addProduct(int productID, int qty) {
        ContentValues values = new ContentValues();//

        values.put(COLUMN_PID, productID);
        values.put(COLUMN_QTY, qty);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CART, null, values);
    }



    void updateQty(int pid,  int qty) {



        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_CART +"SET" + COLUMN_QTY +"="+ qty + " WHERE "+ COLUMN_PID +"="+pid);

    }
/*
    void deleteQty(int pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (getQty(pid) == 1 ){

        } else {
            updateQty(pid,getQty(pid)-1);
        }

    }
*/
    void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null,null);
    }
}
