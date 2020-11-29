package com.example.aawaz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aawaz.Database.PhoneContract;
import com.example.aawaz.Database.PhoneDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestingActivity extends AppCompatActivity {

    @BindView(R.id.testing_tv)
    TextView tv;

    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        ButterKnife.bind(this);
        showInfo();
    }

    private void showInfo(){

        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.query(PhoneContract.ItemEntry.LOCAL_TABLE_NAME,null
                ,null,null,null,null,null);

        if(cursor.moveToFirst()) {

            int phoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_CONTACT_NO);
            int userIdColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_USER_ID);
            int r1PhoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_RELATIVE_1);
            int r2PhoneColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_RELATIVE_2);
            String phonNo = cursor.getString(phoneColumnIndex);
            String userId = cursor.getString(userIdColumnIndex);
            String r1 = cursor.getString(r1PhoneColumnIndex);
            String r2 = cursor.getString(r2PhoneColumnIndex);
            tv.setText(phonNo + ", " + userId + ", "+ r1 +  "," + r2);

        }
        cursor.close();
    }
}