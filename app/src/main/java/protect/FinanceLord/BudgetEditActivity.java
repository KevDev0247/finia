package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.BudgetUtils.BudgetInputUtils;
import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;

public class BudgetEditActivity extends AppCompatActivity {

    private Date startTime;
    private Date endTime;
    private BudgetInputUtils inputUtils = new BudgetInputUtils();

    private String TAG = "BudgetEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_edit);

        ImageButton returnButton = findViewById(R.id.budget_edit_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView pageTitle = findViewById(R.id.budget_page_title);
        pageTitle.setText(getString(R.string.new_budget));

        List<BudgetsType> allBudgetTypes = getIntent().getExtras().getParcelableArrayList(getString(R.string.budget_categories_key));
        List<String> typeNames = new ArrayList<>();
        for (BudgetsType budgetsType : allBudgetTypes){
            typeNames.add(budgetsType.getBudgetsName());
        }
        setUpInputFields(typeNames);
    }

    private void setUpInputFields(List<String> typeNames) {
        inputUtils.nameInputField = findViewById(R.id.budget_name_field);
        inputUtils.valueInputField = findViewById(R.id.budget_value_field);
        inputUtils.startDateInputField = findViewById(R.id.budget_start_date_field);
        inputUtils.endDateInputField = findViewById(R.id.budget_end_date_field);

        inputUtils.nameInput = findViewById(R.id.budget_name_input);
        inputUtils.valueInput = findViewById(R.id.budget_value_input);
        inputUtils.startDateInput = findViewById(R.id.budget_start_date_input);
        inputUtils.endDateInput = findViewById(R.id.budget_end_date_input);
        inputUtils.deleteButton = findViewById(R.id.budget_delete_button);

        inputUtils.nameInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categories_dropdown, typeNames);
        inputUtils.nameInput.setAdapter(adapter);

        inputUtils.startDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(startTimeCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });

        inputUtils.endDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(endTimeCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });

        setUpSaveAndDeleteButton();
    }

    private void setUpSaveAndDeleteButton() {
        ImageButton saveButton = findViewById(R.id.budget_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        inputUtils.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private CalendarDateBroadcast startTimeCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            startTime = date;
            Log.d(TAG, "time is " + startTime);
            String stringDate = TimeProcessor.getStringFromDate(startTime, getString(R.string.date_format));
            inputUtils.startDateInput.setText(stringDate);
        }
    };

    private CalendarDateBroadcast endTimeCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            endTime = date;
            Log.d(TAG, "time is " + endTime);
            String stringDate = TimeProcessor.getStringFromDate(endTime, getString(R.string.date_format));
            inputUtils.endDateInput.setText(stringDate);
        }
    };
}
