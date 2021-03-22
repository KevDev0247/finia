package protect.Finia.networth.editing.utils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.Finia.datastructure.AssetsNodeContainer;
import protect.Finia.datastructure.AssetsTypeTreeProcessor;

/**
 * A Listener class that will detect whether a parent of the items in an expandable list has been clicked
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/03/25
 */
public class AssetsFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private AssetsTypeTreeProcessor typeProcessor;
    private List<AssetsNodeContainer> sectionDataSet;
    private int level;

    public AssetsFragmentChildViewClickListener(List<AssetsNodeContainer> sectionDataSet, AssetsTypeTreeProcessor typeProcessor, int level) {
        this.sectionDataSet = sectionDataSet;
        this.typeProcessor = typeProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        AssetsNodeContainer sectionItem = sectionDataSet.get(i);
        List<AssetsNodeContainer> childSection = typeProcessor.getSubGroup(sectionItem.assetsTypeName, level + 1);

        Log.d("Edit_AFragment", "child Clicked: " + childSection.get(i1).assetsTypeName + ", id in DB: " + childSection.get(i1).assetsTypeId);
        return true;
    }
}
