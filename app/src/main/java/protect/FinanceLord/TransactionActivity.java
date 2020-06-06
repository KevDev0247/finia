package protect.FinanceLord;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.GroupByCategoryCommunicator;
import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsTypeDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.Database.TransactionsDao;
import protect.FinanceLord.TransactionViewingUtils.CategoryLabelsAdapter;
import protect.FinanceLord.TransactionViewingUtils.ViewPagerAdapter;
import protect.FinanceLord.TransactionViewingUtils.View_TransactionsFragment;
import protect.FinanceLord.ViewModels.BudgetTypesViewModel;
import protect.FinanceLord.ViewModels.TransactionsViewModel;

public class TransactionActivity extends AppCompatActivity {

    private TransactionsViewModel transactionsViewModel;
    private CategoryLabelsAdapter adapter;
    private List<BudgetsType> budgetsTypes;
    private TransactionsDao transactionsDao;
    private BudgetsTypeDao budgetsTypeDao;

    static final int MAIN_ACTIVITY_REQUEST_CODE = 1000;
    private static String TAG = "TransactionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        transactionsViewModel = ViewModelProviders.of(TransactionActivity.this).get(TransactionsViewModel.class);
        ImageButton returnButton = findViewById(R.id.transaction_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FinanceLordDatabase database = FinanceLordDatabase.getInstance(TransactionActivity.this);
        transactionsDao = database.transactionsDao();
        budgetsTypeDao = database.budgetsTypeDao();

        retrieveDataFromDatabase(true, false);
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
                final List<Transactions> transactions = transactionsDao.queryAllTransaction();
                budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                for (Transactions transaction : transactions) {
                    Log.d(TAG, "the item is " + transaction.getTransactionName() +
                            " the value is " + transaction.getTransactionValue() +
                            " the category is " + transaction.getTransactionCategoryId());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize){
                            setUpCategoryViewList();
                            setUpTabsAndAddButton(transactions);
                        } else if (refresh){
                            transactionsViewModel.pushToTransactionGroup(transactions);
                        }
                    }
                });
            }
        });
    }

    private void setUpTabsAndAddButton(List<Transactions> transactions) {
        ImageButton addButton = findViewById(R.id.add_transaction_button);
        final TabLayout tabLayout = findViewById(R.id.transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.transaction_view_pager);

        for (Transactions transaction : transactions) {
            Log.d(TAG + " setUpTabsAndAddButton", "this item is " + transaction.getTransactionName() + " value is " + transaction.getTransactionValue());
        }

        ArrayList<Fragment> fragments = new ArrayList<>();
        View_TransactionsFragment revenuesFragment = new View_TransactionsFragment(transactions, budgetsTypes, getString(R.string.revenues_fragment_key));
        View_TransactionsFragment expensesFragment = new View_TransactionsFragment(transactions, budgetsTypes, getString(R.string.expenses_fragments_key));
        fragments.add(revenuesFragment);
        fragments.add(expensesFragment);

        ViewPagerAdapter sectionsPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.expenses_name))) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#9DAE9D"));
                    tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#9DAE9D"));
                } else if (tab.getText().toString().equals(getString(R.string.revenues_name))) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#B592F1"));
                    tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#B592F1"));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        final ArrayList<BudgetsType> finalBudgetsTypes = new ArrayList<>(budgetsTypes);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getString(R.string.budget_categories_key), finalBudgetsTypes);
                intent.setClass(TransactionActivity.this, TransactionAddActivity.class);
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void setUpCategoryViewList() {
        addLabelAll(budgetsTypes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoryLabelsList = findViewById(R.id.transaction_label_list);
        categoryLabelsList.setLayoutManager(layoutManager);
        adapter = new CategoryLabelsAdapter(budgetsTypes, fromAdapterCommunicator);
        categoryLabelsList.setAdapter(adapter);
    }

    private void addLabelAll(List<BudgetsType> budgetsTypes) {
        BudgetsType allBudgets = new BudgetsType();
        allBudgets.setBudgetsName(getString(R.string.all));
        budgetsTypes.add(0, allBudgets);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<BudgetsType> newBudgetTypes = (ArrayList<BudgetsType>) data.getSerializableExtra(getString(R.string.transaction_add_new_types_key));

            budgetsTypes.clear();
            budgetsTypes.addAll(newBudgetTypes);
            addLabelAll(budgetsTypes);
            adapter.notifyDataSetChanged();

            BudgetTypesViewModel viewModel = ViewModelProviders.of(this).get(BudgetTypesViewModel.class);
            viewModel.pushToBudgetTypes(newBudgetTypes);
        }
    }

    private GroupByCategoryCommunicator fromAdapterCommunicator = new GroupByCategoryCommunicator() {
        @Override
        public void message(final String categoryLabel) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    final List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();

                    List<Transactions> groupedTransactions = new ArrayList<>();
                    for (BudgetsType budgetsType : budgetsTypes) {
                        if (budgetsType.getBudgetsName().equals(categoryLabel)) {
                            groupedTransactions = transactionsDao.queryTransactionByCategoryId(budgetsType.getBudgetsCategoryId());
                            Log.d(TAG,"budget type name is " + budgetsType.getBudgetsName());
                        }
                    }

                    if (getString(R.string.all).equals(categoryLabel)){
                        groupedTransactions = transactionsDao.queryAllTransaction();
                    }

                    final List<Transactions> finalGroupedTransactions = groupedTransactions;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transactionsViewModel.pushToTransactionGroup(finalGroupedTransactions);
                        }
                    });
                }
            });
        }
    };
}
