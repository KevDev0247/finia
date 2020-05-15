package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetsValueDao {

    @Insert
    void insertAssetValue(AssetsValue assetsValue);

    @Update
    void updateAssetValue(AssetsValue ... assetsValue);

    @Query("SELECT * FROM AssetsValue WHERE assetsPrimaryId = :assetPrimaryId")
    List<AssetsValue> queryAssetById(int assetPrimaryId);

    @Query("SELECT * FROM AssetsValue WHERE assetsId = :assetId ORDER BY date DESC")
    List<AssetsValue> queryAssetsByTypeId(int assetId);

    @Query("SELECT * FROM AssetsValue WHERE date <= :dateEnd AND date >= :dateStart")
    List<AssetsValue> queryAssetsByTimePeriod(Long dateStart, Long dateEnd);

    @Query("SELECT * FROM AssetsValue WHERE date <= :date AND assetsId = :assetId ORDER BY date DESC")
    List<AssetsValue> querySingleTypeAssetBeforeTime(Long date, int assetId);

    @Query("SELECT * FROM AssetsValue")
    List<AssetsValue> queryAllAssetsValue();

    @Query("SELECT * FROM AssetsValue WHERE assetsId = :assetId ORDER BY date DESC LIMIT 1")
    AssetsValue queryLatestIndividualAsset(int assetId);

    @Query("SELECT * FROM AssetsValue WHERE date = :date AND assetsId = :assetId")
    AssetsValue queryIndividualAssetByDate(Long date, int assetId);
}
