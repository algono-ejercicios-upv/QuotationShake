package labs.dadm.quotationshake.Tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import labs.dadm.quotationshake.Databases.DatabaseProviders;
import labs.dadm.quotationshake.FavouriteActivity;
import labs.dadm.quotationshake.Model.Quotation;

public class FavouriteQuotationDBAsyncTask extends AsyncTask<Boolean, Void, List<Quotation>> {
    private WeakReference<FavouriteActivity> favouriteActivityWeakReference;

    public FavouriteQuotationDBAsyncTask(FavouriteActivity favouriteActivity) {
        this.favouriteActivityWeakReference = new WeakReference<>(favouriteActivity);
    }

    @Override
    protected List<Quotation> doInBackground(Boolean... booleans) {
        FavouriteActivity favouriteActivity = favouriteActivityWeakReference.get();
        if (favouriteActivity == null) {
            // The activity was destroyed
            return null;
        } else {
            // If no provider is specified, the one from preferences will be used
            if (booleans.length == 0) {
                return DatabaseProviders
                        .getCurrentProvider(favouriteActivity)
                        .getAllQuotations();
            } else {
                boolean useRoom = booleans[0];
                return DatabaseProviders
                        .getProvider(favouriteActivity, useRoom)
                        .getAllQuotations();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Quotation> quotationList) {
        super.onPostExecute(quotationList);
        FavouriteActivity favouriteActivity = favouriteActivityWeakReference.get();
        if (favouriteActivity != null) {
            favouriteActivity.addQuotationsToList(quotationList);
        }
    }
}
