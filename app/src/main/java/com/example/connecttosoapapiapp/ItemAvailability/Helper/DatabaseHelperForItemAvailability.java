package com.example.connecttosoapapiapp.ItemAvailability.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemAvailabilityModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForItemAvailability extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForItemAvailability(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create ScanBarCode
        db.execSQL(ItemAvailabilityModule.CREATE_ItemAvailabilityModule_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ItemAvailabilityModule.TABLE_ItemAvailability_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insert_ItemsAvai(String Barcode, String Message,
                                 String UserName
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ItemAvailabilityModule.BarCode, Barcode);
        values.put(ItemAvailabilityModule.Message, Message);
        values.put(ItemAvailabilityModule.UserName, UserName);

        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(ItemAvailabilityModule.TABLE_ItemAvailability_NAME, null, values);

        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }



    public List<ItemAvailabilityModule> select_ItemsAvaiModule(){
        List<ItemAvailabilityModule> ItemsAvai_Modulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + ItemAvailabilityModule.TABLE_ItemAvailability_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ItemAvailabilityModule itemAvailabilityModule = new ItemAvailabilityModule();

                itemAvailabilityModule.setUserName1(cursor.getString(cursor.getColumnIndex(ItemAvailabilityModule.UserName)));
                itemAvailabilityModule.setBarCode1(cursor.getString(cursor.getColumnIndex(ItemAvailabilityModule.BarCode)));

                itemAvailabilityModule.setMessage1(cursor.getString(cursor.getColumnIndex(ItemAvailabilityModule.Message)));

                ItemsAvai_Modulelist.add(itemAvailabilityModule);
                if (ItemsAvai_Modulelist.isEmpty()){
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

        Log.d("po_itemlistsize",""+ItemsAvai_Modulelist.size());
        // return Po_item list
        return ItemsAvai_Modulelist;
    }


    public List<ItemAvailabilityModule> select_ItemsAvaiModulebyBarcode(String Barcode ){
        List<ItemAvailabilityModule> itemAvailabilityModuleList = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + ItemAvailabilityModule.TABLE_ItemAvailability_NAME
                + " where " + ItemAvailabilityModule.BarCode+" = '"+Barcode+"'";
//        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;
        Log.e("barcodequery",""+selectQuery);
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

                ItemAvailabilityModule itemAvailabilityModule = new ItemAvailabilityModule();

                //  Po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));


                itemAvailabilityModule.setBarCode1(cursor.getString(cursor.getColumnIndex(ItemAvailabilityModule.BarCode)));

                itemAvailabilityModuleList.add(itemAvailabilityModule);
                if (itemAvailabilityModuleList.isEmpty()){
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
        Log.d("po_itemlistsize","rch_if_Ba"+itemAvailabilityModuleList.size());
        // return Po_item list
        return itemAvailabilityModuleList;
    }

    public void DeleteItemsAvai_ModuleleTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " + ItemAvailabilityModule.TABLE_ItemAvailability_NAME);

        db.close();
    }


}
