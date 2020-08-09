package protect.Finia.TransactionAdding;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.Finia.Communicators.CalendarDateBroadcast;
import protect.Finia.Communicators.SaveDataCommunicator;
import protect.Finia.Database.BudgetsType;
import protect.Finia.R;
import protect.Finia.TimeUtils.CalendarDialog;
import protect.Finia.TimeUtils.TimeProcessor;
import protect.Finia.Activities.TransactionAddActivity;
import protect.Finia.TransactionModule.TransactionDatabaseHelper;
import protect.Finia.TransactionModule.TransactionInputWidgets;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The class for the fragment to add transactions.
 * The class includes all the input boxes to enter information about the particular transaction.
 * Note that this fragment is reusable. It is used for both expense and revenue sections.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class Add_TransactionsFragment extends Fragment {

    private String fragmentTag;
    private Date currentTime;
    private TransactionDatabaseHelper databaseHelper;
    private TransactionAddActivity transactionAddActivity;
    private List<BudgetsType> budgetsTypes;
    private List<String> typeNames = new ArrayList<>();
    private TransactionInputWidgets inputWidgets = new TransactionInputWidgets();

    private static final String TAG = "Edit_RevenuesFragment";

    public Add_TransactionsFragment(Date currentTime, List<BudgetsType> budgetsTypes, String fragmentTag) {
        this.currentTime = currentTime;
        this.budgetsTypes = budgetsTypes;
        this.fragmentTag = fragmentTag;

        for (BudgetsType budgetsType : budgetsTypes) {
            typeNames.add(budgetsType.getBudgetsName());
        }
    }

    /**
     * Attach the fragment to the activity it belongs to.
     * In this method, the fragment will retrieve the instance of communicator in the activity
     * in order to communicate with the activity.
     *
     * @param context the context of this fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        transactionAddActivity = (TransactionAddActivity) context;

        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            transactionAddActivity.toEditRevenuesCommunicator = fromActivityCommunicator;
        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            transactionAddActivity.toEditExpensesCommunicator = fromActivityCommunicator;
        }
    }

    /**
     * Create the view of the fragment.
     * The method will first determine whether to create a revenues fragment or a expenses fragment based on the fragment tag.
     * Then, the input widgets will be set up.
     *
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     * @return the view of the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_revenues, null);

            setUpRevenuesInputWidgets(revenuesFragmentView);

            return revenuesFragmentView;

        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            View expensesFragmentView = inflater.inflate(R.layout.fragment_edit_expenses, null);

            setUpExpensesInputWidgets(expensesFragmentView);

            return expensesFragmentView;
        }

        return null;
    }

    /**
     * Set up the revenues input widgets including the input boxes, the drop down list, the delete button, and the database helper.
     * First, all the input widgets are associated with the corresponding layout.
     * Next, the adapter for the drop down list will be set up and added to the category input box.
     * Then, the database helper and view model is set up to help insert the data and monitor the change to budget types.
     * Lastly, the method to set up delete button and the date input is called.
     *
     * @param revenuesFragmentView the view of the revenues fragment.
     */
    private void setUpRevenuesInputWidgets(View revenuesFragmentView) {
        inputWidgets.nameInputField = revenuesFragmentView.findViewById(R.id.revenue_name_field);
        inputWidgets.valueInputField = revenuesFragmentView.findViewById(R.id.revenue_value_field);
        inputWidgets.categoryInputField = revenuesFragmentView.findViewById(R.id.revenue_category_field);

        inputWidgets.nameInput = revenuesFragmentView.findViewById(R.id.revenue_name_input);
        inputWidgets.valueInput = revenuesFragmentView.findViewById(R.id.revenue_value_input);
        inputWidgets.commentInput = revenuesFragmentView.findViewById(R.id.revenue_comments_input);
        inputWidgets.categoryInput = revenuesFragmentView.findViewById(R.id.revenue_category_input);
        inputWidgets.dateInput = revenuesFragmentView.findViewById(R.id.revenue_date_input);
        inputWidgets.deleteButton = revenuesFragmentView.findViewById(R.id.revenue_delete_button);

        inputWidgets.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.categories_dropdown, typeNames);
        inputWidgets.categoryInput.setAdapter(adapter);

        BudgetTypesViewModel viewModel = ViewModelProviders.of(transactionAddActivity).get(BudgetTypesViewModel.class);
        databaseHelper = new TransactionDatabaseHelper(getContext(), currentTime, inputWidgets, budgetsTypes, viewModel, getString(R.string.revenues_section_key));

        setUpDeleteButtonAndDateInput();
    }

    /**
     * Set up the expenses input widgets including the input boxes, the drop down list, the delete button, and the database helper.
     * First, all the input widgets are associated with the corresponding layout.
     * Next, the adapter for the drop down list will be set up and added to the category input box.
     * Then, the database helper and view model is set up to help insert the data and detect the change to budget types.
     * Lastly, the method to set up delete button and the date input is called.
     *
     * @param expensesFragmentView the view of the expenses fragment.
     */
    private void setUpExpensesInputWidgets(View expensesFragmentView) {
        inputWidgets.nameInputField = expensesFragmentView.findViewById(R.id.expenses_name_field);
        inputWidgets.valueInputField = expensesFragmentView.findViewById(R.id.expenses_value_field);
        inputWidgets.categoryInputField = expensesFragmentView.findViewById(R.id.expenses_category_field);

        inputWidgets.nameInput = expensesFragmentView.findViewById(R.id.expenses_name_input);
        inputWidgets.valueInput = expensesFragmentView.findViewById(R.id.expenses_value_input);
        inputWidgets.commentInput = expensesFragmentView.findViewById(R.id.expenses_comments_input);
        inputWidgets.categoryInput = expensesFragmentView.findViewById(R.id.expenses_category_input);
        inputWidgets.dateInput = expensesFragmentView.findViewById(R.id.expenses_date_input);
        inputWidgets.deleteButton = expensesFragmentView.findViewById(R.id.expense_delete_button);

        inputWidgets.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.categories_dropdown, typeNames);
        inputWidgets.categoryInput.setAdapter(adapter);

        BudgetTypesViewModel viewModel = ViewModelProviders.of(transactionAddActivity).get(BudgetTypesViewModel.class);
        databaseHelper = new TransactionDatabaseHelper(getContext(), currentTime, inputWidgets, budgetsTypes, viewModel, getString(R.string.expenses_section_key));

        setUpDeleteButtonAndDateInput();
    }

    /**
     * Set up the delete logic as well as the calendarDialogCommunicator to transfer the date picked from calendar dialog.
     */
    private void setUpDeleteButtonAndDateInput() {
        inputWidgets.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        inputWidgets.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });
    }

    /**
     * The communicator that receive the message to save data and notify the database helper to insert data.
     * Text listeners are also added to the input boxes to detect any changes.
     */
    private SaveDataCommunicator fromActivityCommunicator = new SaveDataCommunicator() {
        @Override
        public void message() {
            Log.d(TAG, "the message from activity was received");
            databaseHelper.insertOrUpdateData(true, false, null);
            databaseHelper.addTextListener();
        }
    };

    /**
     * The communicator that communicate the date from calendar dialog to the activity.
     * The date that the user picked will be displayed on the input box.
     */
    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputWidgets.dateInput.setText(stringDate);
        }
    };
}
