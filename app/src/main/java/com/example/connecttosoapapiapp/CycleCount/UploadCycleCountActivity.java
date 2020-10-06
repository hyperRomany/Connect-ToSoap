package com.example.connecttosoapapiapp.CycleCount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Items_For_Logs_Items_SqlServer;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;


public class UploadCycleCountActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>> {
    public final static String CHANNEL_ID = "1";
    public StringRequest request = null;
    public JsonObjectRequest requestitems = null;
    TextView txt_po_number, txt_response;
    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;

    Button btn_export;

    View view;
    RadioButton radiohtp, radiosap;
    ProgressBar progress_for_export;
    String MATERIALDOCUMENT = "Empty", CSVName = "test", MESSAGE;
    String EnvelopeBodyInConstant = "", EnvelopeBodyInCurrent = "";
    String MachaineName, Device_id_Instance_of_MacAdress, UserComp = "01";

    List<Po_Item_of_cycleCount> Po_Item_For_PHYSINVENTORY;
    List<Po_Item_of_cycleCount> Po_Item_For_ftp_Upload;
    List<Users> userList;
    DatabaseHelperForCycleCount databaseHelperForCycleCount;
    DatabaseHelper databaseHelper;
    File filePath;
    Boolean From_Sap_Or_Not = false;
    int Repeat_On_log = 0;
    EditText edit_username, edit_password;
    List<Po_Items_For_Logs_Items_SqlServer> Po_Items_For_LogsArray;
    private int LOADER_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclecount_upload);

        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_po_number=findViewById(R.id.txt_po_number);
        txt_response=findViewById(R.id.txt_response);

        radiohtp=findViewById(R.id.radio_ftp);
        radiosap=findViewById(R.id.radio_sap);

        edit_username=findViewById(R.id.edit_username);
        edit_password =findViewById(R.id.edit_password);


        MachaineName = Build.MODEL;
        Log.d("Build.MODEL",""+MachaineName);


        Device_id_Instance_of_MacAdress = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.e("onCreateU", "Map"+Device_id_Instance_of_MacAdress);


        databaseHelperForCycleCount =new DatabaseHelperForCycleCount(this);
        databaseHelper=new DatabaseHelper(this);

        userList=databaseHelper.getUserData();
        btn_export=findViewById(R.id.btn_export);
        progress_for_export=findViewById(R.id.progress_for_export);



        GetDataFromDB getDataFromDB=new GetDataFromDB();
        getDataFromDB.execute();

//
//         String Path = String.valueOf((UploadForTransferActivity.this).getDatabasePath(DatabaseHelperForTransfer.DATABASE_NAME));
//        Toast.makeText(this, ""+Path, Toast.LENGTH_SHORT).show();
//
//        Log.d("UploadActivity1","filename.csv"+filename);
//        Po_Item_For_Serail_In_Upload = new ArrayList<>();
//
//        Po_Serial_For_Upload = new ArrayList<>();

        // TODO Auto-generated catch block
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Users> userdataList = new ArrayList<>();
//
        userdataList = databaseHelper.getUserData();
        if (userdataList.size() > 0) {
            UserComp = userdataList.get(0).getCompany1();
            UserComp = UserComp.substring(1, 3);
            Log.e("zzzz", "" + UserComp);
        }

    }

    @Override
    public void onBackPressed() {
        List<String> PHYSINVENTORY  = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();
        if (PHYSINVENTORY.size()>0) {
            Intent Go_Back = new Intent(UploadCycleCountActivity.this, ScanActivity.class);
            startActivity(Go_Back);
            super.onBackPressed();
        }else {
            Intent Go_Back = new Intent(UploadCycleCountActivity.this, CycleCountActivity.class);
            startActivity(Go_Back);
            super.onBackPressed();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void Upload(View view) {


        if (radiosap.isChecked()){
            radiosap.setError(null);
          //  Upload(view);
           // radiosap.set
//            if (Po_Item_For_Log_only_Has_value.size()>0) {

                if (Constant.isOnline(UploadCycleCountActivity.this)) {

                    if (edit_username.getText().toString().isEmpty()){
                        edit_username.setError("أدخل الاسم");
                    }else if (edit_password.getText().toString().isEmpty()){
                        edit_password.setError("أدخل كلمه السر");
                    }else {
                        btn_export.setVisibility(View.GONE);
                        progress_for_export.setVisibility(View.VISIBLE);
                        Login_For_UploadCycleCount();

                    }


                    From_Sap_Or_Not = true;
                }else {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }
//            }else {
//                txt_response.setText("كل القم ب 0.0");
//            }

        }else if (radiohtp.isChecked()){


            //UplaodingToFtp();

//            if (Po_Item_For_Log_only_Has_value.size()>0) {

                if (Constant.isOnline(UploadCycleCountActivity.this)) {

                    if (edit_username.getText().toString().isEmpty()){
                        edit_username.setError("أدخل الاسم");
                    }else if (edit_password.getText().toString().isEmpty()){
                        edit_password.setError("أدخل كلمه السر");
                    }else {

                        Login_For_UploadCycleCount();

                    }
                    From_Sap_Or_Not=false;

//                    WriteInLogOf_sapTableOfSqlServer();
//                    // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size(); i++) {
//                    WriteInLogs_sap_ITEMStableOfSqlServer();
                    //   Toast.makeText(this, "if for write", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }

              //  }
//            }else {
//                txt_response.setText("كل القم ب 0.0");
//            }

        }else {
            radiosap.setError("قم بأختيار هذا");
            radiosap.requestFocus();
        }
    }
    public void createCSV(){
        File folder = new File("/data/user/0/com.example.connecttosoapapiapp/databases/");
        boolean var = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        CSVName = "PPC_cyclecount_" + sdf.format(new Date());
        //txt_po_number.setText(CSVName);
        if (!folder.exists()) {
            var = folder.mkdir();
            Toast.makeText(this,"هذا الملف غير موجود",Toast.LENGTH_LONG).show();
        }
        filePath = new File(folder.toString(), CSVName+".CSV");

        try {
            new FileOutputStream(filePath);
            Log.d("UploadActivity11","getDatabasePath"+filePath.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void InsertToCSVFile() {
        try {
            FileWriter writer = new FileWriter(filePath);
//            "PSTNG_DATE","DOC_DATE""PO_NUMBER""PO_ITEM""NO_MORE_GR""SPEC_STOCK""MATERIAL"
//            "PLANT""STGE_LOC""ENTRY_QNT""ENTRY_UOM""EAN_UPC""SERNP"


           /* writer.append("PHYSINVENTORY");
             writer.append(',');
            writer.append("FISCALYEAR");
            writer.append(',');
            writer.append("ITEM");
            writer.append(',');
            writer.append("SERIALNO");
            writer.append(',');
            writer.append("PHYSINVENTORY");
            writer.append(',');
            writer.append("FISCALYEAR");
            writer.append(',');
            writer.append("ITEM");
            writer.append(',');
            writer.append("MATERIAL");
            writer.append(',');
            writer.append("EAN11");
            writer.append(',');
            writer.append("ENTRY_QNT");
            writer.append(',');
            writer.append("ENTRY_UOM");
            writer.append(',');
            writer.append("ZERO_COUNT");
            writer.append(',');
            writer.append("BASE_UOM");
            writer.append('\n');*/

            writer.append("PHYSINVENTORY");
            writer.append(',');
            writer.append("FISCALYEAR");
            writer.append(',');
            writer.append("MATERIAL");
            writer.append(',');
            writer.append("QTY");
            writer.append(',');
            writer.append("ITEM");
            writer.append(',');
            writer.append("BASE_UOM");
            writer.append(',');
            writer.append("SERNP");
            writer.append(',');
            writer.append("EAN");
            writer.append(',');
            writer.append("EAN11");
            writer.append(',');
            writer.append("MEINH");
            writer.append(',');
            writer.append("ZERO_COUNT");
            writer.append('\n');

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String year = sdf.format(new Date());

            /*Po_Item_For_ftp_Upload=databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();

            List<String> PHYSINVENTORY  = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();

            String cc="";
            for (int i=0 ; i<Po_Item_For_ftp_Upload.size(); i++){
                PHYSINVENTORY.remove(Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());

            }
            Log.e("Po_Item_For_ftp_Upload",""+Po_Item_For_ftp_Upload.size());

            for (int i=0 ; i<PHYSINVENTORY.size(); i++){
                Po_Item_For_PHYSINVENTORY=databaseHelperForCycleCount.Get_PHYSINVENTORY_That_Not_Has_QTy(PHYSINVENTORY.get(i));
                Po_Item_For_ftp_Upload.add(new Po_Item_of_cycleCount(Po_Item_For_PHYSINVENTORY.get(0).getPHYSINVENTORY1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getMATERIAL1(),Po_Item_For_PHYSINVENTORY.get(0).getMAKTX1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getQTY1(),Po_Item_For_PHYSINVENTORY.get(0).getITEM1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getBASE_UOM1(),Po_Item_For_PHYSINVENTORY.get(0).getSERNP1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getEAN111(),Po_Item_For_PHYSINVENTORY.get(0).getMEINH1()));
            }
            txt_response.setText(String.valueOf("عدد العناصر التى سيتم رفعها   "+Po_Item_For_ftp_Upload.size()));

*/

            if (Po_Item_For_ftp_Upload.size() ==0){
                Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
            }else {
                Log.e("envelope", "Po_Item_For_ftp_Upload" + Po_Item_For_ftp_Upload.size());

                for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {

                    writer.append(Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());
                    writer.append(',');
                    writer.append(year);
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getMATERIAL1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getMAKTX1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getQTY1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getITEM1());

                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getBASE_UOM1());
                    writer.append(',');

                    writer.append(Po_Item_For_ftp_Upload.get(i).getSERNP1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getEAN111());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getMEINH1());
                    writer.append(',');



                   /* if (Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")){
                        writer.append("");
                        writer.append(',');
                    }else {
                        writer.append("X");
                        writer.append(',');
                    }*/


                    if (Po_Item_For_ftp_Upload.get(i).getQTY1().equalsIgnoreCase("0.0")) {
                        writer.append("0");
                    } else {
                        writer.append("X");
                    }
                    writer.append('\n');
                }

            }
            //generate whatever data you want

            writer.flush();
            writer.close();

//            UplaodingToFtp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

      public void UplaodingToFtp() {
          String PdfID = UUID.randomUUID().toString();
          try {

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                      Log.e("VERSION.SDK_INT " ,"API 26 or higher");

                      // CharSequence name = getString(R.string.channel_name);
                      CharSequence name ="Chanel1";
                      Log.e("VERSION.SDK_INT " , String.valueOf(name));

                      //String description = getString(R.string.channel_description);
                      String description ="chanelforrooms";
                      Log.e("VERSION.SDK_INT " ,description);

                      int importance = NotificationManager.IMPORTANCE_DEFAULT;
                      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                      channel.setDescription(description);
                      // Register the channel with the system; you can't change the importance
                      // or other notification behaviors after this
                      // NotificationManager notificationManager = getSystemService(NotificationManager.class);
                      NotificationManager notificationManager = (NotificationManager) getSystemService
                              (Context.NOTIFICATION_SERVICE);

                      notificationManager.createNotificationChannel(channel);
                  }else
                      Log.e("VERSION.SDK_INT " ,"Not API 26 or higher");

              }
             /* Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

              NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                      .setSmallIcon(R.drawable.logo)
                      //.setLargeIcon(largeIcon(context))
                      .setContentTitle(getString(R.string.app_name))
                      .setContentText(getString(R.string.app_name))
                      // .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                      .setDefaults(Notification.DEFAULT_VIBRATE)
                      //.setContentIntent(contentIntent(context))
                      .setAutoCancel(true)
                      //.setLargeIcon(bitmap)
                      .setAutoCancel(true)
                      .setSound(defaultSoundUri)
                      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                      .setCategory(NotificationCompat.CATEGORY_MESSAGE);

              // COMPLETED (9) If the build version is greater than JELLY_BEAN, set the notification's priority
              // to PRIORITY_HIGH.
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                  notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
              }

              // COMPLETED (11) Get a NotificationManager, using context.getSystemService(Context.NOTIFICATION_SERVICE);
              NotificationManager notificationManager = (NotificationManager)
                      getSystemService(Context.NOTIFICATION_SERVICE);

              // COMPLETED (12) Trigger the notification by calling notify on the NotificationManager.
              // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
              notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
              Log.e("pdfupload",""+filePath.getPath());
              Log.e("pdfupload",""+UploadCycleCountActivity.this.getFilesDir());*/

              MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(this, PdfID, Constant.UploadToCSVFtp_cyclecount);
              multipartUploadRequest.addFileToUpload(filePath.getPath(), "pdf");

              Log.e("bbbbbb","nm,.,mcgvbnjmkl,"+filePath.getPath());
                 // name & room_id & user_id & user_name &  type  this is key between android and php only
              multipartUploadRequest.addParameter("UserType", UserComp);
              multipartUploadRequest.addParameter("name", CSVName);

                 // .addParameter("type", String.valueOf(3))
//              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
Log.e("bbbbbb","nm,.,mcgvbnjmkl,");
                  multipartUploadRequest .setNotificationConfig(new UploadNotificationConfig());
//              }else {
//                  Toast.makeText(this,"تم الرفع",Toast.LENGTH_SHORT).show();
//                  Log.e("bbbbbb","nmelsemcgvbnjmkl,");
//              }
              multipartUploadRequest.setAutoDeleteFilesAfterSuccessfulUpload(true);
              multipartUploadRequest .setMaxRetries(2);
              multipartUploadRequest .startUpload();
              txt_response.setText("تم الرفع بهذا الأسم\n"+CSVName);
              btn_export.setVisibility(View.VISIBLE);
              progress_for_export.setVisibility(View.GONE);

              WriteInLogOf_sapTableOfSqlServer();
              WriteInLogs_sap_ITEMStableOfSqlServer();


//              new AlertDialog.Builder(this)
//                      .setTitle(getString(R.string.delete_all_items))
//                      .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                          public void onClick(DialogInterface dialog, int whichButton) {

              //    databaseHelperForCycleCount.DeleteItemsTable();
//                              Intent GoToBackafter=new Intent(UploadCycleCountActivity.this, CycleCountActivity.class);
//                              startActivity(GoToBackafter);
//                          }
//                      })
//                      .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                          public void onClick(DialogInterface dialog, int whichButton) {
//                              dialog.cancel();
//                          }
//                      }).show();

          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }

       /* String PdfID = UUID.randomUUID().toString();
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              Log.e("VERSION.SDK_INT " ,"API 26 or higher");

              // CharSequence name = getString(R.string.channel_name);
              CharSequence name ="Chanel1";
              Log.e("VERSION.SDK_INT " , String.valueOf(name));

              //String description = getString(R.string.channel_description);
              String description ="chanelforrooms";
              Log.e("VERSION.SDK_INT " ,description);

              int importance = NotificationManager.IMPORTANCE_DEFAULT;
              String CHANNEL_ID="1";
              NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
              channel.setDescription(description);
              // Register the channel with the system; you can't change the importance
              // or other notification behaviors after this
              // NotificationManager notificationManager = getSystemService(NotificationManager.class);
              try {
                  new MultipartUploadRequest(this, PdfID, Constant.UploadToCSVFtp)
                          .addFileToUpload(filePath.getPath(), "csv")
                          //name & room_id & user_id & user_name &  type  this is key between android and php only
                          //                .addParameter("name", FileName)
                          //                .addParameter("room_id", String.valueOf(roomId))
                          //                .addParameter("user_id", String.valueOf(userId))
                          //                .addParameter("user_name", username)
                          //                .addParameter("type", message.getType())

                          //.addParameter("type", String.valueOf(3))
                          .setNotificationConfig(new UploadNotificationConfig())
                          .setMaxRetries(5)
                          .startUpload();
              } catch (MalformedURLException e) {
                  e.printStackTrace();
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              }
          }else
              Log.e("VERSION.SDK_INT " ,"Not API 26 or higher");*/

    }

    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadCycleCountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(UploadCycleCountActivity.this,"أذن قراء من الكارت", Toast.LENGTH_LONG).show();
            UplaodingToFtp();
        } else {

            ActivityCompat.requestPermissions(UploadCycleCountActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(UploadCycleCountActivity.this, "تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    UplaodingToFtp();
                } else {

                    Toast.makeText(UploadCycleCountActivity.this,"تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader =  new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Upload_Cycle_Count , Constant.METHOD_For_Upload_Cycle_Count);

               /* Po_Item_For_ftp_Upload=databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();

                List<String> PHYSINVENTORY  = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();

                String cc="";
                for (int i=0 ; i<Po_Item_For_ftp_Upload.size(); i++){
                    PHYSINVENTORY.remove(Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());

                }
                Log.e("Po_Item_For_ftp_Upload",""+Po_Item_For_ftp_Upload.size());

                for (int i=0 ; i<PHYSINVENTORY.size(); i++){
                    Po_Item_For_PHYSINVENTORY=databaseHelperForCycleCount.Get_PHYSINVENTORY_That_Not_Has_QTy(PHYSINVENTORY.get(i));
                    Po_Item_For_ftp_Upload.add(new Po_Item_of_cycleCount(Po_Item_For_PHYSINVENTORY.get(0).getPHYSINVENTORY1(),
                            Po_Item_For_PHYSINVENTORY.get(0).getMATERIAL1(),Po_Item_For_PHYSINVENTORY.get(0).getMAKTX1(),
                            Po_Item_For_PHYSINVENTORY.get(0).getQTY1(),Po_Item_For_PHYSINVENTORY.get(0).getITEM1(),
                            Po_Item_For_PHYSINVENTORY.get(0).getBASE_UOM1(),Po_Item_For_PHYSINVENTORY.get(0).getSERNP1(),
                            Po_Item_For_PHYSINVENTORY.get(0).getEAN111(),Po_Item_For_PHYSINVENTORY.get(0).getMEINH1()));
                }*/

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                MATERIALDOCUMENT ="Empty";
                if (Po_Item_For_ftp_Upload.size() == 0){
                    Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
                }else {
                    Log.e("envelope", "Po_Item_For_ftp_Upload"+Po_Item_For_ftp_Upload.size());

                    Vector v1 = new Vector();

                    for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {

                        SoapObject rate = new SoapObject(Constant.NAMESPACE_For_Upload_Cycle_Count, "item");

                        rate.addProperty("PHYSINVENTORY", Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());
                        rate.addProperty("FISCALYEAR", year);
                        rate.addProperty("ITEM", Po_Item_For_ftp_Upload.get(i).getITEM1());
                        rate.addProperty("SERIALNO", Po_Item_For_ftp_Upload.get(i).getSERNP1());

                        v1.add(i, rate);
                    }
                    request.addProperty("IT_COUNT_SERIAL", v1);
                    Log.e("envelope", "v1"+v1);

                    Vector v2 = new Vector();

                    //هنملى الداتا فى اللست دى ازاى
//                    Po_Serial_For_Upload = databaseHelperForCycleCount.selectPo_Serials_ToUpload();
                    for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {
//                    if (Po_Serial_For_Upload.size() == 0){
                        SoapObject rate2 = new SoapObject(Constant.NAMESPACE_For_Upload_Cycle_Count, "item");
                        //rate2.addProperty()
                        //this dat should come from Po_serails table
                        rate2.addProperty("PHYSINVENTORY", Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());
                        rate2.addProperty("FISCALYEAR", year);
                        rate2.addProperty("ITEM", Po_Item_For_ftp_Upload.get(i).getITEM1());
                        rate2.addProperty("MATERIAL", Po_Item_For_ftp_Upload.get(i).getMATERIAL1());
                        rate2.addProperty("EAN11", Po_Item_For_ftp_Upload.get(i).getEAN111());
                        rate2.addProperty("ENTRY_QNT", Po_Item_For_ftp_Upload.get(i).getQTY1());
                        rate2.addProperty("ENTRY_UOM", Po_Item_For_ftp_Upload.get(i).getMEINH1());
                        if (Po_Item_For_ftp_Upload.get(i).getQTY1().equalsIgnoreCase("0.0")){
                            rate2.addProperty("ZERO_COUNT","0");
                        }else {
                            rate2.addProperty("ZERO_COUNT","X");
                        }

                        rate2.addProperty("BASE_UOM", Po_Item_For_ftp_Upload.get(i).getBASE_UOM1());


                        v2.add(0, rate2);
                    }
//                    }else {
//                        for (int i = 0; i < Po_Serial_For_Upload.size(); i++) {
//
//                            SoapObject rate2 = new SoapObject(Constant.NAMESPACE_For_Upload_Cycle_Count, "item");
//                            //rate2.addProperty()
//                            //this dat should come from Po_serails table
//
//                            rate2.addProperty("PO_NUMBER", txt_po_number.getText().toString());
//                            rate2.addProperty("PO_ITEM", Po_Serial_For_Upload.get(i).getPo_Item1());
//                            rate2.addProperty("SERIALNO", Po_Serial_For_Upload.get(i).getSerial1());
//
//                            v2.add(i, rate2);
//                        }
//                    }
                    request.addProperty("IT_ITEMS", v2);
                    Log.e("envelope", "v2"+v2);
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Upload_Cycle_Count);

                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Upload_Cycle_Count, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (Po_Item_For_ftp_Upload.size() ==0){
                    Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");

                }else {
                    Log.e("envelopeProMESSAGE", "Po_Item_For_ftp_Upload" + Po_Item_For_ftp_Upload.size());
                    Log.e("envelope", ""+envelope.bodyIn);
                    Log.e("envelope.bodyOut", ""+envelope.bodyOut);
                    Log.e("envelope", ""+((SoapObject)envelope.bodyOut).getPropertyCount());
                    SoapObject soapResponse = (SoapObject) envelope.bodyIn;
                    Log.d("envelopeProMESSAGE",""+soapResponse.getPropertyCount());

                    EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                    EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                    if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                        // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                    }else {
                        SoapObject soapreturn = (SoapObject) soapResponse.getProperty(2);
                        SoapObject soapreturnitem = (SoapObject) soapreturn.getProperty(0);
                        MATERIALDOCUMENT = soapreturnitem.getProperty(3).toString();
                        Log.d("envelopeProMESSAGE", "" + MATERIALDOCUMENT);
//
//                    if (MATERIALDOCUMENT.contains("anyType{}")) {
//                        SoapObject s = (SoapObject) soapResponse.getProperty(4);
//                        SoapObject s2 = (SoapObject) s.getProperty(0);
//                        //SoapObject s3 = (SoapObject) s.getProperty(1);
//                        MESSAGE = (String) s2.getProperty("MESSAGE").toString();
//                       // MESSAGE += "\n"+(String) s3.getProperty("MESSAGE").toString();
//                        Log.d("envelopeProMESSAGE",""+MESSAGE);
//                        Log.d("envelopeProMESSAGE",""+MESSAGE);
//                    }else {
//                        Log.d("envelopeProMESSAGE",""+MESSAGE);
//                    }
//
//                    Log.e("envelopePro", "" + soapResponse.getProperty(3).toString());
                        // Log.e("envelope", ""+id);
                    }
                }
                Log.e("envelope", ""+envelope.bodyIn);
                Log.e("envelope.bodyOut", ""+envelope.bodyOut);
                Log.e("envelope", ""+((SoapObject)envelope.bodyOut).getPropertyCount());

                return null;
            }
        };
        return asyncTaskLoader;
    }

        @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
      //;  Toast.makeText(UploadForTransferActivity.this,"finished ",Toast.LENGTH_LONG);
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.e("onLoadFinis",""+EnvelopeBodyInCurrent);
            Log.e("onLoadFinis",""+EnvelopeBodyInConstant);

            btn_export.setVisibility(View.VISIBLE);
            progress_for_export.setVisibility(View.GONE);

            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                txt_response.setText("خطأ فى البيانات .. أنظر لل log");
            }else if (!MATERIALDOCUMENT.equalsIgnoreCase("Empty")){
                txt_response.setText(MATERIALDOCUMENT);
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                WriteInLogOf_sapTableOfSqlServer();
                WriteInLogs_sap_ITEMStableOfSqlServer();

                databaseHelperForCycleCount.DeleteItemsTable();

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.delete_all_items_cyclecount))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent GoToBackafter = new Intent(UploadCycleCountActivity.this, CycleCountActivity.class);
                                startActivity(GoToBackafter);

                            }
                        }).show();
            }
//            if (MATERIALDOCUMENT.contains("anyType{}") && MESSAGE.contains("Qty and / or ")){
//                //Toast.makeText(this, "There is Error", Toast.LENGTH_SHORT).show();
//                txt_response.setText("هذا الأمر ربما تم تحميله");
//            }else if (MATERIALDOCUMENT.contains("anyType{}")){
//                Toast.makeText(this, "هناك مشكله", Toast.LENGTH_SHORT).show();
//                txt_response.setText("لم يتم رفع"+Po_HeaderList.get(0).getPO_NUMBER1()+"\n"+MESSAGE);
//            }else if (!MATERIALDOCUMENT.contains("anyType{}")){
////                databaseHelperForCycleCount.update_NoMore_To_MaterialNU(MATERIALDOCUMENT);
//                txt_response.setText( "تم الرفع برقم\n"+MATERIALDOCUMENT );
//
////                if (Po_Item_For_Log_only_Has_value.size()>0) {
////                   // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {
//////                    WriteInLogOf_sapTableOfSqlServer();
//////                        WriteInLogs_sap_ITEMStableOfSqlServer();
////                  //  }
////                }else {
////                    txt_response.setText("All Qty Is 0.0");
////
////                }
//            }else if (Po_Item_For_ftp_Upload.size() ==0){
//                txt_response.setText("You not have any change in Quantity");
//            }
          //  else
               // Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//            Intent Go_To_ScanRecieving = new Intent(UploadForTransferActivity.this, ScanRecievingActivity.class);
//            Go_To_ScanRecieving.putExtra("This Is First Time",true);
//            Log.e("This Is First Time","true");
//            startActivity(Go_To_ScanRecieving);

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
    }

    public void Login_For_UploadCycleCount(){

            RequestQueue queue = Volley.newRequestQueue(this);
            // String URL = Constant.LoginURL;
            request = new StringRequest(Request.Method.POST, Constant.Validation_password_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("onResponse", response);
                            Log.d("onResponse", ""+request);

                            try {

                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                Log.d("onResponse", status);

                                String message = object.getString("message");
                                Log.d("onResponse", message);

                                if (status.equalsIgnoreCase("1")){


                                    if (radiosap.isChecked()){
                                        radiosap.setError(null);
                                        //  Upload(view);
                                        // radiosap.set
//            if (Po_Item_For_Log_only_Has_value.size()>0) {

                                        if (Constant.isOnline(UploadCycleCountActivity.this)) {

                                            getLoaderManager().initLoader(LOADER_ID, null, UploadCycleCountActivity.this);
                                            Toast.makeText(UploadCycleCountActivity.this, "تم", Toast.LENGTH_SHORT).show();
                                        }else {

                                            new AlertDialog.Builder(UploadCycleCountActivity.this)
                                                    .setTitle(getString(R.string.textcheckinternet))
                                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }
                                    }else if (radiohtp.isChecked()){
                                        radiosap.setError(null);
                                        if (Constant.isOnline(UploadCycleCountActivity.this)) {


                                            createCSV();
                                            InsertToCSVFile();
                                            RequestRunTimePermission();

                                        }else {
                                            new AlertDialog.Builder(UploadCycleCountActivity.this)
                                                    .setTitle(getString(R.string.textcheckinternet))
                                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }

                                    }else {
                                        radiosap.setError("قم بأختيار هذا");
                                        radiosap.requestFocus();
                                    }

                                }else {
                                    progress_for_export.setVisibility(View.GONE);
                                    btn_export.setVisibility(View.VISIBLE);
                                    Toast.makeText(UploadCycleCountActivity.this, "تاكد من الأسم وكلمه المرور", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    String Date = sdf.format(new Date());

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("User_Name", edit_username.getText().toString());
                    params.put("Password", edit_password.getText().toString());

                    return params;
                }

            };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

    public void WriteInLogs_sap_ITEMStableOfSqlServer(){

        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.WriteInLogs_sap_ITEMStableofcyclecountURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("onResponseU", response);
                        Log.e("onResponseUR", " "+request);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d("onResponse", status);

                            String message = object.getString("message");
                            Log.d("onResponse", message);

                               /* if (status.equalsIgnoreCase("1")){
                                    Intent gotomain =new Intent(UploadForTransferActivity.this,MainActivity.class);
                                    startActivity(gotomain);
                                }else {
                                    Toast.makeText(UploadForTransferActivity.this, "Your User Or Password Is Wrong", Toast.LENGTH_SHORT).show();
                                }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;
                        String errorMsg = " ";
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                JSONObject obj=null;
                JSONArray array=new JSONArray();
                ArrayList arrayreqest=new ArrayList();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String Date = sdf.format(new Date());
                Po_Items_For_LogsArray=new ArrayList<Po_Items_For_Logs_Items_SqlServer>();

                for (int i=0;i<Po_Item_For_ftp_Upload.size();i++) {

                    Po_Items_For_Logs_Items_SqlServer po_items_for_logs_items_sqlServer=
                            new Po_Items_For_Logs_Items_SqlServer();

                    //po_items_for_logs_items_sqlServer.setID( String.valueOf(104 + i));
                    if (From_Sap_Or_Not ==true) {

                        po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER( Po_Item_For_ftp_Upload.get(0).getPHYSINVENTORY1() +"to"+
                                Po_Item_For_ftp_Upload.get(Po_Item_For_ftp_Upload.size()-1).getPHYSINVENTORY1());

                    }else {
                        po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER( CSVName);

                    }
                    po_items_for_logs_items_sqlServer.setP_ORG(MATERIALDOCUMENT);

                    po_items_for_logs_items_sqlServer.setP_GRP( "PHYINV");

                    po_items_for_logs_items_sqlServer.setISS_SITE("PHYINV");

                    po_items_for_logs_items_sqlServer.setISS_STRG_LOC( "PHYINV");

                    po_items_for_logs_items_sqlServer.setREC_SITE( "PHYINV");

                    po_items_for_logs_items_sqlServer.setREC_STRG_LOC( "PHYINV");

                    po_items_for_logs_items_sqlServer.setSTO_CR_DATE( Date);

                    po_items_for_logs_items_sqlServer.setMAT_CODE( Po_Item_For_ftp_Upload.get(i).getMATERIAL1());

                    po_items_for_logs_items_sqlServer.setGTIN( Po_Item_For_ftp_Upload.get(i).getEAN111());

                    po_items_for_logs_items_sqlServer.setGTIN_Desc( Po_Item_For_ftp_Upload.get(i).getMAKTX1());

                    po_items_for_logs_items_sqlServer.setMEINH( Po_Item_For_ftp_Upload.get(i).getMEINH1());

                    po_items_for_logs_items_sqlServer.setQTY( Po_Item_For_ftp_Upload.get(i).getQTY1());
                    po_items_for_logs_items_sqlServer.setUSER_IDD( userList.get(0).getUser_ID1());

                    po_items_for_logs_items_sqlServer.setUSER_NAMME(userList.get(0).getUser_Name1());

                    po_items_for_logs_items_sqlServer.setMODULE( "PHYINV");

                    Po_Items_For_LogsArray.add(po_items_for_logs_items_sqlServer);

                }


                Gson gson = new GsonBuilder().create();
                JsonArray equipmentJsonArray = gson.toJsonTree(Po_Items_For_LogsArray).getAsJsonArray();

                //From_Sap_Or_Not=false;
                params.put("RequestArray", equipmentJsonArray.toString());

                //Log.e("Requestparams",""+obj);

                return params;
            }

        };

        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

    // upload for logs tables
    public void WriteInLogOf_sapTableOfSqlServer() {

        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.WriteInLogOf_sapTableofcyclecountURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("onResponse", response);
                        Log.d("onResponse", "" + request);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d("onResponse", status);

                            String message = object.getString("message");
                            Log.d("onResponse", message);

                                /*if (status.equalsIgnoreCase("1")){
                                    Intent gotomain =new Intent(UploadForTransferActivity.this,MainActivity.class);
                                    startActivity(gotomain);
                                }else {
                                    Toast.makeText(UploadForTransferActivity.this, "Your User Or Password Is Wrong", Toast.LENGTH_SHORT).show();
                                }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String Date = sdf.format(new Date());

                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", userList.get(0).getUser_Name1());
                params.put("Department", "Stockcontrol");
                params.put("MachineName", MachaineName);
                params.put("MACAddress", Device_id_Instance_of_MacAdress);
                params.put("Modulename", "physical inventory");
                params.put("Company", userList.get(0).getCompany1());
                params.put("DateTime", Date);
                if (From_Sap_Or_Not == true) {
                    params.put("Sap_OrderNo", Po_Item_For_ftp_Upload.get(0).getPHYSINVENTORY1() + "to" +
                            Po_Item_For_ftp_Upload.get(Po_Item_For_ftp_Upload.size() - 1).getPHYSINVENTORY1());

                } else {
                    params.put("Sap_OrderNo", CSVName);
                    Log.d("Sap_OrderNo", "" + CSVName);
                    Log.d("Sap_OrderNo", "" + From_Sap_Or_Not);

                    ///Toast.makeText(UploadForTransferActivity.this, "Not from sap", Toast.LENGTH_SHORT).show();

                }
//                Log.d("Build.MODELMac6",""+MATERIALDOCUMENT);

                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

    private class GetDataFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Po_Item_For_ftp_Upload = new ArrayList<>();
            Po_Item_For_PHYSINVENTORY = new ArrayList<>();
            Log.e("zzzbEFORE", "rfe");
            Po_Item_For_ftp_Upload = databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();
            Log.e("zzzbaftergatitems", "rf");

            List<String> PHYSINVENTORY = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();
            Log.e("zzzgatallPh", "fr");
            String cc = "";
            for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {
                PHYSINVENTORY.remove(Po_Item_For_ftp_Upload.get(i).getPHYSINVENTORY1());

            }
            Log.e("Po_Item_For_ftp_Upload", "" + Po_Item_For_ftp_Upload.size());

            for (int i = 0; i < PHYSINVENTORY.size(); i++) {
                Po_Item_For_PHYSINVENTORY = databaseHelperForCycleCount.Get_PHYSINVENTORY_That_Not_Has_QTy("0" + PHYSINVENTORY.get(i));
                Po_Item_For_ftp_Upload.add(new Po_Item_of_cycleCount(Po_Item_For_PHYSINVENTORY.get(0).getPHYSINVENTORY1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getMATERIAL1(), Po_Item_For_PHYSINVENTORY.get(0).getMAKTX1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getQTY1(), Po_Item_For_PHYSINVENTORY.get(0).getITEM1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getBASE_UOM1(), Po_Item_For_PHYSINVENTORY.get(0).getSERNP1(),
                        Po_Item_For_PHYSINVENTORY.get(0).getEAN111(), Po_Item_For_PHYSINVENTORY.get(0).getMEINH1()));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btn_export.setEnabled(true);
            txt_response.setText("عدد العناصر التى سيتم رفعها   " + Po_Item_For_ftp_Upload.size());
        }
    }

}
