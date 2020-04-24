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
    private ArrayList<ReportItemsDataModel> liquidAssetsDataSource = new ArrayList<>();
    private ArrayList<ReportItemsDataModel> personalAssetsDataSource = new ArrayList<>();
    private ArrayList<ReportItemsDataModel> taxableAccountsDataSource = new ArrayList<>();
    private ArrayList<ReportItemsDataModel> retirementAccountsDataSource = new ArrayList<>();
    private ArrayList<ReportItemsDataModel> ownershipInterestsDataSource = new ArrayList<>();

    ListView liquidAssetsListView;
    ListView personalAssetsListView;
    ListView taxableAccountsListView;
    ListView retirementAccountsListView;
    ListView ownershipInterestsListView;

    public Report_AssetsFragment(String title){
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View assetsView = inflater.inflate(R.layout.report_assets_layout, null);

        initDataModels();

        liquidAssetsListView = assetsView.findViewById(R.id.liquid_assets_list);
        personalAssetsListView = assetsView.findViewById(R.id.personal_assets_list);
        taxableAccountsListView = assetsView.findViewById(R.id.taxable_accounts_list);
        retirementAccountsListView = assetsView.findViewById(R.id.retirement_accounts_list);
        ownershipInterestsListView = assetsView.findViewById(R.id.ownership_interests_list);

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
            ReportItemsDataModel dataModel = new ReportItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            liquidAssetsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : personalAssets){
            ReportItemsDataModel dataModel = new ReportItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            personalAssetsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : taxableAccounts){
            ReportItemsDataModel dataModel = new ReportItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            taxableAccountsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : retirementAccounts){
            ReportItemsDataModel dataModel = new ReportItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            retirementAccountsDataSource.add(dataModel);
        }

        for (DataCarrier_Assets dataCarrier : ownershipInterests){
            ReportItemsDataModel dataModel = new ReportItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
            ownershipInterestsDataSource.add(dataModel);
        }
    }
}
