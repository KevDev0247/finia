package protect.FinanceLord.NetWorthEditReportsUtils;

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

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;
import protect.FinanceLord.FragmentAssetUtils.AssetsFragmentAdapter;
import protect.FinanceLord.FragmentAssetUtils.AssetsFragmentChildViewClickListener;
import protect.FinanceLord.FragmentAssetUtils.AssetsFragmentDataProcessor;

public class EditAssetsFragment extends Fragment {

    String title;
    private Button btnCommit;
    private AssetsFragmentDataProcessor dataProcessor;

    ExpandableListView expandableListView;

    public EditAssetsFragment(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsView = inflater.inflate(R.layout.fragment_assets, null);
        expandableListView = assetsView.findViewById(R.id.assets_list_view);
        this.btnCommit = assetsView.findViewById(R.id.btnCommit);
        this.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(EditAssetsFragment.this.getContext());
                        AssetsValueDao dao = database.assetsValueDao();
                        for(AssetsValue assetsValue: EditAssetsFragment.this.dataProcessor.getAllAssetsValues()) {
                            if(assetsValue.getAssetsPrimaryId() != 0) {
                                List<AssetsValue> assetsValues = dao.queryAsset(assetsValue.getAssetsPrimaryId());
                                Log.d("Assets Value Check", " Print assetsValues status " + assetsValues.isEmpty() + " assets value is " + assetsValue.getAssetsValue());
                                if(!assetsValues.isEmpty()) {
                                    dao.updateAssetValue(assetsValue);
                                } else {
                                    Log.w("EditAssetsFragment", "The assets not exists in the database? check if there is anything went wrong!!");
                                }
                            } else {
                                dao.insertAssetValue(assetsValue);
                            }
                        }

                        Date startDate = DateUtils.firstSecondOfThisMinute();
                        EditAssetsFragment.this.dataProcessor.setAllAssetsValues(dao.queryAssetsSinceDate(startDate.getTime()));

                        dataProcessor.calculateParentAssets(dao);

                        Log.d("EditAssetsFragment", "Assets committed!");
                    }
                });
            }
        });

        initAssetCategory();

        return assetsView;
    }

    private void initAssetCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(EditAssetsFragment.this.getContext());
                AssetsTypeDao assetsTypeDao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();
                List<AssetsTypeQuery> assetsTypeQueries = assetsTypeDao.queryGroupedAssetsType();

                Date startOfMinute = DateUtils.firstSecondOfThisMinute();
                Long milliseconds = startOfMinute.getTime();
                List<AssetsValue> assetsValues = assetsValueDao.queryAssetsSinceDate(milliseconds);
                Log.d("EditAssetsFragment", "Query all assets: " + assetsTypeQueries.toString());
                Log.d("EditAssetsFragment", "Query assets Values: " + assetsValues.toString());

                EditAssetsFragment.this.dataProcessor = new AssetsFragmentDataProcessor(assetsTypeQueries, assetsValues);
                final AssetsFragmentAdapter adapter = new AssetsFragmentAdapter(EditAssetsFragment.this.getContext(), dataProcessor, 1,"Total Assets");
                final AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(dataProcessor.getSubSet(null, 0), dataProcessor, 0);
                EditAssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        expandableListView.setAdapter(adapter);
                        expandableListView.setOnChildClickListener(listener);
                    }
                });
            }
        });
    }
}
