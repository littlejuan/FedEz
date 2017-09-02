package org.littlejuan.fedez;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.littlejuan.fedez.database.FedEzContract;
import org.littlejuan.fedez.database.FedEzOpenHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DatabaseActivity extends AppCompatActivity {

    private Spinner mUbicationSpinner, mCountrySpinner;

    private EditText mCostoEditText, mIdEditText, mNombreEditText, mPesoEditText, mPaisEditText;

    private String mUbication = "";

    private String mPais = "";

    private double mCobroValue = 0;

    private FedEzOpenHelper mDbHelper;

    private int ubication = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        mCostoEditText = (EditText) findViewById(R.id.edit_costo);
        mIdEditText = (EditText) findViewById(R.id.edit_id);
        mNombreEditText = (EditText) findViewById(R.id.edit_nombre);
        mPesoEditText = (EditText) findViewById(R.id.edit_peso);
        mPaisEditText = (EditText) findViewById(R.id.edit_pais);

        mUbicationSpinner = (Spinner) findViewById(R.id.spinner_ubication);
        setupSpinner();
        String selection = getIntent().getStringExtra("M_UBICATION_TEXT");
        mUbicationSpinner.setSelection(getIndex(mUbicationSpinner, selection));
        String value = getIntent().getStringExtra("M_COBRO");
        if (!value.isEmpty()){
            mCobroValue = Double.parseDouble(value);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('\'');
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("$ #,###.00", symbols);
            mCostoEditText.setText(decimalFormat.format(mCobroValue));
        }
        mPesoEditText.setText(getIntent().getStringExtra("M_PESO"));
        mDbHelper = FedEzOpenHelper.getInstance(this);
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
                mUbication = getString(R.string.america_n);
            }
        });
    }

    public void ubicationSelected(String ubication){
        if (ubication.equals(getString(R.string.america_n))) {
            mUbication = getString(R.string.america_n);
        } else if (ubication.equals(getString(R.string.america_c))) {
            mUbication = getString(R.string.america_c);
        } else if  (ubication.equals(getString(R.string.america_s))){
            mUbication = getString(R.string.america_s);
        } else if  (ubication.equals(getString(R.string.europa))){
            mUbication = getString(R.string.europa);
        } else if  (ubication.equals(getString(R.string.asia))){
            mUbication = getString(R.string.asia);
        }
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }



    public void onInsertar(View view){
        ContentValues contentValues = new ContentValues();

        String id = mIdEditText.getEditableText().toString();
        String nombre = mNombreEditText.getEditableText().toString();
        String peso = mPesoEditText.getEditableText().toString();
        String continente = mUbication;
        String pais = mPais;
        String costo = String.valueOf(mCobroValue);

        contentValues.put(FedEzContract.FedEzEntry.COLUMN_ID, id);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_NOMBRE, nombre);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_PESO, Double.parseDouble(peso));
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_CONTINENTE, continente);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_PAIS, pais);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_COSTO, Double.parseDouble(costo));

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long inserted =db.insert(FedEzContract.FedEzEntry.TABLE_NAME, null, contentValues);
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Usuario guardado: " + inserted, Toast.LENGTH_SHORT);

        toast1.show();
        db.close();
        clean();
    }

    public void onAlterar(View view){
        ContentValues contentValues = new ContentValues();

        String id = mIdEditText.getEditableText().toString();
        String nombre = mNombreEditText.getEditableText().toString();
        String peso = mPesoEditText.getEditableText().toString();
        String continente = mUbication;
        String pais = mPaisEditText.getEditableText().toString();
        String costo = String.valueOf(mCobroValue);

        contentValues.put(FedEzContract.FedEzEntry.COLUMN_NOMBRE, nombre);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_PESO, Double.parseDouble(peso));
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_CONTINENTE, continente);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_PAIS, pais);
        contentValues.put(FedEzContract.FedEzEntry.COLUMN_COSTO, Double.parseDouble(costo));

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long altered =
                db.update(FedEzContract.FedEzEntry.TABLE_NAME,
                        contentValues, "_id = ?", new String[] {id} );
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Usuario modificado: " + altered, Toast.LENGTH_SHORT);

        toast1.show();
        db.close();
        clean();
    }

    public void onEliminar(View view){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String id = mIdEditText.getEditableText().toString();
        long deleted = db.delete(FedEzContract.FedEzEntry.TABLE_NAME, "_id = ?", new String[] {id});
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Usuario eliminado: " + deleted, Toast.LENGTH_SHORT);

        toast1.show();
        clean();
    }

    public void onBuscar(View view){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String id = mIdEditText.getEditableText().toString();

        Cursor result = db.rawQuery("SELECT * " +
                        "FROM " + FedEzContract.FedEzEntry.TABLE_NAME +
                        " WHERE " + FedEzContract.FedEzEntry.COLUMN_ID + " = ?",
                new String[] {id});

        if (result.moveToFirst()){
            mNombreEditText.setText(result.getString(1));
            mPesoEditText.setText(String.valueOf(result.getDouble(2)));
            mUbicationSpinner.setSelection(getIndex(mUbicationSpinner, result.getString(3)));
            mPaisEditText.setText(String.valueOf(result.getDouble(3)));
            mCobroValue = result.getDouble(5);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('\'');
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("$ #,###.00", symbols);
            mCostoEditText.setText(decimalFormat.format(mCobroValue));

            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    public void clean(){
        mIdEditText.setText("");
        mNombreEditText.setText("");
        mPaisEditText.setText("");
        mPesoEditText.setText("");
        mCostoEditText.setText("");
        mUbicationSpinner.setSelection(getIndex(mUbicationSpinner, getString(R.string.america_n)));
    }

}
