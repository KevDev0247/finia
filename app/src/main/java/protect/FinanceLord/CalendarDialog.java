package protect.FinanceLord;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CalendarDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Context context;
    DatePicker datePicker;
    private int year;
    private int month;
    private int day;

    int DATE_DIALOG_ID = 999;

    public CalendarDialog(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calendar_layout, null);
        setCancelable(false);

        return view;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            datePicker.init(year,month,day, null);
        }
    };

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
