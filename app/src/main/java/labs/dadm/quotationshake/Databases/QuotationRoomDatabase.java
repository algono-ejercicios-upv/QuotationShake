package labs.dadm.quotationshake.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import labs.dadm.quotationshake.Model.Quotation;

@Database(version = 1, entities = {Quotation.class})
public abstract class QuotationRoomDatabase extends RoomDatabase {
    private static final String databaseName = "quotation_database";

    private static QuotationRoomDatabase instance;

    public static synchronized QuotationRoomDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    QuotationRoomDatabase.class,
                    databaseName)
                    .allowMainThreadQueries() // TODO: Delete this after implementing threading
                    .build();
        }
        return instance;
    }

    public abstract QuotationRoomDAO getDAO();
}
