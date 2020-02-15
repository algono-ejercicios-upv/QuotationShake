package labs.dadm.quotationshake.Databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import labs.dadm.quotationshake.Model.Quotation;

@Dao
public interface QuotationRoomDAO extends DatabaseProvider {
    @Insert
    void addQuotation(Quotation quotation);

    @Delete
    void removeQuotation(Quotation quotation);

    @Query("SELECT * FROM quotation_table")
    List<Quotation> getAllQuotations();

    @Query("SELECT * FROM quotation_table WHERE quote = :quoteText")
    Quotation getQuotationByText(String quoteText);

    @Query("DELETE FROM quotation_table")
    void clearQuotations();
}
