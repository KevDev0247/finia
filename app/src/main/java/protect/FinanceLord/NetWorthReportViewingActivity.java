package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import protect.FinanceLord.Communicators.DateCommunicator;
import protect.FinanceLord.NetWorthReportViewingUtils.Report_AssetsFragment;
import protect.FinanceLord.NetWorthReportViewingUtils.Report_LiabilitiesFragment;
import protect.FinanceLord.NetWorthReportViewingUtils.SectionsPagerAdapter;

public class NetWorthReportViewingActivity extends AppCompatActivity {

    public DateCommunicator toViewAssetsFragmentCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_view_report);

        String search = getIntent().getStringExtra(SearchManager.QUERY);
        String itemTime = getIntent().getExtras().getString("itemTime");
        resetView(search, itemTime);
    }

    public void resetView(String search, String date){
        TabLayout tabLayout = findViewById(R.id.report_tab_layout);
        final ViewPager viewPager = findViewById(R.id.report_view_pager);
        Date itemTime = convertDate(date);

        ArrayList<Fragment> fragments = new ArrayList<>();
        Report_AssetsFragment assetsFragment = new Report_AssetsFragment(getString(R.string.assets_name), itemTime);
        Report_LiabilitiesFragment liabilitiesFragment = new Report_LiabilitiesFragment(getString(R.string.liabilities_name));
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public Date convertDate(String itemTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format), Locale.US);
        Date date = null;
        try {
            date = dateFormat.parse(itemTime);
        } catch (ParseException e) {
            Log.d("NetWorthActivity","parse string to date failed");
            e.printStackTrace();
        }
        return date;
    }
}