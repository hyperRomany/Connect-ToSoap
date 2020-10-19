package com.example.connecttosoapapiapp.TransfereModule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
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
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Items_For_Logs_Items_SqlServer;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;


public class UploadForTransferActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{

TextView  txt_response;
RadioButton radiohtp, radiosap;

    View view;
    String MATERIALDOCUMENT=" ", CSVName="test", MESSAGE;
    private int LOADER_ID = 4;

    public StringRequest request=null;
    public JsonObjectRequest requestitems=null;

    String check_of_UserCode, EnvelopeBodyInConstant,EnvelopeBodyInCurrent;
    DatabaseHelper databaseHelper;
    List<Users> userList;

    List<STo_Search> Sto_searchlist;
    List<STO_Header> Sto_headerlist;
    DatabaseHelperForTransfer databaseHelperForTransfer;
    File filePath;
    String MachaineName,Device_id_Instance_of_MacAdress;
Boolean From_Sap_Or_Not=false;
int Repeat_On_log=0;
    List<Po_Items_For_Logs_Items_SqlServer> Po_Items_For_LogsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_for_transfer);
        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_response = findViewById(R.id.txt_response);

        radiohtp = findViewById(R.id.radio_ftp);
        radiosap = findViewById(R.id.radio_sap);

        databaseHelper = new DatabaseHelper(this);

        userList = databaseHelper.getUserData();
        check_of_UserCode = userList.get(0).getCompany1().substring(1, 3);

        Toast.makeText(this, "" + check_of_UserCode, Toast.LENGTH_SHORT).show();

        databaseHelperForTransfer = new DatabaseHelperForTransfer(this);
        Sto_searchlist = databaseHelperForTransfer.selectSto_Search_for_Qty();
        Sto_headerlist = databaseHelperForTransfer.selectSto_Header();

        //TODO
        if (Sto_headerlist.get(0).getIss_Site1().equalsIgnoreCase("01MW")
                || Sto_headerlist.get(0).getRec_Site1().equalsIgnoreCase("01MW")) {
            radiohtp.setChecked(true);
            radiosap.setVisibility(View.GONE);
        }
        Log.e("szzonCreateU", "n " + radiohtp.isChecked());
        Log.e("sazzonCreateU", "n " + Sto_headerlist.get(0).getIss_Site1());
        Log.e("QSzzonCreateU", "n " + Sto_headerlist.get(0).getRec_Site1());

        MachaineName = android.os.Build.MODEL;
        Log.d("Build.MODEL", "" + MachaineName);

        //TODO this is not work in 6.0 and up
        Device_id_Instance_of_MacAdress = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.e("onCreateU", "Map" + Device_id_Instance_of_MacAdress);

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(UploadForTransferActivity.this , TransferSearchActivity.class);
        Go_Back.putExtra("This Is First Time",false);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    public void Upload(View view) {

            if (radiosap.isChecked()) {
                //  Upload(view);
                // radiosap.set
                if (Constant.isOnline(UploadForTransferActivity.this)) {
                    getLoaderManager().initLoader(LOADER_ID, null, UploadForTransferActivity.this);
                }else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();

                                }
                            }).show();
                }
//                From_Sap_Or_Not = true;


            } else if (radiohtp.isChecked()) {
                if (Constant.isOnline(UploadForTransferActivity.this)) {
                    From_Sap_Or_Not = false;
                    createCSV();
                    InsertToCSVFile();
                    RequestRunTimePermission();
                    WriteInLogOf_sapTableOfSqlServer();
                    WriteInLogs_sap_ITEMStableOfSqlServer();
                }else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textcheckinternet))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();

                                }
                            }).show();
                }
                // UplaodingToFtp();

            } else {
                radiosap.setError("قم بأختيار هذا");
            }

    }
    public void createCSV(){
        File folder = new File("/data/user/0/com.example.connecttosoapapiapp/[] databases/");
        boolean var = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HHmmMM-a");
        if (userList.size() >0) {
            CSVName = userList.get(0).getUser_Name1() +"_"+ sdf.format(new Date()) +
                    userList.get(0).getComplexID1()+ "_STO";//+txt_po_number.getText().toString();
        }else {
            CSVName = sdf.format(new Date())  + "_STO";//+txt_po_number.getText().toString();
        }
        CSVName= CSVName.replaceAll(" ","");
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

            writer.append("Company");
             writer.append(',');
            writer.append("PORG");
            writer.append(',');
            writer.append("pgrp");
            writer.append(',');
            writer.append("fsite");
            writer.append(',');
            writer.append("floc");
            writer.append(',');
            writer.append("tsite");
            writer.append(',');
            writer.append("tloc");
            writer.append(',');
            writer.append("stoType");
            writer.append(',');
            writer.append("MAT_CODE");
            writer.append(',');
            writer.append("GTIN");
            writer.append(',');
            writer.append("Description");
            writer.append(',');
            writer.append("MEINH");
            writer.append(',');
            writer.append("QTY");
            writer.append('\n');

            for (int i = 0; i < Sto_searchlist.size(); i++) {

                writer.append(Sto_headerlist.get(0).getComp_Code1().toUpperCase());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getP_Org1());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getP_Grp1());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getIss_Site1());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getIss_Strg_Log1());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getRec_Site1());
                writer.append(',');
                writer.append( Sto_headerlist.get(0).getRec_Site_log1());
                writer.append(',');
                writer.append(Sto_headerlist.get(0).getSto_Type1());
                writer.append(',');
                writer.append( Sto_searchlist.get(i).getMAT_CODE1());
                writer.append(',');
                writer.append( Sto_searchlist.get(i).getGTIN1());
                writer.append(',');
                writer.append( Sto_searchlist.get(i).getUOM_DESC1());
                writer.append(',');
                writer.append( Sto_searchlist.get(i).getMEINH1());
                writer.append(',');
                writer.append( Sto_searchlist.get(i).getQTY1());
                writer.append('\n');

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
           /*   MultipartUploadRequest m=new MultipartUploadRequest(this, PdfID, Constant.UploadToCSVFtpForTransfer);
              m.addFileToUpload(filePath.getPath(), "pdf");
                 // name & room_id & user_id & user_name &  type  this is key between android and php only
              m   .addParameter("name", CSVName);
              m        .addParameter("DepartType", DepartTypeFun());

                 // .addParameter("type", String.valueOf(3))
              m  .setNotificationConfig(new UploadNotificationConfig())

                      // .setBasicAuth()
                  .startUpload();*/
              MultipartUploadRequest multipartUploadRequest=
                      new MultipartUploadRequest(this, PdfID, Constant.UploadToCSVFtpForTransfer);
              multipartUploadRequest.addFileToUpload(filePath.getPath(), "pdf");
          //    multipartUploadRequest.setBasicAuth("mustafa.elsayed","mustafa01065286596");
              Log.e("bbbbbb","nm,.,mcgvbnjmkl,"+filePath.getPath());
              // name & room_id & user_id & user_name &  type  this is key between android and php only
              multipartUploadRequest.addParameter("Branch", check_of_UserCode);
              multipartUploadRequest.addParameter("P_Org", Sto_headerlist.get(0).getP_Org1()/*DepartTypeFun .equalsIgnoreCase("02PO")*/);
              multipartUploadRequest.addParameter("name", CSVName);
//scpo
              // .addParameter("type", String.valueOf(3))
//              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
              Log.e("bbbbbb","P_Org"+Sto_headerlist.get(0).getP_Org1());
              Log.e("bbbbbb","Branch"+check_of_UserCode);

              multipartUploadRequest .setNotificationConfig(new UploadNotificationConfig());
//              }else {
//                  Toast.makeText(this,"تم الرفع",Toast.LENGTH_SHORT).show();
//                  Log.e("bbbbbb","nmelsemcgvbnjmkl,");
//              }
              multipartUploadRequest.setAutoDeleteFilesAfterSuccessfulUpload(true);

              multipartUploadRequest .setMaxRetries(2);
              String Return = multipartUploadRequest.startUpload();
              Log.e("zzccDepart","gf"+Return);

          } catch (MalformedURLException e) {
              Log.e("zzccDepart","gf");
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
              Log.e("zzccDepart","ghf");
    }
    }

    public String DepartTypeFun(){
        Sto_headerlist = databaseHelperForTransfer.selectSto_Header();
        String DepartType="";

        if (Sto_headerlist.size()>0){
            if (Sto_headerlist.get(0).getP_Org1().equalsIgnoreCase("02PO")){
                DepartType="FF";
            }else if (Sto_headerlist.get(0).getP_Org1().equalsIgnoreCase("01PO")
            || Sto_headerlist.get(0).getP_Org1().equalsIgnoreCase("HYPR")){
                DepartType="FMG";
            }else if (Sto_headerlist.get(0).getP_Org1().equalsIgnoreCase("03PO")){
                DepartType="NF";
            }
        }
        Log.e("zzDepart",""+DepartType);
        return DepartType;
 //       p_org
    }
    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadForTransferActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(UploadForTransferActivity.this,"أذن قراء من الكارت", Toast.LENGTH_LONG).show();
            UplaodingToFtp();
            databaseHelperForTransfer.Update_Sto_Header_For_Status(CSVName);
        } else {

            ActivityCompat.requestPermissions(UploadForTransferActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(UploadForTransferActivity.this, "تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    UplaodingToFtp();
                    databaseHelperForTransfer.Update_Sto_Header_For_Status(CSVName);
                } else {

                    Toast.makeText(UploadForTransferActivity.this,"تم إلغاء الأذن", Toast.LENGTH_LONG).show();
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
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Upload_transfere, Constant.METHOD_For_Upload_transfere);
                Log.d("list",""+Sto_searchlist);
                //////////////////////////////////////////////////////////////////////
//                ArrayList<String> GtinList =new ArrayList<>();
//                for (int i=0;i<Sto_searchlist.size();i++)
//                {
//                    GtinList.add(Sto_searchlist.get(i).getGTIN1());
//                }
//
//
//                for (int j=0;j<Sto_searchlist.size();j++) {
//                    for (int x = 0;x<j;x++) {
//                        if (Sto_searchlist.get(j).getGTIN1().equals(Sto_searchlist.get(x).getGTIN1())) {
//                            System.out.println(Sto_searchlist.get(x) + " is duplicated");
//                            Sto_searchlist.remove(x);
//                        }
//                    }
//                }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                MESSAGE ="";
                if (Sto_searchlist.size() == 0){
                    Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
                }else {
                    Log.e("envelope", "Po_Item_For_ftp_Upload"+Sto_searchlist.size());

                    request.addProperty("COMP_CODE", "H010");
                    request.addProperty("ISS_SITE", Sto_headerlist.get(0).getIss_Site1());
                    request.addProperty("ISS_STRG_LOC", Sto_headerlist.get(0).getIss_Strg_Log1());

                    Vector v1 = new Vector();


                        for (int i = 0; i < Sto_searchlist.size(); i++) {

                        SoapObject rate = new SoapObject(Constant.NAMESPACE_For_Upload_transfere, "item");

                        rate.addProperty("MAT_CODE", Sto_searchlist.get(i).getMAT_CODE1());
                        rate.addProperty("GTIN", Sto_searchlist.get(i).getGTIN1());
                        rate.addProperty("QTY", Sto_searchlist.get(i).getQTY1());
                        rate.addProperty("UOM", Sto_searchlist.get(i).getMEINH1());
                        /*
                        int index = Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().indexOf(".")+2;
                        // Log.d("po_itemlistsize","index"+index);
                        String QTY= String.valueOf(Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().substring(0,index));
                        rate.addProperty("ENTRY_QNT",QTY ); */


                        v1.add(i, rate);
                    }
                    request.addProperty("IT_STO_ITEMS", v1);
                    Log.e("envelope", "v1"+v1);

                    request.addProperty("P_GRP", Sto_headerlist.get(0).getP_Grp1());
                    request.addProperty("P_ORG", Sto_headerlist.get(0).getP_Org1());
                    request.addProperty("REC_SITE", Sto_headerlist.get(0).getRec_Site1());
                    request.addProperty("REC_STRG_LOC", Sto_headerlist.get(0).getRec_Site_log1());
                    request.addProperty("STO_CR_DATE", Sto_headerlist.get(0).getSto_CR_Date1());
                    Log.e("STO_CR_DATE", ""+Sto_headerlist.get(0).getSto_CR_Date1());

                    request.addProperty("STO_DEL_DATE", Sto_headerlist.get(0).getSto_Del_Date1());
                    Log.e("STO_DEL_DATE", ""+Sto_headerlist.get(0).getSto_Del_Date1());



                    request.addProperty("STO_TYPE", Sto_headerlist.get(0).getSto_Type1());

                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Upload_transfere);

                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Upload_transfere, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else {
                    Log.e("envelopebodyIn", ""+envelope.bodyIn);
                    Log.e("envelope.bodyOut", ""+envelope.bodyOut);
                    Log.e("envelope", ""+((SoapObject)envelope.bodyIn).getPropertyCount());
                  Log.e("envelope", ""+((SoapObject)envelope.bodyIn).getProperty(0));
                    SoapObject GetReturn= (SoapObject)((SoapObject)envelope.bodyIn).getProperty(0);
                    if (GetReturn.getPropertyCount() >0){
                        for (int i=0 ; i<GetReturn.getPropertyCount() ; i++) {
                            Log.e("envelopeMSG", "" + GetReturn.getProperty(0));
                            Log.e("envelopeMSGcou", "" + GetReturn.getPropertyCount());
                            SoapObject GetReturnitem = (SoapObject) GetReturn.getProperty(0);
                            Log.e("envelopeMSGite"+i, "" + GetReturnitem.getProperty("MSG"));
                            MESSAGE += GetReturnitem.getProperty("MSG").toString()+"\n";

                        }
                        Log.e("envelope", ""+((SoapObject)envelope.bodyIn).getProperty(1));
                        Log.e("envelope", ""+((SoapObject)envelope.bodyIn).getProperty(2));
                    }else {
                        MESSAGE="";
                        Log.e("envelope", ""+((SoapObject)envelope.bodyIn).getProperty(2));
                        databaseHelperForTransfer.Update_Sto_Header_For_Status(((SoapObject)envelope.bodyIn).getProperty(2).toString());
                        MESSAGE ="STO_NO  "+((SoapObject)envelope.bodyIn).getProperty(2).toString();
                        MATERIALDOCUMENT = ((SoapObject)envelope.bodyIn).getProperty(2).toString();
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
            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                 txt_response.setText("Your Data Is Wrong"+"\n"+EnvelopeBodyInCurrent);
            }else {
                From_Sap_Or_Not = true;
                txt_response.setText(MESSAGE);
                WriteInLogOf_sapTableOfSqlServer();
                WriteInLogs_sap_ITEMStableOfSqlServer();
            }
            /*if (MATERIALDOCUMENT.contains("anyType{}") && MESSAGE.contains("Qty and / or ")){
                //Toast.makeText(this, "There is Error", Toast.LENGTH_SHORT).show();
                txt_response.setText("هذا الأمر ربما تم تحميله");
            }else if (MATERIALDOCUMENT.contains("anyType{}")){
                Toast.makeText(this, "هناك مشكله", Toast.LENGTH_SHORT).show();
                txt_response.setText("لم يتم رفع"+Po_HeaderList.get(0).getPO_NUMBER1()+"\n"+MESSAGE);
            }else if (!MATERIALDOCUMENT.contains("anyType{}")){
                databaseHelper.update_NoMore_To_MaterialNU(MATERIALDOCUMENT);
                txt_response.setText( MATERIALDOCUMENT +"تم الرفع برقم\n");

                if (Po_Item_For_Log_only_Has_value.size()>0) {
                   // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {
                    WriteInLogOf_sapTableOfSqlServer();
                        WriteInLogs_sap_ITEMStableOfSqlServer();
                  //  }
                }else {
                    txt_response.setText("All Qty Is 0.0");

                }
            }else if (Po_Item_For_ftp_Upload.size() ==0){
                txt_response.setText("You not have any change in Quantity");
            }*/
          //  else
               // Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//            Intent Go_To_ScanRecieving = new Intent(UploadForTransferActivity.this, ScanRecievingActivity.class);
//            Go_To_ScanRecieving.putExtra("This Is First Time",true);
//            Log.e("This Is First Time","true");
//            startActivity(Go_To_ScanRecieving);5

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
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                params.put("Department", userList.get(0).getUser_Department1());
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                params.put("MachineName",MachaineName);
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                params.put("MACAddress", Device_id_Instance_of_MacAdress);
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                params.put("Modulename", "Transfer ");
                params.put("Company", userList.get(0).getCompany1());
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                params.put("DateTime",Date);
                Log.d("zzSap_OrderNo",""+From_Sap_Or_Not);

                if (From_Sap_Or_Not ==true) {
                    params.put("Sap_OrderNo", MATERIALDOCUMENT);
                    Log.d("Sap_OrderNo",""+MATERIALDOCUMENT);
                }else {
                    params.put("Sap_OrderNo", CSVName);
                    Log.d("Sap_OrderNo",""+CSVName);
                }
               // Log.d("zzzsapTableOfSqlSer",""+MATERIALDOCUMENT);

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


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String Date = sdf.format(new Date());
                Po_Items_For_LogsArray=new ArrayList<Po_Items_For_Logs_Items_SqlServer>();

                for (int i=0;i<Sto_searchlist.size();i++) {

                    Po_Items_For_Logs_Items_SqlServer po_items_for_logs_items_sqlServer=
                            new Po_Items_For_Logs_Items_SqlServer();

                    //po_items_for_logs_items_sqlServer.setID( String.valueOf(104 + i));
                    /*
                     if (From_Sap_Or_Not ==true) {
                    params.put("Sap_OrderNo", MATERIALDOCUMENT);
                    Log.d("Sap_OrderNo",""+MATERIALDOCUMENT);
                    Log.d("Sap_OrderNo",""+From_Sap_Or_Not);
                    /// Toast.makeText(UploadForTransferActivity.this, "from sap", Toast.LENGTH_SHORT).show();

                }else {
                    params.put("Sap_OrderNo", CSVName);
                    Log.d("Sap_OrderNo",""+CSVName);
                    Log.d("Sap_OrderNo",""+From_Sap_Or_Not);
                    ///Toast.makeText(UploadForTransferActivity.this, "Not from sap", Toast.LENGTH_SHORT).show();
                }
                     */
                    if (From_Sap_Or_Not == true) {
                        po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER(MATERIALDOCUMENT);
                        po_items_for_logs_items_sqlServer.setP_ORG( MATERIALDOCUMENT);
                        Log.d("P_ORG", "" + MATERIALDOCUMENT);
                        Log.d("P_ORG", "" + From_Sap_Or_Not);
                    } else {
                        po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER( CSVName);
                        po_items_for_logs_items_sqlServer.setP_ORG( CSVName);
                        Log.d("P_ORG", "" + CSVName);
                        Log.d("P_ORG", "" + From_Sap_Or_Not);
                    }

                    po_items_for_logs_items_sqlServer.setP_GRP( Sto_headerlist.get(0).getP_Grp1());

                    po_items_for_logs_items_sqlServer.setISS_SITE( Sto_searchlist.get(0).getISS_SITE1());

                    po_items_for_logs_items_sqlServer.setISS_STRG_LOC( Sto_searchlist.get(0).getISS_STG_LOG1());

                    po_items_for_logs_items_sqlServer.setREC_SITE( Sto_searchlist.get(0).getREC_SITE1());

                    po_items_for_logs_items_sqlServer.setREC_STRG_LOC( Sto_searchlist.get(i).getREC_SITE_LOG1());

                    po_items_for_logs_items_sqlServer.setSTO_CR_DATE( Date);

                    po_items_for_logs_items_sqlServer.setMAT_CODE( Sto_searchlist.get(i).getMAT_CODE1());

                    po_items_for_logs_items_sqlServer.setGTIN( Sto_searchlist.get(i).getGTIN1());

                    po_items_for_logs_items_sqlServer.setGTIN_Desc( Sto_searchlist.get(i).getUOM_DESC1());

                    po_items_for_logs_items_sqlServer.setMEINH( Sto_searchlist.get(i).getMEINH1());

                    po_items_for_logs_items_sqlServer.setQTY( Sto_searchlist.get(i).getQTY1());
                    po_items_for_logs_items_sqlServer.setUSER_IDD( userList.get(0).getUser_ID1());

                    po_items_for_logs_items_sqlServer.setUSER_NAMME(userList.get(0).getUser_Name1());

                    po_items_for_logs_items_sqlServer.setMODULE( "Transfer");

                    Po_Items_For_LogsArray.add(po_items_for_logs_items_sqlServer);

                }
                /*Gson gson=new Gson();

                String newDataArray=gson.toJson(Po_Items_For_LogsArray); // dataarray is list aaray

                Log.e("Requestparams",""+newDataArray);*/

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

}
