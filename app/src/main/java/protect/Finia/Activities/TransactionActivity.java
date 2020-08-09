package protect.Finia.Activities;

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

import protect.Finia.Communicators.GroupByCategoryCommunicator;
import protect.Finia.Database.BudgetsType;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.Database.Transactions;
import protect.Finia.DAOs.TransactionsDao;
import protect.Finia.R;
import protect.Finia.TransactionViewing.CategoryFiltersAdapter;
import protect.Finia.TransactionViewing.ViewPagerAdapter;
import protect.Finia.TransactionViewing.View_TransactionsFragment;
import protect.Finia.ViewModels.BudgetTypesViewModel;
import protect.Finia.ViewModels.TransactionsViewModel;

/**
 * The activity that displayed the list of transactions in two sections: expenses and revenues.
 * The two sections will be showed in the form of tabs.
 * The activity will allow the user to add or edit their transactions.
 * The ViewModel in android framework is used to communicate between the activity and the fragments.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionActivity extends AppCompatActivity {

    private TransactionsViewModel transactionsViewModel;
    private CategoryFiltersAdapter adapter;
    private List<BudgetsType> budgetsTypes;
    private TransactionsDao transactionsDao;
    private BudgetsTypeDao budgetsTypeDao;
    private View_TransactionsFragment revenuesFragment, expensesFragment;

    private boolean initialize = true;
    private static final int TRANSACTION_ACTIVITY_REQUEST_CODE = 1000;
    private static String TAG = "TransactionActivity";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button and the ViewModel to enable Activity-to-Fragment communication.
     * Lastly, the method will initialize the data access objects used in this activity
     * and call the method to retrieve data from the database.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
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

        FiniaDatabase database = FiniaDatabase.getInstance(TransactionActivity.this);
        transactionsDao = database.transactionsDao();
        budgetsTypeDao = database.budgetsTypeDao();
    }

    /**
     * Call the methods to update the data immediately after the user returned to the page.
     * This method is called first after the activity is created or whenever the user returns to this activity.
     * When the user returned to the activity, the two methods will be called to
     * refresh the data on past report list and the swipe dashboard cards.
     */
    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataFromDatabase(initialize);
        initialize = false;
    }

    /**
     * Retrieve transactions and budgetTypes from the database.
     * The method will first query the transactions and budgetTypes from the database.
     * Query is completed in a separate thread to avoid locking the UI thread for a long period of time.
     * Then, the method will decides whether to set up the view or update the data.
     *
     * @param initialize the variable indicates whether the activity have to initialize the view.
     */
    private void retrieveDataFromDatabase(final boolean initialize) {
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
                            setUpCategoryFiltersList();
                            setUpTabsAndAddButton(transactions);
                        } else {
                            transactionsViewModel.pushToTransactionGroup(transactions);
                            expensesFragment.setUpExpensesEmptyMessage();
                            revenuesFragment.setUpRevenuesEmptyMessage();
                        }
                    }
                });
            }
        });
    }

    /**
     * Set up the adapter and pagers for the tabs as well as the onClickListener for the button.
     * First, the method will initialize the tab layout and the view pager.
     * Then, the method will set up the fragment with the transactions and the budgetTypes queried.
     * Lastly, an onTabSelectedListener will be added to the tab layout to change the color of indicator and color of text when clicked.
     *
     * @param transactions all the transactions inputted
     */
    private void setUpTabsAndAddButton(List<Transactions> transactions) {
        ImageButton addButton = findViewById(R.id.add_transaction_button);
        final TabLayout tabLayout = findViewById(R.id.transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.transaction_view_pager);

        for (Transactions transaction : transactions) {
            Log.d(TAG + " setUpTabsAndAddButton", "this item is " + transaction.getTransactionName() + " value is " + transaction.getTransactionValue());
        }

        ArrayList<Fragment> fragments = new ArrayList<>();
        revenuesFragment = new View_TransactionsFragment(transactions, budgetsTypes, getString(R.string.revenues_fragment_key));
        expensesFragment = new View_TransactionsFragment(transactions, budgetsTypes, getString(R.string.expenses_fragments_key));
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
                startActivityForResult(intent, TRANSACTION_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Set up the adapter for the list of filters of category.
     * First, a recycler view is set up to hold all the filters.
     * Then, an adapter is created to deliver and prepare the view for each filter.
     */
    private void setUpCategoryFiltersList() {
        addFilterAll(budgetsTypes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView categoryFiltersList = findViewById(R.id.transaction_label_list);
        categoryFiltersList.setLayoutManager(layoutManager);
        adapter = new CategoryFiltersAdapter(budgetsTypes, fromAdapterCommunicator);
        categoryFiltersList.setAdapter(adapter);
    }

    /**
     * Add the all filter to the filters list.
     *
     * @param budgetsTypes list of all the budget types stored in the database.
     */
    private void addFilterAll(List<BudgetsType> budgetsTypes) {
        BudgetsType allBudgets = new BudgetsType();
        allBudgets.setBudgetsName(getString(R.string.all));
        budgetsTypes.add(0, allBudgets);
    }

    /**
     * Receive the data when the succeeding activity is finished.
     * When the new data is received, the data source for the adapter is cleared and the new data is assigned to the list.
     * Lastly, the new data list is pushed into the view model to communicate the new data list with the fragment.
     *
     * @param requestCode the request code for identifying the activity.
     * @param resultCode the code to retrieve the results.
     * @param data the intent from the succeeding activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<BudgetsType> newBudgetTypes = (ArrayList<BudgetsType>) data.getSerializableExtra(getString(R.string.transaction_add_new_types_key));

            budgetsTypes.clear();
            budgetsTypes.addAll(newBudgetTypes);
            addFilterAll(budgetsTypes);
            adapter.notifyDataSetChanged();

            BudgetTypesViewModel viewModel = ViewModelProviders.of(this).get(BudgetTypesViewModel.class);
            viewModel.pushToBudgetTypes(newBudgetTypes);
        }
    }

    /**
     * Communicator between this activity and the recycler view adapter.
     * The communicator will transfer the message of filtering to the activity.
     * Method message is implemented by the activity and the object is passed to CalendarDialog.
     * The adapter will call message method to transfer the data to this activity.
     * First, the method query all the budget types from the database.
     * Then, once the for loop finds the match of categoryLabel, a query for the transactions
     * that belong to a particular category will be done and push to the view model.
     */
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
