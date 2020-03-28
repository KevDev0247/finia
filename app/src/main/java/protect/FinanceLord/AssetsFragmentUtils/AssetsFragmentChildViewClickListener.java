package protect.FinanceLord.AssetsFragmentUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

public class AssetsFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {
    private AssetsFragmentDataProcessor dataProcessor;
    private List<AssetsFragmentDataCarrier> sectionDataSet;
    private int level;

    public AssetsFragmentChildViewClickListener(List<AssetsFragmentDataCarrier> sectionDataSet, AssetsFragmentDataProcessor dataProcessor, int level) {
        this.sectionDataSet = sectionDataSet;
        this.dataProcessor = dataProcessor;
        this.level = level;
    }
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        AssetsFragmentDataCarrier sectionItem = sectionDataSet.get(i);
        List<AssetsFragmentDataCarrier>childSection = dataProcessor.getSubSet(sectionItem.assetsTypeName, level + 1);

        Log.d("AssetsFragment", "child Clicked: " + childSection.get(i1).assetsTypeName + ", id in DB: " + childSection.get(i1).assetsId);
        return true;
    }
}
