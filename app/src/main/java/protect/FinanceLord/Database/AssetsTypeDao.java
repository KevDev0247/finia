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

    @Query("SELECT \n" +
            "assetsThirdLevelComposed.*, \n" +
            "assetsFourthLevel.assetsId as assetsFourthLevelId, \n" +
            "assetsFourthLevel.assetsName as assetsFourthLevelName \n" +
            "from ( \n" +
            "select \n" +
            "assetsSecondLevelComposed.assetsFirstLevelId, \n" +
            "assetsSecondLevelComposed.assetsFirstLevelName, \n" +
            "assetsSecondLevelComposed.assetsSecondLevelId, \n" +
            "assetsSecondLevelComposed.assetsSecondLevelName, \n" +
            "assetsThirdLevel.assetsId as assetsThirdLevelId, \n" +
            "assetsThirdLevel.assetsName as assetsThirdLevelName \n" +
            "FROM( \n" +
            "select \n" +
            "assetsFirstLevel.assetsId as assetsFirstLevelId, \n" +
            "assetsFirstLevel.assetsName as assetsFirstLevelName, \n" +
            "assetsSecondLevel.assetsId as assetsSecondLevelId, \n" +
            "assetsSecondLevel.assetsName as assetsSecondLevelName \n" +
            "from AssetsType as assetsFirstLevel \n" +
            "join AssetsType as assetsSecondLevel on assetsFirstLevel.assetsParentType is NULL \n" +
            "and assetsFirstLevel.assetsName = assetsSecondLevel.assetsParentType \n" +
            ") as assetsSecondLevelComposed \n" +
            "left join AssetsType as assetsThirdLevel on assetsSecondLevelComposed.assetsSecondLevelName = assetsThirdLevel.assetsParentType \n" +
            ") as assetsThirdLevelComposed left join AssetsType as assetsFourthLevel on assetsThirdLevelComposed.assetsThirdLevelName = assetsFourthLevel.assetsParentType"
            )
    List<AssetsTypeQuery> queryGroupedAssetsType();
}