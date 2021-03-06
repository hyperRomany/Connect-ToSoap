package com.example.connecttosoapapiapp.TransfereModule;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransferSearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>{
    EditText editbarcodeforsoap,edit_asked_from_site_search;
    String FromSite,ToSite,Department;
    private int LOADER_ID = 1;
    DatabaseHelperForTransfer databaseHelperForTransfer;
    Boolean makeOrder = false;

    String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE, type ;
    List<String> ReturnSearchList ;
    List<STo_Search> CheckItemssize;
    TextView txt_descripation_search,txt_code_item_search,txt_state_item_search,
            txt_from_site_search,txt_available_to_site_search,txt_to_site_search;
    Spinner spiner_storage_location_from,spiner_storage_location_to;
    List<STO_Header> STo_headerlist;
    LinearLayout fromSite_Linear;
    //List<STo_Search> STo_searchlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_search);
        editbarcodeforsoap=findViewById(R.id.edit_barcode_for_saop);

        editbarcodeforsoap.requestFocus();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fromSite_Linear = findViewById(R.id.from_site_linear);
        spiner_storage_location_to=findViewById(R.id.spiner_storage_location_to);
        spiner_storage_location_from=findViewById(R.id.spiner_storage_location_from);

        txt_descripation_search=findViewById(R.id.txt_descripation_search);
        txt_code_item_search=findViewById(R.id.txt_code_item_search);
        txt_state_item_search=findViewById(R.id.txt_state_item_search);
        txt_from_site_search=findViewById(R.id.txt_from_site_search);
        txt_available_to_site_search=findViewById(R.id.txt_available_to_site_search);
        txt_to_site_search=findViewById(R.id.txt_to_site_search);
        edit_asked_from_site_search=findViewById(R.id.edit_asked_from_site_search);

        databaseHelperForTransfer=new DatabaseHelperForTransfer(this);
        //STo_searchlist = databaseHelperForTransfer.selectSto_Search();

        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();

        makeOrder = getIntent().getExtras().getBoolean("MakeOrder");
        type = getIntent().getExtras().getString("type");
        Intent GetData = getIntent();
        if (GetData.getExtras() != null) {
            Department = GetData.getExtras().getString("Department");
            FromSite = GetData.getExtras().getString("FromSite");
            ToSite = GetData.getExtras().getString("ToSite");



            txt_from_site_search.setText(FromSite);
            txt_to_site_search.setText(ToSite);

        }else {

            Toast.makeText(this, "intent is null", Toast.LENGTH_SHORT).show();
            Department = STo_headerlist.get(0).getP_Grp1();
            FromSite = STo_headerlist.get(0).getIss_Site1();
            ToSite = STo_headerlist.get(0).getRec_Site1();
            txt_from_site_search.setText(FromSite);
            txt_to_site_search.setText(ToSite);
        }

        if (makeOrder) {
            fromSite_Linear.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Create Order");
            ToSite = GetData.getExtras().getString("FromSite");
        }


        if (STo_headerlist.size()>0) {
            //TODO ccccccccccccccccccccccccccccc
            if (!STo_headerlist.get(0).getRec_Site_log1().contains("anyType{}")) { //anyType{}
                List<String> Iss_Site_Log_list = new ArrayList<>();

                Iss_Site_Log_list.add(STo_headerlist.get(0).getIss_Strg_Log1());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferSearchActivity.this,
                        android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiner_storage_location_from.setAdapter(adapter);
                // spiner_storage_location_from.setSelection(Integer.valueOf(STo_headerlist.get(0).getIss_Strg_Log1()));

                List<String> Rec_Site_Log_list = new ArrayList<>();
                Rec_Site_Log_list.add(STo_headerlist.get(0).getRec_Site_log1());
                Log.e("getISS_STG", "" + STo_headerlist.get(0).getRec_Site_log1());
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TransferSearchActivity.this,
                        android.R.layout.simple_spinner_item, Rec_Site_Log_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiner_storage_location_to.setAdapter(adapter2);
                // spiner_storage_location_to.setSelection(Integer.valueOf(STo_headerlist.get(0).getRec_Site_log1()));

                spiner_storage_location_from.setEnabled(false);
                spiner_storage_location_to.setEnabled(false);
            }
        }

        spiner_storage_location_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TransferSearchActivity.this, ""+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                List<STo_Search> STo_searchlist_btn = new ArrayList<>();
                Log.e("spiner_storage_l",""+spiner_storage_location_from.getSelectedItem().toString());
                Log.e("spiner_storage_l",""+spiner_storage_location_to.getSelectedItem().toString());
                Log.e("spiner_storage_l",""+editbarcodeforsoap.getText().toString());
                if (!editbarcodeforsoap.getText().toString().isEmpty()) {
                    if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_to.getSelectedItem().toString()
                                , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
                    }else
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_to.getSelectedItem().toString()
                                , editbarcodeforsoap.getText().toString());
                    Log.e("spiner_storage_lsi", "" + STo_searchlist_btn.size());
                    if (STo_searchlist_btn.size() != 0) {
                        txt_descripation_search.setText(STo_searchlist_btn.get(0).getUOM_DESC1());
                        txt_code_item_search.setText(STo_searchlist_btn.get(0).getMAT_CODE1().subSequence(6, 18));
                        txt_state_item_search.setText(STo_searchlist_btn.get(0).getSTATUS1());
                        Double total = Double.valueOf(STo_searchlist_btn.get(0).getAVAILABLE_STOCK1()) - Double.valueOf(STo_searchlist_btn.get(0).getQTY1());
                        txt_available_to_site_search.setText(String.valueOf(
                                new DecimalFormat("###.##").format(Double.valueOf(total))));
                        txt_from_site_search.setText(STo_searchlist_btn.get(0).getISS_SITE1());
                        txt_to_site_search.setText(STo_searchlist_btn.get(0).getREC_SITE1());
                    } else {
                        Log.e("spiner_storage_lsi", "STo_searchlist_btn.size()=0");

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spiner_storage_location_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TransferSearchActivity.this, ""+parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                List<STo_Search> STo_searchlist_btn = new ArrayList<>();
                Log.e("spiner_storage_l",""+spiner_storage_location_from.getSelectedItem().toString());
                Log.e("spiner_storage_l",""+spiner_storage_location_to.getSelectedItem().toString());
                Log.e("spiner_storage_l",""+editbarcodeforsoap.getText().toString());
                if (!editbarcodeforsoap.getText().toString().isEmpty()) {
                    if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_to.getSelectedItem().toString()
                                , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
                    }else
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_to.getSelectedItem().toString()
                                , editbarcodeforsoap.getText().toString());
                    Log.e("spiner_storage_lsi", "" + STo_searchlist_btn.size());

                    if (STo_searchlist_btn.size() != 0) {
                        txt_descripation_search.setText(STo_searchlist_btn.get(0).getUOM_DESC1());
                        txt_code_item_search.setText(STo_searchlist_btn.get(0).getMAT_CODE1().subSequence(6, 18));
                        txt_state_item_search.setText(STo_searchlist_btn.get(0).getSTATUS1());
                        Double total = Double.valueOf(STo_searchlist_btn.get(0).getAVAILABLE_STOCK1()) -
                                Double.valueOf(STo_searchlist_btn.get(0).getQTY1());
                        txt_available_to_site_search.setText(String.valueOf(
                                new DecimalFormat("###.##").format(Double.valueOf(total))));
                        txt_from_site_search.setText(STo_searchlist_btn.get(0).getISS_SITE1());
                        txt_to_site_search.setText(STo_searchlist_btn.get(0).getREC_SITE1());
                    } else {
                        Log.e("spiner_storage_lsi", "STo_searchlist_btn.size()=0");

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void SearchBarCodeFromSoap(View view) {
        // edit_asked_from_site_search.setEnabled(false);
        edit_asked_from_site_search.setHint("?????????????? ????????????");

        if (editbarcodeforsoap.getText().toString().isEmpty()){
            editbarcodeforsoap.setError("???? ???????? ???????? ????????????????");
        }else {
            List<STo_Search> STo_searchlist_btn = new ArrayList<>();
            //search if it is in local database
            if (editbarcodeforsoap.getText().toString().startsWith("23")){
                STo_searchlist_btn = databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
                Log.e("zzzbarcodestart23 ",""+editbarcodeforsoap.getText().toString().substring(0,7)+"00000");
            }else {
                STo_searchlist_btn = databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString());
                Log.e("zzzbarcodeNotstart23 ",""+editbarcodeforsoap.getText().toString());
            }
            Log.e("STo_searchlist is","size"+STo_searchlist_btn.size());
            if (STo_searchlist_btn.size() > 0) {
                for (int j = 0; j < STo_searchlist_btn.size(); j++){
                    if (STo_searchlist_btn.get(j).getSTATUS1().equalsIgnoreCase("1")) {
                        editbarcodeforsoap.setError("?????? ???????????????? ?????? ????????");
                    } else if ((STo_searchlist_btn.get(j).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0"))
                            && !(FromSite.equalsIgnoreCase("01MW")
                            || ToSite.equalsIgnoreCase("01MW"))) { //TODO check qty
                        Log.d("getISSbbbbb_STG_AVAILABLE_STO_j", j + "zz " + STo_searchlist_btn.get(j).getAVAILABLE_STOCK1());
                        if (j == STo_searchlist_btn.size()-1) {
                            Log.d("getISSbbbbb_STG_AVAILABLE_STO", "zz " + STo_searchlist_btn.get(j).getAVAILABLE_STOCK1());
                            Log.d("getISSbbbbb_STG_FromSite", "z" + FromSite);
                            editbarcodeforsoap.setError("???? ???????? ???????? ?????????? ??????????????");
                            edit_asked_from_site_search.setEnabled(false);
                        }
                    } else {
                        STo_headerlist = new ArrayList<>();

                        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
                        Toast.makeText(this, "intent is null", Toast.LENGTH_SHORT).show();
                        if (!STo_headerlist.get(0).getRec_Site_log1().contains("anyType{}")) { //anyType{}
                            List<String> Iss_Site_Log_list = new ArrayList<>();

                            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
//                            STo_searchlist_btn = databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString());
                                Iss_Site_Log_list.add(databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000")).get(0).getISS_STG_LOG1());
                            }
                            else {
                                Iss_Site_Log_list.add(databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString()).get(0).getISS_STG_LOG1());

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_from.setAdapter(adapter);
                            // spiner_storage_location_from.setSelection(Integer.valueOf(STo_headerlist.get(0).getIss_Strg_Log1()));

                            List<String> Rec_Site_Log_list = new ArrayList<>();
                            Rec_Site_Log_list.add(STo_headerlist.get(0).getRec_Site_log1());
                            Log.e("getISS_STG", "" + STo_headerlist.get(0).getRec_Site_log1());
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TransferSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Rec_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_to.setAdapter(adapter2);
                            // spiner_storage_location_to.setSelection(Integer.valueOf(STo_headerlist.get(0).getRec_Site_log1()));

                            spiner_storage_location_from.setEnabled(false);
                            spiner_storage_location_to.setEnabled(false);
                            Log.e("TAG", "SearchBarCodeFromSoap: "+Iss_Site_Log_list.get(0) );
                            if (Iss_Site_Log_list.get(0).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                                    || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {
                                txt_descripation_search.setText(STo_searchlist_btn.get(j).getUOM_DESC1());
                                txt_code_item_search.setText(STo_searchlist_btn.get(j).getMAT_CODE1().subSequence(6, 18));
                                txt_state_item_search.setText(STo_searchlist_btn.get(j).getSTATUS1());
                                Double total = Double.valueOf(STo_searchlist_btn.get(j).getAVAILABLE_STOCK1()) - Double.valueOf(STo_searchlist_btn.get(j).getQTY1());
                                txt_available_to_site_search.setText(String.valueOf(
                                        new DecimalFormat("###.##").format(Double.valueOf(total))));
                                txt_from_site_search.setText(STo_searchlist_btn.get(j).getISS_SITE1());
                                txt_to_site_search.setText(STo_searchlist_btn.get(j).getREC_SITE1());

                                edit_asked_from_site_search.setEnabled(true);
                                edit_asked_from_site_search.requestFocus();
                            }else {
                                editbarcodeforsoap.setError("???? ?????????? storage location");
                                editbarcodeforsoap.requestFocus();
                                edit_asked_from_site_search.setEnabled(false);
                            }
                        } else {
                            List<String> Iss_Site_Log_list = new ArrayList<>();

                            for (int i = 0; i < STo_searchlist_btn.size(); i++) {
                                if (!Iss_Site_Log_list.contains(STo_searchlist_btn.get(i).getISS_STG_LOG1())) {
                                    Iss_Site_Log_list.add(STo_searchlist_btn.get(i).getISS_STG_LOG1());
                                    Log.e("getISS_STG", "" + STo_searchlist_btn.get(i).getISS_STG_LOG1());
                                }
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_from.setAdapter(adapter);

                            List<String> Rec_Site_Log_list = new ArrayList<>();

                            for (int i = 0; i < STo_searchlist_btn.size(); i++) {
                                if (!Rec_Site_Log_list.contains(STo_searchlist_btn.get(i).getREC_SITE_LOG1())) {
                                    Rec_Site_Log_list.add(STo_searchlist_btn.get(i).getREC_SITE_LOG1());
                                    Log.e("getISS_STG", "" + STo_searchlist_btn.get(i).getREC_SITE_LOG1());
                                }
                            }
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TransferSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Rec_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_to.setAdapter(adapter2);


                            if (Iss_Site_Log_list.get(j).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                                    || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {
                                txt_descripation_search.setText(STo_searchlist_btn.get(j).getUOM_DESC1());
                                txt_code_item_search.setText(STo_searchlist_btn.get(j).getMAT_CODE1().subSequence(6, 18));
                                txt_state_item_search.setText(STo_searchlist_btn.get(j).getSTATUS1());
                                Double total = Double.valueOf(STo_searchlist_btn.get(j).getAVAILABLE_STOCK1()) - Double.valueOf(STo_searchlist_btn.get(j).getQTY1());
                                txt_available_to_site_search.setText(String.valueOf(
                                        new DecimalFormat("###.##").format(Double.valueOf(total))));
                                txt_from_site_search.setText(STo_searchlist_btn.get(j).getISS_SITE1());
                                txt_to_site_search.setText(STo_searchlist_btn.get(j).getREC_SITE1());


                                edit_asked_from_site_search.setEnabled(true);
                                edit_asked_from_site_search.requestFocus();

                            }else {
                                editbarcodeforsoap.setError("???? ?????????? storage location");
                                editbarcodeforsoap.requestFocus();
                                edit_asked_from_site_search.setEnabled(false);
                            }
                        }



                        break;
                    }
            }
            } else {
                Toast.makeText(this, "this else", Toast.LENGTH_SHORT).show();
                edit_asked_from_site_search.setText("");
                txt_descripation_search.setText("?????? ??????????");
                txt_code_item_search.setText("?????? ??????????");
                txt_state_item_search.setText("???????? ??????????");
                txt_available_to_site_search.setText("???????????? ??????????????");
                getLoaderManager().initLoader(LOADER_ID, null, TransferSearchActivity.this);
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
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Search_Barcode , Constant.METHOD_For_Search_Barcode);
//                request.addProperty("sap-client", "200");
//                request.addProperty("sap-user", "test");
//                request.addProperty("sap-password", "12345789");
                if (editbarcodeforsoap.getText().toString().startsWith("23")){
                    request.addProperty("GTIN", Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
                    Log.e("zzzbarcode",""+editbarcodeforsoap.getText().toString().substring(0,7)+"00000");
                }else {
                    request.addProperty("GTIN", editbarcodeforsoap.getText().toString());
                }
                request.addProperty("ISS_SITE", FromSite);
                request.addProperty("REC_SITE", ToSite);

                RETURN="Empty";
                MESSAGE="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                //envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Search_Barcode);
                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Search_Barcode, envelope);
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
//                    Log.d("getPropertyCoun", String.valueOf(soapObject_All_response.getPropertyCount()));

                    for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                        Log.d("soapObject", String.valueOf(soapObject_All_response.getProperty(i)));

                        SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);
                        RETURN = (String) soapObject_All_response.getProperty(0).toString();
                        //    Log.d("getPropertyCounitems", String.valueOf(soapObject_items.getPropertyCount()));

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
                                    MESSAGE=soapObject_items_detials.getProperty(1).toString();
                                    //RETURN = (String) soapObject_All_response.getProperty(0).toString();
                                       /* if(RETURN.contains("anyType{}")){

                                        }*/
                                }if (i==1 && RETURN.contains("anyType{}")){
                                    ReturnSearchList.add(String.valueOf(soapObject_items_detials.getProperty(k)));
                                    if (k ==9 &&String.valueOf(soapObject_items_detials.getProperty(5)).contains("anyType{}")){
                                        Log.e("String.valueOf(soapObject_items_detials.getProperty(5)).contains()",soapObject_items_detials.getProperty(5).toString());

                                        if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                            long id = databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                    editbarcodeforsoap.getText().toString().substring(0,7)+"00000"),
                                                    FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                    ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                    ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                    ReturnSearchList.get(7), "0.0");
                                        }else {
                                            long id = databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                    FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                    ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                    ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                    ReturnSearchList.get(7), "0.0");
                                        }
                                        //databaseHelperForTransfer.Update_Sto_header_For_Iss_Site_log(ReturnSearchList.get(4));
                                        Log.e("ReturnSearchList",""+ReturnSearchList.get(4));

                                        Log.d("For_each_itemk=1=9", ReturnSearchList.get(3)+ReturnSearchList.get(4)+ReturnSearchList.get(5));
                                        ReturnSearchList.clear();
                                    }else if (k ==9 && !String.valueOf(soapObject_items_detials.getProperty(5)).contains("anyType{}")){
                                        Log.e("!String.valueOf(soapObject_items_detials.getProperty(5))",String.valueOf(soapObject_items_detials.getProperty(5)).toString());
                                        if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                            CheckItemssize = databaseHelperForTransfer.selectSto_Search_weightBarcoe(editbarcodeforsoap.getText().toString().substring(0,7)+"00000");
                                        }else {
                                            CheckItemssize = databaseHelperForTransfer.selectSto_Search(editbarcodeforsoap.getText().toString());
                                        }
                                        Log.e("CheckItemssizeelseif",""+CheckItemssize.size());
                                        Log.e("CheckItemssizeevvv",""+CheckItemssize.size() *2);
                                        Log.e("CheckItemssizeelvvitv",""+soapObject_items.getPropertyCount());

                                        if (soapObject_items.getPropertyCount() > (CheckItemssize.size() *2)){
                                            if (j < (CheckItemssize.size())){
                                                long id= databaseHelperForTransfer.Update_Sto_search_For_Rec_Site(ReturnSearchList.get(5));
                                                Log.e("itemkif(j",""+id);
                                                Log.d("For_each_itemk=2=9", ReturnSearchList.get(3)+ReturnSearchList.get(4)+ReturnSearchList.get(5));
                                            }else {

                                                if (editbarcodeforsoap.getText().toString().startsWith("23")){
                                                    if(CheckItemssize.size() >0) {
                                                        long id = databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                                editbarcodeforsoap.getText().toString().substring(0,7)+"00000"),
                                                                FromSite,ToSite,ReturnSearchList.get(0),ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2),ReturnSearchList.get(8),ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4),ReturnSearchList.get(5),CheckItemssize.get(0).getAVAILABLE_STOCK1(),
                                                                ReturnSearchList.get(7),"0.0");
                                                    }else {
                                                        long id = databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                                editbarcodeforsoap.getText().toString().substring(0,7)+"00000"),
                                                                FromSite,ToSite,ReturnSearchList.get(0),ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2),ReturnSearchList.get(8),ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4),ReturnSearchList.get(5),ReturnSearchList.get(6),
                                                                ReturnSearchList.get(7),"0.0");
                                                    }

                                                }else {
                                                    if(CheckItemssize.size() >0) {
                                                        long id = databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), CheckItemssize.get(0).getAVAILABLE_STOCK1(),
                                                                ReturnSearchList.get(7), "0.0");
                                                    }else {
                                                        long id = databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                                ReturnSearchList.get(7), "0.0");
                                                    }

                                                }



                                                Log.d("For_each_itemk=3=9", ReturnSearchList.get(3)+ReturnSearchList.get(4)+ReturnSearchList.get(5));
                                                Log.e("itemkif(jelse",""+id);
                                                ReturnSearchList.clear();
                                            }

                                        }else {
                                            long id= databaseHelperForTransfer.Update_Sto_search_For_Rec_Site(ReturnSearchList.get(5));
                                            Log.d("For_each_itemk=4=9", ReturnSearchList.get(3)+ReturnSearchList.get(4)+ReturnSearchList.get(5));
                                            // databaseHelperForTransfer.Update_Sto_Header_For_Rec_Site(ReturnSearchList.get(5));
                                            Log.e("elseount()>(C",""+id);
                                            Log.d("elseount()>(C", ReturnSearchList.get(3)+ReturnSearchList.get(4)+ReturnSearchList.get(5));

                                        }
                                    }
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

    private String Calculatcheckdigitforscales(String toString) {
        String Barcode;
        int  chkdigit;
        Log.e("zzzbarodd1 ",""+toString.charAt(1));
        Log.e("zzzbarodd1 ",""+Integer.valueOf(toString.charAt(1)));
        Log.e("zzzbarodd1.2 ",""+Integer.valueOf(toString.substring(1,2)));
        Log.e("zzzbarodd11 ",""+toString.charAt(11));
        Log.e("zzzbarodd11.12 ",""+Integer.valueOf(toString.substring(11,12)));
        int odd  = Integer.valueOf(toString.substring(1,2)) + Integer.valueOf(toString.substring(3,4)) + Integer.valueOf(toString.substring(5,6)) + Integer.valueOf(toString.substring(7,8)) + Integer.valueOf(toString.substring(9,10)) + Integer.valueOf(toString.substring(11,12));
        int eveen  = Integer.valueOf(toString.substring(0,1)) + Integer.valueOf(toString.substring(2,3)) + Integer.valueOf(toString.substring(4,5)) + Integer.valueOf(toString.substring(6,7)) + Integer.valueOf(toString.substring(8,9)) + Integer.valueOf(toString.substring(10,11));

        Log.e("zzzbarodd",""+odd);
        Log.e("zzzbareveen",""+eveen);
        if ((((odd * 3) + eveen) % 10) != 0 )
            chkdigit = 10 - (((odd * 3) + eveen) % 10) ;
        else
            chkdigit = 0 ;

        Barcode=toString +chkdigit;
        Log.e("zzzbarcode",""+Barcode);
        return Barcode;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Toast.makeText(TransferSearchActivity.this, "finished ", Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.d("soaMESSAGE4", "" + MESSAGE);
        Log.e("This Is First Time", "" + RETURN);

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
            editbarcodeforsoap.setError("" + EnvelopeBodyInCurrent);
            edit_asked_from_site_search.setEnabled(false);
            // btn_loading_purchase_order.setEnabled(true);
        } else if (RETURN.contains("anyType{}")) {
            List<STo_Search> STo_searchlist_bg = new ArrayList<>();
            Log.e("This Is First Time", "" + RETURN);
            Toast.makeText(TransferSearchActivity.this, RETURN, Toast.LENGTH_LONG).show();
            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                STo_searchlist_bg = databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
            } else {
                STo_searchlist_bg = databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString());
            }
//            Log.e("ZZONFINISHTime", "ZZ" + (STo_searchlist_bg.get(0).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0")));
            if (STo_searchlist_bg.size() > 0) {
                for (int j = 0; j < STo_searchlist_bg.size(); j++) {
                    if (STo_searchlist_bg.get(j).getSTATUS1().equalsIgnoreCase("1")) {
                        editbarcodeforsoap.setError("?????? ???????????????? ?????? ????????");
                        edit_asked_from_site_search.setEnabled(false);

                    } else if ((STo_searchlist_bg.get(j).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0"))
                            && !(FromSite.equalsIgnoreCase("01MW")
                            || ToSite.equalsIgnoreCase("01MW"))) { //TODO check qty
                        Log.d("getISSbbbbb_STG_AVAILABLE_STO_j", j + "zz " + STo_searchlist_bg.get(j).getAVAILABLE_STOCK1());
                        if (j == STo_searchlist_bg.size()-1) {
                            Log.d("getISSbbbbb_STG_AVAILABLE_STO", "zz " + STo_searchlist_bg.get(j).getAVAILABLE_STOCK1());
                            Log.d("getISSbbbbb_STG_FromSite", "z" + FromSite);
                            editbarcodeforsoap.setError("???? ???????? ???????? ?????????? ??????????????");
                            edit_asked_from_site_search.setEnabled(false);
                        }
                    } else {



                        List<String> Iss_Site_Log_list = new ArrayList<>();
                        for (int i = 0; i < STo_searchlist_bg.size(); i++) {
                            if (!Iss_Site_Log_list.contains(STo_searchlist_bg.get(i).getISS_STG_LOG1())) {
                                Iss_Site_Log_list.add(STo_searchlist_bg.get(i).getISS_STG_LOG1());
                                Log.e("getISS_STG", "" + STo_searchlist_bg.get(i).getISS_STG_LOG1());
                            }
                        }

                        Iss_Site_Log_list.remove("anyType{}");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferSearchActivity.this,
                                android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiner_storage_location_from.setAdapter(adapter);

                        List<String> Rec_Site_Log_list = new ArrayList<>();

                        for (int i = 0; i < STo_searchlist_bg.size(); i++) {
                            if (!Rec_Site_Log_list.contains(STo_searchlist_bg.get(i).getREC_SITE_LOG1())) {
                                Rec_Site_Log_list.add(STo_searchlist_bg.get(i).getREC_SITE_LOG1());
                                Log.e("getISS_STG", "" + STo_searchlist_bg.get(i).getREC_SITE_LOG1());
                            }
                        }
                        Rec_Site_Log_list.remove("anyType{}");
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TransferSearchActivity.this,
                                android.R.layout.simple_spinner_item, Rec_Site_Log_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiner_storage_location_to.setAdapter(adapter2);
                        Log.e("TAG", "onLoadFinished: "+ databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1());
                        Log.e("TAG", "onLoadFinished: "+ Iss_Site_Log_list.get(j));

                        if (Iss_Site_Log_list.get(j).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                        || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {

                            txt_descripation_search.setText(STo_searchlist_bg.get(j).getUOM_DESC1());
                            txt_code_item_search.setText(STo_searchlist_bg.get(j).getMAT_CODE1().subSequence(6, 18));
                            txt_state_item_search.setText(STo_searchlist_bg.get(j).getSTATUS1());
                            txt_available_to_site_search.setText(STo_searchlist_bg.get(j).getAVAILABLE_STOCK1());

                            Long id = databaseHelperForTransfer.Update_Sto_header_For_Iss_Site_log_if_has_anytypevalue(STo_searchlist_bg.get(j).getISS_STG_LOG1());
                            Log.e("getISSbbbbb_STG", "" + STo_searchlist_bg.get(j).getISS_STG_LOG1());
                            Log.e("getISSbbbbb_STG_update_id ", "" + id);
                            edit_asked_from_site_search.setEnabled(true);
                            edit_asked_from_site_search.requestFocus();
                            spiner_storage_location_from.setSelection(j);
                        }else {
                            editbarcodeforsoap.setError("???? ?????????? storage location");
                            editbarcodeforsoap.requestFocus();
                            edit_asked_from_site_search.setEnabled(false);
                        }
                        break;
                    }

                }
            /*Go_To_ScanRecieving.putExtra("This Is First Time",This_Is_First_Time);
            Log.e("This Is First Time","true");
            startActivity(Go_To_ScanRecieving);
            btn_loading_purchase_order.setEnabled(true);*/
            }
        }else {
            // Toast.makeText(TransferSearchActivity.this,MESSAGE,Toast.LENGTH_LONG).show();
            edit_asked_from_site_search.setEnabled(false);
            editbarcodeforsoap.setError(MESSAGE);
            MESSAGE=" ";
            //  btn_loading_purchase_order.setEnabled(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void Show_Buy_Data(View view) {
        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
        //Log.e("STo_headerlist",""+STo_searchlist.get(0).getIss_Strg_Log1());
        if (STo_headerlist.size() <=0){
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        }else {
            Intent gotoshowBuyData=new Intent(TransferSearchActivity.this, ShowOrderDataActivity.class);
            gotoshowBuyData.putExtra("Iss_Stg_Log",STo_headerlist.get(0).getIss_Strg_Log1());
            gotoshowBuyData.putExtra("Rec_Stg_Log",STo_headerlist.get(0).getRec_Site_log1());
            gotoshowBuyData.putExtra("MakeOrder", makeOrder);
            gotoshowBuyData.putExtra("Department", Department);
            gotoshowBuyData.putExtra("FromSite", FromSite);
            gotoshowBuyData.putExtra("ToSite", ToSite);
            startActivity(gotoshowBuyData);
            finish();

        }

    }


    public void SaveDelivered(View view) {
        if (editbarcodeforsoap.getText().toString().isEmpty()) {
            editbarcodeforsoap.setError("???? ???????? ???????? ????????????????");
        } else if (edit_asked_from_site_search.getText().toString().isEmpty()) {
            edit_asked_from_site_search.setError("???? ???????? ???????? ????????????");
        } else if ((Double.valueOf(edit_asked_from_site_search.getText().toString()) >
                Double.valueOf(txt_available_to_site_search.getText().toString()))
                && !(FromSite.equalsIgnoreCase("01MW")
                || ToSite.equalsIgnoreCase("01MW"))) {//TODO check qty
            edit_asked_from_site_search.setError("?????? ???????????? ???????? ???? ???????????? ??????????????...????????????" + txt_available_to_site_search.getText().toString());
            Log.e("zzsav1", "n" + FromSite.equalsIgnoreCase("01MW"));
            Log.e("zzsav1", "n" + !(FromSite.equalsIgnoreCase("01MW")
                    || ToSite.equalsIgnoreCase("01MW")));

        } else {

            databaseHelperForTransfer.Update_Sto_header_For_Iss_Site_log(spiner_storage_location_from.getSelectedItem().toString());
            databaseHelperForTransfer.Update_Sto_Header_For_Rec_Site_log(spiner_storage_location_to.getSelectedItem().toString());

            spiner_storage_location_from.setEnabled(false);
            spiner_storage_location_to.setEnabled(false);
            Log.e("databaseHelperForTrans", "" + spiner_storage_location_from.getSelectedItem().toString().length());
            Log.e("databaseHelperForTrans", "" + spiner_storage_location_to.getSelectedItem().toString().length());
            List<STo_Search> STo_searchlist_btn = new ArrayList<>();
            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase_For_save_QTY_in_transfersearchactivity(
                        spiner_storage_location_from.getSelectedItem().toString()
                        , spiner_storage_location_to.getSelectedItem().toString()
                        , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
            }else {
                STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase_For_save_QTY_in_transfersearchactivity(
                        spiner_storage_location_from.getSelectedItem().toString()
                        , spiner_storage_location_to.getSelectedItem().toString()
                        , editbarcodeforsoap.getText().toString());
            }
            Log.e("TAG", "SaveDelivered: STo_searchlist_btneditba "+ editbarcodeforsoap.getText().toString());
            Log.e("TAG", "SaveDelivered: STo_searchlist_btnstro "+ spiner_storage_location_from.getSelectedItem().toString());

            if (STo_searchlist_btn.size() == 0) {
                Log.e("TAG", "SaveDelivered: STo_searchlist_btn.size() == 0 "+ spiner_storage_location_from.getSelectedItem().toString());
                Log.e("TAG", "SaveDelivered: STo_searchlist_btn.size() == 0 "+ spiner_storage_location_to.getSelectedItem().toString());

                if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                    databaseHelperForTransfer.Update_Sto_search_For_QTY(edit_asked_from_site_search.getText().toString(),
                            spiner_storage_location_from.getSelectedItem().toString()
                            , spiner_storage_location_to.getSelectedItem().toString()
                            ,  Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));
                }else {
                    databaseHelperForTransfer.Update_Sto_search_For_QTY(edit_asked_from_site_search.getText().toString(),
                            spiner_storage_location_from.getSelectedItem().toString()
                            , spiner_storage_location_to.getSelectedItem().toString()
                            , editbarcodeforsoap.getText().toString());
                }


                Double AvaliableQty = Double.valueOf(txt_available_to_site_search.getText().toString()) -
                        Double.valueOf(edit_asked_from_site_search.getText().toString());
                Log.e("AvaliableQty", "" + AvaliableQty);

                txt_available_to_site_search.setText("" + String.valueOf(
                        new DecimalFormat("###.##").format(Double.valueOf(AvaliableQty))));
                edit_asked_from_site_search.setText("");
                edit_asked_from_site_search.setError(null);
                edit_asked_from_site_search.setHint("Done");
                editbarcodeforsoap.setText("");
                editbarcodeforsoap.requestFocus();
                /*edit_asked_from_site_search.setEnabled(false);
                txt_descripation_search.setText("?????? ??????????");
                txt_code_item_search.setText("?????? ??????????");
                txt_state_item_search.setText("???????? ??????????");
                txt_available_to_site_search.setText("???????????? ??????????????");*/

            }else {
                Double AvaliableQty = Double.valueOf(STo_searchlist_btn.get(0).getAVAILABLE_STOCK1());
                Double lastQty = 0.0;
                if (STo_searchlist_btn.get(0).getQTY1().isEmpty()) {
                    lastQty = 0.0;
                } else {
                    lastQty = Double.valueOf(STo_searchlist_btn.get(0).getQTY1());
                }
                Double currentQty = Double.valueOf(edit_asked_from_site_search.getText().toString());
                Double SumQty = lastQty + currentQty;
                Log.e("zzdatabase", "" + (SumQty <= AvaliableQty));
                Log.e("zzdatabase", "" + !(FromSite.equalsIgnoreCase("01MW")
                        || ToSite.equalsIgnoreCase("01MW")));

                if ((SumQty <= AvaliableQty) || (FromSite.equalsIgnoreCase("01MW")
                        || ToSite.equalsIgnoreCase("01MW"))) {
                    Log.e("SumQty", "" + SumQty);
                    Log.e("SumQty", "" + spiner_storage_location_from.getSelectedItem().toString());
                    Log.e("SumQty", "" + spiner_storage_location_to.getSelectedItem().toString());
                    Log.e("SumQty", "" + editbarcodeforsoap.getText().toString());
                    if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                        new AlertDialog.Builder(this)
                                .setTitle("???? ?????????? ?????????? ???? ?????? ?????????? "+lastQty +"?????? ?????? ?????????? ????????????")
                                .setPositiveButton("??????????", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelperForTransfer.Update_Sto_search_For_QTY(String.valueOf(new DecimalFormat("###.##").format(Double.valueOf(SumQty)))
                                                , spiner_storage_location_from.getSelectedItem().toString()
                                                , spiner_storage_location_to.getSelectedItem().toString()
                                                ,Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0,7)+"00000"));

                                        edit_asked_from_site_search.setText("");
                                        edit_asked_from_site_search.setHint("Done");
                                        txt_available_to_site_search.setText("" + String.valueOf(
                                                new DecimalFormat("###.##").format(Double.valueOf(AvaliableQty - SumQty))));
                                        editbarcodeforsoap.setText("");
                                        editbarcodeforsoap.requestFocus();

                                    }
                                })
                                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                })*/
                                .show();

                    }else {
                        new AlertDialog.Builder(this)
                                .setTitle("???? ?????????? ?????????? ???? ?????? ?????????? "+lastQty +"?????? ?????? ?????????? ????????????")
                                .setPositiveButton("??????????", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelperForTransfer.Update_Sto_search_For_QTY(String.valueOf(new DecimalFormat("###.##").format(Double.valueOf(SumQty)))
                                                , spiner_storage_location_from.getSelectedItem().toString()
                                                , spiner_storage_location_to.getSelectedItem().toString()
                                                , editbarcodeforsoap.getText().toString());

                                        edit_asked_from_site_search.setText("");
                                        edit_asked_from_site_search.setHint("Done");
                                        txt_available_to_site_search.setText("" + String.valueOf(
                                                new DecimalFormat("###.##").format(Double.valueOf(AvaliableQty - SumQty))));
                                        editbarcodeforsoap.setText("");
                                        editbarcodeforsoap.requestFocus();
                                    }
                                })
                                /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                })*/
                                .show();


                    }

                    /*editbarcodeforsoap.setText("");
                    editbarcodeforsoap.requestFocus();
                    edit_asked_from_site_search.setEnabled(false);
                    txt_descripation_search.setText("?????? ??????????");
                    txt_code_item_search.setText("?????? ??????????");
                    txt_state_item_search.setText("???????? ??????????");
                    txt_available_to_site_search.setText("???????????? ??????????????");*/

                } else {//TODO check qty
                    edit_asked_from_site_search.setError("?????? ???????????? ???????? ???? ???????????? ??????????????... ????????????" + AvaliableQty);
                }
            }

        }
        //  txt_available_to_site_search.getText().toString()
    }

    public void Go_To_Upload_Form(View view) {
        String StatusNU = databaseHelperForTransfer.select_what_has_Status_value();
        if (StatusNU.length() != 0) {
            Toast.makeText(this, "?????? ?????????? ???? ???????????? ????????" + StatusNU, Toast.LENGTH_SHORT).show();
        } else {
            List<STo_Search> sTo_searchList_upload = new ArrayList<>();
            sTo_searchList_upload = databaseHelperForTransfer.selectSto_Search_for_Qty();
            if (sTo_searchList_upload.size() > 0) {
                    Intent gotoupload = new Intent(TransferSearchActivity.this, UploadForTransferActivity.class);
                    startActivity(gotoupload);
                    finish();

            } else {
                Toast.makeText(this, "???? ?????????? 0.0", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
