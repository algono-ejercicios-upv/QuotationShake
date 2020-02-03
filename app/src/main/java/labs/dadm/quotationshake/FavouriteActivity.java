package labs.dadm.quotationshake;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FavouriteActivity extends AppCompatActivity {

    private static final String WIKI_SEARCH_URL = "https://en.wikipedia.org/wiki/Special:Search?search=";
    private static final String SAMPLE_AUTHOR = "Albert Einstein";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }

    public void onAuthorInfoPressed(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(WIKI_SEARCH_URL + SAMPLE_AUTHOR));
        startActivity(intent);
    }
}
