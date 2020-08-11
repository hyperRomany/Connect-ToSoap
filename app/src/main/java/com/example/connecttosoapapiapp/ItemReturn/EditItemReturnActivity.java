package com.example.connecttosoapapiapp.ItemReturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.R;

import java.util.ArrayList;
import java.util.List;

public class EditItemReturnActivity extends AppCompatActivity {
    DatabaseHelperForItemReturn databaseHelperForItemReturn;
    List<Item_Return_Search> sTo_searchList;
    TextView txt_barcode,txt_descripation,txt_unite_item;
    EditText edit_last_deliver;
    String TotalavailbleQty;
    String issCheckedIssLog, issCheckedBarcode,issCheckedRecLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_return);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode = findViewById(R.id.txt_barcode);
        txt_descripation = findViewById(R.id.txt_descripation);
        txt_unite_item = findViewById(R.id.txt_unite_item);
        edit_last_deliver = findViewById(R.id.edit_last_deliver);
        Intent getdat=getIntent();
        issCheckedBarcode = getdat.getExtras().getString("Barcode");
        Log.e("issCheckedBarcode",""+issCheckedBarcode);

        sTo_searchList=new ArrayList<>();

        databaseHelperForItemReturn=new DatabaseHelperForItemReturn(this);
        sTo_searchList = databaseHelperForItemReturn.Search_if_Barcode_in_localDataBase(issCheckedBarcode);

        txt_barcode.setText(sTo_searchList.get(0).getGTIN1());
        txt_descripation.setText(sTo_searchList.get(0).getDesc1());
        edit_last_deliver.setText(sTo_searchList.get(0).getQTY1());
            txt_unite_item.setText(sTo_searchList.get(0).getUOM1());

//        Log.e("kjkfdskj",""+sTo_searchList.get(0).getQTY1());
//        TotalavailbleQty=sTo_searchList.get(0).getAVAILABLE_STOCK1();

    }


    public void SaveUpdateQTY(View view) {
        if (!edit_last_deliver.getText().toString().isEmpty() ){
            databaseHelperForItemReturn.Update_Sto_search_For_QTY(edit_last_deliver.getText().toString(),issCheckedBarcode);
            edit_last_deliver.setHint("تم");
            Intent Go_Back= new Intent(EditItemReturnActivity.this , ShowItemsReturnItemActivity.class);
//            Go_Back.putExtra("Iss_Stg_Log",sTo_searchList.get(0).getISS_STG_LOG1());
//            Go_Back.putExtra("Rec_Stg_Log",sTo_searchList.get(0).getREC_SITE_LOG1());
            startActivity(Go_Back);
        }else {
            edit_last_deliver.setError("من فضلك أدخل الكمية"+TotalavailbleQty);
        }

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(EditItemReturnActivity.this , ShowItemsReturnItemActivity.class);
//        Go_Back.putExtra("Iss_Stg_Log",issCheckedIssLog);
//        Go_Back.putExtra("Rec_Stg_Log",issCheckedRecLog);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
