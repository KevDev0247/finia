package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FinancialRecordsDao {

    @Query("SELECT \n" +
            "  FinanceRecord.budgetCategoryId AS budgetCategoryId, \n" +
            "  SUM(FinanceRecord.transactionValue) AS totalUsage, \n" +
            "  FinanceRecord.dateStart AS dateStart, \n" +
            "  FinanceRecord.dateEnd AS dateEnd \n" +
            "FROM ( \n" +
            "  SELECT" +
            "    BudgetsValue.budgetCategoryId, \n" +
            "    BudgetsValue.dateStart, \n" +
            "    BudgetsValue.dateEnd, \n" +
            "    Transactions.transactionValue, \n" +
            "    Transactions.date \n" +
            "  FROM BudgetsValue JOIN Transactions \n" +
            "  ON BudgetsValue.budgetCategoryId = Transactions.transactionCategoryId \n" +
            "  AND Transactions.date >= BudgetsValue.dateStart AND Transactions.date <= BudgetsValue.dateEnd) \n" +
            "AS FinanceRecord \n" +
            "GROUP BY FinanceRecord.budgetCategoryId, FinanceRecord.dateStart, FinanceRecord.dateEnd")
    List<FinancialRecords> queryFinancialRecords();
}
