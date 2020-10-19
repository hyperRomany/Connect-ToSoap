package com.example.connecttosoapapiapp.Promotion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.connecttosoapapiapp.Promotion.Helper.DatabaseHelperForProotion;
import com.example.connecttosoapapiapp.Promotion.Modules.NoteModule;
import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {
    String Barcode,Superviser_id,Superviser_username;
    Boolean modify_for_all;
    DatabaseHelperForProotion databaseHelperForProotion;
    List<Prom_item_Module> prom_item_moduleList,prom_item_moduleListforUpload;
    TextView txt_discoun_no,txt_status,txt_barcode,txt_article,txt_discountdepartment,
            txt_des,txt_sellprice,txt_taxs,txt_superviser_name,txt_totalprice,txt_discountvalue;
    EditText sp_another_notes;
    public StringRequest request=null;
    Spinner sp_notes;
    List<String> noteslist;
    List<NoteModule> idandnoteslist;
    String noteIDIsSelected="";
    Boolean UpdateOrInsert;
    Button btn_ReviewDone;
    String InsertAllOrInsertLastChange;
    List<String> InsetallornotList;
    String check_of_UserCode;
    DatabaseHelper databaseHelper;
    List<Users> userdataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        txt_discoun_no=findViewById(R.id.txt_discoun_no);
        txt_status=findViewById(R.id.txt_status);
        txt_barcode=findViewById(R.id.txt_barcode);
                txt_article=findViewById(R.id.txt_article);
        txt_discountdepartment=findViewById(R.id.txt_discountdepartment);
                txt_des=findViewById(R.id.txt_des);
        txt_sellprice=findViewById(R.id.txt_sellprice);
                txt_taxs=findViewById(R.id.txt_taxs);
        btn_ReviewDone=findViewById(R.id.btn_ReviewDone);
        txt_superviser_name=findViewById(R.id.txt_superviser_name);

        sp_another_notes=findViewById(R.id.sp_another_notes);

        txt_totalprice=findViewById(R.id.txt_totalprice);

        txt_discountvalue=findViewById(R.id.txt_discountvalue);

        sp_notes=findViewById(R.id.sp_notes);

        if (getIntent().getExtras() !=null){
            Barcode=getIntent().getExtras().getString("Barcode");
            Superviser_id=getIntent().getExtras().getString("Superviser_id");
            Superviser_username=getIntent().getExtras().getString("Superviser_username");

            modify_for_all=getIntent().getExtras().getBoolean("modify_for_all");

            UpdateOrInsert=getIntent().getExtras().getBoolean("UpdateOrInsert");
            Log.e("zzzdiscount_id",""+UpdateOrInsert);

            Log.e("zzzdiscount_id",""+Barcode);
            Log.e("zzzSuperviser_id",""+Superviser_id);
            Log.e("zzzSuperviser_username",""+Superviser_username);

            Log.e("zzzmodify_for_all",""+modify_for_all);
        }

        // To get user code
        databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();
        userdataList = databaseHelper.getUserData();
        check_of_UserCode=userdataList.get(0).getCompany1()/*.toString().substring(1,3)*/;
    //    Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();

        databaseHelperForProotion=new DatabaseHelperForProotion(this);

        DecimalFormat dwith0 = new DecimalFormat("###,##0.00");
        dwith0.setRoundingMode(RoundingMode.HALF_UP);

        prom_item_moduleList=new ArrayList<>();
        prom_item_moduleList = databaseHelperForProotion.selectPromItemsByBarcode(Barcode);
        Log.e("zzzprom_item_moduleList",""+prom_item_moduleList.size());

    //    InsertAllOrInsertLastChange=databaseHelperForProotion.selectPromItemsThatHas(prom_item_moduleList.get(0).getDiscountno1());
        txt_discoun_no.setText(prom_item_moduleList.get(0).getDiscountno1());
        txt_status.setText(prom_item_moduleList.get(0).getStatus1());
        txt_barcode.setText(prom_item_moduleList.get(0).getBarcode1());
                txt_article.setText(prom_item_moduleList.get(0).getItemean1());
        txt_discountdepartment.setText(prom_item_moduleList.get(0).getDepartment1());
                txt_des.setText(prom_item_moduleList.get(0).getItem_desc1());
        txt_sellprice.setText(dwith0.format(Double.valueOf(prom_item_moduleList.get(0).getSell_price1())));
        txt_taxs.setText(dwith0.format(Double.valueOf(prom_item_moduleList.get(0).getVatrate1())));


//TODO if discount type not value don't calculte sales price
        if (prom_item_moduleList.get(0).getDiscounttype1().equalsIgnoreCase("3")
        || prom_item_moduleList.get(0).getDiscounttype1().equalsIgnoreCase("2") ) {
            txt_totalprice.setText(String.valueOf(
                    dwith0.format(
                            (Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) -
                            Double.valueOf(prom_item_moduleList.get(0).getDiscountvalue1()))

                            +((Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) -
                                    Double.valueOf(prom_item_moduleList.get(0).getDiscountvalue1()))
                            )
                                    * (Double.valueOf(prom_item_moduleList.get(0).getVatrate1())/100))
            ));

            txt_discountvalue.setText(prom_item_moduleList.get(0).getDiscountvalue1());


        } else if (prom_item_moduleList.get(0).getDiscounttype1().equalsIgnoreCase("101")) {
            txt_totalprice.setText(String.valueOf(
                    dwith0.format(
                            (Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) -
                            (Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) *
                                    (Double.valueOf(prom_item_moduleList.get(0).getDiscountvalue1()) * 0.01 )))

                                    +(
                                            (Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) -
                                    (Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) *
                                            (Double.valueOf(prom_item_moduleList.get(0).getDiscountvalue1()) * 0.01 )))
                            ) *
                                    (Double.valueOf(prom_item_moduleList.get(0).getVatrate1())/100))
            ));

            txt_discountvalue.setText(String.valueOf(Double.valueOf(prom_item_moduleList.get(0).getSell_price1()) *
                    (Double.valueOf(prom_item_moduleList.get(0).getDiscountvalue1()) * 0.01 )));

        } else {
            txt_discountvalue.setVisibility(View.INVISIBLE);
        }
         noteslist=new ArrayList<>();
        idandnoteslist=new ArrayList<>();
        getlistfromsqlserver();


    }


    public void getlistfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.ListfornotsFromSqlServerURL,
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


                            String notessize= object.getString("notessize");

                            Log.e("onResponse", notessize);


                            for (int i=0 ; i<Integer.valueOf(notessize);i++){
                                String id= object.getString("id"+i);
                                String name= object.getString("name"+i);

                              //  String note_name= object.getString("name"+i);
                                Log.e("note_name"+i, name);
                                //if (p_org_id.contains(check_of_UserCode)) {
                                noteslist.add(name);
                                idandnoteslist.add(new NoteModule(id,name));

                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ReviewActivity.this,
                                    android.R.layout.simple_spinner_item, noteslist);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_notes.setAdapter(adapter);
                            sp_notes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Log.e("zzzadapterpos>>",""+parent.getSelectedItem().toString());
                                    Log.e("zzzarraypos>>",""+noteslist.indexOf(parent.getSelectedItem().toString()));
                                    noteIDIsSelected=idandnoteslist.get(noteslist.indexOf(parent.getSelectedItem().toString())).getNote_id();
                                    Log.e("zzzarraynoteid>>",""+noteIDIsSelected);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

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
    public void Back(View view) {
        Intent gotodeatails = new Intent(ReviewActivity.this, PromItemDetailsActivity.class);
        gotodeatails.putExtra("discount_id", txt_discoun_no.getText().toString());
        startActivity(gotodeatails);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent gotodeatails = new Intent(ReviewActivity.this, PromItemDetailsActivity.class);
        gotodeatails.putExtra("discount_id", txt_discoun_no.getText().toString());
        startActivity(gotodeatails);
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void ReviewDone(View view) {

        if (noteIDIsSelected.isEmpty()){
            Toast.makeText(this, "من فضلك أختار الملاحظات", Toast.LENGTH_SHORT).show();
        }else {
        //    WriteInLogs_sap_ITEMStableOfSqlServer();
            btn_ReviewDone.setVisibility(View.GONE);
            InsetallornotList=new ArrayList<>();
            InsetallornotList = databaseHelperForProotion.selectPromItemsForInsetall(txt_discoun_no.getText().toString());
            Log.e("zzzInsetallornotList>>", ""+InsetallornotList.size());

//            if (InsetallornotList.size() !=0) {
                if (modify_for_all == false) {
                    //Log.e("zzzmodify_for_all>>", "ReviewDone");

                    databaseHelperForProotion.Update_noteid_Userid_and_username_For_Barcode(Barcode,
                            Superviser_id, Superviser_username, noteIDIsSelected, sp_another_notes.getText().toString());


                    prom_item_moduleList = new ArrayList<>();
                    prom_item_moduleList = databaseHelperForProotion.selectPromItemsByBarcode(Barcode);
                    WriteInLogs_sap_ITEMStableOfSqlServer();
                } else {

                    databaseHelperForProotion.Update_noteid_Userid_and_username_For_Alldiscuntno(txt_discoun_no.getText().toString(),
                            Superviser_id, Superviser_username, noteIDIsSelected, sp_another_notes.getText().toString());

                    prom_item_moduleList = new ArrayList<>();
                    prom_item_moduleList = databaseHelperForProotion.selectPromItems(txt_discoun_no.getText().toString());

                    WriteInLogs_sap_ITEMStableOfSqlServer();

                }
//            }else {
//                databaseHelperForProotion.Update_Userid_and_username_For_Alldiscuntnoforuploadforfirsttime(txt_discoun_no.getText().toString(),
//                        Superviser_id, Superviser_username);
//                databaseHelperForProotion.Update_noteid_and_noteother_For_Barcode(Barcode, noteIDIsSelected,
//                        sp_another_notes.getText().toString());
//                prom_item_moduleList = new ArrayList<>();
//                prom_item_moduleList = databaseHelperForProotion.selectPromItems(txt_discoun_no.getText().toString());
//                WriteInLogs_sap_ITEMStableOfSqlServer();
//            }
            btn_ReviewDone.setVisibility(View.VISIBLE);
        }
    }

    public void WriteInLogs_sap_ITEMStableOfSqlServer(){

        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.UpdatepromotionafterReviewonSqlServerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("onResponseU", response);
                        Log.e("onResponseUR", " "+request);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d("onResponse", status);

                            String message = object.getString("message");
                            Log.d("onResponse", message);

                                if (status.equalsIgnoreCase("1")){
                                    Toast.makeText(ReviewActivity.this, "تم أرسال المراجعه", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ReviewActivity.this, "لم يتم أرسال المراجعه", Toast.LENGTH_SHORT).show();
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
                        String errorMsg = " ";
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
                JSONObject obj=null;
                JSONArray array=new JSONArray();
                ArrayList arrayreqest=new ArrayList();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String Date = sdf.format(new Date());


                prom_item_moduleListforUpload=new ArrayList<Prom_item_Module>();

                for (int i=0;i<prom_item_moduleList.size();i++) {

                    Prom_item_Module prom_item_module = new Prom_item_Module();

                    prom_item_module.setDiscountno1(prom_item_moduleList.get(i).getDiscountno1());
                    prom_item_module.setDate_from1(prom_item_moduleList.get(i).getDate_from1());
                    prom_item_module.setDate_to1(prom_item_moduleList.get(i).getDate_to1());
                    prom_item_module.setDiscounttype1(prom_item_moduleList.get(i).getDiscounttype1());
                    prom_item_module.setProm_desc1(prom_item_moduleList.get(i).getProm_desc1());

                    //prom_item_moduleList.get(i).getLast_modified_time1()
                    prom_item_module.setLast_modified_time1(Date);

                    prom_item_module.setProm_post1(prom_item_moduleList.get(i).getProm_post1());
                    prom_item_module.setStatus1(prom_item_moduleList.get(i).getStatus1());
                    prom_item_module.setItemean1(prom_item_moduleList.get(i).getItemean1());
                    prom_item_module.setDepartment1(prom_item_moduleList.get(i).getDepartment1());

                    prom_item_module.setReturn_type1(prom_item_moduleList.get(i).getReturn_type1());
                    prom_item_module.setSell_price1(prom_item_moduleList.get(i).getSell_price1());
                    prom_item_module.setVatrate1(prom_item_moduleList.get(i).getVatrate1());
                    prom_item_module.setDiscountvalue1(prom_item_moduleList.get(i).getDiscountvalue1());

                    prom_item_module.setBarcode1(prom_item_moduleList.get(i).getBarcode1());
                    prom_item_module.setItem_desc1(prom_item_moduleList.get(i).getItem_desc1());

//                    if (InsetallornotList.size() ==0) {
//                        prom_item_module.setNote_id1("0");
//                    }else {
                        prom_item_module.setNote_id1(prom_item_moduleList.get(i).getNote_id1());
//                    }
                    if (prom_item_moduleList.get(i).getSupervisor_id1()==null){
                        prom_item_module.setSupervisor_id1("0");
                    }else {
                        prom_item_module.setSupervisor_id1(prom_item_moduleList.get(i).getSupervisor_id1());
                    }
                    if (prom_item_moduleList.get(i).getSupervisor_name1()==null){
                        prom_item_module.setSupervisor_name1("");
                    }else {
                        prom_item_module.setSupervisor_name1(prom_item_moduleList.get(i).getSupervisor_name1());
                    }
                    if (prom_item_moduleList.get(i).getOtherNotes1()==null){
                        prom_item_module.setOtherNotes1("");
                    }else {
                        prom_item_module.setOtherNotes1(prom_item_moduleList.get(i).getOtherNotes1());
                    }

                    prom_item_module.setCompany(check_of_UserCode);
                    prom_item_moduleListforUpload.add(prom_item_module);


                }
                /*Gson gson=new Gson();

                String newDataArray=gson.toJson(Po_Items_For_LogsArray); // dataarray is list aaray

                Log.e("Requestparams",""+newDataArray);*/

                Log.e("zzzsizeupload",""+prom_item_moduleListforUpload.size());
                Log.e("zzzsize",""+prom_item_moduleList.size());
                Gson gson = new GsonBuilder().create();
                JsonArray equipmentJsonArray = gson.toJsonTree(prom_item_moduleListforUpload).getAsJsonArray();
            //    equipmentJsonArray.get(0).
                //From_Sap_Or_Not=false;
                params.put("RequestArray", equipmentJsonArray.toString());

                /*if (UpdateOrInsert == true)
                params.put("UpdateOrNot", "true");
                else
                    params.put("UpdateOrNot", "false");
*/
                //Log.e("Requestparams",""+obj);

                return params;
            }

        };

        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, 1.0f));

        // Start the request immediately
        queue.add(request);

    }
}
