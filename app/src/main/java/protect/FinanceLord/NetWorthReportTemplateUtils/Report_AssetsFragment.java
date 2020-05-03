package protect.FinanceLord.NetWorthReportTemplateUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;
import protect.FinanceLord.R;

public class Report_AssetsFragment extends Fragment {

    String title;
    DataProcessor_Assets dataProcessor;
    private ArrayList<NetWorthItemsDataModel> liquidAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> personalAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> taxableAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> retirementAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> ownershipInterestsDataSource = new ArrayList<>();

    public Report_AssetsFragment(String title){
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View assetsView = inflater.inflate(R.layout.fragment_report_assets, null);

        initDataModels();

        ListView liquidAssetsListView = assetsView.findViewById(R.id.liquid_assets_list);
        ListView personalAssetsListView = assetsView.findViewById(R.id.personal_assets_list);
        ListView taxableAccountsListView = assetsView.findViewById(R.id.taxable_accounts_list);
        ListView retirementAccountsListView = assetsView.findViewById(R.id.retirement_accounts_list);
        ListView ownershipInterestsListView = assetsView.findViewById(R.id.ownership_interests_list);

        ReportListAdapter liquidAssetsAdapter = new ReportListAdapter(getContext(), liquidAssetsDataSource);
        ReportListAdapter personalAssetsAdapter = new ReportListAdapter(getContext(), personalAssetsDataSource);
        ReportListAdapter taxableAccountsAdapter = new ReportListAdapter(getContext(), taxableAccountsDataSource);
        ReportListAdapter retirementAccountsAdapter = new ReportListAdapter(getContext(), retirementAccountsDataSource);
        ReportListAdapter ownershipInterestsAdapter = new ReportListAdapter(getContext(), ownershipInterestsDataSource);

        liquidAssetsListView.setAdapter(liquidAssetsAdapter);
        personalAssetsListView.setAdapter(personalAssetsAdapter);
        taxableAccountsListView.setAdapter(taxableAccountsAdapter);
        retirementAccountsListView.setAdapter(retirementAccountsAdapter);
        ownershipInterestsListView.setAdapter(ownershipInterestsAdapter);

        return assetsView;
    }

    public void initDataModels(){

        List<DataCarrier_Assets> liquidAssets = dataProcessor.getSubSet("Liquid assets",2);
        List<DataCarrier_Assets> personalAssets = dataProcessor.getSubSet("Personal assets", 2);
        List<DataCarrier_Assets> taxableAccounts = dataProcessor.getSubSet("Taxable accounts",3);
        List<DataCarrier_Assets> retirementAccounts = dataProcessor.getSubSet("Retirement accounts", 3);
        List<DataCarrier_Assets> ownershipInterests = dataProcessor.getSubSet("Ownership interests", 3);


        for (DataCarrier_Assets dataCarrier : liquidAssets){
            NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            liquidAssetsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : personalAssets){
            NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            personalAssetsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : taxableAccounts){
            NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            taxableAccountsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : retirementAccounts){
            NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            retirementAccountsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : ownershipInterests){
            NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            ownershipInterestsDataSource.add(dataModel);
        }
    }
}
