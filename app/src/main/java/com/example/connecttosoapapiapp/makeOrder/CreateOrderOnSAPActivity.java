package com.example.connecttosoapapiapp.makeOrder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.TransfereModule.Helper.DatabaseHelperForTransfer;
import com.example.connecttosoapapiapp.TransfereModule.modules.STo_Search;
import com.example.connecttosoapapiapp.helper.BluetoothHandler;
import com.zj.btsdk.BluetoothService;

import net.gotev.uploadservice.UploadService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class CreateOrderOnSAPActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>, EasyPermissions.PermissionCallbacks,
        BluetoothHandler.HandlerInterface {

    private DatePickerDialog.OnDateSetListener setListener;
    private EditText orderDate;
    private String EnvelopeBodyInConstant, EnvelopeBodyInCurrent, MESSAGE;
    private List<STo_Search> sto_SearchList;
    private List<Users> userList;
    private DatabaseHelperForTransfer databaseHelperForTransfer;
    private final int LOADER_ID = 4;
    private TextView response;
    private Button uploadOrder, print_btn;
    private BluetoothService mService;
    public static final int RC_BLUETOOTH = 0;
    public static final int RC_CONNECT_DEVICE = 1;
    public static final int RC_ENABLE_BLUETOOTH = 2;
    private boolean isPrinterReady = false;
    private String ordNumberForPrint;
    private String OrderDate;
    private ProgressBar progressBar;
    private String fromSite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_on_sapactivity);

        setupBluetooth();

        uploadOrder = findViewById(R.id.create_order_btn);
        response = findViewById(R.id.txt_response);
        orderDate = findViewById(R.id.make_order_date);
        print_btn = findViewById(R.id.print_order_number);
        progressBar = findViewById(R.id.progress_upload);
        orderDate.setInputType(0);
        orderDate.setFocusable(true);


        fromSite = getIntent().getExtras().getString("FromSite");

        databaseHelperForTransfer = new DatabaseHelperForTransfer(this);
        sto_SearchList = databaseHelperForTransfer.selectSto_Search_for_Qty();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        userList = databaseHelper.getUserData();


        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getDate();

        uploadOrder.setOnClickListener(v -> uploadList());

        print_btn.setOnClickListener(v -> {
            if (!isPrinterReady) {
                connectIfThereIsNoConnection();
            } else {
                printQRCode(ordNumberForPrint);
            }
        });

    }

    private void getDate() {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        orderDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateOrderOnSAPActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                    setListener, currentYear, currentMonth, currentDay);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();

        });


        setListener = (view, year, month, dayOfMonth) -> {
            month = month + 1;

            if (month < 10) {
                OrderDate = year + "-" + 0 + month + "-" + dayOfMonth;

            } else {
                OrderDate = year + "-" + month + "-" + dayOfMonth;
            }
            orderDate.setText(OrderDate);
        };

    }

    private void uploadList() {
        if (Constant.isOnline(CreateOrderOnSAPActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            uploadOrder.setEnabled(false);
            orderDate.setEnabled(false);
            getLoaderManager().initLoader(LOADER_ID, null, CreateOrderOnSAPActivity.this);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.textcheckinternet))
                    .setPositiveButton("موافق", (dialog, whichButton) -> dialog.cancel()).show();
        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader = new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Object loadInBackground() {
                SoapObject request = new SoapObject(Constant.NAMESPACE_For_MAKE_ORDER, Constant.MAKE_ORDER_API);
                MESSAGE = "";
                if (sto_SearchList.size() == 0) {
                    Toast.makeText(CreateOrderOnSAPActivity.this, "Po_Item_For_ftp_Upload.size() ==0", Toast.LENGTH_LONG).show();
                } else {
                    request.addProperty("CREATED_BY", userList.get(0).getUser_Name1().trim());
                    request.addProperty("CUSTOMER_ID", 14000000);

                    Vector v1 = new Vector();


                    for (int i = 0; i < sto_SearchList.size(); i++) {

                        SoapObject rate = new SoapObject(Constant.NAMESPACE_For_MAKE_ORDER, "item");

                        rate.addProperty("MATERIAL", sto_SearchList.get(i).getMAT_CODE1());
                        rate.addProperty("SALES_UNIT", sto_SearchList.get(i).getMEINH1());
                        rate.addProperty("REQ_DATE", orderDate.getText().toString());
                        rate.addProperty("REQ_QTY", Integer.valueOf(sto_SearchList.get(i).getQTY1()));

                        v1.add(i, rate);
                    }
                    request.addProperty("ITEMS", v1);
                }
                request.addProperty("SALES_ORG", userList.get(0).getCompany1());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport = new HttpTransportSE(Constant.URL_FOR_CREATE_ORDER);

                try {
                    httpTransport.call(Constant.SOAP_ACTION_For_Upload_ORDER, envelope);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                EnvelopeBodyInConstant = "Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);

                if (!EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
                    try {
                        SoapPrimitive GetReturn = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(2);
                        MESSAGE = GetReturn.toString();
                    } catch (Exception e) {
                        MESSAGE = e.getMessage();
                    }
                }

                return null;
            }
        };
        return asyncTaskLoader;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

        getLoaderManager().destroyLoader(LOADER_ID);
        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)) {
            response.setText("Your Data Is Wrong" + "\n" + "Items Error Order Not Created");
        } else {
            response.setText("Order Created with No : " + MESSAGE);
            ordNumberForPrint = MESSAGE;
            print_btn.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
        uploadOrder.setEnabled(false);
        orderDate.setEnabled(false);
        databaseHelperForTransfer.DeleteStoHeaderTable();
        databaseHelperForTransfer.DeleteStoSearchTable();
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }


    private void printQRCode(String orderNumber) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        String currentDateTime = sdf.format(new Date());
        String company = userList.get(0).getCompany1();

        switch (company) {
            case "H010":
                company = "Zayed";
                break;
            case "H020":
                company = "Asher";
                break;
            case "H030":
                company = "Sphinx";
                break;
            default:
                break;
        }


        String print = "^XA\n" +
                "^PW700\n" +
                "^LS0\n" +
                "^LH0,0\n" +
                "^CF0,40^FO10,75^FDHyperone^FS\n" +
                "^CF0,40^FO160,75^FD" + " - " + company + "^FS\n" +
                "^FO15,130^BQ,2,8^FD##" + orderNumber + "^FS\n" +
                "^CF0,25^FO210,140^FD" + "Order Date : " + currentDateTime + "^FS\n" +
                "^CFR,15^FO210,190^FDSales Order : " + orderNumber + "^FS\n" +
                "^FO210,240^FDDestribution Center^FS\n" +
                "^FO210,290^FDCreated By : " + userList.get(0).getUser_Name1() + " ^FS\n" +
                "^XZ";

        byte[] command = print.getBytes();
        mService.write(command);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @AfterPermissionGranted(RC_BLUETOOTH)
    private void setupBluetooth() {
        String[] params = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "You need bluetooth permission",
                    RC_BLUETOOTH, params);
            return;
        }
        mService = new BluetoothService(this, new BluetoothHandler(this));
    }


    @SuppressLint("MissingPermission")
    private void requestBluetooth() {
        if (mService != null) {
            if (!mService.isBTopen()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, RC_ENABLE_BLUETOOTH);
            }
        }
    }


    public void connectIfThereIsNoConnection() {
        if (mService.isBTopen()) {
            startActivityForResult(new Intent(this, DeviceActivity.class),
                    RC_CONNECT_DEVICE);
        } else
            requestBluetooth();
    }


    @Override
    public void onDeviceConnected() {
        isPrinterReady = true;

        if (!isPrinterReady) {
            connectIfThereIsNoConnection();
        } else {
            printQRCode(ordNumberForPrint);
        }
    }

    @Override
    public void onDeviceConnecting() {
        Toast.makeText(this, "محاولة الاتصال .....", Toast.LENGTH_LONG);
    }

    @Override
    public void onDeviceConnectionLost() {
        isPrinterReady = false;
        Toast.makeText(this, "من فضلك تأكد من تشغيل الطابعة", Toast.LENGTH_LONG);

        if (!isPrinterReady) {
            connectIfThereIsNoConnection();
        } else {
            printQRCode(ordNumberForPrint);
        }
    }

    @Override
    public void onDeviceUnableToConnect() {
        Toast.makeText(this, " لم يتم الاتصال .. تأكد من تشغيل الطابعة", Toast.LENGTH_SHORT).show();

        if (!isPrinterReady) {
            connectIfThereIsNoConnection();
        } else {
            printQRCode(ordNumberForPrint);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_ENABLE_BLUETOOTH:


                if (resultCode == RESULT_OK) {
                    connectIfThereIsNoConnection();
                }
                connectIfThereIsNoConnection();
                break;
            case RC_CONNECT_DEVICE:
                if (resultCode == RESULT_OK) {
                    String address = data.getExtras().getString(DeviceActivity.EXTRA_DEVICE_ADDRESS);

                    if (address.contains("device connected!") || address.contains("Backpressed") ||
                            address.contains("يوجد طابعة متصلة!")) {
                    } else {
                        BluetoothDevice mDevice = mService.getDevByMac(address);
                        mService.connect(mDevice);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateOrderOnSAPActivity.this, CreateOrderSearchActivity.class);
        intent.putExtra("Department", fromSite);
        intent.putExtra("FromSite", fromSite);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}