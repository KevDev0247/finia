package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

import protect.FinanceLord.ui.NetWorthEditReports.AssetsFragment;
import protect.FinanceLord.ui.NetWorthEditReports.LiabilitiesFragment;
import protect.FinanceLord.ui.NetWorthEditReports.SectionsPagerAdapter;

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
        final ViewPager viewPager = findViewById(R.id.view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        AssetsFragment assetsFragment = new AssetsFragment("Assets");
        LiabilitiesFragment liabilitiesFragment = new LiabilitiesFragment("Liabilities");
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);

        tabLayout.setupWithViewPager(viewPager);
        
    }
}