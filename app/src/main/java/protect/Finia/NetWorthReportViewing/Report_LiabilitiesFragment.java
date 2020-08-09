package protect.Finia.NetWorthReportViewing;

import android.os.Bundle;
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

import protect.Finia.Database.FiniaDatabase;
import protect.Finia.DAOs.LiabilitiesTypeDao;
import protect.Finia.NetWorthDataStructure.TypeTreeLeaf_Liabilities;
import protect.Finia.Database.LiabilitiesValue;
import protect.Finia.DAOs.LiabilitiesValueDao;
import protect.Finia.NetWorthDataStructure.NodeContainer_Liabilities;
import protect.Finia.NetWorthDataStructure.TypeTreeProcessor_Liabilities;
import protect.Finia.R;

/**
 * The fragment for the Liabilities Fragment sheet.
 * The fragment contains the list of all the liabilities items.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class Report_LiabilitiesFragment extends Fragment {

    private Date itemTime;
    private View contentView;
    private TypeTreeProcessor_Liabilities liabilitiesTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> shortTermLiabilitiesDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> longTermLiabilitiesDataSource = new ArrayList<>();

    public Report_LiabilitiesFragment(Date itemTime) {
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
     * @return the view of the liabilities fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_report_liabilities, null);

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
                FiniaDatabase database = FiniaDatabase.getInstance(Report_LiabilitiesFragment.this.getContext());
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                List<LiabilitiesValue> categoryLiabilities = new ArrayList<>();
                List<LiabilitiesValue> previousCategoryLiabilities = new ArrayList<>();

                List<TypeTreeLeaf_Liabilities> liabilitiesTypes = liabilitiesTypeDao.queryLiabilitiesTypeTreeAsList();
                Report_LiabilitiesFragment.this.liabilitiesTypeProcessor = new TypeTreeProcessor_Liabilities(liabilitiesTypes);

                LiabilitiesValue totalShortTermLiabilities = liabilitiesValueDao.queryIndividualLiabilityByTime(itemTime.getTime(), 12);
                LiabilitiesValue totalLongTermLiabilities = liabilitiesValueDao.queryIndividualLiabilityByTime(itemTime.getTime(), 13);

                categoryLiabilities.add(totalShortTermLiabilities);
                categoryLiabilities.add(totalLongTermLiabilities);

                LiabilitiesValue previousShortTermLiabilities = liabilitiesValueDao.queryPreviousLiabilityBeforeTime(itemTime.getTime(), 12);
                LiabilitiesValue previousLongTermLiabilities = liabilitiesValueDao.queryPreviousLiabilityBeforeTime(itemTime.getTime(), 13);

                previousCategoryLiabilities.add(previousShortTermLiabilities);
                previousCategoryLiabilities.add(previousLongTermLiabilities);

                populateDataModel(liabilitiesValueDao, itemTime, categoryLiabilities, previousCategoryLiabilities);
            }
        });
    }

    /**
     * Populate the data models with data.
     * The data are all related to items that are on the parent nodes.
     * Once, the population of data is complete, the method to refresh the view will be called.
     *
     * @param liabilitiesValueDao the data access object to insert LiabilitiesValue object.
     * @param itemTime the time of the item.
     * @param categoryLiabilities the value items on the parent node.
     * @param previousCategoryLiabilities the value of the previous items on the parent node.
     */
    private void populateDataModel(LiabilitiesValueDao liabilitiesValueDao, Date itemTime, final List<LiabilitiesValue> categoryLiabilities, final List<LiabilitiesValue> previousCategoryLiabilities) {
        /* retrieve all values of leaf nodes and group them into lists */
        List<NodeContainer_Liabilities> shortTermLiabilitiesTypes = liabilitiesTypeProcessor.getSubGroup(getString(R.string.short_term_liabilities_name),2);
        List<NodeContainer_Liabilities> longTermLiabilitiesTypes = liabilitiesTypeProcessor.getSubGroup(getString(R.string.long_term_liabilities_name), 2);

        /* Set up short term liabilities item */
        for (NodeContainer_Liabilities dataCarrier : shortTermLiabilitiesTypes) {
            String difference = getString(R.string.no_data_message);
            String thisLiabilityValue = getString(R.string.no_data_message);
            LiabilitiesValue shortTermLiabilityValue = liabilitiesValueDao.queryIndividualLiabilityByTime(itemTime.getTime(), dataCarrier.liabilitiesId);
            LiabilitiesValue previousLiabilityValue = liabilitiesValueDao.queryPreviousLiabilityBeforeTime(itemTime.getTime(), dataCarrier.liabilitiesId);

            if (shortTermLiabilityValue != null) {
                if (previousLiabilityValue != null){
                    difference = String.valueOf(shortTermLiabilityValue.getLiabilitiesValue() - previousLiabilityValue.getLiabilitiesValue());
                }
                thisLiabilityValue = String.valueOf(shortTermLiabilityValue.getLiabilitiesValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.liabilitiesTypeName, thisLiabilityValue, difference);
                shortTermLiabilitiesDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.liabilitiesTypeName, thisLiabilityValue, difference);
                shortTermLiabilitiesDataSource.add(dataModel);
            }
        }

        /* Set up long term liabilities item */
        for (NodeContainer_Liabilities dataCarrier : longTermLiabilitiesTypes) {
            String difference = getString(R.string.no_data_message);
            String thisLiabilityValue = getString(R.string.no_data_message);
            LiabilitiesValue longTermLiabilityValue = liabilitiesValueDao.queryIndividualLiabilityByTime(itemTime.getTime(), dataCarrier.liabilitiesId);
            LiabilitiesValue previousLiabilityValue = liabilitiesValueDao.queryPreviousLiabilityBeforeTime(itemTime.getTime(), dataCarrier.liabilitiesId);

            if (longTermLiabilityValue != null) {
                if (previousLiabilityValue != null){
                    difference = String.valueOf(longTermLiabilityValue.getLiabilitiesValue() - previousLiabilityValue.getLiabilitiesValue());
                }
                thisLiabilityValue = String.valueOf(longTermLiabilityValue.getLiabilitiesValue());
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.liabilitiesTypeName, thisLiabilityValue, difference);
                longTermLiabilitiesDataSource.add(dataModel);
            } else {
                NetWorthItemsDataModel dataModel = new NetWorthItemsDataModel(dataCarrier.liabilitiesTypeName, thisLiabilityValue, difference);
                longTermLiabilitiesDataSource.add(dataModel);
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshView(contentView, categoryLiabilities, previousCategoryLiabilities);
            }
        });
    }

    /**
     * Refresh the list views.
     * The method will load the data from the data models to the UI widgets and set up the difference blocks.
     * The list adapters will be prepared for delivering the data to UI widgets.
     *
     * @param contentView the view of the fragment.
     * @param categoryLiabilities the value items on the parent node.
     * @param previousCategoryLiabilities the value of the previous items on the parent node.
     */
    private void refreshView(View contentView, List<LiabilitiesValue> categoryLiabilities, List<LiabilitiesValue> previousCategoryLiabilities) {
        /* Initialize all the layout of the lists */
        LinearLayout shortTermLiabilitiesList = contentView.findViewById(R.id.short_term_liabilities_list);
        LinearLayout longTermLiabilitiesList = contentView.findViewById(R.id.long_term_liabilities_list);

        /* Initialize all the adapters for the views */
        ReportListAdapter shortTermLiabilitiesAdapter = new ReportListAdapter(getContext(), shortTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_key));
        ReportListAdapter longTermLiabilitiesAdapter = new ReportListAdapter(getContext(), longTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_key));

        /* Initialize all UI widgets */
        TextView shortTermLiabilitiesValue = contentView.findViewById(R.id.report_short_term_liabilities_value);
        View shortTermLiabilitiesDifferenceBlock = contentView.findViewById(R.id.report_short_term_liabilities_difference_block);
        TextView shortTermLiabilitiesSymbol = contentView.findViewById(R.id.report_short_term_liabilities_symbol);
        TextView shortTermLiabilitiesDifference = contentView.findViewById(R.id.report_short_term_liabilities_difference);

        TextView longTermLiabilitiesValue = contentView.findViewById(R.id.report_long_term_liabilities_value);
        View longTermLiabilitiesDifferenceBlock = contentView.findViewById(R.id.report_long_term_liabilities_difference_block);
        TextView longTermLiabilitiesSymbol = contentView.findViewById(R.id.report_long_term_liabilities_symbol);
        TextView longTermLiabilitiesDifference = contentView.findViewById(R.id.report_long_term_liabilities_difference);

        /* Load data to the Short Term Liabilities to UI widgets */
        shortTermLiabilitiesValue.setText(String.valueOf(categoryLiabilities.get(0).getLiabilitiesValue()));
        if (previousCategoryLiabilities.get(0) == null) {
            shortTermLiabilitiesSymbol.setText("");
            shortTermLiabilitiesDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryLiabilities.get(0).getLiabilitiesValue() - previousCategoryLiabilities.get(0).getLiabilitiesValue();
            shortTermLiabilitiesDifference.setText(String.valueOf(difference));
            if (difference == 0){
                shortTermLiabilitiesSymbol.setText("");

            } else if (difference > 0){
                shortTermLiabilitiesSymbol.setText(getString(R.string.positive_symbol));
                shortTermLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                shortTermLiabilitiesSymbol.setText(getString(R.string.negative_symbol));
                shortTermLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        /* Load data to the Long Term Liabilities to UI widgets */
        longTermLiabilitiesValue.setText(String.valueOf(categoryLiabilities.get(1).getLiabilitiesValue()));
        if (previousCategoryLiabilities.get(1) == null) {
            longTermLiabilitiesSymbol.setText("");
            longTermLiabilitiesDifference.setText(R.string.no_data_message);

        } else {
            float difference = categoryLiabilities.get(1).getLiabilitiesValue() - previousCategoryLiabilities.get(1).getLiabilitiesValue();
            longTermLiabilitiesDifference.setText(String.valueOf(difference));
            if (difference == 0){
                longTermLiabilitiesSymbol.setText("");

            } else if (difference > 0){
                longTermLiabilitiesSymbol.setText(getString(R.string.positive_symbol));
                longTermLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_increase);

            } else if (difference < 0){
                longTermLiabilitiesSymbol.setText(getString(R.string.negative_symbol));
                longTermLiabilitiesDifferenceBlock.setBackgroundResource(R.drawable.net_decrease);
            }
        }

        for (int i = 0; i < shortTermLiabilitiesAdapter.getCount(); i++) {
            View itemView = shortTermLiabilitiesAdapter.getView(i, null, shortTermLiabilitiesList);
            shortTermLiabilitiesList.addView(itemView);
        }

        for (int i = 0; i < longTermLiabilitiesAdapter.getCount(); i++) {
            View itemView = longTermLiabilitiesAdapter.getView(i, null, longTermLiabilitiesList);
            longTermLiabilitiesList.addView(itemView);
        }
    }
}
