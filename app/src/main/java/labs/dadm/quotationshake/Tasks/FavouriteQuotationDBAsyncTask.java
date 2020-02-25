package labs.dadm.quotationshake.Tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import labs.dadm.quotationshake.Databases.DatabaseProviders;
import labs.dadm.quotationshake.Fragments.FavouriteFragment;
import labs.dadm.quotationshake.Model.Quotation;

public class FavouriteQuotationDBAsyncTask extends AsyncTask<Boolean, Void, List<Quotation>> {
    private WeakReference<FavouriteFragment> favouriteActivityWeakReference;

    public FavouriteQuotationDBAsyncTask(FavouriteFragment favouriteFragment) {
        this.favouriteActivityWeakReference = new WeakReference<>(favouriteFragment);
    }

    @Override
    protected List<Quotation> doInBackground(Boolean... booleans) {
        FavouriteFragment favouriteFragment = favouriteActivityWeakReference.get();
        if (favouriteFragment == null) {
            // The activity was destroyed
            return null;
        } else {
            // If no provider is specified, the one from preferences will be used
            if (booleans.length == 0) {
                return DatabaseProviders
                        .getCurrentProvider(favouriteFragment)
                        .getAllQuotations();
            } else {
                boolean useRoom = booleans[0];
                return DatabaseProviders
                        .getProvider(favouriteFragment, useRoom)
                        .getAllQuotations();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Quotation> quotationList) {
        super.onPostExecute(quotationList);
        FavouriteFragment favouriteFragment = favouriteActivityWeakReference.get();
        if (favouriteFragment != null) {
            favouriteFragment.addQuotationsToList(quotationList);
        }
    }
}
