package com.example.connecttosoapapiapp.ReceivingModule;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.PO_SERIAL;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Header;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Items_For_Logs_Items_SqlServer;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.google.gson.Gson;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class UploadActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
TextView txt_po_number, txt_response, txtvendorname, txtvendornumber;
RadioButton radiohtp, radiosap ;
    public final static String CHANNEL_ID ="1";
    View view;
    String MATERIALDOCUMENT=" ", CSVName="test", MESSAGE;
    private int LOADER_ID = 4;

    public StringRequest request=null;
    public JsonObjectRequest requestitems=null;

    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , envelopebodyInIsNull="";

    List<Po_Header> Po_HeaderList ;
    //List<Po_Item_of_cycleCount> Po_Item_For_Upload;
    List<Po_Item> Po_Item_For_ftp_Upload,Po_Item_For_filter_Upload;
    List<Po_Item> Po_Item_For_Log_only_Has_value;
    List<PO_SERIAL> Po_Serial_For_Upload;
    DatabaseHelper databaseHelper;
    File filePath;
    String MachaineName,Device_id_Instance_of_MacAdress;
Boolean From_Sap_Or_Not=false;
int Repeat_On_log=0;
    List<Po_Items_For_Logs_Items_SqlServer> Po_Items_For_LogsArray;
Button btn_export,btn_Get_Document;
    List<Users> userList;
    String Date, UserComp = "01";
    String ExportORGetdocument="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";
        btn_export=findViewById(R.id.btn_export);
        btn_Get_Document=findViewById(R.id.btn_Get_Document);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date = sdf.format(new Date());

        txt_po_number=findViewById(R.id.txt_po_number);
        txt_response=findViewById(R.id.txt_response);

        txtvendorname =findViewById(R.id.txt_vendor_name);
        txtvendornumber =findViewById(R.id.txt_Vendor_number);


        radiohtp=findViewById(R.id.radio_ftp);
        radiosap=findViewById(R.id.radio_sap);

        databaseHelper =new DatabaseHelper(this);
//TODO REMOVE COMMENT
        userList=databaseHelper.getUserData();
        Po_HeaderList = new ArrayList<>();
        Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();

        txt_po_number.setText(Po_HeaderList.get(0).getPO_NUMBER1());

        txtvendorname.setText(Po_HeaderList.get(0).getVENDOR_NAME1());

        //Log.d("Po_HeadersList",""+Po_HeaderList.get(0).getVENDOR_NAME1());
        txtvendornumber.setText(Po_HeaderList.get(0).getVENDOR11());

       // String Path = String.valueOf((UploadForTransferActivity.this).getDatabasePath(DatabaseHelperForTransfer.DATABASE_NAME));
        //Toast.makeText(this, ""+Path, Toast.LENGTH_SHORT).show();

        //Log.d("UploadActivity1","filename.csv"+filename);
        //Po_Item_For_Serail_In_Upload = new ArrayList<>();

        Po_Item_For_Log_only_Has_value= new ArrayList<>();
        Po_Serial_For_Upload = new ArrayList<>();
        Po_Item_For_ftp_Upload = new ArrayList<>();
        Po_Item_For_filter_Upload = new ArrayList<>();

       GetDataFromDB getDataFromDB=new GetDataFromDB();
        getDataFromDB.execute();


        Log.d("zzzsizeUpload",""+Po_Item_For_Log_only_Has_value.size());
         MachaineName = android.os.Build.MODEL;
        Log.d("Build.MODEL",""+MachaineName);

        //this is not work in 6.0 and up

        Device_id_Instance_of_MacAdress = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.e("onCreateU", "Map"+Device_id_Instance_of_MacAdress);

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

//    @Override
//    public void onBackPressed() {
//        Intent Go_Back= new Intent(UploadActivity.this , ScanRecievingActivity.class);
//        Go_Back.putExtra("This Is First Time",false);
//        startActivity(Go_Back);
//        super.onBackPressed();
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //TODO to prevent user from back (Duplication)
    ////1000427782
    //txt_response.setText("تم الرفع برقم\n" + MATERIALDOCUMENT);
    int i = 0;
    @Override
    public void onBackPressed() {
//1000423291

        if (i == 30 || btn_export.getVisibility() ==View.VISIBLE
                || txt_response.getText().toString().contains("تم الرفع برقم")) {
            Toast.makeText(this, "تم" + (30 ), Toast.LENGTH_SHORT).show();

            Intent Go_Back= new Intent(UploadActivity.this , ScanRecievingActivity.class);
            Go_Back.putExtra("This Is First Time",false);
            startActivity(Go_Back);
            super.onBackPressed();
            this.i = 0;
        } else {
           // Toast.makeText(this, "click on more time" + (59 - i), Toast.LENGTH_SHORT).show();
            this.i++;
            Toast.makeText(this, "أضغط عدد مرات أخرى" + (30 - i), Toast.LENGTH_SHORT).show();

        }
    }




    public void Upload(View view) {
        String Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();
        ExportORGetdocument="Export";
        if (radiosap.isChecked()){
          //  Upload(view);
           // radiosap.set
            if (Po_Item_For_Log_only_Has_value.size()>0) {

                if (Constant.isOnline(UploadActivity.this)) {
                    if (Po_item_NoMore.length() != 0) {
                        Toast.makeText(this, "هذا الأمر تم استلامه برقم" + Po_item_NoMore, Toast.LENGTH_SHORT).show();
                        txt_response.setText( "هذا الأمر تم استلامه برقم" + Po_item_NoMore);
                    }else {
                        btn_export.setVisibility(View.GONE);
                        btn_Get_Document.setVisibility(View.GONE);
                        envelopebodyInIsNull="";
                        MATERIALDOCUMENT=" ";
                        MESSAGE="";
                        getLoaderManager().initLoader(LOADER_ID, null, UploadActivity.this);

                        From_Sap_Or_Not = true;
//                      databaseHelper.DeleteDataOfThreeTables();

                    }
                }else {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }else {
                txt_response.setText("كل القم ب 0.0");
            }

        }else if (radiohtp.isChecked()){
            //UplaodingToFtp();
            if (Po_Item_For_Log_only_Has_value.size()>0) {

                if (Constant.isOnline(UploadActivity.this)) {
                    createCSV();
                    InsertToCSVFile();
                    RequestRunTimePermission();
                    From_Sap_Or_Not=false;

                    WriteInLogOf_sapTableOfSqlServer();
                    // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size(); i++) {
                    WriteInLogs_sap_ITEMStableOfSqlServer();
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
            }else {
                txt_response.setText("كل القم ب 0.0");
            }

        }else {
            radiosap.setError("قم بأختيار هذا");
        }
    }
    public void createCSV(){
        File folder = new File("/data/user/0/com.example.connecttosoapapiapp/databases/");
        boolean var = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        CSVName = sdf.format(new Date())+txt_po_number.getText().toString();
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

            writer.append("PSTNG_DATE");
             writer.append(',');
            writer.append("DOC_DATE");
            writer.append(',');
            writer.append("PO_NUMBER");
            writer.append(',');
            writer.append("PO_ITEM");
            writer.append(',');
            writer.append("NO_MORE_GR");
            writer.append(',');
            writer.append("SPEC_STOCK");
            writer.append(',');
            writer.append("MATERIAL");
            writer.append(',');
            writer.append("PLANT");
            writer.append(',');
            writer.append("STGE_LOC");
            writer.append(',');
            writer.append("ENTRY_QNT");
            writer.append(',');
            writer.append("ENTRY_UOM");
            writer.append(',');
            writer.append("EAN_UPC");
            writer.append(',');
            writer.append("SERNP");
            writer.append('\n');



            if (Po_Item_For_ftp_Upload.size() ==0){
                Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
            }else {
                Log.e("envelope", "Po_Item_For_ftp_Upload" + Po_Item_For_ftp_Upload.size());

                for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {

                    writer.append(Date);
                    writer.append(',');
                    writer.append(Date);
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getPO_NUMBER1());
                    writer.append(',');
                    writer.append( Po_Item_For_ftp_Upload.get(i).getPO_ITEM1());
                    writer.append(',');
                    if (Po_Item_For_ftp_Upload.get(i).getQUANTITY1().equalsIgnoreCase("0.0")) {
                        writer.append("0");
                    } else {
                        writer.append("X");
                    }
                    writer.append(',');
                   /* if (Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")){
                        writer.append("");
                        writer.append(',');
                    }else {
                        writer.append("X");
                        writer.append(',');
                    }*/
                    writer.append( Po_Item_For_ftp_Upload.get(i).getITEM_CAT1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getMATERIAL1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getPLANT1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getSTGE_LOC1());
                    writer.append(',');
                    writer.append( Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getMEINH1());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getEAN111());
                    writer.append(',');
                    writer.append(Po_Item_For_ftp_Upload.get(i).getSERNP1());
                    writer.append('\n');
                }
            }
            //generate whatever data you want

            writer.flush();
            writer.close();

            txt_response.setText("تم الرفع بهذا الأسم\n"+CSVName);
            //UplaodingToFtp();
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

              MultipartUploadRequest multipartUploadRequest=  new MultipartUploadRequest(this, PdfID, Constant.UploadToCSVFtp_recieving);
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


          } catch (
    MalformedURLException e) {
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(UploadActivity.this,"أذن قراء من الكارت", Toast.LENGTH_LONG).show();
            UplaodingToFtp();
        } else {

            ActivityCompat.requestPermissions(UploadActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(UploadActivity.this, "تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    UplaodingToFtp();
                } else {

                    Toast.makeText(UploadActivity.this,"تم إلغاء الأذن", Toast.LENGTH_LONG).show();
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

                if (ExportORGetdocument.equalsIgnoreCase("Export")){

                    SoapObject request = new SoapObject(Constant.NAMESPACE_For_Upload, Constant.METHOD_For_Upload);

                // MATERIALDOCUMENT ="Empty";
                if (Po_Item_For_ftp_Upload.size() == 0) {
                    Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
                } else {
                    Log.e("envelope", "Po_Item_For_ftp_Upload" + Po_Item_For_ftp_Upload.size());

                    Vector v1 = new Vector();

                    for (int i = 0; i < Po_Item_For_ftp_Upload.size(); i++) {

                        SoapObject rate = new SoapObject(Constant.NAMESPACE_For_Upload, "item");

                        rate.addProperty("PSTNG_DATE", Date);
                        rate.addProperty("DOC_DATE", Date);
                        rate.addProperty("PO_NUMBER", Po_Item_For_ftp_Upload.get(i).getPO_NUMBER1());
                        rate.addProperty("PO_ITEM", Po_Item_For_ftp_Upload.get(i).getPO_ITEM1());
                        /*if (Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")){
                            rate.addProperty("NO_MORE_GR", "");
                        }else {
                            rate.addProperty("NO_MORE_GR", "X");
                        }*/
                        rate.addProperty("NO_MORE_GR", "X");
                        rate.addProperty("SPEC_STOCK", Po_Item_For_ftp_Upload.get(i).getITEM_CAT1());
                        rate.addProperty("MATERIAL", Po_Item_For_ftp_Upload.get(i).getMATERIAL1());
                        rate.addProperty("PLANT", Po_Item_For_ftp_Upload.get(i).getPLANT1());
                        rate.addProperty("STGE_LOC", Po_Item_For_ftp_Upload.get(i).getSTGE_LOC1());
                        //  int index = Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().indexOf(".")+2;
                        // Log.d("po_itemlistsize","index"+index);

                        //    String QTY= String.valueOf(Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().substring(0,index));

                        rate.addProperty("ENTRY_QNT", Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1());
                        rate.addProperty("ENTRY_UOM", Po_Item_For_ftp_Upload.get(i).getMEINH1());
                        rate.addProperty("EAN_UPC", Po_Item_For_ftp_Upload.get(i).getEAN111());
                        rate.addProperty("SERNP", Po_Item_For_ftp_Upload.get(i).getSERNP1());

                        v1.add(i, rate);
                    }
                    request.addProperty("IT_GOODSMVT", v1);
                    Log.e("envelope", "v1" + v1);

                    Vector v2 = new Vector();

                    //هنملى الداتا فى اللست دى ازاى
                    Po_Serial_For_Upload = databaseHelper.selectPo_Serials_ToUpload();

                    if (Po_Serial_For_Upload.size() == 0) {
                        SoapObject rate2 = new SoapObject(Constant.NAMESPACE_For_Upload, "item");
                        //rate2.addProperty()
                        //this dat should come from Po_serails table
                        rate2.addProperty("PO_NUMBER", "");
                        rate2.addProperty("PO_ITEM", "");
                        rate2.addProperty("SERIALNO", "");

                        v2.add(0, rate2);
                    } else {
                        for (int i = 0; i < Po_Serial_For_Upload.size(); i++) {

                            SoapObject rate2 = new SoapObject(Constant.NAMESPACE_For_Upload, "item");
                            //rate2.addProperty()
                            //this dat should come from Po_serails table

                            rate2.addProperty("PO_NUMBER", txt_po_number.getText().toString());
                            rate2.addProperty("PO_ITEM", Po_Serial_For_Upload.get(i).getPo_Item1());
                            rate2.addProperty("SERIALNO", Po_Serial_For_Upload.get(i).getSerial1());

                            v2.add(i, rate2);
                        }
                    }
                    request.addProperty("IT_GOODSMVT_SERIAL", v2);
                    Log.e("envelope", "v2" + v2);
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_For_Upload);

                try {
                    httpTransport.call(Constant.SOAP_ACTION_For_Upload, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                } else if (envelope.bodyIn == null) {
                    envelopebodyInIsNull = "null";
                } else {

                    if (Po_Item_For_ftp_Upload.size() == 0) {
                        Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");

                    } else {
                        Log.e("envelopeProMESSAGE", "Po_Item_For_ftp_Upload" + Po_Item_For_ftp_Upload.size());
                        SoapObject soapResponse = (SoapObject) envelope.bodyIn;
                        Log.d("envelopeProMESSAGE", "" + soapResponse.getPropertyCount());
                        MATERIALDOCUMENT = (String) soapResponse.getProperty(3).toString();
                        Log.d("envelopeProMESSAGE", "" + MATERIALDOCUMENT);

                        if (MATERIALDOCUMENT.contains("anyType{}")) {
                            SoapObject s = (SoapObject) soapResponse.getProperty(4);
                            SoapObject s2 = (SoapObject) s.getProperty(0);
                            //SoapObject s3 = (SoapObject) s.getProperty(1);
                            MESSAGE = (String) s2.getProperty("MESSAGE").toString();
                            // MESSAGE += "\n"+(String) s3.getProperty("MESSAGE").toString();
                            Log.d("envelopeProMESSAGE", "" + MESSAGE);
                            Log.d("envelopeProMESSAGE", "" + MESSAGE);
                        } else {
                            Log.d("envelopeProMESSAGE", "" + MESSAGE);
                        }

                        Log.e("envelopePro", "" + soapResponse.getProperty(3).toString());
                        // Log.e("envelope", ""+id);
                    }
                    Log.e("envelope", "" + envelope.bodyIn);
                    Log.e("envelope.bodyOut", "" + envelope.bodyOut);
                    Log.e("envelope", "" + ((SoapObject) envelope.bodyOut).getPropertyCount());
                }

            }
                else if (ExportORGetdocument.equalsIgnoreCase("GETDocument")){

                    SoapObject request = new SoapObject(Constant.NAMESPACE_For_Get_Document, Constant.METHOD_For_Get_Document);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                        request.addProperty("MATDOCUMENTYEAR", year);
                       request.addProperty("PURCHASEORDER", txt_po_number.getText().toString());

                    //TODO REMOVE COMMENT  this lines for test
                   /* request.addProperty("MATDOCUMENTYEAR", "2018");
                        request.addProperty("PURCHASEORDER", "6000009303");*/

                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_For_Get_Document,300000);

                    try {
                        httpTransport.call(Constant.SOAP_ACTION_For_Get_Document, envelope);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                    EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                    if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                        // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                    } else if (envelope.bodyIn == null) {
                        envelopebodyInIsNull = "null";
                    } else {
                        Log.e("envelope", "" + envelope.bodyIn);
                        Log.e("envelope.bodyOut", "" + envelope.bodyOut);
                        Log.e("envelope", "" + ((SoapObject) envelope.bodyOut).getPropertyCount());
                        SoapObject soapObject=(SoapObject) envelope.bodyIn;
                        Log.e("envelopeMATERIALDOCUM", "" +soapObject.getProperty("MATERIALDOCUMENT"));
                        Log.e("envelopeMATERIALDOC", "" +((SoapObject)soapObject.getProperty("MATERIALDOCUMENT")).getProperty("MAT_DOC"));
                        Log.e("envelopeRETURN", "" +soapObject.getProperty("RETURN"));
                        if (soapObject.getProperty("RETURN").toString().equalsIgnoreCase("anyType{}")) {
                            MATERIALDOCUMENT = ((SoapObject) soapObject.getProperty("MATERIALDOCUMENT")).getProperty("MAT_DOC").toString();

                        }else {
                            MATERIALDOCUMENT = "anyType{}";
                           // Log.e("envelopeRETURNme", "" +((SoapObject)soapObject.getProperty("RETURN")).getProperty("MESSAGE").toString());
                            Log.e("envelopeRETURNmes", "" +((SoapObject)((SoapObject)
                                    ((SoapObject)soapObject.getProperty("RETURN"))).getProperty(0)).getProperty("MESSAGE").toString());
                            MESSAGE=((SoapObject)((SoapObject)
                                    ((SoapObject)soapObject.getProperty("RETURN"))).getProperty(0)).getProperty("MESSAGE").toString();
                        }

                    }


                }
                return null;
            }
        };
        return asyncTaskLoader;
    }

        @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
      //;  Toast.makeText(UploadForTransferActivity.this,"finished ",Toast.LENGTH_LONG);
        getLoaderManager().destroyLoader(LOADER_ID);
//            btn_export.setVisibility(View.VISIBLE);
//            btn_Get_Document.setVisibility(View.VISIBLE);
            if (ExportORGetdocument.equalsIgnoreCase("Export")) {
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                    txt_response.setText("" + EnvelopeBodyInCurrent+"\n");
                } else if (envelopebodyInIsNull.equalsIgnoreCase("null")) {
                    txt_response.setText("\nلم يتم رفع البيانات المطلوبه null");
                    Get_Document(view);
                    WriteInLogOf_sapTableOfSqlServer();
                    WriteInLogs_sap_ITEMStableOfSqlServer();
                /*if (Po_Item_For_Log_only_Has_value.size()>0) {
                    // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {
                    WriteInLogOf_sapTableOfSqlServer();
                    WriteInLogs_sap_ITEMStableOfSqlServer();
                    //  }
                }else {
                    txt_response.setText("All Qty Is 0.0");

                }*/
                }/*else if (MESSAGE.contains("PU Ordered quantity exceeded by")){
                Toast.makeText(this, "هناك مشكله", Toast.LENGTH_SHORT).show();
                txt_response.setText("تم تسليم  هذا الامر من قبل"+Po_HeaderList.get(0).getPO_NUMBER1());

            }*/ else if (MATERIALDOCUMENT.contains("anyType{}") && MESSAGE.contains("Qty and / or ")) {
                    //Toast.makeText(this, "There is Error", Toast.LENGTH_SHORT).show();
                    txt_response.setText("هذا الأمر ربما تم تحميله");
                } else if (MATERIALDOCUMENT.contains("anyType{}")) {
                    Toast.makeText(this, "هناك مشكله", Toast.LENGTH_SHORT).show();
                    txt_response.setText("لم يتم رفع" + Po_HeaderList.get(0).getPO_NUMBER1() + "\n" + MESSAGE);
                    Get_Document(view);
                    if (Po_Item_For_Log_only_Has_value.size() > 0) {
                        // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {
                        WriteInLogOf_sapTableOfSqlServer();
                        WriteInLogs_sap_ITEMStableOfSqlServer();
                        //  }
                    } else {
                        txt_response.setText("All Qty Is 0.0");

                    }
                } else if (!MATERIALDOCUMENT.contains("anyType{}")) {
                    databaseHelper.update_NoMore_To_MaterialNU(MATERIALDOCUMENT);
                    txt_response.setText("تم الرفع برقم\n" + MATERIALDOCUMENT);
                    WriteInLogOf_sapTableOfSqlServer();
                    WriteInLogs_sap_ITEMStableOfSqlServer();
                    //databaseHelper.DeleteDataOfThreeTables();
                    if (userList.get(0).getPrint().equals("1")) {
                        getLoaderManager().restartLoader(2, null, new UploadActivity.MyLoaderCallbacks03SA());
                    }

                    new AlertDialog.Builder(this)
                            .setTitle("تم الرفع برقم\n" + MATERIALDOCUMENT)
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    databaseHelper.DeleteDataOfThreeTables();
                                    Intent GoToUpload = new Intent(UploadActivity.this, ReceivingActivity.class);
                                    startActivity(GoToUpload);
                                }
                            }).show();

//                    if (Po_Item_For_Log_only_Has_value.size() > 0) {
                        // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {

                        //  }
//                    } else {
//                        txt_response.setText("All Qty Is 0.0");
//
//                    }
                } else if (Po_Item_For_ftp_Upload.size() == 0) {
                    txt_response.setText("You not have any change in Quantity");
                }
                //  else
                // Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//            Intent Go_To_ScanRecieving = new Intent(UploadForTransferActivity.this, ScanRecievingActivity.class);
//            Go_To_ScanRecieving.putExtra("This Is First Time",true);
//            Log.e("This Is First Time","true");
//            startActivity(Go_To_ScanRecieving);
            }
            else if (ExportORGetdocument.equalsIgnoreCase("GETDocument")) {
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                    txt_response.append("\n" + EnvelopeBodyInCurrent+"\n");
                } else if (envelopebodyInIsNull.equalsIgnoreCase("null")) {
                    txt_response.setText("لم يتم وصول الرد null");
                    //////////waiting///////////////////
                    Get_Document(view);
                } else if (!MATERIALDOCUMENT.contains("anyType{}") ) {
                    txt_response.setText("تم الرفع برقم\n" + MATERIALDOCUMENT);
                    databaseHelper.update_NoMore_To_MaterialNU(MATERIALDOCUMENT);
                    new AlertDialog.Builder(this)
                            .setTitle("تم الرفع برقم\n" + MATERIALDOCUMENT)
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    databaseHelper.DeleteDataOfThreeTables();
                                    Intent GoToUpload = new Intent(UploadActivity.this, ReceivingActivity.class);
                                    startActivity(GoToUpload);
                                }
                            }).show();
                }else {
                    txt_response.append(MESSAGE);
                    //Upload(view);
                }
            }
    }

    public void Get_Document(View view) {
        ExportORGetdocument="GETDocument";
        btn_export.setVisibility(View.GONE);
        btn_Get_Document.setVisibility(View.GONE);
        envelopebodyInIsNull="";
        MATERIALDOCUMENT=" ";
        MESSAGE="";
        getLoaderManager().initLoader(LOADER_ID, null, UploadActivity.this);
    }
    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
    }

    public void WriteInLogOf_sapTableOfSqlServer(){

            RequestQueue queue = Volley.newRequestQueue(this);
            // String URL = Constant.LoginURL;
            request = new StringRequest(Request.Method.POST, Constant.WriteInLogOf_sapTableofcyclecountURL,
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
                    Log.d("Build.MODELMac1",""+Po_HeaderList.get(0).getDelievered_BY1());
                    params.put("Department", "rcv");
                    params.put("MachineName",MachaineName);
                    Log.d("Build.MODELMac2",""+MachaineName);
                    params.put("MACAddress", Device_id_Instance_of_MacAdress);
                    Log.d("Build.MODELMac3",""+Device_id_Instance_of_MacAdress);
                    params.put("Modulename", "Recieve Items");
                    params.put("Company", userList.get(0).getCompany1());
                    Log.d("Build.MODELMac4",""+Po_Item_For_Log_only_Has_value.get(0).getCOMP_CODE1());
                    params.put("DateTime",Date);
                    Log.d("Build.MODELMac5",""+Date);
                    if (From_Sap_Or_Not ==true) {
                        params.put("Sap_OrderNo", Po_HeaderList.get(0).getPO_NUMBER1());
                     //   Log.d("Sap_OrderNo",""+MATERIALDOCUMENT);
                    //    Log.d("Sap_OrderNo",""+From_Sap_Or_Not);
                        /// Toast.makeText(UploadForTransferActivity.this, "from sap", Toast.LENGTH_SHORT).show();

                    }else {
                        params.put("Sap_OrderNo", Po_HeaderList.get(0).getPO_NUMBER1());
                     //   Log.d("Sap_OrderNo",""+CSVName);
                    //    Log.d("Sap_OrderNo",""+From_Sap_Or_Not);

                        ///Toast.makeText(UploadForTransferActivity.this, "Not from sap", Toast.LENGTH_SHORT).show();

                    }
                //    Log.d("Build.MODELMac6",""+MATERIALDOCUMENT);

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
                String Date = sdf.format(new Date())+txt_po_number.getText().toString();
                Po_Items_For_LogsArray=new ArrayList<Po_Items_For_Logs_Items_SqlServer>();

                for (int i=0;i<Po_Item_For_Log_only_Has_value.size();i++) {

                    Po_Items_For_Logs_Items_SqlServer po_items_for_logs_items_sqlServer=
                            new Po_Items_For_Logs_Items_SqlServer();

                        //po_items_for_logs_items_sqlServer.setID( String.valueOf(104 + i));
                        po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER( Po_HeaderList.get(0).getPO_NUMBER1());
                        Log.d("Build.MODELMac6", "" + Po_HeaderList.get(0).getPO_NUMBER1());
                        if (From_Sap_Or_Not == true) {
                            po_items_for_logs_items_sqlServer.setP_ORG( MATERIALDOCUMENT);
                            Log.d("P_ORG", "" + MATERIALDOCUMENT);
                            Log.d("P_ORG", "" + From_Sap_Or_Not);
                        } else {
                            po_items_for_logs_items_sqlServer.setP_ORG( CSVName);
                            Log.d("P_ORG", "" + CSVName);
                            Log.d("P_ORG", "" + From_Sap_Or_Not);
                        }

                        po_items_for_logs_items_sqlServer.setP_GRP( Po_Item_For_Log_only_Has_value.get(i).getPUR_GROUP1());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getPUR_GROUP1());

                        po_items_for_logs_items_sqlServer.setISS_SITE( Po_HeaderList.get(0).getVENDOR11());
                        Log.d("Build.MODELMac6", "" + Po_HeaderList.get(0).getVENDOR11());

                        po_items_for_logs_items_sqlServer.setISS_STRG_LOC( Po_HeaderList.get(0).getVENDOR_NAME1());
                        Log.d("Build.MODELMac6", "" + Po_HeaderList.get(0).getVENDOR_NAME1());

                        po_items_for_logs_items_sqlServer.setREC_SITE( Po_Item_For_Log_only_Has_value.get(i).getPLANT1());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getPLANT1());

                        po_items_for_logs_items_sqlServer.setREC_STRG_LOC( Po_Item_For_Log_only_Has_value.get(i).getPLANT_NAME1());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getPLANT_NAME1());

                        po_items_for_logs_items_sqlServer.setSTO_CR_DATE( Date);
                        Log.d("Build.MODELMac6", "" + Date);

                        po_items_for_logs_items_sqlServer.setMAT_CODE( Po_Item_For_Log_only_Has_value.get(i).getMATERIAL1());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getMATERIAL1());

                        po_items_for_logs_items_sqlServer.setGTIN( Po_Item_For_Log_only_Has_value.get(i).getEAN111());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getEAN111());

                        po_items_for_logs_items_sqlServer.setGTIN_Desc( Po_Item_For_Log_only_Has_value.get(i).getSHORT_TEXT1());
                        Log.d("Build.MODELMac6", "" + Po_Item_For_Log_only_Has_value.get(i).getSHORT_TEXT1());

                        po_items_for_logs_items_sqlServer.setMEINH( Po_Item_For_Log_only_Has_value.get(i).getMEINH1());

                        po_items_for_logs_items_sqlServer.setQTY( Po_Item_For_Log_only_Has_value.get(i).getPDNEWQTY1());
                        po_items_for_logs_items_sqlServer.setUSER_IDD( userList.get(0).getUser_ID1());
                        Log.d("Build.MODELMac6", "" + Po_HeaderList.get(0).getDelievered_BY1());

                        po_items_for_logs_items_sqlServer.setUSER_NAMME(userList.get(0).getUser_Name1());
                        Log.d("Build.MODELMac", "USER_NAMME");

                        po_items_for_logs_items_sqlServer.setMODULE( "RCV");
                        Log.d("Build.MODELMacRCV", "RCV");

                    Po_Items_For_LogsArray.add(po_items_for_logs_items_sqlServer);
                }
                Log.e("list",Po_Items_For_LogsArray.toString());
                Log.e("list","size"+Po_Items_For_LogsArray.size());
                /*Gson gson=new Gson();

                String newDataArray=gson.toJson(Po_Items_For_LogsArray); // dataarray is list aaray

                Log.e("Requestparams",""+newDataArray);*/

//                Gson gson = new GsonBuilder().create();
//                JsonArray equipmentJsonArray = gson.toJsonTree(Po_Items_For_LogsArray).getAsJsonArray();
              String  list= new Gson().toJson( Po_Items_For_LogsArray);
                //From_Sap_Or_Not=false;
                params.put("RequestArray", list);

                Log.e("Requestparams",""+list);
                Log.e("Requestparams",""+Po_Items_For_LogsArray.size());

                return params;
            }

        };

        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }




    private class GetDataFromDB extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            List<String> Po_iteamList=new ArrayList<>() ;
            Po_iteamList= databaseHelper.Get_distinct_Po_Item_();

            Po_Item_For_Log_only_Has_value = databaseHelper.Get_Po_Item_That_Has_PDNewQTy();
            Log.e("zzPo_Item_For_Log",""+Po_Item_For_Log_only_Has_value.size());

            Po_Item_For_filter_Upload = databaseHelper.Get_Po_Item_That_Has_PDNewQTy();
            Po_Item_For_ftp_Upload= databaseHelper.Get_Po_Item_That_Has_PDNewQTy();

            //  Po_Item_For_ftp_Upload= databaseHelper.Get_Po_Item_That_Has_PDNewQTy_for_one_Lineiteam();

//            Log.d("zzzsizeUpload",""+Po_iteamList.size());
//            Log.d("zzzsizeUpload",""+Po_Item_For_ftp_Upload.size());

            for (int j=0;j<Po_Item_For_filter_Upload.size();j++) {
                Log.d("zzzQTYUpload",""+Po_Item_For_ftp_Upload.get(j).getPDNEWQTY1());
                Po_iteamList.remove(Po_Item_For_filter_Upload.get(j).getPO_ITEM1());
            }
//            Log.d("zzzsizeUploadaf",""+Po_iteamList.size());
//            Log.d("zzzsizeUploadaf",""+Po_Item_For_ftp_Upload.size());

            for (int i=0;i<Po_iteamList.size();i++){
                    Po_Item_For_ftp_Upload.add(databaseHelper.Get_Po_Item_for_po_iteam_for_upload(
                            Po_iteamList.get(i)/*.getPO_ITEM1()*/
                            /*,
                            Po_iteamList.get(i).getPO_UNIT1()*/).get(0));
        //            Log.d("zzzQTYUpload", Po_iteamList.get(i).getEAN111() + " " + Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1());
                   /* Log.d("zzzsizeUploadftp "+j,""+Po_Item_For_filter_Upload.get(j).getPO_ITEM1());
                    Log.d("zzzsizeUploadLi "+i,""+Po_iteamList.get(i));
//                    Log.d("zzzsizeUploadss",""+databaseHelper.Get_Po_Item_for_po_iteam(Po_iteamList.get(i)).size());
//                    Log.d("zzzsizeUploadss",""+databaseHelper.Get_Po_Item_for_po_iteam(Po_iteamList.get(i)).get(0).getPO_ITEM1());

                    if (!Po_Item_For_filter_Upload.get(j).getPO_ITEM1().contains(Po_iteamList.get(i))){
                        Po_Item_For_ftp_Upload.add(databaseHelper.Get_Po_Item_for_po_iteam(Po_iteamList.get(i)).get(0));

                    }
//                    if (!Po_Item_For_filter_Upload.get(j).getPO_ITEM1().equalsIgnoreCase(Po_iteamList.get(i))) {
//                        Po_Item_For_ftp_Upload.add(databaseHelper.Get_Po_Item_for_po_iteam(Po_iteamList.get(i)).get(0));
//                       // Log.d("zzzsizeUploadss",""+databaseHelper.Get_Po_Item_for_po_iteam(Po_iteamList.get(i)).size());
//                       Log.d("zzzsizeUploadss",""+Po_Item_For_filter_Upload.get(j).getPO_ITEM1());
//                        Log.d("zzzsizeUploadss",""+Po_iteamList.get(i));
//                    //   break;
//                    }*/
  

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btn_export.setVisibility(View.VISIBLE);
//            txt_response.setText(String.valueOf("عدد العناصر التى سيتم رفعها   "+Po_Item_For_ftp_Upload.size()));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private class MyLoaderCallbacks03SA implements LoaderManager.LoaderCallbacks<List<String>> {


        @Override
        public Loader<List<String>> onCreateLoader(int id, Bundle args) {
            @SuppressLint("StaticFieldLeak")
            AsyncTaskLoader asyncTaskLoader = null;

            asyncTaskLoader = new AsyncTaskLoader(UploadActivity.this) {
                @Override
                protected void onReset() {
                    super.onReset();
                }

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public Object loadInBackground() {

                    SoapObject request = new SoapObject(Constant.NAMESPACE_For_print, Constant.METHOD_For_print);
                    request.addProperty("EBELN", Po_HeaderList.get(0).getPO_NUMBER1());
                    request.addProperty("USERID",userList.get(0).getUser_Name1());
                    request.addProperty("USERDESC",userList.get(0).getUser_Describtion1());
                    MESSAGE = "Empty";
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                    //envelope.dotNet=true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_For_print);
                    try {
                        httpTransport.call(Constant.SOAP_ACTION_For_print, envelope);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("envelope", "" + envelope.bodyIn);
                    Log.d("envelope", "" + envelope.bodyOut);
                    EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                    EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                    if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                        // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                    } else {
                        SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;
                        Log.d("getPropertyCoun", String.valueOf(soapObject_All_response.getPropertyCount()));

                        for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                            Log.e("Return",""+soapObject_All_response.getProperty(0).toString().length());


                            File dwldsPath = new File(Environment.getExternalStorageDirectory(), "/test.txt");
                            try {
                                FileOutputStream os = new FileOutputStream(dwldsPath, false);
                                os.write(soapObject_All_response.getProperty(0).toString().getBytes());
                                os.flush();
                                os.close();

                                File sdcard = Environment.getExternalStorageDirectory();
                                File file1 = new File(sdcard,"test.txt");

                                StringBuilder text = new StringBuilder();

                                try {
                                    BufferedReader br = new BufferedReader(new FileReader(file1));
                                    String line;

                                    while ((line = br.readLine()) != null) {
                                        text.append(line);
                                        text.append('\n');
                                    }


                                    br.close();
                                    dwldsPath = new File(Environment.getExternalStorageDirectory(), "/Hyperone.pdf");
                                    byte[] pdfAsBytes = Base64.decode(String.valueOf(text), 0);
                                    os = new FileOutputStream(dwldsPath, false);
                                    os.write(pdfAsBytes);
                                    os.flush();
                                    os.close();
                                } catch (IOException e) {
                                    Log.d("File", "File.toByteArray() error");
                                    e.printStackTrace();

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                    return null;
                }
            };

            return asyncTaskLoader;

        }

        @Override
        public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
            Toast.makeText(UploadActivity.this, "finished ", Toast.LENGTH_LONG).show();
            getLoaderManager().destroyLoader(LOADER_ID);
            Log.d("soaMESSAGE4", "" + MESSAGE);

            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {

                Toast.makeText(UploadActivity.this, EnvelopeBodyInCurrent,Toast.LENGTH_SHORT).show();
//            edit_asked_from_site_search.setEnabled(false);
                // btn_loading_purchase_order.setEnabled(true);
            } else if (MESSAGE.contains("Empty")) {
                List<GIModule> GIModulelist_bg = new ArrayList<>();
                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(Uri.parse("content:///storage/emulated/0/HyperOne.pdf"), "application/pdf");
                objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);//Starting the pdf viewer

            } else {
               Toast.makeText(UploadActivity.this,MESSAGE,Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onLoadFinished: " + MESSAGE + "  " + Po_HeaderList.get(0).getPO_NUMBER1());

            }
        }

        @Override
        public void onLoaderReset(Loader<List<String>> loader) {

        }

    }

}
