package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

import protect.FinanceLord.NetWorthEditReportsUtils.EditAssetsFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.EditLiabilitiesFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.SectionsPagerAdapter;

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
        EditAssetsFragment assetsFragment = new EditAssetsFragment("Assets");
        EditLiabilitiesFragment liabilitiesFragment = new EditLiabilitiesFragment("Liabilities");
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        
    }
}