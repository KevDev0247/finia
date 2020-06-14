package protect.FinanceLord.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import protect.FinanceLord.Database.LiabilitiesValue;

@Dao
public interface LiabilitiesValueDao {

    @Insert
    void insertLiabilityValue (LiabilitiesValue liabilitiesValue);

    @Update
    void updateLiabilityValue (LiabilitiesValue ... liabilitiesValue);

    @Query("SELECT * FROM LiabilitiesValue WHERE liabilitiesPrimaryId = :liabilityPrimaryId")
    List<LiabilitiesValue> queryLiabilitiesById (int liabilityPrimaryId);

    @Query("SELECT * FROM LiabilitiesValue WHERE date <= :dateEnd AND date >= :dateStart")
    List<LiabilitiesValue> queryLiabilitiesByTimePeriod (Long dateStart, Long dateEnd);

    @Query("SELECT * FROM LiabilitiesValue")
    List<LiabilitiesValue> queryAllLiabilities();

    @Query("SELECT * FROM LiabilitiesValue WHERE liabilitiesId = :liabilityId ORDER BY date DESC LIMIT 1")
    LiabilitiesValue queryLatestIndividualLiability (int liabilityId);

    @Query("SELECT * FROM LiabilitiesValue WHERE date = :date AND liabilitiesId = :liabilityId")
    LiabilitiesValue queryIndividualLiabilityByTime(Long date, int liabilityId);

    @Query("SELECT * FROM LiabilitiesValue WHERE date < :date AND liabilitiesId = :liabilityId ORDER BY date DESC  LIMIT 1")
    LiabilitiesValue queryPreviousLiabilityBeforeTime(Long date, int liabilityId);

    @Query("DELETE FROM LiabilitiesValue WHERE date = :date")
    void deleteLiabilitiesAtDate(Long date);
}
