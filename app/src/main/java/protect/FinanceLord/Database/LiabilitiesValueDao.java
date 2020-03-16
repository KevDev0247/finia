package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LiabilitiesValueDao {
    @Insert
    void insertLiabilityId (LiabilitiesValue liabilityId);

    @Update
    void updateLiabilityId (LiabilitiesValue ... liabilityId);

    @Insert
    void insertLiabilityValue (LiabilitiesValue liabilitiesValue);

    @Update
    void updateLiabilityValue (LiabilitiesValue ... liabilitiesValue);

    @Insert
    void insertLiabilityDate (AssetsValue date);

    @Update
    void updateLiabilityDate (AssetsValue ... date);

    @Query("SELECT * FROM LiabilitiesValue WHERE liabilitiesId LIKE :liabilityId")
    List<LiabilitiesValue> queryLiabilitiesById (int liabilityId);

    @Query("SELECT * FROM LiabilitiesValue WHERE liabilitiesValue LIKE :liabilitiesValue")
    List<LiabilitiesValue> queryLiabilitiesByValue (int liabilitiesValue);

    @Query("SELECT * FROM LiabilitiesValue WHERE date LIKE :date")
    List<LiabilitiesValue> queryLiabilitiesByDate (Long date);

    @Query("SELECT * FROM LiabilitiesValue")
    List<LiabilitiesValue> queryAllLiabilities();
}
