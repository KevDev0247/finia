package protect.Finia.networth.editing.utils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.Finia.datastructure.LiabilitiesNodeContainer;
import protect.Finia.datastructure.LiabilitiesTypeTreeProcessor;

/**
 * A Listener class that will detect whether a parent of the items in an expandable list has been clicked
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/04/13
 */
public class LiabilitiesFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private LiabilitiesTypeTreeProcessor typeProcessor;
    private List<LiabilitiesNodeContainer> sectionDataSet;
    private int level;

    public LiabilitiesFragmentChildViewClickListener(List<LiabilitiesNodeContainer> sectionDataSet, LiabilitiesTypeTreeProcessor typeProcessor, int level){
        this.sectionDataSet = sectionDataSet;
        this.typeProcessor = typeProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        LiabilitiesNodeContainer sectionItem = sectionDataSet.get(i);
        List<LiabilitiesNodeContainer> childSection = typeProcessor.getSubGroup(sectionItem.liabilitiesTypeName, level + 1);

        Log.d("Edit_LFragment", "child clicked: " + childSection.get(i1).liabilitiesTypeName + ", id in DB" + childSection.get(i1).liabilitiesId);
        return false;
    }
}
