package protect.FinanceLord.ui.NetWorthEditReports;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;
import protect.FinanceLord.AssetsFragmentUtils.AssetsFragmentAdapter;
import protect.FinanceLord.AssetsFragmentUtils.AssetsFragmentChildViewClickListener;
import protect.FinanceLord.AssetsFragmentUtils.AssetsFragmentDataProcessor;

public class AssetsFragment extends Fragment {
    String title;
    private Button btnCommit;
    private AssetsFragmentDataProcessor dataProcessor;

    public AssetsFragment(String title) {
        this.title = title;
    }

    ExpandableListView expandableListView;

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
                        FinanceLordDatabase database = FinanceLordDatabase.getInstance(AssetsFragment.this.getContext());
                        AssetsValueDao dao = database.assetsValueDao();
                        for(AssetsValue assetsValue: AssetsFragment.this.dataProcessor.getAllAssetsValues()) {
                            List<AssetsValue> assetsValues = dao.queryAsset(assetsValue.getAssetsId(), assetsValue.getDate());
                            if(assetsValues.isEmpty()) {
                                dao.insertAssetValue(assetsValue);

                                // 注意，这里有点小细节，我们在插入新数据的时候，数据库会给我们生成一个id （primarykey),这个是在插入数据后才会生成的，所以我们这时候缓存里
                                // 是不知道这个id的，这样有可能我们下次再次点击提交按钮的时候，由于没有primary key，所以更新会失败，变成插入新数据了，这里我们需要重新query
                                // 一下，刷新缓存，其实我们应该也可以用liveData，不过我没用过，你如果有兴趣可以试试看
                                Date startDate = DateUtils.firstDayOfThisMonth();
                                AssetsFragment.this.dataProcessor.setAssetsValues(dao.queryAssetsSinceDate(startDate.getTime()));
                            } else {
                                dao.updateAssetValue(assetsValue);
                            }
                        }
                        Log.d("AssetsFragment", "Assets committed!");
                    }
                });
            }
        });

        initAssetCategory();

        return assetsView;
    }

    private void updateOrInsertAssetsValues(List<AssetsValue> assetsValues) {

    }
    private void initAssetCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(AssetsFragment.this.getContext());
                AssetsTypeDao dao = database.assetsTypeDao();
                AssetsValueDao assetsValueDao = database.assetsValueDao();
                List<AssetsTypeQuery> assetsTypeQueries = dao.queryGroupedAssetsType();

                Date starOfMonth = DateUtils.firstDayOfThisMonth();
                Long milliseconds = starOfMonth.getTime();
                List<AssetsValue> assetsValues = assetsValueDao.queryAssetsSinceDate(milliseconds);
                Log.d("AssetsFragment", "Query all assets: " + assetsTypeQueries.toString());
                Log.d("AssetsFragment", "Query assets Values: " + assetsValues.toString());

                AssetsFragment.this.dataProcessor = new AssetsFragmentDataProcessor(assetsTypeQueries, assetsValues);
                final AssetsFragmentAdapter adapter = new AssetsFragmentAdapter(AssetsFragment.this.getContext(), dataProcessor, 1,"Total Assets");
                final AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(dataProcessor.getSubSet(null, 0), dataProcessor, 0);
                AssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
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
