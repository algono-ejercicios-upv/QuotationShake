package labs.dadm.quotationshake;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        EditText nameEditText = findViewById(R.id.settings_name_editText);
        String username = sharedPreferences.getString(USERNAME_KEY, "");
        nameEditText.setText(username);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        EditText nameEditText = findViewById(R.id.settings_name_editText);
        String name = nameEditText.getText().toString();

        if (name.isEmpty()) editor.remove(USERNAME_KEY);
        else editor.putString(USERNAME_KEY, name);
        editor.apply();
    }
}
