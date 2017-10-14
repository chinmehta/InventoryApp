package com.example.kleocida.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;



public final class ProductContract
{

    private ProductContract()
    {
        throw new AssertionError("No ProductContract Instances are there for you!!");
    }
    /* TABLE OWNER APP */
    public static final String CONTENT_AUTHORITY = "com.example.kleocida.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventoryapp";

    /* DATA FOR TABLE USE */
    public static final class InventoryEntry implements BaseColumns
    {

        /* URI FOR TABLE */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        /* URI FOR SELECTED ROWS */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                        CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        /* URI TO REACH TABLE */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        /* TABLE NAME */
        public static final String TABLE_NAME = "inventory";

        /* EACH COLUMN IN TABLE */
        public static final String _ID = BaseColumns._ID;
        public static final String PRODUCT_NAME = "name";
        public static final String PRODUCT_PRICE = "price";
        public static final String CURRENT_QUANTITY = "quantity";
        public static final String PRODUCT_SUPMAIL = "supmail";
        public static final String PRODUCT_IMAGE = "image";
        public static final String PRODUCT_SUPNAME = "supname";


    }

}
