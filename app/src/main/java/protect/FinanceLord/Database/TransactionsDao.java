package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionsDao {
    @Insert
    void insertTransactionValue (Transactions transactionsValue);

    @Update
    void updateTransactionValue (Transactions ... transactionsValue);

    @Insert
    void insertTransactionComments (Transactions transactionsComments);

    @Insert
    void updateTransactionComments (Transactions ... transactionsComments);

    @Insert
    void insertTransactionDate (Transactions transactionsDate);

    @Update
    void updateTransactionDate (Transactions ... transactionsDate);

    @Query("SELECT * FROM Transactions WHERE transactionId LIKE :transactionId")
    List<Transactions> queryTransactionsById (int transactionId);

    @Query("SELECT * FROM Transactions WHERE transactionValue LIKE :transactionValue")
    List<Transactions> queryTransactionByByValue (float transactionValue);

    @Query("SELECT * FROM Transactions WHERE transactionCategoryId LIKE :transactionCategoryId")
    List<Transactions> queryTransactionByCategoryId (int transactionCategoryId);

    @Query("SELECT * FROM Transactions WHERE transactionComments LIKE :transactionComments")
    List<Transactions> queryTransactionByComments (String transactionComments);

    @Query("SELECT * FROM Transactions WHERE date LIKE :date")
    List<Transactions> queryTransactionByDate (Long date);

    @Query("SELECT * FROM Transactions")
    List<Transactions> queryAllTransaction ();
}
