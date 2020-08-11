package com.example.connecttosoapapiapp.ReceivingModule.model;

public class Po_Item {

    public static final String TABLE_NAME = "Po_Item_of_Recieving";

    public static final String ID = "id";
    public static final String MATERIAL = "MATERIAL";
    public static final String SHORT_TEXT = "SHORT_TEXT";
    public static final String QUANTITY = "QUANTITY";
    public static final String PO_UNIT = "PO_UNIT";
    public static final String AUOM = "AUOM";
    public static final String QUANTITY_BUOM = "QUANTITY_BUOM";
    public static final String MEINS = "MEINS";
    public static final String NETPR = "NETPR";
    public static final String NETWR = "NETWR";
    public static final String CURRENCY = "CURRENCY";
    public static final String EAN11 = "EAN11";
    public static final String MEINH = "MEINH";
    public static final String GTIN_STATUS = "GTIN_STATUS";
    public static final String VMSTA = "VMSTA";
    public static final String PLANT = "PLANT";
    public static final String  PLANT_NAME= "PLANT_NAME";
    public static final String PO_NUMBER = "PO_NUMBER";
    public static final String  COMP_CODE= "COMP_CODE";
    public static final String  DOC_TYPE= "DOC_TYPE";
    public static final String  DELIVERY_DATE= "DELIVERY_DATE";
    public static final String  PO_ITEM= "PO_ITEM";
    public static final String  CREATE_DATE= "CREATE_DATE";
    public static final String  PUR_GROUP= "PUR_GROUP";
    public static final String  STGE_LOC= "STGE_LOC";
    public static final String  ITEM_CAT= "ITEM_CAT";
    public static final String  SERNP= "SERNP";
    public static final String  UMREZ= "UMREZ";
    public static final String  UMREN= "UMREN";
    public static final String  PDNEWQTY= "PDNEWQTY";
    public static final String  PDNEWUOM= "PDNEWUOM";
    public static final String  VENDOR_NAME= "VENDOR_NAME";
    public static final String  NO_MORE_GR= "NO_MORE_GR";

    private  Boolean Checked_Item;
    private  String ID1;
    private  String MATERIAL1;
    private  String SHORT_TEXT1;
    private  String QUANTITY1;
    private  String PO_UNIT1;
    private  String AUOM1;
    private  String QUANTITY_BUOM1;
    private  String MEINS1;
    private  String NETPR1;
    private  String NETWR1;
    private  String CURRENCY1;
    private  String EAN111;
    private  String MEINH1;
    private  String GTIN_STATUS1;
    private  String VMSTA1;
    private  String PLANT1;
    private  String  PLANT_NAME1;
    private  String PO_NUMBER1;
    private  String  COMP_CODE1;
    private  String  DOC_TYPE1;
    private  String  DELIVERY_DATE1;
    private  String  PO_ITEM1;
    private  String  CREATE_DATE1;
    private  String  PUR_GROUP1;
    private  String  STGE_LOC1;
    private  String  ITEM_CAT1;
    private  String  SERNP1;
    private  String  UMREZ1;
    private  String  UMREN1;

    private String  PDNEWQTY1 ;
    private  String  PDNEWUOM1 ;
    private  String  VENDOR_NAME1 ;
    private  String  NO_MORE_GR1 ;


    // Create table SQL query
    public static final String CREATE_Po_Item_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MATERIAL + " VARCHAR(18),"
                    + SHORT_TEXT + " VARCHAR(40),"
                    + QUANTITY + " VARCHAR(13),"
                    + PO_UNIT + " VARCHAR(3),"
                    + AUOM + " VARCHAR(3),"
                    + QUANTITY_BUOM + " VARCHAR(13),"
                    + MEINS + " VARCHAR(3),"
                    + NETPR + " VARCHAR(11),"
                    + NETWR + " VARCHAR(13),"
                    +CURRENCY + " VARCHAR(5),"
                    + EAN11 + " VARCHAR(18),"
                    + MEINH + " VARCHAR(3),"
                    + GTIN_STATUS + " VARCHAR(1),"
                    + VMSTA + " VARCHAR(2),"
                    + PLANT + " VARCHAR(4),"
                    + PLANT_NAME + " VARCHAR(30),"
                    + PO_NUMBER + " VARCHAR(10),"
                    + COMP_CODE + " VARCHAR(4),"
                    + DOC_TYPE + " VARCHAR(4),"
                    + DELIVERY_DATE + " VARCHAR(10),"
                    + PO_ITEM + " VARCHAR(5),"
                    + CREATE_DATE + " VARCHAR(8),"
                    + PUR_GROUP + " VARCHAR(3),"
                    + STGE_LOC + " VARCHAR(4),"
                    + ITEM_CAT + " VARCHAR(1),"
                    + SERNP + " VARCHAR(18),"
                    + UMREZ + " VARCHAR(5),"   //>>> this column isn't in document image
                    + UMREN + " VARCHAR(5),"    //>>> this column isn't in document image

                    + PDNEWQTY + " VARCHAR(20),"
                    + PDNEWUOM + " VARCHAR(20),"
                    + VENDOR_NAME + " VARCHAR(50),"
                    + NO_MORE_GR + " VARCHAR(225)"
                    + ")";

    public Po_Item() {
    }

    public Po_Item(Boolean Checked_Item, String MATERIAL1, String SHORT_TEXT1, String QUANTITY1,String PDNEWQTY1,
                   String PO_UNIT1, String EAN111, String VENDOR_NAME1) {
        this.Checked_Item=Checked_Item;
        this.MATERIAL1 = MATERIAL1;
        this.SHORT_TEXT1 = SHORT_TEXT1;
        this.QUANTITY1 = QUANTITY1;
        this.PDNEWQTY1 = PDNEWQTY1;
        this.PO_UNIT1 = PO_UNIT1;
        this.EAN111 = EAN111;
        this.VENDOR_NAME1 = VENDOR_NAME1;

    }

    public Po_Item(String MATERIAL1, String SHORT_TEXT1) {
        this.MATERIAL1 = MATERIAL1;
        this.SHORT_TEXT1 = SHORT_TEXT1;
    }

    //This constructor For upload
    private  String  PSTNG_DATE ;
    private  String  DOC_DATE ;

    public Po_Item(String PSTNG_DATE,String  DOC_DATE,String PO_NUMBER1,
             String PO_ITEM1, String NO_MORE_GR1,String ITEM_CAT1,
                   String MATERIAL1, String PLANT, String STGE_LOC1,
                    String PDNEWQTY1, String QUANTITY_BUOM1, String EAN111,
                   String SERNP) {
        this.PSTNG_DATE=PSTNG_DATE;
        this.DOC_DATE=DOC_DATE;
        this.MATERIAL1 = MATERIAL1;
        this.QUANTITY_BUOM1 = QUANTITY_BUOM1;
        this.PO_NUMBER1 = PO_NUMBER1;
        this.PO_ITEM1 = PO_ITEM1;
        this.STGE_LOC1 = STGE_LOC1;
        this.ITEM_CAT1 = ITEM_CAT1;
        this.PDNEWQTY1 = PDNEWQTY1;
        this.NO_MORE_GR1 = NO_MORE_GR1;
        this.PLANT1= PLANT;
        this.EAN111 =EAN111;
        this.SERNP1 = SERNP;
    }

    public Po_Item(String PO_NUMBER1inserail,String PO_ITEM1inserial,String SERIALNO){

    }

    public String getUMREZ1() {
        return UMREZ1;
    }

    public void setUMREZ1(String UMREZ1) {
        this.UMREZ1 = UMREZ1;
    }

    public String getUMREN1() {
        return UMREN1;
    }

    public void setUMREN1(String UMREN1) {
        this.UMREN1 = UMREN1;
    }

    public String getID1() {
        return ID1;
    }

    public void setID1(String ID1) {
        this.ID1 = ID1;
    }

    public Boolean getChecked_Item() {
        return Checked_Item;
    }

    public void setChecked_Item(Boolean checked_Item) {
        Checked_Item = checked_Item;
    }

    public String getPDNEWQTY1() {
        return PDNEWQTY1;
    }

    public void setPDNEWQTY1(String PDNEWQTY1) {
        this.PDNEWQTY1 = PDNEWQTY1;
    }

    public String getPDNEWUOM1() {
        return PDNEWUOM1;
    }

    public void setPDNEWUOM1(String PDNEWUOM1) {
        this.PDNEWUOM1 = PDNEWUOM1;
    }

    public String getVENDOR_NAME1() {
        return VENDOR_NAME1;
    }

    public void setVENDOR_NAME1(String VENDOR_NAME1) {
        this.VENDOR_NAME1 = VENDOR_NAME1;
    }

    public String getNO_MORE_GR1() {
        return NO_MORE_GR1;
    }

    public void setNO_MORE_GR1(String NO_MORE_GR1) {
        this.NO_MORE_GR1 = NO_MORE_GR1;
    }

    public String getMATERIAL1() {
        return MATERIAL1;
    }

    public void setMATERIAL1(String MATERIAL1) {
        this.MATERIAL1 = MATERIAL1;
    }

    public String getSHORT_TEXT1() {
        return SHORT_TEXT1;
    }

    public void setSHORT_TEXT1(String SHORT_TEXT1) {
        this.SHORT_TEXT1 = SHORT_TEXT1;
    }

    public String getQUANTITY1() {
        return QUANTITY1;
    }

    public void setQUANTITY1(String QUANTITY1) {
        this.QUANTITY1 = QUANTITY1;
    }

    public String getPO_UNIT1() {
        return PO_UNIT1;
    }

    public void setPO_UNIT1(String PO_UNIT1) {
        this.PO_UNIT1 = PO_UNIT1;
    }

    public String getAUOM1() {
        return AUOM1;
    }

    public void setAUOM1(String AUOM1) {
        this.AUOM1 = AUOM1;
    }

    public String getQUANTITY_BUOM1() {
        return QUANTITY_BUOM1;
    }

    public void setQUANTITY_BUOM1(String QUANTITY_BUOM1) {
        this.QUANTITY_BUOM1 = QUANTITY_BUOM1;
    }

    public String getMEINS1() {
        return MEINS1;
    }

    public void setMEINS1(String MEINS1) {
        this.MEINS1 = MEINS1;
    }

    public String getNETPR1() {
        return NETPR1;
    }

    public void setNETPR1(String NETPR1) {
        this.NETPR1 = NETPR1;
    }

    public String getNETWR1() {
        return NETWR1;
    }

    public void setNETWR1(String NETWR1) {
        this.NETWR1 = NETWR1;
    }

    public String getCURRENCY1() {
        return CURRENCY1;
    }

    public void setCURRENCY1(String CURRENCY1) {
        this.CURRENCY1 = CURRENCY1;
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

    public String getGTIN_STATUS1() {
        return GTIN_STATUS1;
    }

    public void setGTIN_STATUS1(String GTIN_STATUS1) {
        this.GTIN_STATUS1 = GTIN_STATUS1;
    }

    public String getVMSTA1() {
        return VMSTA1;
    }

    public void setVMSTA1(String VMSTA1) {
        this.VMSTA1 = VMSTA1;
    }

    public String getPLANT1() {
        return PLANT1;
    }

    public void setPLANT1(String PLANT1) {
        this.PLANT1 = PLANT1;
    }

    public String getPLANT_NAME1() {
        return PLANT_NAME1;
    }

    public void setPLANT_NAME1(String PLANT_NAME1) {
        this.PLANT_NAME1 = PLANT_NAME1;
    }

    public String getPO_NUMBER1() {
        return PO_NUMBER1;
    }

    public void setPO_NUMBER1(String PO_NUMBER1) {
        this.PO_NUMBER1 = PO_NUMBER1;
    }

    public String getCOMP_CODE1() {
        return COMP_CODE1;
    }

    public void setCOMP_CODE1(String COMP_CODE1) {
        this.COMP_CODE1 = COMP_CODE1;
    }

    public String getDOC_TYPE1() {
        return DOC_TYPE1;
    }

    public void setDOC_TYPE1(String DOC_TYPE1) {
        this.DOC_TYPE1 = DOC_TYPE1;
    }

    public String getDELIVERY_DATE1() {
        return DELIVERY_DATE1;
    }

    public void setDELIVERY_DATE1(String DELIVERY_DATE1) {
        this.DELIVERY_DATE1 = DELIVERY_DATE1;
    }

    public String getPO_ITEM1() {
        return PO_ITEM1;
    }

    public void setPO_ITEM1(String PO_ITEM1) {
        this.PO_ITEM1 = PO_ITEM1;
    }

    public String getCREATE_DATE1() {
        return CREATE_DATE1;
    }

    public void setCREATE_DATE1(String CREATE_DATE1) {
        this.CREATE_DATE1 = CREATE_DATE1;
    }

    public String getPUR_GROUP1() {
        return PUR_GROUP1;
    }

    public void setPUR_GROUP1(String PUR_GROUP1) {
        this.PUR_GROUP1 = PUR_GROUP1;
    }

    public String getSTGE_LOC1() {
        return STGE_LOC1;
    }

    public void setSTGE_LOC1(String STGE_LOC1) {
        this.STGE_LOC1 = STGE_LOC1;
    }

    public String getITEM_CAT1() {
        return ITEM_CAT1;
    }

    public void setITEM_CAT1(String ITEM_CAT1) {
        this.ITEM_CAT1 = ITEM_CAT1;
    }

    public String getSERNP1() {
        return SERNP1;
    }

    public void setSERNP1(String SERNP1) {
        this.SERNP1 = SERNP1;
    }

   /* public String getUMREZ1() {
        return UMREZ1;
    }

    public void setUMREZ1(String UMREZ1) {
        this.UMREZ1 = UMREZ1;
    }

    public String getUMREN1() {
        return UMREN1;
    }

    public void setUMREN1(String UMREN1) {
        this.UMREN1 = UMREN1;
    }*/
}
