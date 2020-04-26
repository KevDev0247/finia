package protect.FinanceLord;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.NetWorthCalculatorUtils.AssetsValueExtractor;
import protect.FinanceLord.NetWorthCalculatorUtils.NetWorthCalculator;
import protect.FinanceLord.NetWorthPastReportsListUtils.PastReportsAdapter;
import protect.FinanceLord.NetWorthPastReportsListUtils.ReportItemsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsAdapter;

public class NetWorthActivity extends AppCompatActivity implements NetWorthEditReportActivity.CalendarDialogCommunicator {

    Date currentTime = new Date();

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

        loadData(dataModels, adapter);
    }

    protected void loadData(final List<NetWorthCardsDataModel> dataModels, final NetWorthCardsAdapter adapter){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                Long MilliSeconds = currentTime.getTime();
                AssetsValueExtractor assetsValueExtractor = new AssetsValueExtractor(NetWorthActivity.this, MilliSeconds);
                NetWorthCalculator netWorthCalculator = new NetWorthCalculator(assetsValueExtractor);

                float totalAssets = netWorthCalculator.calculateTotalAssets();
                float totalLiquidAssets = netWorthCalculator.calculateTotalLiquidAssets();
                float totalInvestedAssets = netWorthCalculator.calculateTotalInvestedAssets();
                float totalPersonalAssets = netWorthCalculator.calculateTotalPersonalAssets();
                float totalTaxableAccounts = netWorthCalculator.calculateTotalTaxableAccounts();
                float totalRetirementAccounts = netWorthCalculator.calculateTotalRetirementAccounts();
                float totalOwnershipInterests = netWorthCalculator.calculateTotalOwnershipInterests();

                dataModels.add(new NetWorthCardsDataModel(R.drawable.net_worth, "Net Worth","0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_total, "Total Assets", String.valueOf(totalAssets)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_total, "Total Liabilities","0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_short_term, "Short Term Liabilities","0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.liabilities_long_term,"Short Term Liabilities", "0"));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_liquid, "Liquid Assets", String.valueOf(totalLiquidAssets)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_invested, "Invested Assets", String.valueOf(totalInvestedAssets)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.assets_personal, "Personal Assets", String.valueOf(totalPersonalAssets)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_taxable_accounts, "Taxable Accounts", String.valueOf(totalTaxableAccounts)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_retirement, "Retirement Accounts", String.valueOf(totalRetirementAccounts)));
                dataModels.add(new NetWorthCardsDataModel(R.drawable.invested_ownership,"Ownership Interests", String.valueOf(totalOwnershipInterests)));

                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDialogMessage(Date date) {
        currentTime = date;
    }
}
