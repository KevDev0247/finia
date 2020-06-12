package protect.FinanceLord.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import protect.FinanceLord.Database.Transactions;

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

    @Query("SELECT *, strftime('%Y-%m',  datetime(Transactions.date/1000,'unixepoch', 'localtime')) AS month FROM Transactions")
    List<Transactions> queryTransactionGroupedByMonth ();

    @Query("SELECT * FROM Transactions")
    List<Transactions> queryAllTransaction ();

    @Query("DELETE FROM Transactions WHERE transactionId = :transactionId")
    void deleteIndividualTransaction (int transactionId);
}
