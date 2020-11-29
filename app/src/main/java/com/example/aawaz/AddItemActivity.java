package com.example.aawaz;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @BindView(R.id.ed_username)
    TextInputEditText userName;

    @BindView(R.id.ed_phone)
    TextInputEditText phone;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.ed_relative1)
    TextInputEditText r1;

    @BindView(R.id.ed_relative2)
    TextInputEditText r2;

    @BindView(R.id.ed_r1phone)
    TextInputEditText r1Phone;

    @BindView(R.id.ed_r2phone)
    TextInputEditText r2Phone;

    @BindView(R.id.ed_flat_no)
    TextInputEditText houseNo;

    @BindView(R.id.ed_society)
    TextInputEditText societyName;

    @BindView(R.id.ed_street)
    TextInputEditText streetName;

    @BindView(R.id.ed_aadhar)
    TextInputEditText aadhar;

    @BindView(R.id.aadhar)
    ImageView aadharImage;

    @BindView(R.id.addhar_tv)
    TextView addharTv;

    @BindView(R.id.image_tv)
    TextView img_tv;

    private Context context = this;
    private ProgressDialog loading;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int PICK_ADDHAR_FRONT_REQUEST = 3;
    private Uri mImageUri;
    private String mStringImageUrl="";
    private String mStringAddharFrontUrl="";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String stringCustomerId="";
    private String totalCustomers = null;
    private int check=0;
    private Spinner spinner;
    private String mDistrictCode = null;
    private String mDistrictName = null;
    private PhoneDbHelper mDbHelper=new PhoneDbHelper(this);
    private String mR1Phone="nul";
    private String mR2Phone="nul";
    private String mMobile="nul";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        spinner = (Spinner) findViewById(R.id.district_spinner);

        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        img_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(v);
            }
        });

        addharTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(v);
            }
        });
    }

    @OnClick(R.id.txt_save)
    public void onViewClicked() {
        if (validation()) {
            getItems();
        }
    }

    private void getItems() {

        loading = ProgressDialog.show(this, "Adding Item", "Please wait");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://script.google.com/macros/s/AKfycbwVRQ4dKFiARGl88DyX2mj0xigyUUvL7ItZFLi-6S2A8KPZBNo/exec?action=test",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error in data loading", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void parseItems(String jsonResposnce) {


        try {

            JSONObject jobj = new JSONObject(jsonResposnce);
            totalCustomers = jobj.getString("items").trim();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        addItemToSheet();
    }

    public void addItemToSheet() {

        final String mName = userName.getText().toString().trim();
        final String mHouseNo = houseNo.getText().toString().trim();
        final String mSociety = societyName.getText().toString().trim();
        final String mStreetName = streetName.getText().toString().trim();
        final String mAddress = "House No. " + mHouseNo + ", " + mSociety + ", "+mStreetName+", " + mDistrictName
                + ", Meghalaya";
        final String mAddhar = aadhar.getText().toString().trim();

        mMobile = phone.getText().toString().trim();
        final String mR1= r1.getText().toString().trim();
        final String mR2= r2.getText().toString().trim();
        mR1Phone = r1Phone.getText().toString().trim();

        mR2Phone = r2Phone.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://script.google.com/macros/s/AKfycbwVRQ4dKFiARGl88DyX2mj0xigyUUvL7ItZFLi-6S2A8KPZBNo/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        insertInfo();
                        loading.dismiss();

//                        Toast.makeText(AddItemActivity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddItemActivity.this, TrackerActivity.class);
//                        intent.putExtra("mobile", mMobile);
//                        intent.putExtra("customerId", stringCustomerId);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error in data loading", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();


                long customerId = 100000000L + Long.parseLong(mDistrictCode) * 1000L + Long.parseLong(totalCustomers);

                stringCustomerId = String.valueOf(customerId);
                //here we pass params
                parmas.put("action", "addItem");
                parmas.put("name", mName);
                parmas.put("address", mAddress);
                parmas.put("aadhar", mAddhar);
                parmas.put("phone", mMobile);
                parmas.put("photo", mStringImageUrl);
                parmas.put("relative1", mR1);
                parmas.put("r1phone",mR1Phone );
                parmas.put("relative2", mR2);
                parmas.put("r2phone",mR2Phone);
                parmas.put("aadharImage", mStringAddharFrontUrl);
                parmas.put("userId", stringCustomerId);
                return parmas;
            }
        };

        int socketTimeOut = 10000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position<10 && position>0){
            mDistrictCode="0"+position;
            check=1;
        }else if(position>=10){
            mDistrictCode=""+position;
            check=1;
        }
        mDistrictName=spinner.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Toast.makeText(AddItemActivity.this, "Photo Selected", Toast.LENGTH_SHORT).show();
            mImageUri = data.getData();

            Glide.with(this)
                    .load(mImageUri)
                    .into(photo);
            uploadFile(PICK_IMAGE_REQUEST);
        } else if (requestCode == PICK_ADDHAR_FRONT_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Toast.makeText(AddItemActivity.this, "Aadhar Photo Selected", Toast.LENGTH_SHORT).show();
            Glide.with(this)
                    .load(mImageUri)
                    .into(aadharImage);
            uploadFile(PICK_ADDHAR_FRONT_REQUEST);
        }
    }

    private void uploadFile(int identity) {
        if (mImageUri != null) {
            loading = ProgressDialog.show(this, "Adding Item", "Please wait");
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Handler handler=new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mProgressBar.setProgress(0);
//                                }
//                            },5000);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri = uri;
                                    if (identity == PICK_ADDHAR_FRONT_REQUEST) {
                                        mStringAddharFrontUrl = downloadUri.toString().trim();
                                        addharTv.setVisibility(View.GONE);
                                    }else if (identity == PICK_IMAGE_REQUEST) {
                                        mStringImageUrl = downloadUri.toString().trim();
                                        img_tv.setVisibility(View.GONE);
                                    }
                                    Upload upload = new Upload(uri.toString(), "Enter Anything");
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);
                                }
                            });
                            loading.dismiss();
                            Toast.makeText(AddItemActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    for showing progress
//                    double progress=(100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount( ));
//                    mProgressBar.setProgress((int)progress);
                }
            });
        } else {
            Toast.makeText(AddItemActivity.this, "Image Url is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (view.getId() == R.id.image_tv) {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else if (view.getId() == R.id.addhar_tv) {
            startActivityForResult(intent, PICK_ADDHAR_FRONT_REQUEST);
        }
    }

    public boolean validation() {


        if (userName.getText().toString().isEmpty()) {
            userName.setError("Enter Name");
            return false;
        }

        if (houseNo.getText().toString().isEmpty()) {
            houseNo.setError("Enter House No.");
            return false;
        }

        if (societyName.getText().toString().isEmpty()) {
            societyName.setError("Enter Society Name");
            return false;
        }

        if (streetName.getText().toString().isEmpty()) {
            streetName.setError("Enter Street Name");
            return false;
        }

        if (check==0) {
            Toast.makeText(context,"Select District",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (aadhar.getText().toString().isEmpty()) {
            aadhar.setError("Enter Aadhar No.");
            return false;
        }

        if (phone.getText().toString().isEmpty() || !validatePhoneNumber(phone.getText().toString())) {
            phone.setError("Enter Mobile No.");
            return false;
        }
        if (mStringImageUrl == null) {
            Toast.makeText(AddItemActivity.this, "Upload Photo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (r1.getText().toString().isEmpty()) {
            r1.setError("Enter Relative");
            return false;
        }

        if (r2.getText().toString().isEmpty()) {
            r2.setError("Enter Relative");
            return false;
        }

        if (r1Phone.getText().toString().isEmpty()) {
            r1Phone.setError("Enter Phone No.");
            return false;
        }

        if (r2Phone.getText().toString().isEmpty()) {
            r2Phone.setError("Enter Phone No.");
            return false;
        }

        if (mStringAddharFrontUrl == null) {
            Toast.makeText(AddItemActivity.this, "Upload Aadhar Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    private void insertInfo(){

        SQLiteDatabase db=mDbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        String phoneNo=mMobile;
        String userId=stringCustomerId;
        String r1Phone=mR1Phone;
        String r2Phone=mR2Phone;
        if(!phoneNo.isEmpty()) {
            values.put(PhoneContract.ItemEntry.COLUMN_CONTACT_NO, phoneNo);
            values.put(PhoneContract.ItemEntry.COLUMN_USER_ID, userId);
            values.put(PhoneContract.ItemEntry.COLUMN_RELATIVE_1, r1Phone);
            values.put(PhoneContract.ItemEntry.COLUMN_RELATIVE_2, r2Phone);
            long newRowId = db.insert(PhoneContract.ItemEntry.LOCAL_TABLE_NAME, null, values);
            Toast.makeText(this,"Data Saved in local database", Toast.LENGTH_SHORT).show();
            finish();
        }else
            Toast.makeText(this,"Error while saving data into local database", Toast.LENGTH_SHORT).show();
    }
}