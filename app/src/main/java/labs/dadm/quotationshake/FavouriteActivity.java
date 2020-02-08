package labs.dadm.quotationshake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import labs.dadm.quotationshake.Adapter.QuotationAdapter;
import labs.dadm.quotationshake.Model.Quotation;

public class FavouriteActivity extends AppCompatActivity {

    private static final String WIKI_SEARCH_URL = "https://en.wikipedia.org/wiki/Special:Search?search=";
    private static final String SAMPLE_AUTHOR = "Albert Einstein";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ArrayList<Quotation> myMockQuotations = getMockQuotations(20);
        QuotationAdapter quotationAdapter = new QuotationAdapter(myMockQuotations);

        RecyclerView recyclerView = findViewById(R.id.favouriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(quotationAdapter);
    }

    public void onAuthorInfoPressed(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(WIKI_SEARCH_URL + SAMPLE_AUTHOR));
        startActivity(intent);
    }

    private ArrayList<Quotation> getMockQuotations() {
        return getMockQuotations(10);
    }

    private ArrayList<Quotation> getMockQuotations(int number) {
        ArrayList<Quotation> quotations = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            quotations.add(new Quotation("I am number " + i, "Author number " + i));
        }
        return quotations;
    }
}
