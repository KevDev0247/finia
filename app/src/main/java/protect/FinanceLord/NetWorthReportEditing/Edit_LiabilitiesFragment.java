package protect.FinanceLord.NetWorthReportEditing;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.DateCommunicator;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.DAOs.LiabilitiesTypeDao;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeLeaf_Liabilities;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.DAOs.LiabilitiesValueDao;
import protect.FinanceLord.NetWorthDataStructure.ValueTreeProcessor_Liabilities;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeProcessor_Liabilities;
import protect.FinanceLord.NetWorthReportEditingActivity;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.LiabilitiesFragmentAdapter;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.LiabilitiesFragmentChildViewClickListener;
import protect.FinanceLord.R;

public class Edit_LiabilitiesFragment extends Fragment {

    private Date currentTime;
    private ExpandableListView expandableListView;

    private LiabilitiesFragmentAdapter adapter;
    private ValueTreeProcessor_Liabilities valueTreeProcessor;
    private TypeTreeProcessor_Liabilities typeTreeProcessor;

    public Edit_LiabilitiesFragment(Date currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NetWorthReportEditingActivity){
            NetWorthReportEditingActivity activity = (NetWorthReportEditingActivity) context;
            activity.toEditLiabilitiesFragmentCommunicator = fromActivityCommunicator;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View liabilitiesFragmentView = inflater.inflate(R.layout.fragment_edit_liabilities, null);
        expandableListView = liabilitiesFragmentView.findViewById(R.id.liabilities_list_view);
        Button commitButton = liabilitiesFragmentView.findViewById(R.id.liabilities_commit_button);

        initLiabilities();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_LiabilitiesFragment.this.getContext());
                        LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                        for(LiabilitiesValue liabilitiesValueInProcessor: Edit_LiabilitiesFragment.this.valueTreeProcessor.getAllLiabilitiesValues()) {

                            liabilitiesValueInProcessor.setDate(currentTime.getTime());
                            Log.d("Edit_LFragment", "the time of the assets are set to " + currentTime);

                            if (liabilitiesValueInProcessor.getLiabilitiesPrimaryId() != 0){
                                List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryLiabilitiesById(liabilitiesValueInProcessor.getLiabilitiesPrimaryId());
                                Log.d("Edit_LFragment", " Print assetsValues status " + liabilitiesValues.isEmpty() +
                                        ", assets value is " + liabilitiesValueInProcessor.getLiabilitiesValue() +
                                        ", time stored in processor is " + new Date(liabilitiesValueInProcessor.getDate()));
                                if (!liabilitiesValues.isEmpty()){
                                    liabilitiesValueDao.updateLiabilityValue(liabilitiesValueInProcessor);
                                    Log.d("Edit_LFragment", "update time is " + new Date(liabilitiesValueInProcessor.getDate()));
                                } else {
                                    Log.w("Edit_LFragment", "The assets not exists in the database? check if there is anything went wrong!!");
                                }
                            } else {
                                liabilitiesValueDao.insertLiabilityValue(liabilitiesValueInProcessor);
                                Log.d("Edit_LFragment", "insert time is " + new Date(liabilitiesValueInProcessor.getDate()));
                            }
                        }

                        Log.d("Edit_LFragment", "Query [Refreshing] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                        Log.d("Edit_LFragment", "current date: " + currentTime);

                        List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryLiabilitiesByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                        Edit_LiabilitiesFragment.this.valueTreeProcessor.setAllLiabilitiesValues(liabilitiesValues);

                        Log.d("Edit_LFragment", "Query assets values, " + liabilitiesValues);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        valueTreeProcessor.insertOrUpdateParentLiabilities(liabilitiesValueDao);
                        valueTreeProcessor.clearAllLiabilitiesValues();

                        Log.d("Edit_LFragment", "Liabilities committed!");
                    }
                });
            }
        });

        return liabilitiesFragmentView;
    }

    private void initLiabilities() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_LiabilitiesFragment.this.getContext());
                LiabilitiesTypeDao liabilitiesTypeDao = database.liabilitiesTypeDao();
                LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

                List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryLiabilitiesByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                List<TypeTreeLeaf_Liabilities> liabilitiesTypesTree = liabilitiesTypeDao.queryLiabilitiesTypeTreeAsList();

                Log.d("Edit_LFragment", "Query [Initialization] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                for (LiabilitiesValue liabilitiesValue : liabilitiesValues){
                    Log.d("Edit_LFragment", "Query liabilities values, " + liabilitiesValue.getLiabilitiesId() + ", " + liabilitiesValue.getLiabilitiesValue() + ", " + new Date(liabilitiesValue.getDate()) );
                }
                Log.d("Edit_LFragment", "current date: " + currentTime);

                Edit_LiabilitiesFragment.this.valueTreeProcessor = new ValueTreeProcessor_Liabilities(liabilitiesTypesTree, liabilitiesValues, currentTime, getContext());
                Edit_LiabilitiesFragment.this.typeTreeProcessor = new TypeTreeProcessor_Liabilities(liabilitiesTypesTree);
                adapter = new LiabilitiesFragmentAdapter(getContext(), valueTreeProcessor, typeTreeProcessor,1, getString(R.string.total_liabilities_name));
                final LiabilitiesFragmentChildViewClickListener listener = new LiabilitiesFragmentChildViewClickListener(typeTreeProcessor.getSubGroup(null, 0), typeTreeProcessor,0);
                Edit_LiabilitiesFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        expandableListView.setAdapter(adapter);
                        expandableListView.setOnChildClickListener(listener);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private DateCommunicator fromActivityCommunicator = new DateCommunicator() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("Edit_LFragment","the user has selected date: " + currentTime);
            initLiabilities();
        }
    };

    private Date getQueryStartTime(){
        Date date;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    private Date getQueryEndTime(){
        Date date;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date = calendar.getTime();
        return date;
    }
}
