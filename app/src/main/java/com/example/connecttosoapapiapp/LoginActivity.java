package com.example.connecttosoapapiapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText editusername, editpassword;
    Button btnlogin;
    private DatabaseHelper databaseHelper;
    // public static final String TAG = LoginActivity.class.getSimpleName();
    // Tag used to cancel the request
    public StringRequest request = null;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private DownloadManager downloadManager;
    int downloadedSize = 0;
    int totalSize = 0;
    String destination, destinationroot;
    TextView txt_version;
    List<String> VersionDataarray=new ArrayList<>();
    Boolean UpdateDownloadORNot=false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    Date DateFromnServer;
    Date DateOfDevice;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editusername = findViewById(R.id.username);
        editusername.setFocusable(true);

        txt_version = findViewById(R.id.txt_version);
        txt_version.setText(" V " + GetVersionOfApp());
        Log.e("onResponseres", "" + Build.VERSION.SDK_INT + "" + Build.VERSION_CODES.N);

        /*editusername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    Login();
                }

                return false;
            }
        });*/
        editpassword = findViewById(R.id.password);

        DatabaseHelperForCycleCount databaseHelperForCycleCount = new DatabaseHelperForCycleCount(this);
        DatabaseHelperForTransfer databaseHelperForTransfer = new DatabaseHelperForTransfer(this);
        databaseHelper = new DatabaseHelper(this);


        editpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER) {

                    //execute our method for searching
                    Login();
                }

                return false;
            }
        });
        btnlogin = findViewById(R.id.btn_login);
        Log.e("onCreate", "Map");

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isOnline(LoginActivity.this)) {

                    Login();

                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();

                                }
                            }).show();
                }
            }
        });

        VersionDataarray = GetVersionFromServer();
        Log.e("zzzVersionDataarray","oncreate "+VersionDataarray.size());


        sdf = new SimpleDateFormat("yyyy-MM-dd");


//        new DownloadFileFromURL().execute(Constant.ApksURL_ًWithoutName + "app-debugV1.1.apk", "app-debugV1.1.apk");

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Downloading file..");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            Log.e("zzzf_urldoInBac",""+f_url[0]);
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/"+f_url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                UpdateDownloadORNot=true;

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                UpdateDownloadORNot=false;
//                Toast.makeText(LoginActivity.this, "لم يتم التتصال بالسيرفر لتحميل اخر تحديث", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Log.e("zzzonPostExecute","onPostExecute");
            if (UpdateDownloadORNot ==true){
                InstallApk();
            }else if (UpdateDownloadORNot == false){
                Toast.makeText(LoginActivity.this, "لم يتم الأتصال بالسيرفر لتحميل اخر تحديث", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private String GetVersionOfApp() {
        String Version = "0.0";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
            Log.d(TAG, "checkVersion.DEBUG: App version: " + Version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version;
    }

    public List<String> GetVersionFromServer() {
        RequestQueue queue = Volley.newRequestQueue(this);
            request = new StringRequest(Request.Method.POST, Constant.GetVersionURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String encodedstring = null;
                            try {
                                encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                                response = URLDecoder.decode(encodedstring, "UTF-8");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            JsonObject object1 = new JsonObject().getAsJsonObject(response);
                            Log.e("onResponseresoo", "object" + object1);
                            try {
                                JSONObject object = new JSONObject(response.toString());
                                Log.e("onResponse", "object" + object);

                                String status = object.getString("status");
                                Log.d("onResponse", status);
                                if (status.equalsIgnoreCase("1")) {
                                    VersionDataarray.add(object.getString("version"));
                                    Log.d("onResponse", VersionDataarray.get(0));
                                    VersionDataarray.add(object.getString("version_name")) ;
                                    Log.d("onResponse", VersionDataarray.get(1));

                                    try {
                                        DateFromnServer = sdf.parse(object.getString("Today_Date"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    };
                                    Log.d("onResponse", ""+DateFromnServer);

                                    RequestRunTimePermission();

                                }else {
                                    Toast.makeText(LoginActivity.this, "لم يتم الحصول على أصدار من السيرفر", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("log erroree", String.valueOf(e));
                                Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse response = error.networkResponse;
                            String errorMsg = "";
                            if (response != null && response.data != null) {
                                String errorString = new String(response.data);
                                Log.i("log error", errorString);
                                Toast.makeText(LoginActivity.this, "" + errorString, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                  //  params.put("User_Name", editusername.getText().toString());
                    return params;
                }

            };
            // Add the realibility on the connection.
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

            // Start the request immediately
            queue.add(request);

        return VersionDataarray   ;
    }

    public void InstallApk() {

        PackageManager m = getPackageManager();

        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir;
        Log.e("zzapplication", "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        File toInstall = new File(Environment.getExternalStorageDirectory() + File.separator +  VersionDataarray.get(1));

        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(this, "com.example.connecttosoapapiapp.fileprovider", toInstall);

            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, false);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setData(apkUri);

        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ///  Toast.makeText(LoginActivity.this,"أذن كتابه الى الكارت", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {
//                    GetVersionFromServer();

                      Toast.makeText(LoginActivity.this,"تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    Log.e("zzzVersionDataarray","dfdf"+VersionDataarray.size());
                    Log.e("zzzVersionDataarray","  "+Double.valueOf(GetVersionOfApp()));
                    Log.e("zzzVersionDataarray","  "+Double.valueOf(VersionDataarray.get(0)));
                    if (VersionDataarray.size() !=0) {
                        //TODO check more than
                      //  if (!GetVersionOfApp().equalsIgnoreCase(VersionDataarray.get(0))) {
                        if (Double.valueOf(GetVersionOfApp())< Double.valueOf(VersionDataarray.get(0))) {

                            Toast.makeText(this, "هناك تحديث", Toast.LENGTH_SHORT).show();
//                        DownloadData(Uri.parse(Constant.ApksURL));
//                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//                        registerReceiver(downloadReceiver, filter);

                            if (!VersionDataarray.get(1).equalsIgnoreCase("")) {
                                new DownloadFileFromURL().execute(Constant.ApksURL_ًWithoutName + VersionDataarray.get(1), VersionDataarray.get(1));
                            } else {
                                Toast.makeText(this, "لم يتم الحصول على الاسم الاصدار من السيرفر", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Log.e("zzzVersionDataarray","else VersionDataarray.size() =0");
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    public void Login() {
        try {
            DateOfDevice = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("zzDateOfDevice",""+DateOfDevice);
        Log.e("zzDateFromnServer",""+DateFromnServer);

        Log.e("zzcompare","equale"+DateOfDevice.equals(DateFromnServer));
        Log.e("zzcompare","after"+DateOfDevice.after(DateFromnServer));
        Log.e("zzcompare","before"+DateOfDevice.before(DateFromnServer));
        if (editusername.getText().toString().isEmpty()) {
            editusername.setError("من فضلك أدخل الاسم");
        }else if (editpassword.getText().toString().isEmpty()) {
            editpassword.setError("من فضلك أدخل كلمه السر");
        }else if(VersionDataarray.size() !=0){
        //    if(!GetVersionOfApp().equalsIgnoreCase(VersionDataarray.get(0))){
            if (Double.valueOf(GetVersionOfApp())< Double.valueOf(VersionDataarray.get(0))) {
                Toast.makeText(this, "هناك تحديث", Toast.LENGTH_SHORT).show();
            }else if(!DateOfDevice.equals(DateFromnServer)){
                Toast.makeText(this, "أضبط تاريخ الجهاز", Toast.LENGTH_SHORT).show();
            }else {
                RequestQueue queue = Volley.newRequestQueue(this);
                request = new StringRequest(Request.Method.POST, Constant.LoginURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                String encodedstring = null;
                                try {
                                    encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                                    response = URLDecoder.decode(encodedstring, "UTF-8");

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                JsonObject object1 = new JsonObject().getAsJsonObject(response);
                                Log.e("onResponseresoo", "object" + object1);
                                try {

                                    JSONObject object = new JSONObject(response.toString());
                                    Log.e("onResponse", "object" + object);

                                    String status = object.getString("status");
                                    Log.d("onResponse", status);

                                    String message = object.getString("message");
                                    Log.d("onResponse", message);

                                    if (status.equalsIgnoreCase("1")) {
                                        String User_ID = object.getString("User_ID");
                                        Log.d("onResponse", "message" + User_ID);
                                        String username = object.getString("username");
                                        Log.d("onResponse", "message" + username);
                                        String User_Description = object.getString("User_Description");
                                        Log.d("onResponse", "message" + User_Description);
                                        String Group_Name = object.getString("Group_Name");
                                        Log.d("onResponse", "message" + Group_Name);
                                        String User_status = object.getString("User_status");
                                        Log.d("onResponse", "message" + User_status);
                                        String User_Department = object.getString("User_Department");
                                        Log.d("onResponse", "message" + User_Department);
                                        String company = object.getString("company");
                                        Log.d("onResponse", "message" + company);
                                        String GroupID = object.getString("GroupID");
                                        Log.d("onResponse", "message" + GroupID);
                                        String ComplexID = object.getString("ComplexID");
                                        Log.d("onResponse", "message" + ComplexID);

                                        String groupsize = object.getString("groupsize");
                                        Log.d("onResponse", "groupsize" + groupsize);

                                        databaseHelper.DeleteUserDataAndGroupsTables();

                                        if (User_status.equalsIgnoreCase("0")) {
                                            Intent gotomain = new Intent(LoginActivity.this, MainActivity.class);
                                            gotomain.putExtra("UserName", editusername.getText().toString());
                                            databaseHelper.insertuser(User_ID, username, User_Description, Group_Name, User_status,
                                                    User_Department, company, GroupID, ComplexID);

                                            for (int i = 0; i < Integer.valueOf(groupsize); i++) {
                                                String groupsize0 = object.getString("groupsize" + i);
                                                Log.d("onResponse", "groupsize0" + i + groupsize0);
                                                databaseHelper.insertgroup(groupsize0);
                                            }
                                            startActivity(gotomain);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "هذا المستخدم غير فعال", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "تأكد من الاسم وكلمه السر", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("log erroree", String.valueOf(e));
                                    Toast.makeText(LoginActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                NetworkResponse response = error.networkResponse;
                                String errorMsg = "";
                                if (response != null && response.data != null) {
                                    String errorString = new String(response.data);
                                    Log.i("log error", errorString);
                                    Toast.makeText(LoginActivity.this, "" + errorString, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("User_Name", editusername.getText().toString());
                        params.put("Password", editpassword.getText().toString());
                        //params.put("key_1","value_1");
                        // params.put("key_2", "value_2");

                        Log.i("sending ", params.toString());
                        Log.e("onResponser", "response" + request);

                        return params;
                    }

                };


                // Add the realibility on the connection.
                request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

                // Start the request immediately
                queue.add(request);

            }

        } else {
            Toast.makeText(LoginActivity.this, "خطا فى التسجل", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
