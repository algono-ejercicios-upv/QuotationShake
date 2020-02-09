package labs.dadm.quotationshake;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * <p>The reasoning behind using this is avoiding using a switch statement when navigating
     * between activities.</p>
     * <p></p>
     * <p>As the code executed is always the same (navigate to the activity whose class is 'x')
     * we map each button id with the activity it navigates to,
     * so we can access it through 'map.get(...)' more easily
     * (which is probably faster than the switch statement).</p>
     * <p></p>
     * <p>Note: {@link SparseArray}{@literal <T>} is equivalent to
     * {@link java.util.HashMap}{@literal <Integer, T>} (but more memory-efficient)</p>
     *
     * @see #createNavButtonIdMap()
     * @see #onNavigationButtonPressed(View)
     */
    public static final SparseArray<Class> navButtonIdMap = createNavButtonIdMap();

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

    /**
     * <p>The reasoning behind using {@link #navButtonIdMap} is avoiding using a switch statement
     * when navigating between activities.</p>
     * <p></p>
     * <p>For an extended explanation, check this: {@link #navButtonIdMap}.</p>
     * @param v The view associated with the button press, usually the button we pressed
     * @see #navButtonIdMap
     */
    public void onNavigationButtonPressed(View v) {
        Class nextActivityClass = navButtonIdMap.get(v.getId());
        Intent intent = new Intent(this, nextActivityClass);
        startActivity(intent);
    }
}
