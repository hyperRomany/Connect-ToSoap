package com.example.connecttosoapapiapp.GIModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.GIModule.Helper.GIDataBaseHelper;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
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

import androidx.appcompat.app.AppCompatActivity;

public class ScanGIActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
    EditText editbarcodeforsoap,edit_QTY;
    GIDataBaseHelper giDataBaseHelper;
    private int LOADER_ID = 1;
    String EnvelopeBodyInConstant,EnvelopeBodyInConstant2 , EnvelopeBodyInCurrent , RETURN, MESSAGE,MATERIALDOCUMENT="Empty"
            ,GL,CS,Site,REC_SITE_LOG1="";
TextView txt_descripation,txt_code_item,txt_state_item,txt_available_in_site,
        txt_to_site,txt_GL,txt_CS;
    List <GIModule> Po_Item_For_Gi_Upload;
    View view;
    List<String> ReturnSearchList ;
    List<GIModule> CheckItemssize;
    boolean ForUpload=false;
    List<GIModule> GIModulelist_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_gi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editbarcodeforsoap=findViewById(R.id.editbarcodeforsoap);
        editbarcodeforsoap.requestFocus();

        giDataBaseHelper=new GIDataBaseHelper(this);
        txt_descripation=findViewById(R.id.txt_descripation);
        txt_code_item=findViewById(R.id.txt_code_item);
        txt_state_item=findViewById(R.id.txt_state_item);
        txt_available_in_site=findViewById(R.id.txt_available_in_site);
        txt_to_site=findViewById(R.id.txt_to_site);
        edit_QTY=findViewById(R.id.edit_QTY);
        txt_GL=findViewById(R.id.txt_GL);
        txt_CS=findViewById(R.id.txt_CS);

        Intent GetData=getIntent();
        if (GetData.getExtras() !=null) {
            GL = GetData.getExtras().getString("GL");
            CS = GetData.getExtras().getString("CostCenter");
            Site = GetData.getExtras().getString("Site");
            txt_GL.setText(GL);
            txt_CS.setText(CS);
            txt_to_site.setText(Site);
        }

        editbarcodeforsoap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER){

                    //execute our method for searching
                    SearchBarCode(view);
                }
                return false;
            }
        });

    }

    public void SearchBarCode(View view) {
            ForUpload =false;
        // edit_asked_from_site_search.setEnabled(false);
        if (editbarcodeforsoap.getText().toString().isEmpty()){
            editbarcodeforsoap.setError("من فضلك ادخل الباركود");
        }else {
             GIModulelist_btn = new ArrayList<>();
            //search if it is in local database
            GIModulelist_btn = giDataBaseHelper.Search__Barcode(editbarcodeforsoap.getText().toString());
            Log.e("GIModulelist is","size"+GIModulelist_btn.size());
            if (GIModulelist_btn.size() > 0) {
                if(GIModulelist_btn.get(0).getSTATUS1().equalsIgnoreCase("1")){
                    editbarcodeforsoap.setError("هذا الباركود غير فعال");
                } else {
                    txt_descripation.setText(GIModulelist_btn.get(0).getUOM_DESC1());
                    txt_code_item.setText(GIModulelist_btn.get(0).getMAT_CODE1().subSequence(6,18));
                    txt_state_item.setText(GIModulelist_btn.get(0).getSTATUS1());
                    Double total = Double.valueOf(GIModulelist_btn.get(0).getAVAILABLE_STOCK1()) - Double.valueOf(GIModulelist_btn.get(0).getQTY1());
                    txt_available_in_site.setText(String.valueOf(total));
//                    txt_from_site_search.setText(GIModulelist_btn.get(0).getISS_SITE1());
                    txt_to_site.setText(GIModulelist_btn.get(0).getREC_SITE1());
                }
            } else {
                Toast.makeText(this, "this else", Toast.LENGTH_SHORT).show();
//                edit_asked_from_site_search.setText("");
                txt_descripation.setText("وصف الصنف");
                txt_code_item.setText("كود الصنف");
                txt_state_item.setText("حاله الصنف");
                txt_available_in_site.setText("المتاح بالمخزن");
                editbarcodeforsoap.setError(null);
                getLoaderManager().initLoader(LOADER_ID, null, ScanGIActivity.this);
            }
        }

    }

    public void ShowBarcodes_ofGI(View view) {
            Intent GoToShow=new Intent(ScanGIActivity.this, ShowItemsForGIActivity.class);
            GoToShow.putExtra("GL",GL);
            GoToShow.putExtra("CostCenter",CS);
            GoToShow.putExtra("Site",Site);
            startActivity(GoToShow);
    }

    public void SaveBarCodeForGI(View view) {
        if (edit_QTY.getText().toString().isEmpty() || editbarcodeforsoap.getText().toString().isEmpty()
        || txt_code_item.getText().toString().isEmpty()){
            if (edit_QTY.getText().toString().isEmpty()){
                edit_QTY.setError("أدخل الكميه أولا");
            }else if (editbarcodeforsoap.getText().toString().isEmpty()){
                editbarcodeforsoap.setError("أدخل الباركود اولا");
            }else if (txt_code_item.getText().toString().isEmpty()){
                editbarcodeforsoap.setError("أضغط بحث اولا");
            }
        }else {

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.textforupdateQTY))
                    .setPositiveButton("تزويد القيه القديمه", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            giDataBaseHelper.Update_QTY(GIModulelist_btn.get(0).getQTY1()+edit_QTY.getText().toString(),editbarcodeforsoap.getText().toString());
                            txt_code_item.setText("");
                            txt_available_in_site.setText("");
                            txt_descripation.setText("");
                            txt_state_item.setText("");
                            edit_QTY.setText("");
                            editbarcodeforsoap.setText("");
                            editbarcodeforsoap.requestFocus();

                        }
                    })
                    .setNegativeButton("تعديل", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            giDataBaseHelper.Update_QTY(edit_QTY.getText().toString(),editbarcodeforsoap.getText().toString());
                            txt_code_item.setText("");
                            txt_available_in_site.setText("");
                            txt_descripation.setText("");
                            txt_state_item.setText("");
                            edit_QTY.setText("");
                            editbarcodeforsoap.setText("");
                            editbarcodeforsoap.requestFocus();
                        }
                    }).show();


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
                if (ForUpload == false){
//
                SoapObject request = new SoapObject(Constant.NAMESPACE_For_Search_Barcode, Constant.METHOD_For_Search_Barcode);
                request.addProperty("GTIN", editbarcodeforsoap.getText().toString());
                request.addProperty("ISS_SITE", txt_to_site.getText().toString().replaceAll(" ", ""));
                request.addProperty("REC_SITE", txt_to_site.getText().toString().replaceAll(" ", ""));

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

                ReturnSearchList = new ArrayList<>();
                ReturnSearchList.clear();

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
                                    if (!soapObject_items_detials.getProperty(5).toString().equalsIgnoreCase("anyType{}")){
                                        REC_SITE_LOG1 = soapObject_items_detials.getProperty(5).toString();
                                    }

                                }
                            }
                        }
                    }
                }
            }else if(ForUpload == true) {
                    SoapObject request =new SoapObject(Constant.NAMESPACE_For_Upload_GI, Constant.METHOD_For_Upload_GI);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                    Po_Item_For_Gi_Upload=new ArrayList<>();
                    Po_Item_For_Gi_Upload = giDataBaseHelper.selectGIModule_for_Qty();
                    MATERIALDOCUMENT ="Empty";
                    MESSAGE="Empty";

                    if (Po_Item_For_Gi_Upload.size() == 0){
                        Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
                    }else {
                        Log.e("envelope", "Po_Item_For_ftp_Upload"+Po_Item_For_Gi_Upload.size());

                        Vector v1 = new Vector();

                        for (int i = 0; i < Po_Item_For_Gi_Upload.size(); i++) {

                            SoapObject rate = new SoapObject(Constant.NAMESPACE_For_Upload_GI, "item");

                            rate.addProperty("MATERIAL", Po_Item_For_Gi_Upload.get(i).getMAT_CODE1());
                            rate.addProperty("PLANT", Po_Item_For_Gi_Upload.get(i).getREC_SITE1());
//                            rate.addProperty("PLANT", "02SA");

                            ///////////
                            rate.addProperty("STGE_LOC", Po_Item_For_Gi_Upload.get(i).getREC_SITE_LOG1());

                            rate.addProperty("ENTRY_QNT", Po_Item_For_Gi_Upload.get(i).getQTY1());
                            rate.addProperty("ENTRY_UOM", Po_Item_For_Gi_Upload.get(i).getMEINH1());

                            ///////////////////////////////

                            rate.addProperty("COSTCENTER", CS.replaceAll(" ",""));
                            rate.addProperty("GL_ACCOUNT", "00"+GL.replaceAll(" ",""));

                            ///********************

                            rate.addProperty("VENDOR", "");


                            rate.addProperty("DOC_DATE", year);
                            rate.addProperty("PSTNG_DATE", year);
                            rate.addProperty("EAN_UPC", Po_Item_For_Gi_Upload.get(i).getGTIN1());

                            v1.add(i, rate);
                        }
                        request.addProperty("IT_GOODSMVT", v1);
                        Log.e("envelope", "v1"+v1);


                    }
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Upload_GI);

                    try{
                        httpTransport.call(Constant.SOAP_ACTION_For_Upload_GI, envelope);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (envelope.bodyIn == null){
                        Log.e("envelope", "envelope.bodyIn == null");

                    }else {
                        Log.e("envelopebod", ""+envelope.bodyIn);
                        Log.e("envelope.bodyOut", ""+envelope.bodyOut);
                        Log.e("envelope", ""+((SoapObject)envelope.bodyOut).getPropertyCount());

                        EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";

                        EnvelopeBodyInConstant2 = "env:Receiver , Reason:  RABAX occurred on server side ";

                        EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant) ||
                                EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant2)){
                            // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                        }else {
                            SoapObject soapResponse = (SoapObject) envelope.bodyIn;
                            Log.d("envelopeProMESSAGE",""+soapResponse.getPropertyCount());
                            Log.d("envelopeProMESSAGE0", "" + soapResponse.getProperty(0));
                            Log.d("envelopeProMESSAGE1", "" + soapResponse.getProperty(1));
                            MATERIALDOCUMENT =soapResponse.getProperty(2).toString();
                            Log.d("envelopeProMESSAGE2", "" + soapResponse.getProperty(2));
                            Log.d("envelopeProMESSAGE3", "" + soapResponse.getProperty(3));

                            // for return
                            if (!soapResponse.getProperty(3).toString().equalsIgnoreCase("anyType{}")) {
                                SoapObject soapreturn = (SoapObject) soapResponse.getProperty(3);

                                Log.d("envelopeProMESSAGE", "" + soapreturn.getProperty(0));

                                SoapObject soapreturnitem = (SoapObject) soapreturn.getProperty(0);
                                Log.d("envelopeProMESSAGE", "" + soapreturnitem.getProperty(0));
                                Log.d("envelopeProMESSAGE", "" + soapreturnitem.getProperty("MESSAGE"));
                                MESSAGE=soapreturnitem.getProperty("MESSAGE").toString();
                            }
//                            MESSAGE=soapreturnitem.getProperty("MESSAGE").toString();
//                            Log.d("envelopeProMESSAGE", "" + soapreturn.getProperty("MESSAGE"));

//
//                            Log.d("envelopeProMESSAGE", "" + soapreturn.getProperty(0));
//                        SoapObject soapreturnitem = (SoapObject) soapreturn.getProperty(0);
//                            Log.d("envelopeProMESSAGE", "" + soapResponse.getProperty(0));
//                        MATERIALDOCUMENT = soapreturnitem.getProperty(3).toString();
//                        Log.d("envelopeProMESSAGE", "" + MATERIALDOCUMENT);
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
        Toast.makeText(ScanGIActivity.this, "finished ", Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.d("soaMESSAGE4", "" + MESSAGE);
        Log.e("This Is First Time", "" + RETURN);

        if (ForUpload == false) {
            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                editbarcodeforsoap.setError("" + EnvelopeBodyInCurrent);
//            edit_asked_from_site_search.setEnabled(false);
                // btn_loading_purchase_order.setEnabled(true);
            } else if (RETURN.contains("anyType{}")) {
                List<GIModule> GIModulelist_bg = new ArrayList<>();
                Log.e("This Is First Time", "" + RETURN);
                Toast.makeText(ScanGIActivity.this, RETURN, Toast.LENGTH_LONG).show();


//            GIModulelist_bg = giDataBaseHelper.Search__Barcode(editbarcodeforsoap.getText().toString());
                if (ReturnSearchList.get(8).equalsIgnoreCase("1")) {
                    editbarcodeforsoap.setError("هذا الباركود غير فعال");
//                edit_asked_from_site_search.setEnabled(false);
                } /*else if (GIModulelist_bg.get(0).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0")){
                editbarcodeforsoap.setError("لا يوجد كميه متاحه للتحويل");
//                edit_asked_from_site_search.setEnabled(false);
            }*/ else {

                    long id = giDataBaseHelper.insert_GIModule(editbarcodeforsoap.getText().toString(),
                            "01RM", Site, ReturnSearchList.get(0), ReturnSearchList.get(1),
                            ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                            ReturnSearchList.get(4), REC_SITE_LOG1, ReturnSearchList.get(6),
                            ReturnSearchList.get(7), "0.0");

                    //giDataBaseHelper.Update_Sto_header_For_Iss_Site_log(ReturnSearchList.get(4));
                    Log.e("ReturnSearchList", "" + REC_SITE_LOG1);
                    Log.e("ReturnSearchList", "" + ReturnSearchList.get(4));
                    Log.e("ReturnSearchList", "" + ReturnSearchList.get(5));
                    Log.d("For_each_itemk=1=9", ReturnSearchList.get(3) + ReturnSearchList.get(4) + ReturnSearchList.get(5));


                    txt_descripation.setText(ReturnSearchList.get(1));
                    txt_code_item.setText(ReturnSearchList.get(0).subSequence(6, 18));
                    txt_state_item.setText(ReturnSearchList.get(3));
                    txt_available_in_site.setText(ReturnSearchList.get(6));

//                Long id = giDataBaseHelper.Update_Sto_header_For_Iss_Site_log_if_has_anytypevalue(GIModulelist_bg.get(0).getISS_STG_LOG1());
//                Log.e("getISSbbbbb_STG",""+ GIModulelist_bg.get(0).getISS_STG_LOG1());
//                Log.e("getISSbbbbb_STG",""+id);

                    List<String> Iss_Site_Log_list = new ArrayList<>();
                    for (int i = 0; i < GIModulelist_bg.size(); i++) {
                        if (!Iss_Site_Log_list.contains(GIModulelist_bg.get(i).getISS_STG_LOG1())) {
                            Iss_Site_Log_list.add(GIModulelist_bg.get(i).getISS_STG_LOG1());
                            Log.e("getISS_STG", "" + GIModulelist_bg.get(i).getISS_STG_LOG1());
                        }
                    }

//                Iss_Site_Log_list.remove("anyType{}");
//
//                ArrayAdapter<String> adapter=new ArrayAdapter<String>(ScanGIActivity.this,
//                        android.R.layout.simple_spinner_item,Iss_Site_Log_list);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spiner_storage_location_from.setAdapter(adapter);
//
//                List<String> Rec_Site_Log_list = new ArrayList<>();
//
//                for (int i=0;i<GIModulelist_bg.size();i++){
//                    if (!Rec_Site_Log_list.contains(GIModulelist_bg.get(i).getREC_SITE_LOG1())){
//                        Rec_Site_Log_list.add(GIModulelist_bg.get(i).getREC_SITE_LOG1());
//                        Log.e("getISS_STG",""+ GIModulelist_bg.get(i).getREC_SITE_LOG1());
//                    }
//                }
//                Rec_Site_Log_list.remove("anyType{}");
//                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(ScanGIActivity.this,
//                        android.R.layout.simple_spinner_item,Rec_Site_Log_list);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spiner_storage_location_to.setAdapter(adapter2);
//
//                edit_asked_from_site_search.setEnabled(true);
//                edit_asked_from_site_search.requestFocus();
                }
            /*Go_To_ScanRecieving.putExtra("This Is First Time",This_Is_First_Time);
            Log.e("This Is First Time","true");
            startActivity(Go_To_ScanRecieving);
            btn_loading_purchase_order.setEnabled(true);*/
            } else {
                // Toast.makeText(ScanGIActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
//            edit_asked_from_site_search.setEnabled(false);
                editbarcodeforsoap.setError(MESSAGE);
                MESSAGE = " ";
                editbarcodeforsoap.requestFocus();
                //  btn_loading_purchase_order.setEnabled(true);
            }
        } else if (ForUpload == true) {
            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant) ||
                    EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant2)) {
                //Toast.makeText(ScanGIActivity.this, "خطأ فى البيانات .. أنظر لل log", Toast.LENGTH_SHORT).show();
                editbarcodeforsoap.setError("خطأ فى البيانات .. أنظر لل log");
                editbarcodeforsoap.requestFocus();
            } else if (MATERIALDOCUMENT.equalsIgnoreCase("Empty") || MATERIALDOCUMENT.contains("anyType{}")) {
                if (MESSAGE.equalsIgnoreCase("anyType{}")) {
                    Toast.makeText(ScanGIActivity.this, "هناك مشكله", Toast.LENGTH_SHORT).show();
                    editbarcodeforsoap.setError("هناك مشكله");
                    editbarcodeforsoap.requestFocus();
                }else {
                    Toast.makeText(ScanGIActivity.this, ""+MESSAGE, Toast.LENGTH_SHORT).show();
                    editbarcodeforsoap.setError(MESSAGE);
                    editbarcodeforsoap.requestFocus();
                }
            }else {
//                Toast.makeText(ScanGIActivity.this, ""+MATERIALDOCUMENT, Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setTitle("تم الأرسال برقم  "+MATERIALDOCUMENT)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent GoBack=new Intent(ScanGIActivity.this,MainFormOfGIActivity.class);
                                startActivity(GoBack);

                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).show();

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ScanGIActivity.this , MainFormOfGIActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void UploadBarCodesForGI(View view) {
        if (giDataBaseHelper.selectGIModule_for_Qty().size()>0) {
            ForUpload = true;
            getLoaderManager().initLoader(LOADER_ID, null, ScanGIActivity.this);
        }else {
            Toast.makeText(this, "لايوجد باركودات لها كميه للرفع", Toast.LENGTH_SHORT).show();
        }
//        Log.e("ccccccccccccc","from scan");
//
//        UploadGiActivity uploadGiActivity=new UploadGiActivity(ScanGIActivity.this);
//        uploadGiActivity.onclickUploadGI();
//
//        uploadGiActivity.getLoaderManager().initLoader(LOADER_ID, null, this);

    }
}
