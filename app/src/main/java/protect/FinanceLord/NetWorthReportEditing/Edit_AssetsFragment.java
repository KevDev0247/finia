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
import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeTree;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;
import protect.FinanceLord.NetWorthReportEditingActivity;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.AssetsFragmentAdapter;
import protect.FinanceLord.NetWorthReportEditing.FragmentUtils.AssetsFragmentChildViewClickListener;
import protect.FinanceLord.R;

public class Edit_AssetsFragment extends Fragment {

    private Date currentTime;
    private ExpandableListView expandableListView;

    private AssetsFragmentAdapter adapter;
    private DataProcessor_Assets dataProcessor;
    private TypeProcessor_Assets typeProcessor;
    private DateCommunicator fromActivityCommunicator = new DateCommunicator() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("Edit_AFragment","the user has selected date: " + currentTime);
            initAssets();
        }
    };

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
        Button commitButton = assetsFragmentView.findViewById(R.id.assets_commit_button);

        initAssets();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(Edit_AssetsFragment.this.getContext());
                        AssetsValueDao assetsValueDao = database.assetsValueDao();

                        for(AssetsValue assetsValueInProcessor: Edit_AssetsFragment.this.dataProcessor.getAllAssetsValues()) {
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
                        Edit_AssetsFragment.this.dataProcessor.setAllAssetsValues(assetsValues);

                        Log.d("Edit_AFragment", "Query assets values, " + assetsValues);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                        dataProcessor.insertOrUpdateParentAssets(assetsValueDao);
                        dataProcessor.clearAllAssetsValues();

                        //cannot update on the page after insertion!
                        Log.d("Edit_AFragment", "Assets committed!");
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

                List<AssetsValue> assetsValues = assetsValueDao.queryAssetsByTimePeriod(getQueryStartTime().getTime(), getQueryEndTime().getTime());
                List<AssetsTypeTree> assetsTypes = assetsTypeDao.queryGroupedAssetsType();

                Log.d("Edit_AFragment", "Query [Initialization] time interval is " + getQueryStartTime() + " and " + getQueryEndTime());
                for (AssetsValue assetsValue : assetsValues){
                    Log.d("Edit_AFragment", "Query assets values, " + assetsValue.getAssetsId() + ", " + assetsValue.getAssetsValue() + ", " + new Date(assetsValue.getDate()));
                }
                Log.d("Edit_AFragment", "current date: " + currentTime);

                Edit_AssetsFragment.this.dataProcessor = new DataProcessor_Assets(assetsTypes, assetsValues, currentTime, getContext());
                Edit_AssetsFragment.this.typeProcessor = new TypeProcessor_Assets(assetsTypes);
                adapter = new AssetsFragmentAdapter(getContext(), dataProcessor, typeProcessor,1, getString(R.string.total_assets_name));
                final AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(typeProcessor.getSubGroup(null, 0), typeProcessor, 0);
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
