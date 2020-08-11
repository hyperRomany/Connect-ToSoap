package com.example.connecttosoapapiapp.ItemReturn.modules;

public class Item_Return_Header {
    public static final String TABLE_STO_HEADER_NAME = "Item_Return_Header";

    public static final String Vendor = "Vendor";
    public static final String p_org = "p_org";
    public static final String pgrp = "pgrp";
    public static final String Site = "site";
    public static final String Status = "Status";



    // Create table SQL query
    public static final String CREATE_Item_Return_Header_TABLE =
            "CREATE TABLE " + TABLE_STO_HEADER_NAME + "("
                    +Vendor  + " VARCHAR(30),"
                    +p_org  + " VARCHAR(10),"
                    +pgrp  + " VARCHAR(10),"
                    +Site  + " VARCHAR(10),"
                    + Status + " VARCHAR(50)"
                    + ")";

    private String Vendor1 , p_org1 , pgrp1, Site1,Status1;

    public Item_Return_Header() {
    }

    public Item_Return_Header(String vendor1, String p_org1, String pgrp1, String site1, String status1) {
        Vendor1 = vendor1;
        this.p_org1 = p_org1;
        this.pgrp1 = pgrp1;
        Site1 = site1;
        Status1 = status1;
    }

    public String getVendor1() {
        return Vendor1;
    }

    public void setVendor1(String vendor1) {
        Vendor1 = vendor1;
    }

    public String getP_org1() {
        return p_org1;
    }

    public void setP_org1(String p_org1) {
        this.p_org1 = p_org1;
    }

    public String getPgrp1() {
        return pgrp1;
    }

    public void setPgrp1(String pgrp1) {
        this.pgrp1 = pgrp1;
    }

    public String getSite1() {
        return Site1;
    }

    public void setSite1(String site1) {
        Site1 = site1;
    }

    public String getStatus1() {
        return Status1;
    }

    public void setStatus1(String status1) {
        Status1 = status1;
    }
}
