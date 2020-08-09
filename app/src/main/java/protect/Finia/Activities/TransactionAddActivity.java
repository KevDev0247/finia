package protect.Finia.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import protect.Finia.Communicators.SaveDataCommunicator;
import protect.Finia.Database.BudgetsType;
import protect.Finia.R;
import protect.Finia.TransactionAdding.Add_TransactionsFragment;
import protect.Finia.TransactionAdding.EditPagerAdapter;
import protect.Finia.ViewModels.BudgetTypesViewModel;

/**
 * The activity that enables the user to add a transaction item into the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionAddActivity extends AppCompatActivity {

    Date currentTime;
    public SaveDataCommunicator toEditExpensesCommunicator;
    public SaveDataCommunicator toEditRevenuesCommunicator;

    private static final String TAG = "TransactionEditActivity";

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button and the currentTime to the default current time.
     * Lastly, the method will call the method to set up tabs and save button as well as set up the observer.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_add);
        ArrayList<BudgetsType> budgetsTypes = getIntent().getExtras().getParcelableArrayList(getString(R.string.budget_categories_key));

        ImageButton returnButton = findViewById(R.id.transaction_add_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        setUpTabsAndSaveButton(budgetsTypes);

        setUpBudgetTypesObserver();
    }

    /**
     * Set up the expenses tab and revenue tab as well as the save button.
     * The SaveDataCommunicator is used to communicate with the fragments about saving data.
     * The method will determine which fragment the message will be sent to based on the fragment the user is at.
     *
     * @param budgetsTypes the budget types stored in the database.
     */
    private void setUpTabsAndSaveButton(List<BudgetsType> budgetsTypes){
        ImageButton saveButton = findViewById(R.id.transaction_add_save_button);
        final TabLayout tablayout = findViewById(R.id.add_transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_transaction_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        budgetsTypes.remove(0);
        Add_TransactionsFragment revenuesFragment = new Add_TransactionsFragment(currentTime, budgetsTypes, getString(R.string.revenues_fragment_key));
        Add_TransactionsFragment expensesFragment = new Add_TransactionsFragment(currentTime, budgetsTypes, getString(R.string.expenses_fragments_key));
        fragments.add(expensesFragment);
        fragments.add(revenuesFragment);

        EditPagerAdapter sectionsPagerAdapter = new EditPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tablayout.setupWithViewPager(viewPager);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,tablayout.getTabAt(viewPager.getCurrentItem()).getText().toString() + " Fragment has data to save");
                if (tablayout.getTabAt(viewPager.getCurrentItem()).getText().toString().equals(getString(R.string.expenses_name))){
                    toEditExpensesCommunicator.message();
                } else if (tablayout.getTabAt(viewPager.getCurrentItem()).getText().toString().equals(getString(R.string.revenues_name))){
                    toEditRevenuesCommunicator.message();
                }
            }
        });
    }

    /**
     * Receive and transfer the new type the user created to the TransactionActivity for display.
     * The view model will detect the change of budget type list.
     * Once the list has been updated, mostly in the database helper class,
     * the updated class will be transferred to the preceding activity for display.
     */
    private void setUpBudgetTypesObserver() {
        BudgetTypesViewModel viewModel = ViewModelProviders.of(this).get(BudgetTypesViewModel.class);
        viewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
                ArrayList<BudgetsType> budgetsTypes = new ArrayList<>(newBudgetsTypes);
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getString(R.string.transaction_add_new_types_key), budgetsTypes);
                setResult(Activity.RESULT_OK, intent);
            }
        });
    }
}
