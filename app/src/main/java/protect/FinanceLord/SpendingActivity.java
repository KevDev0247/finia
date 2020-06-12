package protect.FinanceLord;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.DAOs.TransactionsDao;
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

                final List<MonthlyTotalSpending> monthlyTotalSpendingList = transactionsDao.queryMonthlyTotalExpenses();
                final List<GroupedSpending> groupedSpendingList = transactionsDao.queryGroupedExpenses();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpSpendingList(monthlyTotalSpendingList, groupedSpendingList);
                    }
                });
            }
        });
    }

    private void setUpSpendingList(List<MonthlyTotalSpending> monthlyTotalSpendingList, List<GroupedSpending> groupedSpendingList) {
        ListView monthlySpendingList = findViewById(R.id.spending_list);
        SpendingListAdapter spendingListAdapter = new SpendingListAdapter(this, monthlyTotalSpendingList);
        monthlySpendingList.setAdapter(spendingListAdapter);

        monthlySpendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
