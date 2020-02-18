package labs.dadm.quotationshake.Tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.QuotationActivity;

public class QuotationWebServiceAsyncTask extends AsyncTask<String, Void, Quotation> {
    private static final String DEFAULT_LANGUAGE_CODE = "en";

    private WeakReference<QuotationActivity> quotationActivityWeakReference;

    public QuotationWebServiceAsyncTask(QuotationActivity quotationActivity) {
        this.quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    private static Uri buildWebServiceUri(String languageCode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.forismatic.com")
                .appendPath("api")
                .appendPath("1.0")
                .appendPath("") // This is on purpose (to add an extra '/')
                .appendQueryParameter("method", "getQuote")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("lang", languageCode);

        return builder.build();
    }

    @Override
    protected Quotation doInBackground(String... strings) {
        String languageCode = strings.length == 0 ? DEFAULT_LANGUAGE_CODE : strings[0];

        if (languageCode == null) {
            System.out.println("No language given. Using default (" + DEFAULT_LANGUAGE_CODE + ")");
            languageCode = DEFAULT_LANGUAGE_CODE;
        } else {
            System.out.println("Using user-defined language (" + languageCode + ")");
        }

        Uri webServiceUri = buildWebServiceUri(languageCode);
        //System.out.println("Web service URI: " + webServiceUri.toString());

        try {
            URL webServiceUrl = new URL(webServiceUri.toString());
            HttpsURLConnection connection = (HttpsURLConnection) webServiceUrl.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    return new Gson().fromJson(reader, Quotation.class);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        QuotationActivity quotationActivity = quotationActivityWeakReference.get();
        if (quotationActivity != null) {
            quotationActivity.onStartFetchingRandomQuote();
        }
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        super.onPostExecute(quotation);
        QuotationActivity quotationActivity = quotationActivityWeakReference.get();
        if (quotationActivity != null) {
            quotationActivity.onEndFetchingRandomQuote(quotation);
        }
    }
}
