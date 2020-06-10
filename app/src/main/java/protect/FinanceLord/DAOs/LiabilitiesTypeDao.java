package protect.FinanceLord.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import protect.FinanceLord.Database.LiabilitiesType;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeLeaf_Liabilities;

@Dao
public interface LiabilitiesTypeDao {

    @Insert
    void insertLiabilitiesTypes(List<LiabilitiesType> liabilitiesTypes);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesId = :liabilitiesId")
    List<LiabilitiesType> queryLiabilitiesById (int liabilitiesId);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesParentType = :liabilitiesParentType")
    List<LiabilitiesType> queryLiabilitiesByParentType (String liabilitiesParentType);

    @Query("SELECT \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesFirstLevelId, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesFirstLevelName, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesSecondLevelId, \n" +
            "  liabilitiesSecondLevelComposed.liabilitiesSecondLevelName, \n" +
            "  liabilitiesThirdLevel.liabilitiesId AS liabilitiesThirdLevelId, \n" +
            "  liabilitiesThirdLevel.liabilitiesName AS liabilitiesThirdLevelName \n" +
            "FROM ( \n" +
            "  SELECT \n" +
            "    liabilitiesFirstLevel.liabilitiesId AS liabilitiesFirstLevelId, \n" +
            "    liabilitiesFirstLevel.liabilitiesName AS liabilitiesFirstLevelName, \n" +
            "    liabilitiesSecondLevel.liabilitiesId AS liabilitiesSecondLevelId, \n" +
            "    liabilitiesSecondLevel.liabilitiesName AS liabilitiesSecondLevelName \n" +
            "  FROM LiabilitiesType AS liabilitiesFirstLevel \n" +
            "  JOIN LiabilitiesType AS liabilitiesSecondLevel \n" +
            "  ON liabilitiesFirstLevel.liabilitiesParentType IS NULL \n" +
            "  AND liabilitiesFirstLevel.liabilitiesName = liabilitiesSecondLevel.liabilitiesParentType)" +
            "AS liabilitiesSecondLevelComposed LEFT JOIN LiabilitiesType AS liabilitiesThirdLevel \n" +
            "ON liabilitiesSecondLevelComposed.liabilitiesSecondLevelName = liabilitiesThirdLevel.liabilitiesParentType")
    List<TypeTreeLeaf_Liabilities> queryLiabilitiesTypeTreeAsList();

    @Query("SELECT * FROM LiabilitiesType")
    List<LiabilitiesType> queryAllLiabilities();

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesName = :liabilitiesName")
    LiabilitiesType queryLiabilitiesByType (String liabilitiesName);
}
