package protect.Finia.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import protect.Finia.dao.AssetsTypeDao;
import protect.Finia.dao.AssetsValueDao;
import protect.Finia.dao.BudgetInfoDao;
import protect.Finia.dao.BudgetsTypeDao;
import protect.Finia.dao.BudgetsValueDao;
import protect.Finia.dao.LiabilitiesTypeDao;
import protect.Finia.dao.LiabilitiesValueDao;
import protect.Finia.dao.ReportItemInfoDao;
import protect.Finia.dao.TransactionsDao;
import protect.Finia.models.AssetsType;
import protect.Finia.models.AssetsValue;
import protect.Finia.models.BudgetsType;
import protect.Finia.models.BudgetsValue;
import protect.Finia.models.LiabilitiesType;
import protect.Finia.models.LiabilitiesValue;
import protect.Finia.models.Transactions;

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
