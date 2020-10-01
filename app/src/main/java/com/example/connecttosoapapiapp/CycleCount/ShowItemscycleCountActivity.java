package com.example.connecttosoapapiapp.CycleCount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connecttosoapapiapp.CycleCount.Adapter.ItemsToEditAdapter;
import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowItemscycleCountActivity extends AppCompatActivity {
    ItemsToEditAdapter itemsToEditAdapter;
    List<Po_Item_of_cycleCount> Po_Item_For_Recycly;
    DatabaseHelperForCycleCount databaseHelperForCycleCount;
    RecyclerView recyclerView;
    EditText edit_Barcode;
    int CountChecked=0;
    String BarCodeToDelete="";
    String BarCodeChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_cyclecount);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =findViewById(R.id.recycle_items_view);
        edit_Barcode=findViewById(R.id.edit_Barcode);

        Po_Item_For_Recycly = new ArrayList<>();
        databaseHelperForCycleCount =new DatabaseHelperForCycleCount(this);

        Po_Item_For_Recycly = databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();
        CreateORUpdateRecycleView();


    }

    public void CreateORUpdateRecycleView(){


        itemsToEditAdapter = new ItemsToEditAdapter(Po_Item_For_Recycly);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemsToEditAdapter);

    }

    public void Search_BarCodeOfOfRecycleview(View view) {
        if (edit_Barcode.getText().toString().isEmpty()){
            edit_Barcode.setError("من فضلك ادخل الباركود");
        }else {
            Po_Item_For_Recycly = new ArrayList<>();

            Po_Item_For_Recycly = databaseHelperForCycleCount.Search_Barcode_in_localDataBase_And_Has_QTy(edit_Barcode.getText().toString());

            if (Po_Item_For_Recycly.size() == 0){
                edit_Barcode.setError("الباركود غير موجود");
            }else {
               // edit_Barcode.setError("الباركود  موجود");
                CreateORUpdateRecycleView();
            }
        }

    }

    public void Delete_PDNEWQTY(View view) {

        List<Po_Item_of_cycleCount> Po_Item_List = itemsToEditAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    CountChecked += 1;
                    BarCodeToDelete =Po_Item_List.get(i).getEAN111();


                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemscycleCountActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()

                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Log.e("btn_editCheckedif",""+BarCodeToDelete);

                                        databaseHelperForCycleCount.Update_QTY("0.0",BarCodeToDelete.replace(" ",""));

                                        Po_Item_For_Recycly = new ArrayList<>();
                                        databaseHelperForCycleCount =new DatabaseHelperForCycleCount(ShowItemscycleCountActivity.this);

                                        Po_Item_For_Recycly = databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();

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
            Toast.makeText(ShowItemscycleCountActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();

    }

    public void Edit_PDNEWQTY(View view) {
        //مافيش colum خاص ب checkbox وكله بيجى ب false
        //ممكن نخلى الموديول هو اللى يشيل الداتا
        List<Po_Item_of_cycleCount> Po_Item_List = itemsToEditAdapter.ReturnListOfItems();
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

                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemscycleCountActivity.this, "لقد اخترت اكثر من اختيار أو لم تقم تختار ", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent GoToEdit = new Intent(ShowItemscycleCountActivity.this, EditItemForCycleCountActivity.class);
                        GoToEdit.putExtra("BarCode",BarCodeChecked.replace(" ",""));

                        Log.e("BarCodeChecked",""+BarCodeChecked.replace(" ",""));

                        startActivity(GoToEdit);
                    }
                }
            }

        }else
            Toast.makeText(ShowItemscycleCountActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("dsdsdsdsdsdsds","dsdsdsdsds");

        Po_Item_For_Recycly = new ArrayList<>();
        databaseHelperForCycleCount =new DatabaseHelperForCycleCount(this);

        Po_Item_For_Recycly = databaseHelperForCycleCount.Get_Items_That_Has_PDNewQTy();
        CreateORUpdateRecycleView();

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ShowItemscycleCountActivity.this, ScanActivity.class);
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
