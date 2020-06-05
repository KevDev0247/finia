package protect.FinanceLord;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.NetWorthReportViewingUtils.ReportPagerAdapter;
import protect.FinanceLord.NetWorthReportViewingUtils.Report_AssetsFragment;
import protect.FinanceLord.NetWorthReportViewingUtils.Report_LiabilitiesFragment;

public class NetWorthReportViewingActivity extends AppCompatActivity {

    String netWorthValue;
    String netWorthDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_view_report);

        ImageButton returnButton = findViewById(R.id.view_report_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String itemTime = getIntent().getExtras().getString(getString(R.string.net_worth_time_key));
        netWorthDifference = getIntent().getExtras().getString(getString(R.string.net_worth_difference_key));
        netWorthValue = getIntent().getExtras().getString(getString(R.string.net_worth_value_key));

        Log.d("NetWorthViewingActivity","the time passed into viewing activity is: " + itemTime);
        Log.d("NetWorthViewingActivity","the value passed into viewing activity is: " + netWorthValue);

        setUpTabs(itemTime);

        retrieveSummaryData(itemTime);
    }

    private void setUpTabs(String date) {
        final TabLayout tabLayout = findViewById(R.id.report_tab_layout);
        final ViewPager viewPager = findViewById(R.id.report_view_pager);
        TextView reportTitle = findViewById(R.id.view_report_title);
        reportTitle.setText(date);

        ArrayList<Fragment> fragments = new ArrayList<>();
        Report_AssetsFragment assetsFragment = new Report_AssetsFragment(convertDate(date));
        Report_LiabilitiesFragment liabilitiesFragment = new Report_LiabilitiesFragment(convertDate(date));
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        ReportPagerAdapter sectionsPagerAdapter = new ReportPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.assets_name))) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#626ee3"));
                    tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#626ee3"));
                } else if (tab.getText().toString().equals(getString(R.string.liabilities_name))) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5ABD5C"));
                    tabLayout.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#5ABD5C"));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void retrieveSummaryData(final String date) {
        final Date itemTime = convertDate(date);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(NetWorthReportViewingActivity.this);
                AssetsValueDao assetsValueDao = database.assetsValueDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                AssetsValue totalAssets = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 35);
                LiabilitiesValue totalLiabilities = liabilitiesValueDao.queryIndividualLiabilityByTime(itemTime.getTime(), 14);
                AssetsValue previousTotalAssetsValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 35);
                LiabilitiesValue previousTotalLiabilitiesValue = liabilitiesValueDao.queryPreviousLiabilityBeforeTime(itemTime.getTime(), 14);

                final float totalAssetsValue = totalAssets.getAssetsValue();
                final float totalLiabilitiesValue = totalLiabilities.getLiabilitiesValue();

                if (previousTotalAssetsValue == null || previousTotalLiabilitiesValue == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshSummaryView(totalAssetsValue, totalLiabilitiesValue, null, null);
                        }
                    });

                } else {
                    final float totalAssetsDifference = totalAssets.getAssetsValue() - previousTotalAssetsValue.getAssetsValue();
                    final float totalLiabilitiesDifference = totalLiabilities.getLiabilitiesValue() - previousTotalLiabilitiesValue.getLiabilitiesValue();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshSummaryView(totalAssetsValue, totalLiabilitiesValue, totalAssetsDifference, totalLiabilitiesDifference);
                        }
                    });
                }

            }
        });
    }

    private void refreshSummaryView(float totalAssetsValue, float totalLiabilitiesValue, Float totalAssetsDifference, Float totalLiabilitiesDifference) {
        TextView reportNetWorthValue = findViewById(R.id.total_net_worth_value);
        TextView reportNetWorthSymbol = findViewById(R.id.total_net_worth_symbol);
        TextView reportNetWorthDifference = findViewById(R.id.total_net_worth_difference);
        View reportNetWorthDifferenceBlock = findViewById(R.id.total_net_worth_difference_block);

        TextView reportTotalAssetsValue = findViewById(R.id.total_assets_value);
        TextView reportTotalAssetsSymbol = findViewById(R.id.total_assets_symbol);
        TextView reportTotalAssetsDifference = findViewById(R.id.total_assets_difference);
        View reportTotalAssetsDifferenceBlock = findViewById(R.id.total_assets_difference_block);

        TextView reportTotalLiabilitiesValue = findViewById(R.id.total_liabilities_value);
        TextView reportTotalLiabilitiesSymbol = findViewById(R.id.total_liabilities_symbol);
        TextView reportTotalLiabilitiesDifference = findViewById(R.id.total_liabilities_difference);
        View reportLiabilitiesDifferenceBlock = findViewById(R.id.total_liabilities_difference_block);

        reportNetWorthValue.setText(netWorthValue);
        if (netWorthDifference.equals(getString(R.string.no_data_initialization))){
            reportNetWorthSymbol.setText("");
            reportNetWorthDifference.setText(getString(R.string.no_data_initialization));

        } else if (Float.parseFloat(netWorthDifference) == 0){
            reportNetWorthSymbol.setText("");
            reportNetWorthDifference.setText(netWorthDifference);

        } else if (Float.parseFloat(netWorthDifference) > 0){
            reportNetWorthDifferenceBlock.setBackgroundResource(R.drawable.net_increase);
            reportNetWorthSymbol.setText(R.string.positive_symbol);
            reportNetWorthDifference.setText(netWorthDifference);

        } else if (Float.parseFloat(netWorthDifference) < 0) {
            reportNetWorthDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            reportNetWorthSymbol.setText(R.string.negative_symbol);
            reportNetWorthDifference.setText(netWorthDifference);
        }

        reportTotalAssetsValue.setText(String.valueOf(totalAssetsValue));
        if (totalAssetsDifference == null){
            reportTotalAssetsSymbol.setText("");
            reportTotalAssetsDifference.setText(getString(R.string.no_data_initialization));

        } else if (totalAssetsDifference == 0) {
            reportTotalAssetsSymbol.setText("");
            reportTotalAssetsDifference.setText(String.valueOf(totalAssetsDifference));

        } else if (totalAssetsDifference > 0){
            reportTotalAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);
            reportTotalAssetsSymbol.setText(R.string.positive_symbol);
            reportTotalAssetsDifference.setText(String.valueOf(totalAssetsDifference));

        } else if (totalAssetsDifference < 0){
            reportTotalAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            reportTotalAssetsSymbol.setText(R.string.negative_symbol);
            reportTotalAssetsDifference.setText(String.valueOf(totalAssetsDifference));
        }

        reportTotalLiabilitiesValue.setText(String.valueOf(totalLiabilitiesValue));
        if (totalLiabilitiesDifference == null){
            reportTotalLiabilitiesSymbol.setText("");
            reportTotalLiabilitiesDifference.setText(getString(R.string.no_data_initialization));

        }else if (totalLiabilitiesDifference == 0){
            reportTotalLiabilitiesSymbol.setText("");
            reportTotalLiabilitiesDifference.setText(String.valueOf(totalLiabilitiesDifference));

        } else if (totalLiabilitiesDifference > 0){
            reportLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_increase);
            reportTotalLiabilitiesSymbol.setText(R.string.positive_symbol);
            reportTotalLiabilitiesDifference.setText(String.valueOf(totalLiabilitiesDifference));

        } else if (totalLiabilitiesDifference < 0){
            reportLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            reportTotalLiabilitiesSymbol.setText(R.string.negative_symbol);
            reportTotalLiabilitiesDifference.setText(String.valueOf(totalLiabilitiesDifference));
        }
    }

    private Date convertDate(String itemTime) {
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