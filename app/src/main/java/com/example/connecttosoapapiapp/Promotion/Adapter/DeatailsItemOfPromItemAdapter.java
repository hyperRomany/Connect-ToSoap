package com.example.connecttosoapapiapp.Promotion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.R;

import java.util.List;

public class DeatailsItemOfPromItemAdapter
        extends RecyclerView.Adapter<DeatailsItemOfPromItemAdapter.MyViewHolder> {

    private List<Prom_item_Module> ItemsList;
    Prom_item_Module prom_item_module;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox_item_search;
        public TextView txt_consultative, txt_barcode, txt_description;

        public MyViewHolder(View view) {
            super(view);
            checkbox_item_search = view.findViewById(R.id.checkbox_item_search);
            txt_consultative =  view.findViewById(R.id.txt_consultative);
            txt_barcode = view.findViewById(R.id.txt_barcode);
            txt_description =  view.findViewById(R.id.txt_description);

        }
    }


    public DeatailsItemOfPromItemAdapter(List<Prom_item_Module> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_details_prom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        prom_item_module = ItemsList.get(position);
//        if (prom_item_module.getChecked_Item()){
//            holder.checkbox_item_search.setChecked(true);
//        }else {
//            holder.checkbox_item_search.setChecked(false);
//        }
//        holder.checkbox_item_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.checkbox_item_search.isChecked()){
//                    //po_item.setChecked_Item(true);
//                    ItemsList.get(position).setChecked_Item(true);
//                    //  Log.e("editChecked",""+ItemsList.get(position).getMATERIAL1());
//
//                }else if (!holder.checkbox_item_search.isChecked()){
//                    //po_item.setChecked_Item(false);
//                    ItemsList.get(position).setChecked_Item(false);
//                    // Log.e("editCheckedUN",""+ItemsList.get(position).getMATERIAL1());
//                }
//            }
//        });

        holder.checkbox_item_search.setVisibility(View.INVISIBLE);
        if (!prom_item_module.getNote_id1().equalsIgnoreCase("0")){
            holder.txt_consultative.setText("تم المراجعة");
            prom_item_module.setUpdateOrInsert(true);
        }else {
            holder.txt_consultative.setText("لم يراجع بعد");
            holder.checkbox_item_search.setChecked(false);
            prom_item_module.setUpdateOrInsert(false);
        }
        holder.txt_barcode.setText(prom_item_module.getBarcode1());
        holder.txt_description.setText(prom_item_module.getItem_desc1());

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<Prom_item_Module> ReturnListOfItems(){
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
