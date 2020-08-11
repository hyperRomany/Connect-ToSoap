package com.example.connecttosoapapiapp.ItemReturn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class UploadItemReturnActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
TextView txt_reason,txt_retun_number;
    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE ,PO_NO;
    List<Item_Return_Search> item_return_searcheslist;
    List<Item_Return_Header> item_return_headerslist;
    DatabaseHelperForItemReturn databaseHelperForItemReturn;
    private int LOADER_ID = 1;
String Date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item_return);


        databaseHelperForItemReturn = new DatabaseHelperForItemReturn(this);
        txt_retun_number=findViewById(R.id.txt_retun_number);
        txt_reason=findViewById(R.id.txt_reason);
        item_return_searcheslist = new ArrayList<>();
        item_return_searcheslist=databaseHelperForItemReturn.selectSto_Search_for_Qty();

        item_return_headerslist = new ArrayList<>();
        item_return_headerslist=databaseHelperForItemReturn.selectReturn_Header();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       Date = sdf.format(new Date());

        getLoaderManager().initLoader(LOADER_ID, null, UploadItemReturnActivity.this);

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

}
