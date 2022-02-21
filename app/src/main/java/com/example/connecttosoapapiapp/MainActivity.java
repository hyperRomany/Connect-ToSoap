package com.example.connecttosoapapiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import com.example.connecttosoapapiapp.ItemAvailability.ItemAvailabilityModuleActivity;
import com.example.connecttosoapapiapp.ItemReturn.MainItemReturnActivity;
import com.example.connecttosoapapiapp.Promotion.MainPromotionActivity;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.ReceivingActivity;
import com.example.connecttosoapapiapp.ReceivingModule.model.Groups;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.ScanBarcode.ScanBarcodActivity;
import com.example.connecttosoapapiapp.StockAvailable.AllSiteAvailabilityActivity;
import com.example.connecttosoapapiapp.TransfereModule.FormTransferActivity;
import com.example.connecttosoapapiapp.makeOrder.ChooseSiteActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

   private TextView txt_module_recieving,txt_module_transafer,txt_module_cyclecount
            ,txt_module_GI,txt_ScanBarCode,txt_ReturnItem,txt_item_availabilty,
            txt_promotion,txt_item_stock, createOrder;
   private DatabaseHelper databaseHelper;
   private SharedPreferences preferences;
   private SharedPreferences.Editor preferencesEditor;


   @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        loadingModules();

        navigateActivity(ReceivingActivity.class, txt_module_recieving);

       txt_module_transafer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, FormTransferActivity.class);
               intent.putExtra("MakeOrder", false);
               startActivity(intent);
           }
       });

        navigateActivity(CycleCountActivity.class, txt_module_cyclecount);

        navigateActivity(MainFormOfGIActivity.class, txt_module_GI);

        navigateActivity(ScanBarcodActivity.class, txt_ScanBarCode);

        navigateActivity(MainItemReturnActivity.class, txt_ReturnItem);

        navigateActivity(ItemAvailabilityModuleActivity.class, txt_item_availabilty);

        navigateActivity(MainPromotionActivity.class, txt_promotion);

        navigateActivity(AllSiteAvailabilityActivity.class, txt_item_stock);

        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseSiteActivity.class);
                intent.putExtra("MakeOrder", true);
                startActivity(intent);
            }
        });

   }


    private void initView()
    {
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
       databaseHelper=new DatabaseHelper(this);
       //createPo_Header();
        ArrayList<String> arrayList_po_header = new ArrayList<>();
       txt_module_recieving=findViewById(R.id.txt_module_recieving);
       txt_module_transafer=findViewById(R.id.txt_module_transafer);
       txt_module_cyclecount=findViewById(R.id.txt_module_cyclecount);
       txt_module_GI=findViewById(R.id.txt_module_GI);
       createOrder = findViewById(R.id.txt_add_order);
       txt_ScanBarCode=findViewById(R.id.txt_ScanBarCode);
       txt_ReturnItem=findViewById(R.id.txt_ReturnItem);
       txt_item_availabilty=findViewById(R.id.txt_item_availabilty);
       txt_promotion=findViewById(R.id.txt_promotion);
       txt_item_stock=findViewById(R.id.txt_item_stock);

        List<Users> userdataList = databaseHelper.getUserData();

       preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       preferencesEditor = preferences.edit();


       if (isFirstRun("updateVersion", GetVersionOfApp())) {
           Details(userdataList.get(0).getUser_Name1().trim());
       }
   }

    private void loadingModules()
    {
        List<Groups> modulesID = databaseHelper.Get_Groups();
        for (int i=0;i<modulesID.size();i++){

            if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("2")){
                txt_module_recieving.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("4")){
                txt_item_availabilty.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("5")){
                txt_ScanBarCode.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("6")){
                txt_module_transafer.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("7")){
                txt_module_cyclecount.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("9")){
                txt_ReturnItem.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("11")){
                txt_promotion.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("19")){
                txt_module_GI.setVisibility(View.VISIBLE);
            }
            else if (modulesID.get(i).getGroup_ID1().equalsIgnoreCase("21")){
                txt_item_stock.setVisibility(View.VISIBLE);
            }
        }
    }


    private void navigateActivity(Class c, TextView module)
    {
        module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, c);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed()
    {
        Intent Go_Back= new Intent(MainActivity.this,LoginActivity.class);
        Go_Back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Go_Back);
        super.onBackPressed();
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    public void Details(final String userName)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Constant.Details_for_Update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            Log.d("onResponse", status);

                            String message = object.getString("message");


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
                Map<String, String> params = new HashMap<>();
                params.put("PocketPCMAC", Device_id_Instance_of_MacAdress);
                params.put("Version", versionName);
                params.put("Modified_date", Date);
                params.put("UserName", userName);


                return params;
            }

        };


        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        queue.add(request);

    }


    public boolean isFirstRun(String forWhat ,String Versioncode)
    {
        Log.d("aaavvPr", " " + preferences.getString(forWhat, "1.0"));

        if (Double.valueOf(preferences.getString(forWhat, "1.0")) <Double.valueOf((Versioncode) )) {
            preferencesEditor.putString(forWhat, Versioncode).commit();
            Log.d("aaavvPr_PU", " " + Versioncode);

            return true;
        } else {
            return false;
        }
    }


    private String GetVersionOfApp()
    {
        String Version = "0.0";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
            Log.d("aaavvvvv", "checkVersion.DEBUG: App version: " + Version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Version;
    }

}