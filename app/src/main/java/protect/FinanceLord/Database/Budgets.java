package protect.FinanceLord.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Budgets")
public class Budgets {
    @PrimaryKey(autoGenerate = true)
    private int budgetsId;

    @ColumnInfo(name = "budgetValue")
    private float budgetsValue;

    @ColumnInfo(name = "budgetCategoryId")
    private int budgetsCategoryId;

    @ColumnInfo(name = "DateStart")
    private Date dateStart;

    @ColumnInfo(name = "DateEnd")
    private Date dateEnd;

    @Ignore
    public Budgets(){

    }

    public Budgets(int budgetsId,float budgetsValue,int budgetsCategoryId,Date dateStart,Date dateEnd){
        this.budgetsId = budgetsId;
        this.budgetsValue = budgetsValue;
        this.budgetsCategoryId = budgetsCategoryId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public int getBudgetsId()       {return budgetsId;}
    public float getBudgetsValue()      {return budgetsValue;}
    public int getBudgetsCategoryId()       {return budgetsCategoryId;}
    public Date getDateStart()      {return dateStart;}
    public Date getDateEnd()        {return dateEnd;}

    public void setBudgetsId(int budgetsId)     {this.budgetsId = budgetsId;}
    public void setBudgetsValue(float budgetsValue)     {this.budgetsValue = budgetsValue;}
    public void setBudgetsCategoryId(int budgetsCategoryId)     {this.budgetsCategoryId = budgetsCategoryId;}
    public void setDateStart(Date dateStart)    {this.dateStart = dateStart;}
    public void setDateEnd(Date dateEnd)    {this.dateEnd = dateEnd;}
}
