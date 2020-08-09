package protect.Finia.NetWorthReportEditing;

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

import protect.Finia.Communicators.DateCommunicator;
import protect.Finia.DAOs.AssetsTypeDao;
import protect.Finia.DAOs.AssetsValueDao;
import protect.Finia.Database.AssetsValue;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.NetWorthDataStructure.TypeTreeLeaf_Assets;
import protect.Finia.NetWorthDataStructure.TypeTreeProcessor_Assets;
import protect.Finia.NetWorthDataStructure.ValueTreeProcessor_Assets;
import protect.Finia.NetWorthReportEditing.FragmentUtils.AssetsFragmentAdapter;
import protect.Finia.NetWorthReportEditing.FragmentUtils.AssetsFragmentChildViewClickListener;
import protect.Finia.Activities.NetWorthReportEditingActivity;
import protect.Finia.R;

/**
 * The class for the fragment to edit the assets sheet.
 * The class includes the expandable list, input fields, and commit button.
 * The custom expandable list in the fragment is a part of UX design designed to improve user experiences to avoid
 * the scenario when user cannot find the right category and input fields.
 * They group items into appropriate parent categories so that the input sheet can be more clean and organized.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class Edit_AssetsFragment extends Fragment {

    private Date currentTime;
    private ExpandableListView expandableListView;

    private AssetsFragmentAdapter adapter;
    private ValueTreeProcessor_Assets valueTreeProcessor;
    private TypeTreeProcessor_Assets typeTreeProcessor;

    public Edit_AssetsFragment(Date currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Attach the fragment to the activity it belongs to.
     * In this method, the fragment will retrieve the instance of communicator in the activity
     * in order to communicate with the activity
     *
     * @param context the context of this fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NetWorthReportEditingActivity) {
            NetWorthReportEditingActivity activity = (NetWorthReportEditingActivity) context;
            activity.toEditAssetsFragmentCommunicator = fromActivityCommunicator;
        }
    }

    /**
     * Create the view of the fragment.
     * First, the method will first set the view of the content by finding the corresponding layout file through id.
     * Then, the method will first set the view of the the expandable list view by finding the corresponding layout file through id.
     * Next, the assets types and values are retrieved from database and set up the expandable list view.
     * Lastly, the method will set up the tree processor and commit and delete logic of the buttons.
     * The commit logic include how the data was retrieved from the input fields and transferred to the type and value tree processor to prepare for the insertion of the data.
     * The delete logic include the action to delete the report of assets at a time that the user picked.
     * Note that delete action only applies to the whole report. If the user merely want to change a number, then it is better to just use the edit mode.
     *
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     * @return the view of the assets fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsFragmentView = inflater.inflate(R.layout.fragment_edit_assets, null);
        expandableListView = assetsFragmentView.findViewById(R.id.assets_list_view);
        RelativeLayout commitButton = assetsFragmentView.findViewById(R.id.assets_commit_button);
        RelativeLayout deleteButton = assetsFragmentView.findViewById(R.id.assets_delete_button);

        initializeAssets();

        setUpCommitAndDeleteButton(commitButton, deleteButton);

        return assetsFragmentView;
    }

    /**
     * Set up the commit and delete button.
     * For the commit logic, the data source in the processor is retrieved and stored into a list.
     * Then the list of data is traversed and inserted or updated into the database.
     * Next, the data is displayed onto the expandable list.
     * Lastly, the data in the processor is deleted.
     * The data source act as a cache for the expandable list.
     * After the data is inserted, the data in the data source is cleared.
     * For the delete logic, the whole report will be deleted.
     *
     * @param commitButton the instance of commit button.
     */
    private void setUpCommitAndDeleteButton(RelativeLayout commitButton, RelativeLayout deleteButton) {
        FiniaDatabase database = FiniaDatabase.getInstance(Edit_AssetsFragment.this.getContext());
        final AssetsValueDao assetsValueDao = database.assetsValueDao();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        assetsValueDao.deleteAssetsAtDate(currentTime.getTime());
                        getActivity().finish();
                    }
                });
            }
        });
    }

    /**
     * Initialize assets values, types, and tree processor set up.
     * First, the assets values of the date is queried from the database.
     * Then, with the assets values and types queried, the type tree processor and value tree processor.
     * The data queried is injected into processors as data source, or cache.
     * Lastly, the tree processors help to set up expandable list.
     */
    private void initializeAssets() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(Edit_AssetsFragment.this.getContext());
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

    /**
     * The communicator that communicate the date from calendar dialog to the fragment.
     */
    private DateCommunicator fromActivityCommunicator = new DateCommunicator() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("Edit_AFragment","the user has selected date: " + currentTime);
            initializeAssets();
        }
    };

    /**
     * Specify the lower bound of the time period, start time.
     *
     * @return the altered date
     */
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

    /**
     * Specify the upper bound of the time period, end time.
     *
     * @return the altered date
     */
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
