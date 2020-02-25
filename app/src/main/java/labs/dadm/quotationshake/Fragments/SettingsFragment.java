package labs.dadm.quotationshake.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import labs.dadm.quotationshake.R;

public class SettingsFragment extends AppCompatActivity {

    public static final String USERNAME_KEY = "username",
            LANGUAGE_KEY = "settings_language",
            HTTP_METHOD_KEY = "settings_http_method",
            DATABASE_KEY = "settings_database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsFrameLayout, new PreferenceSettingsFragment())
                .commit();
    }

    public static class PreferenceSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        }
    }
}
