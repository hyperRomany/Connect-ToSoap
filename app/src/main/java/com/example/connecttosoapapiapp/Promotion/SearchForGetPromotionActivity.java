package com.example.connecttosoapapiapp.Promotion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class SearchForGetPromotionActivity extends AppCompatActivity {
    public StringRequest request=null;
    List<String> pur_org_describtionlist,pur_org_IDlist;
EditText edit_startdate,edit_enddate,edit_Barcode,edit_ID;
    Spinner sp_Department;
int  mYear, mMonth, mDay;
CheckBox check_promotiondate,check_promotion_Barcode , check_promotion_ID
        ,check_promotion_Department,check_promotionforarticle;
DatabaseHelperForProotion databaseHelperForProotion;
Button btn_search_prom;
String TodayOrActive="";
LinearLayout linear_of_date;
    String check_of_UserCode;
    DatabaseHelper databaseHelper;
    List<Users> userdataList;
    String startingDate="",enddate="",Barcode="",department;
    int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_get_promotion);
        edit_startdate=findViewById(R.id.edit_startdate);
        edit_enddate=findViewById(R.id.edit_enddate);
        btn_search_prom=findViewById(R.id.btn_search_prom);
        databaseHelperForProotion=new DatabaseHelperForProotion(this);

        linear_of_date=findViewById(R.id.linear_of_date);
        check_promotiondate=findViewById(R.id.check_promotiondate);
        check_promotion_Barcode=findViewById(R.id.check_promotion_Barcode);
        edit_Barcode=findViewById(R.id.edit_Barcode);
        check_promotion_ID=findViewById(R.id.check_promotion_ID);
        edit_ID=findViewById(R.id.edit_ID);
        check_promotion_Department=findViewById(R.id.check_promotion_Department);
        sp_Department=findViewById(R.id.sp_Department);
        check_promotionforarticle=findViewById(R.id.check_promotionforarticle);

        // To get user code
        databaseHelper=new DatabaseHelper(this);
        userdataList=new ArrayList<>();
        userdataList = databaseHelper.getUserData();
        check_of_UserCode=userdataList.get(0).getCompany1().toString().substring(2,3);
        Toast.makeText(this, ""+check_of_UserCode, Toast.LENGTH_SHORT).show();


        if (getIntent().getExtras() !=null){
            TodayOrActive=getIntent().getExtras().getString("TodayOrActive");
            Log.e("TodayOrActive",TodayOrActive);
        }

        if (TodayOrActive.equalsIgnoreCase("Today")){
            linear_of_date.setVisibility(View.GONE);
        } else {
            linear_of_date.setVisibility(View.VISIBLE);
        }

        check_promotiondate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edit_startdate.setEnabled(true);
                    edit_enddate.setEnabled(true);
                }else {
                    edit_startdate.setEnabled(false);
                    edit_enddate.setEnabled(false);
                }

            }
        });


        check_promotion_Barcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edit_Barcode.setEnabled(true);
                }else {
                    edit_Barcode.setEnabled(false);
                }

            }
        });

        check_promotion_ID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edit_ID.setEnabled(true);

                    check_promotiondate.setChecked(false);
                    check_promotion_Barcode.setChecked(false);
                    check_promotion_Department.setChecked(false);
                    check_promotionforarticle.setChecked(false);

                    check_promotiondate.setEnabled(false);
                    check_promotion_Barcode.setEnabled(false);
                    check_promotion_Department.setEnabled(false);
                    check_promotionforarticle.setEnabled(false);


                }else  if (isChecked == false){
                    edit_ID.setEnabled(false);

                    check_promotiondate.setChecked(false);
                    check_promotion_Barcode.setChecked(false);
                    check_promotion_Department.setChecked(false);
                    check_promotionforarticle.setChecked(false);

                    check_promotiondate.setEnabled(true);
                    check_promotion_Barcode.setEnabled(true);
                    check_promotion_Department.setEnabled(true);
                    check_promotionforarticle.setEnabled(true);
                }

            }
        });
        pur_org_describtionlist = new ArrayList<>();
        pur_org_IDlist = new ArrayList<>();
        check_promotion_Department.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sp_Department.setEnabled(true);
                    if (pur_org_describtionlist.size() ==0)
                        getlistfromsqlserver();
                }else {
                    sp_Department.setEnabled(false);
                }

            }
        });

        pickstartDate();
        pickendDate();

    }

    public void getPromotionfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL="";
        if (TodayOrActive.equalsIgnoreCase("Today") ||
                TodayOrActive.equalsIgnoreCase("Active")){
             URL = Constant.GetTodayPromotionURL;
        }else if (TodayOrActive.equalsIgnoreCase("Expired")){
            URL = Constant.GetExpiredPromotionURL;
        }else if (TodayOrActive.equalsIgnoreCase("Stoped")){
            URL = Constant.GetStopedPromotionURL;
        }else {
            Log.e("zzzzURL", "URL is empty");
        }


        Boolean Multiselection=false;
        if (check_promotion_ID.isChecked() ==true) {
            if (edit_ID.getText().toString().isEmpty()) {
                ID = 0;
            } else {
                ID = Integer.valueOf(edit_ID.getText().toString());
            }
        }else {
            ID=0;
        }

        if (check_promotiondate.isChecked() ==true &&
                (edit_startdate.getText().toString().isEmpty() || edit_enddate.getText().toString().isEmpty())) {

                if (edit_startdate.getText().toString().isEmpty()) {
                    edit_startdate.setError("أدخل تاريخ البداية");
                    edit_startdate.requestFocus();
                }
                if (edit_enddate.getText().toString().isEmpty()) {
                    edit_enddate.setError("أدخل تاريخ الالنهايه");
                    edit_enddate.requestFocus();
                }
            btn_search_prom.setVisibility(View.VISIBLE);

        }else {
            startingDate = edit_startdate.getText().toString();
            enddate = edit_enddate.getText().toString();
//                        Log.e("zzzstartingDate",""+startingDate);
//                        Log.e("zzzenddate",""+enddate);


            Log.e("zzzzURL", "URL is" + URL);
            request = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            btn_search_prom.setVisibility(View.VISIBLE);
                            String encodedstring = null;
                            try {
                                encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                                response = URLDecoder.decode(encodedstring, "UTF-8");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.e("onResponser", "response" + response);
                            //  Log.e("onResponse", "response"+Uri.encode(response, "utf-8").toString());
                            Log.e("onResponse", "request" + request);
                            try {

                                JSONObject object = new JSONObject(response);
                                //Log.e("onResponse", "object"+object);

                                String statusreponse = object.getString("statusreponse");
                                Log.e("onResponse", "object2" + statusreponse);

                                if (Integer.valueOf(statusreponse) == 1) {
                                    //TODO Delete Local DB
                                    databaseHelperForProotion.DeletePromItems();

                                    String notessize = object.getString("notessize");

                                    for (int i = 0; i < Integer.valueOf(notessize); i++) {
                                        String discountno = object.getString("discountno" + i);
                                        String date_from = object.getString("date_from" + i);
                                        String date_to = object.getString("date_to" + i);
                                        String discounttype = object.getString("discounttype" + i);
                                        String prom_desc = object.getString("prom_desc" + i);
                                        String last_modified_time = object.getString("last_modified_time" + i);
                                        String prom_post = object.getString("prom_post" + i);
                                        String status = object.getString("status" + i);
                                        String itemean = object.getString("itemean" + i);
                                        String department = object.getString("department" + i);
                                        String barcodeI = object.getString("barcode" + i);
                                        String item_desc = object.getString("item_desc" + i);

                                        String return_type, qty_std_price;
                                        if (TodayOrActive.equalsIgnoreCase("Today") ||
                                                TodayOrActive.equalsIgnoreCase("Active")) {
                                            return_type = object.getString("return_type" + i);
                                            qty_std_price = "";
                                        } else {
                                            qty_std_price = object.getString("qty_std_price" + i);
                                            return_type = "";
                                        }
                                        String sell_price = object.getString("sell_price" + i);
                                        String vatrate = object.getString("vatrate" + i);
                                        String discountvalue = object.getString("discountvalue" + i);
                                        String note_id = object.getString("note_id" + i);


                                        //TODO inset it to locl database
                                        databaseHelperForProotion.insertProitem(
                                                discountno,
                                                date_from,
                                                date_to,
                                                discounttype,
                                                prom_desc,
                                                last_modified_time,
                                                prom_post,
                                                status,
                                                itemean,
                                                department,
                                                barcodeI,
                                                item_desc,
                                                return_type,
                                                qty_std_price,
                                                sell_price,
                                                vatrate,
                                                discountvalue,
                                                note_id
                                        );


                                    }
                                    Intent GotoShow = new Intent(SearchForGetPromotionActivity.this, ShowItemsPromotionActivity.class);
                                    GotoShow.putExtra("TodayOrActive", TodayOrActive);
                                    startActivity(GotoShow);
                                    //  finish();
                                } else {
                                    Toast.makeText(SearchForGetPromotionActivity.this, "العرض غير موجود..تأكد من الاخيارات", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Log.e("zzzerror", "scacsasca" + e.getMessage());
                                e.printStackTrace();
                                Toast.makeText(SearchForGetPromotionActivity.this, "لم يتم العثور على العرض .. راجع البيانات مره أخرى", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            btn_search_prom.setVisibility(View.VISIBLE);
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


                    if (check_promotion_Barcode.isChecked() == true) {
                        if (edit_Barcode.getText().toString().isEmpty()) {
                            edit_Barcode.setError("أدخل الباركود");
                        } else {
                            Barcode = edit_Barcode.getText().toString();
                        }
                    } else {
                        Barcode = "";
                    }

                    if (check_promotion_Department.isChecked() == true) {
                        //TODO
                        department = pur_org_describtionlist.get(sp_Department.getSelectedItemPosition()).replace(" ", "");
                    } else {
                        department = "0";
                    }

//                Log.e("zzzzCond",""+GetPromotoins(ID
//                        ,0,startingDate
//                        ,enddate,Integer.valueOf(department),
//                        Barcode,false,false,check_promotiondate.isChecked()));

                    if (TodayOrActive.equalsIgnoreCase("Today") ||
                            TodayOrActive.equalsIgnoreCase("Active")) {
                        params.put("Cond", GetPromotoins(ID
                                , 0, startingDate
                                , enddate, Integer.valueOf(department),
                                Barcode, false, check_promotionforarticle.isChecked(), check_promotiondate.isChecked()));
                    } else if (TodayOrActive.equalsIgnoreCase("Expired")) {
                        params.put("Cond", GetPromotoins_Expired(ID
                                , 0, startingDate
                                , enddate, Integer.valueOf(department),
                                Barcode, false, check_promotionforarticle.isChecked(), check_promotiondate.isChecked()));
                    } else if (TodayOrActive.equalsIgnoreCase("Stoped")) {
                        params.put("Cond", GetPromotoins_Stopped(ID
                                , 0, startingDate
                                , enddate, Integer.valueOf(department),
                                Barcode, false, check_promotionforarticle.isChecked(), check_promotiondate.isChecked()));
                    }
                    params.put("UserType", check_of_UserCode);

                    return params;
                }

            };

            // Add the realibility on the connection.
            request.setRetryPolicy(new DefaultRetryPolicy(500000, 1, 1.0f));

            // Start the request immediately
            queue.add(request);

        }

    }






    public void getlistfromsqlserver(){
        //TAG_TRIP_PRICE + Uri.encode(tripFromSelected, "utf-8").toString() + "/" +
        //        Uri.encode(tripToSelected, "utf-8").toString()
        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.Listforpgrp_descriptionFromSqlServerURL,
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


                            String p_orgsize= object.getString("PGRP_PORsize");
                            Log.e("onResponse", p_orgsize);


                            for (int i=0 ; i<Integer.valueOf(p_orgsize);i++){

                                String pgrp_description= object.getString("pgrp_description"+i);
                                String pgrp_id= object.getString("pgrp"+i);
                                Log.e("p_org_id"+i, pgrp_description);
                                //if (p_org_id.contains(check_of_UserCode)) {
                                pur_org_describtionlist.add(pgrp_id);
                                pur_org_IDlist.add(pgrp_id);

                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(SearchForGetPromotionActivity.this,
                                    android.R.layout.simple_spinner_item, pur_org_describtionlist);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_Department.setAdapter(adapter);

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

    private void pickstartDate() {
        edit_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchForGetPromotionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edit_startdate.setText( year + "-" + (monthOfYear + 1) + "-" + dayOfMonth  );
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
        private void pickendDate(){
            edit_enddate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();

                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(SearchForGetPromotionActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    edit_enddate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                } }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            });

    }


    public void GetPromotion(View view) {
        btn_search_prom.setVisibility(View.GONE);

        // TODO let him choice without
        if (TodayOrActive.equalsIgnoreCase("Today") ){
            getPromotionfromsqlserver();

        }else {

            if (check_promotiondate.isChecked() || check_promotion_Barcode.isChecked() ||
                    check_promotion_ID.isChecked() ||
                    check_promotion_Department.isChecked() || check_promotionforarticle.isChecked()) {
                getPromotionfromsqlserver();
            } else {
                btn_search_prom.setVisibility(View.VISIBLE);
                Toast.makeText(this, "من فضلك قم بالاختيار اولا", Toast.LENGTH_SHORT).show();
            }
        }
//        Intent GotoShow = new Intent(SearchForGetPromotionActivity.this, ShowItemsPromotionActivity.class);
//        startActivity(GotoShow);

    }


    public String GetPromotoins(int PromId, int PromotionStatus, String Startingdate,String endeddate,
                                int Department, String Barcode, Boolean IsNew,
                                Boolean MultiDepartment, Boolean CheckDate)
    {
//        if (PromotionStatus == 2)
//            return GetPromotoins_Stopped(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);
//
//        else if (PromotionStatus == 9)
//            return GetPromotoins_Expired(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String Datetoday = sdf.format(new Date());
        String _cond = "";

//        Log.e("zzzstarting1Date",""+Startingdate.substring(0,Startingdate.indexOf("/")));
//        Log.e("zzzstarting2Date",""+Startingdate.substring(Startingdate.indexOf("/")+1,Startingdate.indexOf(",")));
//        Log.e("zzzstarting3Date",""+Startingdate.substring(Startingdate.indexOf(",")+1,Startingdate.length()));

        if (PromId > 0) {
            _cond = " AND discountno = " + PromId;

            if (TodayOrActive.equalsIgnoreCase("Today")){
                //last_modified_time
                _cond += " AND cast ( I.last_modified_time as date ) <=  ''" +Datetoday+"''" ;
            }

        }else {

            if (Department > 0 && !MultiDepartment)
            {
                _cond += " AND department = " + Department;
            }

            if (!Barcode.isEmpty())
            {
                _cond += " AND barcode = convert(varchar(30), " + Barcode + ")";
            }
            // تاريخ بداية العرض أكبر من
            // تاريخ نهاية العرض أقل من
            if (CheckDate) {
//                try {
//                    Date startdate=sdf.parse(Startingdate) ;
//                    Date enddate=sdf.parse(endeddate);
                Log.e("zzzstarting000Date",""+ Startingdate);
//                    _cond += " AND I.date_from >=  dbo.Prom_ConvertDateTime(''" + DateFormat.format("dd",startdate)+
//                            "'',''"+DateFormat.format("MM",startdate)
//                            + "'',''"+ DateFormat.format("yyyy",startdate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'')"+
//                            " AND I.date_to <=  dbo.Prom_ConvertDateTime( ''" + DateFormat.format("dd",enddate)+ "'',''"+
//                            DateFormat.format("MM",enddate)
//                            + "'',''"+ DateFormat.format("yyyy",enddate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'' )";
                _cond += " AND cast ( I.date_from as date ) <=  ''" +Startingdate+"''"+
                        " AND cast ( I.date_to as date ) >=  ''" +endeddate+"''";
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }

            if (TodayOrActive.equalsIgnoreCase("Today")){
                //P.last_modified_time
                _cond += " AND cast ( I.last_modified_time as date ) =  ''" +Datetoday+"''" ;
            }

            //العروض الجديدة فقط .. بدون تحديد فترة
//            if (IsNew) {
//
//
//                _cond += " AND I.date_from >= dbo.Prom_ConvertDate(" +Datetoday + ")";
//
//            }
            //            else
//                _cond += " AND I.date_from < dbo.Prom_ConvertDate(" + DateTime.Now.Day.ToString() + "," + DateTime.Now.Month.ToString()
//                        + "," + DateTime.Now.Year.ToString() + ")";
            Log.e("zzzMultiDepartment",""+MultiDepartment);

            if (MultiDepartment==true)
                //   @
                _cond += "and I.discountno in (select discountno from pos_items I where 1 = 1 "
                        + _cond +
                        " group by discountno having COUNT(distinct department) > 1 )";

        }

        Log.e("zzz_cond",""+_cond);
//      string querySring = "dbo.SP_PROM_GetPromotions_Active";
//        SqlParameter[] param = new SqlParameter[1];
//        param[0] = new SqlParameter("@Cond", _cond);
//
//        DataTable dtPromotions = analyticalConnection.executeSelectQuery(querySring, param, true);
//        return dtPromotions;

        return _cond;
    }

    private String GetPromotoins_Expired(int PromId, int PromotionStatus, String Startingdate,String endeddate,
                                            int Department, String Barcode, Boolean IsNew,
                                            Boolean MultiDepartment, Boolean CheckDate)
    {
//        if (PromotionStatus == 2)
//            return GetPromotoins_Stopped(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);
//
//        else if (PromotionStatus == 9)
//            return GetPromotoins_Expired(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String Datetoday = sdf.format(new Date());
//        String _cond = "";




//        Log.e("zzzstarting1Date",""+Startingdate.substring(0,Startingdate.indexOf("/")));
//        Log.e("zzzstarting2Date",""+Startingdate.substring(Startingdate.indexOf("/")+1,Startingdate.indexOf(",")));
//        Log.e("zzzstarting3Date",""+Startingdate.substring(Startingdate.indexOf(",")+1,Startingdate.length()));
        String _cond = "";
        if (PromId > 0) {
            _cond = " AND I.PROM_ID = " + PromId;

        }
        else {

            if (Department > 0 && !MultiDepartment)
            {
                _cond += " AND I.department = " + Department;
            }

            if (!Barcode.isEmpty())
            {
                _cond += " AND  I.barcode = convert(varchar(30), " + Barcode + ")";
            }
            // تاريخ بداية العرض أكبر من
            // تاريخ نهاية العرض أقل من
            if (CheckDate) {
//                try {
//                    Date startdate=sdf.parse(Startingdate) ;
//                    Date enddate=sdf.parse(endeddate);
                Log.e("zzzstarting000Date",""+ Startingdate);
//                    _cond += " AND I.date_from >=  dbo.Prom_ConvertDateTime(''" + DateFormat.format("dd",startdate)+
//                            "'',''"+DateFormat.format("MM",startdate)
//                            + "'',''"+ DateFormat.format("yyyy",startdate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'')"+
//                            " AND I.date_to <=  dbo.Prom_ConvertDateTime( ''" + DateFormat.format("dd",enddate)+ "'',''"+
//                            DateFormat.format("MM",enddate)
//                            + "'',''"+ DateFormat.format("yyyy",enddate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'' )";
                _cond += " AND cast ( P.date_to as date ) >=  ''" +Startingdate+"''"+
                        " AND cast ( P.date_to as date ) <=  ''" +endeddate+"''";
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
            else {
                _cond += " AND P.date_to <= Getdate() " ;
            }

            //العروض الجديدة فقط .. بدون تحديد فترة
//            if (IsNew) {
//
//
//                _cond += " AND I.date_from >= dbo.Prom_ConvertDate(" +Datetoday + ")";
//
//            }
            //            else
//                _cond += " AND I.date_from < dbo.Prom_ConvertDate(" + DateTime.Now.Day.ToString() + "," + DateTime.Now.Month.ToString()
//                        + "," + DateTime.Now.Year.ToString() + ")";
            Log.e("zzzMultiDepartment",""+MultiDepartment);

            if (MultiDepartment==true)
                //   @
                _cond += "and I.PROM_ID in (select discountno from pos_items I where 1=1 "
                        + _cond +
                        "  group by discountno having COUNT(distinct department) > 1 )";

        }

        Log.e("zzz_cond",""+_cond);
//      string querySring = "dbo.SP_PROM_GetPromotions_Active";
//        SqlParameter[] param = new SqlParameter[1];
//        param[0] = new SqlParameter("@Cond", _cond);
//
//        DataTable dtPromotions = analyticalConnection.executeSelectQuery(querySring, param, true);
//        return dtPromotions;

        return _cond;
    }


    public String GetPromotoins_Stopped(int PromId, int PromotionStatus, String Startingdate,String endeddate,
    int Department, String Barcode, Boolean IsNew,
    Boolean MultiDepartment, Boolean CheckDate)
    {
//        if (PromotionStatus == 2)
//            return GetPromotoins_Stopped(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);
//
//        else if (PromotionStatus == 9)
//            return GetPromotoins_Expired(PromId, PromotionStatus, DateFrom, DateTo, Department, Barcode, IsNew, MultiDepartment, CheckDate);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String Datetoday = sdf.format(new Date());
//        String _cond = "";




//        Log.e("zzzstarting1Date",""+Startingdate.substring(0,Startingdate.indexOf("/")));
//        Log.e("zzzstarting2Date",""+Startingdate.substring(Startingdate.indexOf("/")+1,Startingdate.indexOf(",")));
//        Log.e("zzzstarting3Date",""+Startingdate.substring(Startingdate.indexOf(",")+1,Startingdate.length()));
        String _cond = "";
        if (PromId > 0) {
            _cond = " AND I.PROM_ID = " + PromId;
        }
        else {

            if (Department > 0 && !MultiDepartment)
            {
                _cond += " AND I.department = " + Department;
            }

            if (!Barcode.isEmpty())
            {
                _cond += " AND I.barcode = convert(varchar(30), " + Barcode + ")";
            }
            // تاريخ بداية العرض أكبر من
            // تاريخ نهاية العرض أقل من
            if (CheckDate) {
//                try {
//                    Date startdate=sdf.parse(Startingdate) ;
//                    Date enddate=sdf.parse(endeddate);
                Log.e("zzzstarting000Date",""+ Startingdate);
//                    _cond += " AND I.date_from >=  dbo.Prom_ConvertDateTime(''" + DateFormat.format("dd",startdate)+
//                            "'',''"+DateFormat.format("MM",startdate)
//                            + "'',''"+ DateFormat.format("yyyy",startdate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'')"+
//                            " AND I.date_to <=  dbo.Prom_ConvertDateTime( ''" + DateFormat.format("dd",enddate)+ "'',''"+
//                            DateFormat.format("MM",enddate)
//                            + "'',''"+ DateFormat.format("yyyy",enddate) + "'',''"+"00" + "'',''"+"00" + "'',''"+"00" + "'' )";
                _cond += " AND cast ( P.last_modified_time as date ) >=  ''" +Startingdate+"''"+
                        " AND cast ( P.last_modified_time as date ) <=  ''" +endeddate+"''";
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }

            //العروض الجديدة فقط .. بدون تحديد فترة
//            if (IsNew) {
//
//
//                _cond += " AND I.date_from >= dbo.Prom_ConvertDate(" +Datetoday + ")";
//
//            }
            //            else
//                _cond += " AND I.date_from < dbo.Prom_ConvertDate(" + DateTime.Now.Day.ToString() + "," + DateTime.Now.Month.ToString()
//                        + "," + DateTime.Now.Year.ToString() + ")";
            Log.e("zzzMultiDepartment",""+MultiDepartment);

            if (MultiDepartment==true)
                //   @
                _cond += "and I.PROM_ID in (select discountno from pos_items I where 1=1 "
                        + _cond +
                        "  group by discountno having COUNT(distinct department) > 1 )";

        }

        Log.e("zzz_cond",""+_cond);
//      string querySring = "dbo.SP_PROM_GetPromotions_Active";
//        SqlParameter[] param = new SqlParameter[1];
//        param[0] = new SqlParameter("@Cond", _cond);
//
//        DataTable dtPromotions = analyticalConnection.executeSelectQuery(querySring, param, true);
//        return dtPromotions;

        return _cond;
    }
}
