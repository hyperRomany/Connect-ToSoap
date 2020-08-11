package com.example.connecttosoapapiapp.ReceivingModule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class EditItemActivity extends AppCompatActivity {
    List<Po_Item> Po_Item_list;
    DatabaseHelper databaseHelper;
    Double Current_U_Deliver;
    TextView txt_barcode,txt_descripation,txt_unite_item;
    EditText edit_last_deliver;
    String Barcode , TotalQuantity, PO_UNIT;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent getDta= getIntent();
        Barcode = getDta.getExtras().getString("BarCode");
        TotalQuantity = getDta.getExtras().getString("QUANTITY");
        PO_UNIT = getDta.getExtras().getString("PO_UNIT");
        Log.e("zzPO_UNIT",""+PO_UNIT);

        Po_Item_list = new ArrayList<>();

        databaseHelper =new DatabaseHelper(this);

        Po_Item_list = databaseHelper.Search_Barcode_with_UOM(Barcode,PO_UNIT);

        txt_barcode = findViewById(R.id.txt_barcode);
        txt_descripation = findViewById(R.id.txt_descripation);
        txt_unite_item = findViewById(R.id.txt_unite_item);
        edit_last_deliver = findViewById(R.id.edit_last_deliver);
        edit_last_deliver.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        ||keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    SaveUpdatePDNEWQTY(view);
                }

                return false;
            }
        });
        Log.e("BarCodeChecked",""+Po_Item_list.get(0).getSHORT_TEXT1());
        Log.e("BarCodeChecked",""+Po_Item_list.get(0).getMEINH1());
        Log.e("BarCodeChecked",""+Po_Item_list.get(0).getPDNEWQTY1());
        txt_barcode.setText(Barcode);
        txt_descripation.setText(Po_Item_list.get(0).getSHORT_TEXT1());
        txt_unite_item.setText(Po_Item_list.get(0).getMEINH1());
        edit_last_deliver.setText(Po_Item_list.get(0).getPDNEWQTY1());
    }

    public void SaveUpdatePDNEWQTY(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String DeliveryDate = sdf.format(new Date());

        if (edit_last_deliver.getText().toString().isEmpty()){
            edit_last_deliver.setError("من فضلك أدخل الكميه");
        }else {
            Double DTotalQuantity = Double.valueOf(TotalQuantity);
            Log.d("TotalDeliver", "" + DTotalQuantity);

            Current_U_Deliver = Double.valueOf(edit_last_deliver.getText().toString());
            Log.d("TotalDeliver", "" + Current_U_Deliver);

            Double TotalDeliver = DTotalQuantity - Current_U_Deliver;

            if (TotalDeliver < 0) {
                edit_last_deliver.setError("هذه الكميه أكثر من المطلوب" /*+DTotalQuantity*/);
            } else {

                int ID = databaseHelper.update_PDNEWQTY(Po_Item_list.get(0).getID1(),
                        String.valueOf(new DecimalFormat("###.##").format(Current_U_Deliver)),
                        DeliveryDate /*, VendorName*/);

                edit_last_deliver.setText("");
                edit_last_deliver.setHint("Done");
                edit_last_deliver.setHintTextColor(getResources().getColor(R.color.first_blue));

                String SeralForMaterial = Po_Item_list.get(0).getSERNP1();

                if (SeralForMaterial.equalsIgnoreCase("anytype{}") ||
                        SeralForMaterial.equalsIgnoreCase("")) {
                    Toast.makeText(EditItemActivity.this, "لايوجد سيريال لهذا الباركود", Toast.LENGTH_LONG).show();
                } else {
                    Intent GoToAddSerialOfThisMaterail = new Intent(EditItemActivity.this, SerialViewFormActivity.class);
                    GoToAddSerialOfThisMaterail.putExtra("PDNEWQTY", Po_Item_list.get(0).getPDNEWQTY1());
                    GoToAddSerialOfThisMaterail.putExtra("barCode", Barcode);
                    Log.d("putExtra", "" + Po_Item_list.get(0).getEAN111());
                    GoToAddSerialOfThisMaterail.putExtra("Po_Item_of_cycleCount", Po_Item_list.get(0).getPO_ITEM1());
                    Log.d("putExtra", "" + Po_Item_list.get(0).getPO_ITEM1());
                    GoToAddSerialOfThisMaterail.putExtra("Material", Po_Item_list.get(0).getMATERIAL1());
                    GoToAddSerialOfThisMaterail.putExtra("ShortText", Po_Item_list.get(0).getSHORT_TEXT1());
                    Log.d("putExtra", "" + Po_Item_list.get(0).getMATERIAL1());
                    startActivity(GoToAddSerialOfThisMaterail);
                }
                // Intent GoTocheckItems = new Intent(EditGIItemActivity.this, CheckItemFormActivity.class);
                // startActivity(GoTocheckItems);
            }
            Log.d("TotalDeliver", "" + TotalDeliver);
        }
    }


    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(EditItemActivity.this,CheckItemFormActivity.class);
        startActivity(Go_Back);
       /* List<PO_SERIAL> po_serialslist = new ArrayList<>();

        Log.e("onBackPressedlog",""+Current_U_Deliver);
        Log.e("onBackPressedlogs",""+po_serialslist.size());
        po_serialslist = databaseHelper.selectPo_Serials(Barcode);
        if (Current_U_Deliver ==  po_serialslist.size()){
            Intent Go_Back= new Intent(EditGIItemActivity.this,CheckItemFormActivity.class);
            startActivity(Go_Back);
           // super.onBackPressed();
        }else {
            edit_last_deliver.setError("عدد السيريالات اكبر أو أقل من الكميه المكتوبه  .. قم بتعديلها اولا");
           // super.onBackPressed();
        }*/

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void EditSerial(View view) {
        String SeralForMaterial  = Po_Item_list.get(0).getSERNP1();

        if (SeralForMaterial.equalsIgnoreCase("anytype{}") ||
                SeralForMaterial.equalsIgnoreCase("")){
            Toast.makeText(EditItemActivity.this,"لا يوجد سيريال لهذا الباركود",Toast.LENGTH_LONG).show();
        }else {
            Intent GoToSerialForm=new Intent(EditItemActivity.this,SerialViewFormActivity.class);
            GoToSerialForm.putExtra("PDNEWQTY",Po_Item_list.get(0).getPDNEWQTY1());
            GoToSerialForm.putExtra("barCode",Barcode);
            GoToSerialForm.putExtra("Po_item",Po_Item_list.get(0).getPO_ITEM1());
            //Log.d("Po_Item_list",""+Po_Item_list.get(0).getEAN111());
            GoToSerialForm.putExtra("Material",Po_Item_list.get(0).getMATERIAL1());
            //Log.d("Po_Item_list",""+Po_Item_list.get(0).getMATERIAL1());
            startActivity(GoToSerialForm);
        }
    }
}
