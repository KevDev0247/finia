package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import protect.FinanceLord.Communicators.SaveDataCommunicator;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;
import protect.FinanceLord.TransactionEditingUtils.EditPagerAdapter;
import protect.FinanceLord.TransactionEditingUtils.Edit_ExpensesFragment;
import protect.FinanceLord.TransactionEditingUtils.Edit_RevenuesFragment;

public class TransactionEditActivity extends AppCompatActivity {

    Date currentTime;
    public SaveDataCommunicator toEditExpensesCommunicator;
    public SaveDataCommunicator toEditRevenuesCommunicator;

    private static final String TAG = "TransactionEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);
        ArrayList<BudgetTypesDataModel> dataModels = getIntent().getExtras().getParcelableArrayList(getString(R.string.budget_categories_key));

        resetView(dataModels);
    }

    private void resetView(List<BudgetTypesDataModel> dataModels){
        ImageButton returnButton = findViewById(R.id.edit_transaction_return_button);
        ImageButton saveButton = findViewById(R.id.save_transaction_button);
        final TabLayout tablayout = findViewById(R.id.edit_transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_transaction_view_pager);

        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        ArrayList<Fragment> fragments = new ArrayList<>();
        Edit_ExpensesFragment expensesFragment = new Edit_ExpensesFragment(currentTime, dataModels);
        Edit_RevenuesFragment revenuesFragment = new Edit_RevenuesFragment(currentTime, dataModels);
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
                    toEditExpensesCommunicator.onActivityMessage();

                } else if (tablayout.getTabAt(viewPager.getCurrentItem()).getText().toString().equals(getString(R.string.revenues_name))){
                    toEditRevenuesCommunicator.onActivityMessage();
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
