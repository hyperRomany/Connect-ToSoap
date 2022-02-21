package com.example.connecttosoapapiapp.TransfereModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.TransfereModule.Adapter.Itemtransfer_for_searchAdapter;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;
import com.example.connecttosoapapiapp.makeOrder.CreateOrderSearchActivity;

import java.util.ArrayList;
import java.util.List;

public class ShowOrderDataActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btn_edit, btn_delete;
    private DatabaseHelperForTransfer databaseHelperForTransfer;
    private List<STo_Search> sTo_searchList;
    private Itemtransfer_for_searchAdapter itemtransfer_for_searchAdapter;
    private int CountChecked;
    private boolean makeOrder = false;
    private String issChecked, BarCodeChecked, RecChecked, Department, FromSite, ToSite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual__transfer__order_show_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =  findViewById(R.id.recycle_items_view_transfer);

        btn_edit=findViewById(R.id.btn_edit_transfer);

        btn_delete=findViewById(R.id.btn_delete_transfer);

        CreateORUpdateRecycleView();
        Intent getdat=getIntent();
        issChecked = getdat.getExtras().getString("Iss_Stg_Log");
        RecChecked = getdat.getExtras().getString("Rec_Stg_Log");
        makeOrder = getdat.getExtras().getBoolean("MakeOrder");
        Department = getdat.getExtras().getString("Department");
        FromSite = getdat.getExtras().getString("FromSite");
        ToSite = getdat.getExtras().getString("ToSite");

    }

    public void Delete_PDNEWQTY(View view) {
       List<STo_Search> sTo_searchListfordelete =new ArrayList<>();
        sTo_searchListfordelete = itemtransfer_for_searchAdapter.ReturnListOfItems();

        CountChecked =0;
        if (sTo_searchListfordelete.size() != 0) {
            for (int i = 0; i < sTo_searchListfordelete.size(); i++) {
                Boolean Checked = sTo_searchListfordelete.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =sTo_searchListfordelete.get(i).getGTIN1();

                }if (i == (sTo_searchListfordelete.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowOrderDataActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()

                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        databaseHelperForTransfer.DeleteBarcodeStosearchTable(BarCodeChecked);
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
            Toast.makeText(ShowOrderDataActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();

    }

    public void CreateORUpdateRecycleView(){
        databaseHelperForTransfer =new DatabaseHelperForTransfer(this);
        sTo_searchList=new ArrayList<>();
        sTo_searchList = databaseHelperForTransfer.selectSto_Search_for_Qty();

        itemtransfer_for_searchAdapter = new Itemtransfer_for_searchAdapter(sTo_searchList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemtransfer_for_searchAdapter);

    }

    public void Edit_PDNEWQTY(View view) {

        List<STo_Search> sTo_searchListfordelete = new ArrayList<>();
        sTo_searchListfordelete = itemtransfer_for_searchAdapter.ReturnListOfItems();

        CountChecked =0;
        if (sTo_searchListfordelete.size() != 0) {
            for (int i = 0; i < sTo_searchListfordelete.size(); i++) {
                Boolean Checked = sTo_searchListfordelete.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =sTo_searchListfordelete.get(i).getGTIN1();
                    //BarCodeChecked =sTo_searchListfordelete.get(i).getGTIN1();

                }if (i == (sTo_searchListfordelete.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowOrderDataActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent gotoedit = new Intent(ShowOrderDataActivity.this, Edit_Order_DataActivity.class);

                        gotoedit.putExtra("Iss_Stg_Log", issChecked);
                        gotoedit.putExtra("Barcode", BarCodeChecked);
                        gotoedit.putExtra("Rec_Stg_Log", RecChecked);
                        gotoedit.putExtra("MakeOrder", makeOrder);
                        gotoedit.putExtra("FromSite",FromSite);

                        startActivity(gotoedit);
                        finish();
                    }
                }
            }

        }else
            Toast.makeText(ShowOrderDataActivity.this, "لايوجد بيانات لتعديل", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        Intent Go_Back;
        if (makeOrder)
        {
            Go_Back = new Intent(ShowOrderDataActivity.this, CreateOrderSearchActivity.class);
        }
        else
        {
            Go_Back = new Intent(ShowOrderDataActivity.this, TransferSearchActivity.class);
        }
        Go_Back.putExtra("MakeOrder", makeOrder);
        Go_Back.putExtra("Department", Department);
        Go_Back.putExtra("FromSite", FromSite);
        Go_Back.putExtra("ToSite", ToSite);
        startActivity(Go_Back);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
