package protect.FinanceLord.BudgetUtils;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.Database.BudgetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;

public class BudgetDatabaseUtils {

    private Context context;
    private Date startTime;
    private Date endTime;
    private BudgetInputUtils inputUtils;
    private List<BudgetsType> budgetsTypes;
    private BudgetsValueDao budgetsValueDao;
    private BudgetsValue budgetsValue;

    private boolean nullValue = false;
    private boolean mInsert;
    private boolean mUpdate;

    private String TAG = "BudgetDatabaseUtils";

    public BudgetDatabaseUtils(Context context, Date startTime, Date endTime, BudgetInputUtils inputUtils, List<BudgetsType> budgetsTypes) {
        this.context = context;
        this.inputUtils = inputUtils;
        this.budgetsTypes = budgetsTypes;
        this.startTime = startTime;
        this.endTime = endTime;

        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        budgetsValueDao = database.budgetsValueDao();
    }

    public void insertOrUpdateData(final boolean insert, final boolean update, final Integer budgetId) {
        mInsert = insert;
        mUpdate = update;

        if (!inputUtils.nameInput.getText().toString().isEmpty()){
            for (BudgetsType budgetsType : budgetsTypes) {
                budgetsValue.setBudgetsCategoryId(0);
                if (budgetsType.getBudgetsName().equals(inputUtils.nameInput.getText().toString())) {
                    Log.d(TAG, " find the match ");
                    budgetsValue.setBudgetsCategoryId(budgetsType.getBudgetsCategoryId());
                    break;
                }
            }

            if (budgetsValue.getBudgetsCategoryId() == 0) {
                Log.d(TAG, " a new category is created");
                addNewCategoryToDatabase();
                return;
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.nameInput.setError(context.getString(R.string.budget_name_error_message));
            nullValue = true;
        }

        if (!inputUtils.valueInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's value is " + inputUtils.valueInput.getText());
            budgetsValue.setBudgetsValue(Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.valueInput.setError(context.getString(R.string.budget_value_error_message));
            nullValue = true;
        }

        if (!inputUtils.startDateInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's start date is " + inputUtils.startDateInput.getText());
            budgetsValue.setDateStart(startTime.getTime());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.startDateInput.setError(context.getString(R.string.budget_start_date_error_message));
            nullValue = true;
        }

        if (!inputUtils.endDateInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's end date is " + inputUtils.endDateInput.getText());
            budgetsValue.setDateStart(endTime.getTime());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.endDateInput.setError(context.getString(R.string.budget_end_date_error_message));
            nullValue = true;
        }

        if (budgetId != null) {
            budgetsValue.setBudgetsId(budgetId);
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (!nullValue && insert) {
                    budgetsValueDao.insertBudgetValue(budgetsValue);
                } else if (!nullValue && update) {
                    budgetsValueDao.updateBudgetValue(budgetsValue);
                } else {
                    Log.d(TAG, "the transaction has some null values");
                }
            }
        });
    }

    public void addTextListener() {

    }

    private void addNewCategoryToDatabase() {

    }

    private void insertBudgetWithNewCategory() {

    }
}
