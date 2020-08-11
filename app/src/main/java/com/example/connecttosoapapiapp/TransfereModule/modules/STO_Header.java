package com.example.connecttosoapapiapp.TransfereModule.modules;

public class STO_Header {
    public static final String TABLE_STO_HEADER_NAME = "STO_Header";

    public static final String Sto_CR_Date = "Sto_CR_Date";
    public static final String Iss_Site = "Iss_Site";
    public static final String Iss_Strg_Log = "Iss_Strg_Log";
    public static final String Comp_Code = "Comp_Code";
    public static final String P_Org = "P_Org";
    public static final String P_Grp = "P_Grp";
    public static final String Rec_Site= "Rec_Site";
    public static final String Rec_Site_log = "Rec_Site_log";
    public static final String Sto_Type = "Sto_Type";
    public static final String Sto_Del_Date = "Sto_Del_Date";
    public static final String Status = "Status";



    // Create table SQL query
    public static final String CREATE_STO_HEADER_TABLE =
            "CREATE TABLE " + TABLE_STO_HEADER_NAME + "("
                    +Sto_CR_Date  + " VARCHAR(10),"
                    +Iss_Site  + " VARCHAR(10),"
                    +Iss_Strg_Log  + " VARCHAR(10),"
                    +Comp_Code + " VARCHAR(35),"
                    +P_Org  + " VARCHAR(12),"
                    +P_Grp  + " VARCHAR(12),"
                    +Rec_Site  + " VARCHAR(10),"
                    +Rec_Site_log  + " VARCHAR(10),"
                    +Sto_Type  + " VARCHAR(10),"
                    + Sto_Del_Date + " VARCHAR(20),"
                    + Status + " VARCHAR(50)"
                    + ")";

    private String Sto_CR_Date1 , Iss_Site1 , Iss_Strg_Log1, Comp_Code1, P_Org1 ,
            P_Grp1, Rec_Site1, Rec_Site_log1,Sto_Type1, Sto_Del_Date1, Status1;

    public STO_Header() {
    }

    public STO_Header(String sto_CR_Date1, String iss_Site1, String iss_Strg_Log1,
                      String comp_Code1,
                      String p_Org1, String p_Grp1, String rec_Site1, String rec_Site_log1,
                      String sto_Type1, String sto_Del_Date1, String status1) {
        Sto_CR_Date1 = sto_CR_Date1;
        Iss_Site1 = iss_Site1;
        Iss_Strg_Log1 = iss_Strg_Log1;
        Comp_Code1 = comp_Code1;
        P_Org1 = p_Org1;
        P_Grp1 = p_Grp1;
        Rec_Site1 = rec_Site1;
        Rec_Site_log1 = rec_Site_log1;
        Sto_Type1 = sto_Type1;
        Sto_Del_Date1 = sto_Del_Date1;
        Status1 = status1;
    }

    public String getSto_CR_Date1() {
        return Sto_CR_Date1;
    }

    public void setSto_CR_Date1(String sto_CR_Date1) {
        Sto_CR_Date1 = sto_CR_Date1;
    }

    public String getIss_Site1() {
        return Iss_Site1;
    }

    public void setIss_Site1(String iss_Site1) {
        Iss_Site1 = iss_Site1;
    }

    public String getIss_Strg_Log1() {
        return Iss_Strg_Log1;
    }

    public void setIss_Strg_Log1(String iss_Strg_Log1) {
        Iss_Strg_Log1 = iss_Strg_Log1;
    }

    public String getComp_Code1() {
        return Comp_Code1;
    }

    public void setComp_Code1(String comp_Code1) {
        Comp_Code1 = comp_Code1;
    }

    public String getP_Org1() {
        return P_Org1;
    }

    public void setP_Org1(String p_Org1) {
        P_Org1 = p_Org1;
    }

    public String getP_Grp1() {
        return P_Grp1;
    }

    public void setP_Grp1(String p_Grp1) {
        P_Grp1 = p_Grp1;
    }

    public String getRec_Site1() {
        return Rec_Site1;
    }

    public void setRec_Site1(String rec_Site1) {
        Rec_Site1 = rec_Site1;
    }

    public String getRec_Site_log1() {
        return Rec_Site_log1;
    }

    public void setRec_Site_log1(String rec_Site_log1) {
        Rec_Site_log1 = rec_Site_log1;
    }

    public String getSto_Type1() {
        return Sto_Type1;
    }

    public void setSto_Type1(String sto_Type1) {
        Sto_Type1 = sto_Type1;
    }

    public String getSto_Del_Date1() {
        return Sto_Del_Date1;
    }

    public void setSto_Del_Date1(String sto_Del_Date1) {
        Sto_Del_Date1 = sto_Del_Date1;
    }

    public String getStatus1() {
        return Status1;
    }

    public void setStatus1(String status1) {
        Status1 = status1;
    }
}
