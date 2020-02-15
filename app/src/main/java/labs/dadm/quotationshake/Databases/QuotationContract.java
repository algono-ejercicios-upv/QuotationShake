package labs.dadm.quotationshake.Databases;

import android.provider.BaseColumns;

class QuotationContract {
    private QuotationContract() {
    }

    static class QuotationBaseColumns implements BaseColumns {
        static final String tableName = "quotation_table",
                columnName_authorName = "author",
                columnName_quoteText = "quote";
    }
}
