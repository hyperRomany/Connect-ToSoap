package com.example.connecttosoapapiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.connecttosoapapiapp.CycleCount.CycleCountActivity;
import com.example.connecttosoapapiapp.GIModule.MainFormOfGIActivity;
import com.example.connecttosoapapiapp.ItemAvailability.ScanItemAvailabilityActivity;
import com.example.connecttosoapapiapp.ItemReturn.MainItemReturnActivity;
import com.example.connecttosoapapiapp.Promotion.MainPromotionActivity;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.ReceivingActivity;
import com.example.connecttosoapapiapp.ReceivingModule.model.Groups;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.ScanBarcode.ScanBarcodActivity;
import com.example.connecttosoapapiapp.TransfereModule.FormTransferActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView txt_module_recieving,txt_module_transafer,txt_module_cyclecount
            ,txt_module_GI,txt_ScanBarCode,txt_ReturnItem,txt_item_availabilty,
            txt_promotion;
    DatabaseHelper databaseHelper;
    public StringRequest request = null;
    List<Users> userdataList;
    SharedPreferences preferences;
    SharedPreferences.Editor preferencesEditor;
    // private DatabaseHelperForPoItem databaseHelperForPoItem;

ArrayList<String> arrayList_po_header;
    String UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseHelper=new DatabaseHelper(this);
        //createPo_Header();
        arrayList_po_header = new ArrayList();
        txt_module_recieving=findViewById(R.id.txt_module_recieving);
        txt_module_transafer=findViewById(R.id.txt_module_transafer);
        txt_module_cyclecount=findViewById(R.id.txt_module_cyclecount);
        txt_module_GI=findViewById(R.id.txt_module_GI);
        txt_ScanBarCode=findViewById(R.id.txt_ScanBarCode);
        txt_ReturnItem=findViewById(R.id.txt_ReturnItem);
        txt_item_availabilty=findViewById(R.id.txt_item_availabilty);
        txt_promotion=findViewById(R.id.txt_promotion);

        //new myAsyncTask().execute("gfffffffffff");
        userdataList = databaseHelper.getUserData();

        VisableModules();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferencesEditor = preferences.edit();

        if (isFirstRun("update")==true) {
            Details(userdataList.get(0).getUser_Name1().trim());
        }
    }

    private void VisableModules() {
        List<Groups> modulesID = databaseHelper.Get_Groups();
        for (int i=0;i<modulesID.size();i++){
            if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("4")){
                txt_item_availabilty.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("5")){
                txt_ScanBarCode.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("6")){
                txt_module_transafer.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("7")){
                txt_module_cyclecount.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("9")){
                txt_ReturnItem.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("11")){
                txt_promotion.setVisibility(View.VISIBLE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("19")){
                txt_module_GI.setVisibility(View.VISIBLE);
            }/*else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.GONE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.GONE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.GONE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.GONE);
            }else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.GONE);
            }*/
        }
    }



    public void go_to_receiving_module(View view) {
        Intent Got_To_Reciving_module= new Intent(MainActivity.this, ReceivingActivity.class);
        startActivity(Got_To_Reciving_module);
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(MainActivity.this,LoginActivity.class);
        Go_Back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void go_to_transfer_module(View view) {
            Intent Got_To_Transfer_module= new Intent(MainActivity.this, FormTransferActivity.class);
            startActivity(Got_To_Transfer_module);

    }

    public void go_to_cyclecount_module(View view) {
        Intent Got_To_Transfer_module= new Intent(MainActivity.this, CycleCountActivity.class);
        startActivity(Got_To_Transfer_module);
    }

    public void go_to_GI_module(View view) {

        Intent Go_To_GI_Module = new Intent(MainActivity.this, MainFormOfGIActivity.class);
        startActivity(Go_To_GI_Module);

    }

    public void go_Scan_Barcode_module(View view) {
        Intent Go_To_ScanBarcode =new Intent(MainActivity.this, ScanBarcodActivity.class);
        startActivity(Go_To_ScanBarcode);
    }

    public void go_Return_Ite_module(View view) {
        Intent got_To_ReturnItem=new Intent(MainActivity.this, MainItemReturnActivity.class);
        startActivity(got_To_ReturnItem);

    }

    public void go_Item_Availability(View view) {
        Intent Gotoitemavailability=new Intent(MainActivity.this, ScanItemAvailabilityActivity.class);
        startActivity(Gotoitemavailability);
    }

    public void go_promotion(View view) {
        Intent GoToPromotion=new Intent(MainActivity.this, MainPromotionActivity.class);
        startActivity(GoToPromotion);
    }
    public void Details(final String userName)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        // String URL = Constant.LoginURL;
        request = new StringRequest(Request.Method.POST, Constant.Details_for_Update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("onResponse", response);
                        Log.d("onResponse", ""+request);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d("onResponse", status);

                            String message = object.getString("message");
                            Log.d("onResponse", message);

                                /*if (status.equalsIgnoreCase("1")){
                                    Intent gotomain =new Intent(UploadForTransferActivity.this,MainActivity.class);
                                    startActivity(gotomain);
                                }else {
                                    Toast.makeText(UploadForTransferActivity.this, "Your User Or Password Is Wrong", Toast.LENGTH_SHORT).show();
                                }*/

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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String Date = sdf.format(new Date());
                String Device_id_Instance_of_MacAdress = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String versionName = BuildConfig.VERSION_NAME;
                Map<String, String> params = new HashMap<String, String>();
                params.put("PocketPCMAC", Device_id_Instance_of_MacAdress);
                params.put("Version", versionName);
                params.put("Modified_date",Date);
                params.put("UserName",userName);



                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }


    public boolean isFirstRun(String forWhat) {
        if (preferences.getBoolean(forWhat, true)) {
            preferencesEditor.putBoolean(forWhat, false).commit();
            return true;
        } else {
            return false;
        }
    }
}