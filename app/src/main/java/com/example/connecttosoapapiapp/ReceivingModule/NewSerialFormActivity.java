package com.example.connecttosoapapiapp.ReceivingModule;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ReceivingModule.Adapter.ItemOfPoSerialsAdapter;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.model.PO_SERIAL;

import java.util.ArrayList;
import java.util.List;

public class NewSerialFormActivity extends AppCompatActivity {
List<PO_SERIAL> po_serialslist;
    private DatabaseHelper databaseHelper;

    TextView txt_barcode,txt_quantity_for_serial,txt_descripation_for_serial_form;
      EditText edit_current_serials ;
    String PDNEWQTY,BarCode,Material,Po_item,Shorttext;
    RecyclerView recyclerView;
    ItemOfPoSerialsAdapter itemDelieveredFormAdapter;
int i;
    Double v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_serial_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode =findViewById(R.id.txt_barcode_form);
        txt_quantity_for_serial=findViewById(R.id.txt_quantity_for_serial_form);
        edit_current_serials=findViewById(R.id.edit_current_serials_form);
        txt_descripation_for_serial_form=findViewById(R.id.txt_descripation_for_serial_form);
        recyclerView =  findViewById(R.id.recycle_serials_view_form);

        databaseHelper =new DatabaseHelper(this);

        po_serialslist = new ArrayList<>();

        Intent GetData=getIntent();
         PDNEWQTY=GetData.getExtras().getString("PDNEWQTY");
         BarCode=GetData.getExtras().getString("barCode");
         Material=GetData.getExtras().getString("Material");
        Po_item=GetData.getExtras().getString("Po_Item_of_cycleCount");
        Shorttext=GetData.getExtras().getString("ShortText");

        txt_barcode.setText(BarCode);
        txt_quantity_for_serial.setText(PDNEWQTY);
        txt_descripation_for_serial_form.setText(Shorttext);
        i=0;
        v=Double.valueOf(PDNEWQTY);

        CreateORUpdateRecycleView();
        /*po_serialslist=new ArrayList<>();
        databaseHelper=new DatabaseHelperForTransfer(this);
        po_serialslist.add(new PO_SERIAL())*/
    }

    public void ADDSerial(View view) {
        if (!edit_current_serials.getText().toString().isEmpty()) {
            String FindOrNot = databaseHelper.serach_for_Serial(edit_current_serials.getText().toString());

            if (FindOrNot.equalsIgnoreCase("true")) {
                 v = Double.valueOf(PDNEWQTY);
                po_serialslist = databaseHelper.selectPo_Serials(BarCode);
                i=po_serialslist.size();
                Log.e("edit_current_serials",""+v+"$$"+po_serialslist.size());

                if (v > i) {
                    Log.d("Po_item", "" + Po_item);
                    databaseHelper.insertPo_Serials(Material, Po_item, BarCode, edit_current_serials.getText().toString());
                    CreateORUpdateRecycleView();
                    edit_current_serials.setText("");
                    i++;
                    v--;
                } else
                    Toast.makeText(this, "لقد أدخلت كل السيريال المطلوب", Toast.LENGTH_SHORT).show();
            }else {
                edit_current_serials.setError("لقد أدخلت هذا السيريال من قبل");
                edit_current_serials.setText("");
            }
        }else {
            edit_current_serials.setError("من فضلك ادخل السيريال");
        }
    }

    public void CreateORUpdateRecycleView(){
       // po_serialslist = new ArrayList<>();

        po_serialslist = databaseHelper.selectPo_Serials(BarCode);
        itemDelieveredFormAdapter = new ItemOfPoSerialsAdapter(po_serialslist);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemDelieveredFormAdapter);
    }

    @Override
    public void onBackPressed() {
        v=Double.valueOf(PDNEWQTY);
        po_serialslist = databaseHelper.selectPo_Serials(BarCode);
        Log.e("edit_current_serials",""+v+"$$"+po_serialslist.size());
        if (v <= (po_serialslist.size()) ){
            Intent Go_Back = new Intent(NewSerialFormActivity.this, ScanRecievingActivity.class);
            Go_Back.putExtra("This Is First Time", false);
            startActivity(Go_Back);
           // super.onBackPressed();

        }else {
            edit_current_serials.setError("من فضلك أدخل كل السيرالات");
           // super.onBackPressed();
        }
    }

    //remove this for back arrow in navigation bar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
