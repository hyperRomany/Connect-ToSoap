package com.example.connecttosoapapiapp.ReceivingModule.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.model.PO_SERIAL;

import java.util.List;

public class ItemOfPoSerialsFormAdapter extends RecyclerView.Adapter<ItemOfPoSerialsFormAdapter.MyViewHolder> {

    private List<PO_SERIAL> Poseriallist;
    PO_SERIAL  po_serial;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public TextView txtmaterial, txt_Serial,txt_ean11;

        public MyViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox_item);
            txtmaterial =  view.findViewById(R.id.txt_material);
            txt_ean11 =  view.findViewById(R.id.txt_BarCode);
            txt_Serial =  view.findViewById(R.id.txt_Serial);

        }
    }


    public ItemOfPoSerialsFormAdapter(List<PO_SERIAL> moviesList) {
        this.Poseriallist = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_po_serial, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         po_serial = Poseriallist.get(position);
        if (po_serial.getChecked_Item()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(true);
                    Poseriallist.get(position).setChecked_Item(true);
                  //  Log.e("editChecked",""+ItemsList.get(position).getMATERIAL1());

                }else if (!holder.checkBox.isChecked()){
                    //po_item.setChecked_Item(false);
                    Poseriallist.get(position).setChecked_Item(false);
                   // Log.e("editCheckedUN",""+ItemsList.get(position).getMATERIAL1());
                }
            }
        });

        holder.checkBox.setVisibility(View.VISIBLE);
        holder.txtmaterial.setText(po_serial.getMaterial1());
        holder.txt_ean11.setText(po_serial.getEAN11());
        holder.txt_Serial.setText(po_serial.getSerial1());


       // Log.e("btn_editChecked000",""+ItemsList.size());

    }

    @Override
    public int getItemCount() {
        return Poseriallist.size();
    }

    public List<PO_SERIAL> ReturnListOfItems(){
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
        return Poseriallist;
    }
}
