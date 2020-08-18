package com.example.connecttosoapapiapp.ItemAvailability;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.ItemAvailability.Helper.DatabaseHelperForItemAvailability;
import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemAvailabilityModule;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ScanItemAvailabilityActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
    public StringRequest request=null;
    Spinner spiner_site;
    private int LOADER_ID = 1;
    public final static String CHANNEL_ID ="1";
    String EnvelopeBodyInConstant,EnvelopeBodyInConstant2 , EnvelopeBodyInCurrent , RETURN, MESSAGE,MATERIALDOCUMENT="Empty"
            ,GL,CS,Site,REC_SITE_LOG1="";
    ArrayAdapter<String> adapterForSites;
    List<String> site_list;
    String check_of_UserCode;
    EditText edt_barcode;
    List<String> ReturnSearchList ;
    String Depart,KQTY,GQTY,TotalQTYFor23,BarcodeFor23,editbarcode;
    VolleyError volleyErrorPublic;
    TextView txt_descripation,txt_code_item,txt_state_item,txt_available_in_site,txt_unite,txt_grp,
            txt_sales_today/*,txt_available_minus_sales*/,txt_price;
    Button btn_search_barcode;
    String Article="";
    List<Users> userdataList;
    private DatabaseHelper databaseHelper;
    DatabaseHelperForItemAvailability databaseHelperForItemAvailability;

    File filePath;
    String CSVName="test";
    List<ItemAvailabilityModule> itemAvailabilityModuleList;
Button btn_export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item_availability);
        edt_barcode=findViewById(R.id.edt_barcode);
        spiner_site=findViewById(R.id.spiner_site);
        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";

        btn_search_barcode=findViewById(R.id.btn_search_barcode);
        txt_price=findViewById(R.id.txt_price);
        txt_descripation=findViewById(R.id.txt_descripation);
        txt_code_item=findViewById(R.id.txt_code_item);
        txt_state_item=findViewById(R.id.txt_state_item);
        txt_available_in_site=findViewById(R.id.txt_available_in_site);
        txt_unite=findViewById(R.id.txt_unite);
        txt_grp=findViewById(R.id.txt_grp);

     //   txt_available_minus_sales=findViewById(R.id.txt_available_minus_sales);
        txt_sales_today=findViewById(R.id.txt_sales_today);

        site_list = new ArrayList<>();
        itemAvailabilityModuleList=new ArrayList<>();
        userdataList = new ArrayList<>();
        databaseHelperForItemAvailability=new DatabaseHelperForItemAvailability(this);
        databaseHelper=new DatabaseHelper(this);
        userdataList = databaseHelper.getUserData();
        check_of_UserCode=userdataList.get(0).getCompany1().toString().substring(1,3);

        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();
//        check_of_UserCode="01";
        ReturnSearchList = new ArrayList<>();

        getlistfromsqlserver();


        btn_export=findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemAvailabilityModuleList=new ArrayList<>();
                itemAvailabilityModuleList.clear();
                itemAvailabilityModuleList = databaseHelperForItemAvailability.select_ItemsAvaiModule();
                 if (itemAvailabilityModuleList.size() >0) {
                    createCSV();
                    InsertToCSVFile();
                    RequestRunTimePermission();
                } else {
                    Toast.makeText(ScanItemAvailabilityActivity.this, "لا يوجد بيانات للرفع", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void SearchBarCode(View view) {

        edt_barcode.setError(null);
        txt_available_in_site.setText("المتاح");
        txt_code_item.setText("كود الصنف");
        txt_descripation.setText("الوصف");
        txt_state_item.setText("حالة الصنف");
        txt_unite.setText("وحدة القياس");
        txt_sales_today.setText("مبيعات اليوم");
        txt_price.setText("سعر القطعه");

        if (edt_barcode.getText().toString().isEmpty()){
            edt_barcode.setError("من فضلك ادخل الباركود");
        }else if (site_list.size() ==0){
            Toast.makeText(this, "أختر الموقع أولا", Toast.LENGTH_SHORT).show();
        }else {
            ReturnSearchList.clear();
            getLoaderManager().initLoader(LOADER_ID, null, ScanItemAvailabilityActivity.this);


//            txt_available_minus_sales.setText(String.valueOf(Double.valueOf(txt_available_in_site.getText().toString())-
//                    Double.valueOf(txt_sales_today.getText().toString())));

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

                    SoapObject request = new SoapObject(Constant.NAMESPACE_For_Search_Barcode, Constant.METHOD_For_Search_Barcode);
                    request.addProperty("GTIN", edt_barcode.getText().toString());
                    request.addProperty("ISS_SITE", spiner_site.getSelectedItem().toString());
                    request.addProperty("REC_SITE",  spiner_site.getSelectedItem().toString());

                    RETURN = "Empty";
                    MESSAGE = "Empty";
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                    //envelope.dotNet=true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_For_Search_Barcode);
                    try {
                        httpTransport.call(Constant.SOAP_ACTION_For_Search_Barcode, envelope);
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

                            Log.d("soapObject", String.valueOf(soapObject_All_response.getProperty(i)));

                            SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);
                            RETURN = (String) soapObject_All_response.getProperty(0).toString();
                            Log.d("getPropertyCounitems", String.valueOf(soapObject_items.getPropertyCount()));

                            for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {

                                SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);

                                for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {
                                    // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);
                                    Log.d("For_each_itemwith_k", String.valueOf(soapObject_items_detials.getProperty(k)));
                                    if (i == 0) {
                                        Log.d("for_each_item_for", String.valueOf(soapObject_items.getProperty(j)));
                                      /*  Log.d("for_each_item_for",editbarcodeforsoap.getText().toString()+
                                                FromSite+ToSite+ReturnSearchList.get(0)+ReturnSearchList.get(1)+
                                                ReturnSearchList.get(2)+ReturnSearchList.get(8)+ReturnSearchList.get(3)+
                                                ReturnSearchList.get(4)+ReturnSearchList.get(5)+ReturnSearchList.get(6)+
                                                ReturnSearchList.get(7));*/
                                        MESSAGE = soapObject_items_detials.getProperty(1).toString();
                                        //RETURN = (String) soapObject_All_response.getProperty(0).toString();
                                       /* if(RETURN.contains("anyType{}")){

                                        }*/
                                    }
                                    if (i == 1 && RETURN.contains("anyType{}")) {
                                        ReturnSearchList.add(String.valueOf(soapObject_items_detials.getProperty(k)));
                                    }
                                }
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
        Toast.makeText(ScanItemAvailabilityActivity.this, "finished ", Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.d("soaMESSAGE4", "" + MESSAGE);
        Log.e("This Is First Time", "" + RETURN);

            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                edt_barcode.setError("" + EnvelopeBodyInCurrent);
//            edit_asked_from_site_search.setEnabled(false);
                // btn_loading_purchase_order.setEnabled(true);
            } else if (MESSAGE.contains("Empty")) {
                List<GIModule> GIModulelist_bg = new ArrayList<>();
                Log.e("This Is First Time", "" + RETURN);
                Toast.makeText(ScanItemAvailabilityActivity.this, RETURN, Toast.LENGTH_LONG).show();
                
                if (ReturnSearchList.get(8).equalsIgnoreCase("1")) {
                    edt_barcode.setError("هذا الباركود غير فعال");
                }  else {

                    txt_descripation.setText(ReturnSearchList.get(1));
                    txt_code_item.setText(ReturnSearchList.get(0).subSequence(6, 18));


                    if (ReturnSearchList.get(3).equalsIgnoreCase("0")){
                        txt_state_item.setText("الباركود فعال");

                }else if (ReturnSearchList.get(3).equalsIgnoreCase("1")) {
                        txt_state_item.setText("الباركود غير فعال");
                        txt_state_item.setTextColor(getResources().getColor(R.color.red));
                        edt_barcode.setError("الباركود غير فعال");
                    }
                    txt_available_in_site.setText(ReturnSearchList.get(6));
                    txt_unite.setText(ReturnSearchList.get(2));
                    txt_grp.setText(ReturnSearchList.get(7));

                    Article=txt_code_item.getText().toString();
                    GetDetialsForBarcod();
            }
        }else {
                edt_barcode.setError(MESSAGE);
                itemAvailabilityModuleList.clear();
                itemAvailabilityModuleList = databaseHelperForItemAvailability.select_ItemsAvaiModulebyBarcode(edt_barcode.getText().toString());
                if (itemAvailabilityModuleList.size() ==0) {
                   WriteInLogOf_sapTableofundefinedinSqlServer();
                }else {
                    Toast.makeText(ScanItemAvailabilityActivity.this, "تم عمل scan لهذا الباركود من قبل", Toast.LENGTH_SHORT).show();
//                    Log.d("zzBuild.MODELMac6","الباركود عمل scan له من قبل");

                }
            }
    }
    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    public void WriteInLogOf_sapTableofundefinedinSqlServer(){

        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.WriteInLogOf_sapTableofundefined,
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
                params.put("UserName", userdataList.get(0).getUser_Name1().trim());
                params.put("Barcode", edt_barcode.getText().toString());
                MESSAGE = MESSAGE.replaceAll("\\[","");
                MESSAGE = MESSAGE.replaceAll("\\]","");
                MESSAGE = MESSAGE.replaceAll(" ","");
                params.put("Message",MESSAGE);
                params.put("Date",Date);
                params.put("Site",spiner_site.getSelectedItem().toString());


                databaseHelperForItemAvailability.insert_ItemsAvai(edt_barcode.getText().toString(), MESSAGE, userdataList.get(0).getUser_Name1().trim());

                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

    public void getlistfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.ListfortransferFromSqlServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String encodedstring = null;
                        try {
                            encodedstring = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(encodedstring,"UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("onResponser", "response"+response);
                        //  Log.e("onResponse", "response"+Uri.encode(response, "utf-8").toString());
                        Log.e("onResponse", "request"+request);
                        try {

                            JSONObject object = new JSONObject(response);

                            String Sites_LOCATIONS = object.getString("Sites_LOCATIONS");
                            Log.e("onResponse", Sites_LOCATIONS);

                            for (int i=0 ; i<Integer.valueOf(Sites_LOCATIONS);i++){

                                String pgrp_description= object.getString("pgrp_description"+i);
                                Log.e("pgrp_description"+i, pgrp_description);
                                if (pgrp_description.contains(check_of_UserCode)) {
                                    site_list.add(pgrp_description);
                                }
                            }
                            adapterForSites=new ArrayAdapter<String>(ScanItemAvailabilityActivity.this,
                                    android.R.layout.simple_spinner_item,site_list);
                            adapterForSites.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_site.setAdapter(adapterForSites);

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

                Map<String, String> params = new HashMap<String, String>();
                // params.put("User_Name", editusername.getText().toString());
                //   params.put("Password", editpassword.getText().toString());
                //params.put("key_1","value_1");
                // params.put("key_2", "value_2");
                params.put("type","STO1");
                Log.i("sending ", params.toString());
                Log.e("onResponser", "response"+request);

                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);



    }


    public void GetDetialsForBarcod() {

        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

     
       
        if (edt_barcode.getText().toString().isEmpty()) {
            edt_barcode.setError("من فضلك أدخل الباركود");
            edt_barcode.setText("");
            edt_barcode.requestFocus();


        }else  {

            Depart="";
            KQTY="00";
            GQTY="000";
            TotalQTYFor23="";
            BarcodeFor23="";
            Depart=edt_barcode.getText().toString().substring(0,2);
            if (Depart.equalsIgnoreCase("23")
                    && edt_barcode.getText().toString().length() ==13) {
                KQTY = edt_barcode.getText().toString().substring(7, 9);
                GQTY = edt_barcode.getText().toString().substring(9, 12);
                TotalQTYFor23 = KQTY + "." + GQTY;

                //                BarcodeFor23 = edt_barcode.getText().toString().replace(TotalQTYFor23.replace(".", ""), "00000");
                BarcodeFor23 = edt_barcode.getText().toString().substring(0,7);


            }else if (Depart.equalsIgnoreCase("23")
                    && edt_barcode.getText().toString().length() !=13) {
                edt_barcode.setError("الباركود أقل أو أكبر من 13");
                edt_barcode.setText("");
                edt_barcode.requestFocus();
            }

            else {

                editbarcode = edt_barcode.getText().toString();
                Log.e("editbarcode", "" + BarcodeFor23);
//                progressBar.setVisibility(View.VISIBLE);
                edt_barcode.setEnabled(false);
                btn_search_barcode.setEnabled(false);
                RequestQueue queue = Volley.newRequestQueue(this);
                request = new StringRequest(Request.Method.POST, Constant.GetDetialsURL,
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
                                Log.e("onResponse", "response" + response);
                                Log.e("onResponse", "request" + request);
                                try {
//                                    progressBar.setVisibility(View.GONE);
                                    edt_barcode.setEnabled(true);
                                    edt_barcode.requestFocus();
                                    btn_search_barcode.setEnabled(true);
                                    JSONObject object = new JSONObject(response);
                                    //Log.e("onResponse", "object"+object);

                                    //JSONObject object2 = object.getJSONObject("user");
                                    //String username = object.getString("status");
                                    // Log.e("onResponse", "object2"+object2);

                                    String status = object.getString("status");
                                    Log.d("onResponse", status);

                                    if (status.equalsIgnoreCase("1")) {

                                         edt_barcode.setText(null);
                                            edt_barcode.requestFocus();
                                        txt_sales_today.setText(object.getString("qty"));
                                        txt_price.setText(object.getString("price"));
                                            // edit_current_deliver.setText(null);
//                                            CreateORUpdateRecycleView(postionForsave);
                                            Toast.makeText(ScanItemAvailabilityActivity.this, "تم", Toast.LENGTH_SHORT).show();


                                    } else {
                                       // Toast.makeText(ScanItemAvailabilityActivity.this, , Toast.LENGTH_SHORT).show();
                                  //      edt_barcode.setError("الباركود غير موجود");
                                        txt_sales_today.setText("0.0");
                                        edt_barcode.requestFocus();
                                    }

                                } catch (JSONException e) {
//                                    progressBar.setVisibility(View.GONE);
                                    btn_search_barcode.setEnabled(true);
                                    edt_barcode.setEnabled(true);
                                    edt_barcode.setError("خطأ بالأتصال بخادم POS");
                                    edt_barcode.setText("");
                                    edt_barcode.requestFocus();
                                    Toast.makeText(getBaseContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
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
                                    edt_barcode.setEnabled(true);
                                    btn_search_barcode.setEnabled(true);
                                    edt_barcode.setError("لم يتم أضافة الباركود .Net. حاول مره اخرى");
                                    edt_barcode.setText("");
                                    edt_barcode.requestFocus();
                                    Log.i("log error", errorString);
                                }
                            }
                        }
                ) {
                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        Log.i("log error no respon", "se6");
                        Log.i("log error no respon", ""+volleyError);
                        volleyErrorPublic=volleyError;
                        return super.parseNetworkError(volleyError);
                    }

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                            params.put("Article", Article);
                            Log.e("bbbbbbbbbbbb", "ifff" + Depart);

                            params.put("Barcode", editbarcode);
//                        if (check_of_UserCode.equalsIgnoreCase("01")) {
//                            params.put("Branch", "00001");
//                        }else if (check_of_UserCode.equalsIgnoreCase("03")) {
//                            params.put("Branch", "00003");
//                        }else {
                            params.put("Branch", check_of_UserCode);
//                        }
                        Log.i("sending ", editbarcode);
                        // Log.i("sending ", ""+request);

                        return params;
                    }

                };


                // Add the realibility on the connection.
                request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
                queue.add(request);

                if (volleyErrorPublic != null) {
                    Toast.makeText(ScanItemAvailabilityActivity.this, "لم يتم الاتصال بالسيرفر", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    public void createCSV(){
        File folder = new File("/data/user/0/com.example.connecttosoapapiapp/databases/");
        boolean var = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HHmmMM-a");

         CSVName = sdf.format(new Date()) + "_H" + check_of_UserCode +"0" ;
         Log.e("zzzz", "" + CSVName);

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

            itemAvailabilityModuleList = databaseHelperForItemAvailability.select_ItemsAvaiModule();
           /* writer.append("BarCode");
            writer.append(',');
            writer.append("QTY");
            writer.append(',');
            writer.append("Zone");
            writer.append('\n');*/

            if (itemAvailabilityModuleList.size() ==0){
                Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
            }else {
                Log.e("envelope", "Po_Item_For_ftp_Upload" + itemAvailabilityModuleList.size());

                for (int i = 0; i < itemAvailabilityModuleList.size(); i++) {

                    writer.append(itemAvailabilityModuleList.get(i).getBarCode1());
                    writer.append(',');
                    writer.append(itemAvailabilityModuleList.get(i).getUserName1());
                    writer.append(',');
                    writer.append(itemAvailabilityModuleList.get(i).getMessage1());
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
            MultipartUploadRequest multipartUploadRequest=  new MultipartUploadRequest(this, PdfID, Constant.UploadCSVFile_ItemAvailabilitye);
            multipartUploadRequest.addFileToUpload(filePath.getPath(), "pdf");

            Log.e("bbbbbb","nm,.,mcgvbnjmkl,"+filePath.getPath());
            // name & room_id & user_id & user_name &  type  this is key between android and php only

            multipartUploadRequest.addParameter("UserType", check_of_UserCode);
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
//            txt_response.setText("تم الرفع بهذا الأسم\n"+CSVName);

            //     progress_for_export.setVisibility(View.GONE);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_all_items))
                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            databaseHelperForItemAvailability.DeleteItemsAvai_ModuleleTable( );
                        }
                    })
                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    }).show();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ScanItemAvailabilityActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(ScanItemAvailabilityActivity.this,"أذن قراء من الكارت", Toast.LENGTH_LONG).show();
            UplaodingToFtp();
        } else {

            ActivityCompat.requestPermissions(ScanItemAvailabilityActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ScanItemAvailabilityActivity.this,"تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    UplaodingToFtp();
                } else {

                    Toast.makeText(ScanItemAvailabilityActivity.this,"تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}
