package labs.dadm.quotationshake;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import labs.dadm.quotationshake.Model.Quotation;

public class QuotationActivity extends AppCompatActivity {

    private static final String USERNAME_PLACEHOLDER = "%1s",
            QUOTATION_NUMBER_PLACEHOLDER = "%1$d";

    private static final String
            QUOTATION_KEY = "current_quotation",
            QUOTATION_NUMBER_KEY = "current_quotation_number",
            ADD_OPTION_VISIBLE_KEY = "add_option_visible";

    private int numberOfQuotationsReceived = 0;
    private Quotation currentQuotation;
    private boolean addQuotationToFavouritesVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        if (savedInstanceState == null) {
            showWelcomeMessage(randomQuoteTextView);
        } else {
            currentQuotation = (Quotation) savedInstanceState.getSerializable(QUOTATION_KEY);
            numberOfQuotationsReceived = savedInstanceState.getInt(QUOTATION_NUMBER_KEY, 0);
            addQuotationToFavouritesVisible = savedInstanceState.getBoolean(ADD_OPTION_VISIBLE_KEY, false);

            if (currentQuotation == null) {
                showWelcomeMessage(randomQuoteTextView);
            } else {
                randomQuoteTextView.setText(
                        replaceQuotationNumber(currentQuotation.getQuoteText()));

                String currentAuthor = currentQuotation
                        .getQuoteAuthorOrDefault(getString(R.string.author_anonymous));

                TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);
                randomAuthorTextView.setText(replaceQuotationNumber(currentAuthor));
            }
        }
    }

    private void showWelcomeMessage(TextView randomQuoteTextView) {
        String username = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.USERNAME_KEY,
                        getString(R.string.quotation_default_username));
        randomQuoteTextView.setText(
                getString(R.string.quotation_hello).replace(USERNAME_PLACEHOLDER,
                        username));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(QUOTATION_KEY, currentQuotation);
        outState.putInt(QUOTATION_NUMBER_KEY, numberOfQuotationsReceived);
        outState.putBoolean(ADD_OPTION_VISIBLE_KEY, addQuotationToFavouritesVisible);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quotation_menu, menu);
        menu.findItem(R.id.addQuotationToFavouritesMenuItem)
                .setVisible(addQuotationToFavouritesVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addQuotationToFavouritesMenuItem:
                // TODO: Add quotation to favourites
                addQuotationToFavouritesVisible = false;
                supportInvalidateOptionsMenu();
                return true;
            case R.id.getNewQuotationMenuItem:
                fetchRandomQuotation();
                addQuotationToFavouritesVisible = true;
                supportInvalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetchRandomQuotation() {
        onStartFetchingRandomQuote();

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);

        String nextQuote = getString(R.string.quotation_sample_quote);
        String nextAuthor = getString(R.string.quotation_sample_author);

        currentQuotation = new Quotation(nextQuote, nextAuthor);

        numberOfQuotationsReceived++;

        randomQuoteTextView.setText(replaceQuotationNumber(nextQuote));
        randomAuthorTextView.setText(replaceQuotationNumber(nextAuthor));

        onEndFetchingRandomQuote();
    }

    private String replaceQuotationNumber(String str) {
        return str.replace(QUOTATION_NUMBER_PLACEHOLDER,
                String.valueOf(numberOfQuotationsReceived));
    }

    private void onStartFetchingRandomQuote() {
        findViewById(R.id.progressBarRandomQuote).setVisibility(View.VISIBLE);
    }

    private void onEndFetchingRandomQuote() {
        findViewById(R.id.progressBarRandomQuote).setVisibility(View.INVISIBLE);
    }
}
