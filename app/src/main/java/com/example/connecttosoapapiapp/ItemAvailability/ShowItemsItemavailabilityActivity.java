package com.example.connecttosoapapiapp.ItemAvailability;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.ItemAvailability.Adapter.ItemAvailabilityAdapter;
import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemsSearchModul;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShowItemsItemavailabilityActivity extends AppCompatActivity {

    ItemAvailabilityAdapter itemsToEditAdapter;
    List<ItemsSearchModul> Po_Item_For_Recycly;
    RecyclerView recyclerView;
    EditText edit_describtion;
    String SearchingDESC="";


    public  StringRequest request=null;
    public  StringRequest searchReq;
    private Button btn_search_barcode;


    private Spinner departments_spinner;
    private List<String> departmentList;
    private String Company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itemavailability);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =findViewById(R.id.recycle_items_view);
        edit_describtion=findViewById(R.id.edit_describtion);
        btn_search_barcode=findViewById(R.id.btn_search_barcode);
        departments_spinner = findViewById(R.id.dep_spinner_item_availability);



        departmentList = new ArrayList<>();
        Po_Item_For_Recycly = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Users> userdataList = databaseHelper.getUserData();
        Company= userdataList.get(0).getCompany1();


        setupDepartmentSpinner();



        btn_search_barcode.setOnClickListener(view -> {
            SearchingDESC=edit_describtion.getText().toString();
            Search_BarCodeOfOfRecycleview();
        });


        CreateORUpdateRecycleView();
    }

    private void setupDepartmentSpinner() {
        RequestQueue queue = Volley.newRequestQueue(this);
        request = new StringRequest(Request.Method.POST, Constant.ListfortransferFromSqlServerURL,
                response -> {

                    String encodedstring;
                    try {
                        encodedstring = URLEncoder.encode(response,"ISO-8859-1");
                        response = URLDecoder.decode(encodedstring,"UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {

                        JSONObject object = new JSONObject(response);
                        String p_orgsize= object.getString("p_orgsize");
                        for (int i = 0; i<Integer.parseInt(p_orgsize); i++){

                            String dep= object.getString("pgrp"+i);
                            departmentList.add(dep);
                        }
                        ArrayAdapter<String> adapter=new ArrayAdapter<>(ShowItemsItemavailabilityActivity.this,
                                android.R.layout.simple_spinner_item,departmentList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        departments_spinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        Log.i("log error", errorString);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("type","1");
                params.put("Company",Company);
                Log.i("sending ", params.toString());
                Log.e("onResponser", "response"+request);

                return params;
            }

        };



        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);
    }

    public void CreateORUpdateRecycleView(){
        itemsToEditAdapter = new ItemAvailabilityAdapter(Po_Item_For_Recycly);
        recyclerView.setHasFixedSize(true);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemsToEditAdapter);

    }

    public void Search_BarCodeOfOfRecycleview() {
        if (edit_describtion.getText().toString().isEmpty()){
            edit_describtion.setError("من فضلك ادخل الباركود");
        }else {
            Po_Item_For_Recycly.clear();
            GetDetialsForBarcod();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void GetDetialsForBarcod() {
                RequestQueue queue = Volley.newRequestQueue(this);
                searchReq = new StringRequest(Request.Method.POST, Constant.GetDetialsforsearchingURL,
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
                                JSONArray objectrecords = object.getJSONArray("records");

                                if (objectrecords != null) {
                                    for (int i=0;i<objectrecords.length();i++){
                                        JSONObject objectitem = objectrecords.getJSONObject(i);
                                        Po_Item_For_Recycly.add(new ItemsSearchModul(objectitem.getString("barcode"),
                                                objectitem.getString("a_name"),objectitem.getString("itemean")));
                                    }
                                }
                                Log.e("onResponse length", String.valueOf(objectrecords.length()));
                                itemsToEditAdapter.notifyDataSetChanged();


                            } catch (JSONException e) {
                                edit_describtion.setEnabled(true);
                                edit_describtion.setError("خطأ بالأتصال بخادم POS");
                                edit_describtion.setText("");
                                edit_describtion.requestFocus();
                                Toast.makeText(getBaseContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        },

                        error -> {
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                edit_describtion.setEnabled(true);
                                edit_describtion.setError("لم يتم أضافة الباركود .Net. حاول مره اخرى");
                                edit_describtion.setText("");
                                edit_describtion.requestFocus();
                            }
                        })

                {

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        return super.parseNetworkError(volleyError);
                    }


                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("BarcodeDescription", SearchingDESC);
                        params.put("dep_description", departments_spinner.getSelectedItem().toString());
                        return params;
                    }

                };


                searchReq.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1.0f));
                queue.add(searchReq);
    }
}
