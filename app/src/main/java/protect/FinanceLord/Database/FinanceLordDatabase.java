package protect.FinanceLord.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AssetsType.class, AssetsValue.class, LiabilitiesType.class, LiabilitiesValue.class, BudgetsType.class, BudgetsValue.class, Transactions.class}, version = 8)
public abstract class FinanceLordDatabase extends RoomDatabase {
    public abstract AssetsTypeDao assetsTypeDao();
    public abstract AssetsValueDao assetsValueDao();
    public abstract LiabilitiesTypeDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
    public abstract BudgetsTypeDao budgetsTypeDao();
    public abstract BudgetsValueDao budgetsValueDao();
    public abstract TransactionsDao transactionsDao();
    public abstract ReportItemInfoDao reportItemInfoDao();

    private static FinanceLordDatabase database;

    public static synchronized FinanceLordDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, FinanceLordDatabase.class, "FinanceLordDb").fallbackToDestructiveMigration().build();
            return database;
        }
        return database;
    }
}
