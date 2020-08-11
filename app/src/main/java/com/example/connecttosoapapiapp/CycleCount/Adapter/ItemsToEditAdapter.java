package com.example.connecttosoapapiapp.CycleCount.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ItemsToEditAdapter extends RecyclerView.Adapter<ItemsToEditAdapter.MyViewHolder> {

    private List<Po_Item_of_cycleCount> ItemsList;
    Po_Item_of_cycleCount po_item_of_cycleCount;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public TextView txt_material,txt_nu, txt_short_text,txt_ean11,txt_quantity,txt_po_unite;

        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox_item);
            txt_nu =  view.findViewById(R.id.txt_nu);
            txt_material =  view.findViewById(R.id.txt_material);
            txt_ean11 = view.findViewById(R.id.txt_ean11);
            txt_short_text =  view.findViewById(R.id.txt_short_text);
            txt_quantity =  view.findViewById(R.id.txt_quantity);
            txt_po_unite = view.findViewById(R.id.txt_po_unite);

        }
    }


    public ItemsToEditAdapter(List<Po_Item_of_cycleCount> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_po_item_cyclecount, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        po_item_of_cycleCount = ItemsList.get(position);
        if (po_item_of_cycleCount.getChecked_Item()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(true);
                    ItemsList.get(position).setChecked_Item(true);
                  //  Log.e("editChecked",""+ItemsList.get(position).getMATERIAL1());

                }else if (!holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(false);
                    ItemsList.get(position).setChecked_Item(false);
                   // Log.e("editCheckedUN",""+ItemsList.get(position).getMATERIAL1());
                }
                notifyDataSetChanged();
            }
        });

        if (ItemsList.get(position).getChecked_Item() == true) {
            holder.checkBox.setChecked(true);
        }else if (ItemsList.get(position).getChecked_Item() == false){
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.
                if (ItemsList.get(position).getChecked_Item() == false) {
                    // holder.linear_of_recycle.setBackgroundColor(context.getResources().getColor(R.color.first_blue));
                    //po_item.setIschecked(true);
                    for (int i=0 ; i<ItemsList.size() ; i++){
                        Log.e("mmmmmmmmmm",""+ItemsList.get(i).getChecked_Item());
//                       v.notify();
                        if (i==position){
                            Log.e("mmmmmmmmmmif",""+position);
                            Log.e("mmmmmmmmmmif","sellected"+v.isSelected());
                            ItemsList.get(position).setChecked_Item(true);
                            //  holder.linear_of_recycle.setBackgroundColor(context.getResources().getColor(R.color.first_blue));
//                            v.setBackgroundColor(context.getResources().getColor(R.color.first_blue));
                            Log.e("mmmmmmmmmmI","I"+v.getTag());
//                           v.findViewById(R.id.linear_of_recycle)
//                                   .setOnClickListener(new View.OnClickListener(){
//                                       @Override
//                                       public void onClick(View v) {
//                                           holder.linear_of_recycle.setBackgroundColor(context.getResources().getColor(R.color.first_blue));
//                                       }
//                           });

                        }else if (i != position){

                            ItemsList.get(i).setChecked_Item(false);
                            Log.e("mmmmmmmmmmI","I"+v.getTag());

                            ItemsList.get(position).setChecked_Item(true);

//                           v.findViewById(R.id.linear_of_recycle)
//                                   .setOnClickListener(new View.OnClickListener(){
//                                       @Override
//                                       public void onClick(View v) {
//                                           holder.linear_of_recycle.setBackgroundColor(context.getResources().getColor(R.color.third_white));
//                                       }
//                                   });
                        }
//                       Log.e("mmmmmmmmmm",""+ItemsList.get(i).getIschecked());
                    }
                    Log.e("mmmmmmmmmm",""+position);
                }else if (ItemsList.get(position).getChecked_Item() == true){
//                    holder.linear_of_recycle.setBackgroundColor(context.getResources().getColor(R.color.third_white));
                    // po_item.setIschecked(false);
                    ItemsList.get(position).setChecked_Item(false);
                }
                notifyDataSetChanged();
            }
        });


        holder.checkBox.setVisibility(View.VISIBLE);
        holder.txt_nu.setText(""+(position+1));

        holder.txt_ean11.setText(po_item_of_cycleCount.getEAN111());
        holder.txt_material.setText(po_item_of_cycleCount.getMATERIAL1());
        holder.txt_short_text.setText(po_item_of_cycleCount.getMAKTX1());
        holder.txt_quantity.setText(po_item_of_cycleCount.getQTY1());
        holder.txt_po_unite.setText(po_item_of_cycleCount.getMEINH1());


       // Log.e("btn_editChecked000",""+ItemsList.size());

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<Po_Item_of_cycleCount> ReturnListOfItems(){
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
