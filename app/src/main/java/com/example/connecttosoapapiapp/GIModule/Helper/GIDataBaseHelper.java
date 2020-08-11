package com.example.connecttosoapapiapp.GIModule.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;

import java.util.ArrayList;
import java.util.List;


public class GIDataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public GIDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL(GIModule.CREATE_GIModule_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + GIModule.TABLE_GIModule_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,



    public long insert_GIModule(String GTIN1, String ISS_SITE1, String REC_SITE1, String MAT_CODE1, String UOM_DESC1,
                                  String MEINH1, String UOM1, String STATUS1,
                                  String ISS_STG_LOG1, String REC_SITE_LOG1, String AVAILABLE_STOCK1, String EKGRP1, String Qty) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(GIModule.GTIN, GTIN1);
        values.put(GIModule.ISS_SITE, ISS_SITE1.replaceAll(" ",""));
        values.put(GIModule.REC_SITE, REC_SITE1.replaceAll(" ",""));
        values.put(GIModule.MAT_CODE, MAT_CODE1.replaceAll(" ",""));
        values.put(GIModule.UOM_DESC, UOM_DESC1.replaceAll(" ",""));
        values.put(GIModule.MEINH, MEINH1.replaceAll(" ",""));
        values.put(GIModule.UOM, UOM1);
        values.put(GIModule.STATUS, STATUS1);
        values.put(GIModule.ISS_STG_LOG, ISS_STG_LOG1.replaceAll(" ",""));
        values.put(GIModule.REC_SITE_LOG, REC_SITE_LOG1);
        values.put(GIModule.AVAILABLE_STOCK, AVAILABLE_STOCK1);
        values.put(GIModule.EKGRP, EKGRP1);
        values.put(GIModule.QTY, Qty);
        //values.put(GIModule.NO_MORE_GR, NO_MORE_GR);
        // insert row
        long id = db.insert(GIModule.TABLE_GIModule_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<GIModule> selectGIModule(){
        List<GIModule> GIModulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + GIModule.TABLE_GIModule_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                GIModule giModule = new GIModule();

                giModule.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(GIModule.UOM_DESC)));
                giModule.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(GIModule.MAT_CODE)));
                giModule.setSTATUS1(cursor.getString(cursor.getColumnIndex(GIModule.STATUS)));
                giModule.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(GIModule.AVAILABLE_STOCK)));
                giModule.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_STG_LOG)));
                giModule.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE_LOG)));
                giModule.setISS_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_SITE)));
                giModule.setREC_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE)));
                giModule.setMEINH1(cursor.getString(cursor.getColumnIndex(GIModule.MEINH)));
                giModule.setGTIN1(cursor.getString(cursor.getColumnIndex(GIModule.GTIN)));
                giModule.setQTY1(cursor.getString(cursor.getColumnIndex(GIModule.QTY)));
                giModule.setChecked_Item(false);

                GIModulelist.add(giModule);
                if (GIModulelist.isEmpty()){
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

        Log.d("po_itemlistsize",""+GIModulelist.size());
        // return Po_item list
        return GIModulelist;
    }

    public long Update_REC_SITE_LOG1(String rec_Site_log1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(GIModule.REC_SITE_LOG, rec_Site_log1);

        // updating row
        return db.update(GIModule.TABLE_GIModule_NAME, values, GIModule.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        //return db.update(GIModule.TABLE_GIModule_NAME, values, null, null);
    }




    public List<GIModule> selectGIModule_for_Qty(){
        List<GIModule> GIModulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + GIModule.TABLE_GIModule_NAME
                + " where " + GIModule.QTY +"!=" +"null or "+ GIModule.QTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                GIModule giModule = new GIModule();

                giModule.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(GIModule.UOM_DESC)));
                giModule.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(GIModule.MAT_CODE)));
                giModule.setSTATUS1(cursor.getString(cursor.getColumnIndex(GIModule.STATUS)));
                giModule.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(GIModule.AVAILABLE_STOCK)));
                giModule.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_STG_LOG)));
                giModule.setREC_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE)));
                giModule.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE_LOG)));
                giModule.setMEINH1(cursor.getString(cursor.getColumnIndex(GIModule.MEINH)));
                giModule.setGTIN1(cursor.getString(cursor.getColumnIndex(GIModule.GTIN)));
                giModule.setQTY1(cursor.getString(cursor.getColumnIndex(GIModule.QTY)));
                giModule.setChecked_Item(false);

                GIModulelist.add(giModule);
                if (GIModulelist.isEmpty()){
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

        Log.d("po_itemlistsize",""+GIModulelist.size());
        // return Po_item list
        return GIModulelist;
    }

    public List<GIModule> Search_if_Barcode_in_localDataBase(String iss_stg_log ,String rec_stg_log , String Barcode ){
        List<GIModule> GIModulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + GIModule.TABLE_GIModule_NAME
                + " WHERE " +GIModule.ISS_STG_LOG+" = '"+iss_stg_log+ "' AND "+GIModule.REC_SITE_LOG+" = '"+rec_stg_log+ "' AND "+GIModule.GTIN+" = "+Barcode;
//        //+ " and "  + GIModule.QTY +"!= null" ;
        //+ " where " + GIModule.QTY +"!=" +"null or "+ GIModule.QTY +"!=" +"0.0 and "+GIModule.GTIN+"="+Barcode;

//        String selectQuery = "SELECT *FROM " + GIModule.TABLE_GIModule_NAME
//                + " WHERE " +GIModule.ISS_STG_LOG+" = "+iss_stg_log +" AND "+GIModule.GTIN+" = "+Barcode;

        //+ " and "  + GIModule.QTY +"!= null" ;
        //+ " where " + GIModule.QTY +"!=" +"null or "+ GIModule.QTY +"!=" +"0.0 and "+GIModule.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                GIModule giModule = new GIModule();

                giModule.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(GIModule.UOM_DESC)));
                giModule.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(GIModule.MAT_CODE)));
                giModule.setSTATUS1(cursor.getString(cursor.getColumnIndex(GIModule.STATUS)));
                giModule.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(GIModule.AVAILABLE_STOCK)));
                giModule.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_STG_LOG)));
                giModule.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE_LOG)));
                giModule.setISS_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_SITE)));
                giModule.setREC_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE)));
                giModule.setMEINH1(cursor.getString(cursor.getColumnIndex(GIModule.MEINH)));
                giModule.setGTIN1(cursor.getString(cursor.getColumnIndex(GIModule.GTIN)));
                giModule.setQTY1(cursor.getString(cursor.getColumnIndex(GIModule.QTY)));
                giModule.setChecked_Item(false);

                GIModulelist.add(giModule);
                if (GIModulelist.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+GIModulelist.size());
        // return Po_item list
        return GIModulelist;
    }


    public List<GIModule> Search__Barcode(String Barcode ){
        List<GIModule> GIModulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + GIModule.TABLE_GIModule_NAME
                + " where "+GIModule.GTIN+"="+Barcode;
        //+ " and "  + GIModule.QTY +"!= null" ;
        //+ " where " + GIModule.QTY +"!=" +"null or "+ GIModule.QTY +"!=" +"0.0 and "+GIModule.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                GIModule giModule = new GIModule();

                giModule.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(GIModule.UOM_DESC)));
                giModule.setMAT_CODE1(cursor.getString(cursor.getColumnIndex(GIModule.MAT_CODE)));
                giModule.setSTATUS1(cursor.getString(cursor.getColumnIndex(GIModule.STATUS)));
                giModule.setAVAILABLE_STOCK1(cursor.getString(cursor.getColumnIndex(GIModule.AVAILABLE_STOCK)));
                giModule.setISS_STG_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_STG_LOG)));
                giModule.setREC_SITE_LOG1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE_LOG)));
                giModule.setISS_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.ISS_SITE)));
                giModule.setREC_SITE1(cursor.getString(cursor.getColumnIndex(GIModule.REC_SITE)));
                giModule.setMEINH1(cursor.getString(cursor.getColumnIndex(GIModule.MEINH)));
                giModule.setGTIN1(cursor.getString(cursor.getColumnIndex(GIModule.GTIN)));
                giModule.setQTY1(cursor.getString(cursor.getColumnIndex(GIModule.QTY)));
                giModule.setChecked_Item(false);

                GIModulelist.add(giModule);
                if (GIModulelist.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+GIModulelist.size());
        // return Po_item list
        return GIModulelist;
    }



    public long Update_GIModule_For_QTY(String QTY,String iss_stg_log,String Res_stg_log, String Barcod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(GIModule.QTY, QTY);

        // updating row
        //return db.update(GIModule.TABLE_GIModule_NAME, values, GIModule.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(GIModule.TABLE_GIModule_NAME, values, GIModule.ISS_STG_LOG+ " = ? and "+GIModule.REC_SITE_LOG+" = ? and "+GIModule.GTIN+" = ? ", new String[]{iss_stg_log ,Res_stg_log, Barcod});
    }

    public long Update_QTY(String QTY, String Barcod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(GIModule.QTY, QTY);

        // updating row
        //return db.update(GIModule.TABLE_GIModule_NAME, values, GIModule.REC_SITE_LOG + " = ?", new String[]{"anyType{}"});
        return db.update(GIModule.TABLE_GIModule_NAME, values, GIModule.GTIN+" = ? ", new String[]{ Barcod});
    }

    public void DeleteStoSearchTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +GIModule.TABLE_GIModule_NAME);

        db.close();
    }
}