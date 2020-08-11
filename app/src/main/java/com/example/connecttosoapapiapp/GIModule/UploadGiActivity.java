package com.example.connecttosoapapiapp.GIModule;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.connecttosoapapiapp.GIModule.Helper.GIDataBaseHelper;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
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

public class UploadGiActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<List<String>>{
    String EnvelopeBodyInConstant="",EnvelopeBodyInCurrent="" , MATERIALDOCUMENT ="Empty";;
List <GIModule> Po_Item_For_Gi_Upload;
    private int LOADER_ID = 4;
    GIDataBaseHelper giDataBaseHelper;

Context context;
//TODO error when build
//    public UploadGiActivity(Context context) {
//        this.context = context;
//    }

    public void onclickUploadGI(){
Log.e("ccccccccccccc","from upload");
        // getLoaderManager().initLoader(LOADER_ID, null, this);

    }
    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader =  new AsyncTaskLoader(context) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Upload_GI, Constant.METHOD_For_Upload_GI);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                Po_Item_For_Gi_Upload=new ArrayList<>();
                Po_Item_For_Gi_Upload = giDataBaseHelper.selectGIModule_for_Qty();
                MATERIALDOCUMENT ="Empty";
                if (Po_Item_For_Gi_Upload.size() == 0){
                    Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
                }else {
                    Log.e("envelope", "Po_Item_For_ftp_Upload"+Po_Item_For_Gi_Upload.size());

                    Vector v1 = new Vector();

                    for (int i = 0; i < Po_Item_For_Gi_Upload.size(); i++) {

                        SoapObject rate = new SoapObject(Constant.NAMESPACE_For_Upload_GI, "item");

                        rate.addProperty("MATERIAL", Po_Item_For_Gi_Upload.get(i).getMAT_CODE1());
                        rate.addProperty("PLANT", Po_Item_For_Gi_Upload.get(i).getREC_SITE1());

                        ///////////
                        rate.addProperty("STGE_LOC", Po_Item_For_Gi_Upload.get(i).getREC_SITE_LOG1());

                        rate.addProperty("ENTRY_QNT", Po_Item_For_Gi_Upload.get(i).getQTY1());
                        rate.addProperty("ENTRY_UOM", Po_Item_For_Gi_Upload.get(i).getMEINH1());

                        ///////////////////////////////
                        rate.addProperty("COSTCENTER", "");
                        rate.addProperty("GL_ACCOUNT", "");
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
                if (envelope.bodyIn != null){
                    Log.e("envelope", "Po_Item_For_Gi_Upload.size() ==0");

                }else {
                    Log.e("envelope", ""+envelope.bodyIn);
                    Log.e("envelope.bodyOut", ""+envelope.bodyOut);
                    Log.e("envelope", ""+((SoapObject)envelope.bodyOut).getPropertyCount());
                    SoapObject soapResponse = (SoapObject) envelope.bodyIn;
                    Log.d("envelopeProMESSAGE",""+soapResponse.getPropertyCount());

                    EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                    EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                    if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                        // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                    }else {
//                        SoapObject soapreturn = (SoapObject) soapResponse.getProperty(2);
//                        SoapObject soapreturnitem = (SoapObject) soapreturn.getProperty(0);
//                        MATERIALDOCUMENT = soapreturnitem.getProperty(3).toString();
//                        Log.d("envelopeProMESSAGE", "" + MATERIALDOCUMENT);
                    }
                }


                return null;
            }
        };
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
         Toast.makeText(context,"finished ",Toast.LENGTH_LONG);
//        context.getApplicationContext().getLoaderManager().destroyLoader(LOADER_ID);
        Log.e("onLoadFinis",""+EnvelopeBodyInCurrent);
        Log.e("onLoadFinis",""+EnvelopeBodyInConstant);


        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            Toast.makeText(context, "خطأ فى البيانات .. أنظر لل log", Toast.LENGTH_SHORT).show();
        }else if (!MATERIALDOCUMENT.equalsIgnoreCase("Empty")){
//            txt_response.setText(MATERIALDOCUMENT);
//
//            new AlertDialog.Builder(this)
//                    .setTitle(getString(R.string.delete_all_items))
//                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            databaseHelperForCycleCount.DeleteItemsTable();
//                        }
//                    })
//                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            dialog.cancel();
//                        }
//                    }).show();
        }


          /*  if (MATERIALDOCUMENT.contains("anyType{}") && MESSAGE.contains("Qty and / or ")){
                //Toast.makeText(this, "There is Error", Toast.LENGTH_SHORT).show();
                txt_response.setText("هذا الأمر ربما تم تحميله");
            }else */
          if (MATERIALDOCUMENT.contains("anyType{}")){
                Toast.makeText(context, "هناك مشكله", Toast.LENGTH_SHORT).show();
//                txt_response.setText("لم يتم رفع"+Po_HeaderList.get(0).getPO_NUMBER1()+"\n"+MESSAGE);
            }else if (!MATERIALDOCUMENT.contains("anyType{}")){
//                databaseHelperForCycleCount.update_NoMore_To_MaterialNU(MATERIALDOCUMENT);
//                txt_response.setText( "تم الرفع برقم\n"+MATERIALDOCUMENT );

//                if (Po_Item_For_Log_only_Has_value.size()>0) {
//                   // for (int i = 0; i < Po_Item_For_Log_only_Has_value.size();i++) {
////                    WriteInLogOf_sapTableOfSqlServer();
////                        WriteInLogs_sap_ITEMStableOfSqlServer();
//                  //  }
//                }else {
//                    txt_response.setText("All Qty Is 0.0");
//
//                }
            }else if (Po_Item_For_Gi_Upload.size() ==0){
//                txt_response.setText("You not have any change in Quantity");
            }

//         Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//            Intent Go_To_ScanRecieving = new Intent(UploadForTransferActivity.this, ScanRecievingActivity.class);
//            Go_To_ScanRecieving.putExtra("This Is First Time",true);
//            Log.e("This Is First Time","true");
//            startActivity(Go_To_ScanRecieving);

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
    }

}
