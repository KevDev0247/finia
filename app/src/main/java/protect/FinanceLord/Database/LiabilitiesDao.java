package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LiabilitiesDao {
    @Insert
    void insertLiabilityType(Liabilities liabilitiesType);

    @Insert
    void insertLiabilityParentType(Liabilities liabilitiesParentType);

    @Insert
    void insertLiabilitiesTypes(Liabilities ... liabilitiesTypes);

    @Update
    void updatesLiabilityType(Liabilities ... liabilitiesTypes);

    @Query("SELECT * FROM Liabilities WHERE liabilitiesId LIKE :liabilitiesId")
    List<Liabilities> queryLiabilitiesById (String liabilitiesId);

    @Query("SELECT * FROM Liabilities WHERE liabilitiesName LIKE :liabilitiesName")
    List<Liabilities> queryLiabilitiesByName (String liabilitiesName);

    @Query("SELECT * FROM Liabilities WHERE liabilitiesParentType LIKE :liabilitiesParentType")
    List<Liabilities> queryLiabilitiesByParentType (String liabilitiesParentType);

    @Query("SELECT * FROM Liabilities")
    List<Liabilities> queryAllLiabilities();
}
