package protect.FinanceLord.FragmentAssetUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;

public class AssetsFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private DataProcessor_Assets dataProcessor;
    private List<DataCarrier_Assets> sectionDataSet;
    private int level;

    public AssetsFragmentChildViewClickListener(List<DataCarrier_Assets> sectionDataSet, DataProcessor_Assets dataProcessor, int level) {
        this.sectionDataSet = sectionDataSet;
        this.dataProcessor = dataProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        DataCarrier_Assets sectionItem = sectionDataSet.get(i);
        List<DataCarrier_Assets> childSection = dataProcessor.getSubSet(sectionItem.assetsTypeName, level + 1);

        Log.d("Edit_AssetsFragment", "child Clicked: " + childSection.get(i1).assetsTypeName + ", id in DB: " + childSection.get(i1).assetsId);
        return true;
    }
}
