package protect.Finia.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import protect.Finia.BudgetModule.BudgetDatabaseHelper;
import protect.Finia.BudgetModule.BudgetInputWidgets;
import protect.Finia.Communicators.CalendarDateBroadcast;
import protect.Finia.Database.BudgetsType;
import protect.Finia.R;
import protect.Finia.TimeUtils.CalendarDialog;
import protect.Finia.TimeUtils.TimeProcessor;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The activity that displayed the list of budgets.
 * The activity will allow the user to add or edit their budgets.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetEditActivity extends AppCompatActivity {

    private BudgetDatabaseHelper databaseUtils;
    private BudgetTypesViewModel viewModel;
    private BudgetInputWidgets inputUtils = new BudgetInputWidgets();

    private String TAG = "BudgetEditActivity";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * First, the title will be set to the date of the budget item.
     * Lastly, the methods to set up view models and the input widgets are called.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
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

        setUpBudgetTypesViewModel();

        setUpInputWidgets(typeNames, allBudgetTypes);
    }

    /**
     * Set up the input widgets including the input boxes, the drop down list, the delete button, and the database helper.
     * First, all the input widgets are associated with the corresponding layout.
     * Next, the adapter for the drop down list will be set up and added to the category input box.
     * Then, the database helper is set up to help insert or update the data.
     * Lastly, the end date input and start date input is set up by connecting the communicators with the calendar dialog.
     *
     * @param typeNames list of names of all the categories
     * @param allBudgetTypes list of all the budgetType items.
     */
    private void setUpInputWidgets(List<String> typeNames, List<BudgetsType> allBudgetTypes) {
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

        databaseUtils = new BudgetDatabaseHelper(this, inputUtils, viewModel, getIntent().<BudgetsType>getParcelableArrayListExtra(getString(R.string.budget_categories_key)));

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

        if (getIntent().getExtras().getString(getString(R.string.budget_access_key)).equals(getString(R.string.edit_budget_access_key))) {
            loadDataToInputBox(allBudgetTypes);
        }

        setUpSaveAndDeleteButton();
    }

    /**
     * All the data user stored is loaded into the input boxes
     *
     * @param allBudgetTypes the budget types stored in the database.
     */
    private void loadDataToInputBox(List<BudgetsType> allBudgetTypes) {
        DecimalFormat decimalFormat = new DecimalFormat();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.CANADA);

        inputUtils.valueInput.setText(decimalFormat.format(getIntent().getExtras().getFloat(getString(R.string.budget_total_key))));
        inputUtils.startDateInput.setText(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.budget_start_date_key))));
        inputUtils.endDateInput.setText(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.budget_end_date_key))));
        for (BudgetsType budgetsType : allBudgetTypes) {
            if (budgetsType.getBudgetsCategoryId() == getIntent().getExtras().getInt(getString(R.string.budget_name_id_key))) {
                inputUtils.nameInput.setText(budgetsType.getBudgetsName());
            }
        }
    }

    /**
     * Set up the logic to update and delete for the save and delete button.
     * The update logic is mainly handled with the databaseHelper.
     */
    private void setUpSaveAndDeleteButton() {
        ImageButton saveButton = findViewById(R.id.budget_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseUtils.addTextListener();
                try {
                    if (getIntent().getExtras().getString(getString(R.string.budget_access_key)).equals(getString(R.string.add_budget_access_key))) {
                        databaseUtils.insertOrUpdateData(true,false, null);
                    } else if (getIntent().getExtras().getString(getString(R.string.budget_access_key)).equals(getString(R.string.edit_budget_access_key))) {
                        databaseUtils.insertOrUpdateData(false,true, getIntent().getExtras().getInt(getString(R.string.budget_id_key)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        inputUtils.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getExtras().getString(getString(R.string.budget_access_key)).equals(getString(R.string.add_budget_access_key))) {
                    finish();
                } else if (getIntent().getExtras().getString(getString(R.string.budget_access_key)).equals(getString(R.string.edit_budget_access_key))) {
                    databaseUtils.deleteData(getIntent().getExtras().getInt(getString(R.string.budget_id_key)));
                }
            }
        });
    }

    /**
     * Set up the view model to detect any change of budget types when user update the data.
     * Whenever the view model detects change in budget types,
     * the updated list of budget types will be sent back to the preceding activity.
     */
    private void setUpBudgetTypesViewModel() {
        viewModel = ViewModelProviders.of(BudgetEditActivity.this).get(BudgetTypesViewModel.class);
        viewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
                ArrayList<BudgetsType> budgetsTypes = new ArrayList<>(newBudgetsTypes);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getString(R.string.budget_add_new_types_key), budgetsTypes);
                setResult(Activity.RESULT_OK, intent);
            }
        });
    }

    /**
     * The communicator that communicate the date from calendar dialog to the activity.
     * The date that the user picked will be displayed on the input box.
     */
    private CalendarDateBroadcast startTimeCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            Log.d(TAG, "time is " + date);
            String stringDate = TimeProcessor.getStringFromDate(date, getString(R.string.date_format));
            inputUtils.startDateInput.setText(stringDate);
        }
    };

    /**
     * The communicator that communicate the date from calendar dialog to the activity.
     * The date that the user picked will be displayed on the input box.
     */
    private CalendarDateBroadcast endTimeCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            Log.d(TAG, "time is " + date);
            String stringDate = TimeProcessor.getStringFromDate(date, getString(R.string.date_format));
            inputUtils.endDateInput.setText(stringDate);
        }
    };
}
