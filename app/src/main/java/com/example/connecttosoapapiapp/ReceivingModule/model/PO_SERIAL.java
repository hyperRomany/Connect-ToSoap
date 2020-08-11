package com.example.connecttosoapapiapp.ReceivingModule.model;

public class PO_SERIAL {

    public static final String TABLE_PO_SERIAL_NAME = "PO_SERIAL";

    public static final String ID = "ID";
    public static final String Material = "Material";
    public static final String Po_Item = "Po_Item_of_cycleCount";
    public static final String EAN1 = "EAN1";
    public static final String Serial = "Serial";


    // Create table SQL query
    public static final String CREATE_PO_SERIAL_TABLE =
            "CREATE TABLE " + TABLE_PO_SERIAL_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Material + " VARCHAR(18),"
                    + EAN1 + " VARCHAR(18),"
                    + Po_Item + " VARCHAR(18),"
                    + Serial + " VARCHAR(18)"
                    + ")";
    private Boolean Checked_Item;

    private String Material1;
    private String EAN11;
    private String Serial1;
    private String Po_Item1;

    public PO_SERIAL() {
    }

    public PO_SERIAL(Boolean checked_Item, String material1, String EAN11, String po_Item1, String serial1) {
        Checked_Item = checked_Item;
        Material1 = material1;
        this.EAN11 = EAN11;
        Serial1 = serial1;
        Po_Item1 = po_Item1;
    }

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }

    public String getMaterial1() {
        return Material1;
    }

    public void setMaterial1(String material1) {
        Material1 = material1;
    }

    public String getEAN11() {
        return EAN11;
    }

    public void setEAN11(String EAN11) {
        this.EAN11 = EAN11;
    }

    public String getSerial1() {
        return Serial1;
    }

    public void setSerial1(String serial1) {
        Serial1 = serial1;
    }

    public String getPo_Item1() {
        return Po_Item1;
    }

    public void setPo_Item1(String po_Item1) {
        Po_Item1 = po_Item1;
    }
}