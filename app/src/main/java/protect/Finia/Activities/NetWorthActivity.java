package protect.Finia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import protect.Finia.DAOs.AssetsTypeDao;
import protect.Finia.DAOs.AssetsValueDao;
import protect.Finia.DAOs.LiabilitiesTypeDao;
import protect.Finia.DAOs.LiabilitiesValueDao;
import protect.Finia.DAOs.ReportItemInfoDao;
import protect.Finia.Database.AssetsType;
import protect.Finia.Database.AssetsValue;
import protect.Finia.Database.FinanceLordDatabase;
import protect.Finia.Database.LiabilitiesType;
import protect.Finia.Database.LiabilitiesValue;
import protect.Finia.NetWorthPastReportsList.PastReportsAdapter;
import protect.Finia.NetWorthPastReportsList.ReportItemInfo;
import protect.Finia.NetWorthPastReportsList.ReportItemsDataModel;
import protect.Finia.NetWorthSwipeDashboard.NetWorthCardsAdapter;
import protect.Finia.NetWorthSwipeDashboard.NetWorthCardsDataModel;
import protect.Finia.R;

/**
 * The activity that provides a dashboard of the data of assets and liabilities as well as the list of reports.
 * The dashboard displays the current data. The activity also provides entry to edit report page.
 * The report list in the bottom sheet provide entries to each report that is saved.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NetWorthActivity extends AppCompatActivity {

    private NetWorthCardsAdapter netWorthCardsAdapter;
    private PastReportsAdapter pastReportsAdapter;
    private LinearLayout emptyMessageField;

    private  boolean initialize = true;
    private List<NetWorthCardsDataModel> netWorthCardsDataModels = new ArrayList<>();
    private List<ReportItemsDataModel> reportItemsDataModels = new ArrayList<>();
    private FinanceLordDatabase database = FinanceLordDatabase.getInstance(NetWorthActivity.this);

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button and the entry button to edit report section.
     * Lastly, it will call the methods to create the past report list and the swiping dashboard.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
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

        setUpNetWorthDashboardView();
    }

    /**
     * Call the methods to update the data immediately after the user returned to the page.
     * This method is called first after the activity is created or whenever the user returns to this activity.
     * When the user returned to the activity, the two methods will be called to
     * refresh the data on past report list and the swipe dashboard cards.
     */
    @Override
    protected void onResume() {
        super.onResume();

        retrieveDataFromDatabase(initialize);
        loadDataToDashboard();
        initialize = false;
    }

    /**
     * Retrieve the data of the report items from the database.
     * Then, database is queried for the most up-to-date data for display.
     * the raw data is processed and formatted into a data model that will be injected into the database.
     * Additionally, the difference will be calculated and added to the data model.
     * Query is completed in a separate thread to avoid locking the UI thread for a long period of time.
     *
     * @param initialize indicator of whether to set up the list view of merely update the data
     */
    private void retrieveDataFromDatabase(final boolean initialize) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ReportItemInfoDao reportItemInfoDao = database.reportItemInfoDao();

                List<ReportItemInfo> reportItemInfoList = reportItemInfoDao.queryReportItemsInfo();
                reportItemsDataModels.clear();
                for (ReportItemInfo reportItemInfo : reportItemInfoList){
                    String difference = getString(R.string.no_data_message);
                    if (reportItemInfoList.indexOf(reportItemInfo) + 1 <= reportItemInfoList.size() - 1){
                        ReportItemInfo previousReportItemInfo = reportItemInfoList.get(reportItemInfoList.indexOf(reportItemInfo) + 1);
                        difference = String.valueOf(reportItemInfo.netWorthValue - previousReportItemInfo.netWorthValue);
                    }
                    Log.d("NetWorthActivity", " the time of current item is: " + reportItemInfo.totalAssetsDate + "  the difference of this item is: " + difference);
                    ReportItemsDataModel dataModel = new ReportItemsDataModel(reportItemInfo.totalAssetsDate, reportItemInfo.netWorthValue, difference);
                    reportItemsDataModels.add(dataModel);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (initialize) {
                            setUpPastReportsListView();
                        } else {
                            pastReportsAdapter.notifyDataSetChanged();
                            setUpEmptyMessage();
                        }
                    }
                });
            }
        });
    }

    /**
     * Set up the adapter of the past report list.
     * First, a listView and its adapter PastReportsAdapter is created and set up.
     * View.onclickListener is added to the each item in the list
     * to enable user go to the corresponding report through a click.
     * The information on the list item will be stored in intent and transferred to the report activity for query and display purposes.
     */
    protected void setUpPastReportsListView() {
        ListView pastReportsListView = findViewById(R.id.past_report_list);
        emptyMessageField = findViewById(R.id.past_report_list_empty_message_field);
        pastReportsAdapter = new PastReportsAdapter(this, reportItemsDataModels);
        pastReportsListView.setAdapter(pastReportsAdapter);

        setUpEmptyMessage();

        pastReportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReportItemsDataModel dataModel = reportItemsDataModels.get(position);
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

    /**
     * Set up adapters and insert the data in the swiping dashboard.
     * First, a ViewPager for swiping and its adapter is created and setup.
     * Then, the most up-to-date data will be loaded to the cards.
     */
    protected void setUpNetWorthDashboardView() {
        netWorthCardsAdapter = new NetWorthCardsAdapter(netWorthCardsDataModels,this);

        ViewPager viewPager = findViewById(R.id.assets_cards_view_pager);
        viewPager.setAdapter(netWorthCardsAdapter);
        viewPager.setPadding(80, 0, 80, 0);
    }

    /**
     * Set up the empty message based on the status of the reportItemsDataModels.
     * If the reportItemsDataModels is empty, the empty message will be visible.
     * If the reportItemsDataModels has items, the empty message will be invisible.
     */
    private void setUpEmptyMessage() {
        if (reportItemsDataModels.size() != 0) {
            emptyMessageField.setVisibility(View.GONE);
        } else {
            emptyMessageField.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Load the latest data to the cards
     * First, all the parent categories Total value are initialized.
     * Then, the latest data of assets and liabilities will be queried from the database.
     * Lastly, inject the name, image resource, and value to the data model for the adapter to format and display.
     */
    protected void loadDataToDashboard() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final String finalTotalAssetsValue;
                final String finalLiquidAssetsValue;
                final String finalInvestedAssetsValue;
                final String finalPersonalAssetsValue;
                final String finalTaxableAccountsValue;
                final String finalRetirementAccountsValue;
                final String finalOwnershipInterestsValue;

                final String finalTotalLiabilitiesValue;
                final String finalShortTermLiabilitiesValue;
                final String finalLongTermLiabilitiesValue;

                final String finalNetWorthValue;

                AssetsValueDao assetsValueDao = database.assetsValueDao();
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();

                AssetsType totalAssetsType = assetsTypeDao.queryAssetsByType(getString(R.string.total_assets_name));
                AssetsType liquidAssetsType = assetsTypeDao.queryAssetsByType(getString(R.string.liquid_assets_name));
                AssetsType investedAssetsType = assetsTypeDao.queryAssetsByType(getString(R.string.invested_assets_name));
                AssetsType personalAssetsType = assetsTypeDao.queryAssetsByType(getString(R.string.personal_assets_name));
                AssetsType taxableAccountsType = assetsTypeDao.queryAssetsByType(getString(R.string.taxable_accounts_name));
                AssetsType retirementAccountsType = assetsTypeDao.queryAssetsByType(getString(R.string.retirement_accounts_name));
                AssetsType ownershipInterestsType = assetsTypeDao.queryAssetsByType(getString(R.string.ownership_interest_name));

                LiabilitiesType totalLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.total_liabilities_name));
                LiabilitiesType shortTermLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.short_term_liabilities_name));
                LiabilitiesType longTermLiabilities = liabilitiesTypeDao.queryLiabilitiesByType(getString(R.string.long_term_liabilities_name));

                AssetsValue totalAssetsValue = assetsValueDao.queryLatestIndividualAsset(totalAssetsType.getAssetsId());
                AssetsValue liquidAssetsValue = assetsValueDao.queryLatestIndividualAsset(liquidAssetsType.getAssetsId());
                AssetsValue investedAssetsValue = assetsValueDao.queryLatestIndividualAsset(investedAssetsType.getAssetsId());
                AssetsValue personalAssetsValue = assetsValueDao.queryLatestIndividualAsset(personalAssetsType.getAssetsId());
                AssetsValue taxableAccountsValue = assetsValueDao.queryLatestIndividualAsset(taxableAccountsType.getAssetsId());
                AssetsValue retirementAccountsValue = assetsValueDao.queryLatestIndividualAsset(retirementAccountsType.getAssetsId());
                AssetsValue ownershipInterestsValue = assetsValueDao.queryLatestIndividualAsset(ownershipInterestsType.getAssetsId());

                LiabilitiesValue totalLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(totalLiabilities.getLiabilitiesId());
                LiabilitiesValue shortTermLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(shortTermLiabilities.getLiabilitiesId());
                LiabilitiesValue longTermLiabilitiesValue = liabilitiesValueDao.queryLatestIndividualLiability(longTermLiabilities.getLiabilitiesId());
                
                if (totalAssetsValue == null) {
                    finalTotalAssetsValue = getString(R.string.no_data_message);
                } else {
                    finalTotalAssetsValue = String.valueOf(totalAssetsValue.getAssetsValue());
                }

                if (liquidAssetsValue == null) {
                    finalLiquidAssetsValue = getString(R.string.no_data_message);
                } else {
                    finalLiquidAssetsValue = String.valueOf(liquidAssetsValue.getAssetsValue());
                }

                if (investedAssetsValue == null) {
                    finalInvestedAssetsValue = getString(R.string.no_data_message);
                } else {
                    finalInvestedAssetsValue = String.valueOf(investedAssetsValue.getAssetsValue());
                }

                if (personalAssetsValue == null) {
                    finalPersonalAssetsValue = getString(R.string.no_data_message);
                } else {
                    finalPersonalAssetsValue = String.valueOf(personalAssetsValue.getAssetsValue());
                }

                if (taxableAccountsValue == null) {
                    finalTaxableAccountsValue = getString(R.string.no_data_message);
                } else {
                    finalTaxableAccountsValue = String.valueOf(taxableAccountsValue.getAssetsValue());
                }

                if (retirementAccountsValue == null) {
                    finalRetirementAccountsValue = getString(R.string.no_data_message);
                } else {
                    finalRetirementAccountsValue = String.valueOf(retirementAccountsValue.getAssetsValue());
                }

                if (ownershipInterestsValue == null) {
                    finalOwnershipInterestsValue = getString(R.string.no_data_message);
                } else {
                    finalOwnershipInterestsValue = String.valueOf(ownershipInterestsValue.getAssetsValue());
                }

                if (totalLiabilitiesValue == null) {
                    finalTotalLiabilitiesValue = getString(R.string.no_data_message);
                } else {
                    finalTotalLiabilitiesValue = String.valueOf(totalLiabilitiesValue.getLiabilitiesValue());
                }

                if (shortTermLiabilitiesValue == null) {
                    finalShortTermLiabilitiesValue = getString(R.string.no_data_message);
                } else {
                    finalShortTermLiabilitiesValue = String.valueOf(shortTermLiabilitiesValue.getLiabilitiesValue());
                }

                if (longTermLiabilitiesValue == null) {
                    finalLongTermLiabilitiesValue = getString(R.string.no_data_message);
                } else {
                    finalLongTermLiabilitiesValue = String.valueOf(longTermLiabilitiesValue.getLiabilitiesValue());
                }

                if (totalLiabilitiesValue == null || totalAssetsValue == null) {
                    finalNetWorthValue = getString(R.string.no_data_message);
                } else {
                    finalNetWorthValue = String.valueOf(totalAssetsValue.getAssetsValue() - totalLiabilitiesValue.getLiabilitiesValue());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        netWorthCardsDataModels.clear();

                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.net_worth, getString(R.string.net_worth), finalNetWorthValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.assets_total, getString(R.string.total_assets_name), finalTotalAssetsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.assets_liquid, getString(R.string.liquid_assets_name), finalLiquidAssetsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.assets_invested, getString(R.string.invested_assets_name), finalInvestedAssetsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.assets_personal, getString(R.string.personal_assets_name), finalPersonalAssetsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.invested_taxable_accounts, getString(R.string.taxable_accounts_name), finalTaxableAccountsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.invested_retirement, getString(R.string.retirement_accounts_name), finalRetirementAccountsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.invested_ownership,getString(R.string.ownership_interest_name), finalOwnershipInterestsValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_total, getString(R.string.total_liabilities_name), finalTotalLiabilitiesValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_short_term, getString(R.string.short_term_liabilities_name), finalShortTermLiabilitiesValue));
                        netWorthCardsDataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_long_term, getString(R.string.long_term_liabilities_name), finalLongTermLiabilitiesValue));

                        netWorthCardsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
