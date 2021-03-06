package com.example.connecttosoapapiapp.Promotion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ItemOfPromItemAdapter
        extends RecyclerView.Adapter<ItemOfPromItemAdapter.MyViewHolder> {

    private List<Prom_item_Module> ItemsList;
    Prom_item_Module prom_item_module;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox_item_search;
        public TextView txt_nu ,  txt_consultative, txt_num_prom, txt_type_prom,
                txt_start_prom,txt_end_prom;

        public MyViewHolder(View view) {
            super(view);
            checkbox_item_search = view.findViewById(R.id.checkbox_item_search);
            txt_nu=  view.findViewById(R.id.txt_nu);
            txt_consultative =  view.findViewById(R.id.txt_consultative);
            txt_num_prom = view.findViewById(R.id.txt_num_prom);
            txt_type_prom =  view.findViewById(R.id.txt_type_prom);
            txt_start_prom =  view.findViewById(R.id.txt_start_prom);
            txt_end_prom = view.findViewById(R.id.txt_end_prom);
        }
    }


    public ItemOfPromItemAdapter(List<Prom_item_Module> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_show_prom, parent, false);

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

       // holder.checkbox_item_search.setVisibility(View.VISIBLE);
        if (!prom_item_module.getNote_id1().equalsIgnoreCase("0")){
            holder.txt_consultative.setText("???? ????????????????");
        }else {
            holder.txt_consultative.setText("???? ?????????? ??????");
        }
        holder.txt_nu.setText(""+(position+1));

        holder.txt_num_prom.setText(prom_item_module.getDiscountno1());
        holder.txt_type_prom.setText(prom_item_module.getDiscounttype1());
        holder.txt_start_prom.setText(prom_item_module.getDate_from1());
        holder.txt_end_prom.setText(prom_item_module.getDate_to1());

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
