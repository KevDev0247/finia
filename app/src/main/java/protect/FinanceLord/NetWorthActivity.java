package protect.FinanceLord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import protect.FinanceLord.NetWorthSwipeCardsUtils.AssetsCardsDataModel;
import protect.FinanceLord.NetWorthSwipeCardsUtils.NetWorthCardsAdapter;
import protect.FinanceLord.ui.NetWorthEditReports.DateUtils;
import protect.FinanceLord.ui.NetWorthEditReports.PastReportsAdapter;

public class NetWorthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth);

        PastReportsAdapter pastReportsAdapter = new PastReportsAdapter(this);

        Button completeReportButton = findViewById(R.id.CompleteReport);
        completeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NetWorthActivity.this, NetWorthEditReportsActivity.class);
                startActivity(intent);
            }
        });

        createAssetsCardsView();
    }

    protected void createAssetsCardsView(){

        final NetWorthCardsAdapter adapter;
        final ViewPager viewPager;
        List<AssetsCardsDataModel> dataModels = new ArrayList<>();
        NetWorthCalculator netWorthCalculator;

        Date startOfMinute = DateUtils.firstSecondOfThisMinute();
        Long MilliSeconds = startOfMinute.getTime();

        netWorthCalculator = new NetWorthCalculator(NetWorthActivity.this, MilliSeconds);

        float totalAssets = netWorthCalculator.calculateTotalAssets();
        float totalLiquidAssets = netWorthCalculator.calculateTotalLiquidAssets();
        float totalInvestedAssets = netWorthCalculator.calculateTotalInvestedAssets();
        float totalPersonalAssets = netWorthCalculator.calculateTotalPersonalAssets();
        float totalTaxableAccounts = netWorthCalculator.calculateTotalTaxableAccounts();
        float totalRetirementAccounts = netWorthCalculator.calculateTotalRetirementAccounts();
        float totalOwnershipInterests = netWorthCalculator.calculateTotalOwnershipInterests();

        dataModels.add(new AssetsCardsDataModel(R.drawable.net_worth, "Total Assets", "0"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_liquid, "Liquid Assets", "0"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_invested, "Invested Assets", "0"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_personal, "Personal Assets", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_taxable_accounts, "Taxable Accounts", "0"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_retirement, "Retirement Accounts", "0"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_ownership,"Ownership Interests", "0"));

        adapter = new NetWorthCardsAdapter(dataModels,this);

        viewPager = findViewById(R.id.assets_cards_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);
    }
}
