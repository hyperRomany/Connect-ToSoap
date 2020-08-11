package com.example.connecttosoapapiapp.GIModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connecttosoapapiapp.CycleCount.EditItemForCycleCountActivity;
import com.example.connecttosoapapiapp.GIModule.Adapter.GiEditItemsAdapter;
import com.example.connecttosoapapiapp.GIModule.Helper.GIDataBaseHelper;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.R;

import java.util.ArrayList;
import java.util.List;

public class ShowItemsForGIActivity extends AppCompatActivity {
    GiEditItemsAdapter giEditItemsAdapter;
    List<GIModule> Po_Item_For_Recycly;
    GIDataBaseHelper giDataBaseHelper;
    RecyclerView recyclerView;
    EditText edit_Barcode;
    int CountChecked=0;
    String BarCodeToDelete="";
    String BarCodeChecked, GL,CS,Site;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =findViewById(R.id.recycle_items_view_transfer);
        edit_Barcode=findViewById(R.id.edit_Barcode);

        Intent GetData=getIntent();
        if (GetData.getExtras() !=null) {
            GL = GetData.getExtras().getString("GL");
            CS = GetData.getExtras().getString("CostCenter");
            Site = GetData.getExtras().getString("Site");

        }


        Po_Item_For_Recycly = new ArrayList<>();
        giDataBaseHelper =new GIDataBaseHelper(this);

        Po_Item_For_Recycly = giDataBaseHelper.selectGIModule_for_Qty();
        CreateORUpdateRecycleView();


    }

    public void CreateORUpdateRecycleView(){


        giEditItemsAdapter = new GiEditItemsAdapter(Po_Item_For_Recycly);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(giEditItemsAdapter);

    }



    public void Delete_PDNEWQTY(View view) {

        List<GIModule> Po_Item_List = giEditItemsAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    CountChecked += 1;
                    BarCodeToDelete =Po_Item_List.get(i).getGTIN1();


                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemsForGIActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()

                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Log.e("btn_editCheckedif",""+BarCodeToDelete);

                                        giDataBaseHelper.Update_QTY("0.0",BarCodeToDelete.replace(" ",""));

                                        Po_Item_For_Recycly = new ArrayList<>();

                                        Po_Item_For_Recycly = giDataBaseHelper.selectGIModule_for_Qty();

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
            Toast.makeText(ShowItemsForGIActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();

    }

    public void Edit_PDNEWQTY(View view) {
        //مافيش colum خاص ب checkbox وكله بيجى ب false
        //ممكن نخلى الموديول هو اللى يشيل الداتا
        List<GIModule> Po_Item_List = giEditItemsAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =Po_Item_List.get(i).getGTIN1();

                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemsForGIActivity.this, "لقد اخترت اكثر من اختيار أو لم تقم تختار ", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent GoToEdit = new Intent(ShowItemsForGIActivity.this, EditGIItemActivity.class);
                        GoToEdit.putExtra("GL",GL);
                        GoToEdit.putExtra("CostCenter",CS);
                        GoToEdit.putExtra("Site",Site);
                        GoToEdit.putExtra("BarCode",BarCodeChecked.replace(" ",""));

                        Log.e("BarCodeChecked",""+BarCodeChecked.replace(" ",""));

                        startActivity(GoToEdit);
                    }
                }
            }

        }else
            Toast.makeText(ShowItemsForGIActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("dsdsdsdsdsdsds","dsdsdsdsds");

        Po_Item_For_Recycly = new ArrayList<>();
        giDataBaseHelper =new GIDataBaseHelper(this);

        Po_Item_For_Recycly = giDataBaseHelper.selectGIModule_for_Qty();
        CreateORUpdateRecycleView();

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ShowItemsForGIActivity.this, ScanGIActivity.class);
        Go_Back.putExtra("GL",GL);
        Go_Back.putExtra("CostCenter",CS);
        Go_Back.putExtra("Site",Site);
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
