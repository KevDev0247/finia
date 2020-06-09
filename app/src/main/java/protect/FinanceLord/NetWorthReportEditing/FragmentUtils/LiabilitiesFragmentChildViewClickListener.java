package protect.FinanceLord.NetWorthReportEditing.FragmentUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Liabilities;

public class LiabilitiesFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private TypeProcessor_Liabilities typeProcessor;
    private List<DataCarrier_Liabilities> sectionDataSet;
    private int level;

    public LiabilitiesFragmentChildViewClickListener(List<DataCarrier_Liabilities> sectionDataSet, TypeProcessor_Liabilities typeProcessor, int level){
        this.sectionDataSet = sectionDataSet;
        this.typeProcessor = typeProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        DataCarrier_Liabilities sectionItem = sectionDataSet.get(i);
        List<DataCarrier_Liabilities> childSection = typeProcessor.getSubGroup(sectionItem.liabilitiesTypeName, level + 1);

        Log.d("Edit_LFragment", "child clicked: " + childSection.get(i1).liabilitiesTypeName + ", id in DB" + childSection.get(i1).liabilitiesId);
        return false;
    }
}
