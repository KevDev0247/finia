package protect.FinanceLord;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsType;
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesType;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.Database.ReportItemInfo;
import protect.FinanceLord.Database.ReportItemInfoDao;
import protect.FinanceLord.NetWorthPastReportsListUtils.PastReportsAdapter;
import protect.FinanceLord.NetWorthPastReportsListUtils.ReportItemsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsAdapter;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsDataModel;

public class NetWorthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth);

        ImageButton returnButton = findViewById(R.id.net_worth_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton editReportButton = findViewById(R.id.editReport);
        editReportButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NetWorthActivity.this, NetWorthReportEditingActivity.class);
                startActivity(intent);
            }
        });

        refreshNetWorthCardsView();

        refreshPastReportsListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshPastReportsListView();
        refreshNetWorthCardsView();

        Log.d("NetWorthActivity", "Activity has been refreshed");
    }

    protected void refreshPastReportsListView() {
        ListView pastReportsListView = findViewById(R.id.past_report_list);
        final List<ReportItemsDataModel> dataSources = new ArrayList<>();
        final PastReportsAdapter adapter = new PastReportsAdapter(this, dataSources);

        pastReportsListView.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                FinanceLordDatabase database = FinanceLordDatabase.getInstance(NetWorthActivity.this);
                ReportItemInfoDao reportItemInfoDao = database.reportItemInfoDao();

                List<ReportItemInfo> reportItemInfoList = reportItemInfoDao.queryReportItemsInfo();
                for (ReportItemInfo reportItemInfo : reportItemInfoList){
                    String difference = getString(R.string.no_data_initialization);
                    if (reportItemInfoList.indexOf(reportItemInfo) + 1 <= reportItemInfoList.size() - 1){
                        ReportItemInfo previousReportItemInfo = reportItemInfoList.get(reportItemInfoList.indexOf(reportItemInfo) + 1);
                        difference = String.valueOf(reportItemInfo.netWorthValue - previousReportItemInfo.netWorthValue);
                    }
                    Log.d("NetWorthActivity", " the time of current item is: " + reportItemInfo.totalAssetsDate + "  the difference of this item is: " + difference);
                    ReportItemsDataModel dataModel = new ReportItemsDataModel(reportItemInfo.totalAssetsDate, reportItemInfo.netWorthValue, difference);
                    dataSources.add(dataModel);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        pastReportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ReportItemsDataModel dataModel = dataSources.get(position);
                Log.d("NetWorthActivity", "the user has select the report of time: " + dataModel.time);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.net_worth_time_key), dataModel.time);
                intent.putExtra(getString(R.string.net_worth_value_key), String.valueOf(dataModel.netWorthValue));
                intent.putExtra(getString(R.string.net_worth_difference_key), dataModel.difference);
                intent.setClass(NetWorthActivity.this, NetWorthReportViewingActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void refreshNetWorthCardsView() {
        NetWorthCardsAdapter adapter;
        ViewPager viewPager;
        List<NetWorthCardsDataModel> dataModels = new ArrayList<>();

        adapter = new NetWorthCardsAdapter(dataModels,this);

        viewPager = findViewById(R.id.assets_cards_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        loadDataToCards(dataModels, adapter);
    }

    protected void loadDataToCards(final List<NetWorthCardsDataModel> dataModels, final NetWorthCardsAdapter adapter) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                float totalAssetsValue = 0;
                float liquidAssetsValue = 0;
                float investedAssetsValue = 0;
                float personalAssetsValue = 0;
                float taxableAccountsValue = 0;
                float retirementAccountsValue = 0;
                float ownershipInterestsValue = 0;

                float totalLiabilitiesValue = 0;
                float shortTermLiabilitiesValue = 0;
                float longTermLiabilitiesValue = 0;

                FinanceLordDatabase database = FinanceLordDatabase.getInstance(NetWorthActivity.this);
                AssetsValueDao assetsValueDao = database.assetsValueDao();
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();

                List<AssetsValue> assetsValues = assetsValueDao.queryAllAssetsValue();
                List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryAllLiabilities();

                if (!assetsValues.isEmpty()){
                    AssetsType totalAssets = assetsTypeDao.queryAssetsByType(getString(R.string.total_assets_name));
                    AssetsType liquidAssets = assetsTypeDao.queryAssetsByType(getString(R.string.liquid_assets_name));
                    AssetsType investedAssets = assetsTypeDao.queryAssetsByType(getString(R.string.invested_assets_name));
                    AssetsType personalAssets = assetsTypeDao.queryAssetsByType(getString(R.string.personal_assets_name));
                    AssetsType taxableAccounts = assetsTypeDao.queryAssetsByType(getString(R.string.taxable_accounts_name));
                    AssetsType retirementAccounts = assetsTypeDao.queryAssetsByType(getString(R.string.retirement_accounts_name));
                    AssetsType ownershipInterests = assetsTypeDao.queryAssetsByType(getString(R.string.ownership_interest_name));

                    if (totalAssets != null && liquidAssets != null && investedAssets != null && personalAssets != null && taxableAccounts != null && retirementAccounts != null && ownershipInterests != null){
                        totalAssetsValue = assetsValueDao.queryLatestIndividualAsset(totalAssets.getAssetsId()).getAssetsValue();
                        liquidAssetsValue = assetsValueDao.queryLatestIndividualAsset(liquidAssets.getAssetsId()).getAssetsValue();
                        investedAssetsValue = assetsValueDao.queryLatestIndividualAsset(investedAssets.getAssetsId()).getAssetsValue();
                        personalAssetsValue = assetsValueDao.queryLatestIndividualAsset(personalAssets.getAssetsId()).getAssetsValue();
                        taxableAccountsValue = assetsValueDao.queryLatestIndividualAsset(taxableAccounts.getAssetsId()).getAssetsValue();
                        retirementAccountsValue = assetsValueDao.queryLatestIndividualAsset(retirementAccounts.getAssetsId()).getAssetsValue();
                        ownershipInterestsValue = assetsValueDao.queryLatestIndividualAsset(ownershipInterests.getAssetsId()).getAssetsValue();

                        Log.d("NetWorthActivity","asset items value has been updated");
                    } else {
                        Log.d("NetWorthActivity","some asset items are null");
                    }
                }

                if (!liabilitiesValues.isEmpty()){
                    LiabilitiesType totalLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.total_liabilities_name));
                    LiabilitiesType shortTermLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.short_term_liabilities_name));
                    LiabilitiesType longTermLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.long_term_liabilities_name));

                    if (totalLiabilities != null && shortTermLiabilities != null && longTermLiabilities != null){
                        totalLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(totalLiabilities.getLiabilitiesId()).getLiabilitiesValue();
                        shortTermLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(shortTermLiabilities.getLiabilitiesId()).getLiabilitiesValue();
                        longTermLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(longTermLiabilities.getLiabilitiesId()).getLiabilitiesValue();

                        Log.d("NetWorthActivity","liability items value has been updated");
                    } else {
                        Log.d("NetWorthActivity","some liability items are null");
                    }
                }

                final float finalTotalAssetsValue = totalAssetsValue;
                final float finalLiquidAssetsValue = liquidAssetsValue;
                final float finalInvestedAssetsValue = investedAssetsValue;
                final float finalPersonalAssetsValue = personalAssetsValue;
                final float finalTaxableAccountsValue = taxableAccountsValue;
                final float finalRetirementAccountsValue = retirementAccountsValue;
                final float finalOwnershipInterestsValue = ownershipInterestsValue;

                final float finalTotalLiabilitiesValue = totalLiabilitiesValue;
                final float finalShortTermLiabilitiesValue = shortTermLiabilitiesValue;
                final float finalLongTermLiabilitiesValue = longTermLiabilitiesValue;

                final float finalNetWorthValue = finalTotalAssetsValue - finalTotalLiabilitiesValue;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.net_worth, getString(R.string.net_worth),String.valueOf(finalNetWorthValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_total, getString(R.string.total_assets_name), String.valueOf(finalTotalAssetsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_liquid, getString(R.string.liquid_assets_name), String.valueOf(finalLiquidAssetsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_invested, getString(R.string.invested_assets_name), String.valueOf(finalInvestedAssetsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_personal, getString(R.string.personal_assets_name), String.valueOf(finalPersonalAssetsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_taxable_accounts, getString(R.string.taxable_accounts_name), String.valueOf(finalTaxableAccountsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_retirement, getString(R.string.retirement_accounts_name), String.valueOf(finalRetirementAccountsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_ownership,getString(R.string.ownership_interest_name), String.valueOf(finalOwnershipInterestsValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_total, getString(R.string.total_liabilities_name),String.valueOf(finalTotalLiabilitiesValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_short_term, getString(R.string.short_term_liabilities_name),String.valueOf(finalShortTermLiabilitiesValue)));
                        dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_long_term, getString(R.string.long_term_liabilities_name), String.valueOf(finalLongTermLiabilitiesValue)));

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
