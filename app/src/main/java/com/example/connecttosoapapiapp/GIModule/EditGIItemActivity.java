package com.example.connecttosoapapiapp.GIModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connecttosoapapiapp.GIModule.Helper.GIDataBaseHelper;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.TransfereModule.Edit_Order_DataActivity;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.ShowOrderDataActivity;

import java.util.ArrayList;
import java.util.List;

public class EditGIItemActivity extends AppCompatActivity {
    GIDataBaseHelper giDataBaseHelper;
    TextView txt_barcode,txt_descripation,txt_unite_item;
    EditText edit_last_deliver;
    String TotalavailbleQty;
    String issCheckedBarcode, GL,CS,Site;
    List<GIModule> giModuleList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gi_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode = findViewById(R.id.txt_barcode);
        txt_descripation = findViewById(R.id.txt_descripation);
        txt_unite_item = findViewById(R.id.txt_unite_item);
        edit_last_deliver = findViewById(R.id.edit_last_deliver);
        Intent GetData=getIntent();
        if (GetData.getExtras() !=null) {
            GL = GetData.getExtras().getString("GL");
            CS = GetData.getExtras().getString("CostCenter");
            Site = GetData.getExtras().getString("Site");
            issCheckedBarcode= GetData.getExtras().getString("BarCode");
        }
        giModuleList=new ArrayList<>();

        giDataBaseHelper=new GIDataBaseHelper(this);
        giModuleList = giDataBaseHelper.Search__Barcode(  issCheckedBarcode );

        Log.e("kjkfdskj",""+giModuleList.get(0).getMEINH1());
        txt_barcode.setText(giModuleList.get(0).getGTIN1());
        txt_descripation.setText(giModuleList.get(0).getUOM_DESC1());
        txt_unite_item.setText(giModuleList.get(0).getMEINH1());
        edit_last_deliver.setText(giModuleList.get(0).getQTY1());
        Log.e("kjkfdskj",""+giModuleList.get(0).getQTY1());
        TotalavailbleQty=giModuleList.get(0).getAVAILABLE_STOCK1();

    }


    public void SaveUpdateQTY(View view) {
        if (Double.valueOf(edit_last_deliver.getText().toString()) <= Double.valueOf(TotalavailbleQty)  ){
            giDataBaseHelper.Update_QTY(edit_last_deliver.getText().toString(),issCheckedBarcode);
            edit_last_deliver.setText("Done");
            Intent Go_Back= new Intent(EditGIItemActivity.this , ShowItemsForGIActivity.class);
            Go_Back.putExtra("GL",GL);
            Go_Back.putExtra("CostCenter",CS);
            Go_Back.putExtra("Site",Site);
            startActivity(Go_Back);
        }else {
            edit_last_deliver.setError("هذه الكميه أكبر من المتاح بالمخزن .. المتاح"+TotalavailbleQty);
        }

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(EditGIItemActivity.this , ShowItemsForGIActivity.class);
        Go_Back.putExtra("GL",GL);
        Go_Back.putExtra("CostCenter",CS);
        Go_Back.putExtra("Site",Site);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
