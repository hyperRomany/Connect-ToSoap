package com.example.connecttosoapapiapp.TransfereModule.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import java.util.List;

public class Itemtransfer_for_searchAdapter
        extends RecyclerView.Adapter<Itemtransfer_for_searchAdapter.MyViewHolder> {

    private List<STo_Search> ItemsList;
    STo_Search  sTo_search;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox_item_search;
        public TextView txt_material_search, txt_short_text_search, txt_ean11_search,
                txt_quantity_search,txt_delivered_quantity_search,txt_po_unite_search;

        public MyViewHolder(View view) {
            super(view);
            checkbox_item_search = view.findViewById(R.id.checkbox_item_search);
            txt_material_search =  view.findViewById(R.id.txt_material_search);
            txt_short_text_search = view.findViewById(R.id.txt_short_text_search);
            txt_ean11_search =  view.findViewById(R.id.txt_ean11_search);
            txt_quantity_search =  view.findViewById(R.id.txt_quantity_search);
            txt_delivered_quantity_search = view.findViewById(R.id.txt_delivered_quantity_search);
            txt_po_unite_search =  view.findViewById(R.id.txt_po_unite_search);
        }
    }


    public Itemtransfer_for_searchAdapter(List<STo_Search> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_po_search, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        sTo_search = ItemsList.get(position);
        if (sTo_search.getChecked_Item()){
            holder.checkbox_item_search.setChecked(true);
        }else {
            holder.checkbox_item_search.setChecked(false);
        }
        holder.checkbox_item_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkbox_item_search.isChecked()){
                    //po_item.setChecked_Item(true);
                    ItemsList.get(position).setChecked_Item(true);
                  //  Log.e("editChecked",""+ItemsList.get(position).getMATERIAL1());

                }else if (!holder.checkbox_item_search.isChecked()){
                    //po_item.setChecked_Item(false);
                    ItemsList.get(position).setChecked_Item(false);
                   // Log.e("editCheckedUN",""+ItemsList.get(position).getMATERIAL1());
                }
            }
        });

        holder.checkbox_item_search.setVisibility(View.VISIBLE);
        holder.txt_material_search.setText(sTo_search.getMAT_CODE1());
        holder.txt_short_text_search.setText(sTo_search.getUOM_DESC1());
        holder.txt_ean11_search.setText(sTo_search.getGTIN1());
        holder.txt_quantity_search.setText(sTo_search.getAVAILABLE_STOCK1());
        holder.txt_delivered_quantity_search.setText(sTo_search.getQTY1());
        holder.txt_po_unite_search.setText(sTo_search.getMEINH1());

       // Log.e("btn_editChecked000",""+ItemsList.size());

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<STo_Search> ReturnListOfItems(){
        /*Log.e("btn_editChecked/",""+ItemsList.size());
        List<Po_Item_of_cycleCount> ItemsList0000 = new ArrayList<>();
        Po_Item_of_cycleCount  po_item_Of_Return =new Po_Item_of_cycleCount();
        int CountChecked=0;
        Boolean Checked =false;
        for (int i = 0; i < ItemsList.size(); i++) {

            po_item_Of_Return = ItemsList.get(i);
            ItemsList0000.add(po_item_Of_Return);

             Checked = ItemsList.get(i).getChecked_Item();
            Log.e("btn_editChecked",""+Checked);

            Log.e("btn_editCheckedI",""+i);

            if (Checked == true) {
                Log.e("btn_editCheckedif",""+Checked);
                Log.e("btn_editCheckedifMater",""+ItemsList0000.get(i).getMATERIAL1());
                CountChecked ++;
                //BarCodeChecked =Po_Item_List.get(i).getMEINH1();
                Log.e("btn_editCheckedCount",""+CountChecked);
                Checked =false;
            }


        }*/

        return ItemsList;

    }
}
