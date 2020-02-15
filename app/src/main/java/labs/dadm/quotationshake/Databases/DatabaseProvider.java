package labs.dadm.quotationshake.Databases;

import java.util.List;

import labs.dadm.quotationshake.Model.Quotation;

public interface DatabaseProvider {
    void addQuotation(Quotation quotation);

    void removeQuotation(Quotation quotation);

    List<Quotation> getAllQuotations();

    Quotation getQuotationByText(String quoteText);

    void clearQuotations();
}
