package protect.FinanceLord.NetWorthReportEditing;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Communicators.DateCommunicator;
import protect.FinanceLord.DAOs.AssetsTypeDao;
import protect.FinanceLord.DAOs.AssetsValueDao;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeLeaf_Assets;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeProcessor_Assets;
import protect.FinanceLord.NetWorthDataStructure.ValueTreeProcessor_Assets;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.AssetsFragmentAdapter;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.AssetsFragmentChildViewClickListener;
import protect.FinanceLord.NetWorthReportEditingActivity;
import protect.FinanceLord.R;

public class Edit_AssetsFragment extends Fragment {

    private Date currentTime;
    private ExpandableListView expandableListView;

    private AssetsFragmentAdapter adapter;
    private ValueTreeProcessor_Assets valueTreeProcessor;
    private TypeTreeProcessor_Assets typeTreeProcessor;

    public Edit_AssetsFragment(Date currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NetWorthReportEditingActivity){
            NetWorthReportEditingActivity activity = (NetWorthReportEditingActivity) context;
            activity.toEditAssetsFragmentCommunicator = fromActivityCommunicator;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsFragmentView = inflater.inflate(R.layout.fragment_edit_assets, null);
        expandableListView = assetsFragmentView.findViewById(R.id.assets_list_view);
        RelativeLayout commitButton = assetsFragmentView.findViewById(R.id.assets_commit_button);

        initializeAssets();

        setUpCommitButton(commitButton);

        return assetsFragmentView;
    }

    private void setUpCommitButton(RelativeLayout commitButton) {
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_AssetsFragment.this.getContext());
                        AssetsValueDao assetsValueDao = database.assetsValueDao();

                        for(AssetsValue assetsValueInProcessor: Edit_AssetsFragment.this.valueTreeProcessor.getAllAssetsValues()) {
                            assetsValueInProcessor.setDate(currentTime.getTime());
                            Log.d("Edit_AFragment", "the time of the assets are set to " + currentTime);

                            if(assetsValueInProcessor.getAssetsPrimaryId() != 0) {
                                List<AssetsValue> assetsValues = assetsValueDao.queryAssetById(assetsValueInProcessor.getAssetsPrimaryId());
                                Log.d("Edit_AFragment", " Print assetsValues status " + assetsValues.isEmpty() +
                                        ", assets value is " + assetsValueInProcessor.getAssetsValue() +
                                        ", time stored in processor is " + new Date(assetsValueInProcessor.getDate()));
                                if(!assetsValues.isEmpty()) {
                                    assetsValueDao.updateAssetValue(assetsValueInProcessor);
                                    Log.d("Edit_AFragment", "update time is " + new Date(assetsValueInProcessor.getDate()));
                                } else {
                                    Log.w("Edit_AFragment", "The assets not exists in the database? check if there is anything went wrong!!");
                                }
                            } else {
                                assetsValueDao.insertAssetValue(assetsValueInProcessor);
                                Log.d("Edit_AFragment", "insert time is " + new Date(assetsValueInProcessor.getDate()));
                            }
                        }

                        Log.d("Edit_AFragment", "Query [Refreshing] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                        Log.d("Edit_AFragment", "current date: " + currentTime);

                        List<AssetsValue> assetsValues = assetsValueDao.queryAssetsByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                        Edit_AssetsFragment.this.valueTreeProcessor.setAllAssetsValues(assetsValues);

                        Log.d("Edit_AFragment", "Query assets values, " + assetsValues);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        valueTreeProcessor.insertOrUpdateParentAssets(assetsValueDao);
                        valueTreeProcessor.clearAllAssetsValues();

                        Log.d("Edit_AFragment", "Assets committed!");
                    }
                });
            }
        });
    }

    private void initializeAssets() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_AssetsFragment.this.getContext());
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();

                List<AssetsValue> assetsValues = assetsValueDao.queryAssetsByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                List<TypeTreeLeaf_Assets> assetsTypesTree = assetsTypeDao.queryAssetsTypeTreeAsList();

                Log.d("Edit_AFragment", "Query [Initialization] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                for (AssetsValue assetsValue : assetsValues){
                    Log.d("Edit_AFragment", "Query assets values, " + assetsValue.getAssetsId() + ", " + assetsValue.getAssetsValue() + ", " + new Date(assetsValue.getDate()));
                }
                Log.d("Edit_AFragment", "current date: " + currentTime);

                Edit_AssetsFragment.this.valueTreeProcessor = new ValueTreeProcessor_Assets(assetsTypesTree, assetsValues, currentTime, getContext());
                Edit_AssetsFragment.this.typeTreeProcessor = new TypeTreeProcessor_Assets(assetsTypesTree);
                adapter = new AssetsFragmentAdapter(getContext(), valueTreeProcessor, typeTreeProcessor,1, getString(R.string.total_assets_name));
                final AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(typeTreeProcessor.getSubGroup(null, 0), typeTreeProcessor, 0);
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

    private DateCommunicator fromActivityCommunicator = new DateCommunicator() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("Edit_AFragment","the user has selected date: " + currentTime);
            initializeAssets();
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
