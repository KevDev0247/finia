package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.LinkedHashMap;
import java.util.List;

class ThreeLevelListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<String> parents;
    List<String> secondLevelItems;
    List<LinkedHashMap<String, List<String>>> assetsList;

    public ThreeLevelListAdapter(Context context, List<String> parents, List<String> secondLevelItems, List<LinkedHashMap<String, List<String>>> assetsList){
        this.context = context;
        this.parents = parents;
        this.secondLevelItems = secondLevelItems;
        this.assetsList = assetsList;
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int group, int child) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
