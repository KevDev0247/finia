package protect.FinanceLord.ui.main;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View assetsView = inflater.inflate(R.layout.fragment_assets, null);

        expandableListView = assetsView.findViewById(R.id.assets_list_view);
        assetsCategory = new ArrayList<>();
        assetsList = new HashMap<>();
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

        List<String> personalAssetsList = new ArrayList<>();
        array = getResources().getStringArray(R.array.personal_assets);
        for (String item: array){
            personalAssetsList.add(item);
        }
    }
}
