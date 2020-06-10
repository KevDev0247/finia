package protect.FinanceLord.NetWorthReportViewing;

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

import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesTypeTree;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Liabilities;
import protect.FinanceLord.R;

public class Report_LiabilitiesFragment extends Fragment {

    private Date itemTime;
    private View contentView;
    private TypeProcessor_Liabilities liabilitiesTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> shortTermLiabilitiesDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> longTermLiabilitiesDataSource = new ArrayList<>();

    public Report_LiabilitiesFragment(Date itemTime) {
        this.itemTime = itemTime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_report_liabilities, null);

        getDataFromDatabase(itemTime);

        return contentView;
    }

    private void getDataFromDatabase(final Date itemTime) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Report_LiabilitiesFragment.this.getContext());
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                List<LiabilitiesValue> categoryLiabilities = new ArrayList<>();
                List<LiabilitiesValue> previousCategoryLiabilities = new ArrayList<>();

                List<LiabilitiesTypeTree> liabilitiesTypes = liabilitiesTypeDao.queryGroupedLiabilitiesType();
                Report_LiabilitiesFragment.this.liabilitiesTypeProcessor = new TypeProcessor_Liabilities(liabilitiesTypes);

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

    public void populateDataModel(LiabilitiesValueDao liabilitiesValueDao, Date itemTime, final List<LiabilitiesValue> categoryLiabilities, final List<LiabilitiesValue> previousCategoryLiabilities) {
        List<DataCarrier_Liabilities> shortTermLiabilitiesTypes = liabilitiesTypeProcessor.getSubGroup(getString(R.string.short_term_liabilities_name),2);
        List<DataCarrier_Liabilities> longTermLiabilitiesTypes = liabilitiesTypeProcessor.getSubGroup(getString(R.string.long_term_liabilities_name), 2);

        for (DataCarrier_Liabilities dataCarrier : shortTermLiabilitiesTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisLiabilityValue = getString(R.string.no_data_initialization);
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

        for (DataCarrier_Liabilities dataCarrier : longTermLiabilitiesTypes) {
            String difference = getString(R.string.no_data_initialization);
            String thisLiabilityValue = getString(R.string.no_data_initialization);
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

    public void refreshView(View contentView, List<LiabilitiesValue> categoryLiabilities, List<LiabilitiesValue> previousCategoryLiabilities) {
        LinearLayout shortTermLiabilitiesList = contentView.findViewById(R.id.short_term_liabilities_list);
        LinearLayout longTermLiabilitiesList = contentView.findViewById(R.id.long_term_liabilities_list);

        ReportListAdapter shortTermLiabilitiesAdapter = new ReportListAdapter(getContext(), shortTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_key));
        ReportListAdapter longTermLiabilitiesAdapter = new ReportListAdapter(getContext(), longTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_key));

        TextView shortTermLiabilitiesValue = contentView.findViewById(R.id.report_short_term_liabilities_value);
        View shortTermLiabilitiesDifferenceBlock = contentView.findViewById(R.id.report_short_term_liabilities_difference_block);
        TextView shortTermLiabilitiesSymbol = contentView.findViewById(R.id.report_short_term_liabilities_symbol);
        TextView shortTermLiabilitiesDifference = contentView.findViewById(R.id.report_short_term_liabilities_difference);

        TextView longTermLiabilitiesValue = contentView.findViewById(R.id.report_long_term_liabilities_value);
        View longTermLiabilitiesDifferenceBlock = contentView.findViewById(R.id.report_long_term_liabilities_difference_block);
        TextView longTermLiabilitiesSymbol = contentView.findViewById(R.id.report_long_term_liabilities_symbol);
        TextView longTermLiabilitiesDifference = contentView.findViewById(R.id.report_long_term_liabilities_difference);

        shortTermLiabilitiesValue.setText(String.valueOf(categoryLiabilities.get(0).getLiabilitiesValue()));
        if (previousCategoryLiabilities.get(0) == null) {
            shortTermLiabilitiesSymbol.setText("");
            shortTermLiabilitiesDifference.setText(R.string.no_data_initialization);

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

        longTermLiabilitiesValue.setText(String.valueOf(categoryLiabilities.get(1).getLiabilitiesValue()));
        if (previousCategoryLiabilities.get(1) == null) {
            longTermLiabilitiesSymbol.setText("");
            longTermLiabilitiesDifference.setText(R.string.no_data_initialization);

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
