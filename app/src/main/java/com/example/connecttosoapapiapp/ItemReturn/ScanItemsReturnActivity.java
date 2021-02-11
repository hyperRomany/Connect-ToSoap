package com.example.connecttosoapapiapp.ItemReturn;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ScanItemsReturnActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
    TextView txt_vendor_name,txt_org,txt_des,txt_matcode,edt_qty,txt_uom;
    EditText edt_barcode;
    Button btn_generate_item_return,btn_search_barcode;
    String VENDOR,SITE,P_ORG ;
    DatabaseHelperForItemReturn databaseHelperForItemReturn;

    private int LOADER_ID = 1;
    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE ;
    List<String> ReturnSearchList ;
    List<Item_Return_Search> CheckItemssize;
    List<Item_Return_Header> item_return_headerslist;
    List<Item_Return_Search> item_return_searcheslist;
    List<Item_Return_Header> headerList;
    List<Item_Return_Search> STo_searchlist;
    DatabaseHelper databaseHelper;
    List<Users> userdataList;
    String check_of_UserCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_items_return);

        databaseHelperForItemReturn=new DatabaseHelperForItemReturn(this);

        txt_vendor_name=findViewById(R.id.txt_vendor_name);
        txt_org=findViewById(R.id.txt_org);
        txt_des=findViewById(R.id.txt_des);
        txt_matcode=findViewById(R.id.txt_matcode);
        edt_qty=findViewById(R.id.edt_qty);
        txt_uom=findViewById(R.id.txt_uom);
        edt_barcode=findViewById(R.id.edt_barcode);
        btn_generate_item_return=findViewById(R.id.btn_generate_item_return);
        btn_search_barcode=findViewById(R.id.btn_search_barcode);

        // To get user code
        databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();

        userdataList = databaseHelper.getUserData();
//        txt_user_code.setText(userdataList.get(0).getCompany1());
        check_of_UserCode=userdataList.get(0).getCompany1().toString().substring(1,3)+"RT";
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();
//        txt_user_code.setText("01RT");
        Log.e("zzzzzz",":"+check_of_UserCode);

        headerList = databaseHelperForItemReturn.selectReturn_Header();
        if (headerList.size() !=0){
            VENDOR=headerList.get(0).getVendor1();
            txt_vendor_name.setText(VENDOR);
            P_ORG=headerList.get(0).getP_org1();
            txt_org.setText(P_ORG);
          //  SITE="01SA";  //=headerList.get(0).getSite1();

        }
        Log.e("zzzzzz",""+VENDOR);

    }

    public void GenerateReturnItem(View view) {
        item_return_searcheslist = new ArrayList<>();
        item_return_searcheslist=databaseHelperForItemReturn.selectSto_Search_for_Qty();
        if (item_return_searcheslist.size() != 0) {
            Intent GoToCreate = new Intent(ScanItemsReturnActivity.this, UploadItemReturnActivity.class);
            startActivity(GoToCreate);
        }else {
            Toast.makeText(this, "لا يوجد بيانات للرفع", Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveBarCodeForItemReturn(View view) {

        if (edt_barcode.getText().toString().isEmpty()){
            edt_barcode.setError("من فضلك ادخل الباركود");
            edt_barcode.requestFocus();
        }else if (edt_qty.getText().toString().isEmpty()){
            edt_qty.setError("من فضلك ادخل الكمية");
        }else {

             STo_searchlist = new ArrayList<>();
            //search if it is in local database
            STo_searchlist = databaseHelperForItemReturn.Search__Barcode(edt_barcode.getText().toString());
            Log.e("STo_searchlist is","size"+STo_searchlist.size());
            if (Double.valueOf(STo_searchlist.get(0).getQTY1() ) > 0) {

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.textforupdateQTY))
                        .setPositiveButton("تزويد القيه القديمه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseHelperForItemReturn.Update_Sto_Search(edt_barcode.getText().toString(),
                                        String.valueOf(Double.valueOf(STo_searchlist.get(0).getQTY1() )
                                                + Double.valueOf(edt_qty.getText().toString())));
                                edt_barcode.setText("");
                                edt_qty.setText("");
                                txt_des.setText("الوصف");
                                txt_matcode.setText("كود الصنف");
                                txt_uom.setText("الموقع");
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("تعديل", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseHelperForItemReturn.Update_Sto_Search(edt_barcode.getText().toString(), edt_qty.getText().toString());
                                edt_barcode.setText("");
                                edt_qty.setText("");
                                txt_des.setText("الوصف");
                                txt_matcode.setText("كود الصنف");
                                txt_uom.setText("الموقع");
                                dialog.cancel();
                            }
                        }).show();

            }else {
                databaseHelperForItemReturn.Update_Sto_Search(edt_barcode.getText().toString(), edt_qty.getText().toString());
                edt_barcode.setText("");
                edt_qty.setText("");
                txt_des.setText("الوصف");
                txt_matcode.setText("كود الصنف");
                txt_uom.setText("الموقع");
            }

        }
    }

    public void ShowItems(View view) {
        Intent gotoShowItem=new Intent(ScanItemsReturnActivity.this, ShowItemsReturnItemActivity.class);
        startActivity(gotoShowItem);
    }


    public void Search_Barcode(View view) {
        // edit_asked_from_site_search.setEnabled(false);
        if (edt_barcode.getText().toString().isEmpty()){
            edt_barcode.setError("من فضلك ادخل الباركود");
        }else {
            List<Item_Return_Search> STo_searchlist = new ArrayList<>();
            //search if it is in local database
            STo_searchlist = databaseHelperForItemReturn.Search__Barcode(edt_barcode.getText().toString());
            Log.e("STo_searchlist is","size"+STo_searchlist.size());
            if (STo_searchlist.size() > 0) {
                if(STo_searchlist.get(0).getSTATUS1().equalsIgnoreCase("1")){
                    edt_barcode.setError("هذا الباركود غير فعال");
                }
                else {
                    edt_qty.requestFocus();
                    txt_des.setText(STo_searchlist.get(0).getDesc1());
                    txt_matcode.setText(STo_searchlist.get(0).getMAT_CODE1().subSequence(6,18));
                    txt_uom.setText(STo_searchlist.get(0).getDEF_STG_LOC1());

                }
            } else {
                Toast.makeText(this, "this else", Toast.LENGTH_SHORT).show();
                txt_des.setText("وصف الصنف");
                txt_matcode.setText("كود الصنف");
                txt_uom.setText("الموقع");
                getLoaderManager().initLoader(LOADER_ID, null, ScanItemsReturnActivity.this);
            }
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
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Search_Barcode_returnitem , Constant.METHOD_For_Search_Barcode_returnitem);
                request.addProperty("GTIN", edt_barcode.getText().toString());
                request.addProperty("P_ORG", P_ORG);
                request.addProperty("SITE", check_of_UserCode);
                request.addProperty("VENDOR", VENDOR);

                RETURN="Empty";
                MESSAGE="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                //envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Search_Barcode_returnitem);
                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Search_Barcode_returnitem, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d("envelope", ""+envelope.bodyIn);
                Log.d("envelope", ""+envelope.bodyOut);
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                ReturnSearchList = new ArrayList<>();


                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;

                    Log.d("getPropertyCoun", String.valueOf(soapObject_All_response.getPropertyCount()));

                    for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                        Log.d("soapObject", String.valueOf(soapObject_All_response.getProperty(i)));

                        SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);
                        RETURN = (String) soapObject_All_response.getProperty(i).toString();
                        Log.d("getPropertyCounitems", String.valueOf(soapObject_items.getPropertyCount()));

                        if (i ==0) {
                            for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {
                                SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);
//                                for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {
                                    // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);
                                    Log.d("For_each_itemwith_k_def", String.valueOf(soapObject_items_detials.getProperty(11)));
                                    if (String.valueOf(soapObject_items_detials.getProperty(11)).equalsIgnoreCase("X")) {
                                        Log.e("TAG", "loadInBackground: inset " );
                                        databaseHelperForItemReturn.insert_Item_Return_Search(
                                                soapObject_items_detials.getProperty(0).toString(),
                                                soapObject_items_detials.getProperty(2).toString(),
                                                soapObject_items_detials.getProperty(1).toString(),
                                                soapObject_items_detials.getProperty(4).toString(),
                                                "0.0",
                                                soapObject_items_detials.getProperty(3).toString(),
                                                soapObject_items_detials.getProperty(5).toString(),
                                                soapObject_items_detials.getProperty(6).toString(),
                                                soapObject_items_detials.getProperty(8).toString(),
                                                soapObject_items_detials.getProperty(9).toString());
                                    }
//                                }
                            }
                        }else if (i==1){
                            for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {
                                SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);
                                for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {
                                    // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);
                                    Log.d("For_each_itemwith_k", String.valueOf(soapObject_items_detials.getProperty(k)));
                                    MESSAGE=soapObject_items_detials.getProperty(1).toString();
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
        Toast.makeText(ScanItemsReturnActivity.this,"finished ",Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.d("soaMESSAGE4",""+ MESSAGE);
        Log.e("This Is First Time",""+RETURN);

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            edt_barcode.setError(""+EnvelopeBodyInCurrent);
            // btn_loading_purchase_order.setEnabled(true);
        }else if(RETURN.contains("anyType{}") ){
            List<Item_Return_Search> STo_searchlist_bg=new ArrayList<>();
            Log.e("This Is First Time",""+RETURN);
            Toast.makeText(ScanItemsReturnActivity.this,RETURN,Toast.LENGTH_LONG).show();
            STo_searchlist_bg = databaseHelperForItemReturn.Search__Barcode(edt_barcode.getText().toString());
            Log.e("TAG", "onLoadFinished:ss "+STo_searchlist_bg.size() );
            if (STo_searchlist_bg.get(0).getSTATUS1().equalsIgnoreCase("1")){
                edt_barcode.setError("هذا الباركود غير فعال");
            } else {
                txt_des.setText(STo_searchlist_bg.get(0).getDesc1());
                txt_matcode.setText(STo_searchlist_bg.get(0).getMAT_CODE1().subSequence(6,18));
            }
            /*Go_To_ScanRecieving.putExtra("This Is First Time",This_Is_First_Time);
            Log.e("This Is First Time","true");
            startActivity(Go_To_ScanRecieving);
            btn_loading_purchase_order.setEnabled(true);*/
        }else {
            // Toast.makeText(ScanItemsReturnActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
            edt_barcode.setError(MESSAGE);
            MESSAGE=" ";
            //  btn_loading_purchase_order.setEnabled(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }



}
