package com.example.connecttosoapapiapp.Promotion.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForProotion extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForProotion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create prootion
    //    db.execSQL(Prom_item_Module.CREATE_Prom_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
     //   db.execSQL("DROP TABLE IF EXISTS " + Prom_item_Module.TABLE_Prom_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insertProitem(String Discountno, String Date_from,
                                 String Date_to,
                                 String Discounttype, String Prom_desc, String last_modified_time,
                                 String prom_post, String status,String itemean
                                 , String department  ,   String barcode,String item_desc,String return_type,
                              String sell_price, String vatrate,String discountvalue,String note_id
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Prom_item_Module.Discountno, Discountno);
        values.put(Prom_item_Module.Date_from, Date_from);
        values.put(Prom_item_Module.Date_to, Date_to);
        values.put(Prom_item_Module.Discounttype, Discounttype);
        values.put(Prom_item_Module.Prom_desc, Prom_desc);
        values.put(Prom_item_Module.last_modified_time, last_modified_time);
        values.put(Prom_item_Module.prom_post, prom_post);
        values.put(Prom_item_Module.status, status);
        values.put(Prom_item_Module.itemean,itemean);
        values.put(Prom_item_Module.department, department);
        values.put(Prom_item_Module.barcode,barcode);
        values.put(Prom_item_Module.item_desc,item_desc);
        values.put(Prom_item_Module.return_type,return_type);
       // values.put(Prom_item_Module.qty_std_price,qty_std_price);
        values.put(Prom_item_Module.sell_price,sell_price);
        values.put(Prom_item_Module.vatrate,vatrate);
        values.put(Prom_item_Module.discountvalue,discountvalue);
        if (!note_id.equalsIgnoreCase("0,")) {
            values.put(Prom_item_Module.note_id, note_id.substring(0,note_id.indexOf(",")));
            values.put(Prom_item_Module.OtherNotes,note_id.substring(note_id.indexOf(",")+1));
        }else
            values.put(Prom_item_Module.note_id, note_id.substring(0,note_id.indexOf(",")));



        // insert row
        long id = db.insert(Prom_item_Module.TABLE_Prom_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }


       public List<Prom_item_Module> selectPromItemsAll(){
        List<Prom_item_Module> prom_item_moduleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Prom_item_Module.TABLE_Prom_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Prom_item_Module prom_item_module = new Prom_item_Module();

                prom_item_module.setDiscountno1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discountno)));
                prom_item_module.setDate_from1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_from)));
                prom_item_module.setDate_to1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_to)));
                prom_item_module.setDiscounttype1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discounttype)));
                prom_item_module.setProm_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Prom_desc)));
                prom_item_module.setLast_modified_time1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.last_modified_time)));
                prom_item_module.setProm_post1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.prom_post)));
                prom_item_module.setStatus1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.status)));
                prom_item_module.setItemean1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.itemean)));
                prom_item_module.setDepartment1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.department)));
                prom_item_module.setReturn_type1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.return_type)));
                prom_item_module.setSell_price1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.sell_price)));
                prom_item_module.setVatrate1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.vatrate)));
                prom_item_module.setDiscountvalue1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.discountvalue)));

                prom_item_module.setBarcode1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.barcode)));
                prom_item_module.setItem_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.item_desc)));

                prom_item_module.setNote_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));
                prom_item_module.setOtherNotes1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.OtherNotes)));
                prom_item_module.setSupervisor_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_id)));
                prom_item_module.setSupervisor_name1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_name)));


                Log.d("zznoteid",""+cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));

//                prom_item_module.setChecked_Item(false);

                prom_item_moduleList.add(prom_item_module);
                if (prom_item_moduleList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+prom_item_moduleList.size());
        // return Po_item list
        return prom_item_moduleList;
    }

    public List<Prom_item_Module> selectPromItemsDistinctDiscountNoWithoutNoteId(){
        List<Prom_item_Module> prom_item_moduleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT distinct "+Prom_item_Module.Discountno+","+Prom_item_Module.Date_from+","+
                Prom_item_Module.Date_to
               +","+Prom_item_Module.Discounttype
               // +"," + selectPromItemsThatHas(Prom_item_Module.Discountno)
                /*+"," + Prom_item_Module.Prom_desc+","+
                Prom_item_Module.last_modified_time+","+ Prom_item_Module.prom_post+","+ Prom_item_Module.status+","+
                Prom_item_Module.itemean+","+ Prom_item_Module.department+","+ Prom_item_Module.return_type+","+
                Prom_item_Module.sell_price+","+ Prom_item_Module.vatrate+","+ Prom_item_Module.discountvalue+","+
                Prom_item_Module.barcode+","+Prom_item_Module.item_desc+","+ Prom_item_Module.note_id+","+
                Prom_item_Module.OtherNotes+","+Prom_item_Module.supervisor_id+","+Prom_item_Module.supervisor_name*/

                +" FROM " + Prom_item_Module.TABLE_Prom_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Prom_item_Module prom_item_module = new Prom_item_Module();

                prom_item_module.setDiscountno1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discountno)));
                prom_item_module.setDate_from1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_from)));
                prom_item_module.setDate_to1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_to)));
                prom_item_module.setDiscounttype1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discounttype)));
                prom_item_module.setNote_id1(selectPromItemsThatHas(prom_item_module.getDiscountno1()));

                //     prom_item_module.setNote_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));
//                prom_item_module.setProm_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Prom_desc)));
       //         prom_item_module.setLast_modified_time1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.last_modified_time)));
        //        prom_item_module.setProm_post1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.prom_post)));
       //         prom_item_module.setStatus1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.status)));
         //       prom_item_module.setItemean1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.itemean)));
           //     prom_item_module.setDepartment1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.department)));
             //   prom_item_module.setReturn_type1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.return_type)));
  //              prom_item_module.setSell_price1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.sell_price)));
    //            prom_item_module.setVatrate1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.vatrate)));
      //          prom_item_module.setDiscountvalue1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.discountvalue)));

        //        prom_item_module.setBarcode1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.barcode)));
          //      prom_item_module.setItem_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.item_desc)));

//
            //    prom_item_module.setOtherNotes1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.OtherNotes)));
          //      prom_item_module.setSupervisor_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_id)));
        //        prom_item_module.setSupervisor_name1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_name)));


//                Log.d("zznoteid",""+cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));

//                prom_item_module.setChecked_Item(false);

                prom_item_moduleList.add(prom_item_module);
                if (prom_item_moduleList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+prom_item_moduleList.size());
        // return Po_item list
        return prom_item_moduleList;
    }

    public String selectPromItemsThatHas(String Discountno){
        List<String> prom_item_modulenoteList = new ArrayList<>();
        String ReturnNote="0";
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT "+ Prom_item_Module.note_id +" FROM " + Prom_item_Module.TABLE_Prom_NAME +" Where "
                +Prom_item_Module.Discountno +"="+Discountno ;
//                +Prom_item_Module.note_id +" !=0 "+" or "+
//        Prom_item_Module.note_id +" != "+" null " ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
         Log.d("zzselectPsThatHas",""+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                prom_item_modulenoteList.add(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));

                if (prom_item_modulenoteList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+prom_item_modulenoteList.size());

        if (prom_item_modulenoteList.contains("0")){
            ReturnNote="0";
        }else if (!prom_item_modulenoteList.contains("0")){
            ReturnNote=prom_item_modulenoteList.get(prom_item_modulenoteList.size()-1);
        }
        // return Po_item list
        return ReturnNote;
    }

    public  List<String> selectPromItemsForInsetall(String Discountno){
        List<String> prom_item_modulenoteList = new ArrayList<>();
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT "+ Prom_item_Module.note_id +" FROM " +
                Prom_item_Module.TABLE_Prom_NAME +" Where "
               // +Prom_item_Module.note_id +" =0 "+" and "
                +Prom_item_Module.Discountno +" = "+Discountno +" and "
                +Prom_item_Module.OtherNotes +" !=0";
//                +Prom_item_Module.note_id +" !=0 "+" or "+
//        Prom_item_Module.note_id +" != "+" null " ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";
        Log.d("zzselectQuery",""+selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("zzselomItemsThatHas",""+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                prom_item_modulenoteList.add(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));

                if (prom_item_modulenoteList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("zzItemsForInsesize",""+prom_item_modulenoteList.size());

        // return Po_item list
        return prom_item_modulenoteList;
    }

    public List<Prom_item_Module> selectPromItems(String Discountno){
        List<Prom_item_Module> prom_item_moduleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Prom_item_Module.TABLE_Prom_NAME
                +" where "+Prom_item_Module.Discountno + "= '"+Discountno+"'";

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+STo_Search.REC_SITE_LOG+" = '"+rec_stg_log+ "' AND "+STo_Search.GTIN+" = "+Barcode;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Prom_item_Module prom_item_module = new Prom_item_Module();

                prom_item_module.setDiscountno1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discountno)));
                prom_item_module.setDate_from1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_from)));
                prom_item_module.setDate_to1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_to)));
                prom_item_module.setDiscounttype1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discounttype)));
                prom_item_module.setProm_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Prom_desc)));
                prom_item_module.setLast_modified_time1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.last_modified_time)));
                prom_item_module.setProm_post1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.prom_post)));
                prom_item_module.setStatus1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.status)));
                prom_item_module.setItemean1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.itemean)));
                prom_item_module.setDepartment1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.department)));
                prom_item_module.setReturn_type1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.return_type)));
                prom_item_module.setSell_price1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.sell_price)));
                prom_item_module.setVatrate1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.vatrate)));
                prom_item_module.setDiscountvalue1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.discountvalue)));

                prom_item_module.setBarcode1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.barcode)));
                prom_item_module.setItem_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.item_desc)));

                prom_item_module.setNote_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));
                prom_item_module.setOtherNotes1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.OtherNotes)));
                prom_item_module.setSupervisor_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_id)));
                prom_item_module.setSupervisor_name1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_name)));


//                prom_item_module.setChecked_Item(false);

                prom_item_moduleList.add(prom_item_module);
                if (prom_item_moduleList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+prom_item_moduleList.size());
        // return Po_item list
        return prom_item_moduleList;
    }

    public List<Prom_item_Module> selectSupervisernameandID(){
        List<Prom_item_Module> prom_item_moduleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Prom_item_Module.TABLE_Prom_NAME
                +" where "+Prom_item_Module.supervisor_id + " > 0";

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+STo_Search.REC_SITE_LOG+" = '"+rec_stg_log+ "' AND "+STo_Search.GTIN+" = "+Barcode;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("zzselemItemsThatHas",""+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Prom_item_Module prom_item_module = new Prom_item_Module();

                prom_item_module.setSupervisor_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_id)));
                prom_item_module.setSupervisor_name1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_name)));


//                prom_item_module.setChecked_Item(false);

                prom_item_moduleList.add(prom_item_module);
                if (prom_item_moduleList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsizename",""+prom_item_moduleList.size());
        // return Po_item list
        return prom_item_moduleList;
    }

    public List<Prom_item_Module> selectPromItemsByBarcode(String barcode){
        List<Prom_item_Module> prom_item_moduleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Prom_item_Module.TABLE_Prom_NAME
                +" where "+Prom_item_Module.barcode + "= '"+barcode+"'";

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+STo_Search.REC_SITE_LOG+" = '"+rec_stg_log+ "' AND "+STo_Search.GTIN+" = "+Barcode;

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Prom_item_Module prom_item_module = new Prom_item_Module();

                prom_item_module.setDiscountno1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discountno)));
                prom_item_module.setDate_from1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_from)));
                prom_item_module.setDate_to1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Date_to)));
                prom_item_module.setDiscounttype1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Discounttype)));
                prom_item_module.setProm_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.Prom_desc)));
                prom_item_module.setLast_modified_time1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.last_modified_time)));
                prom_item_module.setProm_post1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.prom_post)));
                prom_item_module.setStatus1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.status)));
                prom_item_module.setItemean1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.itemean)));
                prom_item_module.setDepartment1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.department)));
                prom_item_module.setReturn_type1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.return_type)));
                prom_item_module.setSell_price1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.sell_price)));
                prom_item_module.setVatrate1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.vatrate)));
                prom_item_module.setDiscountvalue1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.discountvalue)));

                prom_item_module.setBarcode1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.barcode)));
                prom_item_module.setItem_desc1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.item_desc)));

                prom_item_module.setNote_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.note_id)));
                prom_item_module.setOtherNotes1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.OtherNotes)));
                prom_item_module.setSupervisor_id1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_id)));
                prom_item_module.setSupervisor_name1(cursor.getString(cursor.getColumnIndex(Prom_item_Module.supervisor_name)));


//                prom_item_module.setChecked_Item(false);

                prom_item_moduleList.add(prom_item_module);
                if (prom_item_moduleList.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+prom_item_moduleList.size());
        // return Po_item list
        return prom_item_moduleList;
    }
/*
    public List<STO_Header> selectSto_Header(){
        List<STO_Header> STo_Headerlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + STO_Header.TABLE_STO_HEADER_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                STO_Header STo_header = new STO_Header();

                STo_header.setIss_Site1(cursor.getString(cursor.getColumnIndex(STO_Header.Iss_Site)));
                STo_header.setRec_Site1(cursor.getString(cursor.getColumnIndex(STO_Header.Rec_Site)));

                STo_header.setIss_Strg_Log1(cursor.getString(cursor.getColumnIndex(STO_Header.Iss_Strg_Log)));
                STo_header.setRec_Site_log1(cursor.getString(cursor.getColumnIndex(STO_Header.Rec_Site_log)));
                STo_header.setComp_Code1(cursor.getString(cursor.getColumnIndex(STO_Header.Comp_Code)));
                STo_header.setP_Grp1(cursor.getString(cursor.getColumnIndex(STO_Header.P_Grp)));
                STo_header.setP_Org1(cursor.getString(cursor.getColumnIndex(STO_Header.P_Org)));
                STo_header.setSto_CR_Date1(cursor.getString(cursor.getColumnIndex(STO_Header.Sto_CR_Date)));
                STo_header.setSto_Del_Date1(cursor.getString(cursor.getColumnIndex(STO_Header.Sto_Del_Date)));
                STo_header.setSto_Type1(cursor.getString(cursor.getColumnIndex(STO_Header.Sto_Type)));

                STo_Headerlist.add(STo_header);
                if (STo_Headerlist.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize",""+STo_Headerlist.size());
        // return Po_item list
        return STo_Headerlist;
    }


  /* public long Update_Sto_Search(String Material , String Qty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STO_Header.Rec_Site_log, rec_Site_log1);

        // updating row
        return db.update(STO_Header.TABLE_STO_HEADER_NAME, values, null,
                null);
    }



  public List<STo_Search> selectSto_Search_for_Qty(){
      List<STo_Search> STo_searchlist = new ArrayList<>();

      // get writable database as we want to write data
      SQLiteDatabase db = this.getWritableDatabase();

      String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
              + " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0";
      // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

      Cursor cursor = db.rawQuery(selectQuery, null);
      // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

      // looping through all rows and adding to list
      if (cursor.moveToFirst()) {
          do {

              STo_Search STo_search = new STo_Search();

              STo_search.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
              STo_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(STo_Search.MAT_CODE)));
              STo_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(STo_Search.STATUS)));
              STo_search.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(STo_Search.AVAILABLE_STOCK)));
              STo_search.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.ISS_STG_LOG)));
              STo_search.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.REC_SITE_LOG)));
              STo_search.setMEINH1(cursor.getString(cursor.getColumnIndex(STo_Search.MEINH)));
              STo_search.setGTIN1(cursor.getString(cursor.getColumnIndex(STo_Search.GTIN)));
              STo_search.setQTY1(cursor.getString(cursor.getColumnIndex(STo_Search.QTY)));
              STo_search.setChecked_Item(false);

              STo_searchlist.add(STo_search);
              if (STo_searchlist.isEmpty()){
                  Log.d("po_itemlist","empty");
              }
              //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
              //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
          } while (cursor.moveToNext());
      }
      cursor.close();
      // close db connection
      db.close();

      Log.d("po_itemlistsize",""+STo_searchlist.size());
      // return Po_item list
      return STo_searchlist;
  }

    public List<STo_Search> Search_if_Barcode_in_localDataBase(String iss_stg_log ,String rec_stg_log , String Barcode ){
        List<STo_Search> STo_searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
                + " WHERE " +STo_Search.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+STo_Search.REC_SITE_LOG+" = '"+rec_stg_log+ "' AND "+STo_Search.GTIN+" = "+Barcode;
//        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = "+iss_stg_log +" AND "+STo_Search.GTIN+" = "+Barcode;

        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                STo_Search STo_search = new STo_Search();

                STo_search.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
                STo_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(STo_Search.MAT_CODE)));
                STo_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(STo_Search.STATUS)));
                STo_search.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(STo_Search.AVAILABLE_STOCK)));
                STo_search.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.ISS_STG_LOG)));
                STo_search.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.REC_SITE_LOG)));
                STo_search.setISS_SITE1(cursor.getString(cursor.getColumnIndex(STo_Search.ISS_SITE)));
                STo_search.setREC_SITE1(cursor.getString(cursor.getColumnIndex(STo_Search.REC_SITE)));
                STo_search.setMEINH1(cursor.getString(cursor.getColumnIndex(STo_Search.MEINH)));
                STo_search.setGTIN1(cursor.getString(cursor.getColumnIndex(STo_Search.GTIN)));
                STo_search.setQTY1(cursor.getString(cursor.getColumnIndex(STo_Search.QTY)));
                STo_search.setChecked_Item(false);

                STo_searchlist.add(STo_search);
                if (STo_searchlist.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize","rch_if_Ba"+STo_searchlist.size());
        // return Po_item list
        return STo_searchlist;
    }


    public List<STo_Search> Search__Barcode(String Barcode ){
        List<STo_Search> STo_searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
                + " where "+STo_Search.GTIN+"="+Barcode;
                //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                STo_Search STo_search = new STo_Search();

                STo_search.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
                STo_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(STo_Search.MAT_CODE)));
                STo_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(STo_Search.STATUS)));
                STo_search.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(STo_Search.AVAILABLE_STOCK)));
                STo_search.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.ISS_STG_LOG)));
                STo_search.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(STo_Search.REC_SITE_LOG)));
                STo_search.setISS_SITE1(cursor.getString(cursor.getColumnIndex(STo_Search.ISS_SITE)));
                STo_search.setREC_SITE1(cursor.getString(cursor.getColumnIndex(STo_Search.REC_SITE)));
                STo_search.setMEINH1(cursor.getString(cursor.getColumnIndex(STo_Search.MEINH)));
                STo_search.setGTIN1(cursor.getString(cursor.getColumnIndex(STo_Search.GTIN)));
                STo_search.setQTY1(cursor.getString(cursor.getColumnIndex(STo_Search.QTY)));
                STo_search.setChecked_Item(false);

                STo_searchlist.add(STo_search);
                if (STo_searchlist.isEmpty()){
                    Log.d("po_itemlist","empty");
                }
                //   Log.d("Po_Headersclass",""+po_item.getMEINH1());
                //Log.d("Po_Headersclasslist",""+po_itemlist.get(0).getMEINH1());


//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
//            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        Log.d("po_itemlistsize","rch_if_Ba"+STo_searchlist.size());
        // return Po_item list
        return STo_searchlist;
    }


    public String select_what_has_Status_value() {
        String StatusNU="";
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});

        // Select All Query
        String selectQuery = "SELECT * FROM " + STO_Header.TABLE_STO_HEADER_NAME + " where " + STO_Header.Status +"!=" +"null or "+
                STO_Header.Status +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StatusNU = cursor.getString(cursor.getColumnIndex(STO_Header.Status));

            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();
        // return Po_item list
        return StatusNU;
    }

    /*update للكميه المستله سابقا
    public int update_PDNEWQTY_onsearch(String iss_StG_Log, String PDNEWQTY) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STo_Search.QTY, PDNEWQTY);


        // updating row
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.ISS_STG_LOG + " = ?",
                new String[]{iss_StG_Log});
    }

    public long Update_Sto_search_For_QTY(String QTY,String iss_stg_log,String Res_stg_log, String Barcod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STo_Search.QTY, QTY);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.ISS_STG_LOG+ " = ? and "+STo_Search.REC_SITE_LOG+" = ? and "+STo_Search.GTIN+" = ? ", new String[]{iss_stg_log ,Res_stg_log, Barcod});
    }


    */

    public long Update_noteid_For_discuntno(String discuntno,String noteid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        // values.put(Prom_item_Module.Discountno, discuntno);
        values.put(Prom_item_Module.note_id, noteid);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Prom_item_Module.TABLE_Prom_NAME, values, Prom_item_Module.Discountno+ " = ? ", new String[]{discuntno});
    }

    public long Update_noteid_Userid_and_username_For_Barcode(String Barcode, String Userid, String username, String noteid, String noteother) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
       // values.put(Prom_item_Module.Discountno, discuntno);
        values.put(Prom_item_Module.supervisor_id, Userid);
        values.put(Prom_item_Module.supervisor_name, username);
        values.put(Prom_item_Module.note_id, noteid);
        values.put(Prom_item_Module.OtherNotes, noteother);
        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Prom_item_Module.TABLE_Prom_NAME, values, Prom_item_Module.barcode+ " = ? ", new String[]{Barcode});
    }

    public long Update_Userid_and_username_For_Alldiscuntnoforuploadforfirsttime(String discuntno,String Userid,String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        // values.put(Prom_item_Module.Discountno, discuntno);
        values.put(Prom_item_Module.supervisor_id, Userid);
        values.put(Prom_item_Module.supervisor_name, username);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Prom_item_Module.TABLE_Prom_NAME, values, Prom_item_Module.Discountno+ " = ? ", new String[]{discuntno});
    }
    public long Update_noteid_and_noteother_For_Barcode(String Barcode, String noteid, String noteother) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        // values.put(Prom_item_Module.Discountno, discuntno);

        values.put(Prom_item_Module.note_id, noteid);
        values.put(Prom_item_Module.OtherNotes, noteother);
        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Prom_item_Module.TABLE_Prom_NAME, values, Prom_item_Module.barcode+ " = ? ", new String[]{Barcode});
    }
    public long Update_noteid_Userid_and_username_For_Alldiscuntno(String discuntno,String Userid,String username,String noteid,String noteother) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        // values.put(Prom_item_Module.Discountno, discuntno);
        values.put(Prom_item_Module.supervisor_id, Userid);
        values.put(Prom_item_Module.supervisor_name, username);
        values.put(Prom_item_Module.note_id, noteid);
        values.put(Prom_item_Module.OtherNotes, noteother);
        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Prom_item_Module.TABLE_Prom_NAME, values, Prom_item_Module.Discountno+ " = ? ", new String[]{discuntno});
    }

    public void DeletePromItems(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " + Prom_item_Module.TABLE_Prom_NAME);

        db.close();
    }

}
