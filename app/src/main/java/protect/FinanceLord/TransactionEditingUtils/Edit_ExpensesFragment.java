package protect.FinanceLord.TransactionEditingUtils;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Communicators.SaveDataCommunicator;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionAddActivity;

public class Edit_ExpensesFragment extends Fragment {

    private Date currentTime;
    private TransactionInsertUtils fragmentUtils;
    private List<BudgetTypesDataModel> dataModels;
    private List<String> typeNames = new ArrayList<>();
    private TransactionInputUtils inputUtils = new TransactionInputUtils();

    private static final String TAG = "Edit_ExpensesFragment";

    public Edit_ExpensesFragment(Date currentTime, List<BudgetTypesDataModel> dataModels){
        this.currentTime = currentTime;
        this.dataModels = dataModels;

        for (BudgetTypesDataModel dataModel : dataModels){
            typeNames.add(dataModel.typeName);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TransactionAddActivity activity = (TransactionAddActivity) context;
        activity.toEditExpensesCommunicator = fromActivityCommunicator;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View expensesFragmentView = inflater.inflate(R.layout.fragment_edit_expenses, null);

        inputUtils.nameInputField = expensesFragmentView.findViewById(R.id.expenses_name_field);
        inputUtils.valueInputField = expensesFragmentView.findViewById(R.id.expenses_value_field);
        inputUtils.categoryInputField = expensesFragmentView.findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = expensesFragmentView.findViewById(R.id.expenses_name_input);
        inputUtils.valueInput = expensesFragmentView.findViewById(R.id.expenses_value_input);
        inputUtils.commentInput = expensesFragmentView.findViewById(R.id.expenses_comments_input);
        inputUtils.categoryInput = expensesFragmentView.findViewById(R.id.expenses_category_input);
        inputUtils.dateInput = expensesFragmentView.findViewById(R.id.expenses_date_input);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.expenses_dropdown_background, null));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.transaction_categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        fragmentUtils = new TransactionInsertUtils(getContext(), currentTime, inputUtils, dataModels, TAG);

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        return expensesFragmentView;
    }

    private SaveDataCommunicator fromActivityCommunicator = new SaveDataCommunicator() {
        @Override
        public void onActivityMessage() {
            Log.d(TAG, "the message from activity was received");

            fragmentUtils.retrieveAndInsertData();
            fragmentUtils.addTextListener();
        }
    };

    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputUtils.dateInput.setText(stringDate);
        }
    };
}
