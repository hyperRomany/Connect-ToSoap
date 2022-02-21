package com.example.connecttosoapapiapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
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



public class LoginActivity extends AppCompatActivity {

    private EditText editusername, editpassword;
    private Button btnlogin;
    private DatabaseHelper databaseHelper;
    public StringRequest request = null;
    private TextView txt_version;
    private List<String> VersionDataarray=new ArrayList<>();
    private Boolean UpdateDownloadORNot=false;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private Date DateFromnServer;
    private Date DateOfDevice;
    private SimpleDateFormat sdf;
    private ProgressBar prog_loading_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        VersionDataarray = GetVersionFromServer();
    }



    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    private void initView()
    {

        txt_version = findViewById(R.id.txt_version);
        btnlogin = findViewById(R.id.btn_login);
        editpassword = findViewById(R.id.password);
        prog_loading_login=findViewById(R.id.prog_loading_login);
        editusername = findViewById(R.id.username);
        editusername.setFocusable(true);

        txt_version.setText(" V " + GetVersionOfApp());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        databaseHelper = new DatabaseHelper(this);
        sdf = new SimpleDateFormat("yyyy-MM-dd");


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

                    if (DateFromnServer != null) {

                        Login();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "لم يتم وصول الى التاريخ من السيرفر", Toast.LENGTH_SHORT).show();
                        VersionDataarray = GetVersionFromServer();
                    }

                }

                return false;
            }
        });



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isOnline(LoginActivity.this)) {
                    if (DateFromnServer != null) {

                        Login();
                    }
                    else {
                        prog_loading_login.setVisibility(View.INVISIBLE);
                        btnlogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "لم يتم وصول الى التاريخ من السيرفر", Toast.LENGTH_SHORT).show();
                        VersionDataarray = GetVersionFromServer();

                    }
                }
                else {
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
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DOWNLOAD_PROGRESS) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Downloading file..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            return mProgressDialog;
        }
        return null;
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
                            try {
                                JSONObject object = new JSONObject(response);

                                String status = object.getString("status");
                                if (status.equalsIgnoreCase("1")) {
                                    VersionDataarray.add(object.getString("version"));
                                    VersionDataarray.add(object.getString("version_name")) ;

                                    try {
                                        DateFromnServer = sdf.parse(object.getString("Today_Date"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    RequestRunTimePermission();

                                }else {
                                    Toast.makeText(LoginActivity.this, "لم يتم الحصول على أصدار من السيرفر", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                                Toast.makeText(LoginActivity.this, "" + errorString, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };

        request.setRetryPolicy(new DefaultRetryPolicy(30000, 3, 1.0f));

        queue.add(request);

        return VersionDataarray;
    }


    private String GetVersionOfApp() {
        String Version = "0.0";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version;
    }


    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(LoginActivity.this, "تم أعطاء الأذن", Toast.LENGTH_LONG).show();

                    if (VersionDataarray.size() != 0) {

                          if (Double.valueOf(GetVersionOfApp()) < Double.valueOf(VersionDataarray.get(0))) {

                           Toast.makeText(this, "هناك تحديث", Toast.LENGTH_SHORT).show();

                            if (!VersionDataarray.get(1).equalsIgnoreCase("")) {

                                new DownloadFileFromURL().execute(Constant.ApksURL_ًWithoutName + VersionDataarray.get(1), VersionDataarray.get(1));
                            }
                            else {
                                Toast.makeText(this, "لم يتم الحصول على الاسم الاصدار من السيرفر", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.e("zzzVersionDataarray", "else VersionDataarray.size() =0");
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
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
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }


    public void RequestRunTimePermission() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void Login() {

        try {

            DateOfDevice = sdf.parse(sdf.format(new Date()));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }


        if (editusername.getText().toString().isEmpty()) {

            editusername.setError("من فضلك أدخل الاسم");
        }

        else if (editpassword.getText().toString().isEmpty()) {

            editpassword.setError("من فضلك أدخل كلمه السر");
        }
        else if(VersionDataarray.size() !=0){

            if (Double.valueOf(GetVersionOfApp())< Double.valueOf(VersionDataarray.get(0))) {

                Toast.makeText(this, "هناك تحديث", Toast.LENGTH_SHORT).show();
            }
            else if(!DateOfDevice.equals(DateFromnServer)){

                Toast.makeText(this, "أضبط تاريخ الجهاز", Toast.LENGTH_SHORT).show();
            }
            else {
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
                                try {

                                    JSONObject object = new JSONObject(response);

                                    String status = object.getString("status");

                                    String message = object.getString("message");

                                    if (status.equalsIgnoreCase("1")) {
                                        String User_ID = object.getString("User_ID");

                                        String username = object.getString("username");

                                        String User_Description = object.getString("User_Description");
                                        String Group_Name = object.getString("Group_Name");
                                        String User_status = object.getString("User_status");
                                        String User_Department = object.getString("User_Department");
                                        String company = object.getString("company");
                                        String GroupID = object.getString("GroupID");
                                        String ComplexID = object.getString("ComplexID");

                                        String groupsize = object.getString("groupsize");

                                        String Print = object.getString("Print");
                                        prog_loading_login.setVisibility(View.VISIBLE);
                                        btnlogin.setVisibility(View.GONE);

                                        databaseHelper.DeleteUserDataAndGroupsTables();

                                        if (User_status.equalsIgnoreCase("0")) {
                                            Intent gotomain = new Intent(LoginActivity.this, MainActivity.class);
                                            gotomain.putExtra("UserName", editusername.getText().toString());
                                            databaseHelper.insertuser(User_ID, username, User_Description, Group_Name, User_status,
                                                    User_Department, company, GroupID, ComplexID, Print);

                                            for (int i = 0; i < Integer.valueOf(groupsize); i++) {
                                                String groupsize0 = object.getString("groupsize" + i);
                                                databaseHelper.insertgroup(groupsize0);
                                            }
                                            startActivity(gotomain);
                                        } else {
                                            prog_loading_login.setVisibility(View.GONE);
                                            btnlogin.setVisibility(View.VISIBLE);
                                            Toast.makeText(LoginActivity.this, "هذا المستخدم غير فعال", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "تأكد من الاسم وكلمه السر", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                                    Toast.makeText(LoginActivity.this, "" + errorString, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<>();
                        params.put("User_Name", editusername.getText().toString());
                        params.put("Password", editpassword.getText().toString());

                        return params;
                    }

                };


                request.setRetryPolicy(new DefaultRetryPolicy(30000, 3, 1.0f));

                queue.add(request);

            }

        }
        else {
            Toast.makeText(LoginActivity.this, "خطا فى التسجل", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
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
                        + "/" + f_url[1]);

                byte[] data = new byte[1024];

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

                UpdateDownloadORNot = true;

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                UpdateDownloadORNot = false;
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

            if (UpdateDownloadORNot) {
                InstallApk();
            } else {
                Toast.makeText(LoginActivity.this, "لم يتم الأتصال بالسيرفر لتحميل اخر تحديث", Toast.LENGTH_SHORT).show();
            }
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
