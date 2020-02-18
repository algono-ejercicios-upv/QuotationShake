package labs.dadm.quotationshake.Databases;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import labs.dadm.quotationshake.R;
import labs.dadm.quotationshake.SettingsActivity;

public class DatabaseProviders {
    public static synchronized DatabaseProvider getCurrentProvider(Context context) {
        String currentProvider = getCurrentProviderName(context);
        if (currentProvider == null) {
            // Default provider: Room
            return getProviderRoom(context);
        } else {
            return getProvider(context, currentProvider);
        }
    }

    @Nullable
    public static String getCurrentProviderName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SettingsActivity.DATABASE_KEY, null);
    }

    public static synchronized DatabaseProvider getProvider(Context context, String providerName) {
        final String roomDatabase = context.getString(R.string.settings_database_room);
        final String helperDatabase = context.getString(R.string.settings_database_helper);

        if (providerName.equals(roomDatabase)) {
            return getProviderRoom(context);
        } else if (providerName.equals(helperDatabase)) {
            return getProviderHelper(context);
        } else {
            throw new IllegalArgumentException("Invalid provider name");
        }
    }

    public static synchronized DatabaseProvider getProvider(Context context, boolean useRoom) {
        if (useRoom) {
            return getProviderRoom(context);
        } else {
            return getProviderHelper(context);
        }
    }

    private static synchronized DatabaseProvider getProviderRoom(Context context) {
        return QuotationRoomDatabase.getInstance(context).getDAO();
    }

    private static synchronized DatabaseProvider getProviderHelper(Context context) {
        return QuotationSQLiteOpenHelper.getInstance(context);
    }
}
