package com.example.connecttosoapapiapp.TransfereModule;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import java.util.ArrayList;
import java.util.List;

public class Edit_Order_DataActivity extends AppCompatActivity {
DatabaseHelperForTransfer databaseHelperForTransfer;
List<STo_Search> sTo_searchList;
    TextView txt_barcode,txt_descripation,txt_unite_item;
    EditText edit_last_deliver;
    String TotalavailbleQty;
    String issCheckedIssLog, issCheckedBarcode,issCheckedRecLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manual__transfer__order_show_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode = findViewById(R.id.txt_barcode);
        txt_descripation = findViewById(R.id.txt_descripation);
        txt_unite_item = findViewById(R.id.txt_unite_item);
        edit_last_deliver = findViewById(R.id.edit_last_deliver);
        Intent getdat=getIntent();
        issCheckedBarcode = getdat.getExtras().getString("Barcode");
        Log.e("issCheckedBarcode",""+issCheckedBarcode);
        issCheckedIssLog = getdat.getExtras().getString("Iss_Stg_Log");
        Log.e("issCheckedBarcode",""+issCheckedIssLog);
        issCheckedRecLog = getdat.getExtras().getString("Rec_Stg_Log");
        Log.e("issCheckedBarcode",""+issCheckedRecLog);

        sTo_searchList=new ArrayList<>();

        databaseHelperForTransfer=new DatabaseHelperForTransfer(this);
        sTo_searchList = databaseHelperForTransfer.Search_if_Barcode_in_localDataBase( issCheckedIssLog ,
                issCheckedRecLog, issCheckedBarcode );

        Log.e("kjkfdskj",""+sTo_searchList.get(0).getMEINH1());
        txt_barcode.setText(sTo_searchList.get(0).getGTIN1());
        txt_descripation.setText(sTo_searchList.get(0).getUOM_DESC1());
        txt_unite_item.setText(sTo_searchList.get(0).getMEINH1());
        edit_last_deliver.setText(sTo_searchList.get(0).getQTY1());
        Log.e("kjkfdskj",""+sTo_searchList.get(0).getQTY1());
        TotalavailbleQty=sTo_searchList.get(0).getAVAILABLE_STOCK1();

    }


    public void SaveUpdateQTY(View view) {
        if (Double.valueOf(edit_last_deliver.getText().toString()) <= Double.valueOf(TotalavailbleQty)  ){
            databaseHelperForTransfer.Update_Sto_search_For_QTY(edit_last_deliver.getText().toString(),issCheckedIssLog,
                    issCheckedRecLog,issCheckedBarcode);
            edit_last_deliver.setText("Done");
            Intent Go_Back= new Intent(Edit_Order_DataActivity.this , ShowOrderDataActivity.class);
            Go_Back.putExtra("Iss_Stg_Log",sTo_searchList.get(0).getISS_STG_LOG1());
            Go_Back.putExtra("Rec_Stg_Log",sTo_searchList.get(0).getREC_SITE_LOG1());
            startActivity(Go_Back);
        }else {
            edit_last_deliver.setError("هذه الكميه أكبر من المتاح بالمخزن .. المتاح"+TotalavailbleQty);
        }

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(Edit_Order_DataActivity.this , ShowOrderDataActivity.class);
        Go_Back.putExtra("Iss_Stg_Log",issCheckedIssLog);
        Go_Back.putExtra("Rec_Stg_Log",issCheckedRecLog);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
