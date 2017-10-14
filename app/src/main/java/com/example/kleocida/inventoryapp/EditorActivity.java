package com.example.kleocida.inventoryapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kleocida.inventoryapp.data.ProductContract.InventoryEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final int SELECT_PHOTO = 100;
    private static final int EXISTING_URI = 0;
    ImageView inventoryImage;
    ImageButton gallery;
    private Bitmap inventory_image;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupnameEditText;
    private EditText mSupmailEditText;

    private Uri inventoryUri;

    private boolean mInventoryHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        inventoryUri = getIntent().getData();

        if (inventoryUri == null)
        {
            setTitle(getString(R.string.add_product));
            invalidateOptionsMenu();
        } else
            {
            setTitle(getString(R.string.edit_product));
            getSupportLoaderManager().initLoader(EXISTING_URI, null, this);
        }


        inventoryImage = (ImageView) findViewById(R.id.product_image);
        mQuantityEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mSupmailEditText = (EditText) findViewById(R.id.edit_product_supmail);
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mSupnameEditText = (EditText) findViewById(R.id.edit_product_supname);

        FloatingActionButton gallery = (FloatingActionButton) findViewById(R.id.add_image);

        mNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupmailEditText.setOnTouchListener(mTouchListener);
        mSupnameEditText.setOnTouchListener(mTouchListener);

        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                trySelector();
            }
        });

    }



    private void openSelector()
    {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType(getString(R.string.intentType));
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectPicture)), 0);
    }
    public void trySelector()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        openSelector();
    }


    // SELECT_PHOTO:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();

            try {
                inventory_image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            inventoryImage.setImageBitmap(inventory_image);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelector();
                }
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (inventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:

                String stringName = mNameEditText.getText().toString().trim();
                String stringPrice = mPriceEditText.getText().toString().trim();
                String stringQuantity = mQuantityEditText.getText().toString().trim();
                String stringSupname = mSupnameEditText.getText().toString().trim();
                String stringSupmail = mSupmailEditText.getText().toString().trim();

                byte[] img = getBytes(inventory_image);
                Boolean condition1=Boolean.FALSE,condition2=Boolean.FALSE;

                if (TextUtils.isEmpty(stringName) || TextUtils.isEmpty(stringPrice) || TextUtils.isEmpty(stringQuantity) ||
                        TextUtils.isEmpty(stringSupmail) ||TextUtils.isEmpty(stringSupname) ) {
                    showToast(getString(R.string.add_allinfo));
                     condition1=Boolean.FALSE;
                }
                else condition1=Boolean.TRUE;
                if ((img == null)&& (inventoryUri == null))  {
                    showToast(getString(R.string.selectPicture));
                     condition2=Boolean.FALSE;
                }
                else condition2=Boolean.TRUE;

                if((condition1==Boolean.TRUE)&&condition2==Boolean.TRUE) {
                    saveProduct();
                    finish();
                }
                return true;

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                NavUtils.navigateUpFromSameTask(EditorActivity.this);

                            }
                        };

                showUnsavedChangedDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog()
    {

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

    private void deleteProduct()
    {

        if (inventoryUri != null) {
            int rowDeleted = getContentResolver().delete(inventoryUri, null, null);

            if (rowDeleted == 0) {
                showToast(getString(R.string.delete_failed));

            } else {
                showToast(getString(R.string.delete_successful));

            }

            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                };

        showUnsavedChangedDialog(discardButtonClickListener);
    }

    private void showUnsavedChangedDialog(DialogInterface.OnClickListener discardButtonClickListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.discard_changes));
        builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveProduct()
    {

        String stringName = mNameEditText.getText().toString().trim();
        String stringPrice = mPriceEditText.getText().toString().trim();
        String stringQuantity = mQuantityEditText.getText().toString().trim();
        String stringSupname = mSupnameEditText.getText().toString().trim();
        String stringSupmail = mSupmailEditText.getText().toString().trim();

        byte[] img = getBytes(inventory_image);

        if (inventoryUri == null &&
                TextUtils.isEmpty(stringName) && TextUtils.isEmpty(stringPrice)
                && TextUtils.isEmpty(stringQuantity) && TextUtils.isEmpty(stringSupname) && TextUtils.isEmpty(stringSupmail)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.PRODUCT_NAME, stringName);
        values.put(InventoryEntry.PRODUCT_PRICE, stringPrice);
        values.put(InventoryEntry.CURRENT_QUANTITY, stringQuantity);
        values.put(InventoryEntry.PRODUCT_SUPNAME, stringSupname);
        values.put(InventoryEntry.PRODUCT_SUPMAIL, stringSupmail);

        if (img != null) {
            values.put(InventoryEntry.PRODUCT_IMAGE, img);

        }

        if (inventoryUri == null) {

            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                showToast(getString(R.string.data_insert_failed));
            } else {
                showToast(getString(R.string.data_insert_successful));
            }
        } else {

            int rowsAffected = getContentResolver().update(inventoryUri, values, null, null);

            if (rowsAffected == 0) {
                showToast(getString(R.string.data_update_failed));
            } else {
                showToast(getString(R.string.data_update_successful));
            }
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
                String supname = data.getString(intSupname);
                String supmail = data.getString(intSupmail);
                int quantity = data.getInt(intQuantity);
                byte[] b = data.getBlob(intImage);

                if (b == null) {
                    inventoryImage.setImageResource(R.drawable.no_img);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                    inventoryImage.setImageBitmap(image);

                }
                mNameEditText.setText(name);
                mPriceEditText.setText(String.valueOf(price));
                mSupnameEditText.setText(supname);
                mSupmailEditText.setText(supmail);
                mQuantityEditText.setText(String.valueOf(quantity));
            }
            while (data.moveToNext());
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        inventoryImage.setImageResource(R.drawable.no_img);
        mNameEditText.setText("");
        mSupnameEditText.setText("");
        mSupmailEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
    }
    private void showToast(String string)
    {
        Toast.makeText(EditorActivity.this, string, Toast.LENGTH_LONG).show();
    }
}
