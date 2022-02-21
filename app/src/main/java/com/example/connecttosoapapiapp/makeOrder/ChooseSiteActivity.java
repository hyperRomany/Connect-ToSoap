package com.example.connecttosoapapiapp.makeOrder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.MainActivity;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.Companies;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class ChooseSiteActivity extends AppCompatActivity {

    private Spinner spiner_from_site;
    private Button createOrder;
    private StringRequest request = null;
    private List<STO_Header> STo_headerlist;
    private ArrayAdapter<String> adapterForSites;
    private int FromPostion;
    private String FromSite;
    private List<String> site_list;
    private DatabaseHelperForTransfer databaseHelperForTransfer;
    private StringBuilder company;
    private List<Users> userdataList;
    private ArrayList<Companies> arrayitems;
    private TextView txt_sto_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_site);

        txt_sto_type = findViewById(R.id.choose_site_create_order_sto_type);
        TextView txt_user_code = findViewById(R.id.choose_site_create_order_user_code);
        spiner_from_site = findViewById(R.id.choose_site_from_spinner);

        databaseHelperForTransfer = new DatabaseHelperForTransfer(this);
        DatabaseHelper databaseHelper;
        site_list = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);
        userdataList = new ArrayList<>();

        userdataList = databaseHelper.getUserData();
        txt_user_code.setText(userdataList.get(0).getCompany1());
        String company1 = userdataList.get(0).getCompany1();
        String check_of_UserCode = txt_user_code.getText().toString().substring(2, 3);
        Toast.makeText(this, "" + check_of_UserCode, Toast.LENGTH_SHORT).show();


        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
        arrayitems = new ArrayList<Companies>();

        getSupportActionBar().setTitle("Create Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getlistcompanies();
    }


    public void getlistfromsqlserver() {
        RequestQueue queue = Volley.newRequestQueue(this);
        request = new StringRequest(Request.Method.POST, Constant.ListfortransferFromSqlServerURL2,
                response -> {

                    String encodedstring;
                    try {
                        encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                        response = URLDecoder.decode(encodedstring, "UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    try {

                        JSONObject object = new JSONObject(response);
                        String Sites_LOCATIONS = object.getString("Sites_LOCATIONS");
                        String Sto_Type = object.getString("STO_TYPEsize");

                        if (Integer.parseInt(Sto_Type) > 0) {
                            txt_sto_type.setText(object.getString("STO_TYPE0"));
                        }

                        for (int i = 0; i < Integer.parseInt(Sites_LOCATIONS); i++) {
                            String pgrp_description = object.getString("pgrp_description" + i);
                            site_list.add(pgrp_description);
                        }

                        adapterForSites = new ArrayAdapter<String>(ChooseSiteActivity.this,
                                android.R.layout.simple_spinner_item, site_list);
                        spiner_from_site.setAdapter(adapterForSites);


                        if (STo_headerlist.size() != 0) {
                            FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                            FromPostion = adapterForSites.getPosition(FromSite);
                            spiner_from_site.setSelection(FromPostion);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                    NetworkResponse response = error.networkResponse;
                    String errorMsg = "";
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                String name = arrayitems.get(0).getCompany();
                company = new StringBuilder("'" + name + "'");

                for (int i = 0; i < arrayitems.size(); i++) {
                    if (i != 0){
                        String z = String.valueOf(arrayitems.get(i).getCompany());
                        company.append("," + "'" + z + "'");
                    }
                }

                params.put("type", "STO1");
                params.put("Company", company.toString());

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        queue.add(request);
    }


    public void getlistcompanies() {

        RequestQueue queue = Volley.newRequestQueue(this);
        request = new StringRequest(Request.Method.POST, Constant.Listforcompanies,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String encodedstring = null;
                        try {
                            encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                            response = URLDecoder.decode(encodedstring, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        try {

                            JSONObject object = new JSONObject(response);

                            JSONArray jarray = object.getJSONArray("Modules");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object2 = jarray.getJSONObject(i);
                                Companies item = new Companies();
                                try {
                                    item.setCompany(object2.getString("Company"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                arrayitems.add(item);

                            }
                            getlistfromsqlserver();

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
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("User_Name", userdataList.get(0).getUser_Name1());

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);

    }

    public void OpenNewOrder(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Date = sdf.format(new Date());

        databaseHelperForTransfer.DeleteStoHeaderTable();
        databaseHelperForTransfer.DeleteStoSearchTable();

        databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                userdataList.get(0).getCompany1(),
                spiner_from_site.getSelectedItem().toString(), spiner_from_site.getSelectedItem().toString(),
                spiner_from_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date, "anyType{}");


        Intent intentSearch = new Intent(ChooseSiteActivity.this, CreateOrderSearchActivity.class);
        intentSearch.putExtra("Department", spiner_from_site.getSelectedItem().toString());
        intentSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
        intentSearch.putExtra("ToSite", spiner_from_site.getSelectedItem().toString());
        startActivity(intentSearch);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intentBack= new Intent(ChooseSiteActivity.this, MainActivity.class);
        startActivity(intentBack);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}