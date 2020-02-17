package labs.dadm.quotationshake;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import labs.dadm.quotationshake.Databases.DatabaseProviders;
import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.Tasks.QuotationWebServiceAsyncTask;

public class QuotationActivity extends AppCompatActivity {

    private static final String USERNAME_PLACEHOLDER = "%1s",
            QUOTATION_NUMBER_PLACEHOLDER = "%1$d";

    private static final String
            QUOTATION_KEY = "current_quotation",
            ADD_OPTION_VISIBLE_KEY = "add_option_visible";

    private Quotation currentQuotation;
    private boolean addQuotationToFavouritesVisible = false;
    private QuotationActivityState state = QuotationActivityState.not_fetched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        if (savedInstanceState == null) {
            showWelcomeMessage(randomQuoteTextView);
        } else {
            currentQuotation = (Quotation) savedInstanceState.getSerializable(QUOTATION_KEY);
            addQuotationToFavouritesVisible = savedInstanceState.getBoolean(ADD_OPTION_VISIBLE_KEY, false);

            if (currentQuotation == null) {
                showWelcomeMessage(randomQuoteTextView);
            } else {
                randomQuoteTextView.setText(currentQuotation.getQuoteText());

                String currentAuthor = currentQuotation
                        .getQuoteAuthorOrDefault(this);

                TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);
                randomAuthorTextView.setText(currentAuthor);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quotation_menu, menu);
        if (state == QuotationActivityState.fetching) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }
        } else {
            menu.findItem(R.id.addQuotationToFavouritesMenuItem)
                    .setVisible(addQuotationToFavouritesVisible);
        }
        return true;
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
        outState.putBoolean(ADD_OPTION_VISIBLE_KEY, addQuotationToFavouritesVisible);
    }

    public void fetchRandomQuotation() {
        new QuotationWebServiceAsyncTask(this).execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addQuotationToFavouritesMenuItem:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseProviders
                                .getCurrentProvider(QuotationActivity.this)
                                .addQuotation(currentQuotation);
                    }
                }).start();

                addQuotationToFavouritesVisible = false;
                supportInvalidateOptionsMenu();
                return true;
            case R.id.getNewQuotationMenuItem:
                fetchRandomQuotation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onStartFetchingRandomQuote() {
        state = QuotationActivityState.fetching;
        supportInvalidateOptionsMenu();

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);

        randomQuoteTextView.setText("");
        randomAuthorTextView.setText("");

        findViewById(R.id.progressBarRandomQuote).setVisibility(View.VISIBLE);
    }

    public void onEndFetchingRandomQuote(Quotation randomQuote) {
        currentQuotation = randomQuote;

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);

        findViewById(R.id.progressBarRandomQuote).setVisibility(View.INVISIBLE);

        randomQuoteTextView.setText(randomQuote.getQuoteText());
        randomAuthorTextView.setText(
                randomQuote.getQuoteAuthorOrDefault(this));

        state = QuotationActivityState.fetched;
        new Thread(new Runnable() {
            @Override
            public void run() {
                addQuotationToFavouritesVisible = DatabaseProviders
                        .getCurrentProvider(QuotationActivity.this)
                        .getQuotationByText(currentQuotation.getQuoteText()) == null;
                supportInvalidateOptionsMenu();
            }
        }).start();
    }

    enum QuotationActivityState {not_fetched, fetching, fetched}
}
