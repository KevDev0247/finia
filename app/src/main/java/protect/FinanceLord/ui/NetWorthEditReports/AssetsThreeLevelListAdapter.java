package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import protect.FinanceLord.R;

class AssetsThreeLevelListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<String> parents;
    List<String[]> secondLevelItems;
    List<LinkedHashMap<String, List<String>>> assetsList;

        public AssetsThreeLevelListAdapter(Context context, List<String> parents, List<String[]> secondLevelItems, List<LinkedHashMap<String, List<String>>> assetsList){
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
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {
        return child;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.assets_list_row_first, null);
        TextView textView = (TextView) convertView.findViewById(R.id.rowParentText);
        textView.setText(this.parents.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final AssetsSecondLevelExpandableListView secondLevelExpandableListView = new AssetsSecondLevelExpandableListView(context);

        String[] headers = secondLevelItems.get(groupPosition);

        List<List<String>> childData = new ArrayList<>();
        LinkedHashMap<String, List<String>> secondLevelData = assetsList.get(groupPosition);

        for (String key : secondLevelData.keySet()){
            childData.add(secondLevelData.get(key));
        }

        secondLevelExpandableListView.setAdapter(new AssetsSecondLevelAdapter(context, childData, headers));
        secondLevelExpandableListView.setGroupIndicator(null);
        secondLevelExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup){
                    secondLevelExpandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });

        return secondLevelExpandableListView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
