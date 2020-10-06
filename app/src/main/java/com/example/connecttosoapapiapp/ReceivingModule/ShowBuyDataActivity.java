package com.example.connecttosoapapiapp.ReceivingModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Adapter.ItemOfPoItemsAdapter;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;

import java.util.List;

public class ShowBuyDataActivity extends AppCompatActivity {

        // private List<Po_Item_of_cycleCount> po_items = new ArrayList<>();

        List<Po_Item> Po_Item_For_Recycly;

        private RecyclerView recyclerView;
        private ItemOfPoItemsAdapter itemOfPoItemsAdapter;
        DatabaseHelper databaseHelper;
        Button btn_edit, btn_delete;
        int CountChecked;
        String BarCodeChecked, ToTalQuantity, VendorName , BarCodeToDelete, VendorNameOfDelete, DeliveryDate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_check_item_form);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Po_Item_For_Recycly = new ArrayList<>();

            recyclerView =  findViewById(R.id.recycle_items_view);

            btn_edit=findViewById(R.id.btn_edit);
            btn_edit.setVisibility(View.GONE);

            btn_delete=findViewById(R.id.btn_delete);
            btn_delete.setVisibility(View.GONE);

       /* Po_Item_For_Recycly.add(new Po_Item_of_cycleCount(true,"material","short text","quantity",
                "pdnewqty1","Po_unite","ean111","vendor_name"));
        Po_Item_For_Recycly.add(new Po_Item_of_cycleCount(false,"material","short text","quantity",
                "pdnewqty1","Po_unite","ean111","vendor_name"));
        Po_Item_For_Recycly.add(new Po_Item_of_cycleCount(false,"material","short text","quantity",
                "pdnewqty1","Po_unite","ean111","vendor_name"));*/


        databaseHelper =new DatabaseHelper(this);

        Po_Item_For_Recycly = databaseHelper.Get_Po_Item();

            itemOfPoItemsAdapter = new ItemOfPoItemsAdapter(Po_Item_For_Recycly);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemOfPoItemsAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ShowBuyDataActivity.this,ScanRecievingActivity.class);
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
