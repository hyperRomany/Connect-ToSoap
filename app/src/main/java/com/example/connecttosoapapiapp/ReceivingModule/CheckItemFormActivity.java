package com.example.connecttosoapapiapp.ReceivingModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Adapter.ItemDelieveredFormAdapter;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckItemFormActivity extends AppCompatActivity {

   // private List<Po_Item_of_cycleCount> po_items = new ArrayList<>();

   List<Po_Item> Po_Item_For_Recycly;

    private RecyclerView recyclerView;
    private ItemDelieveredFormAdapter itemDelieveredFormAdapter;
    DatabaseHelper databaseHelper;
    Button btn_edit, btn_delete;
    int CountChecked;
    String BarCodeChecked, ToTalQuantity, PO_UNIT , BarCodeToDelete, PO_UNITOfDelete, DeliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_item_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       // Po_Item_For_Recycly = new ArrayList<>();

        recyclerView =  findViewById(R.id.recycle_items_view);

        btn_edit=findViewById(R.id.btn_edit);
        btn_delete=findViewById(R.id.btn_delete);
    }

    @Override
    protected void onPause() {
        Log.e("lifecycle","onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("lifecycle","onResume");
        CreateORUpdateRecycleView();
        super.onResume();
    }

    public void CreateORUpdateRecycleView(){
        Po_Item_For_Recycly = new ArrayList<>();
        databaseHelper =new DatabaseHelper(this);

        Po_Item_For_Recycly = databaseHelper.Get_Po_Item_That_Has_PDNewQTy();

        itemDelieveredFormAdapter = new ItemDelieveredFormAdapter(Po_Item_For_Recycly);

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
    protected void onStop() {
        Log.e("lifecycle","onStop");
        super.onStop();

    }

    @Override
    protected void onRestart() {
        Log.e("lifecycle","onRestart");
        super.onRestart();
    }

    public void Delete_PDNEWQTY(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DeliveryDate = sdf.format(new Date());
        List<Po_Item> Po_Item_List = itemDelieveredFormAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeToDelete =Po_Item_List.get(i).getEAN111();
                    PO_UNITOfDelete = Po_Item_List.get(i).getPO_UNIT1();

                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(CheckItemFormActivity.this, "لقد اخترت اكثر من اختيار أو لم تختار شى", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()

                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelper.update_PDNEWQTYByBarcode(BarCodeToDelete,"0.0",DeliveryDate,PO_UNITOfDelete);
                                        databaseHelper.DeleteSerialTable();
                                        CreateORUpdateRecycleView();
                                    }
                                })
                                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }
            }

        }else
            Toast.makeText(CheckItemFormActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();

    }

    public void Edit_PDNEWQTY(View view) {
        //مافيش colum خاص ب checkbox وكله بيجى ب false
        //ممكن نخلى الموديول هو اللى يشيل الداتا
        List<Po_Item> Po_Item_List = itemDelieveredFormAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =Po_Item_List.get(i).getEAN111();
                    ToTalQuantity = Po_Item_List.get(i).getQUANTITY1();
                    PO_UNIT = Po_Item_List.get(i).getPO_UNIT1();
                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(CheckItemFormActivity.this, "لقد اخترت اكثر من اختيار أو لم تختار شى", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent GoToEdit = new Intent(CheckItemFormActivity.this, EditItemActivity.class);
                        GoToEdit.putExtra("BarCode",BarCodeChecked);
                        GoToEdit.putExtra("QUANTITY",ToTalQuantity);
                        GoToEdit.putExtra("PO_UNIT",PO_UNIT);
                        Log.e("BarCodeChecked",""+BarCodeChecked);

                        startActivity(GoToEdit);
                    }
                }
            }

        }else
            Toast.makeText(CheckItemFormActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(CheckItemFormActivity.this,ScanRecievingActivity.class);
        Go_Back.putExtra("This Is First Time",false);

        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
