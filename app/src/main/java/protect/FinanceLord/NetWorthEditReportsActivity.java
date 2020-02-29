package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import protect.FinanceLord.ui.main.SectionsPagerAdapter;

public class NetWorthEditReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_reports);

        String search = getIntent().getStringExtra(SearchManager.QUERY);
        resetView(search);
    }

    private void resetView(String search){
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Assets"));
        tabLayout.addTab(tabLayout.newTab().setText("Liabilities"));

        ViewPager viewPager = findViewById(R.id.view_pager);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);


    }
}