package com.example.connecttosoapapiapp.GIModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.GIModule.Helper.GIDataBaseHelper;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
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

import androidx.appcompat.app.AppCompatActivity;

public class MainFormOfGIActivity extends AppCompatActivity {
    public StringRequest request=null;
Spinner spiner_department,spiner_site;
    TextView txt_user_code,txt_gl,txt_cost_center;
    ArrayList<String> DES_list ,GL_list,CS_list, Sites_list;
    GIDataBaseHelper giDataBaseHelper;
    Button btn_open_new_order,btn_open_last_order;
    String check_of_UserCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form_of_gi);
        spiner_department=findViewById(R.id.spiner_department);
        spiner_site=findViewById(R.id.spiner_site);
        txt_gl=findViewById(R.id.txt_gl);
        txt_cost_center=findViewById(R.id.txt_cost_center);
        txt_user_code=findViewById(R.id.txt_user_code);
        btn_open_new_order=findViewById(R.id.btn_open_new_order);
        btn_open_last_order=findViewById(R.id.btn_open_last_order);

        giDataBaseHelper=new GIDataBaseHelper(this);
        spiner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("zzzzzz",""+position);
                Log.e("zzzz",""+GL_list.size());
                Log.e("zzzz",""+CS_list.size());
                txt_gl.setText(GL_list.get(position));
                txt_cost_center.setText(CS_list.get(position));

                btn_open_new_order.setEnabled(true);
                btn_open_last_order.setEnabled(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        if (savedInstanceState != null){
//            Log.e("zzzzzzzNotNull","notnull");

//            DES_list=new ArrayList<>();
//            GL_list=new ArrayList<>();
//            CS_list=new ArrayList<>();
//
//            DES_list =savedInstanceState.getStringArrayList("DES_list");
//            GL_list=savedInstanceState.getStringArrayList("GL_list");
//            CS_list=savedInstanceState.getStringArrayList("CS_list");
//            Sites_list=savedInstanceState.getStringArrayList("Sites_list");

//            txt_user_code.setText(savedInstanceState.getString("mmmmm"));

//            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainFormOfGIActivity.this,
//                    android.R.layout.simple_spinner_item,DES_list);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spiner_department.setAdapter(adapter);
//
//            ArrayAdapter<String> adaptersites=new ArrayAdapter<String>(MainFormOfGIActivity.this,
//                    android.R.layout.simple_spinner_item,Sites_list);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spiner_site.setAdapter(adaptersites);
//
//        }else {
//            Log.e("zzzzzzzNull","null");
        getlistfromsqlserver();

        // TODO Auto-generated catch block
        List<Users> userdataList;
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();

        userdataList = databaseHelper.getUserData();
        txt_user_code.setText(userdataList.get(0).getCompany1());

        check_of_UserCode=txt_user_code.getText().toString().substring(1,3);
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();

//        }
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//
//        outState.putString("mmmmm",txt_cost_center.getText().toString());
////        outState.putStringArrayList("DES_list",DES_list);
////        outState.putStringArrayList("GL_list",GL_list);
////        outState.putStringArrayList("CS_list",CS_list);
////        outState.putStringArrayList("Sites_list",Sites_list);
//        Log.e("zzzzzzzsavefunction","fun");
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//
//
//        Log.e("zzzzzzzsavefunction","Restorefun");
//
////        DES_list=new ArrayList<>();
////        GL_list=new ArrayList<>();
////        CS_list=new ArrayList<>();
//        savedInstanceState.getString("mmmmm");
////        DES_list =savedInstanceState.getStringArrayList("DES_list");
////        GL_list=savedInstanceState.getStringArrayList("GL_list");
////        CS_list=savedInstanceState.getStringArrayList("CS_list");
////        Sites_list=savedInstanceState.getStringArrayList("Sites_list");
//
//
////        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainFormOfGIActivity.this,
////                android.R.layout.simple_spinner_item,DES_list);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spiner_department.setAdapter(adapter);
////
////        ArrayAdapter<String> adaptersites=new ArrayAdapter<String>(MainFormOfGIActivity.this,
////                android.R.layout.simple_spinner_item,Sites_list);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spiner_site.setAdapter(adaptersites);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    public void OpenLastOrder(View view) {
        List<GIModule> gilist  = giDataBaseHelper.selectGIModule();
        if (gilist.size() !=0) {
            Intent GoToScan = new Intent(MainFormOfGIActivity.this, ScanGIActivity.class);
            GoToScan.putExtra("GL", txt_gl.getText().toString());
            GoToScan.putExtra("CostCenter", txt_cost_center.getText().toString());
            GoToScan.putExtra("Site", spiner_site.getSelectedItem().toString());

            startActivity(GoToScan);
        }else {
            Toast.makeText(this, "لايوجد بيانات سابقه", Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenNewOrder(View view) {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all_items))
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        giDataBaseHelper.DeleteStoSearchTable();
                        Intent GoToScan=new Intent(MainFormOfGIActivity.this,ScanGIActivity.class);
                        GoToScan.putExtra("GL",txt_gl.getText().toString());
                        GoToScan.putExtra("CostCenter",txt_cost_center.getText().toString());
                        GoToScan.putExtra("Site",spiner_site.getSelectedItem().toString());
                        startActivity(GoToScan);
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).show();

    }

    public void getlistfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.ListforGIFromSqlServerURL,
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


                            String Departmentsize= object.getString("Departmentsize");
                            Log.e("onResponse", Departmentsize);

                             DES_list=new ArrayList<>();
                            GL_list=new ArrayList<>();
                            CS_list=new ArrayList<>();
                            for (int i=0 ; i<Integer.valueOf(Departmentsize);i++){

                                String DES= object.getString("sec_desc"+i);
                                String GL= object.getString("GL"+i);
                                String CS= object.getString("CS"+i);
                                DES_list.add(DES);
                                GL_list.add(GL);
                                CS_list.add(CS);

                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainFormOfGIActivity.this,
                                    android.R.layout.simple_spinner_item,DES_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_department.setAdapter(adapter);

                            String Sitessize= object.getString("Sites_LOCATIONSList");
                            Log.e("onResponse", Departmentsize);


                            Sites_list=new ArrayList<>();

                            for (int i=0 ; i<Integer.valueOf(Sitessize);i++){

                                String sites= object.getString("Sites_LOCATIONS"+i);

                                Sites_list.add(sites);

                            }
                            Log.e("onResponseSitessize", ""+Sitessize);
                            ArrayAdapter<String> adaptersites=new ArrayAdapter<String>(MainFormOfGIActivity.this,
                                    android.R.layout.simple_spinner_item,Sites_list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spiner_site.setAdapter(adaptersites);

                            List<GIModule> gilist  = giDataBaseHelper.selectGIModule();
                            if (gilist.size() !=0){

                                spiner_site.setSelection(Sites_list.indexOf(gilist.get(0).getREC_SITE1()));
                                Log.e("zzzz",""+Sites_list.indexOf(gilist.get(0).getREC_SITE1()));
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
                // params.put("User_Name", editusername.getText().toString());
                // TODO Auto-generated catch block
          //      params.put("CompanyCode", txt_user_code.getText().toString());
                Log.i("zzzsendinguser_code ", txt_user_code.getText().toString());
//                if (txt_user_code.getText().toString().equalsIgnoreCase("H010")) {
//                    params.put("CompanyCode", "H010");
//                }else if (txt_user_code.getText().toString().equalsIgnoreCase("H020")){
//                    params.put("CompanyCode", "H020");
//                }else if (txt_user_code.getText().toString().equalsIgnoreCase("H030")){
//                    params.put("CompanyCode", "H030");
//                }else if (txt_user_code.getText().toString().equalsIgnoreCase("H040")){
                    params.put("CompanyCode", txt_user_code.getText().toString() );
//                }
                //params.put("key_1","value_1");
                // params.put("key_2", "value_2");

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

}
