package com.example.connecttosoapapiapp.ReceivingModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.MainActivity;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Header;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReceivingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{

    private String s0,s1,s2;
    private int LOADER_ID = 1;
    private ProgressBar prog_loading_purchesorder;
    private EditText edit_purchaseorder;
    private Button btn_loading_last_purchase_order,btn_loading_new_purchase_order,btn_print_purchase_order;
    private DatabaseHelper databaseHelper;
    private String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE , envelopebodyInIsNull="";
    private List<Po_Header> Po_HeaderList ;
    private List<Po_Item> Po_itemList ;
    private String Po_item_NoMore=" ";
    private List<Users> userList;
    private Boolean This_Is_First_Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving);

        btn_loading_last_purchase_order= findViewById(R.id.btn_loading_last_purchase_order);
        edit_purchaseorder =findViewById(R.id.edit_purchaseorder);
        btn_loading_new_purchase_order= findViewById(R.id.btn_loading_new_purchase_order);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper =new DatabaseHelper(this);
        //databaseHelperForPoItem = new DatabaseHelperForPoItem(this);

        userList = databaseHelper.getUserData();


        Po_HeaderList = new ArrayList<>();
        Po_itemList = new ArrayList<>();
        Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();



        if (Po_HeaderList.size()<=0){
            edit_purchaseorder.setHint("قاعده البيانات فارغه");
            edit_purchaseorder.setHintTextColor(getResources().getColor(R.color.first_blue));
        }
        else if(!Po_HeaderList.get(0).getPO_NUMBER1().equalsIgnoreCase("anyType{}")) {
            edit_purchaseorder.setText(Po_HeaderList.get(0).getPO_NUMBER1());
            edit_purchaseorder.setSelection(edit_purchaseorder.getText().length());

        }
        else {
            edit_purchaseorder.setHint("قاعده البيانات فارغه");
        }

        prog_loading_purchesorder=findViewById(R.id.prog_loading_purchesorder);
        btn_print_purchase_order=findViewById(R.id.btn_print_purchase_order);
        Log.e("print", String.valueOf(databaseHelper.getUserData().get(0).getPrint()));
        if (String.valueOf(databaseHelper.getUserData().get(0).getPrint()).equals("1"))
        {
            btn_print_purchase_order.setVisibility(View.VISIBLE);
        }

        btn_print_purchase_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoaderManager().restartLoader(2, null, new ReceivingActivity.MyLoaderCallbacks03SA());
            }
        });

        btn_loading_last_purchase_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading_PURCHASE_ORDER();
            }
        });

        btn_loading_new_purchase_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loading_PURCHASE_ORDER_New();
            }
        });


        if (!Po_item_NoMore.contains(" ")){
            Log.e("Po_item_NoMore", "!contains" + Po_item_NoMore);
        }
        else {
            Log.e("Po_item_NoMore", "contains" + Po_item_NoMore);
        }

    }


    public void Loading_PURCHASE_ORDER() {
        /**
         *  there are error when return is empty check return response it has value
         *
         */
        Po_item_NoMore="N";
        Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();
        Po_itemList=databaseHelper.Get_Po_Item();
        Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();


        if (edit_purchaseorder.getText().toString().isEmpty()){
            edit_purchaseorder.setError("من فضلك ادخل أمر الشراء");
        }
        else if (Po_HeaderList.size() == 0){
            Toast.makeText(this, "لايوجد أمر سابق برجاء تحميل أمر شراء جديد", Toast.LENGTH_SHORT).show();
        }
        else if (Po_HeaderList.size()!= 0 && Po_itemList.size() !=0){  //!Po_item_NoMore.contains(" ")
            if (Po_HeaderList.get(0).getPO_NUMBER1().equalsIgnoreCase(edit_purchaseorder.getText().toString())
                    && Po_item_NoMore.length()!=0){

                edit_purchaseorder.setError("هذا الأمر تم تسليمه من قبل برقم"+Po_item_NoMore);

            }
            else if (Po_HeaderList.get(0).getPO_NUMBER1().equalsIgnoreCase(edit_purchaseorder.getText().toString())
                    && Po_item_NoMore.length()==0){
                Log.d("soapObjectif", "PO_NUMBER1().equalsfunction");
                This_Is_First_Time=false;
                getLoaderManager().initLoader(LOADER_ID, null, ReceivingActivity.this);
            }
            else if (!Po_HeaderList.get(0).getPO_NUMBER1().equalsIgnoreCase(edit_purchaseorder.getText().toString())){

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.textforpurchaseorderReciev))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                This_Is_First_Time=true;
                                databaseHelper.DeleteDataOfThreeTables();
                                getLoaderManager().initLoader(LOADER_ID, null, ReceivingActivity.this);
                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        }).show();
                 }
                 //this to be more secure
        }
    }


    public void Loading_PURCHASE_ORDER_New() {
        /**
         *  there are error when return is empty check return response it has value
         *
         */
        Po_item_NoMore="N";
        Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();
        Po_itemList=databaseHelper.Get_Po_Item();
        Log.e("Po_item_NoMoreN",""+Po_item_NoMore.length()+"N");
        Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();
        Log.e("Po_item_NoMoreL",""+Po_HeaderList.size());

        if (edit_purchaseorder.getText().toString().isEmpty()){
            edit_purchaseorder.setError("من فضلك ادخل أمر الشراء");
        }else{
            This_Is_First_Time=true;
            databaseHelper.DeleteDataOfThreeTables();
            getLoaderManager().initLoader(LOADER_ID, null, ReceivingActivity.this);

        }
    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader =  new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
                btn_loading_last_purchase_order.setEnabled(false);
                btn_loading_new_purchase_order.setEnabled(false);
                prog_loading_purchesorder.setVisibility(View.VISIBLE);
            }

            @Override
            public Object loadInBackground() {
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Get_Detials , Constant.METHOD_For_Get_Detials);

                request.addProperty("PURCHASEORDER", edit_purchaseorder.getText().toString());

                RETURN="Empty";
                MESSAGE="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                //envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Get_Detials ,300000);
                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Get_Detials, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                }
                else if (envelope.bodyIn == null) {
                    envelopebodyInIsNull="null";
                }
                else {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;



                    ArrayList<String> arrayList_Po_header = new ArrayList<>();
                    ArrayList<String> arrayList_Po_items = new ArrayList<>();

                    Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();
                    if (This_Is_First_Time == false){


                        for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                            SoapObject soapObject_All_Return = (SoapObject) soapObject_All_response.getProperty(i);
                            for (int j = 0; j < soapObject_All_Return.getPropertyCount(); j++) {

                                if (i == 2) {
                                    SoapObject soapObject_for_rows_of_po_item = (SoapObject) soapObject_All_Return.getProperty(j);
                                    RETURN = (String) soapObject_All_response.getProperty("RETURN").toString();
                                    MESSAGE = (String) soapObject_for_rows_of_po_item.getProperty("MESSAGE").toString();


                                }
                            }
                        }
                    }else{
                        for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {


                        SoapObject soapObject_All_Return = (SoapObject) soapObject_All_response.getProperty(i);
                        for (int j = 0; j < soapObject_All_Return.getPropertyCount(); j++) {


                            if (i == 0) {
                                arrayList_Po_header.add(String.valueOf(soapObject_All_Return.getProperty(j)));

                                if (j == 8) {
                                    long id_of_po_header = databaseHelper.insertPo_Header(arrayList_Po_header.get(0), arrayList_Po_header.get(1),
                                            arrayList_Po_header.get(2), arrayList_Po_header.get(3), arrayList_Po_header.get(4), userList.get(0).getUser_Name1(),
                                            arrayList_Po_header.get(5), arrayList_Po_header.get(6), arrayList_Po_header.get(7));

                                }
                            }
                            if (i == 1) {
                                SoapObject soapObject_for_rows_of_po_item = (SoapObject) soapObject_All_Return.getProperty(j);
                                // this for splite row of po_item to vales
                                for (int k = 0; k < soapObject_for_rows_of_po_item.getPropertyCount(); k++) {
                                    // SoapObject soapObject_For_each_item= (SoapObject) soapObject_All_Return.getProperty(k);

                                    arrayList_Po_items.add(String.valueOf(soapObject_for_rows_of_po_item.getProperty(k)));

                                    if (k == 27) {
                                        long id_of_po_item = databaseHelper.insertPo_Item(arrayList_Po_items.get(0), arrayList_Po_items.get(1),
                                                arrayList_Po_items.get(2), arrayList_Po_items.get(3), arrayList_Po_items.get(4),"0.0", "",
                                                arrayList_Po_header.get(3) , arrayList_Po_items.get(5),
                                                arrayList_Po_items.get(6), arrayList_Po_items.get(7), arrayList_Po_items.get(8), arrayList_Po_items.get(9),
                                                arrayList_Po_items.get(10), arrayList_Po_items.get(11), arrayList_Po_items.get(12), arrayList_Po_items.get(13),
                                                arrayList_Po_items.get(14), arrayList_Po_items.get(15), arrayList_Po_items.get(16), arrayList_Po_items.get(17),
                                                arrayList_Po_items.get(18), arrayList_Po_items.get(19), arrayList_Po_items.get(20), arrayList_Po_items.get(21),
                                                arrayList_Po_items.get(22), arrayList_Po_items.get(23), arrayList_Po_items.get(24), arrayList_Po_items.get(25)
                                                  , arrayList_Po_items.get(26), arrayList_Po_items.get(27)

                                        );
                                        arrayList_Po_items.clear();
                                    }
                                }
                            }if (i==2){
                                SoapObject soapObject_for_rows_of_po_item = (SoapObject) soapObject_All_Return.getProperty(j);
                                RETURN = (String) soapObject_All_response.getProperty("RETURN").toString();
                                MESSAGE = (String) soapObject_for_rows_of_po_item.getProperty("MESSAGE").toString();

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
        getLoaderManager().destroyLoader(LOADER_ID);


        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            edit_purchaseorder.setError("من فضلك أدخل أمر الشراء");
            btn_loading_last_purchase_order.setEnabled(true);
            btn_loading_new_purchase_order.setEnabled(true);
            prog_loading_purchesorder.setVisibility(View.GONE);
        }else if (envelopebodyInIsNull.equalsIgnoreCase("null")) {
            edit_purchaseorder.setError("لم يتم تحميل البيانات المطلوبه null");
            btn_loading_last_purchase_order.setEnabled(true);
            btn_loading_new_purchase_order.setEnabled(true);
            prog_loading_purchesorder.setVisibility(View.GONE);
        }else if(MESSAGE.contains("Empty") ){
            Intent Go_To_ScanRecieving = new Intent(ReceivingActivity.this, ScanRecievingActivity.class);
            Log.e("This Is First Time",""+RETURN);
            Go_To_ScanRecieving.putExtra("This Is First Time",This_Is_First_Time);
            Log.e("This Is First Time","true");
            startActivity(Go_To_ScanRecieving);
            btn_loading_last_purchase_order.setEnabled(true);
            btn_loading_new_purchase_order.setEnabled(true);
            prog_loading_purchesorder.setVisibility(View.GONE);
        }else {
            Toast.makeText(ReceivingActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
            edit_purchaseorder.setError(" تم تسليم هذا الأمر"+edit_purchaseorder.getText().toString() +" من قبل .. من فضلك أدخل رقم جديد");
            RETURN=" ";
            edit_purchaseorder.setText("");
            btn_loading_last_purchase_order.setEnabled(true);
            btn_loading_new_purchase_order.setEnabled(true);
            prog_loading_purchesorder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ReceivingActivity.this, MainActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private class MyLoaderCallbacks03SA implements LoaderManager.LoaderCallbacks<List<String>> {
        @Override
        public Loader<List<String>> onCreateLoader(int id, Bundle args) {
            @SuppressLint("StaticFieldLeak")
            AsyncTaskLoader asyncTaskLoader = null;

            asyncTaskLoader = new AsyncTaskLoader(ReceivingActivity.this) {
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
                    request.addProperty("EBELN", edit_purchaseorder.getText().toString());
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
            Toast.makeText(ReceivingActivity.this, "finished ", Toast.LENGTH_LONG).show();
            getLoaderManager().destroyLoader(LOADER_ID);


            if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {

                edit_purchaseorder.setError("" + EnvelopeBodyInCurrent);
            } else if (MESSAGE.contains("Empty")) {
                List<GIModule> GIModulelist_bg = new ArrayList<>();
                Toast.makeText(ReceivingActivity.this, RETURN, Toast.LENGTH_LONG).show();

                Intent objIntent = new Intent(Intent.ACTION_VIEW);
                objIntent.setDataAndType(Uri.parse("content:///storage/emulated/0/HyperOne.pdf"), "application/pdf");
                objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(objIntent);//Starting the pdf viewer


            } else {
                edit_purchaseorder.setError(MESSAGE);

            }

        }
        @Override
        public void onLoaderReset(Loader<List<String>> loader) {
        }

    }

}
