package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface BudgetsDao {
    @Insert
    void insertBudgetValue(Budgets budgetValue);

    @Update
    void updateBudgetValue(Budgets ... budgetValue);

    @Insert
    void insertDateStart(Budgets budgetDateStart);

    @Update
    void updateDateStart(Budgets ... budgetDateStart);

    @Insert
    void insertDateEnd(Budgets budgetDateEnd);

    @Update
    void updateDataEnd(Budgets ... budgetDateEnd);

    @Query("SELECT * FROM Budgets WHERE budgetsId LIKE :budgetsId")
    List<Budgets> queryBudgetsById (String budgetsId);

    @Query("SELECT * FROM Budgets WHERE budgetCategoryId LIKE :budgetsCategoryId")
    List<Budgets> queryBudgetsByCategoryId (String budgetsCategoryId);

    @Query("SELECT * FROM Budgets WHERE dateStart Like :dateStart")
    List<Budgets> queryBudgetsByDateStart (Long dateStart);

    @Query("SELECT * FROM Budgets WHERE dateEnd Like :dateEnd")
    List<Budgets> queryBudgetsByDateEnd (Long dateEnd);

    @Query("SELECT * FROM Budgets")
    List<Budgets> queryAllBudgets ();
}
