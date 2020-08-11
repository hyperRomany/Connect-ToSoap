package com.example.connecttosoapapiapp.ReceivingModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.ReceivingModule.Adapter.ItemOfPoSerialsFormAdapter;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.model.PO_SERIAL;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;

import java.util.ArrayList;
import java.util.List;


public class SerialViewFormActivity extends AppCompatActivity {
    List<PO_SERIAL> po_serialslist;
    private DatabaseHelper databaseHelper;
    Double v;
    TextView txt_barcode,txt_quantity_for_serial,txt_descripation_for_serial;
    EditText edit_current_serials ;
    String PDNEWQTY,BarCode,Material,Po_item,Shorttext;
    RecyclerView recyclerView;
    ItemOfPoSerialsFormAdapter itemDelieveredFormAdapter;
int CountChecked;
    String SerialToDelete, SerialCheckedToEdit, BarCodeCheckedToEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_view_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode =findViewById(R.id.txt_barcode);
        txt_quantity_for_serial=findViewById(R.id.txt_quantity_for_serial);
        edit_current_serials=findViewById(R.id.edit_current_serials);
        txt_descripation_for_serial=findViewById(R.id.txt_descripation_for_serial);
        recyclerView =  findViewById(R.id.recycle_serials_view);

        databaseHelper =new DatabaseHelper(this);

        List<Po_Item> Po_Item_list = new ArrayList<>();

        po_serialslist = new ArrayList<>();

        Intent GetData=getIntent();
        PDNEWQTY=GetData.getExtras().getString("PDNEWQTY");
        Log.d("getExtras",""+PDNEWQTY);

        BarCode=GetData.getExtras().getString("barCode");
        Material=GetData.getExtras().getString("Material");
        Po_item=GetData.getExtras().getString("Po_item");
        //Shorttext=GetData.getExtras().getString("ShortText");

        Po_Item_list = databaseHelper.Search_Barcode_Po_Item(BarCode);
        Shorttext=Po_Item_list.get(0).getSHORT_TEXT1();
        txt_descripation_for_serial.setText(Po_Item_list.get(0).getSHORT_TEXT1());
        txt_barcode.setText(BarCode);
        Log.d("putExtra2", "" + BarCode);
        txt_quantity_for_serial.setText(Po_Item_list.get(0).getPDNEWQTY1());

        CreateORUpdateRecycleViewForm();
        /*po_serialslist=new ArrayList<>();
        databaseHelper=new DatabaseHelperForTransfer(this);
        po_serialslist.add(new PO_SERIAL())*/
    }

    public void ADDSerialForm(View view) {
        if (!edit_current_serials.getText().toString().isEmpty()){
            int LastEdit = po_serialslist.size();
            Log.d("po_itemlistsize;","FindOrNot"+LastEdit);

            String FindOrNot = databaseHelper.serach_for_Serial(edit_current_serials.getText().toString());
            //Log.d("po_itemlistsize","FindOrNot"+FindOrNot);

            //from 0 to 3     so index 4
            int index = PDNEWQTY.indexOf(".")+2;
           // Log.d("po_itemlistsize","index"+index);

            String QTY= String.valueOf(PDNEWQTY.substring(0,index));
            //Log.d("po_itemlistsize","index"+QTY);
            if (LastEdit < Double.valueOf(QTY)){
                if (FindOrNot.equalsIgnoreCase("true")){
                    Log.d("Po_item",""+Po_item);
                    databaseHelper.insertPo_Serials( Material,Po_item, BarCode, edit_current_serials.getText().toString());
                    CreateORUpdateRecycleViewForm();
                }else {
                    edit_current_serials.setError("لقد أدخلت هذا السيريال من قبل");
                    edit_current_serials.setText("");
                }
            }else {
                edit_current_serials.setError("لقد ادخلت كل السيريالات المطلوبه");
            }
        }else {
            edit_current_serials.setError("أدخل السيريال");
        }



    }

    public void CreateORUpdateRecycleViewForm(){
        // po_serialslist = new ArrayList<>();

        po_serialslist = databaseHelper.selectPo_Serials(BarCode);
        itemDelieveredFormAdapter = new ItemOfPoSerialsFormAdapter(po_serialslist);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemDelieveredFormAdapter);

    }

    public void Edit_serial(View view){
        //مافيش colum خاص ب checkbox وكله بيجى ب false
        //ممكن نخلى الموديول هو اللى يشيل الداتا
        List<PO_SERIAL> Po_Item_List = itemDelieveredFormAdapter.ReturnListOfItems();
        Log.e("btn_editChecked",""+Po_Item_List.size());

        CountChecked =0;
        if (Po_Item_List.size() != 0) {
            for (int i = 0; i < Po_Item_List.size(); i++) {
                Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                //Log.e("btn_editChecked",""+Checked);
                if (Checked == true) {
                    //Log.e("btn_editCheckedif",""+Checked);
                    CountChecked += 1;
                    SerialCheckedToEdit =Po_Item_List.get(i).getSerial1();
                    BarCodeCheckedToEdit =Po_Item_List.get(i).getEAN11();
                }if (i == (Po_Item_List.size()-1)){
                    if (CountChecked < 1 || CountChecked > 1) {
                        Toast.makeText(SerialViewFormActivity.this, "لقد أخترت أكثر من اختيار أو لم تقم بأى أختيار ", Toast.LENGTH_LONG).show();
                    }
                    else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()
                        Intent GoToEdit = new Intent(SerialViewFormActivity.this, EditSerialActivity.class);

                        GoToEdit.putExtra("Serial"  , SerialCheckedToEdit);
                        GoToEdit.putExtra("Barcode"  , BarCodeCheckedToEdit);
                        GoToEdit.putExtra("Shorttext"  , Shorttext);


                        startActivity(GoToEdit);
                    }
                }
            }

        }else
            Toast.makeText(SerialViewFormActivity.this, "لايوجد بيانات للادخال", Toast.LENGTH_SHORT).show();
    }

    public void Delete_serial(View view) {
        {

            List<PO_SERIAL> Po_Item_List = itemDelieveredFormAdapter.ReturnListOfItems();
            Log.e("btn_editChecked",""+Po_Item_List.size());

            CountChecked =0;
            if (Po_Item_List.size() != 0) {
                for (int i = 0; i < Po_Item_List.size(); i++) {
                    Boolean Checked = Po_Item_List.get(i).getChecked_Item();
                    //Log.e("btn_editChecked",""+Checked);
                    if (Checked == true) {
                        //Log.e("btn_editCheckedif",""+Checked);
                        CountChecked += 1;
                        SerialToDelete =Po_Item_List.get(i).getSerial1();

                    }if (i == (Po_Item_List.size()-1)){
                        if (CountChecked < 1 || CountChecked > 1) {
                            Toast.makeText(SerialViewFormActivity.this, "لقد أخترت أكثر من اختيار أو لم تقم بأى أختيار ", Toast.LENGTH_LONG).show();
                        }
                        else if (CountChecked == 1 ) {  //&& !BarCodeChecked.isEmpty()


                            new AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.text_for_serial))
                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            databaseHelper.DeleteRowFromSerialTable(SerialToDelete);
                                            CreateORUpdateRecycleViewForm();
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
                Toast.makeText(SerialViewFormActivity.this, "لايوجد بيانات لادخال", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {

        v=Double.valueOf(PDNEWQTY);
        po_serialslist = databaseHelper.selectPo_Serials(BarCode);
        Log.e("edit_current_serials",""+v+"$$"+po_serialslist.size());
        if (v == (po_serialslist.size()) ){
            Intent Go_Back= new Intent(SerialViewFormActivity.this,CheckItemFormActivity.class);
            startActivity(Go_Back);
            // super.onBackPressed();

        }else {
            edit_current_serials.setError("من فضلك أدخل كل السيرالات");
            // super.onBackPressed();
        }


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
