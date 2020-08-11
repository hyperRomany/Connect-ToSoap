package com.example.connecttosoapapiapp.ItemReturn.modules;

public class Item_Return_Search {

    public static final String TABLE_Item_Return_Search_NAME = "Item_Return_Search";

    public static final String GTIN = "GTIN";
    public static final String Desc = "Desc";
    public static final String MAT_CODE = "MAT_CODE";
    public static final String STATUS = "STATUS";
    public static final String QTY = "QTY";

    public static final String UOM ="UOM";
    public static final String  P_GRP="P_GRP";
    public static final String   P_ORG ="P_ORG";
    public static final String   STG_LOC="STG_LOC";
    public static final String   DEF_STG_LOC="DEF_STG_LOC";




    // Create table SQL query
    public static final String CREATE_Item_Return_TABLE =
            "CREATE TABLE " + TABLE_Item_Return_Search_NAME + "("
                    + GTIN + " VARCHAR(30),"
                    + Desc + " VARCHAR(30),"
                    + MAT_CODE + " VARCHAR(35),"
                    + STATUS + " VARCHAR(10),"
                    + QTY + " VARCHAR(20),"
                    + UOM + " VARCHAR(30),"
                    + P_GRP + " VARCHAR(30),"
                    + P_ORG + " VARCHAR(30),"
                    + STG_LOC + " VARCHAR(30),"
                    + DEF_STG_LOC + " VARCHAR(30)"
                    + ")";

    private String GTIN1, Desc1, MAT_CODE1, STATUS1,QTY1,UOM1,P_GRP1,P_ORG1,STG_LOC1,DEF_STG_LOC1;
    private  Boolean Checked_Item;

    public Item_Return_Search() {
    }

    public Item_Return_Search(String GTIN1, String desc1, String MAT_CODE1, String STATUS1, String QTY1, Boolean checked_Item) {
        this.GTIN1 = GTIN1;
        Desc1 = desc1;
        this.MAT_CODE1 = MAT_CODE1;
        this.STATUS1 = STATUS1;
        this.QTY1 = QTY1;
        Checked_Item = checked_Item;
    }

    public String getUOM1() {
        return UOM1;
    }

    public void setUOM1(String UOM1) {
        this.UOM1 = UOM1;
    }

    public String getP_GRP1() {
        return P_GRP1;
    }

    public void setP_GRP1(String p_GRP1) {
        P_GRP1 = p_GRP1;
    }

    public String getP_ORG1() {
        return P_ORG1;
    }

    public void setP_ORG1(String p_ORG1) {
        P_ORG1 = p_ORG1;
    }

    public String getSTG_LOC1() {
        return STG_LOC1;
    }

    public void setSTG_LOC1(String STG_LOC1) {
        this.STG_LOC1 = STG_LOC1;
    }

    public String getDEF_STG_LOC1() {
        return DEF_STG_LOC1;
    }

    public void setDEF_STG_LOC1(String DEF_STG_LOC1) {
        this.DEF_STG_LOC1 = DEF_STG_LOC1;
    }

    public String getGTIN1() {
        return GTIN1;
    }

    public void setGTIN1(String GTIN1) {
        this.GTIN1 = GTIN1;
    }

    public String getDesc1() {
        return Desc1;
    }

    public void setDesc1(String desc1) {
        Desc1 = desc1;
    }

    public String getMAT_CODE1() {
        return MAT_CODE1;
    }

    public void setMAT_CODE1(String MAT_CODE1) {
        this.MAT_CODE1 = MAT_CODE1;
    }

    public String getSTATUS1() {
        return STATUS1;
    }

    public void setSTATUS1(String STATUS1) {
        this.STATUS1 = STATUS1;
    }

    public String getQTY1() {
        return QTY1;
    }

    public void setQTY1(String QTY1) {
        this.QTY1 = QTY1;
    }

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }
}
