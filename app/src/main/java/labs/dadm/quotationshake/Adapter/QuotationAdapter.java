package labs.dadm.quotationshake.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import labs.dadm.quotationshake.FavouriteActivity;
import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.R;

public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.ViewHolder> {

    private List<Quotation> quotationList;

    public QuotationAdapter(List<Quotation> quotationList) {
        this.quotationList = quotationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quotation_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quotation quotation = quotationList.get(position);
        holder.textViewQuoteText.setText(quotation.getQuoteText());
        holder.textViewQuoteAuthor.setText(quotation.getQuoteAuthor());
    }

    @Override
    public int getItemCount() {
        return quotationList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewQuoteText, textViewQuoteAuthor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewQuoteText = itemView.findViewById(R.id.textViewQuoteText);
            this.textViewQuoteAuthor = itemView.findViewById(R.id.textViewQuoteAuthor);
        }
    }
}
