package protect.FinanceLord.FragmentLiabilityUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

public class LiabilitiesFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private LiabilitiesFragmentDataProcessor dataProcessor;
    private List<LiabilitiesFragmentDataCarrier> sectionDataSet;
    private int level;

    public LiabilitiesFragmentChildViewClickListener(LiabilitiesFragmentDataProcessor dataProcessor, List<LiabilitiesFragmentDataCarrier> sectionDataSet, int level){
        this.dataProcessor = dataProcessor;
        this.sectionDataSet = sectionDataSet;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        LiabilitiesFragmentDataCarrier sectionItem = sectionDataSet.get(i);
        List<LiabilitiesFragmentDataCarrier> childSection = dataProcessor.getGroupSet(sectionItem.liabilitiesTypeName, level + 1);

        Log.d("EditLiabilitiesFragment", "child clicked: " + childSection.get(i1).liabilitiesTypeName + ", id in DB" + childSection.get(i1).liabilitiesId);
        return false;
    }
}
