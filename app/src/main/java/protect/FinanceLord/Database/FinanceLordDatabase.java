package protect.FinanceLord.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import protect.FinanceLord.DAOs.AssetsTypeDao;
import protect.FinanceLord.DAOs.AssetsValueDao;
import protect.FinanceLord.DAOs.BudgetInfoDao;
import protect.FinanceLord.DAOs.BudgetsTypeDao;
import protect.FinanceLord.DAOs.BudgetsValueDao;
import protect.FinanceLord.DAOs.LiabilitiesTypeDao;
import protect.FinanceLord.DAOs.LiabilitiesValueDao;
import protect.FinanceLord.DAOs.ReportItemInfoDao;
import protect.FinanceLord.DAOs.TransactionsDao;

/**
 * The abstract interface that defines the database.
 * This interface defines all the entities in the database
 * and all the Data Access Objects used to perform CRUD operations.
 * The database is also created here.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
@Database(entities = {AssetsType.class, AssetsValue.class, LiabilitiesType.class, LiabilitiesValue.class, BudgetsType.class, BudgetsValue.class, Transactions.class}, version = 11)
public abstract class FinanceLordDatabase extends RoomDatabase {

    public abstract AssetsTypeDao assetsTypeDao();
    public abstract AssetsValueDao assetsValueDao();
    public abstract LiabilitiesTypeDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
    public abstract BudgetsTypeDao budgetsTypeDao();
    public abstract BudgetsValueDao budgetsValueDao();
    public abstract TransactionsDao transactionsDao();
    public abstract ReportItemInfoDao reportItemInfoDao();
    public abstract BudgetInfoDao financeRecordsDao();

    private static FinanceLordDatabase database;

    public static synchronized FinanceLordDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, FinanceLordDatabase.class, "FinanceLordDb").fallbackToDestructiveMigration().build();
            return database;
        }
        return database;
    }
}
