package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetsValueDao {
    @Insert
    public void insertAssetValue(AssetsValue assetsValue);

    @Update
    public void updateAssetValue(AssetsValue ... assetsValues);

    @Query("SELECT * FROM AssetsValue WHERE assetsId LIKE :assetsId")
    public List<AssetsValue> queryAssetsById (int assetsId);

    @Query("SELECT * FROM AssetsValue")
    public List<AssetsValue> queryAllAssets ();
}
