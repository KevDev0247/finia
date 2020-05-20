package protect.FinanceLord.TransactionEditingUtils;

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

import java.util.Date;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.NetWorthReportEditingUtils.CalendarDialog;
import protect.FinanceLord.NetWorthReportEditingUtils.NetWorthTimeUtils;
import protect.FinanceLord.R;

public class Edit_RevenuesFragment extends Fragment {

    Date currentTime;
    private TextInputEditText dateInput;

    public Edit_RevenuesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View revenuesFragmentView = inflater.inflate(R.layout.fragment_edit_transactions, null);
        dateInput = revenuesFragmentView.findViewById(R.id.transaction_date_input);

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getFragmentManager();
                Log.d("EditReportPassing", "time is " + currentTime);
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        return revenuesFragmentView;
    }

    CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d("EditReportCommunicator", "time is " + currentTime);
            String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, getString(R.string.date_format));
            dateInput.setText(stringDate);
        }
    };
}
