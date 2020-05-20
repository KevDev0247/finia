package protect.FinanceLord.NetWorthReportViewingUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;
import protect.FinanceLord.R;

public class Report_AssetsFragment extends Fragment {

    private Date itemTime;
    private View contentView;
    private TypeProcessor_Assets assetsTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> liquidAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> personalAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> taxableAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> retirementAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> ownershipInterestsDataSource = new ArrayList<>();

    public Report_AssetsFragment(Date itemTime) {
        this.itemTime = itemTime;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_report_assets, null);

        Log.d("Report_AFragment","the time passed into assets viewing fragment is: " + itemTime);
        getDataFromDatabase(itemTime);

        return contentView;
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

                List<AssetsValue> categoryAssets = new ArrayList<>();
                List<AssetsValue> previousCategoryAssets = new ArrayList<>();

                AssetsValue totalLiquidAssets = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 32);
                AssetsValue totalInvestedAssets = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 33);
                AssetsValue totalPersonalAssets = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 34);
                AssetsValue totalTaxableAccounts = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 29);
                AssetsValue totalRetirementAccounts = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 30);
                AssetsValue totalOwnershipInterests = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), 31);

                categoryAssets.add(totalLiquidAssets);
                categoryAssets.add(totalInvestedAssets);
                categoryAssets.add(totalPersonalAssets);
                categoryAssets.add(totalTaxableAccounts);
                categoryAssets.add(totalRetirementAccounts);
                categoryAssets.add(totalOwnershipInterests);

                AssetsValue previousLiquidAssets = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 32);
                AssetsValue previousInvestedAssets = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 33);
                AssetsValue previousPersonalAssets = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 34);
                AssetsValue previousTaxableAccounts = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 29);
                AssetsValue previousRetirementAccounts = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 30);
                AssetsValue previousOwnershipInterests = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), 31);

                previousCategoryAssets.add(previousLiquidAssets);
                previousCategoryAssets.add(previousInvestedAssets);
                previousCategoryAssets.add(previousPersonalAssets);
                previousCategoryAssets.add(previousTaxableAccounts);
                previousCategoryAssets.add(previousRetirementAccounts);
                previousCategoryAssets.add(previousOwnershipInterests);

                populateDataModels(assetsValueDao, itemTime, categoryAssets, previousCategoryAssets);
            }
        });
    }

    public void populateDataModels(final AssetsValueDao assetsValueDao, final Date itemTime, final List<AssetsValue> categoryAssets, final List<AssetsValue> previousCategoryAssets) {

        List<DataCarrier_Assets> liquidAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.liquid_assets_name),2);
        List<DataCarrier_Assets> personalAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.personal_assets_name), 2);
        List<DataCarrier_Assets> taxableAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.taxable_accounts_name),3);
        List<DataCarrier_Assets> retirementAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.retirement_accounts_name), 3);
        List<DataCarrier_Assets> ownershipInterestsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.ownership_interest_name), 3);

        for (DataCarrier_Assets dataCarrier : liquidAssetsTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisAssetValue = getString(R.string.no_data_initialization);
            AssetsValue liquidAssetValue = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), dataCarrier.assetsTypeId);
            AssetsValue previousAssetValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), dataCarrier.assetsTypeId);

            if (liquidAssetValue != null) {
                if (previousAssetValue != null) {
                    difference = String.valueOf(liquidAssetValue.getAssetsValue() - previousAssetValue.getAssetsValue());
                }
                thisAssetValue = String.valueOf(liquidAssetValue.getAssetsValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                liquidAssetsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                liquidAssetsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : personalAssetsTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisAssetValue = getString(R.string.no_data_initialization);
            AssetsValue personalAssetValue = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), dataCarrier.assetsTypeId);
            AssetsValue previousAssetValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), dataCarrier.assetsTypeId);

            if (personalAssetValue != null) {
                if (previousAssetValue != null) {
                    difference = String.valueOf(personalAssetValue.getAssetsValue() - previousAssetValue.getAssetsValue());
                }
                thisAssetValue = String.valueOf(personalAssetValue.getAssetsValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                personalAssetsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                personalAssetsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : taxableAccountsTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisAssetValue = getString(R.string.no_data_initialization);
            AssetsValue taxableAccountValue = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), dataCarrier.assetsTypeId);
            AssetsValue previousAssetValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), dataCarrier.assetsTypeId);

            if (taxableAccountValue != null) {
                if (previousAssetValue != null) {
                    difference = String.valueOf(taxableAccountValue.getAssetsValue() - previousAssetValue.getAssetsValue());
                }
                thisAssetValue = String.valueOf(taxableAccountValue.getAssetsValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                taxableAccountsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                taxableAccountsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : retirementAccountsTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisAssetValue = getString(R.string.no_data_initialization);
            AssetsValue retirementAccountValue = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), dataCarrier.assetsTypeId);
            AssetsValue previousAssetValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), dataCarrier.assetsTypeId);

            if (retirementAccountValue != null) {
                if (previousAssetValue != null) {
                    difference = String.valueOf(retirementAccountValue.getAssetsValue() - previousAssetValue.getAssetsValue());
                }
                thisAssetValue = String.valueOf(retirementAccountValue.getAssetsValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                retirementAccountsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                retirementAccountsDataSource.add(dataModel);
            }
        }

        for (DataCarrier_Assets dataCarrier : ownershipInterestsTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisAssetValue = getString(R.string.no_data_initialization);
            AssetsValue ownershipInterestValue = assetsValueDao.queryIndividualAssetByTime(itemTime.getTime(), dataCarrier.assetsTypeId);
            AssetsValue previousAssetValue = assetsValueDao.queryPreviousAssetBeforeTime(itemTime.getTime(), dataCarrier.assetsTypeId);

            if (ownershipInterestValue != null) {
                if (previousAssetValue != null) {
                    difference = String.valueOf(ownershipInterestValue.getAssetsValue() - previousAssetValue.getAssetsValue());
                }
                thisAssetValue = String.valueOf(ownershipInterestValue.getAssetsValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                ownershipInterestsDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.assetsTypeName, thisAssetValue, difference);
                ownershipInterestsDataSource.add(dataModel);
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshView(contentView, categoryAssets, previousCategoryAssets);
            }
        });
    }

    public void refreshView(View contentView, List<AssetsValue> categoryAssets, List<AssetsValue> previousCategoryAssets) {

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

        TextView liquidAssetsValue = contentView.findViewById(R.id.report_liquid_assets_value);
        View liquidAssetsDifferenceBlock = contentView.findViewById(R.id.report_liquid_assets_difference_block);
        TextView liquidAssetsSymbol = contentView.findViewById(R.id.report_liquid_assets_symbol);
        TextView liquidAssetsDifference = contentView.findViewById(R.id.report_liquid_assets_difference);

        TextView investedAssetsValue = contentView.findViewById(R.id.report_invested_assets_value);
        View investedAssetsDifferenceBlock = contentView.findViewById(R.id.report_invested_assets_difference_block);
        TextView investedAssetsSymbol = contentView.findViewById(R.id.report_invested_assets_symbol);
        TextView investedAssetsDifference = contentView.findViewById(R.id.report_invested_assets_difference);

        TextView personalAssetsValue = contentView.findViewById(R.id.report_personal_assets_value);
        View personalAssetsDifferenceBlock = contentView.findViewById(R.id.report_personal_assets_difference_block);
        TextView personalAssetsSymbol = contentView.findViewById(R.id.report_personal_assets_symbol);
        TextView personalAssetsDifference = contentView.findViewById(R.id.report_personal_assets_difference);

        TextView taxableAccountsValue = contentView.findViewById(R.id.report_taxable_accounts_value);
        View taxableAccountsDifferenceBlock = contentView.findViewById(R.id.report_taxable_accounts_difference_block);
        TextView taxableAccountsSymbol = contentView.findViewById(R.id.report_taxable_accounts_symbol);
        TextView taxableAccountsDifference = contentView.findViewById(R.id.report_taxable_accounts_difference);

        TextView retirementAccountsValue = contentView.findViewById(R.id.report_retirement_accounts_value);
        View retirementAccountsDifferenceBlock = contentView.findViewById(R.id.report_retirement_accounts_difference_block);
        TextView retirementAccountsSymbol = contentView.findViewById(R.id.report_retirement_accounts_symbol);
        TextView retirementAccountsDifference = contentView.findViewById(R.id.report_retirement_accounts_difference);

        TextView ownershipInterestsValue = contentView.findViewById(R.id.report_ownership_interests_value);
        View ownershipInterestsDifferenceBlock = contentView.findViewById(R.id.report_ownership_interests_difference_block);
        TextView ownershipInterestsSymbol = contentView.findViewById(R.id.report_ownership_interests_symbol);
        TextView ownershipInterestsDifference = contentView.findViewById(R.id.report_ownership_interests_difference);

        liquidAssetsValue.setText(String.valueOf(categoryAssets.get(0).getAssetsValue()));
        if (previousCategoryAssets.get(0) == null) {
            liquidAssetsSymbol.setText("");
            liquidAssetsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(0).getAssetsValue() - previousCategoryAssets.get(0).getAssetsValue();
            liquidAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                liquidAssetsSymbol.setText("");

            } else if (difference > 0){
                liquidAssetsSymbol.setText(getString(R.string.positive_symbol));
                liquidAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                liquidAssetsSymbol.setText(getString(R.string.negative_symbol));
                liquidAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

        investedAssetsValue.setText(String.valueOf(categoryAssets.get(1).getAssetsValue()));
        if (previousCategoryAssets.get(1) == null) {
            investedAssetsSymbol.setText("");
            investedAssetsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(1).getAssetsValue() - previousCategoryAssets.get(1).getAssetsValue();
            investedAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                investedAssetsSymbol.setText("");

            } else if (difference > 0){
                investedAssetsSymbol.setText(getString(R.string.positive_symbol));
                investedAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                investedAssetsSymbol.setText(getString(R.string.negative_symbol));
                investedAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

        personalAssetsValue.setText(String.valueOf(categoryAssets.get(2).getAssetsValue()));
        if (previousCategoryAssets.get(2) == null) {
            personalAssetsSymbol.setText("");
            personalAssetsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(2).getAssetsValue() - previousCategoryAssets.get(2).getAssetsValue();
            personalAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                personalAssetsSymbol.setText("");

            } else if (difference > 0){
                personalAssetsSymbol.setText(getString(R.string.positive_symbol));
                personalAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                personalAssetsSymbol.setText(getString(R.string.negative_symbol));
                personalAssetsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

        taxableAccountsValue.setText(String.valueOf(categoryAssets.get(3).getAssetsValue()));
        if (previousCategoryAssets.get(3) == null) {
            taxableAccountsSymbol.setText("");
            taxableAccountsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(3).getAssetsValue() - (previousCategoryAssets.get(3).getAssetsValue());
            taxableAccountsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                taxableAccountsSymbol.setText("");

            } else if (difference > 0){
                taxableAccountsSymbol.setText(getString(R.string.positive_symbol));
                taxableAccountsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                taxableAccountsSymbol.setText(getString(R.string.negative_symbol));
                taxableAccountsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

        retirementAccountsValue.setText(String.valueOf(categoryAssets.get(4).getAssetsValue()));
        if (previousCategoryAssets.get(4) == null) {
            retirementAccountsSymbol.setText("");
            retirementAccountsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(4).getAssetsValue() - previousCategoryAssets.get(4).getAssetsValue();
            retirementAccountsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                retirementAccountsSymbol.setText("");

            } else if (difference > 0){
                retirementAccountsSymbol.setText(getString(R.string.positive_symbol));
                retirementAccountsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                retirementAccountsSymbol.setText(getString(R.string.negative_symbol));
                retirementAccountsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

        ownershipInterestsValue.setText(String.valueOf(categoryAssets.get(5).getAssetsValue()));
        if (previousCategoryAssets.get(5) == null) {
            ownershipInterestsSymbol.setText("");
            ownershipInterestsDifference.setText(R.string.no_data_initialization);

        } else {
            float difference = categoryAssets.get(5).getAssetsValue() - previousCategoryAssets.get(5).getAssetsValue();
            ownershipInterestsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                ownershipInterestsSymbol.setText("");

            } else if (difference > 0){
                ownershipInterestsSymbol.setText(getString(R.string.positive_symbol));
                ownershipInterestsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_increase);

            } else if (difference < 0){
                ownershipInterestsSymbol.setText(getString(R.string.negative_symbol));
                ownershipInterestsDifferenceBlock.setBackgroundResource(R.drawable.ic_net_decrease);
            }
        }

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
