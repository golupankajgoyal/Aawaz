package com.example.aawaz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aawaz.Database.PhoneContract;
import com.example.aawaz.Database.PhoneDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SmsListActivity extends AppCompatActivity {


    private static final int PERMISSIONS_REQUEST =1 ;
    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);
    private ProgrammingAdapter mAdapter;
    private RecyclerView programList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        programList =findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        programList.setLayoutManager(layoutManager);
        programList.setHasFixedSize(true);
        mAdapter=new ProgrammingAdapter(getContactList(),this);
        programList.setAdapter(mAdapter);

        //Set click listener for FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmsListActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permission == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    PERMISSIONS_REQUEST);
        }
    }


    private ArrayList<String> getContactList(){

        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        ArrayList<String> contactList=new ArrayList<>();
        Cursor cursor=db.query(PhoneContract.ItemEntry.TABLE_NAME,null
                ,null,null,null,null,null);

        if(cursor.moveToFirst()) {

            int phoneColumnIndex=cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_PHONE_NO);
            do {
                String phonNo = cursor.getString(phoneColumnIndex);
                contactList.add(phonNo);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return contactList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sms_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete_all_entries){
            deleteAllItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllItems() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted =db.delete(PhoneContract.ItemEntry.TABLE_NAME,null,null);
        Toast.makeText(this,""+rowsDeleted+" Phone No. are Deleted", Toast.LENGTH_SHORT).show();
        ArrayList<String> newData=getContactList();
        mAdapter.getUpdated(newData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> newData=getContactList();
        mAdapter.getUpdated(newData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            Toast.makeText(this,"Now you can send Sms through our app",Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }
}
