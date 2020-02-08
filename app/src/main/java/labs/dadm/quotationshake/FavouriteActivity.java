package labs.dadm.quotationshake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import labs.dadm.quotationshake.Adapter.QuotationAdapter;
import labs.dadm.quotationshake.Model.Quotation;

public class FavouriteActivity extends AppCompatActivity {

    private static final String WIKI_SEARCH_URL = "https://en.wikipedia.org/wiki/Special:Search?search=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ArrayList<Quotation> myMockQuotations = getMockQuotations();
        final QuotationAdapter quotationAdapter = new QuotationAdapter(myMockQuotations, new QuotationAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(QuotationAdapter adapter, int position) {
                try {
                    displayAuthorInfo(adapter.getQuotationAt(position));
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException ex) {
                    Toast.makeText(getBaseContext(), R.string.exception_unsupported_encoding_author,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.favouriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(quotationAdapter);
    }

    public void displayAuthorInfo(Quotation quotation) throws IllegalArgumentException, UnsupportedEncodingException {
        if (quotation.isAuthorAnonymous()) {
            throw new IllegalArgumentException(getString(R.string.exception_anonymous_author));
        }
        displayAuthorInfo(quotation.getQuoteAuthor());
    }

    public void displayAuthorInfo(String author) throws IllegalArgumentException, UnsupportedEncodingException {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("The author cannot be null or empty");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(WIKI_SEARCH_URL + URLEncoder.encode(author, "UTF-8")));
        startActivity(intent);
    }

    private ArrayList<Quotation> getMockQuotations() {
        ArrayList<Quotation> quotations = new ArrayList<>();
        quotations.add(new Quotation("Think big", "IMAX"));
        quotations.add(new Quotation("Beauty outside. Beast inside.", "Mac Pro"));
        quotations.add(new Quotation("American by birth. Rebel by choice", "Harley Davidson"));
        quotations.add(new Quotation("Don't be evil", "Google"));
        quotations.add(new Quotation("If you want to impress someone, put him on your Black list", "Johnnie Walker"));
        quotations.add(new Quotation("Live in your world. Play in ours", "PlayStation"));
        quotations.add(new Quotation("Impossible is nothing", "Adidas"));
        quotations.add(new Quotation("I am anonymous", null));
        return quotations;
    }
}
