package com.example.connecttosoapapiapp.ReceivingModule.model;

public class Po_Items_For_Logs_Items_SqlServer {

    private String SAP_STO_NUMBER,P_ORG,P_GRP,ISS_SITE,ISS_STRG_LOC,REC_SITE,REC_STRG_LOC,
            STO_CR_DATE,MAT_CODE,GTIN,GTIN_Desc,MEINH,QTY,USER_IDD,USER_NAMME,MODULE;

    public Po_Items_For_Logs_Items_SqlServer() {
    }

    public Po_Items_For_Logs_Items_SqlServer
            ( String SAP_STO_NUMBER,
                                             String p_ORG, String p_GRP, String ISS_SITE,
                                             String ISS_STRG_LOC, String REC_SITE,
                                             String REC_STRG_LOC, String STO_CR_DATE,
                                             String MAT_CODE, String GTIN, String GTIN_Desc,
                                             String MEINH, String QTY, String USER_IDD,
                                             String USER_NAMME, String MODULE) {

        this.SAP_STO_NUMBER = SAP_STO_NUMBER;
        this.P_ORG = p_ORG;
        this.P_GRP = p_GRP;
        this.ISS_SITE = ISS_SITE;
        this.ISS_STRG_LOC = ISS_STRG_LOC;
        this.REC_SITE = REC_SITE;
        this.REC_STRG_LOC = REC_STRG_LOC;
        this.STO_CR_DATE = STO_CR_DATE;
        this.MAT_CODE = MAT_CODE;
        this.GTIN = GTIN;
        this.GTIN_Desc = GTIN_Desc;
        this.MEINH = MEINH;
        this.QTY = QTY;
        this.USER_IDD = USER_IDD;
        this.USER_NAMME = USER_NAMME;
        this.MODULE = MODULE;
    }


    public String getSAP_STO_NUMBER() {
        return SAP_STO_NUMBER;
    }

    public void setSAP_STO_NUMBER(String SAP_STO_NUMBER) {
        this.SAP_STO_NUMBER = SAP_STO_NUMBER;
    }

    public String getP_ORG() {
        return P_ORG;
    }

    public void setP_ORG(String p_ORG) {
        P_ORG = p_ORG;
    }

    public String getP_GRP() {
        return P_GRP;
    }

    public void setP_GRP(String p_GRP) {
        P_GRP = p_GRP;
    }

    public String getISS_SITE() {
        return ISS_SITE;
    }

    public void setISS_SITE(String ISS_SITE) {
        this.ISS_SITE = ISS_SITE;
    }

    public String getISS_STRG_LOC() {
        return ISS_STRG_LOC;
    }

    public void setISS_STRG_LOC(String ISS_STRG_LOC) {
        this.ISS_STRG_LOC = ISS_STRG_LOC;
    }

    public String getREC_SITE() {
        return REC_SITE;
    }

    public void setREC_SITE(String REC_SITE) {
        this.REC_SITE = REC_SITE;
    }

    public String getREC_STRG_LOC() {
        return REC_STRG_LOC;
    }

    public void setREC_STRG_LOC(String REC_STRG_LOC) {
        this.REC_STRG_LOC = REC_STRG_LOC;
    }

    public String getSTO_CR_DATE() {
        return STO_CR_DATE;
    }

    public void setSTO_CR_DATE(String STO_CR_DATE) {
        this.STO_CR_DATE = STO_CR_DATE;
    }

    public String getMAT_CODE() {
        return MAT_CODE;
    }

    public void setMAT_CODE(String MAT_CODE) {
        this.MAT_CODE = MAT_CODE;
    }

    public String getGTIN() {
        return GTIN;
    }

    public void setGTIN(String GTIN) {
        this.GTIN = GTIN;
    }

    public String getGTIN_Desc() {
        return GTIN_Desc;
    }

    public void setGTIN_Desc(String GTIN_Desc) {
        this.GTIN_Desc = GTIN_Desc;
    }

    public String getMEINH() {
        return MEINH;
    }

    public void setMEINH(String MEINH) {
        this.MEINH = MEINH;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getUSER_IDD() {
        return USER_IDD;
    }

    public void setUSER_IDD(String USER_IDD) {
        this.USER_IDD = USER_IDD;
    }

    public String getUSER_NAMME() {
        return USER_NAMME;
    }

    public void setUSER_NAMME(String USER_NAMME) {
        this.USER_NAMME = USER_NAMME;
    }

    public String getMODULE() {
        return MODULE;
    }

    public void setMODULE(String MODULE) {
        this.MODULE = MODULE;
    }
}
