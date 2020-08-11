package com.example.connecttosoapapiapp.ReceivingModule.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ItemOfPoItemsAdapter extends RecyclerView.Adapter<ItemOfPoItemsAdapter.MyViewHolder> {

    private List<Po_Item> ItemsList;
    Po_Item  po_item;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public TextView txtmaterial, txt_vendornam, txt_shorttext,
        txtquantity,txtdelivered_quantity,txt_pounite,txt_ean11;

        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox_item);
            txtmaterial =  view.findViewById(R.id.txt_material);
            txt_vendornam = view.findViewById(R.id.txt_vendor_nam);
            txt_shorttext =  view.findViewById(R.id.txt_short_text);
            txtquantity =  view.findViewById(R.id.txt_quantity);
            txtdelivered_quantity = view.findViewById(R.id.txt_delivered_quantity);
            txt_pounite =  view.findViewById(R.id.txt_po_unite);
            txt_ean11 =  view.findViewById(R.id.txt_ean11);
        }
    }


    public ItemOfPoItemsAdapter(List<Po_Item> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_po_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         po_item = ItemsList.get(position);
        if (po_item.getChecked_Item()){
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
            }
        });

        holder.checkBox.setVisibility(View.INVISIBLE);
        holder.txtmaterial.setText(po_item.getMATERIAL1());
        holder.txt_vendornam.setText(po_item.getVENDOR_NAME1());
        holder.txt_shorttext.setText(po_item.getSHORT_TEXT1());
        holder.txtquantity.setText(po_item.getQUANTITY1());
        holder.txtdelivered_quantity.setText(po_item.getPDNEWQTY1());
        holder.txt_pounite.setText(po_item.getMEINH1());
        holder.txt_ean11.setText(po_item.getEAN111());

//        holder.txt_ean11.setTextIsSelectable(true);
//
//        holder.txtmaterial.requestFocus();
       // Log.e("btn_editChecked000",""+ItemsList.size());
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<Po_Item> ReturnListOfItems(){
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
