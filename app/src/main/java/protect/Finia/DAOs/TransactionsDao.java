package protect.Finia.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import protect.Finia.Database.Transactions;
import protect.Finia.SpendingModule.GroupedSpending;
import protect.Finia.SpendingModule.MonthlyTotalSpending;

@Dao
public interface TransactionsDao {

    @Insert
    void insertTransaction (Transactions transaction);

    @Update
    void updateTransaction (Transactions ... transaction);

    @Query("SELECT * FROM Transactions WHERE transactionCategoryId = :transactionCategoryId")
    List<Transactions> queryTransactionByCategoryId (int transactionCategoryId);

    @Query("SELECT * FROM Transactions")
    List<Transactions> queryAllTransaction ();

    @Query("SELECT \n" +
            "  transactionCategoryId AS categoryId, \n" +
            "  SUM(-transactionValue) AS categoryTotal, \n" +
            "  strftime('%Y-%m',  datetime(Transactions.date/1000,'unixepoch', 'localtime')) AS month \n" +
            "FROM Transactions WHERE transactionValue < 0 \n" +
            "GROUP BY strftime('%Y-%m',  datetime(Transactions.date/1000,'unixepoch', 'localtime')), transactionCategoryId")
    List<GroupedSpending> queryGroupedExpenses();

    @Query("SELECT \n" +
            "  SUM(-transactionValue) AS categoryTotal, \n" +
            "  strftime('%Y-%m',  datetime(Transactions.date/1000,'unixepoch', 'localtime')) AS month \n" +
            "FROM Transactions WHERE transactionValue < 0 \n" +
            "GROUP BY strftime('%Y-%m',  datetime(Transactions.date/1000,'unixepoch', 'localtime'))")
    List<MonthlyTotalSpending> queryMonthlyTotalExpenses();

    @Query("DELETE FROM Transactions WHERE transactionId = :transactionId")
    void deleteIndividualTransaction (int transactionId);
}
