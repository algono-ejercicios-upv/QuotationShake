package labs.dadm.quotationshake.Model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Quotation implements Serializable {
    private String quoteText;

    @Nullable
    private String quoteAuthor;

    public Quotation(String quoteText, @Nullable String quoteAuthor) {
        this.quoteText = quoteText;
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
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
