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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Communicators.SaveDataCommunicator;
import protect.FinanceLord.NetWorthReportEditingUtils.CalendarDialog;
import protect.FinanceLord.NetWorthReportEditingUtils.NetWorthTimeUtils;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionEditActivity;

public class Edit_RevenuesFragment extends Fragment {

    Date currentTime;

    private TransactionInputUtils inputUtils;
    private FragmentUtils fragmentUtils;
    private List<String> typeNames = new ArrayList<>();

    private static final String TAG = "Edit_RevenuesFragment";

    public Edit_RevenuesFragment(Date currentTime, List<BudgetTypesDataModel> dataModels){
        this.currentTime = currentTime;

        for (BudgetTypesDataModel dataModel : dataModels){
            String typeName = dataModel.typeName;
            typeNames.add(typeName);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TransactionEditActivity activity = (TransactionEditActivity) context;
        activity.toEditRevenuesCommunicator = fromActivityCommunicator;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_revenues, null);

        inputUtils = new TransactionInputUtils();

        inputUtils.nameInputField = revenuesFragmentView.findViewById(R.id.revenue_name_field);
        inputUtils.valueInputField = revenuesFragmentView.findViewById(R.id.revenue_value_field);
        inputUtils.categoryInputField = revenuesFragmentView.findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = revenuesFragmentView.findViewById(R.id.revenue_name_input);
        inputUtils.valueInput = revenuesFragmentView.findViewById(R.id.revenue_value_input);
        inputUtils.commentInput = revenuesFragmentView.findViewById(R.id.revenue_comments_input);
        inputUtils.categoryInput = revenuesFragmentView.findViewById(R.id.revenue_category_input);
        inputUtils.dateInput = revenuesFragmentView.findViewById(R.id.revenue_date_input);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        fragmentUtils = new FragmentUtils(getContext(), currentTime, inputUtils, TAG);

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

            fragmentUtils.retrieveDataFromInputBox();
            fragmentUtils.addTextListener();
        }
    };

    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, getString(R.string.date_format));
            inputUtils.dateInput.setText(stringDate);
        }
    };
}
