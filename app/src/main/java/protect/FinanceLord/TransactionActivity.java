package protect.FinanceLord;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.TranscationUtils.CategoryLabelsAdapter;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        resetView();

        retrieveDataFromDatabase();
    }

    private void resetView(){
        ImageButton returnButton = findViewById(R.id.transaction_return_button);
        ImageButton addButton = findViewById(R.id.add_transaction_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void retrieveDataFromDatabase() {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(TransactionActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                final List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshCategoryViewList(budgetsTypes);
                    }
                });
            }
        });
    }

    private void refreshCategoryViewList(List<BudgetsType> budgetsTypes) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoryLabelsList = findViewById(R.id.transaction_label_list);
        categoryLabelsList.setLayoutManager(layoutManager);
        CategoryLabelsAdapter categoryLabelsAdapter = new CategoryLabelsAdapter(this, budgetsTypes);
        categoryLabelsList.setAdapter(categoryLabelsAdapter);
    }
}
