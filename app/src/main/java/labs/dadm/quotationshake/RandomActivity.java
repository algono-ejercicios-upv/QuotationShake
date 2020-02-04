package labs.dadm.quotationshake;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RandomActivity extends AppCompatActivity {

    private static final String USERNAME_PLACEHOLDER = "%1s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        TextView textView = findViewById(R.id.textViewRandomQuote);
        textView.setText(
                textView.getText().toString().replace(USERNAME_PLACEHOLDER,
                        getString(R.string.random_default_username)));
    }

    public void onRandomQuote(View view) {
        onStartFetchingRandomQuote();

        TextView randomQuoteTextView = findViewById(R.id.textViewRandomQuote);
        TextView randomAuthorTextView = findViewById(R.id.textViewRandomAuthor);

        randomQuoteTextView.setText(R.string.random_sample_quote);
        randomAuthorTextView.setText(R.string.random_sample_author);

        onEndFetchingRandomQuote();
    }

    private void onStartFetchingRandomQuote() {
        findViewById(R.id.progressBarRandomQuote).setVisibility(View.VISIBLE);
    }

    private void onEndFetchingRandomQuote() {
        findViewById(R.id.progressBarRandomQuote).setVisibility(View.INVISIBLE);
    }
}
