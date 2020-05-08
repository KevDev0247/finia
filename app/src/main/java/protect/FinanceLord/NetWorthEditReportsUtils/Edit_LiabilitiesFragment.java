package protect.FinanceLord.NetWorthEditReportsUtils;

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

import protect.FinanceLord.Communicators.ActivityToFragment;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.Database.LiabilitiesTypeDao;
import protect.FinanceLord.Database.LiabilitiesTypeQuery;
import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.Database.LiabilitiesValueDao;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Liabilities;
import protect.FinanceLord.NetWorthEditReportActivity;
import protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils.LiabilitiesFragmentAdapter;
import protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils.LiabilitiesFragmentChildViewClickListener;
import protect.FinanceLord.R;

public class Edit_LiabilitiesFragment extends Fragment {

    String title;
    Date currentTime;
    View liabilitiesFragmentView;
    ExpandableListView expandableListView;

    private LiabilitiesFragmentAdapter adapter;
    private DataProcessor_Liabilities dataProcessor;

    public Edit_LiabilitiesFragment(String title, Date currentTime) {
        this.title = title;
        this.currentTime = currentTime;
    }

    //communicators

    ActivityToFragment fromActivityCommunicator = new ActivityToFragment() {
        @Override
        public void onActivityMessage(Date date) {
            currentTime = date;
            Log.d("Edit_LFragment","the user has selected date: " + currentTime);
            initLiabilities();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NetWorthEditReportActivity){
            NetWorthEditReportActivity activity = (NetWorthEditReportActivity) context;
            activity.toFragmentsCommunicator = this.fromActivityCommunicator;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        liabilitiesFragmentView = inflater.inflate(R.layout.fragment_edit_liabilities, null);
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

                        for(LiabilitiesValue liabilitiesValueInProcessor: Edit_LiabilitiesFragment.this.dataProcessor.getAllLiabilitiesValues()) {

                            liabilitiesValueInProcessor.setDate(currentTime.getTime());

                            if (liabilitiesValueInProcessor.getLiabilitiesPrimaryId() != 0){
                                List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryLiabilitiesById(liabilitiesValueInProcessor.getLiabilitiesPrimaryId());
                                if (!liabilitiesValues.isEmpty()){
                                    liabilitiesValueDao.insertLiabilityValue(liabilitiesValueInProcessor);
                                } else {

                                }

                            } else {
                                liabilitiesValueDao.updateLiabilityValue(liabilitiesValueInProcessor);
                            }

                            List<LiabilitiesValue> liabilitiesValues = liabilitiesValueDao.queryLiabilitiesByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                            Edit_LiabilitiesFragment.this.dataProcessor.setAllLiabilitiesValues(liabilitiesValues);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                            dataProcessor.calculateAndInsertParentLiabilities(liabilitiesValueDao);
                            dataProcessor.clearAllLiabilitiesValues();
                        }
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
                List<LiabilitiesTypeQuery> liabilitiesTypes = liabilitiesTypeDao.queryGroupedLiabilitiesType();

                Edit_LiabilitiesFragment.this.dataProcessor = new DataProcessor_Liabilities(liabilitiesTypes, liabilitiesValues, currentTime, getContext());
                adapter = new LiabilitiesFragmentAdapter(getContext(), dataProcessor, 1, getString(R.string.total_liabilities_name));
                final LiabilitiesFragmentChildViewClickListener listener = new LiabilitiesFragmentChildViewClickListener(dataProcessor.getSubSet(null, 0), dataProcessor, 0);
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

    public Date getQueryStartTime(){
        Date date;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    public Date getQueryEndTime(){
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
