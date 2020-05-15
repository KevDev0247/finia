package protect.FinanceLord.NetWorthReportViewingUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    public Report_AssetsFragment(String title, Date itemTime) {
        this.title = title;
        this.itemTime = itemTime;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View assetsView = inflater.inflate(R.layout.fragment_report_assets, null);
        this.contentView = assetsView;

        Log.d("Report_AFragment","the time passed into assets viewing fragment is: " + itemTime);
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

                populateDataModels(assetsValueDao, itemTime);
            }
        });
    }

    public void populateDataModels(AssetsValueDao assetsValueDao, Date itemTime) {

        List<DataCarrier_Assets> liquidAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.liquid_assets_name),2);
        List<DataCarrier_Assets> personalAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.personal_assets_name), 2);
        List<DataCarrier_Assets> taxableAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.taxable_accounts_name),3);
        List<DataCarrier_Assets> retirementAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.retirement_accounts_name), 3);
        List<DataCarrier_Assets> ownershipInterestsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.ownership_interest_name), 3);

        for (DataCarrier_Assets dataCarrier : liquidAssetsTypes) {
            AssetsValue liquidAssetValue = assetsValueDao.queryIndividualAssetByDate(itemTime.getTime(), dataCarrier.assetsTypeId);
            if (liquidAssetValue != null) {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, liquidAssetValue.getAssetsValue(), 0);
                liquidAssetsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
                liquidAssetsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : personalAssetsTypes) {
            AssetsValue personalAssetValue = assetsValueDao.queryIndividualAssetByDate(itemTime.getTime(), dataCarrier.assetsTypeId);
            if (personalAssetValue != null) {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, personalAssetValue.getAssetsValue(), 0);
                personalAssetsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
                personalAssetsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : taxableAccountsTypes) {
            AssetsValue taxableAccountValue = assetsValueDao.queryIndividualAssetByDate(itemTime.getTime(), dataCarrier.assetsTypeId);
            if (taxableAccountValue != null) {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, taxableAccountValue.getAssetsValue(), 0);
                taxableAccountsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
                taxableAccountsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : retirementAccountsTypes) {
            AssetsValue retirementAccountValue = assetsValueDao.queryIndividualAssetByDate(itemTime.getTime(), dataCarrier.assetsTypeId);
            if (retirementAccountValue != null) {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, retirementAccountValue.getAssetsValue(), 0);
                retirementAccountsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
                retirementAccountsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : ownershipInterestsTypes) {
            AssetsValue ownershipInterestValue = assetsValueDao.queryIndividualAssetByDate(itemTime.getTime(), dataCarrier.assetsTypeId);
            if (ownershipInterestValue != null) {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, ownershipInterestValue.getAssetsValue(), 0);
                ownershipInterestsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, 0, 0);
                ownershipInterestsDataSource.add(dataModel);
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshView(contentView);
            }
        });
    }

    public void refreshView(View contentView) {

        LinearLayout liquidAssetsList = contentView.findViewById(R.id.liquid_assets_list);
        LinearLayout personalAssetsList = contentView.findViewById(R.id.personal_assets_list);
        LinearLayout taxableAccountsList = contentView.findViewById(R.id.taxable_accounts_list);
        LinearLayout retirementAccountsList = contentView.findViewById(R.id.retirement_accounts_list);
        LinearLayout ownershipInterestsList = contentView.findViewById(R.id.ownership_interests_list);

        ReportListAdapter liquidAssetsAdapter = new ReportListAdapter(getContext(), liquidAssetsDataSource, getString(R.string.report_assets_fragment_name));
        ReportListAdapter personalAssetsAdapter = new ReportListAdapter(getContext(), personalAssetsDataSource, getString(R.string.report_assets_fragment_name));
        ReportListAdapter taxableAccountsAdapter = new ReportListAdapter(getContext(), taxableAccountsDataSource, getString(R.string.report_assets_fragment_name));
        ReportListAdapter retirementAccountsAdapter = new ReportListAdapter(getContext(), retirementAccountsDataSource, getString(R.string.report_assets_fragment_name));
        ReportListAdapter ownershipInterestsAdapter = new ReportListAdapter(getContext(), ownershipInterestsDataSource, getString(R.string.report_assets_fragment_name));

        for (int i = 0; i < liquidAssetsAdapter.getCount(); i++) {
            View itemView = liquidAssetsAdapter.getView(i, null, liquidAssetsList);
            liquidAssetsList.addView(itemView);
        }

        for (int i = 0; i < personalAssetsAdapter.getCount(); i++) {
            View itemView = personalAssetsAdapter.getView(i, null, personalAssetsList);
            personalAssetsList.addView(itemView);
        }

        for (int i = 0; i < taxableAccountsAdapter.getCount(); i++) {
            View itemView = taxableAccountsAdapter.getView(i, null, taxableAccountsList);
            taxableAccountsList.addView(itemView);
        }

        for (int i = 0; i < retirementAccountsAdapter.getCount(); i++) {
            View itemView = retirementAccountsAdapter.getView(i, null, retirementAccountsList);
            retirementAccountsList.addView(itemView);
        }

        for (int i = 0; i < ownershipInterestsAdapter.getCount(); i++) {
            View itemView = ownershipInterestsAdapter.getView(i, null, ownershipInterestsList);
            ownershipInterestsList.addView(itemView);
        }
    }
}
