package protect.FinanceLord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
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

    private BudgetListAdapter budgetListAdapter;
    private ArrayList<BudgetsType> allBudgetsTypes;

    private boolean initialize = true;
    private static final int BUDGET_ACTIVITY_REQUEST_CODE = 1000;
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

                List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();
                allBudgetsTypes = new ArrayList<>(budgetsTypes);
                final List<FinancialRecords> financialRecords = financialRecordsDao.queryFinancialRecords();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize) {
                            setUpAddButton();
                            setUpBudgetsListView(financialRecords);
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

    private void setUpAddButton() {
        ImageButton addButton = findViewById(R.id.add_budget_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.budget_categories_key), allBudgetsTypes);
                intent.putExtra(getString(R.string.budget_access_key), getString(R.string.add_budget_access_key));
                intent.setClass(BudgetActivity.this, BudgetEditActivity.class);
                startActivityForResult(intent, BUDGET_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void setUpBudgetsListView(final List<FinancialRecords> financialRecords) {
        ListView budgetsList = findViewById(R.id.budgets_list);
        budgetListAdapter = new BudgetListAdapter(this, financialRecords, allBudgetsTypes);
        budgetsList.setAdapter(budgetListAdapter);

        budgetsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FinancialRecords financialRecord = financialRecords.get(position);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.budget_categories_key), allBudgetsTypes);
                intent.putExtra(getString(R.string.budget_access_key), getString(R.string.edit_budget_access_key));
                intent.putExtra(getString(R.string.budget_id_key), financialRecord.budgetId);
                intent.putExtra(getString(R.string.budget_name_id_key), financialRecord.budgetCategoryId);
                intent.putExtra(getString(R.string.budget_total_key), financialRecord.budgetTotal);
                intent.putExtra(getString(R.string.budget_start_date_key), financialRecord.dateStart);
                intent.putExtra(getString(R.string.budget_end_date_key), financialRecord.dateEnd);
                intent.setClass(BudgetActivity.this, BudgetEditActivity.class);
                startActivityForResult(intent, BUDGET_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<BudgetsType> newBudgetTypes = (ArrayList<BudgetsType>) data.getSerializableExtra(getString(R.string.budget_add_new_types_key));
            budgetListAdapter.refreshBudgetTypes(newBudgetTypes);
            budgetListAdapter.notifyDataSetChanged();
        }
    }
}
