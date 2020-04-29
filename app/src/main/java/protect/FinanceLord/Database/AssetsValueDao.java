package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface AssetsValueDao {

    @Insert
    void insertAssetValue(AssetsValue assetsValue);

    @Update
    void updateAssetValue(AssetsValue ... assetsValue);

    @Query("SELECT * FROM AssetsValue WHERE assetsId = :assetId")
    List<AssetsValue> queryAssetsByTypeId(int assetId);

    @Query("SELECT * FROM AssetsValue WHERE assetsPrimaryId = :assetPrimaryId")
    List<AssetsValue> queryAssetById(int assetPrimaryId);

    @Query("SELECT * FROM AssetsValue WHERE date >= :date")
    List<AssetsValue> queryAssetsSinceDate(Long date);

    @Query("SELECT * FROM AssetsValue WHERE date <= :date")
    List<AssetsValue> queryAssetsBeforeDate(Long date);

    @Query("SELECT * FROM AssetsValue WHERE date <= :dateEnd AND date >= :dateStart")
    List<AssetsValue> queryAssetsByDate(Long dateStart, Long dateEnd);

    @Query("SELECT assetsPrimaryId, assetsId, assetsValue, MIN(date) FROM AssetsValue WHERE assetsId = :assetId")
    AssetsValue queryLatestIndividualAsset(int assetId);

    @Query("SELECT * FROM AssetsValue")
    List<AssetsValue> queryAllAssetsValue();
}
