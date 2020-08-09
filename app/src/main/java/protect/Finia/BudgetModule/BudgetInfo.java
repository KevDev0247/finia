package protect.Finia.BudgetModule;

/**
 * The class to store the data of the budget queried from the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetInfo {
    public int budgetId;
    public int budgetCategoryId;
    public float budgetTotal;
    public float totalUsage;
    public Long dateStart;
    public Long dateEnd;
}
