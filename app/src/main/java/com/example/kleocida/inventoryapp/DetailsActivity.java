package com.example.kleocida.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kleocida.inventoryapp.data.ProductContract.InventoryEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{


    private Uri inventoryUri;
    private static final int EXISTING_INVENTORY_LOADER = 0;

    ImageView mProductImage;
    TextView mProductName;
    TextView mProductPrice;
    TextView productQuantity;
    TextView productSupname;
    TextView productSupmail;
    String supname;
    String supmail;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(getString(R.string.product_details));

        mProductImage = (ImageView) findViewById(R.id.details_product_image);
        mProductName = (TextView) findViewById(R.id.details_product_name);
        mProductPrice = (TextView) findViewById(R.id.details_product_price);
        productQuantity = (TextView) findViewById(R.id.details_product_quantity);
        productSupname = (TextView) findViewById(R.id.details_product_supname);
        productSupmail = (TextView) findViewById(R.id.details_product_supmail);


        inventoryUri = getIntent().getData();
        FloatingActionButton sellButton = (FloatingActionButton) findViewById(R.id.sell_button);
        FloatingActionButton shipmentButton = (FloatingActionButton) findViewById(R.id.shipment_button);


        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                final EditText edittext = new EditText(v.getContext());
                edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setMessage(getString(R.string.selling_quantity));
                builder.setView(edittext);

                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int subtract;
                        if (TextUtils.isEmpty(edittext.getText().toString().trim())) {
                            subtract = 0;
                        } else {
                            subtract = Integer.parseInt(edittext.getText().toString().trim());
                        }

                        String[] projection =
                                {
                                        InventoryEntry._ID,
                                        InventoryEntry.PRODUCT_NAME,
                                        InventoryEntry.PRODUCT_PRICE,
                                        InventoryEntry.CURRENT_QUANTITY,
                                        InventoryEntry.PRODUCT_SUPNAME,
                                        InventoryEntry.PRODUCT_SUPMAIL,
                                        InventoryEntry.PRODUCT_IMAGE
                                };

                        Cursor cursor = getContentResolver().query(inventoryUri, projection, null, null, null);

                        if (cursor.moveToFirst()) {
                            do {
                                int intName = cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME);
                                int intPrice = cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE);
                                int intQuantity = cursor.getColumnIndex(InventoryEntry.CURRENT_QUANTITY);
                                int intSupname = cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPNAME);
                                int intSupmail = cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPMAIL);


                                String name = cursor.getString(intName);
                                int price = cursor.getInt(intPrice);
                                supname = cursor.getString(intSupname);
                                supmail = cursor.getString(intSupmail);
                                int quantity = cursor.getInt(intQuantity);


                                if (quantity - subtract >= 0) {
                                    int current = (quantity - subtract);

                                    ContentValues values2 = new ContentValues();
                                    values2.put(InventoryEntry.PRODUCT_NAME, name);
                                    values2.put(InventoryEntry.PRODUCT_PRICE, price);
                                    values2.put(InventoryEntry.PRODUCT_SUPNAME, supname);
                                    values2.put(InventoryEntry.PRODUCT_SUPMAIL, supmail);
                                    values2.put(InventoryEntry.CURRENT_QUANTITY, current);

                                    int rowsAffected = getContentResolver().update(inventoryUri, values2, null, null);
                                } else if (quantity - subtract < 0) {
                                    Toast.makeText(DetailsActivity.this, "Wrong input only " + quantity + " products available !", Toast.LENGTH_SHORT).show();
                                }

                            }
                            while (cursor.moveToNext());
                        }


                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        shipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                final EditText edittext1 = new EditText(v.getContext());
                edittext1.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setMessage(getString(R.string.received_shipment));
                builder.setView(edittext1);

                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int addition;
                        if (TextUtils.isEmpty(edittext1.getText().toString().trim())) {
                            addition = 0;
                        } else {
                            addition = Integer.parseInt(edittext1.getText().toString().trim());
                        }

                        String[] projection =
                                {
                                        InventoryEntry._ID,
                                        InventoryEntry.PRODUCT_NAME,
                                        InventoryEntry.PRODUCT_PRICE,
                                        InventoryEntry.CURRENT_QUANTITY,
                                        InventoryEntry.PRODUCT_SUPNAME,
                                        InventoryEntry.PRODUCT_SUPMAIL,
                                        InventoryEntry.PRODUCT_IMAGE
                                };

                        Cursor cursor = getContentResolver().query(inventoryUri, projection, null, null, null);

                        if (cursor.moveToFirst()) {
                            do {
                                int intName = cursor.getColumnIndex(InventoryEntry.PRODUCT_NAME);
                                int intPrice = cursor.getColumnIndex(InventoryEntry.PRODUCT_PRICE);
                                int intQuantity = cursor.getColumnIndex(InventoryEntry.CURRENT_QUANTITY);
                                int intSupname = cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPNAME);
                                int intSupmail = cursor.getColumnIndex(InventoryEntry.PRODUCT_SUPMAIL);


                                String name = cursor.getString(intName);
                                int price = cursor.getInt(intPrice);
                                supname = cursor.getString(intSupname);
                                supmail = cursor.getString(intSupmail);
                                int quantity = cursor.getInt(intQuantity);

                                int current = (quantity + addition);

                                ContentValues values3 = new ContentValues();
                                values3.put(InventoryEntry.PRODUCT_NAME, name);
                                values3.put(InventoryEntry.PRODUCT_PRICE, price);
                                values3.put(InventoryEntry.PRODUCT_SUPNAME, supname);
                                values3.put(InventoryEntry.PRODUCT_SUPMAIL, supmail);
                                values3.put(InventoryEntry.CURRENT_QUANTITY, current);

                                int rowsAffected = getContentResolver().update(inventoryUri, values3, null, null);

                            }
                            while (cursor.moveToNext());
                        }
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        getSupportLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailsactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_current_product:
                showDeleteConfirmationDialog();
                return true;

            case R.id.edit_current_product:
                Intent intent = new Intent(DetailsActivity.this, EditorActivity.class);
                intent.setData(inventoryUri);
                startActivity(intent);
                return true;

            case R.id.details_order:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.ordering_products));
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{supmail});
                String message = "Dear Product Supplier! \n\nWe would like to place the following order of " + mProductName.getText().toString().trim() + "\n Please let us know when we can expect the delivery.";
                i.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, getString(R.string.send_email)));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_current_product));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {

        if (inventoryUri != null) {
            int rowDeleted = getContentResolver().delete(inventoryUri, null, null);


            if (rowDeleted == 0) {

                Toast.makeText(this, getString(R.string.delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.delete_successful),
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection =
                {
                        InventoryEntry._ID,
                        InventoryEntry.PRODUCT_NAME,
                        InventoryEntry.PRODUCT_PRICE,
                        InventoryEntry.CURRENT_QUANTITY,
                        InventoryEntry.PRODUCT_SUPNAME,
                        InventoryEntry.PRODUCT_SUPMAIL,
                        InventoryEntry.PRODUCT_IMAGE
                };


        return new CursorLoader(this,
                inventoryUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            do {
                int intName = data.getColumnIndex(InventoryEntry.PRODUCT_NAME);
                int intPrice = data.getColumnIndex(InventoryEntry.PRODUCT_PRICE);
                int intQuantity = data.getColumnIndex(InventoryEntry.CURRENT_QUANTITY);
                int intSupname = data.getColumnIndex(InventoryEntry.PRODUCT_SUPNAME);
                int intSupmail = data.getColumnIndex(InventoryEntry.PRODUCT_SUPMAIL);
                int intImage = data.getColumnIndex(InventoryEntry.PRODUCT_IMAGE);

                String name = data.getString(intName);
                int price = data.getInt(intPrice);
                supname = data.getString(intSupname);
                supmail = data.getString(intSupmail);
                int quantity = data.getInt(intQuantity);
                byte[] b = data.getBlob(intImage);

                if (b == null) {
                    mProductImage.setImageResource(R.drawable.no_img);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                    mProductImage.setImageBitmap(image);
                }
                mProductName.setText(name);
                mProductPrice.setText(String.valueOf(price));


                if (TextUtils.isEmpty(supname)) {
                    String suppname = getString(R.string.unknown);
                    productSupname.setText(suppname);
                } else {
                    productSupname.setText(supname);
                }


                if (TextUtils.isEmpty(supmail)) {
                    String supp = getString(R.string.unknown);
                    productSupmail.setText(supp);
                } else {
                    productSupmail.setText(supmail);
                }
                productQuantity.setText(String.valueOf(quantity));
            }
            while (data.moveToNext());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mProductImage.setImageResource(R.drawable.no_img);
        mProductName.setText("");
        mProductPrice.setText("");
        productSupname.setText("");
        productSupmail.setText("");
        productQuantity.setText("");

    }
}
