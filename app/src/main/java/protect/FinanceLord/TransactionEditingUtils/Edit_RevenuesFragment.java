package protect.FinanceLord.TransactionEditingUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Communicators.SaveDataCommunicator;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;
import protect.FinanceLord.NetWorthReportEditingUtils.CalendarDialog;
import protect.FinanceLord.NetWorthReportEditingUtils.NetWorthTimeUtils;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionEditActivity;

public class Edit_RevenuesFragment extends Fragment {

    Date currentTime;

    private TextInputLayout nameInputField;
    private TextInputLayout valueInputField;

    private TextInputEditText dateInput;
    private TextInputEditText nameInput;
    private TextInputEditText commentInput;
    private TextInputEditText valueInput;

    private static final String TAG = "Edit_RevenuesFragment";

    private SaveDataCommunicator fromActivityCommunicator = new SaveDataCommunicator() {
        @Override
        public void onActivityMessage() {
            Log.d(TAG, "the message from activity was received");

            retrieveDataFromInputBox();
        }
    };

    public Edit_RevenuesFragment(Date currentTime){
        this.currentTime = currentTime;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TransactionEditActivity activity = (TransactionEditActivity) context;
        activity.toEditRevenuesFragmentCommunicator = fromActivityCommunicator;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_revenues, null);

        nameInputField = revenuesFragmentView.findViewById(R.id.revenue_name_field);
        valueInputField = revenuesFragmentView.findViewById(R.id.revenue_value_field);

        nameInput = revenuesFragmentView.findViewById(R.id.revenue_name_input);
        valueInput = revenuesFragmentView.findViewById(R.id.revenue_value_input);
        commentInput = revenuesFragmentView.findViewById(R.id.revenue_comments_input);
        dateInput = revenuesFragmentView.findViewById(R.id.revenue_date_input);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        return revenuesFragmentView;
    }

    CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, getString(R.string.date_format));
            dateInput.setText(stringDate);
        }
    };

    private void retrieveDataFromInputBox(){
        Transactions transaction = new Transactions();

        if (!nameInput.getText().toString().isEmpty()){
            Log.d(TAG, "this transaction's name is " + nameInput.getText().toString());
            transaction.setTransactionName(nameInput.getText().toString());
        } else {
            Log.d(TAG, "no data is inputted, an error should be displayed ");
            nameInputField.setError(getString(R.string.transaction_name_error_message));
        }

        if (!valueInput.getText().toString().isEmpty()){
            transaction.setTransactionValue(Float.parseFloat(valueInput.getText().toString().replace(",", "")));
        } else {
            valueInputField.setError(getString(R.string.transaction_value_error_message));
        }

        if (!commentInput.getText().toString().isEmpty()){
            transaction.setTransactionComments(commentInput.getText().toString());
        } else {
            transaction.setTransactionComments(null);
        }
        transaction.setDate(currentTime.getTime());

        insertTransactionIntoDb(transaction);
    }

    private void insertTransactionIntoDb(final Transactions transaction) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(getContext());
                TransactionsDao transactionsDao = database.transactionsDao();

                transactionsDao.insertTransaction(transaction);
            }
        });
    }
}
