package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LiabilitiesTypeDao {
    @Insert
    void insertLiabilityType(LiabilitiesType liabilitiesType);

    @Insert
    void insertLiabilityParentType(LiabilitiesType liabilitiesTypeParentType);

    @Insert
    void insertLiabilitiesTypes(LiabilitiesType... liabilitiesTypeTypes);

    @Insert
    void insertLiabilitiesTypes(List<LiabilitiesType> liabilitiesTypes);

    @Update
    void updatesLiabilityType(LiabilitiesType... liabilitiesTypeTypes);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesId LIKE :liabilitiesId")
    List<LiabilitiesType> queryLiabilitiesById (int liabilitiesId);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesName LIKE :liabilitiesName")
    List<LiabilitiesType> queryLiabilitiesByName (int liabilitiesName);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesParentType LIKE :liabilitiesParentType")
    List<LiabilitiesType> queryLiabilitiesByParentType (String liabilitiesParentType);

    @Query("SELECT * FROM LiabilitiesType")
    List<LiabilitiesType> queryAllLiabilities();

    @Query("SELECT \n" +
            "liabilitiesSecondLevelComposed.liabilitiesFirstLevelId, \n" +
            "liabilitiesSecondLevelComposed.liabilitiesFirstLevelName, \n" +
            "liabilitiesSecondLevelComposed.liabilitiesSecondLevelId, \n" +
            "liabilitiesSecondLevelComposed.liabilitiesSecondLevelName, \n" +
            "liabilitiesThirdLevel.liabilitiesId AS liabilitiesThirdLevelId, \n" +
            "liabilitiesThirdLevel.liabilitiesName AS liabilitiesThirdLevelName \n" +
            "FROM ( \n" +
            "SELECT \n" +
            "liabilitiesFirstLevel.liabilitiesId AS liabilitiesFirstLevelId, \n" +
            "liabilitiesFirstLevel.liabilitiesName AS liabilitiesFirstLevelName, \n" +
            "liabilitiesSecondLevel.liabilitiesId AS liabilitiesSecondLevelId, \n" +
            "liabilitiesSecondLevel.liabilitiesName AS liabilitiesSecondLevelName \n" +
            "FROM LiabilitiesType AS liabilitiesFirstLevel \n" +
            "JOIN LiabilitiesType AS liabilitiesSecondLevel ON liabilitiesFirstLevel.liabilitiesParentType IS NULL \n" +
            "AND liabilitiesFirstLevel.liabilitiesName = liabilitiesSecondLevel.liabilitiesParentType)" +
            "AS liabilitiesSecondLevelComposed LEFT JOIN LiabilitiesType AS liabilitiesThirdLevel \n" +
            "ON liabilitiesSecondLevelComposed.liabilitiesSecondLevelName = liabilitiesThirdLevel.liabilitiesParentType")
    List<LiabilitiesTypeQuery> queryGroupedLiabilitiesType();
}
