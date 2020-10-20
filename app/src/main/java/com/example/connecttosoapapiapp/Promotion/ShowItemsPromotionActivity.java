package com.example.connecttosoapapiapp.Promotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.connecttosoapapiapp.Promotion.Adapter.ItemOfPromItemAdapter;
import com.example.connecttosoapapiapp.Promotion.Helper.DatabaseHelperForProotion;
import com.example.connecttosoapapiapp.Promotion.Helper.ItemClickSupport;
import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowItemsPromotionActivity extends AppCompatActivity {
RecyclerView recyclerView;
    DatabaseHelperForProotion databaseHelperForProotion;
    List<Prom_item_Module> prom_item_moduleList;
    ItemOfPromItemAdapter itemOfPromItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_promotion);
        recyclerView=findViewById(R.id.recycle_items_for_show_prom);

        databaseHelperForProotion=new DatabaseHelperForProotion(this);
        prom_item_moduleList=new ArrayList<>();
        prom_item_moduleList=  databaseHelperForProotion.selectPromItemsDistinctDiscountNoWithoutNoteId();

        CreateORUpdateRecycleView();
    }
    public void CreateORUpdateRecycleView(){


        itemOfPromItemAdapter = new ItemOfPromItemAdapter(sortlistnote(prom_item_moduleList));

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemOfPromItemAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                List<Prom_item_Module> Po_Item_List = itemOfPromItemAdapter.ReturnListOfItems();

                Intent gotodeatails=new Intent(ShowItemsPromotionActivity.this,PromItemDetailsActivity.class);
                gotodeatails.putExtra("discount_id", Po_Item_List.get(position).getDiscountno1());
                startActivity(gotodeatails);
                //finish();
            }
        });
    }



  /*  public void Edit_PDNEWQTY(View view) {
        //مافيش colum خاص ب checkbox وكله بيجى ب false
        //ممكن نخلى الموديول هو اللى يشيل الداتا
        List<Prom_item_Module> Po_Item_List = itemOfPromItemAdapter.ReturnListOfItems();
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

    }*/

    @Override
    public void onBackPressed() {
//        Intent Go_Back= new Intent(ShowItemsForGIActivity.this, ScanGIActivity.class);
//        Go_Back.putExtra("GL",GL);
//        Go_Back.putExtra("CostCenter",CS);
//        Go_Back.putExtra("Site",Site);
//        Go_Back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(Go_Back);
//        Intent gotodeatails = new Intent(PromItemDetailsActivity.this, ShowItemsPromotionActivity.class);
//        startActivity(gotodeatails);
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<Prom_item_Module> sortlistnote(List<Prom_item_Module> list)
    {
        Collections.sort(list, new Comparator<Prom_item_Module>() {
            @Override
            public int compare(Prom_item_Module o1, Prom_item_Module o2) {
                return o1.getNote_id1().compareTo(o2.getNote_id1());
            }
        });
        return list;
    }

}
