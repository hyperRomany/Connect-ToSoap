package com.example.connecttosoapapiapp.TransfereModule.modules;

public class STo_Search {

    public static final String TABLE_STO_Search_NAME = "STO_Search";

    public static final String GTIN = "GTIN";
    public static final String ISS_SITE = "ISS_SITE";
    public static final String REC_SITE = "REC_SITE";
    public static final String MAT_CODE = "MAT_CODE";
    public static final String UOM_DESC = "UOM_DESC";
    public static final String MEINH = "MEINH";
    public static final String UOM = "UOM";
    public static final String STATUS = "STATUS";
    public static final String ISS_STG_LOG = "ISS_STG_LOG";
    public static final String REC_SITE_LOG = "REC_SITE_LOG";
    public static final String AVAILABLE_STOCK = "AVAILABLE_STOCK";
    public static final String EKGRP = "EKGRP";
    public static final String QTY = "QTY";


    // Create table SQL query
    public static final String CREATE_STO_SEARCH_TABLE =
            "CREATE TABLE " + TABLE_STO_Search_NAME + "("
                    + GTIN + " VARCHAR(10),"
                    + ISS_SITE + " VARCHAR(10),"
                    + REC_SITE + " VARCHAR(10),"
                    + MAT_CODE + " VARCHAR(35),"
                    + UOM_DESC + " VARCHAR(12),"
                    + MEINH + " VARCHAR(12),"
                    + UOM + " VARCHAR(10),"
                    + STATUS + " VARCHAR(10),"
                    + ISS_STG_LOG + " VARCHAR(10),"
                    + REC_SITE_LOG + " VARCHAR(20),"
                    + AVAILABLE_STOCK + " VARCHAR(50),"
                    + EKGRP + " VARCHAR(20),"
                    + QTY + " VARCHAR(20)"
                    + ")";

    private String GTIN1, ISS_SITE1, REC_SITE1, MAT_CODE1, UOM_DESC1,
            MEINH1, UOM1, STATUS1, ISS_STG_LOG1, REC_SITE_LOG1, AVAILABLE_STOCK1,EKGRP1,QTY1;
    private  Boolean Checked_Item;

    public STo_Search() {
    }

    public STo_Search(String GTIN1, String ISS_SITE1, String REC_SITE1, String MAT_CODE1, String UOM_DESC1,
                      String MEINH1, String UOM1, String STATUS1,
                      String ISS_STG_LOG1, String REC_SITE_LOG1, String AVAILABLE_STOCK1, String EKGRP1,
                      String QTY1) {
        this.GTIN1 = GTIN1;
        this.ISS_SITE1 = ISS_SITE1;
        this.REC_SITE1 = REC_SITE1;
        this.MAT_CODE1 = MAT_CODE1;
        this.UOM_DESC1 = UOM_DESC1;
        this.MEINH1 = MEINH1;
        this.UOM1 = UOM1;
        this.STATUS1 = STATUS1;
        this.ISS_STG_LOG1 = ISS_STG_LOG1;
        this.REC_SITE_LOG1 = REC_SITE_LOG1;
        this.AVAILABLE_STOCK1 = AVAILABLE_STOCK1;
        this.EKGRP1 = EKGRP1;
        this.QTY1 = QTY1;
    }

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }

    public String getGTIN1() {
        return GTIN1;
    }

    public void setGTIN1(String GTIN1) {
        this.GTIN1 = GTIN1;
    }

    public String getISS_SITE1() {
        return ISS_SITE1;
    }

    public void setISS_SITE1(String ISS_SITE1) {
        this.ISS_SITE1 = ISS_SITE1;
    }

    public String getREC_SITE1() {
        return REC_SITE1;
    }

    public void setREC_SITE1(String REC_SITE1) {
        this.REC_SITE1 = REC_SITE1;
    }

    public String getMAT_CODE1() {
        return MAT_CODE1;
    }

    public void setMAT_CODE1(String MAT_CODE1) {
        this.MAT_CODE1 = MAT_CODE1;
    }

    public String getUOM_DESC1() {
        return UOM_DESC1;
    }

    public void setUOM_DESC1(String UOM_DESC1) {
        this.UOM_DESC1 = UOM_DESC1;
    }

    public String getMEINH1() {
        return MEINH1;
    }

    public void setMEINH1(String MEINH1) {
        this.MEINH1 = MEINH1;
    }

    public String getUOM1() {
        return UOM1;
    }

    public void setUOM1(String UOM1) {
        this.UOM1 = UOM1;
    }

    public String getSTATUS1() {
        return STATUS1;
    }

    public void setSTATUS1(String STATUS1) {
        this.STATUS1 = STATUS1;
    }

    public String getISS_STG_LOG1() {
        return ISS_STG_LOG1;
    }

    public void setISS_STG_LOG1(String ISS_STG_LOG1) {
        this.ISS_STG_LOG1 = ISS_STG_LOG1;
    }

    public String getREC_SITE_LOG1() {
        return REC_SITE_LOG1;
    }

    public void setREC_SITE_LOG1(String REC_SITE_LOG1) {
        this.REC_SITE_LOG1 = REC_SITE_LOG1;
    }

    public String getAVAILABLE_STOCK1() {
        return AVAILABLE_STOCK1;
    }

    public void setAVAILABLE_STOCK1(String AVAILABLE_STOCK1) {
        this.AVAILABLE_STOCK1 = AVAILABLE_STOCK1;
    }

    public String getEKGRP1() {
        return EKGRP1;
    }

    public void setEKGRP1(String EKGRP1) {
        this.EKGRP1 = EKGRP1;
    }

    public String getQTY1() {
        return QTY1;
    }

    public void setQTY1(String QTY1) {
        this.QTY1 = QTY1;
    }
}
