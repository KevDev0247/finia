package protect.FinanceLord.NetWorthEditReportsUtils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthEditReportActivity;
import protect.FinanceLord.ParentActivityCommunicator;
import protect.FinanceLord.R;
import protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils.AssetsFragmentAdapter;
import protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils.AssetsFragmentChildViewClickListener;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;

public class Edit_AssetsFragment extends Fragment {

    String title;
    Date currentTime;
    View assetsFragmentView;
    ExpandableListView expandableListView;

    private AssetsFragmentAdapter adapter;
    private DataProcessor_Assets dataProcessor;

    public Edit_AssetsFragment(String title, Date currentTime) {
        this.title = title;
        this.currentTime = currentTime;
    }

    ParentActivityCommunicator parentActivityCommunicator = new ParentActivityCommunicator() {
        @Override
        public void onActivityMessage(Date date) {
            currentTime = date;
            initAssets();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NetWorthEditReportActivity){
            NetWorthEditReportActivity activity = (NetWorthEditReportActivity) context;
            activity.parentActivityCommunicator = this.parentActivityCommunicator;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assetsFragmentView = inflater.inflate(R.layout.fragment_edit_assets, null);
        expandableListView = assetsFragmentView.findViewById(R.id.assets_list_view);
        Button btnCommit = assetsFragmentView.findViewById(R.id.btnCommit);

        initAssets();

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_AssetsFragment.this.getContext());
                        AssetsValueDao assetsValueDao = database.assetsValueDao();

                        for(AssetsValue assetsValueInProcessor: Edit_AssetsFragment.this.dataProcessor.getAllAssetsValues()) {
                            if(assetsValueInProcessor.getAssetsPrimaryId() != 0) {
                                List<AssetsValue> assetsValues = assetsValueDao.queryAsset(assetsValueInProcessor.getAssetsPrimaryId());
                                Log.d("Edit_AssetsFragment", " Print assetsValues status " + assetsValues.isEmpty() + " assets value is " + assetsValueInProcessor.getAssetsValue());
                                if(!assetsValues.isEmpty()) {
                                    assetsValueDao.updateAssetValue(assetsValueInProcessor);
                                    Log.d("Edit_AssetsFragment", "update time is " + currentTime);
                                } else {
                                    Log.w("Edit_AssetsFragment", "The assets not exists in the database? check if there is anything went wrong!!");
                                }
                            } else {
                                assetsValueDao.insertAssetValue(assetsValueInProcessor);
                                Log.d("Edit_AssetsFragment", "insert time is " + currentTime);
                            }
                        }

                        Log.d("Edit_AssetsFragment", "Query [Refreshing] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                        Edit_AssetsFragment.this.dataProcessor.setAllAssetsValues(assetsValueDao.queryAssetsByDate(getQueryStartTime().getTime(), getQueryEndTime().getTime()));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        dataProcessor.calculateParentAssets(assetsValueDao);
                        
                        Log.d("Edit_AssetsFragment", "Assets committed!");
                    }
                });
            }
        });

        return assetsFragmentView;
    }

    public void initAssets() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_AssetsFragment.this.getContext());
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();

                List<AssetsValue> assetsValues = assetsValueDao.queryAssetsByDate(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                List<AssetsTypeQuery> assetsTypes = assetsTypeDao.queryGroupedAssetsType();

                Log.d("Edit_AssetsFragment", "Query [Initialization] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());

                Edit_AssetsFragment.this.dataProcessor = new DataProcessor_Assets(assetsTypes, assetsValues);
                adapter = new AssetsFragmentAdapter(Edit_AssetsFragment.this.getContext(), dataProcessor, 1,"Total Assets");
                final AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(dataProcessor.getSubSet(null, 0), dataProcessor, 0);
                Edit_AssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
        calendar.set(Calendar.YEAR, currentTime.getYear());
        calendar.set(Calendar.MONTH, currentTime.getMonth());
        calendar.set(Calendar.DATE, currentTime.getDate());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    public Date getQueryEndTime(){
        Date date;
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, currentTime.getYear());
        calendar.set(Calendar.MONTH, currentTime.getMonth());
        calendar.set(Calendar.DATE, currentTime.getDate());
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date = calendar.getTime();
        return date;
    }
}
