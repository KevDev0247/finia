package protect.Finia.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import protect.Finia.Database.AssetsValue;

@Dao
public interface AssetsValueDao {

    @Insert
    void insertAssetValue(AssetsValue assetsValue);

    @Update
    void updateAssetValue(AssetsValue ... assetsValue);

    @Query("SELECT * FROM AssetsValue WHERE assetsPrimaryId = :assetPrimaryId")
    List<AssetsValue> queryAssetById(int assetPrimaryId);

    @Query("SELECT * FROM AssetsValue WHERE date <= :dateEnd AND date >= :dateStart")
    List<AssetsValue> queryAssetsByTimePeriod(Long dateStart, Long dateEnd);

    @Query("SELECT * FROM AssetsValue")
    List<AssetsValue> queryAllAssetsValue();

    @Query("SELECT * FROM AssetsValue WHERE assetsId = :assetId ORDER BY date DESC LIMIT 1")
    AssetsValue queryLatestIndividualAsset(int assetId);

    @Query("SELECT * FROM AssetsValue WHERE date = :date AND assetsId = :assetId")
    AssetsValue queryIndividualAssetByTime(Long date, int assetId);

    @Query("SELECT * FROM AssetsValue WHERE date < :date AND assetsId = :assetId ORDER BY date DESC LIMIT 1")
    AssetsValue queryPreviousAssetBeforeTime(Long date, int assetId);

    @Query("DELETE FROM AssetsValue WHERE date = :date")
    void deleteAssetsAtDate(Long date);
}
