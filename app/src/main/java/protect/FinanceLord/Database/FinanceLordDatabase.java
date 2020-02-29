package protect.FinanceLord.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AssetsType.class, AssetsValue.class, LiabilitiesType.class, LiabilitiesValue.class}, version = 3)
public abstract class FinanceLordDatabase extends RoomDatabase {
    public abstract AssetsTypeDao assetsTypeDao();
    public abstract AssetsValueDao assetsValueDao();
    public abstract LiabilitiesTypeDao liabilitiesTypeDao();
    public abstract LiabilitiesValueDao liabilitiesValueDao();
}
