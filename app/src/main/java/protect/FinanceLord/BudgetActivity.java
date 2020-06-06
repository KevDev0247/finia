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
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.Database.BudgetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.FinancialRecordsDao;
import protect.FinanceLord.Database.TransactionsDao;

public class BudgetActivity extends AppCompatActivity {

    private List<BudgetsType> allBudgetsTypes;
    private List<BudgetsValue> allBudgetsValues;
    private BudgetListAdapter budgetListAdapter;

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

        retrieveDataFromDatabase(true,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataFromDatabase(false, true);
    }

    private void retrieveDataFromDatabase(final boolean initialize, final boolean refresh) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(BudgetActivity.this);
                BudgetsValueDao budgetsValueDao = database.budgetsValueDao();
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                FinancialRecordsDao financialRecordsDao = database.financeRecordsDao();
                final TransactionsDao transactionsDao = database.transactionsDao();

                allBudgetsValues = budgetsValueDao.queryAllBudgets();
                allBudgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize) {
                            setUpAddButton(allBudgetsTypes);
                            setUpBudgetsListView(transactionsDao);
                        } else if (refresh) {
                            if (budgetListAdapter != null) {
                                for (BudgetsValue budgetsValue : allBudgetsValues) {
                                    Log.d(TAG, " this budget id is " + budgetsValue.getBudgetsCategoryId() +
                                            " value is " + budgetsValue.getBudgetsValue() +
                                            " date start is " + budgetsValue.getDateStart() +
                                            " date end is " + budgetsValue.getDateEnd());
                                }
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

    private void setUpBudgetsListView(TransactionsDao transactionsDao) {
        ListView budgetsList = findViewById(R.id.budgets_list);
        budgetListAdapter = new BudgetListAdapter(this, transactionsDao, allBudgetsValues, allBudgetsTypes);
        budgetsList.setAdapter(budgetListAdapter);
    }
}
