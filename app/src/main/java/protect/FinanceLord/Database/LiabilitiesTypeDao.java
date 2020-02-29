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
    void insertLiabilityParentType(LiabilitiesType liabilitiesParentType);

    @Insert
    void insertLiabilitiesTypes(LiabilitiesType ... liabilitiesTypes);

    @Update
    void updatesLiabilityType(LiabilitiesType ... liabilitiesTypes);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesId LIKE :liabilitiesId")
    List<LiabilitiesType> queryLiabilitiesById (String liabilitiesId);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesName LIKE :liabilitiesName")
    List<LiabilitiesType> queryLiabilitiesByName (String liabilitiesName);

    @Query("SELECT * FROM LiabilitiesType WHERE liabilitiesParentType LIKE :liabilitiesParentType")
    List<LiabilitiesType> queryLiabilitiesByParentType (String liabilitiesParentType);

    @Query("SELECT * FROM LiabilitiesType")
    List<LiabilitiesType> queryAllLiabilities();
}
