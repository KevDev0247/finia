package protect.Finia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.DAOs.TransactionsDao;
import protect.Finia.Database.BudgetsType;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.R;
import protect.Finia.SpendingModule.GroupedSpending;
import protect.Finia.SpendingModule.MonthlyTotalSpending;
import protect.Finia.SpendingModule.SpendingListAdapter;

/**
 * The activity that displayed the list of spending report to the user.
 * Each report item will navigate the user to the report sheet.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class SpendingActivity extends AppCompatActivity {

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button and the ViewModel to enable Activity-to-Fragment communication.
     * Lastly, the method will call the method to retrieve data from the database.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending);

        ImageButton returnButton = findViewById(R.id.spending_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retrieveDataFromDatabase();
    }

    /**
     * Retrieve monthly spending, grouped spending, and budgetTypes from the database.
     * The method will first query the date mentioned above from the database.
     * Query is completed in a separate thread to avoid locking the UI thread for a long period of time.
     * Then, the method will call the method to set up the spending list.
     * MonthlyTotalSpending will store the data that will be displayed on the report item as a summary.
     * GroupedSpending will store the more detailed data used to generate the report.
     */
    private void retrieveDataFromDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(SpendingActivity.this);
                TransactionsDao transactionsDao = database.transactionsDao();
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();

                final List<MonthlyTotalSpending> monthlyTotalSpendingList = transactionsDao.queryMonthlyTotalExpenses();
                final List<GroupedSpending> groupedSpendingList = transactionsDao.queryGroupedExpenses();
                final List<BudgetsType> allBudgetTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpSpendingList(monthlyTotalSpendingList, groupedSpendingList, allBudgetTypes);
                    }
                });
            }
        });
    }

    /**
     * Set up the spending list that display the spending report item.
     * The monthlyTotalSpendingList is used as a data source for the spendingListAdapter.
     * Lastly, onItemClickListener is set up for the monthlySpendingList to navigate the user to the report the user selected.
     *
     * @param allBudgetTypes all the budget types stored in the database.
     * @param groupedSpendingList the class that store the more detailed data used to generate the report.
     * @param monthlyTotalSpendingList the class that store the data that will be displayed on the report item as a summary.
     */
    private void setUpSpendingList(final List<MonthlyTotalSpending> monthlyTotalSpendingList, final List<GroupedSpending> groupedSpendingList, final List<BudgetsType> allBudgetTypes) {
        ListView monthlySpendingList = findViewById(R.id.spending_list);
        LinearLayout initializationMessage = findViewById(R.id.spending_initialization_message);
        SpendingListAdapter spendingListAdapter = new SpendingListAdapter(this, monthlyTotalSpendingList);
        monthlySpendingList.setAdapter(spendingListAdapter);

        if (monthlyTotalSpendingList.size() != 0) {
            initializationMessage.setVisibility(View.GONE);
        }

        monthlySpendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.spending_report_month_key), monthlyTotalSpendingList.get(position).month);
                intent.putParcelableArrayListExtra(getString(R.string.spending_monthly_list_key), new ArrayList<>(groupedSpendingList));
                intent.putParcelableArrayListExtra(getString(R.string.spending_categories), new ArrayList<>(allBudgetTypes));
                intent.setClass(SpendingActivity.this, SpendingReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
