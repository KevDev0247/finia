package protect.Finia.NetWorthReportViewing;

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

import protect.Finia.DAOs.AssetsTypeDao;
import protect.Finia.NetWorthDataStructure.TypeTreeLeaf_Assets;
import protect.Finia.DAOs.AssetsValueDao;
import protect.Finia.Database.AssetsValue;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.NetWorthDataStructure.NodeContainer_Assets;
import protect.Finia.NetWorthDataStructure.TypeTreeProcessor_Assets;
import protect.Finia.R;

/**
 * The fragment for the Assets Fragment sheet.
 * The fragment contains the list of all the assets items.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class Report_AssetsFragment extends Fragment {

    private Date itemTime;
    private View contentView;
    private TypeTreeProcessor_Assets assetsTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> liquidAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> personalAssetsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> taxableAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> retirementAccountsDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> ownershipInterestsDataSource = new ArrayList<>();

    public Report_AssetsFragment(Date itemTime) {
        this.itemTime = itemTime;
    }

    /**
     * Create the view of the fragment.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, the method calls the method to get data from the database.
     *
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     * @return the view of the assets fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_report_assets, null);

        Log.d("Report_AFragment","the time passed into assets viewing fragment is: " + itemTime);
        getDataFromDatabase(itemTime);

        return contentView;
    }

    /**
     * Retrieve the data from database.
     * The method will query the current item value and the previous value of the asset.
     * The query will be performed in a separate thread to prevent locking the UI thread for a long period of time.
     * Lastly, a method to populate the data models will be called to prepare the data source for ListViews
     *
     * @param itemTime the time of the item.
     */
    private void getDataFromDatabase(final Date itemTime) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(Report_AssetsFragment.this.getContext());
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();

                List<TypeTreeLeaf_Assets> assetsTypes = assetsTypeDao.queryAssetsTypeTreeAsList();
                Report_AssetsFragment.this.assetsTypeProcessor = new TypeTreeProcessor_Assets(assetsTypes);

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

    /**
     * Populate the data models with data.
     * The data are all related to items that are on the parent nodes.
     * Once, the population of data is complete, the method to refresh the view will be called.
     *
     * @param assetsValueDao the data access object to insert AssetsValue object.
     * @param itemTime the time of the item.
     * @param categoryAssets the value items on the parent node.
     * @param previousCategoryAssets the value of the previous items on the parent node.
     */
    private void populateDataModels(final AssetsValueDao assetsValueDao, final Date itemTime, final List<AssetsValue> categoryAssets, final List<AssetsValue> previousCategoryAssets) {
        /* retrieve all values of leaf nodes and group them into lists */
        List<NodeContainer_Assets> liquidAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.liquid_assets_name),2);
        List<NodeContainer_Assets> personalAssetsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.personal_assets_name), 2);
        List<NodeContainer_Assets> taxableAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.taxable_accounts_name),3);
        List<NodeContainer_Assets> retirementAccountsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.retirement_accounts_name), 3);
        List<NodeContainer_Assets> ownershipInterestsTypes = assetsTypeProcessor.getSubGroup(getString(R.string.ownership_interest_name), 3);

        /* Set up liquid assets item */
        for (NodeContainer_Assets dataCarrier : liquidAssetsTypes) {
            String difference = getString(R.string.no_data_message);
            String thisAssetValue = getString(R.string.no_data_message);
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

        /* Set up personal assets item */
        for (NodeContainer_Assets dataCarrier : personalAssetsTypes) {
            String difference = getString(R.string.no_data_message);
            String thisAssetValue = getString(R.string.no_data_message);
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

        /* Set up taxable accounts item */
        for (NodeContainer_Assets dataCarrier : taxableAccountsTypes) {
            String difference = getString(R.string.no_data_message);
            String thisAssetValue = getString(R.string.no_data_message);
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

        /* Set up retirement accounts item */
        for (NodeContainer_Assets dataCarrier : retirementAccountsTypes) {
            String difference = getString(R.string.no_data_message);
            String thisAssetValue = getString(R.string.no_data_message);
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

        /* Set up ownership interests item */
        for (NodeContainer_Assets dataCarrier : ownershipInterestsTypes) {
            String difference = getString(R.string.no_data_message);
            String thisAssetValue = getString(R.string.no_data_message);
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

    /**
     * Refresh the list views.
     * The method will load the data from the data models to the UI widgets and set up the difference blocks.
     * The list adapters will be prepared for delivering the data to UI widgets.
     *
     * @param contentView the view of the fragment.
     * @param categoryAssets the value items on the parent node.
     * @param previousCategoryAssets the value of the previous items on the parent node.
     */
    private void refreshView(View contentView, List<AssetsValue> categoryAssets, List<AssetsValue> previousCategoryAssets) {
        /* Initialize all the layout of the lists */
        LinearLayout liquidAssetsList = contentView.findViewById(R.id.liquid_assets_list);
        LinearLayout personalAssetsList = contentView.findViewById(R.id.personal_assets_list);
        LinearLayout taxableAccountsList = contentView.findViewById(R.id.taxable_accounts_list);
        LinearLayout retirementAccountsList = contentView.findViewById(R.id.retirement_accounts_list);
        LinearLayout ownershipInterestsList = contentView.findViewById(R.id.ownership_interests_list);

        /* Initialize all the adapters for the views */
        ReportListAdapter liquidAssetsAdapter = new ReportListAdapter(getContext(), liquidAssetsDataSource, getString(R.string.report_assets_fragment_key));
        ReportListAdapter personalAssetsAdapter = new ReportListAdapter(getContext(), personalAssetsDataSource, getString(R.string.report_assets_fragment_key));
        ReportListAdapter taxableAccountsAdapter = new ReportListAdapter(getContext(), taxableAccountsDataSource, getString(R.string.report_assets_fragment_key));
        ReportListAdapter retirementAccountsAdapter = new ReportListAdapter(getContext(), retirementAccountsDataSource, getString(R.string.report_assets_fragment_key));
        ReportListAdapter ownershipInterestsAdapter = new ReportListAdapter(getContext(), ownershipInterestsDataSource, getString(R.string.report_assets_fragment_key));

        /* Initialize all UI widgets */
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

        /* Load data to the Liquid Assets UI widgets */
        liquidAssetsValue.setText(String.valueOf(categoryAssets.get(0).getAssetsValue()));
        if (previousCategoryAssets.get(0) == null) {
            liquidAssetsSymbol.setText("");
            liquidAssetsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(0).getAssetsValue() - previousCategoryAssets.get(0).getAssetsValue();
            liquidAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                liquidAssetsSymbol.setText("");

            } else if (difference > 0){
                liquidAssetsSymbol.setText(getString(R.string.positive_symbol));
                liquidAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                liquidAssetsSymbol.setText(getString(R.string.negative_symbol));
                liquidAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Invested Assets UI widgets */
        investedAssetsValue.setText(String.valueOf(categoryAssets.get(1).getAssetsValue()));
        if (previousCategoryAssets.get(1) == null) {
            investedAssetsSymbol.setText("");
            investedAssetsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(1).getAssetsValue() - previousCategoryAssets.get(1).getAssetsValue();
            investedAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                investedAssetsSymbol.setText("");

            } else if (difference > 0){
                investedAssetsSymbol.setText(getString(R.string.positive_symbol));
                investedAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                investedAssetsSymbol.setText(getString(R.string.negative_symbol));
                investedAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Personal Assets UI widgets */
        personalAssetsValue.setText(String.valueOf(categoryAssets.get(2).getAssetsValue()));
        if (previousCategoryAssets.get(2) == null) {
            personalAssetsSymbol.setText("");
            personalAssetsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(2).getAssetsValue() - previousCategoryAssets.get(2).getAssetsValue();
            personalAssetsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                personalAssetsSymbol.setText("");

            } else if (difference > 0){
                personalAssetsSymbol.setText(getString(R.string.positive_symbol));
                personalAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                personalAssetsSymbol.setText(getString(R.string.negative_symbol));
                personalAssetsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Taxable Accounts UI widgets */
        taxableAccountsValue.setText(String.valueOf(categoryAssets.get(3).getAssetsValue()));
        if (previousCategoryAssets.get(3) == null) {
            taxableAccountsSymbol.setText("");
            taxableAccountsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(3).getAssetsValue() - (previousCategoryAssets.get(3).getAssetsValue());
            taxableAccountsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                taxableAccountsSymbol.setText("");

            } else if (difference > 0){
                taxableAccountsSymbol.setText(getString(R.string.positive_symbol));
                taxableAccountsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                taxableAccountsSymbol.setText(getString(R.string.negative_symbol));
                taxableAccountsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Retirement Accounts UI widgets */
        retirementAccountsValue.setText(String.valueOf(categoryAssets.get(4).getAssetsValue()));
        if (previousCategoryAssets.get(4) == null) {
            retirementAccountsSymbol.setText("");
            retirementAccountsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(4).getAssetsValue() - previousCategoryAssets.get(4).getAssetsValue();
            retirementAccountsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                retirementAccountsSymbol.setText("");

            } else if (difference > 0){
                retirementAccountsSymbol.setText(getString(R.string.positive_symbol));
                retirementAccountsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                retirementAccountsSymbol.setText(getString(R.string.negative_symbol));
                retirementAccountsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Ownership Interests UI widgets */
        ownershipInterestsValue.setText(String.valueOf(categoryAssets.get(5).getAssetsValue()));
        if (previousCategoryAssets.get(5) == null) {
            ownershipInterestsSymbol.setText("");
            ownershipInterestsDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryAssets.get(5).getAssetsValue() - previousCategoryAssets.get(5).getAssetsValue();
            ownershipInterestsDifference.setText(String.valueOf(difference));
            if (difference == 0){
                ownershipInterestsSymbol.setText("");

            } else if (difference > 0){
                ownershipInterestsSymbol.setText(getString(R.string.positive_symbol));
                ownershipInterestsDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                ownershipInterestsSymbol.setText(getString(R.string.negative_symbol));
                ownershipInterestsDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Add the item view to the corresponding list */
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
