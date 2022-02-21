package com.example.connecttosoapapiapp.TransfereModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.connecttosoapapiapp.makeOrder.CreateOrderSearchActivity;

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

public class FormTransferActivity extends AppCompatActivity {

    public StringRequest request = null;
    private Spinner spiner_pur_org, spiner_pur_grp, spiner_from_site, spiner_to_site;
    private TextView txt_user_code, txt_sto_type;
    private List<String> pur_org_list, pur_grp_list, site_list;
    private DatabaseHelper databaseHelper;
    private List<Users> userdataList;
    private ArrayList<Companies> arrayitems;
    private StringBuilder company;
    private DatabaseHelperForTransfer databaseHelperForTransfer;

    private String FromSite, ToSite, sto_type;
    private List<STO_Header> STo_headerlist;
    private ArrayAdapter<String> adapterForSites;
    private int FromPostion, ToPostion;
    private String StatusNU, check_of_UserCode, Company, textComp;
    private boolean makeOrderBool = false;
    private LinearLayout toSiteLinear;
    private Button lastOp_btn,newOp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_transfer);


        initView();

        getlistcompanies();

        makeOrderBool = getIntent().getExtras().getBoolean("MakeOrder");

        if (makeOrderBool)
        {
            toSiteLinear.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Create Order");
            newOp_btn.setText("Create Order");
            lastOp_btn.setVisibility(View.GONE);
        }

    }


    private void initView()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelperForTransfer=new DatabaseHelperForTransfer(this);

        newOp_btn = findViewById(R.id.btn_open_new_order);
        lastOp_btn = findViewById(R.id.btn_open_last_order);
        toSiteLinear = findViewById(R.id.to_site_linear);
        txt_sto_type = findViewById(R.id.txt_sto_type);
        spiner_pur_org = findViewById(R.id.spiner_pur_org);
        spiner_pur_grp = findViewById(R.id.spiner_pur_grp);
        spiner_from_site = findViewById(R.id.spiner_from_site);
        spiner_to_site = findViewById(R.id.spiner_to_site);
        txt_user_code = findViewById(R.id.txt_user_code);
        pur_org_list =new ArrayList<>();
        pur_grp_list = new ArrayList<>();
        site_list = new ArrayList<>();
        arrayitems = new ArrayList<Companies>();
        databaseHelper = new DatabaseHelper(this);
        userdataList = new ArrayList<>();
        userdataList = databaseHelper.getUserData();
        txt_user_code.setText(userdataList.get(0).getCompany1());
        Company=userdataList.get(0).getCompany1();
        check_of_UserCode=txt_user_code.getText().toString().substring(2,3);
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();

        STo_headerlist = new ArrayList<>();
        STo_headerlist = databaseHelperForTransfer.selectSto_Header();
    }

    public void getlistfromsqlserver(){
            RequestQueue queue = Volley.newRequestQueue(this);
            request = new StringRequest(Request.Method.POST, Constant.ListfortransferFromSqlServerURL2,
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

                            try {

                                JSONObject object = new JSONObject(response);
                                String p_orgsize= object.getString("p_orgsize");
                                String PGRP_PORsize = object.getString("PGRP_PORsize");
                                String Sites_LOCATIONS = object.getString("Sites_LOCATIONS");
                                String Sto_Type = object.getString("STO_TYPEsize");

                                if (Integer.valueOf(Sto_Type) >0) {
                                        txt_sto_type.setText(object.getString("STO_TYPE0"));
                                        sto_type = object.getString("STO_TYPE0").toString();
                                    }

                                for (int i=0 ; i<Integer.valueOf(p_orgsize);i++){
                                    String p_org_id= object.getString("p_org_id"+i);
                                        pur_org_list.add(p_org_id);
                                }

                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(FormTransferActivity.this,
                                        android.R.layout.simple_spinner_item,pur_org_list);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiner_pur_org.setAdapter(adapter);

                                for (int i=0 ; i<Integer.valueOf(PGRP_PORsize);i++){

                                    String pgrp= object.getString("pgrp"+i);
                                        pur_grp_list.add(pgrp);

                                }

                                ArrayAdapter<String> adapter2=new ArrayAdapter<String>(FormTransferActivity.this,
                                        android.R.layout.simple_spinner_item,pur_grp_list);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiner_pur_grp.setAdapter(adapter2);

                                for (int i=0 ; i<Integer.valueOf(Sites_LOCATIONS);i++){

                                    String pgrp_description= object.getString("pgrp_description"+i);
                                    site_list.add(pgrp_description);
                                }

                                adapterForSites=new ArrayAdapter<String>(FormTransferActivity.this,
                                        android.R.layout.simple_spinner_item,site_list);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiner_from_site.setAdapter(adapterForSites);
                                spiner_to_site.setAdapter(adapterForSites);


                                if (STo_headerlist.size() !=0) {

                                    FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                                    ToSite = STo_headerlist.get(0).getRec_Site1().toString();

                                     FromPostion =adapterForSites.getPosition(FromSite);
                                     ToPostion=  adapterForSites.getPosition(ToSite);
                                     spiner_from_site.setSelection(FromPostion);
                                     spiner_to_site.setSelection(ToPostion);

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
                            }
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    String name = arrayitems.get(0).getCompany();
                    company = new StringBuilder("'"+name+"'");

                    for (int i = 0; i < arrayitems.size(); i++) {

                        if (i == 0) {
                            continue;
                        }
                        else {
                            String z = String.valueOf(arrayitems.get(i).getCompany());
                            company.append("," + "'" + z + "'");
                        }
                    }
                    params.put("type","STO1");
                    params.put("Company",company.toString());


                    return params;
                }

            };


            request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

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

            if (STo_headerlist.size() != 0) {
                FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                ToSite = STo_headerlist.get(0).getRec_Site1().toString();

                FromPostion = adapterForSites.getPosition(FromSite);
                ToPostion = adapterForSites.getPosition(ToSite);


                int FromForNewPostion = spiner_from_site.getSelectedItemPosition();
                int ToForNewPostion = spiner_to_site.getSelectedItemPosition();


                if (FromPostion == FromForNewPostion && ToPostion == ToForNewPostion) {
                    StatusNU = databaseHelperForTransfer.select_what_has_Status_value();
                    if (StatusNU.length() != 0) {
                        Toast.makeText(this, "هذا الأمر تم تحويله برقم" + StatusNU, Toast.LENGTH_SHORT).show();
                    } else {

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


            if (STo_headerlist.size() != 0) {
                FromSite = STo_headerlist.get(0).getIss_Site1().toString();
                ToSite = STo_headerlist.get(0).getRec_Site1().toString();

                FromPostion = adapterForSites.getPosition(FromSite);
                ToPostion = adapterForSites.getPosition(ToSite);


                int FromForNewPostion = spiner_from_site.getSelectedItemPosition();
                int ToForNewPostion = spiner_to_site.getSelectedItemPosition();

                String message = "";

                if (makeOrderBool)
                {
                    message = "احذف اخر امر شراء";
                }
                else
                {
                    message = "هل أنت متاكد من أنك تريد حذف أخر أمرتحويل";
                }

                if (FromPostion == FromForNewPostion && ToPostion == ToForNewPostion) {

                    new AlertDialog.Builder(this)
                            .setTitle(message)
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

                                    Intent GoToSearch;
                                    if (makeOrderBool)
                                    {
                                        GoToSearch = new Intent(FormTransferActivity.this, CreateOrderSearchActivity.class);
                                        GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                        GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                        GoToSearch.putExtra("ToSite", spiner_from_site.getSelectedItem().toString());
                                        GoToSearch.putExtra("type", sto_type);
                                    }
                                    else
                                    {
                                        GoToSearch = new Intent(FormTransferActivity.this, TransferSearchActivity.class);
                                        GoToSearch.putExtra("Department", spiner_pur_grp.getSelectedItem().toString());
                                        GoToSearch.putExtra("FromSite", spiner_from_site.getSelectedItem().toString());
                                        GoToSearch.putExtra("ToSite", spiner_to_site.getSelectedItem().toString());
                                    }
                                    GoToSearch.putExtra("MakeOrder",makeOrderBool);
                                    startActivity(GoToSearch);

                                    finish();

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
        }
    }

    public void
    getlistcompanies(){

        RequestQueue queue = Volley.newRequestQueue(this);
        request = new StringRequest(Request.Method.POST, Constant.Listforcompanies,
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

                Map<String, String> params = new HashMap<String, String>();;
                params.put("User_Name",userdataList.get(0).getUser_Name1());

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(request);

    }

}
