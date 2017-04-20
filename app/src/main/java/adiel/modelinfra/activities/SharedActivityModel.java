package adiel.modelinfra.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import adiel.modelinfra.R;

public class SharedActivityModel extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public final static String SP_VALUE = "spValue";

    EditText etSpValue;
    TextView tvSpValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_model);

        etSpValue = (EditText) findViewById(R.id.etSpValue);
        tvSpValue = (TextView) findViewById(R.id.tvSpValue);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void saveToSp(View view) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SP_VALUE,etSpValue.getText().toString());
        editor.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, "onSharedPreferenceChanged", Toast.LENGTH_SHORT).show();
        String spValue = sharedPreferences.getString(s, "defValue");
        tvSpValue.setText(spValue);

    }
}
