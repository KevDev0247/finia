package protect.Finia.BudgetModule;

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

import protect.Finia.Database.BudgetsType;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.Database.BudgetsValue;
import protect.Finia.DAOs.BudgetsValueDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.R;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The helper class that helps to insert of update the data regarding budgets into the database.
 * The helper class helps the program to carry out basic actions on a database entity.
 * The basic actions that the helper class carries out includes:
 * Insert: insert the new budget into database.
 * Update: update an existing budget in the database.
 * Delete: delete an existing budget in the database.
 * Two entries where designed to help insert of update: Normal Entry and New Category Entry.
 * Normal Entry refers to the cases when the budget's category is already stored in the database.
 * New Category Entry refers to the cases when the budget's category are created by user and not stored in the database.
 * When the transaction is determined to be sent to the New Category Entry, a new category will also be created and inserted into the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetDatabaseHelper {

    private Context context;
    private BudgetInputWidgets inputUtils;
    private List<BudgetsType> budgetsTypes;
    private BudgetTypesViewModel viewModel;
    private BudgetsValueDao budgetsValueDao;
    private BudgetsTypeDao budgetsTypeDao;
    private BudgetsValue budgetsValue = new BudgetsValue();

    private boolean invalidInput;
    private boolean mInsert;
    private boolean mUpdate;

    private String TAG = "BudgetDatabaseUtils";

    public BudgetDatabaseHelper(Context context, BudgetInputWidgets inputUtils, BudgetTypesViewModel viewModel, List<BudgetsType> budgetsTypes) {
        this.context = context;
        this.inputUtils = inputUtils;
        this.viewModel = viewModel;
        this.budgetsTypes = budgetsTypes;

        FiniaDatabase database = FiniaDatabase.getInstance(context);
        budgetsValueDao = database.budgetsValueDao();
        budgetsTypeDao = database.budgetsTypeDao();
    }

    /**
     * Insert new budget or update an existing budget.
     * First, the data inputted into the input boxes will be retrieved.
     * If the input of a required nonnull input box is empty, an error message will be displayed.
     * Once input of all the required input boxes are valid, the data will be inserted or updated.
     * The insertion and update is completed in a separate thread to avoid locking the UI thread for a long period of time.
     *
     * @param insert indicator of whether to insert
     * @param update indicator of whether to update
     * @param budgetId the id of the transaction to be updated.
     */
    public void insertOrUpdateData(final boolean insert, final boolean update, final Integer budgetId) throws ParseException {
        mInsert = insert;
        mUpdate = update;
        invalidInput = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.CANADA);

        Log.d(TAG, "this budget's id is " + budgetId);
        if (budgetId != null) {
            budgetsValue.setBudgetsId(budgetId);
        }

        if (inputUtils.nameInput.getText().toString().isEmpty()) {
            Log.d(TAG, "no data is inputted for name, an error should be displayed ");
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

        if (!inputUtils.nameInput.getText().toString().isEmpty()) {
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
            Log.d(TAG, "no data is inputted for name, an error should be displayed ");
            inputUtils.nameInputField.setError(context.getString(R.string.budget_name_error_message));
            invalidInput = true;
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (!invalidInput && insert) {
                    budgetsValueDao.insertBudgetValue(budgetsValue);
                    Log.d(TAG, "the budget was inserted through normal entry");
                    ((Activity) context).finish();
                } else if (!invalidInput && update) {
                    budgetsValueDao.updateBudgetValue(budgetsValue);
                    Log.d(TAG, "the budget was updated through normal entry is");
                    ((Activity) context).finish();
                } else {
                    Log.d(TAG, "the budget has some null values");
                }
            }
        });
    }

    /**
     * Delete an existing budget.
     * The deletion is completed in a separate thread to avoid locking the UI thread for a long period of time.
     *
     * @param budgetId the id of the transaction to be deleted.
     */
    public void deleteData(final int budgetId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                budgetsValueDao.deleteIndividualBudget(budgetId);
                ((Activity) context).finish();
            }
        });
    }

    /**
     * Add text listeners to input boxes to monitor the input boxes.
     * Once, the empty input boxes are filled, the error message will disappear.
     */
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
                if (inputUtils.valueInputField.isErrorEnabled()) {
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

    /**
     * Add the new category that the user created into the database.
     * After the new category is inserted, it will also be pushed to the view model to notify the activity to update the filters.
     * Then, the new category will be passed to the method to insert or update through a New Category Entry.
     * All insertions or updates are completed in a separate thread to avoid locking the UI thread for a long period of time.
     */
    private void addNewCategoryToDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                boolean inputError = false;
                BudgetsType newType = new BudgetsType();
                newType.setBudgetsName(inputUtils.nameInput.getText().toString());
                budgetsTypeDao.insertIndividualBudgetType(newType);

                Log.d(TAG, " the new category's name is set to " + inputUtils.nameInput.getText().toString());
                final List<BudgetsType> allBudgetTypes = budgetsTypeDao.queryAllBudgetsTypes();
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG + " Insert Utilities", " data has changed, ready to push to view model");
                        viewModel.pushToBudgetTypes(allBudgetTypes);
                    }
                });

                for (BudgetsType budgetsType : allBudgetTypes) {
                    if (budgetsType.getBudgetsName().equals(inputUtils.nameInput.getText().toString())) {
                        Log.d(TAG, " the new category's name is " + budgetsType.getBudgetsName() + " id is " + budgetsType.getBudgetsCategoryId());
                        inputError = insertOrUpdateWithNewCategory(budgetsType.getBudgetsCategoryId());
                    }
                }

                if (!inputError) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    /**
     * Insert or update in the New Category Entry.
     *
     * @param budgetsCategoryId the id of the new category of the transaction to be inserted or updated.
     * @return whether the transaction has some input errors
     */
    private boolean insertOrUpdateWithNewCategory(int budgetsCategoryId) {
        budgetsValue.setBudgetsCategoryId(budgetsCategoryId);
        if (!invalidInput && mInsert) {
            budgetsValueDao.insertBudgetValue(budgetsValue);
            Log.d(TAG, "the budget was inserted through new category entry");
            return false;
        } else if (!invalidInput && mUpdate) {
            budgetsValueDao.updateBudgetValue(budgetsValue);
            Log.d(TAG, "the budget was updated through new category entry & new category is" + budgetsValue.getBudgetsId());
            return false;
        } else {
            Log.d(TAG, "the budget has some null values");
            return true;
        }
    }
}
