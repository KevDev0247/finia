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
import protect.FinanceLord.TransactionUtils.TransactionInputUtils;
import protect.FinanceLord.TransactionUtils.TransactionInsertUtils;

public class Edit_RevenuesFragment extends Fragment {

    private Date currentTime;
    private TransactionInsertUtils insertUtils;
    private List<BudgetTypesDataModel> dataModels;
    private List<String> typeNames = new ArrayList<>();
    private TransactionInputUtils inputUtils = new TransactionInputUtils();

    private static final String TAG = "Edit_RevenuesFragment";

    public Edit_RevenuesFragment(Date currentTime, List<BudgetTypesDataModel> dataModels){
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
        activity.toEditRevenuesCommunicator = fromActivityCommunicator;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_revenues, null);

        inputUtils.nameInputField = revenuesFragmentView.findViewById(R.id.revenue_name_field);
        inputUtils.valueInputField = revenuesFragmentView.findViewById(R.id.revenue_value_field);
        inputUtils.categoryInputField = revenuesFragmentView.findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = revenuesFragmentView.findViewById(R.id.revenue_name_input);
        inputUtils.valueInput = revenuesFragmentView.findViewById(R.id.revenue_value_input);
        inputUtils.commentInput = revenuesFragmentView.findViewById(R.id.revenue_comments_input);
        inputUtils.categoryInput = revenuesFragmentView.findViewById(R.id.revenue_category_input);
        inputUtils.dateInput = revenuesFragmentView.findViewById(R.id.revenue_date_input);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.transaction_categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

//        insertUtils = new TransactionInsertUtils(getContext(), currentTime, inputUtils, dataModels, getString(R.string.revenues_section_key));

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
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

    private SaveDataCommunicator fromActivityCommunicator = new SaveDataCommunicator() {
        @Override
        public void onActivityMessage() {
            Log.d(TAG, "the message from activity was received");

            insertUtils.insertOrUpdateData(true, false, null);
            insertUtils.addTextListener();
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
