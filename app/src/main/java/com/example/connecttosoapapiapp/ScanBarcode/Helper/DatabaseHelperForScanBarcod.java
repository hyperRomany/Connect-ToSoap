package com.example.connecttosoapapiapp.ScanBarcode.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ScanBarcode.Module.ScanBarcodeModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForScanBarcod extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constant.Constant_DATABASE_VERSION;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForScanBarcod(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create ScanBarCode
        db.execSQL(ScanBarcodeModule.CREATE_ScanBarCodeModule_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
       // db.execSQL("DROP TABLE IF EXISTS " + ScanBarcodeModule.TABLE_ScanBarcode_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insert_ScanBarcode(String Barcode, String Qty,
                                 String Zone
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ScanBarcodeModule.BarCode, Barcode);
        values.put(ScanBarcodeModule.QTY, Qty);
        values.put(ScanBarcodeModule.Zone, Zone);

        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(ScanBarcodeModule.TABLE_ScanBarcode_NAME, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }



    public List<ScanBarcodeModule> selectScanBarcodeModule(){
        List<ScanBarcodeModule> ScanBarcodeModulelist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT *FROM " + ScanBarcodeModule.TABLE_ScanBarcode_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                ScanBarcodeModule scanBarcodeModule = new ScanBarcodeModule();

                scanBarcodeModule.setBarCode1(cursor.getString(cursor.getColumnIndex(ScanBarcodeModule.BarCode)));
                scanBarcodeModule.setQTY1(cursor.getString(cursor.getColumnIndex(ScanBarcodeModule.QTY)));

                scanBarcodeModule.setZone1(cursor.getString(cursor.getColumnIndex(ScanBarcodeModule.Zone)));

                ScanBarcodeModulelist.add(scanBarcodeModule);
                if (ScanBarcodeModulelist.isEmpty()){
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

        Log.d("po_itemlistsize",""+ScanBarcodeModulelist.size());
        // return Po_item list
        return ScanBarcodeModulelist;
    }



    public void DeleteScanBarCodeTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +ScanBarcodeModule.TABLE_ScanBarcode_NAME);

        db.close();
    }


}
