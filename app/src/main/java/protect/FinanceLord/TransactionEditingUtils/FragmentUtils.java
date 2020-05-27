package protect.FinanceLord.TransactionEditingUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;
import protect.FinanceLord.R;

class FragmentUtils {

    private Context context;
    private String TAG;
    private Date currentTime;
    private TransactionInputUtils inputUtils;
    private List<BudgetTypesDataModel> dataModels;

    FragmentUtils(Context context, Date currentTime, TransactionInputUtils inputUtils, List<BudgetTypesDataModel> dataModels, String TAG) {
        this.context = context;
        this.currentTime = currentTime;
        this.inputUtils = inputUtils;
        this.dataModels = dataModels;
        this.TAG = TAG;
    }

    void retrieveAndInsertData() {
        final Transactions transaction = new Transactions();
        boolean insert = true;

        if (!inputUtils.nameInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's name is " + inputUtils.nameInput.getText());
            transaction.setTransactionName(inputUtils.nameInput.getText().toString());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.nameInputField.setError(context.getString(R.string.transaction_name_error_message));
            insert = false;
        }

        if (!inputUtils.valueInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's value is " + inputUtils.valueInput.getText());
            if (TAG.equals(context.getString(R.string.edit_revenues_fragment_key))) {
                transaction.setTransactionValue(Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
            } else if (TAG.equals(context.getString(R.string.edit_expenses_fragment_key))) {
                transaction.setTransactionValue( - Float.parseFloat(inputUtils.valueInput.getText().toString().replace(",", "")));
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.valueInputField.setError(context.getString(R.string.transaction_value_error_message));
            insert = false;
        }

        if (!inputUtils.commentInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's comment is " + inputUtils.commentInput.getText());
            transaction.setTransactionComments(inputUtils.commentInput.getText().toString());
        } else {
            transaction.setTransactionComments(null);
        }

        if (!inputUtils.categoryInput.getText().toString().isEmpty()) {
            Log.d(TAG, "this transaction's category is " + inputUtils.categoryInput.getText());
            for (BudgetTypesDataModel dataModel : dataModels){
                if (dataModel.typeName.equals(inputUtils.categoryInput.getText().toString())){
                    transaction.setTransactionCategoryId(dataModel.typeId);
                }
            }
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            inputUtils.valueInputField.setError(context.getString(R.string.transaction_category_error_message));
            insert = false;
        }

        Log.d(TAG, "this transaction's date is " + currentTime.toString());
        transaction.setDate(currentTime.getTime());

        if (insert) {

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
                    TransactionsDao transactionsDao = database.transactionsDao();

                    transactionsDao.insertTransaction(transaction);
                }
            });
        } else {
            Log.d(TAG, "the transaction has some null values");
        }
    }

    void addTextListener() {
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
}
