package com.example.connecttosoapapiapp.ItemReturn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ItemReturn.Adapter.ItemReturn_for_searchAdapter;
import com.example.connecttosoapapiapp.ItemReturn.Helper.DatabaseHelperForItemReturn;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.TransfereModule.Adapter.Itemtransfer_for_searchAdapter;
import com.example.connecttosoapapiapp.TransfereModule.Edit_Order_DataActivity;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.ShowOrderDataActivity;
import com.example.connecttosoapapiapp.TransfereModule.TransferSearchActivity;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import java.util.ArrayList;
import java.util.List;

public class ShowItemsReturnItemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Button btn_edit, btn_delete;
    DatabaseHelperForItemReturn databaseHelperForItemReturn;
    List<Item_Return_Search> item_return_searchList;
    ItemReturn_for_searchAdapter itemtransfer_for_searchAdapter;
    int CountChecked;
    String issChecked, BarCodeChecked, RecChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_return_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView =  findViewById(R.id.recycle_items_view_itemreturn);

        btn_edit=findViewById(R.id.btn_edit_transfer);

        btn_delete=findViewById(R.id.btn_delete_transfer);

        CreateORUpdateRecycleView();
        Intent getdat=getIntent();


        Log.e("databaseHelperForTrans",""+issChecked);
        Log.e("databaseHelperForTrans",""+ RecChecked);
    }

    public void Delete_PDNEWQTY(View view) {
        List<Item_Return_Header> item_return_headers =new ArrayList<>();
        item_return_searchList = itemtransfer_for_searchAdapter.ReturnListOfItems();

        CountChecked =0;
        if (item_return_searchList.size() != 0) {
            for (int i = 0; i < item_return_searchList.size(); i++) {
                Boolean Checked = item_return_searchList.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =item_return_searchList.get(i).getGTIN1();

                }if (i == (item_return_searchList.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemsReturnItemActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()

                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.text))
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                          databaseHelperForItemReturn.Update_Sto_search_For_QTY("0.0",BarCodeChecked);
                                        // databaseHelperForTransfer.DeleteSerialTable();
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
            Toast.makeText(ShowItemsReturnItemActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();

    }

    public void CreateORUpdateRecycleView(){
        databaseHelperForItemReturn =new DatabaseHelperForItemReturn(this);
        item_return_searchList=new ArrayList<>();
        item_return_searchList = databaseHelperForItemReturn.selectSto_Search_for_Qty();

        itemtransfer_for_searchAdapter = new ItemReturn_for_searchAdapter(item_return_searchList);

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

        List<Item_Return_Search> item_return_headers = new ArrayList<>();
        item_return_headers = itemtransfer_for_searchAdapter.ReturnListOfItems();

        CountChecked =0;
        if (item_return_headers.size() != 0) {
            for (int i = 0; i < item_return_headers.size(); i++) {
                Boolean Checked = item_return_headers.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    BarCodeChecked =item_return_headers.get(i).getGTIN1();
                    //BarCodeChecked =item_return_headers.get(i).getGTIN1();

                }if (i == (item_return_headers.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(ShowItemsReturnItemActivity.this, "لقد قمت باختيار اكثر من أختيار", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent gotoedit=new Intent(ShowItemsReturnItemActivity.this, EditItemReturnActivity.class);

                        gotoedit.putExtra("Barcode",BarCodeChecked);
                        startActivity(gotoedit);
                    }
                }
            }

        }else
            Toast.makeText(ShowItemsReturnItemActivity.this, "لايوجد بيانات لتعديل", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ShowItemsReturnItemActivity.this , ScanItemsReturnActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
