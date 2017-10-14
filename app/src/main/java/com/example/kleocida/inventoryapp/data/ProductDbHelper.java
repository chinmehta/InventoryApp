package com.example.kleocida.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kleocida.inventoryapp.data.ProductContract.InventoryEntry;

/* DATABASE HELPER FOR INVENTORY DATABASE */

public class ProductDbHelper extends SQLiteOpenHelper
{
    /* DATABASE NAME */
    public static final int DATABASE_VERSION = 1;

    /* DATABASE VERSION */
    public static final String DATABASE_NAME = "inventory.db";

    public ProductDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /* CREATE TABLE */
        /* CREATE STRING FOR
            THE SQL STATEMENT */
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + InventoryEntry.TABLE_NAME + " ("
                        + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + InventoryEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                        + InventoryEntry.CURRENT_QUANTITY + " INTEGER DEFAULT 0, "
                        + InventoryEntry.PRODUCT_PRICE + " INTEGER NOT NULL, "
                        + InventoryEntry.PRODUCT_IMAGE + " BLOB, "
                        + InventoryEntry.PRODUCT_SUPNAME + " TEXT,"
                        + InventoryEntry.PRODUCT_SUPMAIL + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}

