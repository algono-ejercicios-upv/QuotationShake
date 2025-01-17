package labs.dadm.quotationshake.Fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import labs.dadm.quotationshake.Databases.DatabaseProviders;
import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.R;
import labs.dadm.quotationshake.Tasks.QuotationWebServiceAsyncTask;

public class QuotationFragment extends AppCompatActivity {

    private static final String USERNAME_PLACEHOLDER = "%1s";

    private static final String
            QUOTATION_KEY = "current_quotation",
            ADD_OPTION_VISIBLE_KEY = "add_option_visible";

    private Quotation currentQuotation;
    private boolean addQuotationToFavouritesVisible = false;

    private QuotationWebServiceAsyncTask webServiceAsyncTask;
    private QuotationActivityState state = QuotationActivityState.not_fetched;

    public void fetchRandomQuotation() {
        if (isInternetAvailable()) {
            String languageCode = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(SettingsFragment.LANGUAGE_KEY, null);

            String requestMethod = PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .getString(SettingsFragment.HTTP_METHOD_KEY, null);

            webServiceAsyncTask = new QuotationWebServiceAsyncTask(this);

            if (requestMethod != null) {
                webServiceAsyncTask.setRequestMethod(
                        QuotationWebServiceAsyncTask.RequestMethod.valueOf(requestMethod));
            }

            webServiceAsyncTask.execute(languageCode);
        } else {
            Toast.makeText(
                    this,
                    R.string.internet_not_available,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quotation);

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
                .getString(SettingsFragment.USERNAME_KEY,
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

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addQuotationToFavouritesMenuItem:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseProviders
                                .getCurrentProvider(QuotationFragment.this)
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

    public void onEndFetchingRandomQuote(Quotation randomQuote) {
        currentQuotation = randomQuote;

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);

        findViewById(R.id.progressBarRandomQuote).setVisibility(View.INVISIBLE);

        if (currentQuotation == null) {
            state = QuotationActivityState.not_fetched;
            addQuotationToFavouritesVisible = false;
            supportInvalidateOptionsMenu();

            Toast.makeText(
                    this,
                    R.string.quotation_fetching_failed,
                    Toast.LENGTH_SHORT).show();
        } else {
            randomQuoteTextView.setText(randomQuote.getQuoteText());
            randomAuthorTextView.setText(
                    randomQuote.getQuoteAuthorOrDefault(this));

            state = QuotationActivityState.fetched;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addQuotationToFavouritesVisible = DatabaseProviders
                            .getCurrentProvider(QuotationFragment.this)
                            .getQuotationByText(currentQuotation.getQuoteText()) == null;
                    supportInvalidateOptionsMenu();
                }
            }).start();
        }

        webServiceAsyncTask = null;
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

    @Override
    protected void onStop() {
        super.onStop();
        if (webServiceAsyncTask != null
                && webServiceAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            webServiceAsyncTask.cancel(true);
        }
    }

    enum QuotationActivityState {not_fetched, fetching, fetched}
}
