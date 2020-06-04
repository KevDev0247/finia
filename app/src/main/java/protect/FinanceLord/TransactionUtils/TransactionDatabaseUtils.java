package protect.FinanceLord.TransactionUtils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;
import protect.FinanceLord.R;
import protect.FinanceLord.ViewModels.BudgetTypesViewModel;

public class TransactionDatabaseUtils {

    private Context context;
    private String TAG;
    private Date currentTime;
    private TransactionInputUtils inputUtils;
    private List<BudgetsType> budgetsTypes;
    private BudgetTypesViewModel viewModel;
    private TransactionsDao transactionsDao;
    private BudgetsTypeDao budgetsTypeDao;
    private Transactions transaction = new Transactions();

    private boolean nullValue = false;
    private boolean mInsert;
    private boolean mUpdate;

    public TransactionDatabaseUtils(Context context, Date currentTime, TransactionInputUtils inputUtils, List<BudgetsType> budgetsTypes, BudgetTypesViewModel viewModel, String TAG) {
        this.context = context;
        this.currentTime = currentTime;
        this.inputUtils = inputUtils;
        this.budgetsTypes = budgetsTypes;
        this.viewModel = viewModel;
        this.TAG = TAG;

        FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
        transactionsDao = database.transactionsDao();
        budgetsTypeDao = database.budgetsTypeDao();
    }

    public void insertOrUpdateData(final boolean insert, final boolean update, final Integer transactionId) {
        mInsert = insert;
        mUpdate = update;

        if (!inputUtils.nameInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's name is " + inputUtils.nameInput.getText());
            transaction.setTransactionName(inputUtils.nameInput.getText().toString());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.nameInputField.setError(context.getString(R.string.transaction_name_error_message));
            nullValue = true;
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
            nullValue = true;
        }

        if (!inputUtils.commentInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's comment is " + inputUtils.commentInput.getText());
            transaction.setTransactionComments(inputUtils.commentInput.getText().toString());
        } else {
            transaction.setTransactionComments(null);
        }

        Log.d(TAG, "this transaction's date is " + currentTime.toString());
        Log.d(TAG, "this transaction's id is " + transactionId);
        transaction.setDate(currentTime.getTime());
        if (transactionId != null) {
            transaction.setTransactionId(transactionId);
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

            if (transaction.getTransactionCategoryId() == 0) {
                Log.d(TAG, " a new category is created");
                addNewCategoryToDatabase();
                return;
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.categoryInputField.setError(context.getString(R.string.transaction_category_error_message));
            nullValue = true;
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (!nullValue && insert) {
                    transactionsDao.insertTransaction(transaction);
                } else if (!nullValue && update) {
                    transactionsDao.updateTransaction(transaction);
                } else {
                    Log.d(TAG, "the transaction has some null values");
                }
            }
        });
    }

    public void deleteData(final int transactionId) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                transactionsDao.deleteIndividualTransaction(transactionId);
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

    private void addNewCategoryToDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                BudgetsType newType = new BudgetsType();
                newType.setBudgetsName(inputUtils.categoryInput.getText().toString());
                budgetsTypeDao.insertIndividualBudgetType(newType);

                Log.d(TAG, " the new category's name is set to " + inputUtils.categoryInput.getText().toString());

                final List<BudgetsType> allBudgetTypes = budgetsTypeDao.queryAllBudgetsTypes();
                for (BudgetsType budgetsType : allBudgetTypes) {
                    if (budgetsType.getBudgetsName().equals(inputUtils.categoryInput.getText().toString())) {
                        Log.d(TAG, " the new category's name is " + budgetsType.getBudgetsName() + " id is " + budgetsType.getBudgetsCategoryId());

                        insertTransactionWithNewCategory(budgetsType.getBudgetsCategoryId());
                    }
                }

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG + " Insert Utilities", " data has changed");
                        viewModel.pushToBudgetTypes(allBudgetTypes);
                    }
                });
            }
        });
    }

    private void insertTransactionWithNewCategory(int budgetsCategoryId) {
        transaction.setTransactionCategoryId(budgetsCategoryId);

        if (!nullValue && mInsert) {
            transactionsDao.insertTransaction(transaction);
        } else if (!nullValue && mUpdate) {
            transactionsDao.updateTransaction(transaction);
        } else {
            Log.d(TAG, "the transaction has some null values");
        }
    }
}
