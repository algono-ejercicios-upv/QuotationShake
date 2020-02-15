package labs.dadm.quotationshake.Databases;

import androidx.annotation.Nullable;

import java.util.List;

import labs.dadm.quotationshake.Model.Quotation;

public interface DatabaseProvider {
    void addQuotation(Quotation quotation);

    void removeQuotation(Quotation quotation);

    List<Quotation> getAllQuotations();

    @Nullable
    Quotation getQuotationByText(String quoteText);

    void clearQuotations();
}
