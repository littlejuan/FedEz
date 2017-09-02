package org.littlejuan.fedez;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CalculatorActivity extends AppCompatActivity {

    private Spinner mUbicationSpinner;
    private EditText mPesoEditText, mCobroEditText;

    private double mUbication = 0;
    private String mUbicationText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        mPesoEditText = (EditText) findViewById(R.id.edit_peso);
        mCobroEditText = (EditText) findViewById(R.id.edit_cobro);


        mUbicationSpinner = (Spinner) findViewById(R.id.spinner_ubication_flete);
        setupSpinner();

    }

    private void setupSpinner() {
        ArrayAdapter ubicationSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_ubication_options, android.R.layout.simple_spinner_item);

        ubicationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mUbicationSpinner.setAdapter(ubicationSpinnerAdapter);

        mUbicationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                ubicationSelected(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUbication = 0; // Unknown
            }
        });
    }


    public void ubicationSelected(String ubication){
        if (ubication.equals(getString(R.string.america_n))) {
            mUbication = 3800;
            mUbicationText = getString(R.string.america_n);
        } else if (ubication.equals(getString(R.string.america_c))) {
            mUbication = 3100;
            mUbicationText = getString(R.string.america_c);
        } else if  (ubication.equals(getString(R.string.america_s))){
            mUbication = 2900;
            mUbicationText = getString(R.string.america_s);
        } else if  (ubication.equals(getString(R.string.europa))){
            mUbication = 4200;
            mUbicationText = getString(R.string.europa);
        } else if  (ubication.equals(getString(R.string.asia))){
            mUbication = 5300;
            mUbicationText = getString(R.string.asia);
        }
    }

    public void onCalcular(View view){
        double kilogramo = Double.parseDouble(mPesoEditText.getEditableText().toString());
        if (kilogramo < 5) {
            double gramo =  kilogramo * 1000;
            double cobro = gramo * mUbication;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('\'');
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("$ #,###.00", symbols);
            mCobroEditText.setText(decimalFormat.format(cobro));
            Intent intent = new Intent();
            intent.putExtra("mCobro", String.valueOf(cobro));
            intent.putExtra("mUbicationText", mUbicationText);
            intent.putExtra("mPesoText", mPesoEditText.getText().toString());
            setResult(RESULT_OK, intent);
        }else {
            alerta("El peso del paquete excede el límite permitido.");
        }

    }

    public void alerta(String msg){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setCancelable(true).setTitle("Error:");
        dialogBuilder.create().show();
    }
}
