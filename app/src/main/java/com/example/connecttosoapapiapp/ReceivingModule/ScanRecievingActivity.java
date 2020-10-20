package com.example.connecttosoapapiapp.ReceivingModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Header;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ScanRecievingActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    TextView txtponumber,txt_comp_code,txt_user_code, txtvendorname, txtvendornumber,
            txtdescripation ,txtcodeitem, txtuniteitem,txtstateitem,
            txtlastdeliver;
    EditText editbarcode, editcurrentdeliver;

    Button btnsearchbarcode;
LinearLayout linear_parent;
    List<Po_Header> Po_HeaderList;

    Double AskaedQuantity=0.0;
    View view;
    List<Po_Item> Po_Item_List;
    List<Po_Item> Po_Item_all_List;
    List<Po_Item> Po_Item_all_List_for_barcode;
    List<Po_Item> Po_Item_List_ForPlant;
    List<Users> user_List;
    List<Po_Item> Po_Item_For_Log_only_Has_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_recieving);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseHelper =new DatabaseHelper(this);

        Po_HeaderList = new ArrayList<>();

        txtponumber = findViewById(R.id.txt_po_number);
        txtvendorname = findViewById(R.id.txt_vendor_name);
        txtvendornumber = findViewById(R.id.txt_Vendor_number);
        txt_comp_code = findViewById(R.id.txt_comp_code);
        txt_user_code = findViewById(R.id.txt_user_code);
        editbarcode=findViewById(R.id.edit_barcode);

        linear_parent=findViewById(R.id.linear_parent);
        btnsearchbarcode=findViewById(R.id.btn_search_barcode);
        editcurrentdeliver = findViewById(R.id.edit_current_deliver);

        txtdescripation = findViewById(R.id.txt_descripation);
        txtcodeitem = findViewById(R.id.txt_code_item);
        txtuniteitem = findViewById(R.id.txt_unite_item);
        txtstateitem = findViewById(R.id.txt_state_item);
        txtlastdeliver =findViewById(R.id.txt_last_deliver);

        editbarcode.requestFocus();

        editbarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER){

                    //execute our method for searching
                    SearchBarCode(view);
                }
                return false;
            }
        });

        editcurrentdeliver.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_GO
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent == null
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER
                        || keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER){

                    //execute our method for searching
                    SaveDelivered(view);
                }
                return false;
            }
        });


        user_List=databaseHelper.getUserData();
        if (user_List.size()!=0) {
            txt_user_code.setText(user_List.get(0).getCompany1());
        }else {
            txtponumber.setText("لم يتم تحمل بيانات المستخدم");
        }
        Po_HeaderList =  databaseHelper.getPo_number_Po_Headers();

        if (Po_HeaderList.size()!=0){
            //Log.d("Po_HeadersList",""+Po_HeaderList.get(0).getPO_NUMBER1());
            txtponumber.setText(Po_HeaderList.get(0).getPO_NUMBER1());

            // Log.d("Po_HeadersList",""+Po_HeaderList.get(0).getVENDOR11());
            txtvendorname.setText(Po_HeaderList.get(0).getVENDOR_NAME1());

            //Log.d("Po_HeadersList",""+Po_HeaderList.get(0).getVENDOR_NAME1());
            txtvendornumber.setText(Po_HeaderList.get(0).getVENDOR11());

            txt_comp_code.setText(Po_HeaderList.get(0).getCOMP_CODE1());

        }else {
            txtponumber.setText("لم يتم تحميل Header");
        }



        Po_Item_List_ForPlant=new ArrayList<>();
        Po_Item_List_ForPlant = databaseHelper.Get_Po_Item();
        if (Po_Item_List_ForPlant.size() !=0) {
            txt_comp_code.setText(Po_Item_List_ForPlant.get(0).getPLANT1());
        }else {
            txt_comp_code.setText("لم يتم تحميل PO_Item");
        }

        if (user_List.size() !=0 && Po_Item_List_ForPlant.size() !=0) {
            if (user_List.get(0).getCompany1().equalsIgnoreCase("h010")
                    && Po_Item_List_ForPlant.get(0).getPLANT1().contains("01")) {
                //          Toast.makeText(this,"المستخدم من نفس مكان تسجل أمر الشراء",Toast.LENGTH_SHORT).show();
            } else if (user_List.get(0).getCompany1().equalsIgnoreCase("h020")
                    && Po_Item_List_ForPlant.get(0).getPLANT1().contains("02")) {
                //           Toast.makeText(this,"المستخدم من نفس مكان تسجل أمر الشراء",Toast.LENGTH_SHORT).show();
            } else if (user_List.get(0).getCompany1().equalsIgnoreCase("h030")
                    && Po_Item_List_ForPlant.get(0).getPLANT1().contains("03")) {
                //         Toast.makeText(this,"المستخدم من نفس مكان تسجل أمر الشراء",Toast.LENGTH_SHORT).show();
            } else if (user_List.get(0).getCompany1().equalsIgnoreCase("h040")
                    && Po_Item_List_ForPlant.get(0).getPLANT1().contains("04")) {
                //        Toast.makeText(this,"المستخدم من نفس مكان تسجل أمر الشراء",Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.text_not_same_cop_cod))
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent Go_Back = new Intent(ScanRecievingActivity.this, ReceivingActivity.class);
                                Go_Back.putExtra("UserName", Po_HeaderList.get(0).getDelievered_BY1());
                                startActivity(Go_Back);
                            }
                        })
                        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })*/
                        .show();
                linear_parent.setVisibility(View.GONE);
            }
        }else {
            Toast.makeText(this, "لم يتم الحصول على بيانات المستخدم او بيانات امر الشراء فارغه", Toast.LENGTH_SHORT).show();
        }
            boolean Check_If_This_Is_First_Time = getIntent().getExtras().getBoolean("This Is First Time");
            Log.e("This Is First TimeFSC", "" + Check_If_This_Is_First_Time);
// this to update vendor name from null to it's name and PDNEWQTY
//            if (Check_If_This_Is_First_Time == true) {
//                int ID = databaseHelper.update_Vendor_PDNEWQTY_Date("0.0", "", txtvendorname.getText().toString());
//                Log.e("This Is First TimeID", "" + ID);
//            }
    }

    public void SearchBarCode(View view) {
        editcurrentdeliver.setError(null);
       //  Po_Item_List;
        Po_Item_List = new ArrayList<>();
        Po_Item_all_List = new ArrayList<>();
        Po_Item_all_List_for_barcode = new ArrayList<>();
        String barCode = editbarcode.getText().toString();

//        String barCode,K,G;
        editcurrentdeliver.setText("");

//        if (editbarcode.getText().toString().startsWith("23")) {
//            barCode = Calculatcheckdigitforscales(editbarcode.getText().toString().substring(0,7)+"00000");
//            K=editbarcode.getText().toString().substring(7,9);
//            G=editbarcode.getText().toString().substring(9,12);
//            editcurrentdeliver.setText(K+"."+G);
//        }else {
//            barCode = editbarcode.getText().toString();
//        }

            if (!barCode.isEmpty()){
// get all data for this barcode ===> this give us 5 because article has required 5 times
            Po_Item_all_List_for_barcode = databaseHelper.Search_Barcode_Po_Item(barCode);
            Log.e("zzzSearch_Bar1 ",""+Po_Item_all_List_for_barcode.size());
            if (Po_Item_all_List_for_barcode.size() == 1) {  // if barcode has one Po-iteam
                Log.e("zzzSearch_Bar2Bef ",""+Po_Item_all_List_for_barcode.size());
                for (int i=0;i<Po_Item_all_List_for_barcode.size();i++) {
                    List<Po_Item> po_itemlist = new ArrayList<>();
                    Log.e("zzzSearch_BarPoUN ",""+Po_Item_all_List_for_barcode.get(i).getPO_ITEM1());

                    po_itemlist = databaseHelper.Get_Po_Item_for_po_iteam(Po_Item_all_List_for_barcode.get(i).getPO_ITEM1());
                    for (int J=0;J<po_itemlist.size();J++) {
                        Po_Item_all_List.add(po_itemlist.get(J));
                        Log.e("zzzSearch_BarC ",""+Po_Item_all_List.get(J).getEAN111());

                    }

                }
                Log.e("zzzSearch_Bar2 ",""+Po_Item_all_List.size());

                for (int i=0;i<Po_Item_all_List.size();i++) {
                    if (Po_Item_all_List.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List.get(i).getMEINH1())
                            &&// Po_Item_all_List.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")
                            Po_Item_all_List.get(i).getEAN111().equalsIgnoreCase(barCode)) {
                        Po_Item_List.add(Po_Item_all_List.get(i));
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getEAN111());
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getPO_UNIT1());
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getPDNEWQTY1());
                    }else if (Po_Item_all_List.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List.get(i).getMEINH1())
                            && !Po_Item_all_List.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")
                            && !Po_Item_all_List.get(i).getEAN111().equalsIgnoreCase(barCode)){
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getEAN111());
                        Log.e("zzzSearch_Barclear ",""+Po_Item_all_List.get(i).getPO_UNIT1());
                        Log.e("zzzSearch_Barclear ",""+Po_Item_all_List.get(i).getPDNEWQTY1());
                        Po_Item_List.clear();
                        break;
                    }
                }

            }


            else if (Po_Item_all_List_for_barcode.size() > 1) {  // if barcode has many Po-iteam
                Log.e("zzzSearch_many Po-iteam ",""+Po_Item_all_List_for_barcode.size());
                for (int i=0;i<Po_Item_all_List_for_barcode.size();i++) {
                    Log.e("zzzSearch__many Po-iteam ",""+Po_Item_all_List_for_barcode.get(i).getPO_ITEM1());

                    List<Po_Item> po_itemlist_for_po_item = new ArrayList<>();
                    po_itemlist_for_po_item = databaseHelper.Get_sum_qty_for_po_iteam(Po_Item_all_List_for_barcode.get(i).getPO_ITEM1());
                    Log.e("zzzfor_po_itemsiz ",""+po_itemlist_for_po_item.size());

                    if (Double.valueOf(po_itemlist_for_po_item.get(0).getPDNEWQTY1())>0){
                        //this mean that there is a barcode has qty >0 for this po_item
                        Log.e("zzzfor_qty ","more than "+Double.valueOf(po_itemlist_for_po_item.get(0).getPDNEWQTY1()));
                        if (Po_Item_all_List_for_barcode.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List_for_barcode.get(i).getMEINH1())
                                && Double.valueOf(Po_Item_all_List_for_barcode.get(i).getPDNEWQTY1())>0
                             ) {
                            Log.e("zzzfor_qty ",">0 ");

                            Po_Item_List.add(Po_Item_all_List_for_barcode.get(i));
                        }

                    }else {
                        Log.e("zzzfor_qty ","equal "+Double.valueOf(po_itemlist_for_po_item.get(0).getPDNEWQTY1()));
                        if (Po_Item_all_List_for_barcode.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List_for_barcode.get(i).getMEINH1())
                                && Double.valueOf(Po_Item_all_List_for_barcode.get(i).getPDNEWQTY1()) == 0
                        ) {
                            Log.e("zzzfor_qty ","=0 ");
                            Po_Item_List.add(Po_Item_all_List_for_barcode.get(i));
                        }
                    }


                }


                /*
                for (int i=0;i<Po_Item_all_List.size();i++) {
                    if (Po_Item_all_List.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List.get(i).getMEINH1())
                            &&// Po_Item_all_List.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")
                            Po_Item_all_List.get(i).getEAN111().equalsIgnoreCase(barCode)) {
                        Log.e("zzzSearch_Beforeadd ",""+Po_Item_List.size());

                        Po_Item_List.add(Po_Item_all_List.get(i));


                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getEAN111());
                        Log.e("zzzSearch_BarPO_UN ",""+Po_Item_all_List.get(i).getPO_UNIT1());
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getPDNEWQTY1());
                        Log.e("zzzSearch_Baradd ",""+Po_Item_all_List.get(i).getPO_ITEM1());
                    }else if (Po_Item_all_List.get(i).getPO_UNIT1().equalsIgnoreCase(Po_Item_all_List.get(i).getMEINH1())
                            && !Po_Item_all_List.get(i).getPDNEWQTY1().equalsIgnoreCase("0.0")
                            && !Po_Item_all_List.get(i).getEAN111().equalsIgnoreCase(barCode)){
                        Log.e("zzzSearch_Baraddclear",""+Po_Item_all_List.get(i).getEAN111());
                        Log.e("zzzSearch_Barclear ",""+Po_Item_all_List.get(i).getPO_UNIT1());
                        Log.e("zzzSearch_Barclear ",""+Po_Item_all_List.get(i).getPDNEWQTY1());
                        Log.e("zzzSearch_Barclear ",""+Po_Item_all_List.get(i).getPO_ITEM1());

                        for (int k=0;k<Po_Item_List.size();k++) {
                            if (Po_Item_all_List.get(i).getPO_ITEM1().equalsIgnoreCase(Po_Item_List.get(k).getPO_ITEM1())) {
                                Log.e("zzzSearch_BarclearD ",""+Po_Item_all_List.get(i).getPO_ITEM1());

                                Po_Item_List.clear();
                            }
                        }
                    }
                }*/
            }

            Log.e("zzzPo_Item_List ",""+Po_Item_List.size());


            if (Po_Item_List.size()<=0){
                editbarcode.setError("هذا الباركود غير متاح..أو ليس لديه نفس الوحده المطلوبه..أوهذا الصنف تم أستلامه من قبل");
                txtdescripation.setText("الوصف");
                txtcodeitem.setText("كود الصنف");
                txtuniteitem.setText("وحده الصنف");
                txtstateitem.setText("حاله الصنف");
                txtlastdeliver.setText("0");
                editcurrentdeliver.setText("");
            }/*else if (Po_Item_List.get(0).getNO_MORE_GR1().equalsIgnoreCase("X")){
                editbarcode.setError("هذا الصنف تم أستلامه من قبل");
                txtdescripation.setText("الوصف");
                txtcodeitem.setText("كود الصنف");
                txtuniteitem.setText("وحده الصنف");
                txtstateitem.setText("حاله الصنف");
                txtlastdeliver.setText("0");
                editcurrentdeliver.setText("");
            }*/else {
                if (!Po_Item_List.get(0).getPO_UNIT1().equalsIgnoreCase(Po_Item_List.get(0).getMEINH1())) {
                    String RequiredQY = Po_Item_List.get(0).getPO_UNIT1();
                    Log.d("RequiredQY", "" + RequiredQY);
                    Log.d("RequiredQY", "" + Po_Item_List.get(0).getMEINH1());
                    //  RequiredQY.equalsIgnoreCase(Po_Item_List.get(0).getMEINH1())
                    editbarcode.setError("هذا الباركود ليس لديه نفس الوحده المطلوبه");
                    txtdescripation.setText("الوصف");
                    txtcodeitem.setText("كود الصنف");
                    txtuniteitem.setText("وحده الصنف");
                    txtstateitem.setText("حاله الصنف");
                    txtlastdeliver.setText("0");
                    editcurrentdeliver.setText("");

                } else if (Po_Item_List.get(0).getGTIN_STATUS1().equalsIgnoreCase("1")) {
                    editbarcode.setError("هذا الباركود GTIN_STATUS غير فعال");
                } else if (!Po_Item_List.get(0).getVMSTA1().equalsIgnoreCase("anyType{}")) {
                    editbarcode.setError("هذا الباركود VMSTA غير فعال");
                } else {

                    // Log.d("Po_Headersshorttext",""+Po_Item_List.get(0).getSHORT_TEXT1());
                    txtdescripation.setText(Po_Item_List.get(0).getSHORT_TEXT1());


                    //Log.d("Po_Headersshorttext", "" + Po_Item_List.get(0).getSHORT_TEXT1());
                    txtcodeitem.setText(Po_Item_List.get(0).getMATERIAL1());

                    //  Log.d("Po_Headersshorttext", "" + Po_Item_List.get(0).getMATERIAL1());

                    txtuniteitem.setText(Po_Item_List.get(0).getMEINH1());

                    txtstateitem.setText(Po_Item_List.get(0).getGTIN_STATUS1());

                    if (Po_Item_List.get(0).getPDNEWQTY1() != null) {
                        txtlastdeliver.setText(Po_Item_List.get(0).getPDNEWQTY1());
                        Log.d("TotalDeliver", "!= null");
                    } else {
                        txtlastdeliver.setText("0");
                        Log.d("TotalDeliver", "" + txtlastdeliver.getText().toString());
                    }

                    AskaedQuantity = Double.valueOf(Po_Item_List.get(0).getQUANTITY1());

                    editcurrentdeliver.setEnabled(true);
                    editcurrentdeliver.requestFocus();
                }
            }
        }else {
            editbarcode.setError("من فضلك ادخل الباركود");
            txtdescripation.setText("الوصف");
            txtcodeitem.setText("كود الصنف");
            txtuniteitem.setText("وحده الصنف");
            txtstateitem.setText("حاله الصنف");
            txtlastdeliver.setText("0");
            editcurrentdeliver.setText("");

        }


    }

    public void SaveDelivered(View view) {
        String Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();

        String barCode = editbarcode.getText().toString();
        String lastdeliver = txtlastdeliver.getText().toString();
        String PDNEWQTY = editcurrentdeliver.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String DeliveryDate = sdf.format(new Date());

        if (Po_item_NoMore.length() != 0) {
            Toast.makeText(this, "هذا الأمر تم استلامه برقم" + Po_item_NoMore, Toast.LENGTH_SHORT).show();
        } else if (barCode.isEmpty() /*|| txtlastdeliver.getText().toString().equalsIgnoreCase("0")*/) {
                editbarcode.setError("من فضلك أدخل الباركود");
            txtdescripation.setText("الوصف");
            txtcodeitem.setText("كود الصنف");
            txtuniteitem.setText("وحده الصنف");
            txtstateitem.setText("حاله الصنف");
                txtlastdeliver.setText("0");
        } else {
            //String barCode = editbarcode.getText().toString();
            if (Po_Item_List.size() == 0) {
                editbarcode.setError("الباركود غير موجود");
                txtdescripation.setText("الوصف");
                txtcodeitem.setText("كود الصنف");
                txtuniteitem.setText("وحده الصنف");
                txtstateitem.setText("حاله الصنف");
                txtlastdeliver.setText("0");
            }else {
         //       Po_Item_List = databaseHelper.Search_Barcode_Po_Item(barCode);
                String RequiredQY = Po_Item_List.get(0).getPO_UNIT1();
                Log.d("RequiredQY", "" + RequiredQY);
                Log.d("RequiredQY", "" + Po_Item_List.get(0).getMEINH1());
                //  RequiredQY.equalsIgnoreCase(Po_Item_List.get(0).getMEINH1())
                //  Po_Item_List;
                if (!barCode.isEmpty() && /*Double.valueOf(PDNEWQTY) > 0*/ !PDNEWQTY.isEmpty()&&
                        RequiredQY.equalsIgnoreCase(Po_Item_List.get(0).getMEINH1())) {

                    Double LDeliver = Double.valueOf(lastdeliver);
                    Log.d("TotalDeliver", "" + LDeliver);

                    Double CDeliver = Double.valueOf(PDNEWQTY);
                    Log.d("TotalDeliver", "" + CDeliver);

                    Double TotalDeliver = AskaedQuantity - (LDeliver + CDeliver);
                    Double Reminde = (LDeliver + CDeliver) - AskaedQuantity;

                    Log.d("TotalDeliver", "" + TotalDeliver);

                    if (TotalDeliver < 0) {
                        editcurrentdeliver.setError(   "هذه الكميه أكبر ب" +(Reminde)  );
                    } else {
                        String STotalDeliver = String.valueOf(LDeliver + CDeliver);
                        txtlastdeliver.setText(String.valueOf(
                                new DecimalFormat("###.##").format(Double.valueOf(LDeliver + CDeliver))));
                        int ID = databaseHelper.update_PDNEWQTY(Po_Item_List.get(0).getID1(),
                                new DecimalFormat("###.##").format(Double.valueOf(STotalDeliver))
                                , DeliveryDate /*, txtvendorname.getText().toString()*/);

                       //  databaseHelper.update_po_iteam_nomoreGR_with_X(Po_Item_List.get(0).getPO_ITEM1());

                        editcurrentdeliver.setText("");
                        editcurrentdeliver.setHint("Done");
                        editcurrentdeliver.setHintTextColor(getResources().getColor(R.color.first_blue));
                        Log.d("update_PDNEWQTYID", "" + STotalDeliver);
                        Log.d("update_PDNEWQTYID", "" + ID);
                        Log.d("update_PDNEWQTYID", "" + DeliveryDate);

                        String SeralForMaterial = Po_Item_List.get(0).getSERNP1();
                        editbarcode.setText("");
                        editbarcode.requestFocus();

                        if (SeralForMaterial.equalsIgnoreCase("anytype{}") ||
                                SeralForMaterial.equalsIgnoreCase("")) {
                            Toast.makeText(ScanRecievingActivity.this, "لايوجد سيريال لهذا الباركود", Toast.LENGTH_LONG).show();
                        } else {
                            Intent GoToAddSerialOfThisMaterail = new Intent(ScanRecievingActivity.this, NewSerialFormActivity.class);
                            GoToAddSerialOfThisMaterail.putExtra("PDNEWQTY", STotalDeliver);
                            GoToAddSerialOfThisMaterail.putExtra("barCode", barCode);
                            Log.d("putExtra", "" + Po_Item_List.get(0).getEAN111());
                            GoToAddSerialOfThisMaterail.putExtra("Po_Item_of_cycleCount", Po_Item_List.get(0).getPO_ITEM1());
                            Log.d("putExtra", "" + Po_Item_List.get(0).getPO_ITEM1());
                            GoToAddSerialOfThisMaterail.putExtra("Material", Po_Item_List.get(0).getMATERIAL1());
                            GoToAddSerialOfThisMaterail.putExtra("ShortText", Po_Item_List.get(0).getSHORT_TEXT1());

                            Log.d("putExtra", "" + Po_Item_List.get(0).getMATERIAL1());
                            startActivity(GoToAddSerialOfThisMaterail);
                        }
                    }
                } else {
                    if (PDNEWQTY.isEmpty()) {
                        editcurrentdeliver.setError("من فضلك أدخل الكميه المستلمه");
                    }/*if (PDNEWQTY.isEmpty()) {
                        editcurrentdeliver.setError("من فضلك أدخل الكميه المستلمه");
                    }*/if (!RequiredQY.equalsIgnoreCase(Po_Item_List.get(0).getMEINH1())) {
                        editcurrentdeliver.setError( RequiredQY + "هذه ليست الوحده المطلوبه ... المطلوب " );
                    }
                }

            }
        }

    }

    public void Go_To_Upload_Form(View view) {
        String Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();
        Date Date=null,DCreateDate=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
             Date = sdf.parse(sdf.format(new Date()));
            String CreateDate = databaseHelper.select_CreateDate();

            if (!CreateDate.isEmpty()){
                 DCreateDate = sdf.parse(CreateDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("zzzCreateDate",""+DCreateDate);
        Log.e("zzzCreateDate",""+Date);

        if (Po_item_NoMore.length() != 0) {
            Toast.makeText(this, "هذا الأمر تم استلامه برقم" + Po_item_NoMore, Toast.LENGTH_SHORT).show();
        } else if (DCreateDate.after(Date)) {     //TODO Remove This is for Prod
            Toast.makeText(this, "تاريخ الجهاز غير مضبوط" + Po_item_NoMore, Toast.LENGTH_SHORT).show();
        }else {

            Po_Item_For_Log_only_Has_value= new ArrayList<>();

            Po_Item_For_Log_only_Has_value = databaseHelper.Get_Po_Item_That_Has_PDNewQTy();


            if (Po_Item_For_Log_only_Has_value.size()>0) {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.numberofitemstoupload)+Po_Item_For_Log_only_Has_value.size()+"عنصر")
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent GoToUpload = new Intent(ScanRecievingActivity.this, UploadActivity.class);
                                    startActivity(GoToUpload);
                                }
                            }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    }).show();

            }else {
                editcurrentdeliver.setError("كل القم ب 0.0");
            }
        }
    }

    public void Show_Deliver_Data(View view) {
        String Po_item_NoMore = databaseHelper.select_what_has_NoMore_value();

        if (Po_item_NoMore.length() != 0) {
            Toast.makeText(this, "هذا الأمر تم استلامه برقم" + Po_item_NoMore, Toast.LENGTH_SHORT).show();
        }else {
            Intent goTocheckItemForm = new Intent(ScanRecievingActivity.this, CheckItemFormActivity.class);
            startActivity(goTocheckItemForm);
        }
    }

    public void Show_Buy_Data(View view) {
        Intent GoToShowBuyData =new Intent(ScanRecievingActivity.this,ShowBuyDataActivity.class);
        startActivity(GoToShowBuyData);
    }


    private String Calculatcheckdigitforscales(String toString) {
        String Barcode;
        int  chkdigit;
        Log.e("zzzbarodd1 ",""+toString.charAt(1));
        Log.e("zzzbarodd1 ",""+Integer.valueOf(toString.charAt(1)));
        Log.e("zzzbarodd1.2 ",""+Integer.valueOf(toString.substring(1,2)));
        Log.e("zzzbarodd11 ",""+toString.charAt(11));
        Log.e("zzzbarodd11.12 ",""+Integer.valueOf(toString.substring(11,12)));
        int odd  = Integer.valueOf(toString.substring(1,2)) + Integer.valueOf(toString.substring(3,4)) + Integer.valueOf(toString.substring(5,6)) + Integer.valueOf(toString.substring(7,8)) + Integer.valueOf(toString.substring(9,10)) + Integer.valueOf(toString.substring(11,12));
        int eveen  = Integer.valueOf(toString.substring(0,1)) + Integer.valueOf(toString.substring(2,3)) + Integer.valueOf(toString.substring(4,5)) + Integer.valueOf(toString.substring(6,7)) + Integer.valueOf(toString.substring(8,9)) + Integer.valueOf(toString.substring(10,11));

        Log.e("zzzbarodd",""+odd);
        Log.e("zzzbareveen",""+eveen);
        if ((((odd * 3) + eveen) % 10) != 0 )
            chkdigit = 10 - (((odd * 3) + eveen) % 10) ;
        else
            chkdigit = 0 ;

        Barcode=toString +chkdigit;
        Log.e("zzzbarcode",""+Barcode);
        return Barcode;
    }


    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(ScanRecievingActivity.this,ReceivingActivity.class);
        Go_Back.putExtra("UserName",Po_HeaderList.get(0).getDelievered_BY1());
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
