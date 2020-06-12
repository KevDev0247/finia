package protect.FinanceLord.TimeUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.R;

/**
 * The dialog that allows user to pick dates
 * The CalendarDateBroadcast communicator is used to communicate the date with the other entity
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class CalendarDialog extends DialogFragment {

    private DatePicker datePicker;
    private CalendarDateBroadcast communicator;
    private TimeProcessor timeUtils = new TimeProcessor();

    public CalendarDialog(CalendarDateBroadcast communicator){
        this.communicator = communicator;
    }

    /**
     * Create the view of the dialog.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then the method will set up the confirm and cancel button.
     * Lastly, the onClickListener was set to communicate the date once the date is confirmed.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View calendarView = inflater.inflate(R.layout.time_setting_layout, null);
        datePicker = calendarView.findViewById(R.id.date_picker);

        RelativeLayout confirmTimeButton = calendarView.findViewById(R.id.confirm_time_button);
        RelativeLayout cancelTimeButton = calendarView.findViewById(R.id.cancel_time_button);
        setCancelable(true);

        confirmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = timeUtils.setTime(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth());

                Log.d("CalendarDialog", "date picked is " + currentTime);
                communicator.message(currentTime);

                dismiss();
            }
        });

        cancelTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return calendarView;
    }
}
