package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.Database.BudgetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;

public class BudgetActivity extends AppCompatActivity {

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

                List<BudgetsValue> allBudgetsValues = budgetsValueDao.queryAllBudgets();
                final List<BudgetsType> allBudgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpSaveButton(allBudgetsTypes);
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
}
