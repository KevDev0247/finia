package protect.FinanceLord.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import protect.FinanceLord.Database.BudgetsValue;

@Dao
public interface BudgetsValueDao {
    @Insert
    void insertBudgetValue(BudgetsValue budgetValue);

    @Update
    void updateBudgetValue(BudgetsValue... budgetValue);

    @Query("SELECT * FROM BudgetsValue")
    List<BudgetsValue> queryAllBudgets ();

    @Query("DELETE FROM BudgetsValue WHERE budgetsId = :budgetId")
    void deleteIndividualBudget(int budgetId);
}
