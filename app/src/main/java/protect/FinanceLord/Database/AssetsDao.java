package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssetsDao {
    @Insert
    void insertAssetType(Assets assetsType);

    @Insert
    public void insertAssetValue(Assets assetsValue);

    @Update
    public void updateAssetValue(Assets ... assetsValues);

    @Insert
    void insertAssetParentType(Assets assetsParentType);

    @Insert
    void insertAssetsTypes(List<Assets> assetsTypes);

    @Update
    void updateAssetsType(Assets ... assetsTypes);

    @Query("SELECT * FROM Assets WHERE assetsId LIKE :assetsId")
    List<Assets> queryAssetsById (String assetsId);

    @Query("SELECT * FROM Assets WHERE assetsName LIKE :assetsName")
    List<Assets> queryAssetsByName (String assetsName);

    @Query("SELECT * FROM Assets WHERE assetsParentType LIKE :assetsParentType")
    List<Assets> queryAssetsByParentType (String assetsParentType);

    @Query("SELECT * FROM assets")
    List<Assets> queryAllAssets();
}
