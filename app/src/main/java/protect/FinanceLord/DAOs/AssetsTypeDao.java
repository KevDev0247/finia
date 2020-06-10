package protect.FinanceLord.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import protect.FinanceLord.Database.AssetsType;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeLeaf_Assets;

@Dao
public interface AssetsTypeDao {

    @Insert
    void insertAssetsTypes(List<AssetsType> assetsTypes);

    @Query("SELECT * FROM AssetsType WHERE assetsId = :assetsId")
    List<AssetsType> queryAssetsById (int assetsId);

    @Query("SELECT * FROM AssetsType WHERE assetsParentType = :assetsParentType")
    List<AssetsType> queryAssetsByParentType (String assetsParentType);

    @Query("SELECT \n" +
            "  assetsThirdLevelComposed.*, \n" +
            "  assetsFourthLevel.assetsId AS assetsFourthLevelId, \n" +
            "  assetsFourthLevel.assetsName AS assetsFourthLevelName \n" +
            "FROM ( \n" +
            "  SELECT \n" +
            "    assetsSecondLevelComposed.assetsFirstLevelId, \n" +
            "    assetsSecondLevelComposed.assetsFirstLevelName, \n" +
            "    assetsSecondLevelComposed.assetsSecondLevelId, \n" +
            "    assetsSecondLevelComposed.assetsSecondLevelName, \n" +
            "    assetsThirdLevel.assetsId AS assetsThirdLevelId, \n" +
            "    assetsThirdLevel.assetsName AS assetsThirdLevelName \n" +
            "  FROM( \n" +
            "    SELECT \n" +
            "      assetsFirstLevel.assetsId AS assetsFirstLevelId, \n" +
            "      assetsFirstLevel.assetsName AS assetsFirstLevelName, \n" +
            "      assetsSecondLevel.assetsId AS assetsSecondLevelId, \n" +
            "      assetsSecondLevel.assetsName AS assetsSecondLevelName \n" +
            "    FROM AssetsType AS assetsFirstLevel \n" +
            "    JOIN AssetsType AS assetsSecondLevel \n" +
            "    ON assetsFirstLevel.assetsParentType IS NULL \n" +
            "    AND assetsFirstLevel.assetsName = assetsSecondLevel.assetsParentType) \n" +
            "  AS assetsSecondLevelComposed \n" +
            "  LEFT JOIN AssetsType AS assetsThirdLevel ON assetsSecondLevelComposed.assetsSecondLevelName = assetsThirdLevel.assetsParentType) \n" +
            "AS assetsThirdLevelComposed LEFT JOIN AssetsType AS assetsFourthLevel \n" +
            "ON assetsThirdLevelComposed.assetsThirdLevelName = assetsFourthLevel.assetsParentType")
    List<TypeTreeLeaf_Assets> queryAssetsTypeTreeAsList();

    @Query("SELECT * FROM AssetsType")
    List<AssetsType> queryAllAssetsType();

    @Query("SELECT * FROM AssetsType WHERE assetsName LIKE :assetsName")
    AssetsType queryAssetsByType (String assetsName);
}