package com.example.connecttosoapapiapp.Promotion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.Promotion.Adapter.DeatailsItemOfPromItemAdapter;
import com.example.connecttosoapapiapp.Promotion.Helper.DatabaseHelperForProotion;
import com.example.connecttosoapapiapp.Promotion.Helper.ItemClickSupport;
import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

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

public class PromItemDetailsActivity extends AppCompatActivity {
String discount_id;
CheckBox check_modify_for_all;
DatabaseHelperForProotion databaseHelperForProotion;
TextView txt_discoun_no,txt_status,txt_discoun_type,txt_discountcount,txt_superviser_name;
EditText edt_superviser_code,edit_startdate,edit_enddate;
    RecyclerView recyclerView;
    List<Prom_item_Module> prom_item_moduleList ;
    List<Prom_item_Module> Superviser_name_IDList;
    DeatailsItemOfPromItemAdapter deatailsItemOfPromItemAdapter;
    public StringRequest request=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prom_item_details);
        recyclerView=findViewById(R.id.recycle_items_for_detail_prom);
        txt_discoun_no=findViewById(R.id.txt_discoun_no);
        txt_status=findViewById(R.id.txt_status);
        edit_startdate=findViewById(R.id.edit_startdate);
        edit_enddate=findViewById(R.id.edit_enddate);
        check_modify_for_all=findViewById(R.id.check_modify_for_all);
        check_modify_for_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked ==true){
                    GetUsernamefromsqlserver();
                }
            }
        });
        txt_discoun_type=findViewById(R.id.txt_discoun_type);

        txt_discountcount=findViewById(R.id.txt_discountcount);
        txt_superviser_name=findViewById(R.id.txt_superviser_name);

        edt_superviser_code=findViewById(R.id.edt_superviser_code);
        databaseHelperForProotion=new DatabaseHelperForProotion(this);

        Superviser_name_IDList=new ArrayList<>();
        Superviser_name_IDList = databaseHelperForProotion.selectSupervisernameandID();
        Log.e("zzzname_ID",""+Superviser_name_IDList.size());
        if (Superviser_name_IDList.size() >0){
            txt_superviser_name.setText(Superviser_name_IDList.get(0).getSupervisor_name1());
            edt_superviser_code.setText(Superviser_name_IDList.get(0).getSupervisor_id1());
        }else {
            edt_superviser_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_GO
                            || actionId == EditorInfo.IME_ACTION_NEXT
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent == null
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                            || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                            || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyEvent.isShiftPressed()) {

                        //execute our method for searching
                        GetUsernamefromsqlserver();

                    }

                    return false;
                }
            });
        }
        if (getIntent().getExtras() !=null){
            discount_id=getIntent().getExtras().getString("discount_id");
            Log.e("zzzdiscount_id",""+discount_id);
        }


         prom_item_moduleList=new ArrayList<>();
        prom_item_moduleList = databaseHelperForProotion.selectPromItems(discount_id);
        Log.e("zzzprom_item_moduleList",""+prom_item_moduleList.size());


        if (prom_item_moduleList.size()>0) {
            txt_discoun_no.setText(prom_item_moduleList.get(0).getDiscountno1());
            txt_status.setText(prom_item_moduleList.get(0).getStatus1());
            txt_discoun_type.setText(prom_item_moduleList.get(0).getProm_desc1());
            txt_discountcount.setText(prom_item_moduleList.get(0).getReturn_type1());
            edit_startdate.setText(prom_item_moduleList.get(0).getDate_from1());
            edit_enddate.setText(prom_item_moduleList.get(0).getDate_to1());
        }
        CreateORUpdateRecycleView();
    }

    public void CreateORUpdateRecycleView() {


        deatailsItemOfPromItemAdapter = new DeatailsItemOfPromItemAdapter(prom_item_moduleList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(deatailsItemOfPromItemAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView DeatailsItemOfPromItemAdapter, int position, View v) {
                List<Prom_item_Module> Po_Item_List = deatailsItemOfPromItemAdapter.ReturnListOfItems();

                if (edt_superviser_code.getText().toString().isEmpty()){
                    edt_superviser_code.setError("أدخل كود المستخدم");
                    edt_superviser_code.requestFocus();
                }else if (txt_superviser_name.getText().toString().isEmpty()){
                   // Toast.makeText(PromItemDetailsActivity.this, "أدخل كود المستخدم", Toast.LENGTH_SHORT).show();
                    GetUsernamefromsqlserver();

                }else {
                    Intent gotodeatails = new Intent(PromItemDetailsActivity.this, ReviewActivity.class);
                    gotodeatails.putExtra("Barcode", Po_Item_List.get(position).getBarcode1());
                    gotodeatails.putExtra("modify_for_all", check_modify_for_all.isChecked());

                    gotodeatails.putExtra("Superviser_id",edt_superviser_code.getText().toString());
                    gotodeatails.putExtra("Superviser_username", txt_superviser_name.getText().toString());

                    if (Po_Item_List.get(position).getUpdateOrInsert() == true){
                        gotodeatails.putExtra("UpdateOrInsert", true);
                    }else {
                        gotodeatails.putExtra("UpdateOrInsert", false);
                    }


                    startActivity(gotodeatails);
                    finish();
                }
            }
        });


    }

    public void GetUsernamefromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.GetUserNameFromSqlServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String encodedstring = null;
                        try {
                            encodedstring = URLEncoder.encode(response,"ISO-8859-1");
                            response = URLDecoder.decode(encodedstring,"UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("onResponser", "response"+response);
                        //  Log.e("onResponse", "response"+Uri.encode(response, "utf-8").toString());
                        Log.e("onResponse", "request"+request);
                        try {

                            JSONObject object = new JSONObject(response);


                            String status= object.getString("status");
                            String message= object.getString("message");



                            if (status.equalsIgnoreCase("0")){
                               // txt_superviser_name.setText(getString(R.string.user_not_found));

                                new AlertDialog.Builder(PromItemDetailsActivity.this)
                                        .setTitle(getString(R.string.user_not_found))
                                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.cancel();
                                            }
                                        })
//                                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                                dialog.cancel();
//                                            }
//                                        })
                                        .show();
                            }else if (status.equalsIgnoreCase("1")) {
                                String LNAMR= object.getString("LNAMR");

                                txt_superviser_name.setText(LNAMR);

                            }


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
                            Log.i("log error", errorString);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("User_Code",edt_superviser_code.getText().toString());

                Log.i("sending ", params.toString());
                Log.e("onResponser", "response"+request);

                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

    @Override
    public void onBackPressed() {
        Intent gotodeatails = new Intent(PromItemDetailsActivity.this, ShowItemsPromotionActivity.class);
        startActivity(gotodeatails);
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    public void Back(View view) {
//        Intent gotodeatails = new Intent(ReviewActivity.this, PromItemDetailsActivity.class);
//        gotodeatails.putExtra("discount_id", txt_discoun_no.getText().toString());
//        startActivity(gotodeatails);
//    }
}
