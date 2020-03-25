package protect.FinanceLord.ui.NetWorthEditReports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;

public class AssetsFragment extends Fragment {
    String title;
    public AssetsFragment(String title) {
        this.title = title;
    }

    ExpandableListView expandableListView;

    LinkedHashMap<String, List<String>> assetsThirdLevelCategory1 = new LinkedHashMap<>();
    LinkedHashMap<String, List<String>> assetsThirdLevelCategory2 = new LinkedHashMap<>();
    LinkedHashMap<String, List<String>> assetsThirdLevelCategory3 = new LinkedHashMap<>();

    List<String[]> secondLevelItems = new ArrayList<>();

    List<LinkedHashMap<String, List<String>>> assetsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsView = inflater.inflate(R.layout.fragment_assets, null);
        expandableListView = assetsView.findViewById(R.id.assets_list_view);

        initAssetCategory();

        return assetsView;
    }

    private void initAssetCategory() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                FinanceLordDatabase database = FinanceLordDatabase.getInstance(AssetsFragment.this.getContext());
                AssetsTypeDao dao = database.assetsTypeDao();
                List<AssetsTypeQuery> assetsTypeQueries = dao.queryGroupedAssetsType();
                Log.d("AssetsFragment", "Query all assets: " + assetsTypeQueries.toString());
            }
        });


        String[] array;

        List<String> parents = new ArrayList<>();
        array = getResources().getStringArray(R.array.parents);
        for (String item: array){
            parents.add(item);
        }

        List<String> liquidAssetsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.liquid_assets);
        for (String item: array){
            liquidAssetsList.add(item);
        }

        List<String> taxableAccountsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.taxable_accounts);
        for (String item: array){
            taxableAccountsList.add(item);
        }

        List<String> retirementAccountsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.retirement_accounts);
        for (String item: array){
            retirementAccountsList.add(item);
        }

        List<String> ownershipInterestsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.ownership_interests);
        for (String item: array){
            ownershipInterestsList.add(item);
        }

        List<String> personalAssetsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.personal_assets);
        for (String item: array){
            personalAssetsList.add(item);
        }


        String[] investedAssetsList = new String[3];
        array = getResources().getStringArray(R.array.invested_assets);
        for (int i = 0; i < array.length; i++){
            investedAssetsList[i] = array[i];
        }

        String[] liquidAssets = new String[]{"All Assets"};
        String[] personalAssets = new String[]{"All Assets"};

        secondLevelItems.add(liquidAssets);
        secondLevelItems.add(investedAssetsList);
        secondLevelItems.add(personalAssets);

        assetsThirdLevelCategory1.put(getString(R.string.liquid_assets), liquidAssetsList);
        assetsThirdLevelCategory2.put(investedAssetsList[0], taxableAccountsList);
        assetsThirdLevelCategory2.put(investedAssetsList[1], retirementAccountsList);
        assetsThirdLevelCategory2.put(investedAssetsList[2], ownershipInterestsList);
        assetsThirdLevelCategory3.put(getString(R.string.personal_assets), personalAssetsList);

        assetsList.add(assetsThirdLevelCategory1);
        assetsList.add(assetsThirdLevelCategory2);
        assetsList.add(assetsThirdLevelCategory3);

        AssetsThreeLevelListAdapter threeLevelListAdapter = new AssetsThreeLevelListAdapter(getContext(), parents, secondLevelItems, assetsList);
        expandableListView.setAdapter(threeLevelListAdapter);

        threeLevelListAdapter.notifyDataSetChanged();
    }
}
