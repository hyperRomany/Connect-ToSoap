package com.example.connecttosoapapiapp.ReceivingModule.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Po_Header {
    public static final String TABLE_Po_Header_NAME = "Po_Header_Recieving";

    public static final String ID = "ID";
    public static final String PO_NUMBER = "PO_NUMBER";
    public static final String DOC_TYPE = "DOC_TYPE";
    public static final String VENDOR = "VENDOR";
    public static final String VENDOR_NAME = "VENDOR_NAME";
    public static final String CREATE_BY = "CREATE_BY";
    public static final String Delievered_BY= "Delievered_BY";
    public static final String DELIVERY_DATE = "DELIVERY_DATE";
    public static final String COMP_CODE = "COMP_CODE";
    public static final String PUR_GROUP = "PUR_GROUP";
    public static final String NO_MORE_GR = "NO_MORE_GR";



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_Po_Header_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +PO_NUMBER  + " VARCHAR(10),"
                    +DOC_TYPE  + " VARCHAR(4),"
                    +VENDOR  + " VARCHAR(10),"
                    +VENDOR_NAME + " VARCHAR(35),"
                    +CREATE_BY  + " VARCHAR(12),"
                    +Delievered_BY  + " VARCHAR(12),"
                    +DELIVERY_DATE  + " VARCHAR(10),"
                    +COMP_CODE  + " VARCHAR(4),"
                    +PUR_GROUP  + " VARCHAR(3),"
                    + NO_MORE_GR + " VARCHAR(1)"
                    + ")";

  private String PO_NUMBER1 , DOC_TYPE1 , VENDOR11, VENDOR_NAME1, CREATE_BY1,Delievered_BY1, DELIVERY_DATE1,
          COMP_CODE1, PUR_GROUP1, NO_MORE_GR1;

    public Po_Header() {

    }

    public Po_Header(String PO_NUMBER1, String VENDOR11, String VENDOR_NAME1) {
        this.PO_NUMBER1 = PO_NUMBER1;
        this.VENDOR11 = VENDOR11;
        this.VENDOR_NAME1 = VENDOR_NAME1;
    }

    public Po_Header(String PO_NUMBER1, String DOC_TYPE1, String VENDOR11, String VENDOR_NAME1,
                     String CREATE_BY1, String DELIVERY_DATE1, String COMP_CODE1,
                     String PUR_GROUP1, String NO_MORE_GR1) {
        this.PO_NUMBER1 = PO_NUMBER1;
        this.DOC_TYPE1 = DOC_TYPE1;
        this.VENDOR11 = VENDOR11;
        this.VENDOR_NAME1 = VENDOR_NAME1;
        this.CREATE_BY1 = CREATE_BY1;
        this.Delievered_BY1 = Delievered_BY1;
        this.DELIVERY_DATE1 = DELIVERY_DATE1;
        this.COMP_CODE1 = COMP_CODE1;
        this.PUR_GROUP1 = PUR_GROUP1;
        this.NO_MORE_GR1 = NO_MORE_GR1;
    }

    public String getDelievered_BY1() {
        return Delievered_BY1;
    }

    public void setDelievered_BY1(String delievered_BY1) {
        Delievered_BY1 = delievered_BY1;
    }

    public String getPO_NUMBER1() {
        return PO_NUMBER1;
    }

    public void setPO_NUMBER1(String PO_NUMBER1) {
        this.PO_NUMBER1 = PO_NUMBER1;
    }

    public String getDOC_TYPE1() {
        return DOC_TYPE1;
    }

    public void setDOC_TYPE1(String DOC_TYPE1) {
        this.DOC_TYPE1 = DOC_TYPE1;
    }

    public String getVENDOR11() {
        return VENDOR11;
    }

    public void setVENDOR11(String VENDOR11) {
        this.VENDOR11 = VENDOR11;
    }

    public String getVENDOR_NAME1() {
        return VENDOR_NAME1;
    }

    public void setVENDOR_NAME1(String VENDOR_NAME1) {
        this.VENDOR_NAME1 = VENDOR_NAME1;
    }

    public String getCREATE_BY1() {
        return CREATE_BY1;
    }

    public void setCREATE_BY1(String CREATE_BY1) {
        this.CREATE_BY1 = CREATE_BY1;
    }

    public String getDELIVERY_DATE1() {
        return DELIVERY_DATE1;
    }

    public void setDELIVERY_DATE1(String DELIVERY_DATE1) {
        this.DELIVERY_DATE1 = DELIVERY_DATE1;
    }

    public String getCOMP_CODE1() {
        return COMP_CODE1;
    }

    public void setCOMP_CODE1(String COMP_CODE1) {
        this.COMP_CODE1 = COMP_CODE1;
    }

    public String getPUR_GROUP1() {
        return PUR_GROUP1;
    }

    public void setPUR_GROUP1(String PUR_GROUP1) {
        this.PUR_GROUP1 = PUR_GROUP1;
    }

    public String getNO_MORE_GR1() {
        return NO_MORE_GR1;
    }

    public void setNO_MORE_GR1(String NO_MORE_GR1) {
        this.NO_MORE_GR1 = NO_MORE_GR1;
    }
}
