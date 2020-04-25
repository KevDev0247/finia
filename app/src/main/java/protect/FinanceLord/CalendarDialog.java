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

    View calendarView;
    DatePicker datePicker;
    TimePicker timePicker;
    Button timeButton;
    NetWorthEditReportActivity.Communicator communicator;
    TimeUtils timeUtils = new TimeUtils();

    public CalendarDialog(NetWorthEditReportActivity.Communicator communicator){
        this.communicator = communicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        calendarView = inflater.inflate(R.layout.time_setting_layout, null);
        datePicker = calendarView.findViewById(R.id.date_picker);
        timePicker = calendarView.findViewById(R.id.time_picker);
        setCancelable(true);

        return calendarView;
    }

    public void loadData(){
        this.timeButton = calendarView.findViewById(R.id.time_setting_button);
        this.timeButton.setOnClickListener(new View.OnClickListener() {
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
    }
}
