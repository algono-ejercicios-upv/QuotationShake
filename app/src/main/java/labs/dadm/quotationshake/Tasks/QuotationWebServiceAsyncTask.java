package labs.dadm.quotationshake.Tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.QuotationActivity;

public class QuotationWebServiceAsyncTask extends AsyncTask<Void, Void, Quotation> {
    private static int times = 0; // Test code. TODO: Remove when implementing web service

    private WeakReference<QuotationActivity> quotationActivityWeakReference;

    public QuotationWebServiceAsyncTask(QuotationActivity quotationActivity) {
        this.quotationActivityWeakReference = new WeakReference<>(quotationActivity);
    }

    @Override
    protected Quotation doInBackground(Void... voids) {
        // Test code. TODO: Remove when implementing web service
        times++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Quotation(
                String.format("Hey, this is test number %d!", times)
                , "Test guy");
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
