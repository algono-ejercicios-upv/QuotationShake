package labs.dadm.quotationshake.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "quotation_table")
public class Quotation implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;

    @ColumnInfo(name = "quote")
    @NonNull
    private String quoteText;

    @ColumnInfo(name = "author")
    @Nullable
    private String quoteAuthor;

    public Quotation() {
    }

    public Quotation(@NonNull String quoteText, @Nullable String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(@NonNull String quoteText) {
        this.quoteText = quoteText;
    }

    @Nullable
    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(@Nullable String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteAuthorOrDefault(String defaultValue) {
        return isAuthorAnonymous() ? defaultValue : quoteAuthor;
    }

    public boolean isAuthorAnonymous() {
        return quoteAuthor == null || quoteAuthor.isEmpty();
    }
}
