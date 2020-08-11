package com.example.connecttosoapapiapp.CycleCount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;

import java.util.ArrayList;
import java.util.List;

public class EditItemForCycleCountActivity extends AppCompatActivity {
TextView txt_barcode,txt_descripation,txt_unite_item;
    EditText edit_QTY;
    DatabaseHelperForCycleCount databaseHelperForCycleCount;
    List<Po_Item_of_cycleCount> Po_Item_For_Recycly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_edit_item_for_cycle_count);
        txt_barcode=findViewById(R.id.txt_barcode);
        txt_descripation=findViewById(R.id.txt_descripation);
        txt_unite_item=findViewById(R.id.txt_unite_item);

        edit_QTY =findViewById(R.id.edit_qty);

        txt_barcode.setText(getIntent().getExtras().getString("BarCode"));

        Log.e("txt_barcode",""+getIntent().getExtras().getString("BarCode"));

        databaseHelperForCycleCount=new DatabaseHelperForCycleCount(this);

        Po_Item_For_Recycly = new ArrayList<>();

        Po_Item_For_Recycly = databaseHelperForCycleCount.Search_Barcode_in_localDataBase_And_Has_QTy(txt_barcode.getText().toString());


        txt_descripation.setText(Po_Item_For_Recycly.get(0).getMAKTX1());
        txt_unite_item.setText(Po_Item_For_Recycly.get(0).getMEINH1());

    }

    public void SaveUpdatePDNEWQTY(View view) {
        if (edit_QTY.getText().toString().isEmpty()){
            edit_QTY.setError("من فضلك ادخل الكميه");
        }else {
            databaseHelperForCycleCount.Update_QTY(edit_QTY.getText().toString(),txt_barcode.getText().toString());
            edit_QTY.setText("");
            edit_QTY.setHint("Done");
        }
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(EditItemForCycleCountActivity.this, ShowItemscycleCountActivity.class);
        Go_Back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
