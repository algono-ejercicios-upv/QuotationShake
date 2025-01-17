package labs.dadm.quotationshake.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import labs.dadm.quotationshake.Adapter.QuotationAdapter;
import labs.dadm.quotationshake.Databases.DatabaseProviders;
import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.R;
import labs.dadm.quotationshake.Tasks.FavouriteQuotationDBAsyncTask;

public class FavouriteFragment extends AppCompatActivity {

    private static final String WIKI_SEARCH_URL = "https://en.wikipedia.org/wiki/Special:Search?search=";

    private QuotationAdapter quotationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favourite);

        quotationAdapter = new QuotationAdapter(new ArrayList<Quotation>(), new QuotationAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(QuotationAdapter adapter, int position) {
                try {
                    displayAuthorInfo(adapter.getQuotationAt(position));
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(FavouriteFragment.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException ex) {
                    Toast.makeText(FavouriteFragment.this, R.string.exception_unsupported_encoding_author,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, new QuotationAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(final QuotationAdapter adapter, final int position) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteFragment.this);
                dialogBuilder.setMessage(R.string.confirmation_delete_quotation);
                dialogBuilder.setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Quotation quotationToRemove = adapter.getQuotationAt(position);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseProviders
                                        .getCurrentProvider(FavouriteFragment.this)
                                        .removeQuotation(quotationToRemove);
                            }
                        }).start();
                        adapter.removeQuotationAt(position);
                    }
                });
                dialogBuilder.setNegativeButton(R.string.confirmation_no, null);

                dialogBuilder.show();
                return true;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.favouriteRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(quotationAdapter);

        FavouriteQuotationDBAsyncTask getQuotationsFromDBTask
                = new FavouriteQuotationDBAsyncTask(this);
        getQuotationsFromDBTask.execute();
    }

    public void addQuotationsToList(List<Quotation> quotationList) {
        if (quotationList.size() > 0) {
            quotationAdapter.addAllQuotations(quotationList);
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favourite_menu, menu);
        menu.findItem(R.id.clearAllQuotationsMenuItem)
                .setVisible(quotationAdapter.getItemCount() > 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearAllQuotationsMenuItem:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteFragment.this);
                dialogBuilder.setMessage(R.string.confirmation_clear_all_quotations);
                dialogBuilder.setPositiveButton(R.string.confirmation_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DatabaseProviders
                                        .getCurrentProvider(FavouriteFragment.this)
                                        .clearQuotations();
                            }
                        }).start();
                        quotationAdapter.clearAllQuotations();
                        item.setVisible(false);
                    }
                });
                dialogBuilder.setNegativeButton(R.string.confirmation_no, null);

                dialogBuilder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
