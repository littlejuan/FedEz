package org.littlejuan.fedez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int FLETE_REQUEST = 1;

    private String mCobro, mPeso;
    private String mUbicationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCobro = "";
        mPeso = "";
        mUbicationText = getString(R.string.america_n);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        selectedItem(id);
        return super.onOptionsItemSelected(item);
    }

    public void selectedItem(int id){
        switch (id){
            case R.id.database_id:
                Intent dbIntent = new Intent(this, DatabaseActivity.class);
                dbIntent.putExtra("M_COBRO", mCobro);
                dbIntent.putExtra("M_UBICATION_TEXT", mUbicationText);
                dbIntent.putExtra("M_PESO", mPeso);
                startActivity(dbIntent);
                mCobro = "";
                mPeso = "";
                mUbicationText = getString(R.string.america_n);
                break;
            case R.id.calculator_id:
                Intent calculatorIntent = new Intent(this, CalculatorActivity.class);
                startActivityForResult(calculatorIntent, FLETE_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                mCobro = data.getStringExtra("mCobro");
                mUbicationText = data.getStringExtra("mUbicationText");
                mPeso = data.getStringExtra("mPesoText");
            }
        }
    }
}
