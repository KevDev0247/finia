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

    @Query("select secondLevel.secondLevelId, \n" +
            "secondLevel.assetsCategory, \n" +
            "secondLevel.assetsSubType, \n" +
            "thirdLevel.assetsId as thirdLevelId, \n" +
            "thirdLevel.assetsName \n" +
            "from (\n" +
            "\tselect assetsCategory.assetsId as secondLevelId, \n" +
            "\tassetsCategory.assetsName as assetsCategory, \n" +
            "\tassetsSubType.assetsName as assetsSubType \n" +
            "\tfrom AssetsType as assetsCategory \n" +
            "\tjoin AssetsType as assetsSubType on assetsCategory.assetsParentType is NULL \n" +
            "\tand assetsSubType.assetsParentType = assetsCategory.assetsName\n" +
            "\t) as secondLevel \n" +
            "\tleft join AssetsType as thirdLevel on secondLevel.assetsSubType = thirdLevel.assetsParentType ")
    List<AssetsTypeQuery> queryGroupedAssetsType();
}