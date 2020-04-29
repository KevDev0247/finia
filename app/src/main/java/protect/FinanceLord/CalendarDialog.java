package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;

public class CalendarDialog extends DialogFragment {

    View calendarView;
    DatePicker datePicker;
    Button confirmTimeButton;
    Button cancelTimeButton;
    CalendarDateBroadcast communicator;
    NetWorthTimeUtils timeUtils = new NetWorthTimeUtils();


    public CalendarDialog(CalendarDateBroadcast communicator){
        this.communicator = communicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        calendarView = inflater.inflate(R.layout.time_setting_layout, null);
        datePicker = calendarView.findViewById(R.id.date_picker);
        this.confirmTimeButton = calendarView.findViewById(R.id.confirm_time_button);
        this.cancelTimeButton = calendarView.findViewById(R.id.cancel_time_button);
        setCancelable(true);

        this.confirmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = timeUtils.setTime(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());

                Log.d("CalendarDialog", "date picked is " + currentTime);
                communicator.onDialogMessage(currentTime);

                dismiss();
            }
        });

        this.cancelTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return calendarView;
    }
}
