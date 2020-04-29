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

    @Query("SELECT * FROM AssetsType WHERE assetsId = :assetsId")
    List<AssetsType> queryAssetsById (int assetsId);

    @Query("SELECT * FROM AssetsType WHERE assetsParentType = :assetsParentType")
    List<AssetsType> queryAssetsByParentType (String assetsParentType);

    @Query("SELECT * FROM AssetsType WHERE assetsName = :assetsName")
    AssetsType queryAssetsByType (String assetsName);

    @Query("SELECT * FROM AssetsType")
    List<AssetsType> queryAllAssetsType();

    @Query("SELECT \n" +
            "assetsThirdLevelComposed.*, \n" +
            "assetsFourthLevel.assetsId AS assetsFourthLevelId, \n" +
            "assetsFourthLevel.assetsName AS assetsFourthLevelName \n" +
            "FROM ( \n" +
            "SELECT \n" +
            "assetsSecondLevelComposed.assetsFirstLevelId, \n" +
            "assetsSecondLevelComposed.assetsFirstLevelName, \n" +
            "assetsSecondLevelComposed.assetsSecondLevelId, \n" +
            "assetsSecondLevelComposed.assetsSecondLevelName, \n" +
            "assetsThirdLevel.assetsId AS assetsThirdLevelId, \n" +
            "assetsThirdLevel.assetsName AS assetsThirdLevelName \n" +
            "FROM( \n" +
            "SELECT \n" +
            "assetsFirstLevel.assetsId AS assetsFirstLevelId, \n" +
            "assetsFirstLevel.assetsName AS assetsFirstLevelName, \n" +
            "assetsSecondLevel.assetsId AS assetsSecondLevelId, \n" +
            "assetsSecondLevel.assetsName AS assetsSecondLevelName \n" +
            "FROM AssetsType AS assetsFirstLevel \n" +
            "JOIN AssetsType AS assetsSecondLevel ON assetsFirstLevel.assetsParentType IS NULL \n" +
            "AND assetsFirstLevel.assetsName = assetsSecondLevel.assetsParentType \n" +
            ") AS assetsSecondLevelComposed \n" +
            "LEFT JOIN AssetsType AS assetsThirdLevel ON assetsSecondLevelComposed.assetsSecondLevelName = assetsThirdLevel.assetsParentType \n" +
            ") AS assetsThirdLevelComposed LEFT JOIN AssetsType AS assetsFourthLevel ON assetsThirdLevelComposed.assetsThirdLevelName = assetsFourthLevel.assetsParentType"
            )
    List<AssetsTypeQuery> queryGroupedAssetsType();
}