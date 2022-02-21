package com.example.connecttosoapapiapp.makeOrder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.ShowOrderDataActivity;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateOrderSearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    private EditText editbarcodeforsoap, edit_asked_from_site_search;
    private String FromSite, ToSite, Department;
    private final int LOADER_ID = 1;
    private DatabaseHelperForTransfer databaseHelperForTransfer;
    private Boolean makeOrder = false;

    private String EnvelopeBodyInConstant, EnvelopeBodyInCurrent, RETURN, MESSAGE;
    private List<String> ReturnSearchList;
    private List<STo_Search> CheckItemssize;
    private TextView txt_descripation_search, txt_code_item_search, txt_state_item_search,
            txt_from_site_search, txt_available_to_site_search;
    private Spinner spiner_storage_location_from;
    private List<STO_Header> STo_headerlist;
    private ProgressBar searchProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_search);
        editbarcodeforsoap = findViewById(R.id.add_order_search_barcode);

        editbarcodeforsoap.requestFocus();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Create Order");



        spiner_storage_location_from = findViewById(R.id.add_order_from_site_spinner);
        txt_descripation_search = findViewById(R.id.add_order_item_desc);
        txt_code_item_search = findViewById(R.id.add_order_item_code);
        txt_state_item_search = findViewById(R.id.add_order_item_status);
        txt_from_site_search = findViewById(R.id.add_order_from_site);
        txt_available_to_site_search = findViewById(R.id.add_order_available_in_site);
        edit_asked_from_site_search = findViewById(R.id.add_order_requested_amount_edit);
        searchProgress = findViewById(R.id.search_pgb);


        databaseHelperForTransfer = new DatabaseHelperForTransfer(this);

        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();

        makeOrder = true;

        Department = getIntent().getExtras().getString("Department");
        FromSite = getIntent().getExtras().getString("FromSite");
        ToSite = getIntent().getExtras().getString("FromSite");


        txt_from_site_search.setText(FromSite);



        if (STo_headerlist.size() > 0) {

            if (!STo_headerlist.get(0).getRec_Site_log1().contains("anyType{}")) { //anyType{}
                List<String> Iss_Site_Log_list = new ArrayList<>();

                Iss_Site_Log_list.add(STo_headerlist.get(0).getIss_Strg_Log1());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOrderSearchActivity.this,
                        android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiner_storage_location_from.setAdapter(adapter);


                spiner_storage_location_from.setEnabled(false);

            }
        }

        spiner_storage_location_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(CreateOrderSearchActivity.this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                List<STo_Search> STo_searchlist_btn;
                if (!editbarcodeforsoap.getText().toString().isEmpty()) {
                    if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_from.getSelectedItem().toString()
                                , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
                    } else
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_from.getSelectedItem().toString()
                                , editbarcodeforsoap.getText().toString());

                    if (STo_searchlist_btn.size() != 0) {
                        txt_descripation_search.setText(STo_searchlist_btn.get(0).getUOM_DESC1());
                        txt_code_item_search.setText(STo_searchlist_btn.get(0).getMAT_CODE1().subSequence(6, 18));
                        txt_state_item_search.setText(STo_searchlist_btn.get(0).getSTATUS1());
                        double total = Double.parseDouble(STo_searchlist_btn.get(0).getAVAILABLE_STOCK1()) - Double.parseDouble(STo_searchlist_btn.get(0).getQTY1());
                        txt_available_to_site_search.setText(String.valueOf(
                                new DecimalFormat("###.##").format(Double.valueOf(total))));
                        txt_from_site_search.setText(STo_searchlist_btn.get(0).getISS_SITE1());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void SearchBarCodeFromSoap(View view) {

        edit_asked_from_site_search.setHint("المطلوب تحويله");

        if (editbarcodeforsoap.getText().toString().isEmpty()) {
            editbarcodeforsoap.setError("من فضلك ادخل الباركود");
        } else {
            List<STo_Search> STo_searchlist_btn;
            searchProgress.setVisibility(View.VISIBLE);
            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                STo_searchlist_btn = databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
            } else {
                STo_searchlist_btn = databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString());
            }
            if (STo_searchlist_btn.size() > 0) {
                for (int j = 0; j < STo_searchlist_btn.size(); j++) {
                    if (STo_searchlist_btn.get(j).getSTATUS1().equalsIgnoreCase("1")) {
                        editbarcodeforsoap.setError("هذا الباركود غير فعال");
                        searchProgress.setVisibility(View.GONE);
                    } else if ((STo_searchlist_btn.get(j).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0"))
                            && !(FromSite.equalsIgnoreCase("01MW")
                            || ToSite.equalsIgnoreCase("01MW"))) {
                        Log.d("getISSbbbbb_STG_AVAILABLE_STO_j", j + "zz " + STo_searchlist_btn.get(j).getAVAILABLE_STOCK1());
                        if (j == STo_searchlist_btn.size() - 1) {
                            editbarcodeforsoap.setError("لا يوجد كميه متاحه للتحويل");
                            edit_asked_from_site_search.setEnabled(false);
                            searchProgress.setVisibility(View.GONE);
                        }
                    } else {
                        STo_headerlist = new ArrayList<>();

                        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
                        Toast.makeText(this, "intent is null", Toast.LENGTH_SHORT).show();
                        List<String> Iss_Site_Log_list = new ArrayList<>();
                        if (!STo_headerlist.get(0).getRec_Site_log1().contains("anyType{}")) { //anyType{}

                            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                Iss_Site_Log_list.add(databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000")).get(0).getISS_STG_LOG1());
                            } else {
                                Iss_Site_Log_list.add(databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString()).get(0).getISS_STG_LOG1());

                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOrderSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_from.setAdapter(adapter);


                            spiner_storage_location_from.setEnabled(false);

                            if (Iss_Site_Log_list.get(0).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                                    || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {
                                txt_descripation_search.setText(STo_searchlist_btn.get(j).getUOM_DESC1());
                                txt_code_item_search.setText(STo_searchlist_btn.get(j).getMAT_CODE1().subSequence(6, 18));
                                txt_state_item_search.setText(STo_searchlist_btn.get(j).getSTATUS1());
                                Double total = Double.parseDouble(STo_searchlist_btn.get(j).getAVAILABLE_STOCK1()) - Double.parseDouble(STo_searchlist_btn.get(j).getQTY1());
                                txt_available_to_site_search.setText(String.valueOf(
                                        new DecimalFormat("###.##").format(total)));
                                txt_from_site_search.setText(STo_searchlist_btn.get(j).getISS_SITE1());

                                edit_asked_from_site_search.setEnabled(true);
                                edit_asked_from_site_search.requestFocus();
                                searchProgress.setVisibility(View.GONE);
                            } else {
                                editbarcodeforsoap.setError("تم تغيير storage location");
                                editbarcodeforsoap.requestFocus();
                                edit_asked_from_site_search.setEnabled(false);
                                searchProgress.setVisibility(View.GONE);
                            }
                        } else {

                            for (int i = 0; i < STo_searchlist_btn.size(); i++) {
                                if (!Iss_Site_Log_list.contains(STo_searchlist_btn.get(i).getISS_STG_LOG1())) {
                                    Iss_Site_Log_list.add(STo_searchlist_btn.get(i).getISS_STG_LOG1());
                                }
                                searchProgress.setVisibility(View.GONE);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOrderSearchActivity.this,
                                    android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_storage_location_from.setAdapter(adapter);

                            List<String> Rec_Site_Log_list = new ArrayList<>();

                            for (int i = 0; i < STo_searchlist_btn.size(); i++) {
                                if (!Rec_Site_Log_list.contains(STo_searchlist_btn.get(i).getREC_SITE_LOG1())) {
                                    Rec_Site_Log_list.add(STo_searchlist_btn.get(i).getREC_SITE_LOG1());
                                }
                            }


                            if (Iss_Site_Log_list.get(j).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                                    || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {
                                txt_descripation_search.setText(STo_searchlist_btn.get(j).getUOM_DESC1());
                                txt_code_item_search.setText(STo_searchlist_btn.get(j).getMAT_CODE1().subSequence(6, 18));
                                txt_state_item_search.setText(STo_searchlist_btn.get(j).getSTATUS1());
                                double total = Double.parseDouble(STo_searchlist_btn.get(j).getAVAILABLE_STOCK1()) - Double.parseDouble(STo_searchlist_btn.get(j).getQTY1());
                                txt_available_to_site_search.setText(String.valueOf(
                                        new DecimalFormat("###.##").format(Double.valueOf(total))));
                                txt_from_site_search.setText(STo_searchlist_btn.get(j).getISS_SITE1());


                                edit_asked_from_site_search.setEnabled(true);
                                edit_asked_from_site_search.requestFocus();
                                searchProgress.setVisibility(View.GONE);

                            } else {
                                editbarcodeforsoap.setError("تم تغيير storage location");
                                editbarcodeforsoap.requestFocus();
                                edit_asked_from_site_search.setEnabled(false);
                                searchProgress.setVisibility(View.GONE);
                            }
                        }


                        break;
                    }
                }
            } else {
                edit_asked_from_site_search.setText("");
                txt_descripation_search.setText("وصف الصنف");
                txt_code_item_search.setText("كود الصنف");
                txt_state_item_search.setText("حاله الصنف");
                txt_available_to_site_search.setText("المتاح بالمخزن");
                getLoaderManager().initLoader(LOADER_ID, null, CreateOrderSearchActivity.this);
                searchProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader = new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {


                SoapObject request = new SoapObject(Constant.NAMESPACE_For_Search_Barcode, Constant.METHOD_For_Search_Barcode);
                if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                    request.addProperty("GTIN", Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
                } else {
                    request.addProperty("GTIN", editbarcodeforsoap.getText().toString());
                }
                request.addProperty("ISS_SITE", FromSite);
                request.addProperty("REC_SITE", FromSite);

                RETURN = "Empty";
                MESSAGE = "Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_For_Search_Barcode);
                try {
                    httpTransport.call(Constant.SOAP_ACTION_For_Search_Barcode, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                ReturnSearchList = new ArrayList<>();


                if (!EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;

                    for (int i = 0; i < soapObject_All_response.getPropertyCount(); i++) {

                        SoapObject soapObject_items = (SoapObject) soapObject_All_response.getProperty(i);
                        RETURN = (String) soapObject_All_response.getProperty(0).toString();

                        for (int j = 0; j < soapObject_items.getPropertyCount(); j++) {

                            SoapObject soapObject_items_detials = (SoapObject) soapObject_items.getProperty(j);

                            for (int k = 0; k < soapObject_items_detials.getPropertyCount(); k++) {

                                if (i == 0) {
                                    MESSAGE = soapObject_items_detials.getProperty(1).toString();

                                }
                                if (i == 1 && RETURN.contains("anyType{}")) {
                                    ReturnSearchList.add(String.valueOf(soapObject_items_detials.getProperty(k)));
                                    if (k == 9 && String.valueOf(soapObject_items_detials.getProperty(5)).contains("anyType{}")) {

                                        if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                             databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                    editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"),
                                                    FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                    ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                    ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                    ReturnSearchList.get(7), "0.0");
                                        } else {
                                             databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                    FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                    ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                    ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                    ReturnSearchList.get(7), "0.0");
                                        }

                                        ReturnSearchList.clear();

                                    } else if (k == 9 && !String.valueOf(soapObject_items_detials.getProperty(5)).contains("anyType{}")) {

                                        if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                            CheckItemssize = databaseHelperForTransfer.selectSto_Search_weightBarcoe(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000");
                                        } else {
                                            CheckItemssize = databaseHelperForTransfer.selectSto_Search(editbarcodeforsoap.getText().toString());
                                        }

                                        if (soapObject_items.getPropertyCount() > (CheckItemssize.size() * 2)) {
                                            if (j < (CheckItemssize.size())) {
                                                 databaseHelperForTransfer.Update_Sto_search_For_Rec_Site(ReturnSearchList.get(5));

                                            } else {

                                                if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                                    if (CheckItemssize.size() > 0) {
                                                         databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                                editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), CheckItemssize.get(0).getAVAILABLE_STOCK1(),
                                                                ReturnSearchList.get(7), "0.0");
                                                    } else {
                                                         databaseHelperForTransfer.insert_Sto_Search(Calculatcheckdigitforscales(
                                                                editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                                ReturnSearchList.get(7), "0.0");
                                                    }

                                                } else {
                                                    if (CheckItemssize.size() > 0) {
                                                         databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), CheckItemssize.get(0).getAVAILABLE_STOCK1(),
                                                                ReturnSearchList.get(7), "0.0");
                                                    } else {
                                                         databaseHelperForTransfer.insert_Sto_Search(editbarcodeforsoap.getText().toString(),
                                                                FromSite, ToSite, ReturnSearchList.get(0), ReturnSearchList.get(1),
                                                                ReturnSearchList.get(2), ReturnSearchList.get(8), ReturnSearchList.get(3),
                                                                ReturnSearchList.get(4), ReturnSearchList.get(5), ReturnSearchList.get(6),
                                                                ReturnSearchList.get(7), "0.0");
                                                    }

                                                }
                                                ReturnSearchList.clear();
                                            }

                                        } else {
                                             databaseHelperForTransfer.Update_Sto_search_For_Rec_Site(ReturnSearchList.get(5));
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
        int chkdigit;
        int odd = Integer.parseInt(toString.substring(1, 2)) + Integer.parseInt(toString.substring(3, 4)) + Integer.parseInt(toString.substring(5, 6)) + Integer.valueOf(toString.substring(7, 8)) + Integer.valueOf(toString.substring(9, 10)) + Integer.valueOf(toString.substring(11, 12));
        int eveen = Integer.parseInt(toString.substring(0, 1)) + Integer.parseInt(toString.substring(2, 3)) + Integer.parseInt(toString.substring(4, 5)) + Integer.valueOf(toString.substring(6, 7)) + Integer.valueOf(toString.substring(8, 9)) + Integer.valueOf(toString.substring(10, 11));

        if ((((odd * 3) + eveen) % 10) != 0) {
            chkdigit = 10 - (((odd * 3) + eveen) % 10);
        }
        else {
            chkdigit = 0;
        }

        Barcode = toString + chkdigit;
        return Barcode;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        getLoaderManager().destroyLoader(LOADER_ID);
        searchProgress.setVisibility(View.GONE);

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {

            editbarcodeforsoap.setError("" + EnvelopeBodyInCurrent);
            edit_asked_from_site_search.setEnabled(false);

        } else if (RETURN.contains("anyType{}")) {
            List<STo_Search> STo_searchlist_bg;
            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                STo_searchlist_bg = databaseHelperForTransfer.Search__Barcode(Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
            } else {
                STo_searchlist_bg = databaseHelperForTransfer.Search__Barcode(editbarcodeforsoap.getText().toString());
            }
            if (STo_searchlist_bg.size() > 0) {
                for (int j = 0; j < STo_searchlist_bg.size(); j++) {
                    if (STo_searchlist_bg.get(j).getSTATUS1().equalsIgnoreCase("1")) {
                        editbarcodeforsoap.setError("هذا الباركود غير فعال");
                        edit_asked_from_site_search.setEnabled(false);

                    } else if ((STo_searchlist_bg.get(j).getAVAILABLE_STOCK1().equalsIgnoreCase("0.0"))
                            && !(FromSite.equalsIgnoreCase("01MW")
                            || ToSite.equalsIgnoreCase("01MW"))) { //TODO check qty
                        Log.d("getISSbbbbb_STG_AVAILABLE_STO_j", j + "zz " + STo_searchlist_bg.get(j).getAVAILABLE_STOCK1());
                        if (j == STo_searchlist_bg.size() - 1) {
                            editbarcodeforsoap.setError("لا يوجد كميه متاحه للتحويل");
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOrderSearchActivity.this,
                                android.R.layout.simple_spinner_item, Iss_Site_Log_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiner_storage_location_from.setAdapter(adapter);

                        List<String> Rec_Site_Log_list = new ArrayList<>();

                        for (int i = 0; i < STo_searchlist_bg.size(); i++) {
                            if (!Rec_Site_Log_list.contains(STo_searchlist_bg.get(i).getREC_SITE_LOG1())) {
                                Rec_Site_Log_list.add(STo_searchlist_bg.get(i).getREC_SITE_LOG1());
                            }
                        }

                        if (Iss_Site_Log_list.get(j).equalsIgnoreCase(databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1())
                                || databaseHelperForTransfer.selectSto_Header().get(0).getIss_Strg_Log1() == null) {

                            txt_descripation_search.setText(STo_searchlist_bg.get(j).getUOM_DESC1());
                            txt_code_item_search.setText(STo_searchlist_bg.get(j).getMAT_CODE1().subSequence(6, 18));
                            txt_state_item_search.setText(STo_searchlist_bg.get(j).getSTATUS1());
                            txt_available_to_site_search.setText(STo_searchlist_bg.get(j).getAVAILABLE_STOCK1());

                            databaseHelperForTransfer.Update_Sto_header_For_Iss_Site_log_if_has_anytypevalue(STo_searchlist_bg.get(j).getISS_STG_LOG1());
                            edit_asked_from_site_search.setEnabled(true);
                            edit_asked_from_site_search.requestFocus();
                            spiner_storage_location_from.setSelection(j);
                        } else {
                            editbarcodeforsoap.setError("تم تغيير storage location");
                            editbarcodeforsoap.requestFocus();
                            edit_asked_from_site_search.setEnabled(false);
                        }
                        break;
                    }

                }
            }
        } else {
            edit_asked_from_site_search.setEnabled(false);
            editbarcodeforsoap.setError(MESSAGE);
            MESSAGE = " ";
        }
    }


    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onBackPressed() {
        databaseHelperForTransfer.DeleteStoHeaderTable();
        databaseHelperForTransfer.DeleteStoSearchTable();
        Intent intent = new Intent(CreateOrderSearchActivity.this, ChooseSiteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void Show_Buy_Data(View view) {
        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
        if (STo_headerlist.size() <= 0) {
            Toast.makeText(this, "There is no data", Toast.LENGTH_SHORT).show();
        } else {
            Intent showData = new Intent(CreateOrderSearchActivity.this, ShowOrderDataActivity.class);
            showData.putExtra("Iss_Stg_Log", STo_headerlist.get(0).getIss_Strg_Log1());
            showData.putExtra("Rec_Stg_Log", STo_headerlist.get(0).getRec_Site_log1());
            showData.putExtra("MakeOrder", makeOrder);
            showData.putExtra("Department", Department);
            showData.putExtra("FromSite", FromSite);
            showData.putExtra("ToSite", ToSite);
            startActivity(showData);
            finish();

        }

    }


    @SuppressLint("SetTextI18n")
    public void SaveDelivered(View view) {
        if (editbarcodeforsoap.getText().toString().isEmpty()) {
            editbarcodeforsoap.setError("من فضلك أدخل الباركود");
        } else if (edit_asked_from_site_search.getText().toString().isEmpty()) {
            edit_asked_from_site_search.setError("من فضلك أدخل الكميه");
        } else if ((!(Double.parseDouble(edit_asked_from_site_search.getText().toString()) >
                Double.parseDouble(txt_available_to_site_search.getText().toString())))
                || (FromSite.equalsIgnoreCase("01MW")
                || ToSite.equalsIgnoreCase("01MW"))) {

                    databaseHelperForTransfer.Update_Sto_header_For_Iss_Site_log(spiner_storage_location_from.getSelectedItem().toString());
                    databaseHelperForTransfer.Update_Sto_Header_For_Rec_Site_log(spiner_storage_location_from.getSelectedItem().toString());

                    spiner_storage_location_from.setEnabled(false);


                    List<STo_Search> STo_searchlist_btn;
                    if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase_For_save_QTY_in_transfersearchactivity(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_from.getSelectedItem().toString()
                                , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
                    } else {
                        STo_searchlist_btn = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase_For_save_QTY_in_transfersearchactivity(
                                spiner_storage_location_from.getSelectedItem().toString()
                                , spiner_storage_location_from.getSelectedItem().toString()
                                , editbarcodeforsoap.getText().toString());
                    }

                    if (STo_searchlist_btn.size() == 0) {

                        if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                            databaseHelperForTransfer.Update_Sto_search_For_QTY(edit_asked_from_site_search.getText().toString(),
                                    spiner_storage_location_from.getSelectedItem().toString()
                                    , spiner_storage_location_from.getSelectedItem().toString()
                                    , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));
                        } else {
                            databaseHelperForTransfer.Update_Sto_search_For_QTY(edit_asked_from_site_search.getText().toString(),
                                    spiner_storage_location_from.getSelectedItem().toString()
                                    , spiner_storage_location_from.getSelectedItem().toString()
                                    , editbarcodeforsoap.getText().toString());
                        }


                        Double AvaliableQty = Double.parseDouble(txt_available_to_site_search.getText().toString()) -
                                Double.parseDouble(edit_asked_from_site_search.getText().toString());
                        Log.e("AvaliableQty", "" + AvaliableQty);

                        txt_available_to_site_search.setText("" + new DecimalFormat("###.##").format(AvaliableQty));
                        edit_asked_from_site_search.setText("");
                        edit_asked_from_site_search.setError(null);
                        edit_asked_from_site_search.setHint("Done");
                        editbarcodeforsoap.setText("");
                        editbarcodeforsoap.requestFocus();

                    } else {
                        double AvaliableQty = Double.parseDouble(STo_searchlist_btn.get(0).getAVAILABLE_STOCK1());
                        double lastQty;
                        if (STo_searchlist_btn.get(0).getQTY1().isEmpty()) {
                            lastQty = 0.0;
                        } else {
                            lastQty = Double.parseDouble(STo_searchlist_btn.get(0).getQTY1());
                        }
                        double currentQty = Double.parseDouble(edit_asked_from_site_search.getText().toString());
                        double SumQty = lastQty + currentQty;


                        if ((SumQty <= AvaliableQty) || (FromSite.equalsIgnoreCase("01MW")
                                || ToSite.equalsIgnoreCase("01MW"))) {

                            if (editbarcodeforsoap.getText().toString().startsWith("23")) {
                                new AlertDialog.Builder(this)
                                        .setTitle("تم أدخال الصنف من قبل بكميه " + lastQty + "سوف يتم تزويد الكميه")
                                        .setPositiveButton("موافق", (dialog, whichButton) -> {
                                            databaseHelperForTransfer.Update_Sto_search_For_QTY(String.valueOf(new DecimalFormat("###.##").format(Double.valueOf(SumQty)))
                                                    , spiner_storage_location_from.getSelectedItem().toString()
                                                    , spiner_storage_location_from.getSelectedItem().toString()
                                                    , Calculatcheckdigitforscales(editbarcodeforsoap.getText().toString().substring(0, 7) + "00000"));

                                            edit_asked_from_site_search.setText("");
                                            edit_asked_from_site_search.setHint("Done");
                                            txt_available_to_site_search.setText("" + new DecimalFormat("###.##").format(Double.valueOf(AvaliableQty - SumQty)));
                                            editbarcodeforsoap.setText("");
                                            editbarcodeforsoap.requestFocus();

                                        })
                                        .show();

                            } else {
                                new AlertDialog.Builder(this)
                                        .setTitle("تم أدخال الصنف من قبل بكميه " + lastQty + "سوف يتم تزويد الكميه")
                                        .setPositiveButton("موافق", (dialog, whichButton) -> {
                                            databaseHelperForTransfer.Update_Sto_search_For_QTY(String.valueOf(new DecimalFormat("###.##").format(Double.valueOf(SumQty)))
                                                    , spiner_storage_location_from.getSelectedItem().toString()
                                                    , spiner_storage_location_from.getSelectedItem().toString()
                                                    , editbarcodeforsoap.getText().toString());

                                            edit_asked_from_site_search.setText("");
                                            edit_asked_from_site_search.setHint("Done");
                                            txt_available_to_site_search.setText("" + new DecimalFormat("###.##").format(Double.valueOf(AvaliableQty - SumQty)));
                                            editbarcodeforsoap.setText("");
                                            editbarcodeforsoap.requestFocus();
                                        })
                                        .show();
                            }

                        } else {
                            edit_asked_from_site_search.setError("هذه الكميه أكبر من المتاح بالمخزن... المتاح" + AvaliableQty);
                        }
                    }

                } else {//TODO check qty
            edit_asked_from_site_search.setError("هذه الكميه أكبر من المتاح بالمخزن...المتاح" + txt_available_to_site_search.getText().toString());

        }
    }

    public void Go_To_Upload_Form(View view) {
        String StatusNU = databaseHelperForTransfer.select_what_has_Status_value();
        if (StatusNU.length() != 0) {
            Toast.makeText(this, "هذا الأمر تم تحويله برقم" + StatusNU, Toast.LENGTH_SHORT).show();
        } else {
            List<STo_Search> sTo_searchList_upload;
            sTo_searchList_upload = databaseHelperForTransfer.selectSto_Search_for_Qty();
            if (sTo_searchList_upload.size() > 0) {
                Intent gotoupload = new Intent(CreateOrderSearchActivity.this, CreateOrderOnSAPActivity.class);
                gotoupload.putExtra("FromSite", FromSite);
                startActivity(gotoupload);
                finish();
            } else {
                Toast.makeText(this, "كل القيم 0.0", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
