package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BudgetsTypeDao {

    @Insert
    void insertIndividualBudgetType(BudgetsType budgetType);

    @Insert
    void insertBudgetsTypes(List<BudgetsType> budgetsTypes);

    @Query("SELECT * FROM BudgetsType")
    List<BudgetsType> queryAllBudgetsTypes();
}
