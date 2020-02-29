package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LiabilitiesValueDao {
    @Insert
    public void insertLiabilityValue(LiabilitiesValue liabilitiesValue);

    @Update
    public void updateLiabilityValue(LiabilitiesValue ... liabilitiesValues);

    @Query("SELECT * FROM LiabilitiesValue WHERE liabilitiesId LIKE :liabilitiesId")
    public List<LiabilitiesValue> queryLiabilitiesById (int liabilitiesId);

    @Query("SELECT * FROM LiabilitiesValue")
    public List<LiabilitiesValue> queryAllLiabilities ();
}
