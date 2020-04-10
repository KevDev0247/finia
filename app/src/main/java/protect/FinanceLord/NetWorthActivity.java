package protect.FinanceLord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.NetWorthCardsUtils.AssetsCardsDataModel;
import protect.FinanceLord.NetWorthCardsUtils.NetWorthCardsAdapter;
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

        dataModels.add(new AssetsCardsDataModel(R.drawable.net_worth, "Net Worth", "Your current net worth is" + "$"));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_liquid, "Liquid Assets", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_invested, "Invested Assets", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.assets_personal, "Personal Assets", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_taxable_accounts, "Taxable Accounts", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_retirement, "Retirement Accounts", ""));
        dataModels.add(new AssetsCardsDataModel(R.drawable.invested_ownership,"Ownership Interests", ""));

        adapter = new NetWorthCardsAdapter(dataModels,this);

        viewPager = findViewById(R.id.assets_cards_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 1500, 130, 500);
    }
}
