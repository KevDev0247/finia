package protect.Finia.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.concurrent.Executors;

import protect.Finia.Communicators.CalendarDateBroadcast;
import protect.Finia.Database.BudgetsType;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.R;
import protect.Finia.TimeUtils.CalendarDialog;
import protect.Finia.TimeUtils.TimeProcessor;
import protect.Finia.TransactionModule.TransactionDatabaseHelper;
import protect.Finia.TransactionModule.TransactionInputWidgets;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The activity that enables the user to edit an existing transaction.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionEditActivity extends AppCompatActivity {

    private Date currentTime;
    private TransactionDatabaseHelper databaseHelper;
    private BudgetTypesViewModel viewModel;
    private SimpleDateFormat dateFormat;
    private TransactionInputWidgets inputWidgets = new TransactionInputWidgets();

    private static String TAG = "TransactionEditActivity";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * First, the information stored in the intent from the preceding activity is retrieved.
     * Then, the edit sheet will be set up by determine whether the user picked a expense or revenue.
     * After the sheet is set up, the method to retrieve data will be called.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        ImageButton returnButton = findViewById(R.id.transaction_edit_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.CANADA);
        TextView pageTitle = findViewById(R.id.transaction_edit_item_date);
        pageTitle.setText(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.transaction_date_key))));

        String fragmentTag = getIntent().getExtras().getString(getString(R.string.transaction_fragment_key));
        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_revenues, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag);

        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_expenses, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag);
        }
    }

    /**
     * Retrieve all the budget types stored in database to set up drop down list.
     *
     * @param fragmentTag the name of type of transaction the user picked.
     */
    private void retrieveDataFromDatabase(final String fragmentTag) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(TransactionEditActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                final List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
                            setUpRevenueSection(budgetsTypes);

                        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
                            setUpExpenseSection(budgetsTypes);
                        }
                    }
                });
            }
        });
    }

    /**
     * Set up the revenue edit sheet including the input fields, buttons, as well as observers.
     * First, all the input widgets are associated with the corresponding layout.
     * Next, the methods to set up some particular widgets were called
     * and the data the user inputted previously is also loaded into the input boxes.
     * Lastly, the database helper is set up to help update the data.
     *
     * @param budgetsTypes the budget types stored in the database.
     */
    private void setUpRevenueSection(List<BudgetsType> budgetsTypes) {
        inputWidgets.nameInputField = findViewById(R.id.revenue_name_field);
        inputWidgets.valueInputField = findViewById(R.id.revenue_value_field);
        inputWidgets.categoryInputField = findViewById(R.id.revenue_category_field);

        inputWidgets.nameInput = findViewById(R.id.revenue_name_input);
        inputWidgets.valueInput = findViewById(R.id.revenue_value_input);
        inputWidgets.commentInput = findViewById(R.id.revenue_comments_input);
        inputWidgets.categoryInput = findViewById(R.id.revenue_category_input);
        inputWidgets.dateInput = findViewById(R.id.revenue_date_input);

        inputWidgets.deleteButton = findViewById(R.id.revenue_delete_button);

        setUpBudgetTypesViewModel();

        setUpCategoryAndDateInput(budgetsTypes);

        loadDataToInputBoxes(budgetsTypes);

        setUpSaveAndDeleteButton();

        databaseHelper = new TransactionDatabaseHelper(this, currentTime, inputWidgets, budgetsTypes, viewModel, getString(R.string.revenues_section_key));
    }

    /**
     * Set up the expense edit sheet including the input fields, buttons, as well as observers.
     * First, all the input widgets are associated with the corresponding layout.
     * Next, the methods to set up some particular widgets were called
     * and the data the user inputted previously is also loaded into the input boxes.
     * Lastly, the database helper is set up to help update the data.
     *
     * @param budgetsTypes the budget types stored in the database.
     */
    private void setUpExpenseSection(List<BudgetsType> budgetsTypes) {
        inputWidgets.nameInputField = findViewById(R.id.expenses_name_field);
        inputWidgets.valueInputField = findViewById(R.id.expenses_value_field);
        inputWidgets.categoryInputField = findViewById(R.id.expenses_category_field);

        inputWidgets.nameInput = findViewById(R.id.expenses_name_input);
        inputWidgets.valueInput = findViewById(R.id.expenses_value_input);
        inputWidgets.commentInput = findViewById(R.id.expenses_comments_input);
        inputWidgets.categoryInput = findViewById(R.id.expenses_category_input);
        inputWidgets.dateInput = findViewById(R.id.expenses_date_input);

        inputWidgets.deleteButton = findViewById(R.id.expense_delete_button);

        setUpBudgetTypesViewModel();

        setUpCategoryAndDateInput(budgetsTypes);

        loadDataToInputBoxes(budgetsTypes);

        setUpSaveAndDeleteButton();

        databaseHelper = new TransactionDatabaseHelper(this, currentTime, inputWidgets, budgetsTypes, viewModel, getString(R.string.expenses_section_key));
    }

    /**
     * The drop down list is added onto the category input box and the calendarDialogCommunicator is also deployed.
     * An ArrayAdapter is set up to help deliver the budgetTypes data to the drop down list.
     *
     * @param budgetsTypes the budget types stored in the database.
     */
    private void setUpCategoryAndDateInput(List<BudgetsType> budgetsTypes) {
        List<String> typeNames = new ArrayList<>();
        for (BudgetsType budgetsType : budgetsTypes){
            typeNames.add(budgetsType.getBudgetsName());
        }

        inputWidgets.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions_dropdown_background, null));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.categories_dropdown, typeNames);
        inputWidgets.categoryInput.setAdapter(adapter);

        inputWidgets.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });
    }

    /**
     * All the data user stored is loaded into the input boxes
     *
     * @param budgetsTypes the budget types stored in the database.
     */
    private void loadDataToInputBoxes(List<BudgetsType> budgetsTypes) {
        DecimalFormat decimalFormat = new DecimalFormat();

        inputWidgets.nameInput.setText(getIntent().getStringExtra(getString(R.string.transaction_name_key)));
        inputWidgets.valueInput.setText(decimalFormat.format(getIntent().getExtras().getFloat(getString(R.string.transaction_value_key))).replace("-",""));
        inputWidgets.dateInput.setText(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.transaction_date_key))));

        try {
            currentTime = TimeProcessor.parseDateString(dateFormat.format(getIntent().getExtras().getLong(getString(R.string.transaction_date_key))), getString(R.string.date_format));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (getIntent().getStringExtra(getString(R.string.transaction_comments_key)) != null) {
            inputWidgets.commentInput.setText(getIntent().getStringExtra(getString(R.string.transaction_comments_key)));
        }

        for (BudgetsType budgetsType : budgetsTypes) {
            if (getIntent().getExtras().getInt(getString(R.string.transaction_category_key)) == budgetsType.getBudgetsCategoryId()) {
                inputWidgets.categoryInput.setText(budgetsType.getBudgetsName());
            }
        }
    }

    /**
     * Set up the logic to update and delete for the save and delete button.
     * The update logic is mainly handled with the databaseHelper.
     */
    private void setUpSaveAndDeleteButton() {
        ImageButton saveButton = findViewById(R.id.transaction_edit_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.insertOrUpdateData(false, true, getIntent().getExtras().getInt(getString(R.string.transaction_id_key)));
                databaseHelper.addTextListener();
            }
        });

        inputWidgets.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteData(getIntent().getExtras().getInt(getString(R.string.transaction_id_key)));
            }
        });
    }

    /**
     * Set up the view model to detect any change of budget types when user update the data.
     * Whenever the view model detects change in budget types,
     * the updated list of budget types will be sent back to the preceding activity.
     */
    private void setUpBudgetTypesViewModel() {
        viewModel = ViewModelProviders.of(this).get(BudgetTypesViewModel.class);
        viewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
                ArrayList<BudgetsType> budgetsTypes = new ArrayList<>(newBudgetsTypes);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getString(R.string.transaction_add_new_types_key), budgetsTypes);
                setResult(Activity.RESULT_OK, intent);
            }
        });
    }

    /**
     * The communicator that communicate the date from calendar dialog to the activity.
     * The date that the user picked will be displayed on the input box.
     */
    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputWidgets.dateInput.setText(stringDate);
        }
    };
}
