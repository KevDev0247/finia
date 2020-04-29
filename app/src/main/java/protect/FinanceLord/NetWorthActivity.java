package protect.FinanceLord;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsType;
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthPastReportsListUtils.PastReportsAdapter;
import protect.FinanceLord.NetWorthPastReportsListUtils.ReportItemsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsAdapter;

public class NetWorthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth);

        createPastReportsListView();

        createAssetsCardsView();

        Button editReportButton = findViewById(R.id.editReport);
        editReportButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NetWorthActivity.this, NetWorthEditReportActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void createPastReportsListView(){
        ArrayList<ReportItemsDataModel> dataSources = new ArrayList<>();
        PastReportsAdapter adapter;
        ListView pastReportsListView = findViewById(R.id.past_report_list);

        adapter = new PastReportsAdapter(this, dataSources);
        pastReportsListView.setAdapter(adapter);
    }

    protected void createAssetsCardsView(){

        NetWorthCardsAdapter adapter;
        ViewPager viewPager;
        List<NetWorthCardsDataModel> dataModels = new ArrayList<>();

        adapter = new NetWorthCardsAdapter(dataModels,this);

        viewPager = findViewById(R.id.assets_cards_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        loadDataToCards(dataModels, adapter);
    }

    protected void loadDataToCards(final List<NetWorthCardsDataModel> dataModels, final NetWorthCardsAdapter adapter){
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

                FinanceLordDatabase database = FinanceLordDatabase.getInstance(NetWorthActivity.this);
                AssetsValueDao assetsValueDao = database.assetsValueDao();
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();

                AssetsType totalAssets = assetsTypeDao.queryAssetsByType(getString(R.string.total_assets));
                AssetsType liquidAssets = assetsTypeDao.queryAssetsByType(getString(R.string.liquid_assets));
                AssetsType investedAssets = assetsTypeDao.queryAssetsByType(getString(R.string.invested_assets));
                AssetsType personalAssets = assetsTypeDao.queryAssetsByType(getString(R.string.personal_assets));
                AssetsType taxableAccounts = assetsTypeDao.queryAssetsByType(getString(R.string.taxable_accounts));
                AssetsType retirementAccounts = assetsTypeDao.queryAssetsByType(getString(R.string.retirement_accounts));
                AssetsType ownershipInterests = assetsTypeDao.queryAssetsByType(getString(R.string.ownership_interest));

                if (totalAssets != null && liquidAssets != null && investedAssets != null && personalAssets != null && taxableAccounts != null && retirementAccounts != null && ownershipInterests != null){
                    totalAssetsValue = assetsValueDao.queryLatestIndividualAsset(totalAssets.getAssetsId()).getAssetsValue();
                    liquidAssetsValue = assetsValueDao.queryLatestIndividualAsset(liquidAssets.getAssetsId()).getAssetsValue();
                    investedAssetsValue = assetsValueDao.queryLatestIndividualAsset(investedAssets.getAssetsId()).getAssetsValue();
                    personalAssetsValue = assetsValueDao.queryLatestIndividualAsset(personalAssets.getAssetsId()).getAssetsValue();
                    taxableAccountsValue = assetsValueDao.queryLatestIndividualAsset(taxableAccounts.getAssetsId()).getAssetsValue();
                    retirementAccountsValue = assetsValueDao.queryLatestIndividualAsset(retirementAccounts.getAssetsId()).getAssetsValue();
                    ownershipInterestsValue = assetsValueDao.queryLatestIndividualAsset(ownershipInterests.getAssetsId()).getAssetsValue();

                    Log.d("NetWorthActivity","items value has been updated");
                } else {
                    Log.d("NetWorthActivity","some items are null");
                }

                dataModels.add(new NetWorthCardsDataModel(R.drawable.net_worth, getString(R.string.net_worth),"0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_total, getString(R.string.total_assets), String.valueOf(totalAssetsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_liquid, getString(R.string.liquid_assets), String.valueOf(liquidAssetsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_invested, getString(R.string.invested_assets), String.valueOf(investedAssetsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_personal, getString(R.string.personal_assets), String.valueOf(personalAssetsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_taxable_accounts, getString(R.string.taxable_accounts), String.valueOf(taxableAccountsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_retirement, getString(R.string.retirement_accounts), String.valueOf(retirementAccountsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_ownership,getString(R.string.ownership_interest), String.valueOf(ownershipInterestsValue)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_total, getString(R.string.total_liabilities),"0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_short_term, getString(R.string.short_term_liabilities),"0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_long_term, getString(R.string.long_term_liabilities), "0"));

                adapter.notifyDataSetChanged();
            }
        });
    }
}
