package protect.FinanceLord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import protect.FinanceLord.NetWorthEditReportsUtils.TimeUtils;

public class CalendarDialog extends DialogFragment {

    Communicator communicator;
    DatePicker datePicker;
    TimePicker timePicker;
    Button dateButton;
    TimeUtils timeUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_layout, null);
        setCancelable(true);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = timeUtils.setTime(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getHour(),
                        timePicker.getMinute());

                communicator.onDialogMessage(currentTime);
            }
        });

        return view;
    }

    interface Communicator {
        void onDialogMessage(Date date);
    }
}
