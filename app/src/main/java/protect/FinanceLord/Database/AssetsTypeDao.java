package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetsTypeDao {
    @Insert
    void insertAssetType(AssetsType assetsType);

    @Insert
    void insertAssetParentType(AssetsType assetsParentType);

    @Insert
    void insertAssetsTypes(List<AssetsType> assetsTypes);

    @Update
    void updateAssetsType(AssetsType ... assetsTypes);

    @Query("SELECT * FROM AssetsType WHERE assetsId LIKE :assetsId")
    List<AssetsType> queryAssetsById (int assetsId);

    @Query("SELECT * FROM AssetsType WHERE assetsName LIKE :assetsName")
    List<AssetsType> queryAssetsByName (String assetsName);

    @Query("SELECT * FROM AssetsType WHERE assetsParentType LIKE :assetsParentType")
    List<AssetsType> queryAssetsByParentType (String assetsParentType);

    @Query("SELECT * FROM AssetsType")
    List<AssetsType> queryAllAssetsType();
}
