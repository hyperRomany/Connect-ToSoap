package com.example.connecttosoapapiapp.ReceivingModule.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.connecttosoapapiapp.CycleCount.Modules.Po_Item_of_cycleCount;
import com.example.connecttosoapapiapp.GIModule.Modules.GIModule;
import com.example.connecttosoapapiapp.ItemAvailability.Module.ItemAvailabilityModule;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Header;
import com.example.connecttosoapapiapp.ItemReturn.modules.Item_Return_Search;
import com.example.connecttosoapapiapp.Promotion.Modules.Prom_item_Module;
import com.example.connecttosoapapiapp.ReceivingModule.model.Groups;
import com.example.connecttosoapapiapp.ReceivingModule.model.PO_SERIAL;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Header;
import com.example.connecttosoapapiapp.ReceivingModule.model.Po_Item;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.ScanBarcode.Module.ScanBarcodeModule;
import com.example.connecttosoapapiapp.TransfereModule.modules.STO_Header;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "Import1.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Po_Headers table
        db.execSQL(Users.CREATE_User_TABLE);
        db.execSQL(Groups.CREATE_Groups_TABLE);
        db.execSQL(Po_Header.CREATE_TABLE);
        db.execSQL(Po_Item.CREATE_Po_Item_TABLE);
        db.execSQL(PO_SERIAL.CREATE_PO_SERIAL_TABLE);

        //otem availability
        db.execSQL(ItemAvailabilityModule.CREATE_ItemAvailabilityModule_TABLE);
//        // create transfere
        db.execSQL(STO_Header.CREATE_STO_HEADER_TABLE);
        db.execSQL(STo_Search.CREATE_STO_SEARCH_TABLE);

//// create cyclecount
        db.execSQL(Po_Item_of_cycleCount.CREATE_Po_Item_TABLE);

        // create ScanBarCode
        db.execSQL(ScanBarcodeModule.CREATE_ScanBarCodeModule_TABLE);

        // GI
        db.execSQL(GIModule.CREATE_GIModule_TABLE);

        // create ItemReturn
        db.execSQL(Item_Return_Header.CREATE_Item_Return_Header_TABLE);
        db.execSQL(Item_Return_Search.CREATE_Item_Return_TABLE);

        // create prootion
        db.execSQL(Prom_item_Module.CREATE_Prom_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_User_Name);
        db.execSQL("DROP TABLE IF EXISTS " + Groups.TABLE_Group_Name);

        db.execSQL("DROP TABLE IF EXISTS " + Po_Item_of_cycleCount.TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + PO_SERIAL.TABLE_PO_SERIAL_NAME);

        //otem availability
        db.execSQL("DROP TABLE IF EXISTS " + ItemAvailabilityModule.TABLE_ItemAvailability_NAME);

         //  ScanBarCode
        db.execSQL("DROP TABLE IF EXISTS " + ScanBarcodeModule.TABLE_ScanBarcode_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + STO_Header.TABLE_STO_HEADER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STo_Search.TABLE_STO_Search_NAME);

// promotion
        db.execSQL("DROP TABLE IF EXISTS " + Prom_item_Module.TABLE_Prom_NAME);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Item_Return_Header.TABLE_STO_HEADER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item_Return_Search.TABLE_Item_Return_Search_NAME);

        // Drop older table if existed
     //   db.execSQL("DROP TABLE IF EXISTS " + Po_Item_of_cycleCount.TABLE_NAME);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + GIModule.TABLE_GIModule_NAME);

        //For Recieving
        db.execSQL("DROP TABLE IF EXISTS " +Po_Header.TABLE_Po_Header_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +Po_Item.TABLE_NAME);

//        db.execSQL("DROP TABLE IF EXISTS " + Po_Item_of_cycleCount.TABLE_NAME);

//        db.execSQL("DROP TABLE IF EXISTS " + GIModule.TABLE_GIModule_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertuser(String User_ID, String User_Name, String User_Describtion,
                           String Group_Name, String User_status,
                                String User_Department , String Company, String Group_ID, String ComplexID
                                //, String NO_MORE_GR
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Users.User_ID, User_ID);
        values.put(Users.User_Name, User_Name);
        values.put(Users.User_Describtion, User_Describtion);
        values.put(Users.Group_Name, Group_Name);
        values.put(Users.User_status, User_status);
        values.put(Users.User_Department, User_Department);
        values.put(Users.Company, Company);
        values.put(Users.Group_ID, Group_ID);
        values.put(Users.ComplexID, ComplexID);
        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Users.TABLE_User_Name, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertgroup(String Group_ID
                           //, String NO_MORE_GR
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Groups.Group_ID, Group_ID);

        //values.put(Users.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Groups.TABLE_Group_Name, null, values);

        // close db connection
        db.close();
        db.close();

        // return newly inserted row id
        return id;
    }
    public List<Groups> Get_Groups() {
        List<Groups> grouplist = new ArrayList<>();
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Groups.TABLE_Group_Name ;
        //  + " where " + Po_Item_of_cycleCount.PDNEWQTY +"!=" +"null or "+ Po_Item_of_cycleCount.PDNEWQTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Groups groups = new Groups();
                //الوصف
                groups.setGroup_ID1(cursor.getString(cursor.getColumnIndex(Groups.Group_ID)));

                grouplist.add(groups);

                if (grouplist.isEmpty()){
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

        Log.d("po_itemlistsize",""+grouplist.size());
        // return Po_item list
        return grouplist;
    }
    public List<Users> getUserData() {
        List<Users> userlist = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Users.TABLE_User_Name;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();
                users.setCompany1(cursor.getString(cursor.getColumnIndex(Users.Company)));
                users.setUser_Name1(cursor.getString(cursor.getColumnIndex(Users.User_Name)));

                users.setUser_ID1(cursor.getString(cursor.getColumnIndex(Users.User_ID)));
                users.setUser_Department1(cursor.getString(cursor.getColumnIndex(Users.User_Department)));

                users.setComplexID1(cursor.getString(cursor.getColumnIndex(Users.ComplexID)));
                users.setUser_Describtion1(cursor.getString(cursor.getColumnIndex(Users.User_Describtion)));

                users.setGroup_ID1(cursor.getString(cursor.getColumnIndex(Users.Group_ID)));
                /*Po_Headerlist.add(new Po_Header(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/
                userlist.add(users);
           /* Log.d("Po_Headersclass",""+po_header.getVENDOR11());
            Log.d("Po_Headersclasslist",""+Po_Headerlist.get(0).getVENDOR11());


            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));*/
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return Po_Headers list
        return userlist;
    }


    public long insertPo_Header(String PO_NUMBER, String DOC_TYPE, String VENDOR, String VENDOR_NAME, String CREATE_BY,
                                String Delievered_BY , String DELIVERY_DATE, String COMP_CODE, String PUR_GROUP
                                //, String NO_MORE_GR
    ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Po_Header.PO_NUMBER, PO_NUMBER);
        values.put(Po_Header.DOC_TYPE, DOC_TYPE);
        values.put(Po_Header.VENDOR, VENDOR);
        values.put(Po_Header.VENDOR_NAME, VENDOR_NAME);
        values.put(Po_Header.CREATE_BY, CREATE_BY);
        values.put(Po_Header.Delievered_BY, Delievered_BY);
        values.put(Po_Header.DELIVERY_DATE, DELIVERY_DATE);
        values.put(Po_Header.COMP_CODE, COMP_CODE);
        values.put(Po_Header.PUR_GROUP, PUR_GROUP);
        //values.put(Po_Header.NO_MORE_GR, NO_MORE_GR);

        // insert row
        long id = db.insert(Po_Header.TABLE_Po_Header_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertPo_Item(String MATERIAL, String SHORT_TEXT, String QUANTITY, String PO_UNIT, String AUOM, String PDNEWQTY,String DeliveryDate,
                              String VendorName ,String QUANTITY_BUOM, String MEINS, String NETPR, String NETWR,
                              String CURRENCY, String EAN11, String MEINH, String GTIN_STATUS, String VMSTA,
                              String PLANT, String PLANT_NAME, String PO_NUMBER, String COMP_CODE,
                              String DOC_TYPE, String DELIVERY_DATE, String PO_ITEM, String CREATE_DATE,
                              String PUR_GROUP, String STGE_LOC, String ITEM_CAT, String SERNP
            ,String UMREZ,String UMREN
                              ) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Po_Item.MATERIAL, MATERIAL);
        values.put(Po_Item.SHORT_TEXT, SHORT_TEXT);
        values.put(Po_Item.QUANTITY, QUANTITY);
        values.put(Po_Item.PO_UNIT, PO_UNIT);
        values.put(Po_Item.AUOM, AUOM);

        values.put(Po_Item.PDNEWQTY, PDNEWQTY);

        values.put(Po_Item.DELIVERY_DATE, DeliveryDate);

        values.put(Po_Item.VENDOR_NAME,VendorName);

        values.put(Po_Item.QUANTITY_BUOM, QUANTITY_BUOM);
        values.put(Po_Item.MEINS, MEINS);
        values.put(Po_Item.NETPR, NETPR);
        values.put(Po_Item.NETWR, NETWR);

        values.put(Po_Item.CURRENCY, CURRENCY);
        values.put(Po_Item.EAN11, EAN11);
        values.put(Po_Item.MEINH, MEINH);
        values.put(Po_Item.GTIN_STATUS, GTIN_STATUS);
        values.put(Po_Item.VMSTA, VMSTA);
        values.put(Po_Item.PLANT, PLANT);
        values.put(Po_Item.PLANT_NAME, PLANT_NAME);
        values.put(Po_Item.PO_NUMBER, PO_NUMBER);
        values.put(Po_Item.COMP_CODE, COMP_CODE);
        values.put(Po_Item.DOC_TYPE, DOC_TYPE);
        values.put(Po_Item.DELIVERY_DATE, DELIVERY_DATE);
        values.put(Po_Item.PO_ITEM, PO_ITEM);
        values.put(Po_Item.CREATE_DATE, CREATE_DATE);
        values.put(Po_Item.PUR_GROUP, PUR_GROUP);
        values.put(Po_Item.STGE_LOC, STGE_LOC);
        values.put(Po_Item.ITEM_CAT, ITEM_CAT);
        if (SERNP.contains("anyType{}")){
            values.put(Po_Item.SERNP, "");
        }else {
            values.put(Po_Item.SERNP, SERNP);
        }


        values.put(Po_Item.UMREZ, UMREZ);
        values.put(Po_Item.UMREN, UMREN);
        // insert row
        long id = db.insert(Po_Item.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public long insertPo_Serials(String Material1,String Po_item, String EAN11, String Serial1) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(PO_SERIAL.Material, Material1);
        values.put(PO_SERIAL.Po_Item, Po_item);
        values.put(PO_SERIAL.EAN1, EAN11);
        values.put(PO_SERIAL.Serial, Serial1);

        // insert row
        long id = db.insert(PO_SERIAL.TABLE_PO_SERIAL_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<PO_SERIAL> selectPo_Serials(String BarCode){
        List<PO_SERIAL> Po_seriallist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + PO_SERIAL.TABLE_PO_SERIAL_NAME + " where " + PO_SERIAL.EAN1 +"=" +BarCode  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                PO_SERIAL po_serial = new PO_SERIAL();

                po_serial.setMaterial1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Material)));

                po_serial.setEAN11(cursor.getString(cursor.getColumnIndex(PO_SERIAL.EAN1)));

                po_serial.setSerial1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Serial)));

                po_serial.setPo_Item1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Po_Item)));

                po_serial.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

                Po_seriallist.add(po_serial);
                if (Po_seriallist.isEmpty()){
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

        Log.d("po_itemlistsize",""+Po_seriallist.size());
        // return Po_item list
        return Po_seriallist;
    }

    public List<PO_SERIAL> selectPo_Serials_ToUpload(){
        List<PO_SERIAL> Po_seriallist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + PO_SERIAL.TABLE_PO_SERIAL_NAME ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                PO_SERIAL po_serial = new PO_SERIAL();

                po_serial.setMaterial1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Material)));

                po_serial.setEAN11(cursor.getString(cursor.getColumnIndex(PO_SERIAL.EAN1)));

                po_serial.setSerial1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Serial)));

                po_serial.setPo_Item1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Po_Item)));

                po_serial.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

                Po_seriallist.add(po_serial);
                if (Po_seriallist.isEmpty()){
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

        Log.d("po_itemlistsize",""+Po_seriallist.size());
        // return Po_item list
        return Po_seriallist;
    }

    public String serach_for_Serial(String Serial){
        List<PO_SERIAL> Po_seriallist = new ArrayList<>();

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + PO_SERIAL.TABLE_PO_SERIAL_NAME + " where " + PO_SERIAL.Serial +"=" +Serial  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PO_SERIAL po_serial=new PO_SERIAL();

                po_serial.setSerial1(cursor.getString(cursor.getColumnIndex(PO_SERIAL.Serial)));

               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

                Po_seriallist.add(po_serial);

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

        if (Po_seriallist.size()==0){
            Log.d("po_itemlistsize","true"+Po_seriallist.size());
            return "true";

        }else {
            Log.d("po_itemlistsize","false"+Po_seriallist.size());
            return "false";
        }
        //Log.d("po_itemlistsize",""+Po_seriallist.size());
        // return Po_item list
    }

    public int update_Serial(String OldSerial, String NewSerial) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PO_SERIAL.Serial, NewSerial);

        // updating row
        return db.update(PO_SERIAL.TABLE_PO_SERIAL_NAME, values, PO_SERIAL.Serial + " = ?",
                new String[]{OldSerial});

    }

    /*public Po_Header getPo_Header(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Po_Header.TABLE_NAME,
                new String[]{Po_Header.COLUMN_ID, Po_Header.COLUMN_USERNAME, Po_Header.COLUMN_PASSWORD},
                Po_Header.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Po_Header object
        Po_Header Po_Header = new Po_Header(
                cursor.getInt(cursor.getColumnIndex(Po_Header.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Po_Header.COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndex(Po_Header.COLUMN_PASSWORD)));

        // close the db connection
        cursor.close();

        return Po_Header;
    }*/

    public List<Po_Header> getPo_number_Po_Headers() {
        List<Po_Header> Po_Headerlist = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Header.TABLE_Po_Header_NAME;
               // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Po_Header po_header = new Po_Header();
               po_header.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
                po_header.setVENDOR11(cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
                po_header.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));
                po_header.setDelievered_BY1(cursor.getString(cursor.getColumnIndex(Po_Header.Delievered_BY)));
                po_header.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Header.COMP_CODE)));

                /*Po_Headerlist.add(new Po_Header(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/
            Po_Headerlist.add(po_header);
           /* Log.d("Po_Headersclass",""+po_header.getVENDOR11());
            Log.d("Po_Headersclasslist",""+Po_Headerlist.get(0).getVENDOR11());


            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)));
            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)));
            Log.d("Po_Headers",""+cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME)));*/
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return Po_Headers list
        return Po_Headerlist;
    }

    public List<Po_Item> Search_Barcode_Po_Item(String BarCode ) {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME +
                " where " + Po_Item.EAN11 +"='" +BarCode +"'" ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("zzpo_itemlist",""+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item po_item = new Po_Item();
                po_item.setID1(cursor.getString(cursor.getColumnIndex(Po_Item.ID)));
                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //وحده قياس المطلوبه
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                //حاله الباركود
                po_item.setGTIN_STATUS1(cursor.getString(cursor.getColumnIndex(Po_Item.GTIN_STATUS)));
                //الكميه المستلمه سابقا
                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
                //اذا كان له serial ولا لا
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));


                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));

                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));

                po_item.setVMSTA1(cursor.getString(cursor.getColumnIndex(Po_Item.VMSTA)));
                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));

               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/


                po_itemlist.add(po_item);
                if (po_itemlist.isEmpty()){
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

        Log.d("zzpo_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public List<Po_Item> Search_Barcode_with_UOM(String BarCode , String PO_UNIT) {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME +
                " where " + Po_Item.EAN11 +"='" +BarCode +"' and " + Po_Item.PO_UNIT +" = '" +PO_UNIT +"'" ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("zzpo_itemlist",""+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item po_item = new Po_Item();
                po_item.setID1(cursor.getString(cursor.getColumnIndex(Po_Item.ID)));
                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //وحده قياس المطلوبه
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                //حاله الباركود
                po_item.setGTIN_STATUS1(cursor.getString(cursor.getColumnIndex(Po_Item.GTIN_STATUS)));
                //الكميه المستلمه سابقا
                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
                //اذا كان له serial ولا لا
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));


                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));

                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));

                po_item.setVMSTA1(cursor.getString(cursor.getColumnIndex(Po_Item.VMSTA)));
                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));

               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/


                po_itemlist.add(po_item);
                if (po_itemlist.isEmpty()){
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

        Log.d("zzpo_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public List<Po_Item> Get_Po_Item_That_Has_PDNewQTy() {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME + " where " + Po_Item.PDNEWQTY +"!=" +"null or "+
                Po_Item.PDNEWQTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item po_item = new Po_Item();
                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //الباركود
                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));
                //الكميه المستلمه سابقا
                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));

                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));

                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));

                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));

                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));
                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));

                //this data for write inn sqlserver Log
                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));

                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

                po_itemlist.add(po_item);
                if (po_itemlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public List<Po_Item> Get_Po_Item_That_Has_PDNewQTy_for_one_Lineiteam() {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME + " where ( " + Po_Item.PDNEWQTY +"!=" +"null or "+
                Po_Item.PDNEWQTY +"!=" +"0.0 ) Limit 1  ";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Po_Item po_item = new Po_Item();
                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //الباركود
                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));


                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));

                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));

                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));

                //الكميه المستلمه سابقا
                //           po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                po_item.setPDNEWQTY1(Get_QTY_for_one_article(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM))));

                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));


                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));

                //this data for write inn sqlserver Log
                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));

                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

                po_itemlist.add(po_item);
                if (po_itemlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public String Get_QTY_for_one_article(String PO_ITEM) {
        //List<Po_Item> po_itemlist = new ArrayList<>();
        String ResaultPO_ITEM="";
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT "+" sum(" +Po_Item.PDNEWQTY+ ") QTY FROM " + Po_Item.TABLE_NAME +
                " where " + Po_Item.PO_ITEM +"='" +PO_ITEM+"'";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
         Log.d("zzquer","Get_QTY_for_one_article "+selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ResaultPO_ITEM=cursor.getString(cursor.getColumnIndex("QTY"));
                Log.d("zzquer","Get_QTY_for_one_article "+ResaultPO_ITEM);

//                Po_Item po_item = new Po_Item();
//                //الوصف
//                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
//                //رقم الصنف
//                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
//                //وحده قياس الصنف
//                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
//                //الباركود
//                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));
//                //الكميه المستلمه سابقا
//                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
//                //الكميه المطلوبه
//                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
//
//                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));
//
//                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
//
//                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
//
//                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
//                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
//                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
//                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
//                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));
//                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
//                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
//                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
//                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
//                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
//                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));
//
//                //this data for write inn sqlserver Log
//                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));
//                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
//
//                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/

//                po_itemlist.add(po_item);
//                if (po_itemlist.isEmpty()){
//                    Log.d("po_itemlist","empty");
//                }
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

        Log.d("po_itemlistsize",""+ResaultPO_ITEM);
        // return Po_item list
        return ResaultPO_ITEM;
    }


    public List<Po_Item> Get_Po_Item() {
        List<Po_Item> po_itemlist = new ArrayList<>();
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME ;
              //  + " where " + Po_Item_of_cycleCount.PDNEWQTY +"!=" +"null or "+ Po_Item_of_cycleCount.PDNEWQTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Po_Item po_item = new Po_Item();
                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //الباركود
                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));
                //الكميه المستلمه سابقا
                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));
                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));

                //this data for write inn sqlserver Log
                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));

                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/


                po_itemlist.add(po_item);
                if (po_itemlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public List<String> Get_distinct_Po_Item_() {
        List<String> po_itemlist = new ArrayList<>();
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT distinct "+Po_Item.PO_ITEM /* +","+Po_Item.PO_UNIT */+" FROM " + Po_Item.TABLE_NAME;
        //  + " where " + Po_Item.PDNEWQTY +"!=" +"null or "+ Po_Item.PDNEWQTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                 Po_Item po_item = new Po_Item();
                //الوصف
//                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
//                //رقم الصنف
//                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
//                //وحده قياس الصنف
//                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
//                //الباركود
//                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));
//                //الكميه المستلمه سابقا
//                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
//                //الكميه المطلوبه
//                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
//                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));
                //ToDo
  //              po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
//                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
//                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
//                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
//                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
//                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
//                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
//                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
//                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
//                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
//                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
//                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));
//
//                //this data for write inn sqlserver Log
//                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));
//
//                po_item.setChecked_Item(false);
//               po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
//                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
//                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/
//
//
                po_itemlist.add(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));

                if (po_itemlist.isEmpty()){
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

        Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public  List<Po_Item> Get_Po_Item_for_po_iteam(String po_iteam1) {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME+
                " where " + Po_Item.PO_ITEM  +" = '" + po_iteam1+"'" ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));
        Log.e("zzzselectQuery",""+selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Po_Item  po_item = new Po_Item();
                po_item.setID1(cursor.getString(cursor.getColumnIndex(Po_Item.ID)));

                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //الباركود
                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));

                //الكميه المستلمه سابقا
                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
//                po_item.setPDNEWQTY1(Get_QTY_for_one_article(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM))));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));
                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));

                po_item.setGTIN_STATUS1(cursor.getString(cursor.getColumnIndex(Po_Item.GTIN_STATUS)));
                po_item.setVMSTA1(cursor.getString(cursor.getColumnIndex(Po_Item.VMSTA)));

                //this data for write inn sqlserver Log
                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));

                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/


                po_itemlist.add(po_item);
//                if (po_itemlist.isEmpty()){
//                    Log.d("po_itemlist","empty");
//                }
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

        //  Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }

    public  List<Po_Item> Get_Po_Item_for_po_iteam_for_upload(String po_iteam1/*,String PO_UNIT*/) {
        List<Po_Item> po_itemlist = new ArrayList<>();

        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME+
                " where " + Po_Item.PO_ITEM  +" = '" + po_iteam1+"' "; // and +Po_Item.GTIN_STATUS +" != 1 " ;//and
             //   +Po_Item.PO_UNIT +" = '"+PO_UNIT+"'";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));
        Log.e("zzzselectQuery",""+selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Po_Item  po_item = new Po_Item();
                po_item.setID1(cursor.getString(cursor.getColumnIndex(Po_Item.ID)));

                //الوصف
                po_item.setSHORT_TEXT1(cursor.getString(cursor.getColumnIndex(Po_Item.SHORT_TEXT)));
                //رقم الصنف
                po_item.setMATERIAL1(cursor.getString(cursor.getColumnIndex(Po_Item.MATERIAL)));
                //وحده قياس الصنف
                po_item.setMEINH1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINH)));
                //الباركود
                po_item.setEAN111(cursor.getString(cursor.getColumnIndex(Po_Item.EAN11)));
                //الكميه المستلمه سابقا
//                po_item.setPDNEWQTY1(cursor.getString(cursor.getColumnIndex(Po_Item.PDNEWQTY)));
                po_item.setPDNEWQTY1(Get_QTY_for_one_article(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM))));
                //الكميه المطلوبه
                po_item.setQUANTITY1(cursor.getString(cursor.getColumnIndex(Po_Item.QUANTITY)));
                po_item.setVENDOR_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.VENDOR_NAME)));
                po_item.setPO_UNIT1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_UNIT)));
                po_item.setCREATE_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE)));
                po_item.setDELIVERY_DATE1(cursor.getString(cursor.getColumnIndex(Po_Item.DELIVERY_DATE)));
                po_item.setPO_NUMBER1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_NUMBER)));
                po_item.setPO_ITEM1(cursor.getString(cursor.getColumnIndex(Po_Item.PO_ITEM)));
                po_item.setPLANT1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT)));
                po_item.setSTGE_LOC1(cursor.getString(cursor.getColumnIndex(Po_Item.STGE_LOC)));
                po_item.setITEM_CAT1(cursor.getString(cursor.getColumnIndex(Po_Item.ITEM_CAT)));
                po_item.setMEINS1(cursor.getString(cursor.getColumnIndex(Po_Item.MEINS)));
                po_item.setSERNP1(cursor.getString(cursor.getColumnIndex(Po_Item.SERNP)));
                po_item.setPUR_GROUP1(cursor.getString(cursor.getColumnIndex(Po_Item.PUR_GROUP)));
                po_item.setPLANT_NAME1(cursor.getString(cursor.getColumnIndex(Po_Item.PLANT_NAME)));

                po_item.setGTIN_STATUS1(cursor.getString(cursor.getColumnIndex(Po_Item.GTIN_STATUS)));
                po_item.setVMSTA1(cursor.getString(cursor.getColumnIndex(Po_Item.VMSTA)));

                //this data for write inn sqlserver Log
                po_item.setCOMP_CODE1(cursor.getString(cursor.getColumnIndex(Po_Item.COMP_CODE)));

                po_item.setChecked_Item(false);
               /* po_itemlist.add(new Po_Item_of_cycleCount(cursor.getString(cursor.getColumnIndex(Po_Header.PO_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR)),
                        cursor.getString(cursor.getColumnIndex(Po_Header.VENDOR_NAME))));*/


                po_itemlist.add(po_item);
//                if (po_itemlist.isEmpty()){
//                    Log.d("po_itemlist","empty");
//                }
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

        //  Log.d("po_itemlistsize",""+po_itemlist.size());
        // return Po_item list
        return po_itemlist;
    }
    /* public int getPo_HeadersCount() {
        String countQuery = "SELECT  * FROM " + Po_Header.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }*/
//update للكميه المستله سابقاوتاريخ الاستلام
    public int update_PDNEWQTY(String ID, String PDNEWQTY, String DeliveryDate /*,String VendorName*/) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Po_Item.PDNEWQTY, PDNEWQTY);
        values.put(Po_Item.DELIVERY_DATE, DeliveryDate);
     //   values.put(Po_Item.VENDOR_NAME,VendorName);

        // updating row
        return db.update(Po_Item.TABLE_NAME, values, Po_Item.ID + " = ?",
                new String[]{ID});
    }

    //update للكميه المستله سابقاوتاريخ الاستلام
    // to deliever only one iteam
    public int update_po_iteam_nomoreGR_with_X(String PO_ITEM) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Po_Item.NO_MORE_GR,"X");

        // updating row
        return db.update(Po_Item.TABLE_NAME, values, Po_Item.PO_ITEM + " = ?",
                new String[]{PO_ITEM});
    }

    public int update_PDNEWQTYByBarcode(String Barcode, String PDNEWQTY, String DeliveryDate,String PO_UNIT) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Po_Item.PDNEWQTY, PDNEWQTY);
        values.put(Po_Item.DELIVERY_DATE, DeliveryDate);
      // values.put(Po_Item.PO_UNIT,PO_UNIT);

        // updating row
        return db.update(Po_Item.TABLE_NAME, values, Po_Item.EAN11 + " =  '"+Barcode +"' and " +Po_Item.PO_UNIT + " = '" + PO_UNIT+"'",
                null);
    }
    public void DeleteDataOfThreeTables(){
        SQLiteDatabase db = this.getWritableDatabase();
       // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +Po_Header.TABLE_Po_Header_NAME);
        db.execSQL("DELETE FROM " +Po_Item.TABLE_NAME);
        db.execSQL("DELETE FROM " +PO_SERIAL.TABLE_PO_SERIAL_NAME);
        db.close();
    }
    //DeleteUserDataTables
    public void DeleteUserDataAndGroupsTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +Users.TABLE_User_Name);
        db.execSQL("DELETE FROM " +Groups.TABLE_Group_Name);

        db.close();
    }

    public void DeleteSerialTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // "DELETE FROM " + Po_Header.TABLE_Po_Header_NAME;
        db.execSQL("DELETE FROM " +PO_SERIAL.TABLE_PO_SERIAL_NAME);
        db.close();
    }

    public boolean isTableExists(String tableName, boolean openDb) {
       /* if(openDb) {
            if(mDatabase == null || !mDatabase.isOpen()) {
                mDatabase = getReadableDatabase();
            }

            if(!mDatabase.isReadOnly()) {
                mDatabase.close();
                mDatabase = getReadableDatabase();
            }
        }**/
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from Data where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public int update_Vendor_PDNEWQTY_Date(String PDNEWQTY, String DeliveryDate,String VendorName) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Po_Item.PDNEWQTY, PDNEWQTY);
            values.put(Po_Item.DELIVERY_DATE, DeliveryDate);
            values.put(Po_Item.VENDOR_NAME,VendorName);

            // updating row
            return db.update(Po_Item.TABLE_NAME, values, null, null);

    }

    public String select_what_has_NoMore_value() {
        String DocumentNU="";
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item.TABLE_NAME + " where " + Po_Item.NO_MORE_GR +"!=" +"null or "+
                Po_Item.NO_MORE_GR +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                 DocumentNU = cursor.getString(cursor.getColumnIndex(Po_Item.NO_MORE_GR));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();


        // return Po_item list
        return DocumentNU;
    }

    public String select_CreateDate() {
        String CreateDate="";
        /*
        // Select All Query
        String selectQuery = "SELECT * FROM " + Po_Item_of_cycleCount.TABLE_NAME + " where " + Po_Item_of_cycleCount.EAN11 +" = ?"  ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{BarCode});
         */
        // Select All Query
        String selectQuery = "SELECT " + Po_Item.CREATE_DATE + " FROM " + Po_Item.TABLE_NAME +
                " Limit 1 " ;
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Log.d("po_itemlist","empty"+cursor.getString(cursor.getColumnIndex(Po_Item_of_cycleCount.SHORT_TEXT)));

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CreateDate = cursor.getString(cursor.getColumnIndex(Po_Item.CREATE_DATE));

            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();


        // return Po_item list
        return CreateDate;
    }

    public void update_NoMore_To_MaterialNU(String MaterialNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Po_Item.NO_MORE_GR, MaterialNumber);

        String selectQuery = " Update " + Po_Item.TABLE_NAME +" set "+Po_Item.NO_MORE_GR +" = "+MaterialNumber
                + " where " + Po_Item.PDNEWQTY +"!=" +"null or "+ Po_Item.PDNEWQTY +"!=" +"0.0";
        // +" ORDER BY " + Po_Header.COLUMN_PASSWORD + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {


                 cursor.getString(cursor.getColumnIndex(Po_Item.NO_MORE_GR));


            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // updating row
        return ;
//db.update(Po_Item_of_cycleCount.TABLE_NAME, values,  Po_Item_of_cycleCount.PDNEWQTY +"!=" +"null or "+
//                Po_Item_of_cycleCount.PDNEWQTY +"!=" +"0.0", null)
    }


    public void DeleteRowFromSerialTable(String serial) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PO_SERIAL.TABLE_PO_SERIAL_NAME, PO_SERIAL.Serial + " = ?",
                new String[]{String.valueOf(serial)});
        db.close();
    }

  /*  public void deletePo_Header(Po_Header Po_Header) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Po_Header.TABLE_NAME, Po_Header.COLUMN_ID + " = ?",
                new String[]{String.valueOf(Po_Header.getId())});
        db.close();
    }*/

}
