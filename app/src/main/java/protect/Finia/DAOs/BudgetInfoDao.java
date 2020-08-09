package protect.Finia.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import protect.Finia.BudgetModule.BudgetInfo;

@Dao
public interface BudgetInfoDao {

    @Query("SELECT \n" +
            "  FinanceRecord.budgetsId AS budgetId, \n" +
            "  FinanceRecord.budgetCategoryId AS budgetCategoryId, \n" +
            "  FinanceRecord.budgetValue AS budgetTotal, \n" +
            "  SUM(FinanceRecord.transactionValue) AS totalUsage, \n" +
            "  FinanceRecord.dateStart AS dateStart, \n" +
            "  FinanceRecord.dateEnd AS dateEnd \n" +
            "FROM ( \n" +
            "  SELECT" +
            "    BudgetsValue.budgetsId, \n" +
            "    BudgetsValue.budgetCategoryId, \n" +
            "    BudgetsValue.budgetValue, \n" +
            "    BudgetsValue.dateStart, \n" +
            "    BudgetsValue.dateEnd, \n" +
            "    Transactions.transactionValue, \n" +
            "    Transactions.date \n" +
            "  FROM BudgetsValue LEFT JOIN Transactions \n" +
            "  ON BudgetsValue.budgetCategoryId = Transactions.transactionCategoryId \n" +
            "  AND Transactions.date >= BudgetsValue.dateStart AND Transactions.date <= BudgetsValue.dateEnd) \n" +
            "AS FinanceRecord \n" +
            "GROUP BY FinanceRecord.budgetCategoryId, FinanceRecord.dateStart, FinanceRecord.dateEnd")
    List<BudgetInfo> queryFinancialRecords();
}
