package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "CartDB";

    private static final String TABLE_CART = "Cart";
    private static final String TABLE_PREORDER = "PreOrder";

    private static final String COLUMN_ID = "cartId";
    private static final String COLUMN_PID = "productId";
    private static final String COLUMN_QTY = "qtySelected";



    private static final String COLUMN_RNAME = "recipientName";
    private static final String COLUMN_RMOBILE = "recipientMobile";
    private static final String COLUMN_LOCATION = "deliveryLocation";
    private static final String COLUMN_MSG = "message";
    private static final String COLUMN_VOUCHER = "discountVoucher";




    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PID + " INTEGER NOT NULL UNIQUE,"
                + COLUMN_QTY + " INTEGER NOT NULL"
                + ")";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_PREORDER_TABLE = "CREATE TABLE " + TABLE_PREORDER
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RNAME + " NCHAR NOT NULL,"
                + COLUMN_RMOBILE + " INTEGER NOT NULL UNIQUE,"
                + COLUMN_LOCATION + " NCHAR NOT NULL UNIQUE,"
                + COLUMN_MSG + " NCHAR NOT NULL UNIQUE,"
                + COLUMN_VOUCHER + " NCHAR NOT NULL UNIQUE"
                + ")";
        db.execSQL(CREATE_PREORDER_TABLE);
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


                storeCart.add(new Products(id, pId,qty));

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }


    ArrayList<Integer> listProducts() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(0));
                int pId = cursor.getInt((1));

                storeCart.add(pId);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }

    ArrayList<Integer> listQty() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(0));
                int pId = cursor.getInt((1));
                int qty = cursor.getInt((2));

                storeCart.add(qty);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }




    int getQty(int pID) {
        String sql = "select * from " + TABLE_CART + " where " + COLUMN_PID + " = "+ pID;
        SQLiteDatabase db = this.getReadableDatabase();
        int productQty = 0;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(0));
                int pId = cursor.getInt((1));
                int qty = cursor.getInt((2));

                productQty = qty;
            }
            while (cursor.moveToNext());
        }
        if (cursor.getCount() > 0) {
            return productQty;
        } else {
            cursor.close();
            return 0;
        }
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
        }
    }




    void addProduct(Products product) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_PID, product.getProductId());
        values.put(COLUMN_QTY, 1);


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CART, null, values);
    }



    void updateQty(int pid,  int qty) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE "+TABLE_CART+ " SET "+ COLUMN_QTY +"="+ qty + " WHERE "+ COLUMN_PID +" = " +  pid );

    }

    void deleteProduct(int pid) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+TABLE_CART+" WHERE "+COLUMN_PID+" = "+pid);


    }

    void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ TABLE_CART);
       // db.delete(TABLE_CART,);
    }

    public int sumPriceCartItems(List<Integer> price) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i =0; i < price.size(); i++) {

            Log.d("prices: ",  String.valueOf(price.get(i)));

            Cursor cur = db.rawQuery("SELECT SUM(" + COLUMN_QTY + " * " + price.get(i) + ") FROM " + TABLE_CART, null);
            if (cur.moveToFirst()) {
                result = cur.getInt(0);
            }

            cur.close();

        }
        return result;
    }
}
