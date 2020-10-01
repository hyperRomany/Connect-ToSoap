package com.example.connecttosoapapiapp.CycleCount.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelperForCycleCount extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelperForCycleCount(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Po_Headers table
//        db.execSQL(Po_Item_of_cycleCount.CREATE_Po_Item_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

      //  db.execSQL("DROP TABLE IF EXISTS " + Po_Item_of_cycleCount.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
//String iss_Strg_Log1,String rec_Site_log1,

    public long insertitems(String PHYSINVENTORY , String MATERIAL1, String MAKTX1,String QTY,
                            String ITEM1 , String BASE_UOM1,
                            String SERNP1, String EAN111, String MEINH1
                                 //, String NO_MORE_GR     String ISS_STG_LOG1,
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Po_Item_of_cycleCount.PHYSINVENTORY, PHYSINVENTORY);

        if (MATERIAL1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.MATERIAL,"" );

        }else {
            values.put(Po_Item_of_cycleCount.MATERIAL, MATERIAL1);
        }

        if (MAKTX1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.MAKTX, "");

        }else {
            values.put(Po_Item_of_cycleCount.MAKTX, MAKTX1);
        }


        values.put(Po_Item_of_cycleCount.QTY, QTY);

        if (ITEM1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.ITEM, "");

        }else {
            values.put(Po_Item_of_cycleCount.ITEM, ITEM1);
        }


        if (BASE_UOM1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.BASE_UOM, "");

        }else {
            values.put(Po_Item_of_cycleCount.BASE_UOM, BASE_UOM1);
        }

        if (SERNP1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.SERNP, "");

        }else {
            values.put(Po_Item_of_cycleCount.SERNP, SERNP1);
        }
        if (EAN111.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.EAN11, "");

        }else {
            values.put(Po_Item_of_cycleCount.EAN11, EAN111);
        }

        if (MEINH1.equalsIgnoreCase("anyType{}")){
            values.put(Po_Item_of_cycleCount.MEINH, "");

        }else {
            values.put(Po_Item_of_cycleCount.MEINH, MEINH1);
        }


        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Po_Item_of_cycleCount.TABLE_NAME, null, values);

        Log.e("no_of_itemcccQ" , String.valueOf(db.insert(Po_Item_of_cycleCount.TABLE_NAME, null, values)));

        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public void insertitems(String SqlQuere){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SqlQuere);
    }

    public long insertitemsList(ContentValues values
                                //, String NO_MORE_GR     String ISS_STG_LOG1,
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

      /*  ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Po_Item_of_cycleCount.PHYSINVENTORY, Po_items.get("PHYSINVENTORY"));

            values.put(Po_Item_of_cycleCount.MATERIAL,Po_items.get("MATERIAL") );

            values.put(Po_Item_of_cycleCount.MAKTX, Po_items.get("MAKTX"));

            values.put(Po_Item_of_cycleCount.QTY, Po_items.get("QTY"));

            values.put(Po_Item_of_cycleCount.ITEM, Po_items.get("ITEM"));

            values.put(Po_Item_of_cycleCount.BASE_UOM, Po_items.get("BASE_UOM"));

            values.put(Po_Item_of_cycleCount.SERNP, Po_items.get("SERNP"));

            values.put(Po_Item_of_cycleCount.EAN11, Po_items.get("EAN11"));

            values.put(Po_Item_of_cycleCount.MEINH, Po_items.get("MEINH"));*/


        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Po_Item_of_cycleCount.TABLE_NAME, null, values);

//        Log.e("no_of_itemcccQ" , String.valueOf(db.insert(Po_Item_of_cycleCount.TABLE_NAME, null, values)));

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }
    public long Update_QTY(String QTY , String Barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        Barcode=Barcode+" ";
        ContentValues values = new ContentValues();
        //values.put(STO_Header.Iss_Strg_Log, iss_Strg_Log1);
        values.put(Po_Item_of_cycleCount.QTY, QTY);

        // updating row
        return db.update(Po_Item_of_cycleCount.TABLE_NAME, values, Po_Item_of_cycleCount.EAN11 + " = ?", new String[]{Barcode});
        //return db.update(STo_Search.TABLE_STO_Search_NAME, values, null, null);
    }


    public List<Po_Item_of_cycleCount> Search_Barcode_in_localDataBase(String Barcode ){
        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME
                + " where " + Po_Item_of_cycleCount.EAN11+" = '"+Barcode+" '";
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

                Po_Item_of_cycleCount po_Item_of_cycleCount = new Po_Item_of_cycleCount();

              //  Po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));


                po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MATERIAL)));

                po_Item_of_cycleCount.setMAKTX1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MAKTX)));
                po_Item_of_cycleCount.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.EAN11)));
                po_Item_of_cycleCount.setQTY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.QTY)));
                po_Item_of_cycleCount.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MEINH)));
                po_Item_of_cycleCount.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SERNP)));

//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_Item_of_cycleCount.setChecked_Item(false);
                po_item_list.add(po_Item_of_cycleCount);
                if (po_item_list.isEmpty()){
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
        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        // return Po_item list
        return po_item_list;
    }

    public List<Po_Item_of_cycleCount> Search_Barcode_in_localDataBase_And_Has_QTy(String Barcode){
        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();
        Log.d("po_itemlistsize","rch_if_Ba"+Barcode);
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();


        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME
                + " where " + Po_Item_of_cycleCount.EAN11+" = '"+Barcode+" '"+" AND "   //+ Po_Item_of_cycleCount.QTY +"!=" +"null AND "
                + Po_Item_of_cycleCount.QTY +"!=" +"0.0";
//        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = "+iss_stg_log +" AND "+STo_Search.GTIN+" = "+Barcode;

        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";
        Log.e("barcodequery",""+selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item_of_cycleCount po_Item_of_cycleCount = new Po_Item_of_cycleCount();

                po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MATERIAL)));

                po_Item_of_cycleCount.setMAKTX1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MAKTX)));
                po_Item_of_cycleCount.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.EAN11)));
                po_Item_of_cycleCount.setQTY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.QTY)));
                po_Item_of_cycleCount.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MEINH)));
                po_Item_of_cycleCount.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SERNP)));

//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_Item_of_cycleCount.setChecked_Item(false);
                po_item_list.add(po_Item_of_cycleCount);

                if (po_item_list.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        if (!po_item_list.isEmpty()){
            Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.get(0).getQTY1());
            Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.get(0).getEAN111());
        }

        // return Po_item list
        return po_item_list;
    }

    public long Num_of_items_localDataBase( ){
//        List<Po_Item_of_cycleCount>

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        long count = DatabaseUtils.queryNumEntries(db, Po_Item_of_cycleCount.TABLE_NAME);

        return count;
    }





    public List<Po_Item_of_cycleCount> Select_Num_of_items_localDataBase( ){
//        List<Po_Item_of_cycleCount>
        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
//        String selectQuery = "SELECT DISTINCT " +Po_Item_of_cycleCount.EAN11+" FROM " + Po_Item_of_cycleCount.TABLE_NAME;
        String selectQuery = "SELECT *FROM " + Po_Item_of_cycleCount.TABLE_NAME;
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

                Po_Item_of_cycleCount po_Item_of_cycleCount = new Po_Item_of_cycleCount();

                //  Po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));

                po_Item_of_cycleCount.setPHYSINVENTORY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.PHYSINVENTORY)));
                po_Item_of_cycleCount.setMAKTX1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MAKTX)));
                po_Item_of_cycleCount.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.EAN11)));
                po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MATERIAL)));
                po_Item_of_cycleCount.setQTY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.QTY)));
                po_Item_of_cycleCount.setITEM1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.ITEM)));
                po_Item_of_cycleCount.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MEINH)));
                po_Item_of_cycleCount.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SERNP)));

//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_Item_of_cycleCount.setChecked_Item(false);
                po_item_list.add(po_Item_of_cycleCount);
                if (po_item_list.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        // return Po_item list
        return po_item_list;
    }



    public List<Po_Item_of_cycleCount> Get_Items_That_Has_PDNewQTy( ){
        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME
                + " where " + Po_Item_of_cycleCount.QTY +"!=" +"null or "+
                Po_Item_of_cycleCount.QTY +"!=" +"0.0";
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

                Po_Item_of_cycleCount po_Item_of_cycleCount = new Po_Item_of_cycleCount();

                po_Item_of_cycleCount.setPHYSINVENTORY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.PHYSINVENTORY)).substring(1));
                po_Item_of_cycleCount.setMAKTX1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MAKTX)));
                po_Item_of_cycleCount.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.EAN11)).replace(" ",""));
                po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MATERIAL)).replace(" ",""));
                po_Item_of_cycleCount.setQTY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.QTY)).replace(" ",""));
                po_Item_of_cycleCount.setITEM1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.ITEM)).replace(" ",""));
                po_Item_of_cycleCount.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MEINH)).replace(" ",""));
                po_Item_of_cycleCount.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SERNP)));

                po_Item_of_cycleCount.setBASE_UOM1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.BASE_UOM)));

//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_Item_of_cycleCount.setChecked_Item(false);
                po_item_list.add(po_Item_of_cycleCount);

                if (po_item_list.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        // return Po_item list
        return po_item_list;
    }

//
    public List<String> Get_All_PHYSINVENTORY( ){
        List<String> po_item_list = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();


        String selectQuery = "SELECT DISTINCT "+ Po_Item_of_cycleCount.PHYSINVENTORY +" FROM " + Po_Item_of_cycleCount.TABLE_NAME
//                + " where (" + Po_Item_of_cycleCount.QTY +"=" +"null or "+ Po_Item_of_cycleCount.QTY +"=" +"0.0 )"
                ;
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

//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_item_list.add(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.PHYSINVENTORY)).substring(1));

                if (po_item_list.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        // return Po_item list

        return po_item_list ;

    }

    public List<Po_Item_of_cycleCount> Get_PHYSINVENTORY_That_Not_Has_QTy(String PHYSINVENTORY) {
        List<Po_Item_of_cycleCount> po_item_list = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

//        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME
//                + " where " + Po_Item_of_cycleCount.PHYSINVENTORY +"= '" + PHYSINVENTORY+"'";

//        Log.e("Po_Item_PHYSINVENTORY ",""+selectQuery);

//        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

//        String selectQuery = "SELECT *FROM " + STo_Search.TABLE_STO_Search_NAME
//                + " WHERE " +STo_Search.ISS_STG_LOG+" = "+iss_stg_log +" AND "+STo_Search.GTIN+" = "+Barcode;

        //+ " and "  + STo_Search.QTY +"!= null" ;
        //+ " where " + STo_Search.QTY +"!=" +"null or "+ STo_Search.QTY +"!=" +"0.0 and "+STo_Search.GTIN+"="+Barcode;

        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

       // Cursor cursor = db.rawQuery(selectQuery, null);

//   Po_Item_of_cycleCount.EAN11 + " = ?", new String[]{Barcode}


        Cursor cursor =db.query(false, Po_Item_of_cycleCount.TABLE_NAME,null, Po_Item_of_cycleCount.PHYSINVENTORY +" = ?",
                new String[]{PHYSINVENTORY},null,null,null,"1");
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item_of_cycleCount po_Item_of_cycleCount = new Po_Item_of_cycleCount();

                po_Item_of_cycleCount.setPHYSINVENTORY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.PHYSINVENTORY)).substring(1));
                po_Item_of_cycleCount.setMAKTX1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MAKTX)));
                po_Item_of_cycleCount.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.EAN11)).replace(" ",""));
                po_Item_of_cycleCount.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MATERIAL)).replace(" ",""));
                po_Item_of_cycleCount.setQTY1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.QTY)).replace(" ",""));
                po_Item_of_cycleCount.setITEM1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.ITEM)).replace(" ",""));
                po_Item_of_cycleCount.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.MEINH)).replace(" ",""));
                po_Item_of_cycleCount.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SERNP)));
                po_Item_of_cycleCount.setBASE_UOM1(cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.BASE_UOM)));


//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//                Po_Item_of_cycleCount.setUOM_DESC1(cursor.getString(cursor.getColumnIndex(STo_Search.UOM_DESC)));
//
                po_Item_of_cycleCount.setChecked_Item(false);
                po_item_list.add(po_Item_of_cycleCount);

                if (po_item_list.isEmpty()){
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

        Log.d("po_itemlistsize","rch_if_Ba"+po_item_list.size());
        // return Po_item list
        return po_item_list;
    }


    public void DeleteItemsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " + Po_Item_of_cycleCount.TABLE_NAME);

db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME ='" + Po_Item_of_cycleCount.TABLE_NAME+"'" );
        db.close();
    }



}
