package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import protect.FinanceLord.R;

public class AssetsFragment extends Fragment {
    String title;
    public AssetsFragment(String title) {
        this.title = title;
    }

    ExpandableListView expandableListView;
    List<String> assetsCategory;
    HashMap<String, List<String>> assetsList;
    AssetsListAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsView = inflater.inflate(R.layout.fragment_assets, null);

        expandableListView = assetsView.findViewById(R.id.assets_list_view);
        assetsCategory = new ArrayList<>();
        assetsList = new HashMap<>();
        adapter = new AssetsListAdapter(getContext(), assetsCategory, assetsList);
        expandableListView.setAdapter(adapter);
        initAssetCategory();

        return assetsView;
    }

    private void initAssetCategory() {
        assetsCategory.add(getString(R.string.liquid_assets));
        assetsCategory.add(getString(R.string.invested_assets));
        assetsCategory.add(getString(R.string.personal_assets));

        String[] array;

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

        assetsList.put(assetsCategory.get(0),liquidAssetsList);
        assetsList.put(assetsCategory.get(1),investedAssetsList);
        assetsList.put(assetsCategory.get(2),personalAssetsList);
        adapter.notifyDataSetChanged();
    }
}
