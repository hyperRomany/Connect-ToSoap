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

import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Object> {
TextView txt_num_of_barcodes,txt_describtion,txt_unite,txt_Serial;
Button btn_get_info,btn_search_barcode,save_Qty;
EditText edit_from,edit_to ,edit_Barcode,edit_qty ;
    private int LOADER_ID = 2;
    List<Po_Item_of_cycleCount> po_item_of_cycleCountsforsave;

    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE
            ,Description ,Date,Items,User,Status;
DatabaseHelperForCycleCount databaseHelperForCycleCount;
ProgressBar progress_get_info;
     AsyncTaskLoader asyncTaskLoader= null;
     HashMap<String ,String> hashMap;
    ContentValues values;
    String Respones = "NO";
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
//100019480
//100019482
        databaseHelperForCycleCount=new DatabaseHelperForCycleCount(this);

        Intent getData=getIntent();
        Log.e("getdata",""+getData);
        if ( getData.getExtras()!= null) {
            edit_from.setText(getIntent().getExtras().getString("DocumentNuberfrom"));
            edit_to.setText(getIntent().getExtras().getString("DocumentNuberto"));
            edit_from.setEnabled(false);
            edit_to.setEnabled(false);
        }else {
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
        btn_search_barcode=findViewById(R.id.btn_search_barcode);
        save_Qty=findViewById(R.id.save_Qty);

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

//                    databaseHelperForCycleCount.DeleteItemsTable();

//                    btn_get_info.setEnabled(false);
                    Respones="No";
                    getLoaderManager().initLoader(LOADER_ID, null, ScanActivity.this);

//                    Log.e("soaMESSAGE4ffffff",""+MESSAGE);
//                    progress_get_info.setVisibility(View.GONE);
//                    btn_get_info.setVisibility(View.VISIBLE);
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

                    Log.e("envelsoaMESSAGE4onstart",""+MESSAGE);
                    asyncTaskLoader.cancelLoadInBackground();
                    getLoaderManager().destroyLoader(LOADER_ID);
                    Respones = Respones.replaceAll(";","");
                    Respones = Respones.replaceAll("\\،","");
                    Respones = Respones.replaceAll("\\'","");
//
                    Respones = Respones.replaceAll("\\,","");

//            Respones = Respones.replaceAll("\\+","");
//            Respones = Respones.replaceAll("\\(","");
//            Respones = Respones.replaceAll("\\)","");
//            Respones = Respones.replaceAll("\\*","");
//
//            Respones = Respones.replaceAll("\\�","");
//            Respones = Respones.replaceAll("\\-","");
//            Respones = Respones.replaceAll("\\/","");
//            Respones = Respones.replaceAll("\\?","");

                    Respones = Respones.replaceAll("anyType\\{\\}","");
                    Respones = Respones.replaceAll("item=anyType\\{PHYSINVENTORY=","('");

                    Log.e("zzzzzzfromoncreate",""+Respones);

//                    for (int i =0 ;!Respones.contains("PHYS_INV_NO_LONG");i++){
//                       // if (Respones.contains("PHYS_INV_NO_LONG")){
//                      //  }else {
//                     //       break;
//                       // }
//                    }
                    //    Log.e("zzzzzzzz",""+Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG") ,Respones.indexOf("MATERIAL=")));
                    //     Log.e("zzzzzzzz",""+Respones.indexOf(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG") ,Respones.indexOf("MATERIAL="))));
                    Log.e("zzzzzzzz",""+Respones);

                            //                            Respones = Respones.replace(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG") ,Respones.indexOf("MATERIAL=")) ,"");
//                            Respones = Respones.replace(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG") ,Respones.indexOf("MATERIAL=")) ,"");

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
                    Log.e("zzzzzzzz",""+Respones);

                    Log.e("zzzzzzzz",""+Respones.length());
                    Log.e("zzzzzzzz",""+Respones.charAt(Respones.length()-8)+Respones.charAt(Respones.length()-7)+Respones.charAt(Respones.length()-6)+Respones.charAt(Respones.length()-5)+Respones.charAt(Respones.length()-4)+Respones.charAt(Respones.length()-3)+Respones.charAt(Respones.length()-2)+Respones.charAt(Respones.length()-1));
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=304 SA 12/2019" ,"");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=305 SA 12/2019","");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=307 SA 12/2019" ,"");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=308 SA 12/2019" ,"");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=309 SA 12/2019" ,"");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=310 SA 12/2019" ,"");
//                    Respones = Respones.replaceAll("PHYS_INV_NO_LONG= COUNTED= COUNT_STATUS= COUNT_DATE=0000-00-00 USERNAME=58 PHYS_INV_REF=101 RT 12/2019" ,"");
                    String insertRows = "INSERT INTO 'Po_Item_of_cycleCount' ('PHYSINVENTORY','MATERIAL','QTY','MAKTX','BASE_UOM','ITEM','SERNP','EAN11','MEINH') VALUES";
                    insertRows +=Respones;
                    Log.e("zzzzzzz",""+insertRows);
                    insertRows = insertRows.substring(0,insertRows.length()-5) +";";
                    Log.e("zzzzzzz",""+insertRows);

                    databaseHelperForCycleCount.insertitems(insertRows);
                    //                       String insertRows = "INSERT INTO 'Po_Item_of_cycleCount'  VALUES";
                    Log.e("zzzzzzz","BeforLoop");

                    List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
                    po_item_list=databaseHelperForCycleCount.Select_Num_of_items_localDataBase();
                    txt_num_of_barcodes.setText(String.valueOf(po_item_list.size()));
                    progress_get_info.setVisibility(View.GONE);
                    btn_get_info.setVisibility(View.VISIBLE);
//            Toast.makeText(ReceivingActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
//            edit_purchaseorder.setError(" تم تسليم هذا الأمر"+edit_purchaseorder.getText().toString() +" من قبل .. من فضلك أدخل رقم جديد");
//            RETURN=" ";
//            edit_purchaseorder.setText("");
//            btn_loading_purchase_order.setEnabled(true);
                    }else {
                    Log.e("envelsoaelseforce","");
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
                Log.d("envelope", ""+year);
                request.addProperty("PHYSINVENTORY_FROM", edit_from.getText().toString());
                Log.d("envelope", ""+edit_from.getText().toString());
                if (edit_to.getText().toString().isEmpty()){
                    request.addProperty("PHYSINVENTORY_TO", "?");
                }else {
                    request.addProperty("PHYSINVENTORY_TO", edit_to.getText().toString());
                }

                Log.d("envelope", ""+edit_to.getText().toString());

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
                Log.d("envelopeoutbut", ""+envelope.bodyIn);
                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else if (envelope.bodyIn.toString().isEmpty()){

                }else {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;
                    Log.e("zzno_of_item", String.valueOf(soapObject_All_response.getPropertyCount()));

                    SoapObject soapObject_Items_Return = (SoapObject) soapObject_All_response.getProperty(0);
//                    Log.e("no_of_item", String.valueOf(soapObject_Items_Return.getPropertyCount()));
                    SoapObject soapObject_only_Return = (SoapObject) soapObject_All_response.getProperty(1);
//                    Log.e("no_of_item", String.valueOf(soapObject_only_Return.getPropertyCount()));

//                    databaseHelperForCycleCount.insertitems("anyType{}","b","v","v","d"
//                    ,"ds");
                    if (soapObject_Items_Return.getPropertyCount() >0 ) {

//                        asyncTaskLoader.stopLoading();
//                        asyncTaskLoader.cancelLoad();
//                        asyncTaskLoader.cancelLoadInBackground();
//                        getLoaderManager().destroyLoader(LOADER_ID);
                        Log.e("envelzzzno_of_itemccc", String.valueOf(soapObject_Items_Return.getPropertyCount()));
//                        Log.e("zzzzzzzz", String.valueOf(soapObject_Items_Return));

                        Respones = "";
                         Respones = ""+soapObject_Items_Return;
                        Log.e("envel", ""+Respones);
 //                        item=anyType{PHYSINVENTORY=0100002999; PHYS_INV_NO_LONG=anyType{}; COUNTED=anyType{}; COUNT_STATUS=anyType{}; COUNT_DATE=0000-00-00; USERNAME=WGOMAA; PHYS_INV_REF=anyType{}; MATERIAL=000000110000001199; MAKTX=جرين لاند نكتار برتقال200مل; BASE_UOM=EA; ITEM=001; SERNP=anyType{}; EAN11=6221011120783; MEINH=EA; ITEMS_COUNT=000000; };

                          //  for (int i = 0; i < soapObject_Items_Return.getPropertyCount(); i++) {
                         //       Log.e("zzzzHGGG", ""+ soapObject_Items_Return.getProperty(8).toString());
//    //                        for (int i = 0; i < 10 ; i++) {
//                                //    for (int j=0; j< soapObject_Items_Return.getPropertyCount() ; j++){
//                                SoapObject soapObject_Items_of_Items = (SoapObject) soapObject_Items_Return.getProperty(i);
////                                Log.e("no_of_itemccc" + i, String.valueOf(soapObject_Items_of_Items.getPropertyCount()));
//
//                            insertRows += "('" +
//                                    soapObject_Items_of_Items.getProperty(0).toString() + "','"  +
//                                    soapObject_Items_of_Items.getProperty(7).toString() + "','"  +
//                                    soapObject_Items_of_Items.getProperty(8).toString() +"','"  +
//                                    "0.0" + "','" +
//                                    soapObject_Items_of_Items.getProperty(10).toString() + "','"  +
//                                    soapObject_Items_of_Items.getProperty(9).toString() + "','" +
//                                    soapObject_Items_of_Items.getProperty(11).toString() + "','"  +
//                                    soapObject_Items_of_Items.getProperty(12).toString() + "','"  +
//                                    soapObject_Items_of_Items.getProperty(13).toString() + "'),";
//                                 values.put(Po_Item_of_cycleCount.PHYSINVENTORY,soapObject_Items_of_Items.getProperty(0).toString());
////                                values.put(Po_Item_of_cycleCount.MATERIAL,soapObject_Items_of_Items.getProperty(7).toString());
////                                values.put(Po_Item_of_cycleCount.MAKTX,soapObject_Items_of_Items.getProperty(8).toString());
////                                values.put(Po_Item_of_cycleCount.QTY,"0.0");
////                                values.put(Po_Item_of_cycleCount.ITEM,soapObject_Items_of_Items.getProperty(10).toString());
////                                values.put(Po_Item_of_cycleCount.BASE_UOM,soapObject_Items_of_Items.getProperty(9).toString());
////                                values.put(Po_Item_of_cycleCount.SERNP,soapObject_Items_of_Items.getProperty(11).toString());
////                                values.put(Po_Item_of_cycleCount.EAN11,soapObject_Items_of_Items.getProperty(12).toString());
////                                values.put(Po_Item_of_cycleCount.MEINH,soapObject_Items_of_Items.getProperty(13).toString());
////
////                              Log.e("no_of_itemcc"+i , "hasmap"+values.size());
////                                databaseHelperForCycleCount.insertitemsList(values);
                     //   }
//                        Log.e("no_of_itemcccTstr" , String.valueOf(soapObject_Items_Return.getPropertyCount()));
//                        Log.e("no_of_itemcccval" , String.valueOf(values.size()));
//                        Log.e("zzzzzzz","afterLoop");  //this is take less than one second
//
//                        for (int i =0 ; i < arrayList.size() ; i++) {
//
//                                                        insertRows += "('" +
//                                                                arrayList.get(i).getPHYSINVENTORY1() + "','"  +
//                                                                arrayList.get(i).getMATERIAL1() + "','"  +
//                                                                arrayList.get(i).getMAKTX1() +"','"  +
//                                                                arrayList.get(i).getQTY1() + "','" +
//                                                                arrayList.get(i).getITEM1() + "','"  +
//                                                                arrayList.get(i).getBASE_UOM1() + "','" +
//                                                                arrayList.get(i).getSERNP1() + "','"  +
//                                                                arrayList.get(i).getEAN111() + "','"  +
//                                                                arrayList.get(i).getMEINH1() + "'),";
//                            Log.e("zzzzzzz"+i,"Loopingof");
//                        }

//                        databaseHelperForCycleCount.insertitemsList(hashMap);


            //            Log.e("zzzzzzz","afterLoopofaddstring");
   //                     insertRows = insertRows.substring(0,insertRows.length()-1) +";";
//                        Log.e("zzzzzzz",""+insertRows);

//                        databaseHelperForCycleCount.insertitems(insertRows);
//                        Log.e("no_of_itemcccTT" , String.valueOf(soapObject_Items_Return.getPropertyCount()));
/*
for (Po_Item_of_cycleCount a:arrayList) {


                                                        insertRows += "('" +
                                                                a.getPHYSINVENTORY1() + "','"  +
                                                                a.getMATERIAL1() + "','"  +
                                                                a.getMAKTX1() +"','"  +
                                                                a.getQTY1() + "','" +
                                                                a.getITEM1() + "','"  +
                                                                a.getBASE_UOM1() + "','" +
                                                                a.getSERNP1() + "','"  +
                                                                a.getEAN111() + "','"  +
                                                                a.getMEINH1() + "'),";

                            Log.e("zzzzzzz"+a.getID1(),"Loopingof");
 */
                    }else {

                        SoapObject soapObject_Items_of_Return = (SoapObject) soapObject_only_Return.getProperty(0);
                        MESSAGE = (String) soapObject_Items_of_Return.getProperty("MESSAGE").toString();
                        Log.e("envelMESSAGE", String.valueOf(MESSAGE));

                     //   txt_describtion.setText(null);
                    }

                }
//                getLoaderManager().destroyLoader(LOADER_ID);
//                asyncTaskLoader.cancelLoadInBackground();
                //               // asyncTaskLoader.onCanceled(asyncTaskLoader);

                //this two lines for long loading
//                asyncTaskLoader.cancelLoad();
//                asyncTaskLoader.stopLoading();
//
//                if (asyncTaskLoader.toString().equals(null)){
//                    Log.e("envelopeofas","asyncTaskLoader is null");
//
//                }else {
//                    Log.e("envelopeofas","asyncTaskLoader is not null");
//
//                }
//                    Log.e("envelopeofas",""+asyncTaskLoader.toString());

                return null;
            }


        };
//        if (asyncTaskLoader.toString().equals(null)){
//            try {
//                asyncTaskLoader.wait();
//                Log.e("envelopeofas","asyncTaskLoader is null afterwait");
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return asyncTaskLoader;
//        }else {
//            Log.e("envelopeofas","asyncTaskLoader is not null afterwait");
            return asyncTaskLoader;
        //}
    }

//    private void onLoadFinished() {
//        getLoaderManager().destroyLoader(LOADER_ID);
//        Log.e("soaMESSAGE4ffffff",""+MESSAGE);
//        progress_get_info.setVisibility(View.GONE);
//        btn_get_info.setVisibility(View.VISIBLE);
//    }
    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        //Toast.makeText(ReceivingActivity.this,"finished ",Toast.LENGTH_LONG).show();
        Log.e("enveloaMESSAGE4onfinish",""+MESSAGE);
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.e("soaMESSAGE4",""+MESSAGE);
        //Toast.makeText(ReceivingActivity.this,"finished "+MESSAGE,Toast.LENGTH_LONG).show();

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            edit_from.setError("من فضلك انظر لل Log");
            edit_from.requestFocus();
            txt_describtion.setText(null);
            btn_get_info.setEnabled(true);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
//            btn_get_info.setEnabled(true);
        }else if(!MESSAGE.contains("Empty") ){

            edit_from.setError(MESSAGE);
            edit_from.requestFocus();
            txt_describtion.setText(null);
            btn_get_info.setEnabled(true);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
//            Intent Go_To_ScanRecieving = new Intent(CycleCountActivity.this, ScanRecievingActivity.class);
//            Log.e("This Is First Time",""+RETURN);
//            Go_To_ScanRecieving.putExtra("This Is First Time",This_Is_First_Time);
//            Log.e("This Is First Time","true");
//            startActivity(Go_To_ScanRecieving);
//            btn_loading_purchase_order.setEnabled(true);
        }else {

            Respones = Respones.replaceAll(";","");
            Respones = Respones.replaceAll("\\،","");
            Respones = Respones.replaceAll("\\'","");
//
            Respones = Respones.replaceAll("\\,","");

//            Respones = Respones.replaceAll("\\+","");
//            Respones = Respones.replaceAll("\\(","");
//            Respones = Respones.replaceAll("\\)","");
//            Respones = Respones.replaceAll("\\*","");
//
//            Respones = Respones.replaceAll("\\�","");
//            Respones = Respones.replaceAll("\\-","");
//            Respones = Respones.replaceAll("\\/","");
//            Respones = Respones.replaceAll("\\?","");

            Respones = Respones.replaceAll("anyType\\{\\}","");
            Respones = Respones.replaceAll("item=anyType\\{PHYSINVENTORY=","('");

            Log.e("zzzzzzzz",""+Respones);

//            for (int i =0 ; ;i++){
//                if (Respones.contains("PHYS_INV_NO_LONG") ==true) {
//                    String sub = Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL"));
//                    Respones = Respones.replaceAll(sub, "");


//            String sub1 = Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL"));
//            Respones = Respones.replaceAll(sub1, "");
//            String sub3 = Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL"));
//            Respones = Respones.replaceAll(sub3, "");

//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");
//            Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("PHYS_INV_NO_LONG"), Respones.indexOf("MATERIAL")), "");

//                }else
//                    break;

            //}
            Log.e("zzzzzzzz",""+Respones);
            Respones = Respones.replaceAll("MATERIAL=","','");
            Respones = Respones.replaceAll("MAKTX=","','0.0','");
            Respones = Respones.replaceAll("BASE_UOM=","','");
            Respones = Respones.replaceAll("ITEM=","','");
            Respones = Respones.replaceAll("SERNP=","','");
            Respones = Respones.replaceAll("EAN11=","','");
            Respones = Respones.replaceAll("MEINH=","','");
            Log.e("zzzzzzzz",""+Respones);
            Log.e("zzzzzzzz",""+Respones.indexOf("ITEMS_COUNT") );
            //Log.e("zzzzzzzz",""+Respones.indexOf("} ("));

          //  Respones = Respones.replaceAll(Respones.substring(Respones.indexOf("ITEMS_COUNT") ,Respones.indexOf("ITEMS_COUNT")+19) ,"");
            Respones = Respones.replaceAll("\\}","'),");
            Respones = Respones.replaceAll("anyType\\{","");
            Log.e("zzzzzzzz",""+Respones);

            Log.e("zzzzzzzz",""+Respones.length());
            Log.e("zzzzzzzz",""+Respones.charAt(Respones.length()-8)+Respones.charAt(Respones.length()-7)+Respones.charAt(Respones.length()-6)+Respones.charAt(Respones.length()-5)+Respones.charAt(Respones.length()-4)+Respones.charAt(Respones.length()-3)+Respones.charAt(Respones.length()-2)+Respones.charAt(Respones.length()-1));


            String insertRows = "INSERT INTO 'Po_Item_of_cycleCount' ('PHYSINVENTORY','MATERIAL','QTY','MAKTX','BASE_UOM','ITEM','SERNP','EAN11','MEINH') VALUES";
            insertRows +=Respones;
            Log.e("zzzzzzz",""+insertRows);
            insertRows = insertRows.substring(0,insertRows.length()-5) +";";
            Log.e("zzzzzzz",""+insertRows);

            databaseHelperForCycleCount.insertitems(insertRows);
            //                       String insertRows = "INSERT INTO 'Po_Item_of_cycleCount'  VALUES";
            Log.e("zzzzzzz","BeforLoop");


            List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
            po_item_list=databaseHelperForCycleCount.Select_Num_of_items_localDataBase();
            txt_num_of_barcodes.setText(String.valueOf(po_item_list.size()));
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
//            Toast.makeText(ReceivingActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
//            edit_purchaseorder.setError(" تم تسليم هذا الأمر"+edit_purchaseorder.getText().toString() +" من قبل .. من فضلك أدخل رقم جديد");
//            RETURN=" ";
//            edit_purchaseorder.setText("");
//            btn_loading_purchase_order.setEnabled(true);
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

                                        databaseHelperForCycleCount.Update_QTY(String.valueOf(Double.valueOf(po_item_of_cycleCountsforsave.get(0).getQTY1())+
                                                new DecimalFormat("###.#####").format(Double.valueOf(Double.valueOf(edit_qty.getText().toString())))), edit_Barcode.getText().toString());
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
