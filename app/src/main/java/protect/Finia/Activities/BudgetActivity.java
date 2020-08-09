package protect.Finia.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.Finia.BudgetModule.BudgetInfo;
import protect.Finia.BudgetModule.BudgetListAdapter;
import protect.Finia.Database.BudgetsType;
import protect.Finia.DAOs.BudgetsTypeDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.DAOs.BudgetInfoDao;
import protect.Finia.R;

/**
 * The activity that displayed the list of budgets.
 * The activity will allow the user to view their budgets.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetActivity extends AppCompatActivity {

    private BudgetListAdapter budgetListAdapter;
    private ArrayList<BudgetsType> allBudgetsTypes;
    private LinearLayout emptyMessageField;

    private boolean initialize = true;
    private static final int BUDGET_ACTIVITY_REQUEST_CODE = 1000;
    private String TAG = "BudgetActivity";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
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

    /**
     * Call the methods to update the data immediately after the user returned to the page.
     * This method is called first after the activity is created or whenever the user returns to this activity.
     * When the user returned to the activity or the activity is created, the method to retrieve data is called.
     */
    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataFromDatabase(initialize);
        initialize = false;
    }

    /**
     * Retrieve budgetTypes and budget information from the database.
     * The method will first query the budget information and budgetTypes from the database.
     * Query is completed in a separate thread to avoid locking the UI thread for a long period of time.
     * Then, the method will decides whether to set up the view or update the data.
     *
     * @param initialize the variable indicates whether the activity have to initialize the view.
     */
    private void retrieveDataFromDatabase(final boolean initialize) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(BudgetActivity.this);
                BudgetsTypeDao budgetsTypeDao = database.budgetsTypeDao();
                BudgetInfoDao budgetInfoDao = database.financeRecordsDao();

                List<BudgetsType> budgetsTypes = budgetsTypeDao.queryAllBudgetsTypes();
                allBudgetsTypes = new ArrayList<>(budgetsTypes);
                final List<BudgetInfo> budgetInfoList = budgetInfoDao.queryFinancialRecords();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize) {
                            setUpAddButton();
                            setUpBudgetsListView(budgetInfoList);
                        } else {
                            if (budgetListAdapter != null) {
                                for (BudgetInfo budgetInfo : budgetInfoList) {
                                    Log.d(TAG, " this financial record id is " + budgetInfo.budgetCategoryId +
                                            " total value is " + budgetInfo.budgetTotal +
                                            " total usage is" + budgetInfo.totalUsage +
                                            " date start is " + budgetInfo.dateStart +
                                            " date end is " + budgetInfo.dateEnd);
                                }
                                budgetListAdapter.clear();
                                budgetListAdapter.addAll(budgetInfoList);
                                budgetListAdapter.notifyDataSetChanged();
                                setUpEmptyMessage(budgetInfoList);
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Set up the add button's layout and onClickListener.
     * The onClickListener will navigate the user to the add budget page.
     */
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

    /**
     * Set up budgets list view's adapter and onItemClickListener.
     * The budgetInfoList is passed into the adapter to deliver the data to the UI widgets.
     * The onItemClickListener will navigate the user to the edit sheet.
     * the data of the budget is stored in the intent and transferred to the edit activity.
     *
     * @param budgetInfoList the list of budget item information from the database.
     */
    private void setUpBudgetsListView(final List<BudgetInfo> budgetInfoList) {
        ListView budgetsList = findViewById(R.id.budgets_list);
        emptyMessageField = findViewById(R.id.budget_initialization_message);
        budgetListAdapter = new BudgetListAdapter(this, budgetInfoList, allBudgetsTypes);
        budgetsList.setAdapter(budgetListAdapter);

        setUpEmptyMessage(budgetInfoList);

        budgetsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BudgetInfo budgetInfo = budgetInfoList.get(position);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.budget_categories_key), allBudgetsTypes);
                intent.putExtra(getString(R.string.budget_access_key), getString(R.string.edit_budget_access_key));
                intent.putExtra(getString(R.string.budget_id_key), budgetInfo.budgetId);
                intent.putExtra(getString(R.string.budget_name_id_key), budgetInfo.budgetCategoryId);
                intent.putExtra(getString(R.string.budget_total_key), budgetInfo.budgetTotal);
                intent.putExtra(getString(R.string.budget_start_date_key), budgetInfo.dateStart);
                intent.putExtra(getString(R.string.budget_end_date_key), budgetInfo.dateEnd);
                intent.setClass(BudgetActivity.this, BudgetEditActivity.class);
                startActivityForResult(intent, BUDGET_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    /**
     * Set up the empty message based on the status of the budgetInfoList.
     * If the budgetInfoList is empty, the empty message will be visible.
     * If the budgetInfoList has items, the empty message will be invisible.
     *
     * @param budgetInfoList the list of budget item information from the database.
     */
    private void setUpEmptyMessage(List<BudgetInfo> budgetInfoList) {
        if (budgetInfoList.size() != 0) {
            emptyMessageField.setVisibility(View.GONE);
        } else {
            emptyMessageField.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Receive the data when the succeeding activity is finished.
     * When the new data is received, the data source for the adapter is cleared and the new data is assigned to the list.
     * Lastly, notify the adapter to update the list.
     *
     * @param requestCode the request code for identifying the activity.
     * @param resultCode the code to retrieve the results.
     * @param data the intent from the succeeding activity.
     */
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
