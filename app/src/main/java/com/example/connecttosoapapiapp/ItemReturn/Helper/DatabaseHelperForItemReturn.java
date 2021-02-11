package com.example.connecttosoapapiapp.ItemReturn.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForItemReturn extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForItemReturn(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create ItemReturn
//        db.execSQL(Item_Return_Header.CREATE_Item_Return_Header_TABLE);
//        db.execSQL(Item_Return_Search.CREATE_Item_Return_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Item_Return_Header.TABLE_STO_HEADER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item_Return_Search.TABLE_Item_Return_Search_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insertItem_Return_Header(String Vendor, String p_org,
                                 String pgrp,
                                 String sitecode,
                                 String Status
                                 //, String NO_MORE_GR     String ISS_STG_LOG1,
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Item_Return_Header.Vendor, Vendor);
        values.put(Item_Return_Header.p_org, p_org);
        values.put(Item_Return_Header.pgrp, pgrp);
        values.put(Item_Return_Header.Site, sitecode);
        values.put(Item_Return_Header.Status, Status);


        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Item_Return_Header.TABLE_STO_HEADER_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insert_Item_Return_Search(String GTIN, String Desc, String MAT_CODE, String STATUS,String Qty,
                                          String UOM,String P_GRP,String P_ORG,String STG_LOC,String DEF_STG_LOC ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Item_Return_Search.GTIN, GTIN);
        values.put(Item_Return_Search.Desc, Desc);
        values.put(Item_Return_Search.MAT_CODE, MAT_CODE);
        values.put(Item_Return_Search.STATUS, STATUS);
        values.put(Item_Return_Search.QTY, Qty);

        values.put(Item_Return_Search.UOM, UOM);
        values.put(Item_Return_Search.P_GRP, P_GRP);
        values.put(Item_Return_Search.P_ORG, P_ORG);
        values.put(Item_Return_Search.STG_LOC, STG_LOC);
        values.put(Item_Return_Search.DEF_STG_LOC, DEF_STG_LOC);


        //values.put(STo_Search.NO_MORE_GR, NO_MORE_GR);
        // insert row
        long id = db.insert(Item_Return_Search.TABLE_Item_Return_Search_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }

    /*public long Update_Sto_search_For_Rec_Site(String rec_Site_log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(STo_Search.REC_SITE_LOG, rec_Site_log1);

        // updating row
        return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, null, null);
    }*/



    public long Update_Sto_Header_For_Status(String STO_NO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(Item_Return_Header.Status, STO_NO);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Item_Return_Header.TABLE_STO_HEADER_NAME, values, null, null);
    }


    public List<Item_Return_Header> selectReturn_Header(){
        List<Item_Return_Header> Item_Return_Headerlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Item_Return_Header.TABLE_STO_HEADER_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Item_Return_Header item_return_header = new Item_Return_Header();

                item_return_header.setVendor1(cursor.getString(cursor.getColumnIndex(Item_Return_Header.Vendor)));
                item_return_header.setP_org1(cursor.getString(cursor.getColumnIndex(Item_Return_Header.p_org)));
                item_return_header.setPgrp1(cursor.getString(cursor.getColumnIndex(Item_Return_Header.pgrp)));
                item_return_header.setStatus1(cursor.getString(cursor.getColumnIndex(Item_Return_Header.Status)));

            //    item_return_header.setChecked_Item(false);

                Item_Return_Headerlist.add(item_return_header);
                if (Item_Return_Headerlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+Item_Return_Headerlist.size());
        // return Po_item list
        return Item_Return_Headerlist;
    }

    public List<Item_Return_Search> selectReturn_Search(){
        List<Item_Return_Search> Item_Return_Searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Item_Return_Search.TABLE_Item_Return_Search_NAME;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Item_Return_Search item_return_search = new Item_Return_Search();

                item_return_search.setGTIN1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.GTIN)));
                item_return_search.setDesc1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.Desc)));

                item_return_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.MAT_CODE)));
                item_return_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.STATUS)));
                item_return_search.setQTY1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.QTY)));


                Item_Return_Searchlist.add(item_return_search);
                if (Item_Return_Searchlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+Item_Return_Searchlist.size());
        // return Po_item list
        return Item_Return_Searchlist;
    }


   public long Update_Sto_Search(String Barcode , String Qty) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
 //       values.put(Item_Return_Search.GTIN, Barcode);
        values.put(Item_Return_Search.QTY, Qty);
//TODO
        // updating row
        return db.update(Item_Return_Search.TABLE_Item_Return_Search_NAME, values, Item_Return_Search.GTIN + " = ?",
                new String[]{Barcode});
    }



  public List<Item_Return_Search> selectSto_Search_for_Qty(){
      List<Item_Return_Search> Item_Return_Searchlist = new ArrayList<>();

      // get writable database as we want to write data
      SQLiteDatabase db = this.getWritableDatabase();

      String selectQuery = "SELECT *FROM " + Item_Return_Search.TABLE_Item_Return_Search_NAME
              + " where " + Item_Return_Search.QTY +"!=" +"null or "+ Item_Return_Search.QTY +"!=" +"0.0";
      // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

      Cursor cursor = db.rawQuery(selectQuery, null);
      // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

      // looping through all rows and adding to list
      if (cursor.moveToFirst()) {
          do {

              Item_Return_Search item_return_search = new Item_Return_Search();

              item_return_search.setGTIN1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.GTIN)));
              item_return_search.setDesc1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.Desc)));

              item_return_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.MAT_CODE)));
              item_return_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.STATUS)));
              item_return_search.setQTY1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.QTY)));
              item_return_search.setUOM1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.UOM)));

              item_return_search.setSTG_LOC1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.STG_LOC)));
              item_return_search.setDEF_STG_LOC1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.DEF_STG_LOC)));

              item_return_search.setP_GRP1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.P_GRP)));
              item_return_search.setP_ORG1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.P_ORG)));

              item_return_search.setChecked_Item(false);
              Item_Return_Searchlist.add(item_return_search);
              if (Item_Return_Searchlist.isEmpty()){
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

      Log.d("po_itemlistsize",""+Item_Return_Searchlist.size());
      // return Po_item list
      return Item_Return_Searchlist;
  }

    public List<Item_Return_Search> Search_if_Barcode_in_localDataBase(/*String iss_stg_log ,String rec_stg_log ,*/ String Barcode ){
        List<Item_Return_Search> Item_Return_Searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Item_Return_Search.TABLE_Item_Return_Search_NAME
                + " WHERE "/* +Item_Return_Search.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+Item_Return_Search.REC_SITE_LOG+
                " = '"+rec_stg_log+ "' AND "*/+ Item_Return_Search.GTIN+" = "+Barcode;
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

                Item_Return_Search item_return_search = new Item_Return_Search();

                item_return_search.setGTIN1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.GTIN)));
                item_return_search.setDesc1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.Desc)));

                item_return_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.MAT_CODE)));
                item_return_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.STATUS)));
                item_return_search.setQTY1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.QTY)));
                item_return_search.setUOM1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.UOM)));

                Item_Return_Searchlist.add(item_return_search);
                if (Item_Return_Searchlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+Item_Return_Searchlist.size());
        // return Po_item list
        return Item_Return_Searchlist;
    }


    public List<Item_Return_Search> Search__Barcode(String Barcode ){
        List<Item_Return_Search> Item_Return_Searchlist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + Item_Return_Search.TABLE_Item_Return_Search_NAME
                + " where "+Item_Return_Search.GTIN+"='"+Barcode+"'";
                //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Item_Return_Search item_return_search = new Item_Return_Search();

                item_return_search.setGTIN1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.GTIN)));
                item_return_search.setDesc1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.Desc)));

                item_return_search.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.MAT_CODE)));
                item_return_search.setSTATUS1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.STATUS)));
                item_return_search.setQTY1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.QTY)));
                item_return_search.setDEF_STG_LOC1(cursor.getString(cursor.getColumnIndex(Item_Return_Search.DEF_STG_LOC)));



                Item_Return_Searchlist.add(item_return_search);
                if (Item_Return_Searchlist.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+Item_Return_Searchlist.size());
        // return Po_item list
        return Item_Return_Searchlist;
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
        String selectQuery = "SELECT * FROM " + Item_Return_Search.TABLE_Item_Return_Search_NAME + " where " + Item_Return_Search.STATUS +"!=" +"null or "+
                Item_Return_Search.STATUS +"!=" +"0.0";
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

    public long Update_Sto_search_For_QTY(String QTY/*,String iss_stg_log,String Res_stg_log*/, String Barcod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(Item_Return_Search.QTY, QTY);

        // updating row
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, STo_Search.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(Item_Return_Search.TABLE_Item_Return_Search_NAME, values, /*Item_Return_Search.ISS_STG_LOG+ " = ? and "+Item_Return_Search.REC_SITE_LOG+" = ? and "+*/Item_Return_Search.GTIN+" = ? ", new String[]{/*iss_stg_log ,Res_stg_log,*/ Barcod});
    }


    public void DeleteStoHeaderTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +Item_Return_Header.TABLE_STO_HEADER_NAME);

        db.close();
    }

    public void DeleteStoSearchTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +Item_Return_Search.TABLE_Item_Return_Search_NAME);

        db.close();
    }
}
