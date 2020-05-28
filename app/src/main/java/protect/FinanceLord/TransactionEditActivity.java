package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;
import protect.FinanceLord.TransactionEditingUtils.TransactionInputUtils;

public class TransactionEditActivity extends AppCompatActivity {

    private Date currentTime;
    private TransactionInputUtils inputUtils = new TransactionInputUtils();

    private static String TAG = "TransactionEditActivity";

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

        String fragmentTag = getIntent().getExtras().getString(getString(R.string.transaction_fragment_key));
        if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_revenues, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag, editSectionView);

        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
            View editSectionView = LayoutInflater.from(this).inflate(R.layout.fragment_edit_expenses, null, false);
            LinearLayout sheet = findViewById(R.id.transaction_section_view);
            sheet.addView(editSectionView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            retrieveDataFromDatabase(fragmentTag, editSectionView);
        }
    }

    private void retrieveDataFromDatabase(final String fragmentTag, final View editSectionView) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(TransactionEditActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                final List<String> typeNames = new ArrayList<>();
                for (BudgetsType budgetsType : budgetsTypes){
                    typeNames.add(budgetsType.getBudgetsName());
                }

                if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeRevenueSection(typeNames, editSectionView);
                        }
                    });

                } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeExpenseSection(typeNames, editSectionView);
                        }
                    });
                }
            }
        });
    }

    private void initializeRevenueSection(List<String> typeNames, View editSectionView) {
        inputUtils.nameInputField = findViewById(R.id.revenue_name_field);
        inputUtils.valueInputField = findViewById(R.id.revenue_value_field);
        inputUtils.categoryInputField = findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = findViewById(R.id.revenue_name_input);
        inputUtils.valueInput = findViewById(R.id.revenue_value_input);
        inputUtils.commentInput = findViewById(R.id.revenue_comments_input);
        inputUtils.categoryInput = findViewById(R.id.revenue_category_input);
        inputUtils.dateInput = findViewById(R.id.revenue_date_input);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.expenses_dropdown_background, null));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.transaction_categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });
    }

    private void initializeExpenseSection(List<String> typeNames, View editSectionView) {
        inputUtils.nameInputField = editSectionView.findViewById(R.id.expenses_name_field);
        inputUtils.valueInputField = editSectionView.findViewById(R.id.expenses_value_field);
        inputUtils.categoryInputField = editSectionView.findViewById(R.id.expenses_category_field);

        inputUtils.nameInput = findViewById(R.id.expenses_name_input);
        inputUtils.valueInput = findViewById(R.id.expenses_value_input);
        inputUtils.commentInput = findViewById(R.id.expenses_comments_input);
        inputUtils.categoryInput = findViewById(R.id.expenses_category_input);
        inputUtils.dateInput = findViewById(R.id.expenses_date_input);

        inputUtils.categoryInput.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.expenses_dropdown_background, null));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.transaction_categories_dropdown, typeNames);
        inputUtils.categoryInput.setAdapter(adapter);

        inputUtils.dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                Log.d(TAG, "Date input is clicked");
                calendarDialog.show(getSupportFragmentManager(), "DateTimePicker");
            }
        });
    }

    private CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d(TAG, "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            inputUtils.dateInput.setText(stringDate);
        }
    };
}
