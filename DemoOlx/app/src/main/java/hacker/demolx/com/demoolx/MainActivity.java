package hacker.demolx.com.demoolx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ExpandebleApdater;

public class MainActivity extends AppCompatActivity   {
    private EditText description,price;
    private Spinner location,category;

    private ImageView  mimage;
    ExpandebleApdater listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> mlist1;
    private File path;
    private String[] state= {"Delhi","Assam","Rajasthan","Bihar","Haryana","Himachal Pradesh", "Jammu and Kashmir", "Jharkhand","Karnataka", "Kerala","Tamil Nadu"};
    List<String> mobile,car,elctronic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mimage=(ImageView)findViewById(R.id.postimage);
        description=(EditText)findViewById(R.id.decription);
        price=(EditText)findViewById(R.id.editprice);
        location=(Spinner)findViewById(R.id.spinner);
        category=(Spinner)findViewById(R.id.spinner2);
        mobile = new ArrayList<String>();
        car= new ArrayList<String>();
        elctronic= new ArrayList<String>();

        path = new File(getExternalCacheDir().getAbsolutePath()
                + "/picolx");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        location.setAdapter(adapter_state);

        prepareListData();
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listDataHeader);
        adapter_state1
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        category.setAdapter(adapter_state1);
        // setting list adapter


        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_Picker_Dialog();
            }
        });


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.customdialog);


                ListView mlist2= (ListView)dialog.findViewById(R.id.mlist);

                if(listDataHeader.get(position).equals("Mobiles")){
                    mlist1=mobile;
                }else if(listDataHeader.get(position).equals("Cars")){
                     mlist1=car;
                }else{
                     mlist1=elctronic;
                }
                ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, mlist1);
                adapter_state2
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mlist2.setAdapter(adapter_state2);
                // set the custom dialog components - text, image and
                // button
                mlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub

                        category.setPrompt(mlist1.get(arg2));
dialog.dismiss();

                    }

                });


                dialog.show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location.setSelection(position);
                String selState = (String) location.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void Image_Picker_Dialog() {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Pictures Option");
        myAlertDialog.setMessage("Select Picture Mode");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                        pickPhoto.setType("image/*");
                        // pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT,
                        // Uri.fromFile(path));
                        startActivityForResult(pickPhoto, 0);
                        //
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent takePicture = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(path));
                        startActivityForResult(takePicture, 1);

                    }
                });
        myAlertDialog.show();

    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    path = new File(getRealPathFromURI(this, selectedImage));
                    // Image_Selecting_Task(data);
                    InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                        Bitmap mbit = null;
                        mbit = BitmapFactory.decodeStream(imageStream);
                        //
                        mimage.setImageBitmap(mbit);
                        // mProfilePicture.setImageBitmap(mbit);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Tag_location.getInstance().setBitmap(mbit);
                    // Tag_location.getInstance().setByteArray(getByteArray(mbit));
                    // mTagImage.setImageURI(selectedImage);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {

                    // Uri selectedImage1 = data.getData();
                    Bitmap mbit = null;

                    // Image_Selecting_Task(data);
                    mbit = BitmapFactory.decodeFile(path.getAbsolutePath());
                    // mProfilePicture.setImageBitmap(mbit);
                    mimage.setImageBitmap(mbit);
                    // Tag_location.getInstance().setByteArray(getByteArray(mbit));

                }
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Mobiles");
        listDataHeader.add("Cars");
        listDataHeader.add("Electronics");

        // Adding child data

        mobile.add("Window");
        mobile.add("Nokia");
        mobile.add("Iphone");
        mobile.add("Blackberry");
        mobile.add("Android");








        car.add("Fiat");
        car.add("Honda");
        car.add("Maruti");
        car.add("Hyundai");
        car.add("Volkswagon");
        car.add("Skoda");


        elctronic.add(" Air Conditioner");
        elctronic.add("TV");
        elctronic.add("Refrigerator");
        elctronic.add("Camera");
        elctronic.add("Washing Machine");


    }
}

