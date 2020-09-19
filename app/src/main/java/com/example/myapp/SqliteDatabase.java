package com.example.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Cart";
    private static final String TABLE_CART = "Cart";
    private static final String COLUMN_ID = "productId";
    private static final String COLUMN_QTY = "qtySelected";


    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_QTY + " INTEGER" + ")";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("SELECT  " + TABLE_CART);
        onCreate(db);
    }

    ArrayList<Products> listProducts() {
        String sql = "select * from " + TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Products> storeCart = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                int qty = cursor.getInt(1);
                storeCart.add(new Products(id,qty));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeCart;
    }

    void addProduct(Products products) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, products.getProductId());
        values.put(COLUMN_QTY, products.getQty());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CART, null, values);
    }

    void updateProduct(Products products) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, products.getProductId());
        values.put(COLUMN_QTY, products.getQty());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CART, values, COLUMN_ID + " = ?", new String[]{String.valueOf(products.getId())});
    }

    void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }


}