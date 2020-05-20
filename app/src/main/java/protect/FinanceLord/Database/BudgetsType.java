package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "BudgetsType")
public class BudgetsType {
    @PrimaryKey(autoGenerate = true)
    private int budgetsCategoryId;

    @ColumnInfo(name = "budgetsName")
    private String budgetsName;

    @Ignore
    public BudgetsType() { }

    public BudgetsType(int budgetsCategoryId, String budgetsName) {
        this.budgetsCategoryId = budgetsCategoryId;
        this.budgetsName = budgetsName;
    }

    public int getBudgetsCategoryId()   { return budgetsCategoryId;}
    public String getBudgetsName()     { return budgetsName;}

    public void setBudgetsCategoryId(int budgetsCategoryId)     { this.budgetsCategoryId = budgetsCategoryId; }
    public void setBudgetsName(String budgetsName) { this.budgetsName = budgetsName;}
}
