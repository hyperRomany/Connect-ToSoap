package com.example.connecttosoapapiapp.CycleCount;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class ScanActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {


    private TextView txt_num_of_barcodes,txt_describtion,txt_unite,txt_Serial;
     private Button btn_get_info,btn_search_barcode,save_Qty;
     private EditText edit_from,edit_to ,edit_Barcode,edit_qty ;
     private int LOADER_ID = 2;
     private List<Po_Item_of_cycleCount> po_item_of_cycleCountsforsave;
     private String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE;
     private DatabaseHelperForCycleCount databaseHelperForCycleCount;
     private ProgressBar progress_get_info;
     private AsyncTaskLoader asyncTaskLoader= null;
     private HashMap<String ,String> hashMap;
     private ContentValues values;
     private String Respones = "NO";


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progress_get_info=findViewById(R.id.progress_get_info);
        txt_num_of_barcodes=findViewById(R.id.txt_num_of_barcodes);
        txt_describtion=findViewById(R.id.txt_describtion);
        txt_unite=findViewById(R.id.txt_unite);
        txt_Serial=findViewById(R.id.txt_Serial);

        edit_from=findViewById(R.id.edit_from);
        edit_to=findViewById(R.id.edit_to);
        edit_Barcode=findViewById(R.id.edit_Barcode);
        edit_Barcode.requestFocus();
        edit_qty=findViewById(R.id.edit_qty);
        btn_get_info=findViewById(R.id.btn_get_info);
        btn_search_barcode=findViewById(R.id.btn_search_barcode);
        save_Qty=findViewById(R.id.save_Qty);

        databaseHelperForCycleCount=new DatabaseHelperForCycleCount(this);

        Intent getData=getIntent();

        if ( getData.getExtras()!= null) {
            edit_from.setText(getIntent().getExtras().getString("DocumentNuberfrom"));
            edit_to.setText(getIntent().getExtras().getString("DocumentNuberto"));
            edit_from.setEnabled(false);
            edit_to.setEnabled(false);
        }
        else {
            edit_from.setText(null);
            edit_to.setText(null);
            btn_get_info.setEnabled(false);
            edit_from.setEnabled(false);
            edit_to.setEnabled(false);
            List<String> PHYSINVENTORY  = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();
            if (PHYSINVENTORY.size()>0) {
                if (String.valueOf(PHYSINVENTORY.get(0).charAt(0)).equalsIgnoreCase("0")) {
                    edit_from.setText(PHYSINVENTORY.get(0).substring(1));
                } else {
                    edit_from.setText(PHYSINVENTORY.get(0));
                }

                if (String.valueOf(PHYSINVENTORY.get(PHYSINVENTORY.size() - 1).charAt(0)).equalsIgnoreCase("0")) {
                    edit_to.setText(PHYSINVENTORY.get(PHYSINVENTORY.size() - 1).substring(1));
                } else {
                    edit_to.setText(PHYSINVENTORY.get(PHYSINVENTORY.size() - 1));
                }
            }
        }


        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
        long Num =databaseHelperForCycleCount.Num_of_items_localDataBase();
        txt_num_of_barcodes.setText(String.valueOf(Num));
        if (po_item_list.size() >0){
            btn_get_info.setEnabled(false);
        }


        btn_get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_from.getText().toString().isEmpty()){
                    edit_from.setError("أدخل الرقم ");
                }else {
                    progress_get_info.setVisibility(View.VISIBLE);
                    btn_get_info.setVisibility(View.GONE);
                    Respones="No";
                    getLoaderManager().initLoader(LOADER_ID, null, ScanActivity.this);
                }
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
          asyncTaskLoader =  new AsyncTaskLoader(this){

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (!Respones.equalsIgnoreCase("No")){

                    asyncTaskLoader.cancelLoadInBackground();
                    getLoaderManager().destroyLoader(LOADER_ID);
                    Respones = Respones.replaceAll(";","");
                    Respones = Respones.replaceAll("\\،","");
                    Respones = Respones.replaceAll("\\'","");
//
                    Respones = Respones.replaceAll("\\,","");


                    Respones = Respones.replaceAll("anyType\\{\\}","");
                    Respones = Respones.replaceAll("item=anyType\\{PHYSINVENTORY=","('");


                    Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG") ,Respones.indexOf("MATERIAL=")) ,"");

                    Respones = Respones.replaceAll("MATERIAL=","','");
                    Respones = Respones.replaceAll("MAKTX=","','0.0','");
                    Respones = Respones.replaceAll("BASE_UOM=","','");
                    Respones = Respones.replaceAll("ITEM=","','");
                    Respones = Respones.replaceAll("SERNP=","','");
                    Respones = Respones.replaceAll("EAN11=","','");
                    Respones = Respones.replaceAll("MEINH=","','");
                    Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("ITEMS_COUNT") ,Respones.indexOf("ITEMS_COUNT")+19) ,"");
                    Respones = Respones.replaceAll("\\}","'),");
                    Respones = Respones.replaceAll("anyType\\{","");

                    String insertRows = "INSERT INTO 'Po_Item_of_cycleCount' ('PHYSINVENTORY','MATERIAL','QTY','MAKTX','BASE_UOM','ITEM','SERNP','EAN11','MEINH') VALUES";
                    insertRows +=Respones;
                    Log.e("zzzzzzz",""+insertRows);
                    insertRows = insertRows.substring(0,insertRows.length()-5) +";";
                    Log.e("zzzzzzz",""+insertRows);

                    databaseHelperForCycleCount.insertitems(insertRows);


                    List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
                    po_item_list=databaseHelperForCycleCount.Select_Num_of_items_localDataBase();
                    txt_num_of_barcodes.setText(String.valueOf(po_item_list.size()));
                    progress_get_info.setVisibility(View.GONE);
                    btn_get_info.setVisibility(View.VISIBLE);

                    }else {
                        forceLoad();
                  }
                btn_get_info.setEnabled(false);
            }
            @Override
            public Object loadInBackground() {
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Get_Cycle_Count , Constant.METHOD_For_Get_Cycle_Count);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                request.addProperty("FISCALYEAR", year);
                request.addProperty("PHYSINVENTORY_FROM", edit_from.getText().toString());

                if (edit_to.getText().toString().isEmpty()){
                    request.addProperty("PHYSINVENTORY_TO", "?");
                }
                else {
                    request.addProperty("PHYSINVENTORY_TO", edit_to.getText().toString());
                }

                RETURN="Empty";
                MESSAGE="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                //envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Get_Cycle_Count,6000000);
                try{

                    httpTransport.call(Constant.SOAP_ACTION_For_Get_Cycle_Count, envelope);

                }catch (Exception e){
                    e.printStackTrace();
                }
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else if (envelope.bodyIn.toString().isEmpty()){

                }else {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;

                    SoapObject soapObject_Items_Return = (SoapObject) soapObject_All_response.getProperty(0);
                    SoapObject soapObject_only_Return = (SoapObject) soapObject_All_response.getProperty(1);


                    if (soapObject_Items_Return.getPropertyCount() >0 ) {

                        Respones = "";
                         Respones = ""+soapObject_Items_Return;

                    }else {

                        SoapObject soapObject_Items_of_Return = (SoapObject) soapObject_only_Return.getProperty(0);
                        MESSAGE = (String) soapObject_Items_of_Return.getProperty("MESSAGE").toString();

                    }

                }

                return null;
            }


        };
            return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        getLoaderManager().destroyLoader(LOADER_ID);


        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            edit_from.setError("من فضلك انظر لل Log");
            edit_from.requestFocus();
            txt_describtion.setText(null);
            btn_get_info.setEnabled(true);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
        }
        else if(!MESSAGE.contains("Empty") ){

            edit_from.setError(MESSAGE);
            edit_from.requestFocus();
            txt_describtion.setText(null);
            btn_get_info.setEnabled(true);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
        }
        else {

            Respones = Respones.replaceAll(";","");
            Respones = Respones.replaceAll("\\،","");
            Respones = Respones.replaceAll("\\'","");

            Respones = Respones.replaceAll("\\,","");



            Respones = Respones.replaceAll("anyType\\{\\}","");
            Respones = Respones.replaceAll("item=anyType\\{PHYSINVENTORY=","('");
            Respones = Respones.replaceAll("MATERIAL=","','");
            Respones = Respones.replaceAll("MAKTX=","','0.0','");
            Respones = Respones.replaceAll("BASE_UOM=","','");
            Respones = Respones.replaceAll("ITEM=","','");
            Respones = Respones.replaceAll("SERNP=","','");
            Respones = Respones.replaceAll("EAN11=","','");
            Respones = Respones.replaceAll("MEINH=","','");


            Respones = Respones.replaceAll("\\}","'),");
            Respones = Respones.replaceAll("anyType\\{","");



            String insertRows = "INSERT INTO 'Po_Item_of_cycleCount' ('PHYSINVENTORY','MATERIAL','QTY','MAKTX','BASE_UOM','ITEM','SERNP','EAN11','MEINH') VALUES";
            insertRows +=Respones;
            insertRows = insertRows.substring(0,insertRows.length()-5) +";";

            databaseHelperForCycleCount.insertitems(insertRows);



            List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
            po_item_list=databaseHelperForCycleCount.Select_Num_of_items_localDataBase();
            txt_num_of_barcodes.setText(String.valueOf(po_item_list.size()));
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }


    public void Upload_saved_Data(View view) {
        Intent GotoUpload=new Intent(ScanActivity.this, UploadCycleCountActivity.class);
        startActivity(GotoUpload);
    }

    public void show_Saved_Data(View view) {
        Intent GoToShowData=new Intent(ScanActivity.this, ShowItemscycleCountActivity.class);
        startActivity(GoToShowData);
    }

    public void Search_BarCodeOfCycleCount(View view) {
        if (edit_Barcode.getText().toString().isEmpty()){
                edit_Barcode.setError("قم بأدخال الباركود");
        }else {
            List<Po_Item_of_cycleCount> po_item_of_cycleCounts = new ArrayList<>();
    Log.e("barcode",""+edit_Barcode.getText().toString());
            po_item_of_cycleCounts = databaseHelperForCycleCount.Search_Barcode_in_localDataBase(edit_Barcode.getText().toString());

            if (po_item_of_cycleCounts.size() >0) {
                txt_describtion.setText(po_item_of_cycleCounts.get(0).getMAKTX1());
                if (po_item_of_cycleCounts.get(0).getSERNP1().toString().equalsIgnoreCase("anyType{}")
                    || po_item_of_cycleCounts.get(0).getSERNP1().toString().equalsIgnoreCase("") ){
                    txt_Serial.setText("لايوجد سيريال");
                }else {
                    txt_Serial.setText(po_item_of_cycleCounts.get(0).getSERNP1());
                }

                txt_unite.setText(po_item_of_cycleCounts.get(0).getMEINH1());
                edit_qty.requestFocus();
            }else {
                edit_Barcode.setError("هذا الباركود غير موجود");
                edit_Barcode.requestFocus();
            }
        }
    }

    public void save_Qty(View view) {
        if (edit_Barcode.getText().toString().isEmpty()){
            edit_Barcode.setError("من فضلك ادخل الباركود أولا");
            edit_Barcode.requestFocus();
        }else {

            po_item_of_cycleCountsforsave = new ArrayList<>();

            po_item_of_cycleCountsforsave = databaseHelperForCycleCount.Search_Barcode_in_localDataBase(edit_Barcode.getText().toString());

            if (po_item_of_cycleCountsforsave.size() > 0) {
                if (edit_qty.getText().toString().isEmpty()) {
                    edit_qty.setError("من فضلك ادخل الكميه");
                    edit_qty.requestFocus();
                } else {
                    if (po_item_of_cycleCountsforsave.get(0).getQTY1().equalsIgnoreCase("0.0")) {
                        databaseHelperForCycleCount.Update_QTY(edit_qty.getText().toString(), edit_Barcode.getText().toString());
                        edit_Barcode.setError(null);
                        edit_Barcode.setText("");
                        edit_Barcode.requestFocus();
                        edit_qty.setError(null);
                        edit_qty.setText("");
                        txt_describtion.setText("الوصف");
                        txt_Serial.setText("السيريال");
                        txt_unite.setText("وحدة القياس");
                    } else {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.textforupdateQTY))
                                .setPositiveButton("تزويد الكميه", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        Log.e("zzzupdate", "onClick:get0:  "+ Double.valueOf(
                                                po_item_of_cycleCountsforsave.get(0).getQTY1()));
                                        Log.e("zzzupdate", "onClick:getedit "+
                                                Double.valueOf(edit_qty.getText().toString()));
                                        Log.e("zzzupdate", "onClick:sum "+
                                                Double.valueOf(
                                                        po_item_of_cycleCountsforsave.get(0).getQTY1())+
                                                Double.valueOf(edit_qty.getText().toString()));

                                        databaseHelperForCycleCount.
                                                Update_QTY(String.valueOf(Double.valueOf(
                                                        po_item_of_cycleCountsforsave.get(0).getQTY1())+
                                                                Double.valueOf(edit_qty.getText().toString())),
                                                        edit_Barcode.getText().toString());


                                        edit_Barcode.setError(null);
                                        edit_Barcode.setText("");
                                        edit_Barcode.requestFocus();
                                        edit_qty.setError(null);
                                        edit_qty.setText("");
                                        txt_describtion.setText("الوصف");
                                        txt_Serial.setText("السيريال");
                                        txt_unite.setText("وحدة القياس");
                                    }
                                })
                                .setNegativeButton("تعديل الكميه", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelperForCycleCount.Update_QTY(edit_qty.getText().toString(), edit_Barcode.getText().toString());
                                        edit_Barcode.setError(null);
                                        edit_Barcode.setText("");
                                        edit_Barcode.requestFocus();
                                        edit_qty.setError(null);
                                        edit_qty.setText("");
                                        txt_describtion.setText("الوصف");
                                        txt_Serial.setText("السيريال");
                                        txt_unite.setText("وحدة القياس");
                                    }
                                }).show();
                    }
                }
            }else {
                edit_Barcode.setError("من فضلك أضغط على بحث أولا");
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ScanActivity.this , CycleCountActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
