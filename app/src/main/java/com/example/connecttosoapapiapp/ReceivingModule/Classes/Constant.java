package com.example.connecttosoapapiapp.ReceivingModule.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {
    public static final int Constant_DATABASE_VERSION = 4;
    //String URL = "http://<>:8000/sap/bc/srt/rfc/sap/z_sample_webservice_add_number/750/z_sample_webservice_add_number/z_sample_webservice_add_number?sap-client=750";
    //public final static String URL = "http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po?wsdl&sap-client=200&sap-user=test&sap-password=123456789";
    //public final static String URL = "http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po?&sap-client=200";
    //    public final static String METHOD = "ZwsPppcPo";
//public final static String METHOD = "ZWS_PPC_PO";
    //You can see it in WSDL File too, Just search string “namespace”.
    //public final  static String NAMESPACE = "urn:sap-com:document:sap:soap:functions:mc-style";
    //String METHOD = "ZSampleWebserviceAddNumber";
    //It’s a Service name you can see it in WSDL file.

    // For get detials of purchase order  For Recieving module
//    public final static String URL_For_Get_Detials = "http://10.10.5.11:8001/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po_getdetail/zws_ppc_po_getdetail";
//    public final static String METHOD_For_Get_Detials = "ZPPC_PO_GETDETAIL";
//    public final static String NAMESPACE_For_Get_Detials = "urn:sap-com:document:sap:rfc:functions";
//    public final static String SOAP_ACTION_For_Get_Detials= "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_PO_GETDETAIL:ZPPC_PO_GETDETAILRequest";

//for pro For Recieving module
    public final static String URL_For_Get_Detials = "http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/300/zws_ppc_po_getdetail/zbn_ppc_po_getdetail";
    public final static String METHOD_For_Get_Detials = "ZPPC_PO_GETDETAIL";
    public final static String NAMESPACE_For_Get_Detials = "urn:sap-com:document:sap:rfc:functions";
    public final static String SOAP_ACTION_For_Get_Detials= "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_PO_GETDETAIL:ZPPC_PO_GETDETAILRequest";

//1000226744
    // For upload of purchase order to qas For Recieving module
//    public final static String URL_For_Upload = "http://10.10.5.11:8001/sap/bc/srt/rfc/sap/zws_ppc_goods_receipt/200/zws_ppc_goods_receipt/zws_ppc_goods_receipt";
//    public final static String METHOD_For_Upload = "ZPPC_GOODS_RECEIPT";
//    public final static String NAMESPACE_For_Upload = "urn:sap-com:document:sap:rfc:functions";
//    public final static String SOAP_ACTION_For_Upload = "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_GOODS_RECEIPT:ZPPC_GOODS_RECEIPTRequest";

    //for prd For Recieving module
    public final static String URL_For_Upload = "http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_goods_receipt/300/zsn_ppc_goods_receipt/zbn_ppc_goods_receipt";
    public final static String METHOD_For_Upload = "ZPPC_GOODS_RECEIPT";
    public final static String NAMESPACE_For_Upload = "urn:sap-com:document:sap:rfc:functions";
    public final static String SOAP_ACTION_For_Upload = "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_GOODS_RECEIPT:ZPPC_GOODS_RECEIPTRequest";

    //for PRD For Recieving module Get_Document num
    public final static String URL_For_Get_Document = "http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zppc_goods_receipt_mu_ws/300/zppc_goods_receipt_mu_ws/zppc_goods_receipt_mu_ws";
    public final static String METHOD_For_Get_Document = "ZPPC_GOODS_RECEIPT_MU";
    public final static String NAMESPACE_For_Get_Document = "urn:sap-com:document:sap:rfc:functions";
    public final static String SOAP_ACTION_For_Get_Document = "urn:sap-com:document:sap:rfc:functions:ZPPC_GOODS_RECEIPT_MU_WS:ZPPC_GOODS_RECEIPT_MURequest";


    //String Output;


    //private HttpTransportSE androidHttpTransport;
    //private SoapSerializationEnvelope envelope;
    //View view;

    //http://schemas.xmlsoap.org/wsdl/
    //Z_CUSTOMER_LOOKUP1
    //final String METHOD_NAME = "ZPPC_PO_GETDETAIL";  /// from dot net MF



    //  NAMESPACE+METHOD_NAME =====>>>> SOAP_ACTION        not confirmed
    ///urn:sap-com:document:sap:soap:functions:mc-style
    /////////////////////////////////////:mc-style
    //final String NAMESPACE = "urn:sap-com:document:sap:rfc:functions"; // from dot net MF

    //http://********************:8000/sap/bc/srt/rfc/sap/z_customer_lookup1/800/z_customer_lookup1/z_customer_lookup1_bind/Z_CUSTOMER_LOOKUP1

    //urn:sap-com:document:sap:rfc:functions:ZWS_PPC_PO_GETDETAIL:ZPPC_PO_GETDETAILRequest     from sap xml
    //final String SOAP_ACTION = NAMESPACE + METHOD_NAME;

    //http://******************:8000/sap/bc/srt/wsdl/srvc_14DAE9C8D79F1EE196F1FC6C6518A345/wsdl11/allinone/ws_policy/document?sap-client=800&sap-user=test&sap-password=123456789
    //?sap-client=200&sap-user=test&sap-password=123456789         add them as property
    //http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po?sap-client=200&sap-user=test&sap-password=123456789
    //http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po?wsdl&sap-client=200&sap-user=test&sap-password=123456789
    //final String URL = "http://ERP-QAS.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po";
    //final String URL = "http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po";

    //http://10.1.1.155:8000/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_po_getdetail/200/zws_ppc_po/zws_ppc_po?wsdl


    //constant to download apk
     //   public final static String ApksURL_ًWithoutName ="http://10.128.6.160:82/PhpQB/";
//    public final static String ApksURL ="http://192.168.1.50:8080/PPCAPK/app-debugV2.apk";
    //TODO Allways make sure that xampp apache is starting
    public final static String ApksURL_ًWithoutName ="http://10.2.1.220:82/PPCAPK/";
//    public final static String ApksURL ="https://translationapp.000webhostapp.com/ChatServerFiles/UploadFromPPc/app-debugV2.apk";

    public final static String GetVersionURL = "http://10.2.1.220:8080/PPCModules/GetVersionPPC.php";

    /// constant to connect to server on login activity
    public final static String LoginURL = "http://10.2.1.220:8080/PPCModules/LoginPPCtest.php";
    public final static String RecievingAuthorizationURL = "http://10.2.1.220:8080/PPCModules/RecievingAuthorization.php";

    public static final String WriteInLogOf_sapTableURL = "http://10.128.6.160:81/PhpProject1/writeinLogs_saptable.php";
    public static final String WriteInLogs_sap_ITEMStableURL = "http://10.128.6.160:81/PhpProject1/writeinLogs_sap_ITEMStable.php";

    public static final String UploadToCSVFtp_recieving = "http://10.2.1.220:8080/PPCModules/UploadCSVFile_recieving.php";

    public static final String UploadToCSVFtp_cyclecount = "http://10.2.1.220:8080/PPCModules/UploadCSVFile_cyclecount.php";

    //public static final String UploadCSVFile_ScanBarcode = "http://10.2.1.220:8080/PPCModules/UploadCSVFile_ScanBarcode.php";

    public static final String UploadCSVFile_ScanBarcode = "http://10.2.1.220:8080/PPCModules/UploadCSVFile_ScanBarcode.php";

    public static final String UploadCSVFile_ItemAvailabilitye = "http://10.2.1.220:8080/PPCModules/UploadCSVFile_ItemAvailability.php";


    public static final String UploadToCSVFtpForTransfer = "http://10.2.1.220:8080/PPCModules/UploadCSVFileForTransfer.php";
//    public static final String UploadToCSVFtpForTransfer = "http://10.128.6.160:81/PhpProject1/UploadCSVFileForTransfer.php";

    //public static final String UploadToCSVFtp = "https://translationapp.000webhostapp.com/ChatServerFiles/UploadCSVFile.php";
    //public final static String LoginURL ="http://http://12.34.56.78:81/PhpProject1/MainForm.php";

//TODO change SelectThreeTablesforlist_V1 to SelectThreeTablesforlist
    //for transfer  $$ itemavalability  $$ return item
    public final static String ListfortransferFromSqlServerURL ="http://10.2.1.220:8080/PPCModules/SelectThreeTablesforlist_V1.php";
    public final static String ListfortransferFromSqlServerURL2 ="http://10.2.1.220:8080/PPCModules/SelectThreeTablesforlist_V2.php";
    public final static String Listforcompanies ="http://10.2.1.220:8080/PPCModules/LoginPPCCopy.php";

    //For GI
    public final static String ListforGIFromSqlServerURL ="http://10.2.1.220:8080/PPCModules/SelectTablesforlistforGI.php";

    //for transfer and search in GI
    public static String NAMESPACE_For_Search_Barcode="urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Search_Barcode="ZBAPI_PPC_SEARCH_SALES";
    public static String URL_For_Search_Barcode="http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_search_sales/300/zws_ppc_search_sales_sn/zws_ppc_search_sales_bn";
    public static String SOAP_ACTION_For_Search_Barcode="urn:sap-com:document:sap:rfc:functions:ZWS_PPC_SEARCH_SALES:ZBAPI_PPC_SEARCH_SALESRequest";

    public static String NAMESPACE_For_print="urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_print="ZFA_MM_GR2";
    public static String URL_For_print="http://prd-erp-pri.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_mm_gr2/300/zws_mm_gr2/zws_mm_gr2";
    public static String SOAP_ACTION_For_print="urn:sap-com:document:sap:rfc:functions:ZWS_MM_GR2:ZWS_MM_GR2Request";



    public static String NAMESPACE_For_Upload_transfere ="urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Upload_transfere="ZBAPI_PPC_CREATE_STO";
   // http://QAS-ERP:8001/sap/bc/srt/wsdl/flv_10002A111AD1/bndg_url/sap/bc/srt/rfc/sap/zws_ppc_create_sto/200/zws_ppc_create_sto/zws_ppc_create_sto
   // public static String URL_For_Upload_transfere="http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_sto/300/zws_ppc_create_sto_sn/zws_ppc_create_sto_bn";
    public static String URL_For_Upload_transfere="http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_sto/300/zws_ppc_create_sto_sn/zws_ppc_create_sto_bn";
    public static String SOAP_ACTION_For_Upload_transfere="urn:sap-com:document:sap:rfc:functions:ZWS_PPC_CREATE_STO:ZBAPI_PPC_CREATE_STORequest";


    ////*******************    Cycle Count   ******************////////////////////

//for qas
//    public static String NAMESPACE_For_Check_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
//    public static String METHOD_For_Check_Cycle_Count="ZPPC_CYCLE_COUNT_HEADER";
//    public static String URL_For_Check_Cycle_Count="http://ERP-QAS.hyperone.com:8000/sap/bc/srt/rfc/sap/zppc_cycle_count_header/200/zppc_cycle_count_header/zppc_cycle_count_header";
//    public static String SOAP_ACTION_For_Check_Cycle_Count="urn:sap-com:document:sap:rfc:functions:ZPPC_CYCLE_COUNT_HEADER:ZPPC_CYCLE_COUNT_HEADERRequest";

    //for dev
    public static String NAMESPACE_For_Check_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Check_Cycle_Count="ZPPC_CYCLE_COUNT_HEAD";
    public static String URL_For_Check_Cycle_Count="http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zppc_cycle_count_head_ws/300/zppc_cycle_count_head_ws/zppc_cycle_count_head_ws";
    public static String SOAP_ACTION_For_Check_Cycle_Count="urn:sap-com:document:sap:rfc:functions:ZPPC_CYCLE_COUNT_HEAD_WS:ZPPC_CYCLE_COUNT_HEADRequest";

//for QAS
//    public static String NAMESPACE_For_Get_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
//    public static String METHOD_For_Get_Cycle_Count="ZPPC_CYCLE_COUNT";
//    public static String URL_For_Get_Cycle_Count="http://ERP-QAS.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_cycle_count/200/zws_ppc_cycle_count/zws_ppc_cycle_count";
//    public static String SOAP_ACTION_For_Get_Cycle_Count="urn:sap-com:document:sap:rfc:functions:Zws_PPC_CYCLE_COUNT:ZPPC_CYCLE_COUNTRequest";

// for  Dev
//    public static String NAMESPACE_For_Get_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
//    public static String METHOD_For_Get_Cycle_Count="ZPPC_CYCLE_COUNT";
//    public static String URL_For_Get_Cycle_Count="http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_cycle_count/300/zws_ppc_cycle_count_sn/zws_ppc_cycle_count_bn";
//    public static String SOAP_ACTION_For_Get_Cycle_Count="urn:sap-com:document:sap:rfc:functions:Zws_PPC_CYCLE_COUNT:ZPPC_CYCLE_COUNTRequest";

    public static String NAMESPACE_For_Get_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Get_Cycle_Count="ZPPC_CYCLE_COUNT2";
    public static String URL_For_Get_Cycle_Count="http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zppc_cycle_count2_ws/300/zppc_cycle_count2_ws/zppc_cycle_count2_ws";
    public static String SOAP_ACTION_For_Get_Cycle_Count="urn:sap-com:document:sap:rfc:functions:ZPPC_CYCLE_COUNT2_WS:ZPPC_CYCLE_COUNT2Request";


    //password for uploading
    public static final String Validation_password_URL = "http://10.2.1.220:8080/PPCModules/CyclecountUpload/Login_For_UploadCycleCount.php";


    //write in log tables
    public static final String WriteInLogOf_sapTableofcyclecountURL = "http://10.2.1.220:8080/PPCModules/CyclecountUpload/writeinLogs_saptable.php";
    public static final String WriteInLogs_sap_ITEMStableofcyclecountURL = "http://10.2.1.220:8080/PPCModules/CyclecountUpload/writeinLogs_sap_ITEMStable.php";

    public static final String WriteInLogOf_sapTableofundefined = "http://10.2.1.220:8080/PPCModules/writeinLogs_sap_ofundefined.php";

    public static final String CheckForEcomerceTable = "http://10.2.1.220:8080/PPCModules/CheckEcomerceBarcodes.php";

    public static final String Details_for_Update = "http://10.2.1.220:8080/PPCModules/Details.php";
    //for qas
//    public static String NAMESPACE_For_Upload_Cycle_Count ="urn:sap-com:document:sap:rfc:functions";
//    public static String METHOD_For_Upload_Cycle_Count="ZPPC_CREATE_COUNT";
//    public static String URL_For_Upload_Cycle_Count="http://ERP-QAS.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_count/200/zws_ppc_create_count/zws_ppc_create_count";
//    public static String SOAP_ACTION_For_Upload_Cycle_Count="urn:sap-com:document:sap:rfc:functions:Zws_PPC_CREATE_COUNT:ZPPC_CREATE_COUNTRequest";

    //for Dev
    public static String NAMESPACE_For_Upload_Cycle_Count = "urn:sap-com:document:sap:rfc:functions";
    // for promotion module
    public final static String Listforpgrp_descriptionFromSqlServerURL = "http://10.2.1.220:8080/PPCModules/SelectPGRP_PORG.php";
    public final static String ListfornotsFromSqlServerURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/GetNots.php";
    public final static String GetUserNameFromSqlServerURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/GetUserName.php";
    //    public static String GetTodayPromotionURL="http://10.2.1.220:8080/PPCModules/PromotionModule/SP_PROM_GetPromotions_Active_issue.php";
    public final static String UpdatepromotionafterReviewonSqlServerURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/UpdatepromotionAfterReview.php";
    public static String METHOD_For_Upload_Cycle_Count = "ZPPC_CREATE_COUNT";
    public static String URL_For_Upload_Cycle_Count = "http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_count/300/zws_ppc_createe_count_sn/zws_ppc_createe_count_bn";
    public static String SOAP_ACTION_For_Upload_Cycle_Count = "urn:sap-com:document:sap:rfc:functions:Zws_PPC_CREATE_COUNT:ZPPC_CREATE_COUNTRequest";
    //////////////////////////////////////////////////////GI
    public static String NAMESPACE_For_Upload_GI = "urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Upload_GI = "Z_PPC_GOODS_ISSUE_CC_EH";
    public static String URL_For_Upload_GI = "http://ERP-QAS.hyperone.com:8000/sap/bc/srt/rfc/sap/zppc_gi/200/zppc_gi_cc/zppc_gi_cc";
    public static String SOAP_ACTION_For_Upload_GI = "urn:sap-com:document:sap:rfc:functions:zppc_GI:Z_PPC_GOODS_ISSUE_CC_EHRequest";
    /*****************************************////// for search Item Return
    public static String NAMESPACE_For_Search_Barcode_returnitem = "urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_Search_Barcode_returnitem = "ZBAPI_PPC_SEARCH_PURCH";
    public static String URL_For_Search_Barcode_returnitem = "http://PRD-ERP-PRI.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_search_purch/300/zws_ppc_search_purch_sn/zws_ppc_search_purch_bn";
    public static String SOAP_ACTION_For_Search_Barcode_returnitem = "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_SEARCH_PURCH:ZBAPI_PPC_SEARCH_PURCHRequest";
    //*//////////////////////  for create item return
    public static String NAMESPACE_For_create_returnitem = "urn:sap-com:document:sap:rfc:functions";
    public static String METHOD_For_create_returnitem = "ZBAPI_PPC_CREATE_RETURN_PO";
    //http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_return_po/300/zws_ppc_create_return_po_sn/zws_ppc_create_return_po_bn
    public static String URL_For_create_returnitem = "http://PRD-ERP-DIA.hyperone.com:8000/sap/bc/srt/rfc/sap/zws_ppc_create_return_po/300/zws_ppc_create_return_po_sn/zws_ppc_create_return_po_bn";
    public static String SOAP_ACTION_For_create_returnitem = "urn:sap-com:document:sap:rfc:functions:ZWS_PPC_CREATE_RETURN_PO:ZBAPI_PPC_CREATE_RETURN_PORequest";
    // for item availability module
    public static String GetDetialsURL = "http://10.2.1.220:8080/PPCModules/SelectDataForBarcodeForViewItems.php";
    // for searching in item availability
    public static String GetDetialsforsearchingURL = "http://10.2.1.220:8080/PPCModules/SelectDataForSearchBarcodeInitemAvaliability.php";
    public static String GetExpiredPromotionURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/SP_PROM_GetPromotions_Expired.php";
    public static String GetStopedPromotionURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/SP_PROM_GetPromotions_Stopped.php";
    public static String GetTodayPromotionURL = "http://10.2.1.220:8080/PPCModules/PromotionModule/SP_PROM_GetPromotions_Active.php";


    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }
}
