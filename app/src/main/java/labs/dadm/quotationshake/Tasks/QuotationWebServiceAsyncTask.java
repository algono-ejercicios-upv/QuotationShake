package labs.dadm.quotationshake.Tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import labs.dadm.quotationshake.Fragments.QuotationFragment;
import labs.dadm.quotationshake.Model.Quotation;

public class QuotationWebServiceAsyncTask extends AsyncTask<String, Void, Quotation> {
    private RequestMethod requestMethod = RequestMethod.GET;

    private static final String DEFAULT_LANGUAGE_CODE = "en";

    private WeakReference<QuotationFragment> quotationActivityWeakReference;

    private static void addGetQueryParameters(Uri.Builder builder, String languageCode) {
        builder.appendQueryParameter("method", "getQuote")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("lang", languageCode);
    }

    public QuotationWebServiceAsyncTask(QuotationFragment quotationFragment) {
        this.quotationActivityWeakReference = new WeakReference<>(quotationFragment);
    }

    private static String getPostQueryParameters(String languageCode) {
        final String format = "%s=%s", concat = "&";
        return
                String.format(format, "method", "getQuote") + concat
                        + String.format(format, "format", "json") + concat
                        + String.format(format, "lang", languageCode);
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    private Uri buildWebServiceUri(String languageCode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.forismatic.com")
                .appendPath("api")
                .appendPath("1.0")
                .appendPath(""); // This is on purpose (to add an extra '/')

        if (requestMethod == RequestMethod.GET) {
            addGetQueryParameters(builder, languageCode);
        }

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
        System.out.println("Web service URI: " + webServiceUri.toString());
        System.out.println("Request method: " + requestMethod.toString());

        try {
            URL webServiceUrl = new URL(webServiceUri.toString());
            HttpsURLConnection connection = (HttpsURLConnection) webServiceUrl.openConnection();
            connection.setRequestMethod(requestMethod.toString());

            if (requestMethod == RequestMethod.POST) {
                connection.setDoOutput(true);
                try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                    writer.write(getPostQueryParameters(languageCode));
                    writer.flush();
                }
            }

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

    public enum RequestMethod {GET, POST}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        QuotationFragment quotationFragment = quotationActivityWeakReference.get();
        if (quotationFragment != null) {
            quotationFragment.onStartFetchingRandomQuote();
        }
    }

    @Override
    protected void onPostExecute(Quotation quotation) {
        super.onPostExecute(quotation);
        QuotationFragment quotationFragment = quotationActivityWeakReference.get();
        if (quotationFragment != null) {
            quotationFragment.onEndFetchingRandomQuote(quotation);
        }
    }
}
