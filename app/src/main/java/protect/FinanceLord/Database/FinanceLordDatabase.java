package protect.FinanceLord.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import protect.FinanceLord.Converters;

@Database(entities = {Assets.class, Liabilities.class, Budgets.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class FinanceLordDatabase extends RoomDatabase {
    public abstract AssetsDao assetsDao();
    public abstract LiabilitiesDao liabilitiesDao();
    public abstract BudgetsDao budgetsDao();
}
