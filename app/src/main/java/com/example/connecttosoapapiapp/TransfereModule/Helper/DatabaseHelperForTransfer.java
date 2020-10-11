package com.example.connecttosoapapiapp.TransfereModule.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForTransfer extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForTransfer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create transfere
        db.execSQL(STO_Header.CREATE_STO_HEADER_TABLE);
        db.execSQL(STo_Search.CREATE_STO_SEARCH_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + STO_Header.TABLE_STO_HEADER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STo_Search.TABLE_STO_Search_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insertSto_header(String sto_CR_Date1, String iss_Site1,
                                 String comp_Code1,
                                 String p_Org1, String p_Grp1, String rec_Site1,
                                 String sto_Type1, String sto_Del_Date1,String REC_SITE_LOG1
                                 //, String NO_MORE_GR     String ISS_STG_LOG1,
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(STO_Header.Sto_CR_Date, sto_CR_Date1);
        values.put(STO_Header.Iss_Site, iss_Site1);
        values.put(STO_Header.Comp_Code, comp_Code1);
        values.put(STO_Header.P_Org, p_Org1);
        values.put(STO_Header.P_Grp, p_Grp1);
        values.put(STO_Header.Rec_Site, rec_Site1);
        values.put(STO_Header.Sto_Type, sto_Type1);
        values.put(STO_Header.Sto_Del_Date, sto_Del_Date1);
        //values.put(STO_Header.Iss_Strg_Log,ISS_STG_LOG1);
        values.put(STO_Header.Rec_Site_log,REC_SITE_LOG1);

        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(STO_Header.TABLE_STO_HEADER_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insert_Sto_Search(String GTIN1, String ISS_SITE1, String REC_SITE1, String MAT_CODE1, String UOM_DESC1,
                                  String MEINH1, String UOM1, String STATUS1,
                                  String ISS_STG_LOG1, String REC_SITE_LOG1, String AVAILABLE_STOCK1, String EKGRP1 ,String Qty) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(STo_Search.GTIN, GTIN1);
        values.put(STo_Search.ISS_SITE, ISS_SITE1);
        values.put(STo_Search.REC_SITE, REC_SITE1);
        values.put(STo_Search.MAT_CODE, MAT_CODE1);
        values.put(STo_Search.UOM_DESC, UOM_DESC1);
        values.put(STo_Search.MEINH, MEINH1);
        values.put(STo_Search.UOM, UOM1);
        values.put(STo_Search.STATUS, STATUS1);
        values.put(STo_Search.ISS_STG_LOG, ISS_STG_LOG1);
        values.put(STo_Search.REC_SITE_LOG, REC_SITE_LOG1);
        values.put(STo_Search.AVAILABLE_STOCK, AVAILABLE_STOCK1);
        values.put(STo_Search.EKGRP, EKGRP1);
        values.put(STo_Search.QTY, Qty);
        //values.put(STo_Search.NO_MORE_GR, NO_MORE_GR);
        // insert row
        long id = db.insert(STo_Search.TABLE_STO_Search_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }

    public long Update_Sto_search_For_Rec_Site(String rec_Site_log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STo_Search.REC_SITE_LOG, rec_Site_log1);

        // updating row
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, null, null);
    }

    public long Update_Sto_Header_For_Rec_Site_log(String rec_Site_log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STO_Header.Rec_Site_log, rec_Site_log1);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(STO_Header.TABLE_STO_HEADER_NAME, values, null, null);
    }
    public long Update_Sto_header_For_Iss_Site_log(String iss_Strg_Log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);

        // updating row
        return db.update(STO_Header.TABLE_STO_HEADER_NAME, values, null,
                null);
    }
    public long Update_Sto_header_For_Iss_Site_log_if_has_anytypevalue(String iss_Strg_Log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STo_Search.ISS_STG_LOG, iss_Strg_Log1);

        // updating row
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.ISS_STG_LOG + " = ?",
                new String[]{"anyType{}"});
    }
    public long Update_Sto_Header_For_Status(String STO_NO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STO_Header.Status, STO_NO);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(STO_Header.TABLE_STO_HEADER_NAME, values, null, null);
    }


    public List<STo_Search> selectSto_Search(){
        List<STo_Search> STo_searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME ;
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

        Log.d("po_itemlistsize",""+STo_searchlist.size());
        // return Po_item list
        return STo_searchlist;
    }

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
                STo_header.setStatus1(cursor.getString(cursor.getColumnIndex(STO_Header.Status)));

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
    }*/



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
                + " where "+STo_Search.GTIN+"='"+Barcode+"'";
                //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";
         Log.e("zzzzQueryforselect","Q "+selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

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
         */
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
    }*/

    public long Update_Sto_search_For_QTY(String QTY,String iss_stg_log,String Res_stg_log, String Barcod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STo_Search.QTY, QTY);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.ISS_STG_LOG+ " = ? and "+STo_Search.REC_SITE_LOG+" = ? and "+STo_Search.GTIN+" = ? ", new String[]{iss_stg_log ,Res_stg_log, Barcod});
    }

    public void DeleteBarcodeStosearchTable(String Barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +STo_Search.TABLE_STO_Search_NAME +" where "+STo_Search.GTIN+" = "+Barcode);

        db.close();
    }

    public void DeleteStoHeaderTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +STO_Header.TABLE_STO_HEADER_NAME);

        db.close();
    }

    public void DeleteStoSearchTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +STo_Search.TABLE_STO_Search_NAME);

        db.close();
    }
}
