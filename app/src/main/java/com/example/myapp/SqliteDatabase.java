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
    private static final String TABLE_CART = "Cart";
    private static final String COLUMN_ID = "cartId";
    private static final String COLUMN_PID = "productId";
    private static final String COLUMN_QTY = "qtySelected";


    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PID + " INTEGER ,"
                + COLUMN_QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("SELECT  " + TABLE_CART);
        onCreate(db);
    }

    ArrayList<Products> listAll() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Products> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                int pId = cursor.getInt((1));
                int qty = cursor.getInt((2));

                storeCart.add(new Products(id, pId, qty));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }


    ArrayList<Integer> listProducts() {
        String sql = "select "+ COLUMN_PID +" from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                int pId = cursor.getInt((1));

                storeCart.add(pId);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }



    int getQty(int pID) {
        String sql = "select "+ COLUMN_QTY + " from " + TABLE_CART + " where " + COLUMN_PID + " = "+ pID;
        SQLiteDatabase db = this.getReadableDatabase();
        int productQty = 0;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int qty = cursor.getInt((2));
                productQty = qty;
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




    void addProduct(Products product) {
        ContentValues values = new ContentValues();//

        values.put(COLUMN_PID, product.getProductId());
        values.put(COLUMN_QTY, product.getQty());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CART, null, values);
    }



    void addQty(int pid,  int qty) {

        int productID =pid;

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE TABLE_CART SET YOUR_COLUMN="+ qty + " WHERE "+COLUMN_PID+"="+pid);

    }

    void deleteQty(int pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (getQty(pid) == 1 ){



            db.execSQL("delete from "+TABLE_CART+" WHERE "+COLUMN_PID+"="+pid);
        } else {
            addQty(pid,getQty(pid)-1);
        }

    }

    void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null,null);
    }
}
