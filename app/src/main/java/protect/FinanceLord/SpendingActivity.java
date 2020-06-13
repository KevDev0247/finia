package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.DAOs.BudgetsTypeDao;
import protect.FinanceLord.DAOs.TransactionsDao;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.SpendingUtils.GroupedSpending;
import protect.FinanceLord.SpendingUtils.MonthlyTotalSpending;
import protect.FinanceLord.SpendingUtils.SpendingListAdapter;

public class SpendingActivity extends AppCompatActivity {

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

    private void retrieveDataFromDatabase() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(SpendingActivity.this);
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

    private void setUpSpendingList(final List<MonthlyTotalSpending> monthlyTotalSpendingList, final List<GroupedSpending> groupedSpendingList, final List<BudgetsType> allBudgetTypes) {
        ListView monthlySpendingList = findViewById(R.id.spending_list);
        SpendingListAdapter spendingListAdapter = new SpendingListAdapter(this, monthlyTotalSpendingList);
        monthlySpendingList.setAdapter(spendingListAdapter);

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
