package labs.dadm.quotationshake.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import labs.dadm.quotationshake.Model.Quotation;

import static labs.dadm.quotationshake.Databases.QuotationContract.QuotationBaseColumns;

public class QuotationSQLiteOpenHelper extends SQLiteOpenHelper implements DatabaseProvider {
    private static final String databaseName = "quotation_database";

    private static QuotationSQLiteOpenHelper instance;

    private QuotationSQLiteOpenHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    public static synchronized QuotationSQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuotationSQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                String.format("CREATE TABLE %s (" +
                                "%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "%s TEXT NOT NULL, " +
                                "%s TEXT, " +
                                "UNIQUE(%s)" +
                                ");",
                        QuotationBaseColumns.tableName,
                        QuotationBaseColumns._ID,
                        QuotationBaseColumns.columnName_quoteText,
                        QuotationBaseColumns.columnName_authorName,
                        QuotationBaseColumns.columnName_quoteText));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Empty on purpose
    }

    public List<Quotation> getAllQuotations() {
        ArrayList<Quotation> quotationList = new ArrayList<>();
        try (SQLiteDatabase database = getReadableDatabase()) {
            try (Cursor quotationCursor = database.query(QuotationBaseColumns.tableName,
                    new String[]{QuotationBaseColumns.columnName_quoteText,
                            QuotationBaseColumns.columnName_authorName},
                    null, null, null, null, null)) {
                while (quotationCursor.moveToNext()) {
                    String quoteText = quotationCursor.getString(0);
                    String quoteAuthor = quotationCursor.getString(1);
                    quotationList.add(new Quotation(quoteText, quoteAuthor));
                }
            }
        }
        return quotationList;
    }

    public Quotation getQuotationByText(String quoteText) {
        try (SQLiteDatabase database = getReadableDatabase()) {
            try (Cursor quotationCursor = database.query(QuotationBaseColumns.tableName,
                    new String[]{QuotationBaseColumns.columnName_quoteText,
                            QuotationBaseColumns.columnName_authorName},
                    String.format("%s=?", QuotationBaseColumns.columnName_quoteText),
                    new String[]{quoteText},
                    null, null, null)) {
                quotationCursor.moveToFirst();
                String databaseQuoteText = quotationCursor.getString(0);
                String quoteAuthor = quotationCursor.getString(1);
                return new Quotation(databaseQuoteText, quoteAuthor);
            }
        }
    }

    public boolean quotationExists(Quotation quotation) {
        try (SQLiteDatabase database = getReadableDatabase()) {
            try (Cursor cursor = database.query(QuotationBaseColumns.tableName,
                    null,
                    String.format("%s=?", QuotationBaseColumns.columnName_quoteText),
                    new String[]{quotation.getQuoteText()},
                    null, null, null)) {
                return cursor.getCount() > 0;
            }
        }
    }

    public void addQuotation(Quotation quotation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuotationBaseColumns.columnName_quoteText, quotation.getQuoteText());
        contentValues.put(QuotationBaseColumns.columnName_authorName, quotation.getQuoteAuthor());

        try (SQLiteDatabase database = getWritableDatabase()) {
            database.insert(QuotationBaseColumns.tableName, null, contentValues);
        }
    }

    public void removeQuotation(Quotation quotation) {
        try (SQLiteDatabase database = getWritableDatabase()) {
            database.delete(QuotationBaseColumns.tableName,
                    String.format("%s=?", QuotationBaseColumns.columnName_quoteText),
                    new String[]{quotation.getQuoteText()});
        }
    }

    public void clearQuotations() {
        try (SQLiteDatabase database = getWritableDatabase()) {
            database.delete(QuotationBaseColumns.tableName, null, null);
        }
    }
}
