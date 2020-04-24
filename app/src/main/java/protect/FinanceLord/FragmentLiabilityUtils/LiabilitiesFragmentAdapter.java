package protect.FinanceLord.FragmentLiabilityUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;

public class LiabilitiesFragmentAdapter extends BaseExpandableListAdapter {

    private DataProcessor_Liabilities dataProcessor;
    private List<DataCarrier_Liabilities> sectionDataSet;
    private int level;
    private Context context;

    public LiabilitiesFragmentAdapter(DataProcessor_Liabilities dataProcessor, List<DataCarrier_Liabilities> sectionDataSet, int level, Context context){
        this.dataProcessor = dataProcessor;
        this.sectionDataSet = sectionDataSet;
        this.level = level;
        this.context = context;
    }

    public String getLiabilitiesName(int position){
        return sectionDataSet.get(position).liabilitiesTypeName;
    }

    @Override
    public int getGroupCount() {
        return sectionDataSet.size();
    }

    @Override
    public int getChildrenCount(int i1) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return sectionDataSet.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        String liabilitiesTypeName = getLiabilitiesName(i);
        List<DataCarrier_Liabilities> carriers = dataProcessor.getGroupSet(liabilitiesTypeName, level + 1);
        return carriers.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
