package protect.FinanceLord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.TransactionViewingUtils.CategoryLabelsAdapter;
import protect.FinanceLord.TransactionViewingUtils.View_ExpensesFragment;
import protect.FinanceLord.TransactionViewingUtils.View_RevenuesFragment;
import protect.FinanceLord.TransactionViewingUtils.ViewPagerAdapter;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        resetView();

        retrieveDataFromDatabase();
    }

    private void resetView() {
        ImageButton returnButton = findViewById(R.id.transaction_return_button);
        ImageButton addButton = findViewById(R.id.add_transaction_button);
        TabLayout tablayout = findViewById(R.id.transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.transaction_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        View_ExpensesFragment expensesFragment = new View_ExpensesFragment();
        View_RevenuesFragment revenuesFragment = new View_RevenuesFragment();
        fragments.add(expensesFragment);
        fragments.add(revenuesFragment);

        ViewPagerAdapter sectionsPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tablayout.setupWithViewPager(viewPager);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TransactionActivity.this, TransactionEditActivity.class);
                startActivity(intent);
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
