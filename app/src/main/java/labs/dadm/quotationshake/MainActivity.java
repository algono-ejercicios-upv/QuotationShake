package labs.dadm.quotationshake;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // SparseArray<T> is equivalent to HashMap<Integer, T> (but better)
    private static final SparseArray<Class> navButtonIdMap = createNavButtonIdMap();

    /**
     * Don't use this method directly. Call the private field 'navButtonIdMap' instead.
     * @return A SparseIntArray mapping the id of nav buttons with the activities it navigates to.
     */
    private static SparseArray<Class> createNavButtonIdMap() {
        SparseArray<Class> navButtonIdMap = new SparseArray<>();

        navButtonIdMap.append(R.id.button_goto_get, QuotationActivity.class);
        navButtonIdMap.append(R.id.button_goto_about, AboutActivity.class);
        navButtonIdMap.append(R.id.button_goto_favourite, FavouriteActivity.class);
        navButtonIdMap.append(R.id.button_goto_settings, SettingsActivity.class);

        return navButtonIdMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNavigationButtonPressed(View v) {
        Class nextActivityClass = navButtonIdMap.get(v.getId());
        Intent intent = new Intent(this, nextActivityClass);
        startActivity(intent);
    }
}
