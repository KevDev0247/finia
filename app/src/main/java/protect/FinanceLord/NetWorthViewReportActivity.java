package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;
import protect.FinanceLord.NetWorthEditReportsUtils.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthReportTemplateUtils.Report_AssetsFragment;
import protect.FinanceLord.NetWorthReportTemplateUtils.Report_LiabilitiesFragment;
import protect.FinanceLord.NetWorthReportTemplateUtils.SectionsPagerAdapter;

public class NetWorthViewReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_view_report);

        String search = getIntent().getStringExtra(SearchManager.QUERY);
        resetView(search);
    }

    public void resetView(String search){
        TabLayout tabLayout = findViewById(R.id.report_tab_layout);
        final ViewPager viewPager = findViewById(R.id.report_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        Report_AssetsFragment assetsFragment = new Report_AssetsFragment("Assets");
        Report_LiabilitiesFragment liabilitiesFragment = new Report_LiabilitiesFragment("Liabilities");
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}