package protect.FinanceLord.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import protect.FinanceLord.Converters;

@Database(entities = {AssetsType.class, AssetsValue.class, LiabilitiesType.class, LiabilitiesValue.class, Budgets.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class FinanceLordDatabase extends RoomDatabase {
    public abstract AssetsTypeDao assetsTypeDao();
    public abstract AssetsValueDao assetsValueDao();
    public abstract LiabilitiesTypeDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
    public abstract BudgetsDao budgetsDao();
}
