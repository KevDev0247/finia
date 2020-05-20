package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Transactions")
public class Transactions {

    @PrimaryKey(autoGenerate = true)
    private int transactionId;

    @ColumnInfo(name = "TransactionName")
    private String transactionName;

    @ColumnInfo(name = "TransactionValue")
    private float transactionValue;

    @ColumnInfo(name = "TransactionCategoryId")
    private int transactionCategoryId;

    @ColumnInfo(name = "TransactionComments")
    private String transactionComments;

    @ColumnInfo(name = "Date")
    private Long date;

    @Ignore
    public Transactions(){ }

    public Transactions(int transactionId, String transactionName, float transactionValue, int transactionCategoryId, String transactionComments, Long date){
        this.transactionId = transactionId;
        this.transactionName = transactionName;
        this.transactionValue = transactionValue;
        this.transactionCategoryId = transactionCategoryId;
        this.transactionComments = transactionComments;
        this.date = date;
    }

    public int getTransactionId()       {return transactionId;}
    public String getTransactionName()      {return transactionName; }
    public float getTransactionValue()      {return transactionValue;}
    public int getTransactionCategoryId()       {return transactionCategoryId;}
    public String getTransactionComments()      {return transactionComments;}
    public Long getDate()       {return date;}

    public void setTransactionId(int transactionId)     {this.transactionId = transactionId;}
    public void setTransactionName(String transactionName)      {this.transactionName = transactionName;}
    public void setTransactionValue(float transactionValue)     {this.transactionValue = transactionValue;}
    public void setTransactionCategoryId(int transactionCategoryId)     {this.transactionCategoryId = transactionCategoryId;}
    public void setTransactionComments(String transactionComments)      {this.transactionComments = transactionComments;}
    public void setDate(Long date)      {this.date = date;}
}
