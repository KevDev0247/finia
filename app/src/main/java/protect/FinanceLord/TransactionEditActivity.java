package protect.FinanceLord;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import protect.FinanceLord.TransactionEditingUtils.EditPagerAdapter;
import protect.FinanceLord.TransactionEditingUtils.Edit_ExpensesFragment;
import protect.FinanceLord.TransactionEditingUtils.Edit_RevenuesFragment;

public class TransactionEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);

        resetView();
    }

    private void resetView(){
        ImageButton returnButton = findViewById(R.id.edit_transaction_return_button);
        TabLayout tablayout = findViewById(R.id.edit_transaction_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_transaction_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        Edit_ExpensesFragment expensesFragment = new Edit_ExpensesFragment();
        Edit_RevenuesFragment revenuesFragment = new Edit_RevenuesFragment();
        fragments.add(expensesFragment);
        fragments.add(revenuesFragment);

        EditPagerAdapter sectionsPagerAdapter = new EditPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tablayout.setupWithViewPager(viewPager);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
