package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
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

public class BudgetActivity extends AppCompatActivity {

    private List<BudgetsType> allBudgetsTypes;
    private List<BudgetsValue> allBudgetsValues;

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

        retrieveDataFromDatabase();
    }

    private void retrieveDataFromDatabase(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(BudgetActivity.this);
                BudgetsValueDao budgetsValueDao = database.budgetsValueDao();
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();

                allBudgetsValues = budgetsValueDao.queryAllBudgets();
                allBudgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpSaveButton(allBudgetsTypes);
                        setUpBudgetsListView();
                    }
                });
            }
        });
    }

    private void setUpSaveButton(List<BudgetsType> allBudgetsTypes){
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
        BudgetListAdapter budgetListAdapter = new BudgetListAdapter(this, allBudgetsValues, allBudgetsTypes);
        budgetsList.setAdapter(budgetListAdapter);
    }
}
