package com.example.connecttosoapapiapp.TransfereModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.connecttosoapapiapp.MainActivity;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;

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

import androidx.appcompat.app.AppCompatActivity;

public class FormTransferActivity extends AppCompatActivity {

    public StringRequest request=null;
Spinner spiner_pur_org,spiner_pur_grp,spiner_from_site,spiner_to_site;
TextView txt_user_code,txt_sto_type;
List<String>  pur_org_list ,pur_grp_list,site_list;
DatabaseHelper databaseHelper;
List<Users> userdataList;
DatabaseHelperForTransfer databaseHelperForTransfer;

    String FromSite,ToSite;
    List<STO_Header> STo_headerlist;
    ArrayAdapter<String> adapterForSites;
    int FromPostion,ToPostion;
    String StatusNU, check_of_UserCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_transfer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelperForTransfer=new DatabaseHelperForTransfer(this);

        txt_sto_type=findViewById(R.id.txt_sto_type);
        spiner_pur_org=findViewById(R.id.spiner_pur_org);
        spiner_pur_grp=findViewById(R.id.spiner_pur_grp);
        spiner_from_site=findViewById(R.id.spiner_from_site);
        spiner_to_site=findViewById(R.id.spiner_to_site);
        txt_user_code=findViewById(R.id.txt_user_code);
        pur_org_list =new ArrayList<>();
        pur_grp_list = new ArrayList<>();
        site_list = new ArrayList<>();

        databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();

        userdataList = databaseHelper.getUserData();
        txt_user_code.setText(userdataList.get(0).getCompany1());
        check_of_UserCode=txt_user_code.getText().toString().substring(1,3);
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();


        getlistfromsqlserver();

        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();

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
                                    if (Integer.valueOf(Sto_Type) >0) {
                                        txt_sto_type.setText(object.getString("STO_TYPE0"));
                                        Log.e("STO_TYPE0", ""+object.getString("STO_TYPE0"));
                                    }


                                for (int i=0 ; i<Integer.valueOf(p_orgsize);i++){

                                    String p_org_id= object.getString("p_org_id"+i);
                                    Log.e("p_org_id"+i, p_org_id);
                                    //if (p_org_id.contains(check_of_UserCode)) {
                                        pur_org_list.add(p_org_id);
                                   // }

                                }
                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(FormTransferActivity.this,
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
                                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(FormTransferActivity.this,
                                        android.R.layout.simple_spinner_item,pur_grp_list);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiner_pur_grp.setAdapter(adapter2);
                                for (int i=0 ; i<Integer.valueOf(Sites_LOCATIONS);i++){

                                    String pgrp_description= object.getString("pgrp_description"+i);
                                    Log.e("pgrp_description"+i, pgrp_description);
                                    if (pgrp_description.contains(check_of_UserCode)) {
                                        site_list.add(pgrp_description);
                                    }
                                }
                                adapterForSites=new ArrayAdapter<String>(FormTransferActivity.this,
                                        android.R.layout.simple_spinner_item,site_list);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiner_from_site.setAdapter(adapterForSites);
                                spiner_to_site.setAdapter(adapterForSites);


                                if (STo_headerlist.size() !=0) {
                                   // Toast.makeText(this, "local DB is not null", Toast.LENGTH_SHORT).show();
                                    FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                                    ToSite = STo_headerlist.get(0).getRec_Site1().toString();
                                    Log.e("local DB is not null", "fromsite" + FromSite);
                                    Log.e("local DB  is not null", "tosite" + ToSite);
                                     FromPostion =adapterForSites.getPosition(FromSite);
                                     ToPostion=  adapterForSites.getPosition(ToSite);
                                    spiner_from_site.setSelection(FromPostion);
                                    spiner_to_site.setSelection(ToPostion);
                                    Log.e("local DB is not null", "fromsite" + FromPostion);
                                    Log.e("local DB  is not null", "tosite" + ToPostion);
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
                 //   params.put("Password", editpassword.getText().toString());
                    //params.put("key_1","value_1");
                    // params.put("key_2", "value_2");
                    params.put("type","STO1");
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
        Intent Go_Back= new Intent(FormTransferActivity.this,MainActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void OpenLastOrder(View view) {
        if (adapterForSites == null){
            Toast.makeText(this, "لم يتم تحميل البيانات من السيرفر .. تاكد من اتصالك بالسيرفر", Toast.LENGTH_SHORT).show();
        }else {
            STo_headerlist = databaseHelperForTransfer.selectSto_Header();

            Log.e("local DB is not null", "listsize" + STo_headerlist.size());


            if (STo_headerlist.size() != 0) {
                // Toast.makeText(this, "local DB is not null", Toast.LENGTH_SHORT).show();
                FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                ToSite = STo_headerlist.get(0).getRec_Site1().toString();

                FromPostion = adapterForSites.getPosition(FromSite);
                ToPostion = adapterForSites.getPosition(ToSite);
                //spiner_from_site.setSelection(FromPostion);
                // spiner_to_site.setSelection(ToPostion);

//            Log.e("local DB is not null", "fromL1site" + FromPostion);
//            Log.e("local DB  is not null", "toL1site" + ToPostion);

                int FromForNewPostion = spiner_from_site.getSelectedItemPosition();
                int ToForNewPostion = spiner_to_site.getSelectedItemPosition();

                Log.e("local DB is not null", "fromNsite" + spiner_from_site.getSelectedItem().toString());
                Log.e("local DB  is not null", "toNsite" + ToForNewPostion);

                if (FromPostion == FromForNewPostion && ToPostion == ToForNewPostion) {
                    StatusNU = databaseHelperForTransfer.select_what_has_Status_value();
                    if (StatusNU.length() != 0) {
                        Toast.makeText(this, "هذا الأمر تم تحويله برقم" + StatusNU, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("local DB  is not null", "==" + ToForNewPostion);

                        Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                        GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                        GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                        GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                        startActivity(GoToSearch);
                    }
                } else {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textfordeletelastsearch))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String Date = sdf.format(new Date());
                                    databaseHelperForTransfer.DeleteStoHeaderTable();
                                    databaseHelperForTransfer.DeleteStoSearchTable();

                                    databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                                            userdataList.get(0).getCompany1(),
                                            spiner_pur_org.getSelectedItem().toString(), spiner_pur_grp.getSelectedItem().toString(),
                                            spiner_to_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date,"anyType{}");
                                    Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                                    GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                    GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                    GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                                    startActivity(GoToSearch);

                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.textfornewsearch))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String Date = sdf.format(new Date());
                                databaseHelperForTransfer.DeleteStoHeaderTable();
                                databaseHelperForTransfer.DeleteStoSearchTable();

                                databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                                        userdataList.get(0).getCompany1(),
                                        spiner_pur_org.getSelectedItem().toString(), spiner_pur_grp.getSelectedItem().toString(),
                                        spiner_to_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date,"anyType{}");
                                Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                                GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                                startActivity(GoToSearch);

                            }
                        })
                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }

    public void OpenNewOrder(View view) {
        if (adapterForSites == null) {
            Toast.makeText(this, "لم يتم تحميل البيانات من السيرفر .. تاكد من اتصالك بالسيرفر", Toast.LENGTH_SHORT).show();
        } else {
            STo_headerlist = databaseHelperForTransfer.selectSto_Header();

            Log.e("local DB is not null", "listsize" + STo_headerlist.size());

            if (STo_headerlist.size() != 0) {
                // Toast.makeText(this, "local DB is not null", Toast.LENGTH_SHORT).show();
                FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                ToSite = STo_headerlist.get(0).getRec_Site1().toString();

                FromPostion = adapterForSites.getPosition(FromSite);
                ToPostion = adapterForSites.getPosition(ToSite);
                //spiner_from_site.setSelection(FromPostion);
                //spiner_to_site.setSelection(ToPostion);

//            Log.e("local DB is not null", "fromL1site" + FromPostion);
//            Log.e("local DB  is not null", "toL1site" + ToPostion);

                int FromForNewPostion = spiner_from_site.getSelectedItemPosition();
                int ToForNewPostion = spiner_to_site.getSelectedItemPosition();
                //Log.e("local DB is not null", "fromNsite" + FromForNewPostion);
                //        Log.e("local DB  is not null", "toNsite" + ToForNewPostion);

                if (FromPostion == FromForNewPostion && ToPostion == ToForNewPostion) {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textforpurchaseorder))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String Date = sdf.format(new Date());

                                    databaseHelperForTransfer.DeleteStoHeaderTable();
                                    databaseHelperForTransfer.DeleteStoSearchTable();

                                    databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                                            userdataList.get(0).getCompany1(),
                                            spiner_pur_org.getSelectedItem().toString(), spiner_pur_grp.getSelectedItem().toString(),
                                            spiner_to_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date,"anyType{}");
                                    Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                                    GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                    GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                    GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                                    startActivity(GoToSearch);

                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.textforpurchaseorder))
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String Date = sdf.format(new Date());
                                    databaseHelperForTransfer.DeleteStoHeaderTable();
                                    databaseHelperForTransfer.DeleteStoSearchTable();

                                    databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                                            userdataList.get(0).getCompany1(),
                                            spiner_pur_org.getSelectedItem().toString(), spiner_pur_grp.getSelectedItem().toString(),
                                            spiner_to_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date,"anyType{}");
                                    Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                                    GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                    GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                    GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                                    startActivity(GoToSearch);

                                }
                            })
                            .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            }).show();

                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String Date = sdf.format(new Date());
                databaseHelperForTransfer.DeleteStoHeaderTable();
                databaseHelperForTransfer.DeleteStoSearchTable();

                databaseHelperForTransfer.insertSto_header(Date, spiner_from_site.getSelectedItem().toString(),
                        userdataList.get(0).getCompany1(),
                        spiner_pur_org.getSelectedItem().toString(), spiner_pur_grp.getSelectedItem().toString(),
                        spiner_to_site.getSelectedItem().toString(), txt_sto_type.getText().toString(), Date,"anyType{}");
                Intent GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                startActivity(GoToSearch);
            }

//        Log.e("local DB is not null", "fromLsite" + FromPostion);
//        Log.e("local DB  is not null", "toLsite" + ToPostion);


        }
    }
}
