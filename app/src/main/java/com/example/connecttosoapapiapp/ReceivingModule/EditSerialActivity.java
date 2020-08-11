package com.example.connecttosoapapiapp.ReceivingModule;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.R;

public class EditSerialActivity extends AppCompatActivity {
    TextView txt_barcode,txt_serial,txt_descripation_for_serial_form;
    EditText edit_new_serial ;
    DatabaseHelper databaseHelper;
    String OldSerial,barcode,Shorttext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_serial);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_barcode =findViewById(R.id.txt_barcode_edit);
        txt_serial=findViewById(R.id.txt_old_serial);
        edit_new_serial=findViewById(R.id.edit_current_serials_form);
        txt_descripation_for_serial_form=findViewById(R.id.txt_descripation_for_serial_form);
        databaseHelper=new DatabaseHelper(this);

        Intent i=getIntent();
         OldSerial = i.getExtras().getString("Serial");
        barcode = i.getExtras().getString("Barcode");
        Shorttext = i.getExtras().getString("Shorttext");

        txt_serial.setText(OldSerial);
        txt_barcode.setText(barcode);
        txt_descripation_for_serial_form.setText(Shorttext);
    }

    public void UpdateSerial(View view) {
        if (!edit_new_serial.getText().toString().isEmpty()) {
            Log.d("po_itemlistsize;","FindOrNot"+edit_new_serial.getText().toString());
            String FindOrNot = databaseHelper.serach_for_Serial(edit_new_serial.getText().toString());
            Log.d("po_itemlistsize;","FindOrNot"+FindOrNot);

            if (FindOrNot.equalsIgnoreCase("true")) {
                databaseHelper = new DatabaseHelper(this);
                databaseHelper.update_Serial(OldSerial, edit_new_serial.getText().toString());
                txt_serial.setText(edit_new_serial.getText().toString());
                edit_new_serial.setText("");
                edit_new_serial.setHint("تم");
                Intent Go_Back= new Intent(EditSerialActivity.this,CheckItemFormActivity.class);
                startActivity(Go_Back);
            } else {
                edit_new_serial.setError("لقد أدخلت هذا السيريال من قبل");
                edit_new_serial.setText("");
                edit_new_serial.setHint(null);
            }
        }else {
            edit_new_serial.setError("أدخل السيريال");
        }
    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(EditSerialActivity.this,CheckItemFormActivity.class);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
