package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetsValueDao {
    @Insert
    void insertAssetId (AssetsValue assetId);

    @Update
    void updateAssetId (AssetsValue assetId);

    @Insert
    void insertAssetValue(AssetsValue assetsValue);

    @Update
    void updateAssetValue(AssetsValue ... assetsValue);

    @Insert
    void insertAssetDate (AssetsValue date);

    @Update
    void updateAssetDate (AssetsValue ... date);

    @Query("SELECT * FROM AssetsValue WHERE assetsId LIKE :assetId")
    List<AssetsValue> queryAssetsById  (int assetId);

    @Query("SELECT * FROM AssetsValue WHERE assetsValue LIKE :assetsValue")
    List<AssetsValue> queryAssetsByValue (float assetsValue);

    @Query("SELECT * FROM AssetsValue WHERE date LIKE :date")
    List<AssetsValue> queryAssetsByDate (Long date);

    @Query("SELECT * FROM AssetsValue")
    List<AssetsValue> queryAllAssetsValue();
}
