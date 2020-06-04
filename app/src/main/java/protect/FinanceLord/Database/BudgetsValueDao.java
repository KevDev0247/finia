package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BudgetsValueDao {
    @Insert
    void insertBudgetValue(BudgetsValue budgetValue);

    @Update
    void updateBudgetValue(BudgetsValue... budgetValue);

    @Query("SELECT * FROM BudgetsValue WHERE budgetsId LIKE :budgetsId")
    List<BudgetsValue> queryBudgetsById (int budgetsId);

    @Query("SELECT * FROM BudgetsValue WHERE budgetValue LIKE :budgetsValue")
    List<BudgetsValue> queryBudgetsByValue (float budgetsValue);

    @Query("SELECT * FROM BudgetsValue WHERE budgetCategoryId LIKE :budgetsCategoryId")
    List<BudgetsValue> queryBudgetsByCategoryId (int budgetsCategoryId);

    @Query("SELECT * FROM BudgetsValue WHERE dateStart Like :dateStart")
    List<BudgetsValue> queryBudgetsByDateStart (Long dateStart);

    @Query("SELECT * FROM BudgetsValue WHERE dateEnd Like :dateEnd")
    List<BudgetsValue> queryBudgetsByDateEnd (Long dateEnd);

    @Query("SELECT * FROM BudgetsValue")
    List<BudgetsValue> queryAllBudgets ();
}
