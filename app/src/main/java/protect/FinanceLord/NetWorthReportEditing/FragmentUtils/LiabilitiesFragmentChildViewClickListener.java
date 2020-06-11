package protect.FinanceLord.NetWorthReportEditing.FragmentUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.FinanceLord.NetWorthDataStructure.NodeContainer_Liabilities;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeProcessor_Liabilities;

/**
 * A Listener class that will detect whether a parent of the items in an expandable list has been clicked
 */
public class LiabilitiesFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private TypeTreeProcessor_Liabilities typeProcessor;
    private List<NodeContainer_Liabilities> sectionDataSet;
    private int level;

    public LiabilitiesFragmentChildViewClickListener(List<NodeContainer_Liabilities> sectionDataSet, TypeTreeProcessor_Liabilities typeProcessor, int level){
        this.sectionDataSet = sectionDataSet;
        this.typeProcessor = typeProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        NodeContainer_Liabilities sectionItem = sectionDataSet.get(i);
        List<NodeContainer_Liabilities> childSection = typeProcessor.getSubGroup(sectionItem.liabilitiesTypeName, level + 1);

        Log.d("Edit_LFragment", "child clicked: " + childSection.get(i1).liabilitiesTypeName + ", id in DB" + childSection.get(i1).liabilitiesId);
        return false;
    }
}
