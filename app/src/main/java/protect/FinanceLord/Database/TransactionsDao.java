package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionsDao {

    @Insert
    void insertTransaction (Transactions transaction);

    @Update
    void updateTransaction (Transactions ... transaction);

    @Query("SELECT * FROM Transactions WHERE transactionId = :transactionId")
    List<Transactions> queryTransactionsById (int transactionId);

    @Query("SELECT * FROM Transactions WHERE transactionCategoryId = :transactionCategoryId")
    List<Transactions> queryTransactionByCategoryId (int transactionCategoryId);

    @Query("SELECT * FROM Transactions WHERE date = :date")
    List<Transactions> queryTransactionByDate (Long date);

    @Query("SELECT * FROM Transactions")
    List<Transactions> queryAllTransaction ();

    @Query("DELETE FROM Transactions WHERE transactionId = :transactionId")
    void deleteIndividualTransaction (int transactionId);
}
