package protect.FinanceLord.ui.NetWorthEditReports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

    List<String> secondLevelItems = new ArrayList<String>();

    List<LinkedHashMap<String, List<String>>> assetsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsView = inflater.inflate(R.layout.fragment_assets, null);
        expandableListView = assetsView.findViewById(R.id.assets_list_view);

        initAssetCategory();

        return assetsView;
    }

    private void initAssetCategory() {
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

        List<String> investedAssetsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.invested_assets);
        for (String item: array){
            investedAssetsList.add(item);
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

        secondLevelItems.add(investedAssetsList.get(0));
        secondLevelItems.add(investedAssetsList.get(1));
        secondLevelItems.add(investedAssetsList.get(2));

        assetsThirdLevelCategory1.put(parents.get(1), liquidAssetsList);
        assetsThirdLevelCategory2.put(investedAssetsList.get(0), taxableAccountsList);
        assetsThirdLevelCategory2.put(investedAssetsList.get(1), retirementAccountsList);
        assetsThirdLevelCategory2.put(investedAssetsList.get(2), ownershipInterestsList);
        assetsThirdLevelCategory3.put(parents.get(3), personalAssetsList);

        assetsList.add(assetsThirdLevelCategory1);
        assetsList.add(assetsThirdLevelCategory2);
        assetsList.add(assetsThirdLevelCategory3);

        ThreeLevelListAdapter threeLevelListAdapter = new ThreeLevelListAdapter(getContext(), parents, secondLevelItems, assetsList);
        expandableListView.setAdapter(threeLevelListAdapter);

        threeLevelListAdapter.notifyDataSetChanged();
    }
}
