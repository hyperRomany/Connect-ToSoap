package com.example.connecttosoapapiapp.ItemAvailability.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemsSearchModul;
import com.example.connecttosoapapiapp.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ItemAvailabilityAdapter extends RecyclerView.Adapter<ItemAvailabilityAdapter.MyViewHolder> {

    private List<ItemsSearchModul> ItemsList;
    ItemsSearchModul itemsSearchModul;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public TextView txt_material,txt_nu, txt_short_text,txt_ean11;

        public MyViewHolder(View view) {
            super(view);
//            checkBox = view.findViewById(R.id.checkbox_item);
//            txt_nu =  view.findViewById(R.id.txt_nu);
            txt_material =  view.findViewById(R.id.txt_material);
            txt_ean11 = view.findViewById(R.id.txt_ean11);
            txt_short_text =  view.findViewById(R.id.txt_short_text);
//            txt_quantity =  view.findViewById(R.id.txt_quantity);
//            txt_po_unite = view.findViewById(R.id.txt_po_unite);

        }
    }


    public ItemAvailabilityAdapter(List<ItemsSearchModul> moviesList) {
        this.ItemsList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_for_searchingbarcodeinitemavailability, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        itemsSearchModul = ItemsList.get(position);

//        holder.checkBox.setVisibility(View.VISIBLE);
//        holder.txt_nu.setText(""+(position+1));

        holder.txt_ean11.setText(itemsSearchModul.getBarCode());
        holder.txt_short_text.setText(itemsSearchModul.getDescribtion());
        holder.txt_material.setText(itemsSearchModul.getItemean());
        holder.txt_ean11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) v.getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", holder.txt_ean11.getText());
                manager.setPrimaryClip(clipData);
                Toast.makeText(v.getContext(),"تم النسخ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    public List<ItemsSearchModul> ReturnListOfItems(){
        return ItemsList;

    }
}
