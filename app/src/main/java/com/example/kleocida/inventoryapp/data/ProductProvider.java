package com.example.kleocida.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kleocida.inventoryapp.data.ProductContract.InventoryEntry;

/**
 * Created by kleocida on 07/06/17.
 */

/* CONTENT PROVIDER FOR UNIVERSAL INVENTORY */
public class ProductProvider extends ContentProvider {
    /* TAG FOR LOG MESSAGE */
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();
    /* URI MATCHER FOR THE INVENTORY TABLE */
    private static final int INVENTORY = 100;
    /* URI MATCHER FOR A SINGLE ITEM FROM INVENTORY TABLE */
    private static final int INVENTORY_ID = 101;
    /* URI MATCHER TO SELECT CORRECT URI */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /* ADD URIS TO URI MATCHER */
    static {
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_INVENTORY + "/#", INVENTORY_ID);
    }
    /* DATABASE HELPER OBJECT */
    private ProductDbHelper mDbHelper;
    /* INITIALIZE THE DATABASE HELPER */
    @Override
    public boolean onCreate() {

        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }
    /* THE QUERY METHOD FOR URI */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
/* GET READABLE DATABASE */
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        /* CURSOR FOR QUERY RESULT */
        Cursor cursor;

          /* SELECT THE CORRECT CASE BASED ON URI MATCHER */
        int match = sUriMatcher.match(uri);

        switch (match) {
            case INVENTORY:
                /* QUERY TABLE */
                cursor = db.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            /* INVALID URI */
            default:
                throw new IllegalArgumentException("Cannot Resolve URI " + uri);
        }
  /* HANDLE TABLE CHANGE */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INVENTORY:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + "with match " + match);
        }

    }
    /* INSERT NEW DATA IN TABLE */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertInventory(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }
    /* METHOD TO INSERT DATA */
    private Uri insertInventory(Uri uri, ContentValues values) {
        String productName = values.getAsString(InventoryEntry.PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("Item requires a name");
        }
        String productSupname = values.getAsString(InventoryEntry.PRODUCT_SUPNAME);
        if (productSupname == null) {
            throw new IllegalArgumentException("Item requires a supplier name");
        }
        String productSupmail = values.getAsString(InventoryEntry.PRODUCT_SUPMAIL);
        if (productSupmail == null) {
            throw new IllegalArgumentException("Item requires a supplier email");
        }

        Integer productQuantity = values.getAsInteger(InventoryEntry.CURRENT_QUANTITY);
        if (productQuantity != null && productQuantity < 0) {
            throw new IllegalArgumentException("Product requires a valid weight");
        }

        Integer productPrice = values.getAsInteger(InventoryEntry.PRODUCT_PRICE);
        if (productPrice != null && productPrice < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(InventoryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    /* DELETE TABLE DATA OR SELECTED ITEM BASED ON SELECTION AND SELECTION ARGS */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INVENTORY:
                rowsDeleted = db.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = db.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + "with match " + match);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }
    /* UPDATE TABLE OR SELECTED ITEM BASED ON SELECTION AD SELECTION RGS */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case INVENTORY:
                return updateInventory(uri, values, selection, selectionArgs);

            case INVENTORY_ID:

                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return updateInventory(uri, values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }
    /* UPDATE SELECTED ITEM METHOD */
    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String productName = values.getAsString(InventoryEntry.PRODUCT_NAME);
        if (productName == null) {
            throw new IllegalArgumentException("Item requires a name");
        }
        String productSupname = values.getAsString(InventoryEntry.PRODUCT_SUPNAME);
        if (productSupname == null) {
            throw new IllegalArgumentException("Item requires a supplier name");
        }
        String productSupmail = values.getAsString(InventoryEntry.PRODUCT_SUPMAIL);

        if (productSupmail == null) {
            throw new IllegalArgumentException("Item requires a supplier email");
        }

        Integer productQuantity = values.getAsInteger(InventoryEntry.CURRENT_QUANTITY);
        if (productQuantity != null && productQuantity < 0) {
            throw new IllegalArgumentException("Product requires a valid weight");
        }

        Integer productPrice = values.getAsInteger(InventoryEntry.PRODUCT_PRICE);
        if (productPrice != null && productPrice < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }
}
