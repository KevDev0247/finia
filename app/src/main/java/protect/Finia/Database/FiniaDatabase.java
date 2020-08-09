package protect.Finia.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import protect.Finia.DAOs.AssetsTypeDao;
import protect.Finia.DAOs.AssetsValueDao;
import protect.Finia.DAOs.BudgetInfoDao;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.DAOs.BudgetsValueDao;
import protect.Finia.DAOs.LiabilitiesTypeDao;
import protect.Finia.DAOs.LiabilitiesValueDao;
import protect.Finia.DAOs.ReportItemInfoDao;
import protect.Finia.DAOs.TransactionsDao;

/**
 * The abstract interface that defines the database.
 * This interface defines all the entities in the database.
 * and all the Data Access Objects used to perform CRUD operations.
 * The database is also created here.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
@Database(entities = {AssetsType.class, AssetsValue.class, LiabilitiesType.class, LiabilitiesValue.class, BudgetsType.class, BudgetsValue.class, Transactions.class}, version = 11)
public abstract class FiniaDatabase extends RoomDatabase {

    public abstract AssetsTypeDao assetsTypeDao();
    public abstract AssetsValueDao assetsValueDao();
    public abstract LiabilitiesTypeDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
    public abstract BudgetsTypeDao budgetsTypeDao();
    public abstract BudgetsValueDao budgetsValueDao();
    public abstract TransactionsDao transactionsDao();
    public abstract ReportItemInfoDao reportItemInfoDao();
    public abstract BudgetInfoDao financeRecordsDao();

    private static FiniaDatabase database;

    public static synchronized FiniaDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, FiniaDatabase.class, "FinanceLordDb").fallbackToDestructiveMigration().build();
            return database;
        }
        return database;
    }
}
