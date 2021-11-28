package com.example.connecttosoapapiapp.ItemAvailability;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.ItemAvailability.Adapter.ItemAvailabilityAdapter;
import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemsSearchModul;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowItemsItemavailabilityActivity extends AppCompatActivity {
    ItemAvailabilityAdapter itemsToEditAdapter;
    List<ItemsSearchModul> Po_Item_For_Recycly;
    DatabaseHelperForCycleCount databaseHelperForCycleCount;
    RecyclerView recyclerView;
    EditText edit_describtion;
    String SearchingDESC="";
    public StringRequest request=null;
    Button btn_search_barcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itemavailability);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =findViewById(R.id.recycle_items_view);
        edit_describtion=findViewById(R.id.edit_describtion);
        btn_search_barcode=findViewById(R.id.btn_search_barcode);
        btn_search_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchingDESC=edit_describtion.getText().toString();
                Search_BarCodeOfOfRecycleview();
            }
        });
        Po_Item_For_Recycly = new ArrayList<>();

        CreateORUpdateRecycleView();


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
                request = new StringRequest(Request.Method.POST, Constant.GetDetialsforsearchingURL,
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
                                Log.e("onResponse", "response" + response);
                                Log.e("onResponse", "request" + request);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    JSONArray objectrecords = object.getJSONArray("records");
                                    //JSONObject object = objectrecords.getJSONObject()
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
//                                    progressBar.setVisibility(View.GONE);
                                    //btn_search_barcode.setEnabled(true);
                                    edit_describtion.setEnabled(true);
                                    edit_describtion.setError("خطأ بالأتصال بخادم POS");
                                    edit_describtion.setText("");
                                    edit_describtion.requestFocus();
                                    Toast.makeText(getBaseContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
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
                                    edit_describtion.setEnabled(true);
                                    //btn_search_barcode.setEnabled(true);
                                    edit_describtion.setError("لم يتم أضافة الباركود .Net. حاول مره اخرى");
                                    edit_describtion.setText("");
                                    edit_describtion.requestFocus();
                                    Log.i("log error", errorString);
                                }
                            }
                        }
                ) {
                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        Log.i("log error no respon", "se6");
                        Log.i("log error no respon", ""+volleyError);
                        //volleyErrorPublic=volleyError;
                        return super.parseNetworkError(volleyError);
                    }

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();

                        Log.e("BarcodeDescription",SearchingDESC);
                        params.put("BarcodeDescription", SearchingDESC);
                        //params.put("Branch", Company.substring(1,3));

                        return params;
                    }

                };

                request.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1.0f));
                queue.add(request);


    }
}
