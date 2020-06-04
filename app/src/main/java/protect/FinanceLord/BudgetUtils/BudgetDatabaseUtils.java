package protect.FinanceLord.BudgetUtils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.Database.BudgetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;

public class BudgetDatabaseUtils {

    private Context context;
    private BudgetInputUtils inputUtils;
    private List<BudgetsType> budgetsTypes;
    private BudgetsValueDao budgetsValueDao;
    private BudgetsTypeDao budgetsTypeDao;
    private BudgetsValue budgetsValue = new BudgetsValue();

    private boolean invalidInput = false;
    private boolean mInsert;
    private boolean mUpdate;

    private String TAG = "BudgetDatabaseUtils";

    public BudgetDatabaseUtils(Context context, BudgetInputUtils inputUtils, List<BudgetsType> budgetsTypes) {
        this.context = context;
        this.inputUtils = inputUtils;
        this.budgetsTypes = budgetsTypes;

        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        budgetsValueDao = database.budgetsValueDao();
        budgetsTypeDao = database.budgetsTypeDao();
    }

    public void insertOrUpdateData(final boolean insert, final boolean update, final Integer budgetId) throws ParseException {
        mInsert = insert;
        mUpdate = update;
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.CANADA);

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
            inputUtils.nameInputField.setError(context.getString(R.string.budget_name_error_message));
            invalidInput = true;
        }

        if (!inputUtils.valueInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's value is " + inputUtils.valueInput.getText());
            budgetsValue.setBudgetsValue(Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.valueInputField.setError(context.getString(R.string.budget_value_error_message));
            invalidInput = true;
        }

        if (!inputUtils.startDateInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's start date is " + inputUtils.startDateInput.getText());
            budgetsValue.setDateStart(dateFormat.parse(inputUtils.startDateInput.getText().toString()).getTime());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.startDateInputField.setError(context.getString(R.string.budget_start_date_error_message));
            invalidInput = true;
        }

        if (!inputUtils.endDateInput.getText().toString().isEmpty()) {
            Log.d(TAG, "the budget's end date is " + inputUtils.endDateInput.getText());
            budgetsValue.setDateEnd(dateFormat.parse(inputUtils.endDateInput.getText().toString()).getTime());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.endDateInputField.setError(context.getString(R.string.budget_end_date_error_message));
            invalidInput = true;
        }

        Date startDate = dateFormat.parse(inputUtils.startDateInput.getText().toString());
        Date endDate = dateFormat.parse(inputUtils.endDateInput.getText().toString());
        if (endDate.compareTo(startDate) < 0) {
            inputUtils.startDateInputField.setError(context.getString(R.string.date_order_error_message));
            inputUtils.endDateInputField.setError(context.getString(R.string.date_order_error_message));
            invalidInput = true;
        }

        Log.d(TAG, "this transaction's id is " + budgetId);
        if (budgetId != null) {
            budgetsValue.setBudgetsId(budgetId);
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (!invalidInput && insert) {
                    budgetsValueDao.insertBudgetValue(budgetsValue);
                    ((Activity) context).finish();
                } else if (!invalidInput && update) {
                    budgetsValueDao.updateBudgetValue(budgetsValue);
                    ((Activity) context).finish();
                } else {
                    Log.d(TAG, "the budget has some null values");
                }
            }
        });
    }

    public void addTextListener() {
        inputUtils.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.nameInputField.isErrorEnabled()) {
                    inputUtils.nameInputField.setErrorEnabled(false);
                }
            }
        });

        inputUtils.valueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.nameInputField.isErrorEnabled()) {
                    inputUtils.valueInputField.setErrorEnabled(false);
                }
            }
        });

        inputUtils.startDateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.startDateInputField.isErrorEnabled()) {
                    inputUtils.startDateInputField.setErrorEnabled(false);
                }
            }
        });

        inputUtils.endDateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.endDateInputField.isErrorEnabled()) {
                    inputUtils.endDateInputField.setErrorEnabled(false);
                }
            }
        });
    }

    private void addNewCategoryToDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                BudgetsType newType = new BudgetsType();
                newType.setBudgetsName(inputUtils.nameInput.getText().toString());
                budgetsTypeDao.insertIndividualBudgetType(newType);

                Log.d(TAG, " the new category's name is set to " + inputUtils.nameInput.getText().toString());
                final List<BudgetsType> allBudgetTypes = budgetsTypeDao.queryAllBudgetsTypes();
                for (BudgetsType budgetsType : allBudgetTypes) {
                    if (budgetsType.getBudgetsName().equals(inputUtils.nameInput.getText().toString())) {
                        Log.d(TAG, " the new category's name is " + budgetsType.getBudgetsName() + " id is " + budgetsType.getBudgetsCategoryId());

                        insertOrUpdateWithNewCategory(budgetsType.getBudgetsCategoryId());
                    }
                }

                ((Activity) context).finish();
            }
        });
    }

    private void insertOrUpdateWithNewCategory(int budgetsCategoryId) {
        budgetsValue.setBudgetsId(budgetsCategoryId);
        if (!invalidInput && mInsert) {
            budgetsValueDao.insertBudgetValue(budgetsValue);
        } else if (!invalidInput && mUpdate) {
            budgetsValueDao.updateBudgetValue(budgetsValue);
        } else {
            Log.d(TAG, "the budget has some null values");
        }
    }
}
