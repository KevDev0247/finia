package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.BudgetUtils.BudgetListAdapter;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.FinancialRecords;
import protect.FinanceLord.Database.FinancialRecordsDao;

public class BudgetActivity extends AppCompatActivity {

    private List<BudgetsType> allBudgetsTypes;
    private List<FinancialRecords> financialRecords;
    private BudgetListAdapter budgetListAdapter;

    private boolean initialize = true;
    private String TAG = "BudgetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        ImageButton returnButton = findViewById(R.id.budget_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataFromDatabase(initialize);
        initialize = false;
    }

    private void retrieveDataFromDatabase(final boolean initialize) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(BudgetActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                FinancialRecordsDao financialRecordsDao = database.financeRecordsDao();

                allBudgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();
                financialRecords = financialRecordsDao.queryFinancialRecords();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize) {
                            setUpAddButton(allBudgetsTypes);
                            setUpBudgetsListView();
                        } else {
                            if (budgetListAdapter != null) {
                                for (FinancialRecords financialRecord : financialRecords) {
                                    Log.d(TAG, " this financial record id is " + financialRecord.budgetCategoryId +
                                            " total value is " + financialRecord.budgetTotal +
                                            " total usage is" + financialRecord.totalUsage +
                                            " date start is " + financialRecord.dateStart +
                                            " date end is " + financialRecord.dateEnd);
                                }
                                budgetListAdapter.clear();
                                budgetListAdapter.addAll(financialRecords);
                                budgetListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });
    }

    private void setUpAddButton(List<BudgetsType> allBudgetsTypes) {
        final ArrayList<BudgetsType> budgetTypes = new ArrayList<>(allBudgetsTypes);
        ImageButton addButton = findViewById(R.id.add_budget_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.budget_categories_key), budgetTypes);
                intent.setClass(BudgetActivity.this, BudgetEditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpBudgetsListView() {
        ListView budgetsList = findViewById(R.id.budgets_list);
        budgetListAdapter = new BudgetListAdapter(this, financialRecords, allBudgetsTypes);
        budgetsList.setAdapter(budgetListAdapter);
    }
}
