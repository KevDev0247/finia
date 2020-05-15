package protect.FinanceLord.NetWorthReportViewingUtils;

import android.os.Bundle;
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

import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesTypeQuery;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Liabilities;
import protect.FinanceLord.R;

public class Report_LiabilitiesFragment extends Fragment {

    String title;
    private Date itemTime;
    private View contentView;
    private TypeProcessor_Liabilities liabilitiesTypeProcessor;
    private ArrayList<NetWorthItemsDataModel> shortTermLiabilitiesDataSource = new ArrayList<>();
    private ArrayList<NetWorthItemsDataModel> longTermLiabilitiesDataSource = new ArrayList<>();

    public Report_LiabilitiesFragment(String title, Date itemTime) {
        this.title = title;
        this.itemTime = itemTime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View liabilitiesView = inflater.inflate(R.layout.fragment_report_liabilities, null);
        this.contentView = liabilitiesView;

        getDataFromDatabase(itemTime);

        return liabilitiesView;
    }

    private void getDataFromDatabase(final Date itemTime) {

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Report_LiabilitiesFragment.this.getContext());
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                List<LiabilitiesTypeQuery> liabilitiesTypes = liabilitiesTypeDao.queryGroupedLiabilitiesType();
                Report_LiabilitiesFragment.this.liabilitiesTypeProcessor = new TypeProcessor_Liabilities(liabilitiesTypes);

                populateDataModel(liabilitiesValueDao, itemTime);
            }
        });
    }

    public void populateDataModel(LiabilitiesValueDao liabilitiesValueDao, Date itemTime) {

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
                refreshView(contentView);
            }
        });
    }

    public void refreshView(View liabilitiesView) {

        LinearLayout shortTermLiabilitiesList = liabilitiesView.findViewById(R.id.short_term_liabilities_list);
        LinearLayout longTermLiabilitiesList = liabilitiesView.findViewById(R.id.long_term_liabilities_list);

        ReportListAdapter shortTermLiabilitiesAdapter = new ReportListAdapter(getContext(), shortTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_name));
        ReportListAdapter longTermLiabilitiesAdapter = new ReportListAdapter(getContext(), longTermLiabilitiesDataSource, getString(R.string.report_liabilities_fragment_name));

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
