package protect.FinanceLord.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Assets.class, Liabilities.class, LiabilitiesValue.class}, version = 3)
public abstract class FinanceLordDatabase extends RoomDatabase {
    public abstract AssetsDao assetsDao();
    public abstract LiabilitiesDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
}
