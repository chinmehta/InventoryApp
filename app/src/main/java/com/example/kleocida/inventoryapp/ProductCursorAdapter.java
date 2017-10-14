package com.example.kleocida.inventoryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kleocida.inventoryapp.data.ProductContract.InventoryEntry;




public class ProductCursorAdapter extends CursorAdapter {

    private CatalogActivity activity = new CatalogActivity();

    public ProductCursorAdapter(CatalogActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        final long id;
        final int mQuantity;
        final String mName;
        final int mPrice;
        final String mSupname;
        final String mSupplier;

        TextView inventoryName = (TextView) view.findViewById(R.id.list_item_name);
        TextView inventoryPrice = (TextView) view.findViewById(R.id.list_item_price);
        TextView inventoryQuantity = (TextView) view.findViewById(R.id.list_item_quantity);
        ImageView inventorySale = (ImageView) view.findViewById(R.id.list_item_button);

        String name = cursor.getString(cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME));
        int price = cursor.getInt(cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE));
        int quantity = cursor.getInt(cursor.getColumnIndex(InventoryEntry.CURRENT_QUANTITY));
        id = cursor.getLong(cursor.getColumnIndex(InventoryEntry._ID));
        String supname = cursor.getString(cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPNAME));
        String supmail = cursor.getString(cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPMAIL));

        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mSupname = supname;
        mSupplier = supmail;

        inventorySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if (mQuantity  > 0) {
                            activity.onSaleClick(id, mName, mPrice, mSupname, mSupplier, mQuantity, 1);
                            Toast.makeText(activity, "Sell successfull", Toast.LENGTH_SHORT).show();

                        } else if (mQuantity  <= 0) {
                            Toast.makeText(activity, "Stock is empty!!", Toast.LENGTH_SHORT).show();
                        }

            }
        });


        inventoryName.setText(name);
        inventoryPrice.setText("Price: " + String.valueOf(price) + " $");
        inventoryQuantity.setText("Quantity: " + String.valueOf(quantity));

    }

}
