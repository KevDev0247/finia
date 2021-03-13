package protect.Finia.budget;

/**
 * The class to store the data of the budget queried from the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/06/05
 */
public class BudgetInfo {
    public int budgetId;
    public int budgetCategoryId;
    public float budgetTotal;
    public float totalUsage;
    public Long dateStart;
    public Long dateEnd;
}
