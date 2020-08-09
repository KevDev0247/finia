package protect.Finia.TransactionModule;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.Finia.Database.BudgetsType;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.Database.Transactions;
import protect.Finia.DAOs.TransactionsDao;
import protect.Finia.R;
import protect.Finia.TimeUtils.TimeProcessor;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The helper class that helps to insert of update the data regarding transactions into the database.
 * The helper class helps the program to carry out basic actions on a database entity.
 * The basic actions that the helper class carries out includes:
 * Insert: insert the new transaction into database.
 * Update: update an existing transaction in the database.
 * Delete: delete an existing transaction in the database.
 * Two entries where designed to help insert of update: Normal Entry and New Category Entry.
 * Normal Entry refers to the cases when the transaction's category is already stored in the database.
 * New Category Entry refers to the cases when the transaction's category are created by user and not stored in the database.
 * When the transaction is determined to be sent to the New Category Entry, a new category will also be created and inserted into the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionDatabaseHelper {

    private Context context;
    private String TAG;
    private Date currentTime;
    private TransactionInputWidgets inputUtils;
    private List<BudgetsType> budgetsTypes;
    private BudgetTypesViewModel viewModel;
    private TransactionsDao transactionsDao;
    private BudgetsTypeDao budgetsTypeDao;
    private Transactions transaction = new Transactions();

    private boolean invalidInput;
    private boolean mInsert;
    private boolean mUpdate;

    public TransactionDatabaseHelper(Context context, Date currentTime, TransactionInputWidgets inputUtils, List<BudgetsType> budgetsTypes, BudgetTypesViewModel viewModel, String TAG) {
        this.context = context;
        this.currentTime = currentTime;
        this.inputUtils = inputUtils;
        this.budgetsTypes = budgetsTypes;
        this.viewModel = viewModel;
        this.TAG = TAG;

        FiniaDatabase database = FiniaDatabase.getInstance(context);
        transactionsDao = database.transactionsDao();
        budgetsTypeDao = database.budgetsTypeDao();
    }

    /**
     * Insert new transaction or update an existing transaction.
     * First, the data inputted into the input boxes will be retrieved.
     * If the input of a required nonnull input box is empty, an error message will be displayed.
     * Once input of all the required input boxes are valid, the data will be inserted or updated.
     * The insertion and update is completed in a separate thread to avoid locking the UI thread for a long period of time.
     *
     * @param insert indicator of whether to insert
     * @param update indicator of whether to update
     * @param transactionId the id of the transaction to be updated.
     */
    public void insertOrUpdateData(final boolean insert, final boolean update, final Integer transactionId) {
        mInsert = insert;
        mUpdate = update;
        invalidInput = false;

        Log.d(TAG, "this transaction's id is " + transactionId);
        if (transactionId != null) {
            transaction.setTransactionId(transactionId);
        }

        if (!inputUtils.nameInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's name is " + inputUtils.nameInput.getText());
            transaction.setTransactionName(inputUtils.nameInput.getText().toString());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.nameInputField.setError(context.getString(R.string.transaction_name_error_message));
            invalidInput = true;
        }

        if (!inputUtils.valueInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's value is " + inputUtils.valueInput.getText());
            if (TAG.equals(context.getString(R.string.revenues_section_key))) {
                transaction.setTransactionValue(Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
            } else if (TAG.equals(context.getString(R.string.expenses_section_key))) {
                transaction.setTransactionValue( - Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.valueInputField.setError(context.getString(R.string.transaction_value_error_message));
            invalidInput = true;
        }

        if (!inputUtils.commentInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's comment is " + inputUtils.commentInput.getText());
            transaction.setTransactionComments(inputUtils.commentInput.getText().toString());
        } else {
            transaction.setTransactionComments(null);
        }

        if (!inputUtils.dateInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's date is " + inputUtils.dateInput.getText());
            try {
                transaction.setDate(TimeProcessor.parseDateString(inputUtils.dateInput.getText().toString(), context.getString(R.string.date_format)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            transaction.setDate(currentTime.getTime());
        }

        if (!inputUtils.categoryInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's category is " + inputUtils.categoryInput.getText());
            for (BudgetsType budgetsType : budgetsTypes) {
                transaction.setTransactionCategoryId(0);
                Log.d(TAG, "this data model item is " + budgetsType.getBudgetsName());
                if (budgetsType.getBudgetsName().equals(inputUtils.categoryInput.getText().toString())) {
                    Log.d(TAG, " find the match ");
                    transaction.setTransactionCategoryId(budgetsType.getBudgetsCategoryId());
                    break;
                }
            }
            if (transaction.getTransactionCategoryId() == 0 && !invalidInput) {
                Log.d(TAG, " a new category is created");
                addNewCategoryToDatabase();
                return;
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.categoryInputField.setError(context.getString(R.string.transaction_category_error_message));
            invalidInput = true;
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (!invalidInput && insert) {
                    transactionsDao.insertTransaction(transaction);
                    Log.d(TAG, "the transaction was inserted through normal entry");
                    ((Activity) context).finish();
                } else if (!invalidInput && update) {
                    transactionsDao.updateTransaction(transaction);
                    Log.d(TAG, "the transaction was updated through normal entry");
                    ((Activity) context).finish();
                } else {
                    Log.d(TAG, "the transaction has some null values");
                }
            }
        });
    }

    /**
     * Delete an existing transaction.
     * The deletion is completed in a separate thread to avoid locking the UI thread for a long period of time.
     *
     * @param transactionId the id of the transaction to be deleted.
     */
    public void deleteData(final int transactionId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                transactionsDao.deleteIndividualTransaction(transactionId);
                ((Activity) context).finish();
            }
        });
    }

    /**
     * Add text listeners to input boxes to monitor the input boxes.
     * Once, the empty input boxes are filled, the error message will disappear.
     */
    public void addTextListener() {
        inputUtils.categoryInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.categoryInputField.isErrorEnabled()) {
                    inputUtils.categoryInputField.setErrorEnabled(false);
                }
            }
        });

        inputUtils.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputUtils.nameInputField.isErrorEnabled()){
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
                if (inputUtils.valueInputField.isErrorEnabled()){
                    inputUtils.valueInputField.setErrorEnabled(false);
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
                newType.setBudgetsName(inputUtils.categoryInput.getText().toString());
                budgetsTypeDao.insertIndividualBudgetType(newType);

                Log.d(TAG, " the new category's name is set to " + inputUtils.categoryInput.getText().toString());
                final List<BudgetsType> allBudgetTypes = budgetsTypeDao.queryAllBudgetsTypes();
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG + " Insert Utilities", " data has changed, ready to push to view model");
                        viewModel.pushToBudgetTypes(allBudgetTypes);
                    }
                });

                for (BudgetsType budgetsType : allBudgetTypes) {
                    if (budgetsType.getBudgetsName().equals(inputUtils.categoryInput.getText().toString())) {
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
        transaction.setTransactionCategoryId(budgetsCategoryId);
        if (!invalidInput && mInsert) {
            transactionsDao.insertTransaction(transaction);
            Log.d(TAG, "the transaction was inserted through new category entry");
            return false;
        } else if (!invalidInput && mUpdate) {
            transactionsDao.updateTransaction(transaction);
            Log.d(TAG, "the transaction was updated through new category entry & new category is" + transaction.getTransactionCategoryId());
            return false;
        } else {
            Log.d(TAG, "the transaction has some null values");
            return true;
        }
    }
}
