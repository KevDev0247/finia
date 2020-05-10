package protect.FinanceLord.NetWorthReportEditingUtils.FragmentsUtils;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Assets;
import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Assets;
import protect.FinanceLord.NetWorthDataTerminal.TypeProcessor_Assets;

public class AssetsFragmentChildViewClickListener implements ExpandableListView.OnChildClickListener {

    private DataProcessor_Assets dataProcessor;
    private TypeProcessor_Assets typeProcessor;
    private List<DataCarrier_Assets> sectionDataSet;
    private int level;

    public AssetsFragmentChildViewClickListener(List<DataCarrier_Assets> sectionDataSet, DataProcessor_Assets dataProcessor, TypeProcessor_Assets typeProcessor, int level) {
        this.sectionDataSet = sectionDataSet;
        this.dataProcessor = dataProcessor;
        this.typeProcessor = typeProcessor;
        this.level = level;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        DataCarrier_Assets sectionItem = sectionDataSet.get(i);
        List<DataCarrier_Assets> childSection = typeProcessor.getSubGroup(sectionItem.assetsTypeName, level + 1);

        Log.d("Edit_AFragment", "child Clicked: " + childSection.get(i1).assetsTypeName + ", id in DB: " + childSection.get(i1).assetsId);
        return true;
    }
}
