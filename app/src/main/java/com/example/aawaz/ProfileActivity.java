package com.example.aawaz;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.aawaz.Database.PhoneContract;
import com.example.aawaz.Database.PhoneDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.detail_id)
    TextView id_tv;

    @BindView(R.id.detail_name)
    TextView name_tv;

    @BindView(R.id.detail_phone)
    TextView phone_tv;

    @BindView(R.id.detail_address)
    TextView address_tv;

    @BindView(R.id.detail_addhar_no)
    TextView aadhar_tv;

    @BindView(R.id.detail_addhar_front)
    ImageView aadharImage;

    @BindView(R.id.r1_name_tv)
    TextView r1_name_tv;

    @BindView(R.id.r1_phone_tv)
    TextView r1_phone_tv;

    @BindView(R.id.r2_name_tv)
    TextView r2_name_tv;

    @BindView(R.id.r2_phone_tv)
    TextView r2_phone_tv;

    private ProgressDialog loading;
    private Context context=this;
    private ArrayList<ItemData> customersList;
    private String mUserId=null;
    private ItemData data=new ItemData();
    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getItems();


    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://script.google.com/macros/s/AKfycbwVRQ4dKFiARGl88DyX2mj0xigyUUvL7ItZFLi-6S2A8KPZBNo/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        data=parseItems(response);
                        id_tv.setText(data.getUserId());
                        name_tv.setText(data.getName());
                        phone_tv.setText(data.getPhone());
                        address_tv.setText(data.getAddress());
                        aadhar_tv.setText(data.getAadhar());

                        Glide.with(context)
                                .load(data.getAadharImage())
                                .placeholder(R.drawable.id_card)
                                .circleCrop()
                                .into(aadharImage);

                        Glide.with(context)
                                .load(data.getPhoto())
                                .placeholder(R.drawable.id_card)
                                .circleCrop()
                                .into(avatar);
                        r1_name_tv.setText(data.getRelative1());
                        r1_phone_tv.setText(data.getR1phone());

                        r2_name_tv.setText(data.getRelative2());
                        r2_phone_tv.setText(data.getR2phone());

                        loading.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error in data loading",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private ItemData parseItems(String jsonResposnce) {


        customersList = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");
            mUserId=getUserKey();
            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                if(mUserId!=null && mUserId.equals(jo.getString("userId").trim()))
                {
                    String mName = jo.getString("name");
                    String mAddress = jo.getString("address");
                    String mAddhar = jo.getString("aadhar").trim();
                    String mMobile = jo.getString("phone").trim();

                    String mImageUrl = jo.getString("photo").trim();
                    String mAddharFront = jo.getString("aadharImage").trim();
                    String mRelative1= jo.getString("relative1").trim();
                    String mRelative1Phone=jo.getString("r1phone").trim();
                    String mRelative2=jo.getString("relative2").trim();
                    String mRelative2Phone=jo.getString("r2phone").trim();

                    ItemData itemData=new ItemData(mUserId,mName,mMobile,mImageUrl,mRelative1,mRelative1Phone,
                            mRelative2,mRelative2Phone,mAddhar,mAddharFront,mAddress);
                    return itemData;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getUserKey(){

        String key=null;
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.query(PhoneContract.ItemEntry.LOCAL_TABLE_NAME,null
                ,null,null,null,null,null);

        if(cursor.moveToFirst()) {
            int userIdColumnIndex = cursor.getColumnIndex(PhoneContract.ItemEntry.COLUMN_USER_ID);
            key=cursor.getString(userIdColumnIndex);
        }
        cursor.close();
        return key;
    }

}