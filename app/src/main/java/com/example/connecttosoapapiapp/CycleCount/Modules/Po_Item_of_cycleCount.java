package com.example.connecttosoapapiapp.CycleCount.Modules;

public class Po_Item_of_cycleCount {

    public static final String TABLE_NAME = "Po_Item_of_cycleCount";

    public static final String ID = "id";
    public static final String PHYSINVENTORY = "PHYSINVENTORY";
    public static final String MATERIAL = "MATERIAL";
    public static final String MAKTX = "MAKTX";
    public static final String QTY = "QTY";
    public static final String ITEM ="ITEM";
    public static final String BASE_UOM = "BASE_UOM";
    public static final String SERNP = "SERNP";
    public static final String EAN11 = "EAN11";
    public static final String MEINH = "MEINH";


    private Boolean Checked_Item;
    private String ID1;
    private String PHYSINVENTORY1;
    private String MATERIAL1;
    private String MAKTX1;
    private String BASE_UOM1;
    private String ITEM1;
    private String QTY1;
    private String SERNP1;
    private String EAN111;
    private String MEINH1;

    // Create table SQL query
    public static final String CREATE_Po_Item_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PHYSINVENTORY + " VARCHAR(20),"
                    + MATERIAL + " VARCHAR(20),"
                    + MAKTX + " VARCHAR(40),"
                    + QTY + " VARCHAR(20),"
                    + ITEM + " VARCHAR(20),"
                    + BASE_UOM + " VARCHAR(20),"
                    + SERNP + " VARCHAR(20),"
                    + EAN11 + " VARCHAR(20),"
                    + MEINH + " VARCHAR(20)"
                    + ")";

    public Po_Item_of_cycleCount() {
    }

    public Po_Item_of_cycleCount(String PHYSINVENTORY , String MATERIAL1, String MAKTX1, String QTY1,String ITEM1, String BASE_UOM1,
                                 String SERNP1, String EAN111, String MEINH1) {

        this.PHYSINVENTORY1 =PHYSINVENTORY;
        this.MATERIAL1 = MATERIAL1;
        this.MAKTX1 = MAKTX1;
        this.QTY1 = QTY1;
        this.ITEM1 = ITEM1;
        this.BASE_UOM1 = BASE_UOM1;
        this.SERNP1 = SERNP1;
        this.EAN111 = EAN111;
        this.MEINH1 = MEINH1;

    }

    public String getPHYSINVENTORY1() {
        return PHYSINVENTORY1;
    }

    public void setPHYSINVENTORY1(String PHYSINVENTORY1) {
        this.PHYSINVENTORY1 = PHYSINVENTORY1;
    }

    public String getITEM1() {
        return ITEM1;
    }

    public void setITEM1(String ITEM1) {
        this.ITEM1 = ITEM1;
    }

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public String getMATERIAL1() {
        return MATERIAL1;
    }

    public void setMATERIAL1(String MATERIAL1) {
        this.MATERIAL1 = MATERIAL1;
    }

    public String getMAKTX1() {
        return MAKTX1;
    }

    public void setMAKTX1(String MAKTX1) {
        this.MAKTX1 = MAKTX1;
    }

    public String getBASE_UOM1() {
        return BASE_UOM1;
    }

    public void setBASE_UOM1(String BASE_UOM1) {
        this.BASE_UOM1 = BASE_UOM1;
    }

    public String getQTY1() {
        return QTY1;
    }

    public void setQTY1(String QTY1) {
        this.QTY1 = QTY1;
    }

    public String getSERNP1() {
        return SERNP1;
    }

    public void setSERNP1(String SERNP1) {
        this.SERNP1 = SERNP1;
    }

    public String getEAN111() {
        return EAN111;
    }

    public void setEAN111(String EAN111) {
        this.EAN111 = EAN111;
    }

    public String getMEINH1() {
        return MEINH1;
    }

    public void setMEINH1(String MEINH1) {
        this.MEINH1 = MEINH1;
    }
}