package com.example.connecttosoapapiapp.ItemReturn;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
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
import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Items_For_Logs_Items_SqlServer;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

public class UploadItemReturnActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
TextView txt_reason,txt_retun_number;
    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE ,PO_NO;
    List<Item_Return_Search> item_return_searcheslist;
    List<Item_Return_Header> item_return_headerslist;
    DatabaseHelperForItemReturn databaseHelperForItemReturn;
    private int LOADER_ID = 1;
String Date="";
    public StringRequest request=null;
    public JsonObjectRequest requestitems=null;
    List<Users> userList;
    DatabaseHelper databaseHelper;
    String MachaineName,Device_id_Instance_of_MacAdress;
    List<Po_Item> Po_Item_For_Log_only_Has_value;
    List<Po_Items_For_Logs_Items_SqlServer> Po_Items_For_LogsArray;
    List<Item_Return_Header> ItemReturn_HeaderList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item_return);


        databaseHelperForItemReturn = new DatabaseHelperForItemReturn(this);
        databaseHelper =new DatabaseHelper(this);
        userList=databaseHelper.getUserData();
        txt_retun_number=findViewById(R.id.txt_retun_number);
        txt_reason=findViewById(R.id.txt_reason);
        Po_Item_For_Log_only_Has_value= new ArrayList<>();
        item_return_searcheslist = new ArrayList<>();
        ItemReturn_HeaderList=new ArrayList<>();
        item_return_searcheslist=databaseHelperForItemReturn.selectSto_Search_for_Qty();
       // Po_Item_For_Log_only_Has_value=databaseHelperForItemReturn.selectSto_Search_for_Qty();
        ItemReturn_HeaderList=databaseHelperForItemReturn.selectReturn_Header();

        item_return_headerslist = new ArrayList<>();
        item_return_headerslist=databaseHelperForItemReturn.selectReturn_Header();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Date = sdf.format(new Date());
        MachaineName = android.os.Build.MODEL;
        Log.d("Build.MODEL",""+MachaineName);

        //this is not work in 6.0 and up
        Device_id_Instance_of_MacAdress = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        Log.e("onCreateU", "Map"+Device_id_Instance_of_MacAdress);

        getLoaderManager().initLoader(LOADER_ID, null, UploadItemReturnActivity.this);

//        WriteInLogs_sap_ITEMStableOfSqlServer();
//        PO_NO="11111111";
//        Log.e("TAG", "gfhjk0"+PO_NO );
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
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_create_returnitem ,
                        Constant.METHOD_For_create_returnitem);

                // this is from user table
                // To get user code
//        databaseHelper=new DatabaseHelper(this);
//        userdataList=new ArrayList<>();
//
//        userdataList = databaseHelper.getUserData();
//        txt_user_code.setText(userdataList.get(0).getCompany1());
//        check_of_UserCode=txt_user_code.getText().toString().substring(1,3);
//        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();

                request.addProperty("COMP_CODE", "H010");


                Vector v1 = new Vector();

                for (int i = 0; i < item_return_searcheslist.size(); i++) {

                    SoapObject rate = new SoapObject(Constant.NAMESPACE_For_create_returnitem, "item");

                    rate.addProperty("MAT_CODE", item_return_searcheslist.get(i).getMAT_CODE1());
                    rate.addProperty("GTIN", item_return_searcheslist.get(i).getGTIN1());
                    rate.addProperty("QTY", item_return_searcheslist.get(i).getQTY1());
                    rate.addProperty("UOM", item_return_searcheslist.get(i).getUOM1());
                    rate.addProperty("RET_FLAG", "X");
                    rate.addProperty("DELV_DATE", Date);
                    rate.addProperty("ISS_SITE", item_return_searcheslist.get(i).getSTG_LOC1());
                    rate.addProperty("STRG_LOC", item_return_searcheslist.get(i).getDEF_STG_LOC1());
                        /*
                        int index = Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().indexOf(".")+2;
                        // Log.d("po_itemlistsize","index"+index);

                        String QTY= String.valueOf(Po_Item_For_ftp_Upload.get(i).getPDNEWQTY1().substring(0,index));

                        rate.addProperty("ENTRY_QNT",QTY ); */


                    v1.add(i, rate);
                }
                request.addProperty("IT_PO_ITEMS", v1);
                Log.e("envelope", "v1"+v1);


                request.addProperty("PO_TYPE", "ZRTS");
                request.addProperty("P_GRP", item_return_headerslist.get(0).getPgrp1());
                request.addProperty("P_ORG", item_return_headerslist.get(0).getP_org1());
                request.addProperty("VENDOR", item_return_headerslist.get(0).getVendor1());



                RETURN="Empty";
                MESSAGE="Empty";
                PO_NO="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                //envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_create_returnitem);
                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_create_returnitem, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("envelope", ""+envelope.bodyIn);
                Log.d("envelope", ""+envelope.bodyOut);
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                item_return_searcheslist = new ArrayList<>();


                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;

                    Log.d("getPropertyCoun", String.valueOf(soapObject_All_response.getPropertyCount()));

                    for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                        Log.d("soapObject", String.valueOf(soapObject_All_response.getProperty(i)));

                        RETURN = (String) soapObject_All_response.getProperty(i).toString();

                        if (i ==0) {
                            SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);

                            for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {
                                SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);
//                                for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {
                                // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);

                            }
                        }else if (i==1){
                            SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);

                            MESSAGE=" ";
                            for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {
                                SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);
                                for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {
                                    // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);
                                    Log.d("For_each_itemwith_k", String.valueOf(soapObject_items_detials.getProperty(k)));
                                    if (k ==1) {
                                        MESSAGE += soapObject_items_detials.getProperty(1).toString() + "\n";
                                    }
                                }
                            }
                        }else if (i == 2){
                            PO_NO = (String) soapObject_All_response.getProperty(i).toString();

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
        Toast.makeText(UploadItemReturnActivity.this,"finished ",Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.d("soaMESSAGE4",""+ MESSAGE);
        Log.e("This Is First Time",""+RETURN);

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            txt_reason.setError(""+EnvelopeBodyInCurrent);
            // btn_loading_purchase_order.setEnabled(true);
        }else if(!PO_NO.contains("Empty") ){
            txt_retun_number.setText(PO_NO);
            txt_reason.setText(MESSAGE);
            WriteInLogOf_sapTableOfSqlServer();
            WriteInLogs_sap_ITEMStableOfSqlServer();
        }else {
            // Toast.makeText(UploadItemReturnActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
            txt_reason.setText(MESSAGE);
            MESSAGE=" ";
            //  btn_loading_purchase_order.setEnabled(true);
        }
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
               // Log.d("Build.MODELMac1",""+Po_HeaderList.get(0).getDelievered_BY1());
                params.put("Department", "ItemReturn");
                params.put("MachineName",MachaineName);
                Log.d("Build.MODELMac2",""+MachaineName);
                params.put("MACAddress", Device_id_Instance_of_MacAdress);
                Log.d("Build.MODELMac3",""+Device_id_Instance_of_MacAdress);
                params.put("Modulename", "ItemReturn");
                params.put("Company", userList.get(0).getCompany1());
               // Log.d("Build.MODELMac4",""+item_return_searcheslist.get(0).getCOMP_CODE1());
                params.put("DateTime",Date);
                Log.d("Build.MODELMac5",""+Date);

                params.put("Sap_OrderNo", PO_NO);
//               Log.d("Sap_OrderNo",""+From_Sap_Or_Not);
                    /// Toast.makeText(UploadForTransferActivity.this, "from sap", Toast.LENGTH_SHORT).show();


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
                Log.d("Build.MODELMac6", "item_return_searcheslist.size()" + item_return_searcheslist.size());
                item_return_searcheslist=databaseHelperForItemReturn.selectSto_Search_for_Qty();

                for (int i=0;i<item_return_searcheslist.size();i++) {

                    Po_Items_For_Logs_Items_SqlServer po_items_for_logs_items_sqlServer=
                            new Po_Items_For_Logs_Items_SqlServer();

                    //po_items_for_logs_items_sqlServer.setID( String.valueOf(104 + i));
                    po_items_for_logs_items_sqlServer.setSAP_STO_NUMBER( PO_NO);
                    Log.d("Build.MODELMac6", "PO_NO" + PO_NO);
                    po_items_for_logs_items_sqlServer.setP_ORG(  ItemReturn_HeaderList.get(0).getP_org1());
                    Log.d("Build.MODELMac6", "getP_org1" + ItemReturn_HeaderList.get(0).getP_org1());

                    po_items_for_logs_items_sqlServer.setP_GRP( ItemReturn_HeaderList.get(0).getPgrp1());
                    Log.d("Build.MODELMac6", "getPgrp1" + ItemReturn_HeaderList.get(0).getPgrp1());

                    po_items_for_logs_items_sqlServer.setISS_SITE( item_return_searcheslist.get(i).getSTG_LOC1());
                    Log.d("Build.MODELMac6", "getSTG_LOC1" +  item_return_searcheslist.get(i).getSTG_LOC1());

                    po_items_for_logs_items_sqlServer.setISS_STRG_LOC( item_return_searcheslist.get(i).getDEF_STG_LOC1());
                    Log.d("Build.MODELMac6", "getDEF_STG_LOC1" + item_return_searcheslist.get(i).getDEF_STG_LOC1());

                    po_items_for_logs_items_sqlServer.setREC_SITE( item_return_searcheslist.get(i).getSTG_LOC1());
                    Log.d("Build.MODELMac6", "getSTG_LOC1" + item_return_searcheslist.get(i).getSTG_LOC1());

                    po_items_for_logs_items_sqlServer.setREC_STRG_LOC( item_return_searcheslist.get(i).getDEF_STG_LOC1());
                    Log.d("Build.MODELMac6", "setREC_STRG_LOC" +  item_return_searcheslist.get(i).getDEF_STG_LOC1());

                    po_items_for_logs_items_sqlServer.setSTO_CR_DATE( Date);
                    Log.d("Build.MODELMac6", "Date" + Date);

                    po_items_for_logs_items_sqlServer.setMAT_CODE( item_return_searcheslist.get(i).getMAT_CODE1());
                    Log.d("Build.MODELMac6", "getMAT_CODE1" + item_return_searcheslist.get(i).getMAT_CODE1());

                    po_items_for_logs_items_sqlServer.setGTIN( item_return_searcheslist.get(i).getGTIN1());
                    Log.d("Build.MODELMac6", "getGTIN1" + item_return_searcheslist.get(i).getGTIN1());

                    po_items_for_logs_items_sqlServer.setGTIN_Desc( item_return_searcheslist.get(i).getDesc1());
                    Log.d("Build.MODELMac6", "getDesc1" + item_return_searcheslist.get(i).getDesc1());

                    po_items_for_logs_items_sqlServer.setMEINH( item_return_searcheslist.get(i).getUOM1());
                    Log.d("Build.MODELMac6", "getUOM1" + item_return_searcheslist.get(i).getUOM1());

                    po_items_for_logs_items_sqlServer.setQTY( item_return_searcheslist.get(i).getQTY1());
                    Log.d("Build.MODELMac6", "getQTY1" + item_return_searcheslist.get(i).getQTY1());

                    po_items_for_logs_items_sqlServer.setUSER_IDD( userList.get(0).getUser_ID1());
                    Log.d("Build.MODELMac6", "getUser_ID1" + userList.get(0).getUser_ID1());

                    po_items_for_logs_items_sqlServer.setUSER_NAMME(userList.get(0).getUser_Name1());
                    Log.d("Build.MODELMac6", "getUser_Name1" + userList.get(0).getUser_Name1());

                    po_items_for_logs_items_sqlServer.setMODULE( "ItemReturn");
                    Log.d("Build.MODELMacRCV", "ItemReturn");

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
