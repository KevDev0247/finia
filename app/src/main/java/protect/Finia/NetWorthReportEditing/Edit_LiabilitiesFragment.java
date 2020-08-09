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
import protect.Finia.DAOs.LiabilitiesTypeDao;
import protect.Finia.DAOs.LiabilitiesValueDao;
import protect.Finia.Database.FiniaDatabase;
import protect.Finia.Database.LiabilitiesValue;
import protect.Finia.NetWorthDataStructure.TypeTreeLeaf_Liabilities;
import protect.Finia.NetWorthDataStructure.TypeTreeProcessor_Liabilities;
import protect.Finia.NetWorthDataStructure.ValueTreeProcessor_Liabilities;
import protect.Finia.NetWorthReportEditing.FragmentUtils.LiabilitiesFragmentAdapter;
import protect.Finia.NetWorthReportEditing.FragmentUtils.LiabilitiesFragmentChildViewClickListener;
import protect.Finia.Activities.NetWorthReportEditingActivity;
import protect.Finia.R;

/**
 * The class for the fragment to edit the liabilities sheet.
 * The class includes the expandable list, input fields, and commit button.
 * The custom expandable list in the fragment is a part of UX design designed to improve user experiences to avoid
 * the scenario when user cannot find the right category and input fields.
 * They group items into appropriate parent categories so that the input sheet can be more clean and organized
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class Edit_LiabilitiesFragment extends Fragment {

    private Date currentTime;
    private ExpandableListView expandableListView;

    private LiabilitiesFragmentAdapter adapter;
    private ValueTreeProcessor_Liabilities valueTreeProcessor;
    private TypeTreeProcessor_Liabilities typeTreeProcessor;

    public Edit_LiabilitiesFragment(Date currentTime) {
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
            activity.toEditLiabilitiesFragmentCommunicator = fromActivityCommunicator;
        }
    }

    /**
     * Create the view of the fragment.
     * First, the method will first set the view of the content by finding the corresponding layout file through id.
     * Then, the method will first set the view of the the expandable list view by finding the corresponding layout file through id.
     * Next, the liabilities types and values are retrieved from database and set up the expandable list view.
     * Lastly, the method will set up the tree processor and commit and delete logic of the buttons.
     * The commit logic include how the data was retrieved from the input fields and transferred to the type and value tree processor to prepare for the insertion of the data.
     * The delete logic include the action to delete the report of assets at a time that the user picked.
     * Note that delete action only applies to the whole report. If the user merely want to change a number, then it is better to just use the edit mode.
     *
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     * @return the view of the liabilities fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View liabilitiesFragmentView = inflater.inflate(R.layout.fragment_edit_liabilities, null);
        expandableListView = liabilitiesFragmentView.findViewById(R.id.liabilities_list_view);
        RelativeLayout commitButton = liabilitiesFragmentView.findViewById(R.id.liabilities_commit_button);
        RelativeLayout deleteButton = liabilitiesFragmentView.findViewById(R.id.liabilities_delete_button);

        initializeLiabilities();

        setUpCommitButton(commitButton, deleteButton);

        return liabilitiesFragmentView;
    }

    /**
     * Set up the commit button.
     * First, the data source in the processor is retrieved and stored into a list.
     * Then the list of data is traversed and inserted or updated into the database.
     * Next, the data is displayed onto the expandable list.
     * Lastly, the data in the processor is deleted.
     * The data source act as a cache for the expandable list.
     * After the data is inserted, the data in the data source is cleared.
     * For the delete logic, the whole report will be deleted.
     *
     * @param commitButton the instance of commit button.
     */
    private void setUpCommitButton(RelativeLayout commitButton, RelativeLayout deleteButton) {
        FiniaDatabase database = FiniaDatabase.getInstance(Edit_LiabilitiesFragment.this.getContext());
        final LiabilitiesValueDao liabilitiesValueDao = database.liabilitiesValueDao();

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        liabilitiesValueDao.deleteLiabilitiesAtDate(currentTime.getTime());
                        getActivity().finish();
                    }
                });
            }
        });
    }

    /**
     * Initialize liabilities values, types, and tree processor set up.
     * First, the liabilities values of the date is queried from the database.
     * Then, with the liabilities values and types queried, the type tree processor and value tree processor.
     * The data queried is injected into processors as data source, or cache.
     * Lastly, the tree processors help to set up expandable list.
     */
    private void initializeLiabilities() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FiniaDatabase database = FiniaDatabase.getInstance(Edit_LiabilitiesFragment.this.getContext());
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

    /**
     * The communicator that communicate the date from calendar dialog to the fragment.
     */
    private DateCommunicator fromActivityCommunicator = new DateCommunicator() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("Edit_LFragment","the user has selected date: " + currentTime);
            initializeLiabilities();
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
     * @author Owner  Kevin Zhijun Wang
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
