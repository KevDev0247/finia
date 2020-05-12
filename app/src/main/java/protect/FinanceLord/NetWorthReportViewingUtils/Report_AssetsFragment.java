package protect.FinanceLord.NetWorthReportViewingUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;
import protect.FinanceLord.R;

public class Report_AssetsFragment extends Fragment {

    String title;
    private Date itemTime;
    private View contentView;
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
        this.contentView = assetsView;

        getDataFromDatabase(itemTime);

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

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshView(contentView);
            }
        });
    }

    public void refreshView(View contentView) {

        LinearLayout liquidAssetsListView = contentView.findViewById(R.id.liquid_assets_list);
        LinearLayout personalAssetsListView = contentView.findViewById(R.id.personal_assets_list);
        LinearLayout taxableAccountsListView = contentView.findViewById(R.id.taxable_accounts_list);
        LinearLayout retirementAccountsListView = contentView.findViewById(R.id.retirement_accounts_list);
        LinearLayout ownershipInterestsListView = contentView.findViewById(R.id.ownership_interests_list);

        ReportListAdapter liquidAssetsAdapter = new ReportListAdapter(getContext(), liquidAssetsDataSource);
        ReportListAdapter personalAssetsAdapter = new ReportListAdapter(getContext(), personalAssetsDataSource);
        ReportListAdapter taxableAccountsAdapter = new ReportListAdapter(getContext(), taxableAccountsDataSource);
        ReportListAdapter retirementAccountsAdapter = new ReportListAdapter(getContext(), retirementAccountsDataSource);
        ReportListAdapter ownershipInterestsAdapter = new ReportListAdapter(getContext(), ownershipInterestsDataSource);

        for (int i = 0; i < liquidAssetsAdapter.getCount(); i++){
            View itemView = liquidAssetsAdapter.getView(i, null, liquidAssetsListView);
            liquidAssetsListView.addView(itemView);
        }

        for (int i = 0; i < personalAssetsAdapter.getCount(); i++){
            View itemView = personalAssetsAdapter.getView(i, null, personalAssetsListView);
            personalAssetsListView.addView(itemView);
        }

        for (int i = 0; i < taxableAccountsAdapter.getCount(); i++){
            View itemView = taxableAccountsAdapter.getView(i, null, taxableAccountsListView);
            personalAssetsListView.addView(itemView);
        }

        for (int i = 0; i < retirementAccountsAdapter.getCount(); i++){
            View itemView = retirementAccountsAdapter.getView(i, null, retirementAccountsListView);
            retirementAccountsListView.addView(itemView);
        }

        for (int i = 0; i < ownershipInterestsAdapter.getCount(); i++){
            View itemView = ownershipInterestsAdapter.getView(i, null, ownershipInterestsListView);
            ownershipInterestsListView.addView(itemView);
        }
    }
}
