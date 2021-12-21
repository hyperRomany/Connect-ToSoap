package com.example.connecttosoapapiapp.ItemReturn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainItemReturnActivity extends AppCompatActivity {
    public StringRequest request=null;
    List<String> pur_org_list ,pur_grp_list;
    List<Item_Return_Header> headerList;
    List<Users> userdataList;
    String check_of_UserCode ,Company;

    EditText edt_vendor;
    TextView txt_user_code;
    Spinner spiner_pur_org,spiner_pur_grp;
    DatabaseHelperForItemReturn databaseHelperForItemReturn;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item_return);

        edt_vendor=findViewById(R.id.edt_vendor);
        spiner_pur_org=findViewById(R.id.spiner_pur_org);
        spiner_pur_grp=findViewById(R.id.spiner_pur_grp);
        txt_user_code=findViewById(R.id.txt_user_code);
        pur_org_list =new ArrayList<>();
        pur_grp_list = new ArrayList<>();

        headerList=new ArrayList<>();

        getlistfromsqlserver();
        databaseHelperForItemReturn=new DatabaseHelperForItemReturn(this);
        // To get user code
        databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();

        userdataList = databaseHelper.getUserData();
//        txt_user_code.setText(userdataList.get(0).getCompany1());
        Company=userdataList.get(0).getCompany1();
        check_of_UserCode=userdataList.get(0).getCompany1().toString().substring(1,3);
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();
//        txt_user_code.setText("01RT");
        if (check_of_UserCode.equalsIgnoreCase("10")) {
            txt_user_code.setText("01RT");
        }/*else if (check_of_UserCode.equalsIgnoreCase("20")) {
            txt_user_code.setText("02RT");
        }else if (check_of_UserCode.equalsIgnoreCase("30")) {
            txt_user_code.setText("03RT");
        }*/else{
            txt_user_code.setText(check_of_UserCode + "RT");
        }
        headerList = databaseHelperForItemReturn.selectReturn_Header();
        if (headerList.size() !=0){
            edt_vendor.setText(headerList.get(0).getVendor1());
     //       txt_user_code.setText(headerList.get(0).getSite1());

        }
    }

    public void getlistfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.ListfortransferFromSqlServerURL,
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
                            //Log.e("onResponse", "object"+object);

                            //JSONObject object2 = object.getJSONObject("user");
                            //String username = object.getString("status");
                            // Log.e("onResponse", "object2"+object2);


                            String p_orgsize= object.getString("p_orgsize");
                            Log.e("onResponse", p_orgsize);

                            String PGRP_PORsize = object.getString("PGRP_PORsize");
                            Log.e("onResponse", PGRP_PORsize);

                            String Sites_LOCATIONS = object.getString("Sites_LOCATIONS");
                            Log.e("onResponse", Sites_LOCATIONS);

                            String Sto_Type = object.getString("STO_TYPEsize");
                            Log.e("onResponse", Sto_Type);
//                            if (Integer.valueOf(Sto_Type) >0) {
//                                txt_sto_type.setText(object.getString("STO_TYPE0"));
//                                Log.e("STO_TYPE0", ""+object.getString("STO_TYPE0"));
//                            }


                            for (int i=0 ; i<Integer.valueOf(p_orgsize);i++){

                                String p_org_id= object.getString("p_org_id"+i);
                                Log.e("p_org_id"+i, p_org_id);
                                //if (p_org_id.contains(check_of_UserCode)) {
                                pur_org_list.add(p_org_id);
                                // }

                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainItemReturnActivity.this,
                                    android.R.layout.simple_spinner_item,pur_org_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_pur_org.setAdapter(adapter);
                            for (int i=0 ; i<Integer.valueOf(PGRP_PORsize);i++){

                                String pgrp= object.getString("pgrp"+i);
                                Log.e("pgrp"+i, pgrp);
                                // if (pgrp.contains(check_of_UserCode)) {
                                pur_grp_list.add(pgrp);
                                // }
                            }
                            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(MainItemReturnActivity.this,
                                    android.R.layout.simple_spinner_item,pur_grp_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_pur_grp.setAdapter(adapter2);
//                            for (int i=0 ; i<Integer.valueOf(Sites_LOCATIONS);i++){
//
//                                String pgrp_description= object.getString("pgrp_description"+i);
//                                Log.e("pgrp_description"+i, pgrp_description);
//                                if (pgrp_description.contains(check_of_UserCode)) {
//                                    site_list.add(pgrp_description);
//                                }
//                            }
//                            adapterForSites=new ArrayAdapter<String>(MainItemReturnActivity.this,
//                                    android.R.layout.simple_spinner_item,site_list);
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spiner_from_site.setAdapter(adapterForSites);
//                            spiner_to_site.setAdapter(adapterForSites);


//                            if (STo_headerlist.size() !=0) {
//                                // Toast.makeText(this, "local DB is not null", Toast.LENGTH_SHORT).show();
//                                FromSite = STo_headerlist.get(0).getIss_Site1().toString();
//                                ToSite = STo_headerlist.get(0).getRec_Site1().toString();
//                                Log.e("local DB is not null", "fromsite" + FromSite);
//                                Log.e("local DB  is not null", "tosite" + ToSite);
//                                FromPostion =adapterForSites.getPosition(FromSite);
//                                ToPostion=  adapterForSites.getPosition(ToSite);
//                                spiner_from_site.setSelection(FromPostion);
//                                spiner_to_site.setSelection(ToPostion);
//                                Log.e("local DB is not null", "fromsite" + FromPostion);
//                                Log.e("local DB  is not null", "tosite" + ToPostion);
//                            }
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
                // params.put("User_Name", editusername.getText().toString());
                //   params.put("Password", editpassword.getText().toString());
                //params.put("key_1","value_1");
                // params.put("key_2", "value_2");
                params.put("type","1");
                params.put("Company",Company);
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

    public void OpenNewOrder(View view) {
        if (edt_vendor.getText().toString().isEmpty()){
            edt_vendor.setError("من فضلك أدخل رقم المورد");
        }else if (spiner_pur_org.getSelectedItem() ==null){
           // edt_vendor.setError();
            Toast.makeText(this, "من فضلك أختار القسم", Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_all_items))
                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            databaseHelperForItemReturn.DeleteStoHeaderTable();
                            databaseHelperForItemReturn.DeleteStoSearchTable();

                            databaseHelperForItemReturn.insertItem_Return_Header(edt_vendor.getText().toString(),
                                    spiner_pur_org.getSelectedItem().toString(),spiner_pur_grp.getSelectedItem().toString(),
                                    txt_user_code.getText().toString(),"");

                            Intent goToScan=new Intent(MainItemReturnActivity.this,ScanItemsReturnActivity.class);
                            startActivity(goToScan);
                        }
                    })
                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    }).show();


        }
    }

    public void OpenLastOrder(View view) {
        headerList = databaseHelperForItemReturn.selectReturn_Header();
        if (headerList.size() !=0) {
            Intent goToScan = new Intent(MainItemReturnActivity.this, ScanItemsReturnActivity.class);
            startActivity(goToScan);
        }else {
            Toast.makeText(this, "لا يوجد أمر مسجل", Toast.LENGTH_SHORT).show();
        }

    }
}
