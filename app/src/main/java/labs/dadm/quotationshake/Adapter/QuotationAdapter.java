package labs.dadm.quotationshake.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import labs.dadm.quotationshake.Model.Quotation;
import labs.dadm.quotationshake.R;

public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.ViewHolder> {

    private List<Quotation> quotationList;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener longClickListener;

    public QuotationAdapter(List<Quotation> quotationList, OnItemClickListener itemClickListener, OnItemLongClickListener longClickListener) {
        this.quotationList = quotationList;
        this.itemClickListener = itemClickListener;
        this.longClickListener = longClickListener;
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

        String author = quotation.getQuoteAuthor();
        if (author == null) {
            holder.textViewQuoteAuthor.setText(R.string.author_anonymous);
        } else {
            holder.textViewQuoteAuthor.setText(quotation.getQuoteAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return quotationList.size();
    }

    public Quotation getQuotationAt(int position) {
        return quotationList.get(position);
    }

    public void removeQuotationAt(int position) {
        quotationList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearAllQuotations() {
        quotationList.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(QuotationAdapter adapter, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClickListener(QuotationAdapter adapter, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewQuoteText, textViewQuoteAuthor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewQuoteText = itemView.findViewById(R.id.textViewQuoteText);
            this.textViewQuoteAuthor = itemView.findViewById(R.id.textViewQuoteAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClickListener(QuotationAdapter.this, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return longClickListener.onItemLongClickListener(QuotationAdapter.this, getAdapterPosition());
                }
            });
        }
    }
}
