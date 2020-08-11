package com.example.connecttosoapapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.connecttosoapapiapp.CycleCount.CycleCountActivity;
import com.example.connecttosoapapiapp.GIModule.MainFormOfGIActivity;
import com.example.connecttosoapapiapp.ItemAvailability.ScanItemAvailabilityActivity;
import com.example.connecttosoapapiapp.ItemReturn.MainItemReturnActivity;
import com.example.connecttosoapapiapp.Promotion.MainPromotionActivity;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.ReceivingActivity;
import com.example.connecttosoapapiapp.ReceivingModule.model.Groups;
import com.example.connecttosoapapiapp.ScanBarcode.ScanBarcodActivity;
import com.example.connecttosoapapiapp.TransfereModule.FormTransferActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView txt_module_recieving,txt_module_transafer,txt_module_cyclecount
            ,txt_module_GI,txt_ScanBarCode,txt_ReturnItem,txt_item_availabilty,
            txt_promotion;
    DatabaseHelper databaseHelper;
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

        VisableModules();
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

}