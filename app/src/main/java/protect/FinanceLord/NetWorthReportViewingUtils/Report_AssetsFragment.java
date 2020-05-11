package protect.FinanceLord.NetWorthReportViewingUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.DateCommunicator;
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;
import protect.FinanceLord.NetWorthReportViewingActivity;
import protect.FinanceLord.R;

public class Report_AssetsFragment extends Fragment {

    String title;
    private Date itemTime;
    private TypeProcessor_Assets assetsTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> liquidAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> personalAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> taxableAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> retirementAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> ownershipInterestsDataSource = new ArrayList<>();

    public Report_AssetsFragment(String title, Date itemTime){
        this.title = title;
        this.itemTime = itemTime;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View assetsView = inflater.inflate(R.layout.fragment_report_assets, null);

        getDataFromDatabase(itemTime);

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

    private void getDataFromDatabase(final Date itemTime) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Report_AssetsFragment.this.getContext());
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();

                List<AssetsTypeQuery> assetsTypes = assetsTypeDao.queryGroupedAssetsType();
                Report_AssetsFragment.this.assetsTypeProcessor = new TypeProcessor_Assets(assetsTypes);

                initDataModels(assetsValueDao, itemTime);
            }
        });
    }

    public void initDataModels(AssetsValueDao assetsValueDao, Date itemTime){

        List<DataCarrier_Assets> liquidAssets = assetsTypeProcessor.getSubGroup(getString(R.string.liquid_assets_name),2);
        List<DataCarrier_Assets> personalAssets = assetsTypeProcessor.getSubGroup(getString(R.string.personal_assets_name), 2);
        List<DataCarrier_Assets> taxableAccounts = assetsTypeProcessor.getSubGroup(getString(R.string.taxable_accounts_name),3);
        List<DataCarrier_Assets> retirementAccounts = assetsTypeProcessor.getSubGroup(getString(R.string.retirement_accounts_name), 3);
        List<DataCarrier_Assets> ownershipInterests = assetsTypeProcessor.getSubGroup(getString(R.string.ownership_interest_name), 3);

        // need to add the code for query data at itemTime
        // may need another class to store the data

        for (DataCarrier_Assets dataCarrier : liquidAssets){
            AssetsValue liquidAssetsValue = new AssetsValue();
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
