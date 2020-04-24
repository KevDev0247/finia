package protect.FinanceLord.FragmentLiabilityUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;

public class LiabilitiesFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private DataProcessor_Liabilities dataProcessor;
    private List<DataCarrier_Liabilities> sectionDataSet;
    private int level;

    public LiabilitiesFragmentChildViewClickListener(DataProcessor_Liabilities dataProcessor, List<DataCarrier_Liabilities> sectionDataSet, int level){
        this.dataProcessor = dataProcessor;
        this.sectionDataSet = sectionDataSet;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        DataCarrier_Liabilities sectionItem = sectionDataSet.get(i);
        List<DataCarrier_Liabilities> childSection = dataProcessor.getGroupSet(sectionItem.liabilitiesTypeName, level + 1);

        Log.d("Edit_LiabilitiesFragment", "child clicked: " + childSection.get(i1).liabilitiesTypeName + ", id in DB" + childSection.get(i1).liabilitiesId);
        return false;
    }
}
