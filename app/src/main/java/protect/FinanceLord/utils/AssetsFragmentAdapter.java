package protect.FinanceLord.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import protect.FinanceLord.R;
import protect.FinanceLord.ui.NetWorthEditReports.AssetsSecondLevelExpandableListView;

public class AssetsFragmentAdapter extends BaseExpandableListAdapter {

    private AssetsFragmentDataProcessor dataProcessor;
    private List<AssetsFragmentDataCarrier> sectionDataSet;
    private int level;
    private Context context;

    public AssetsFragmentAdapter(Context context, AssetsFragmentDataProcessor dataProcessor, int level, String parentSection) {
        this.context = context;
        this.dataProcessor = dataProcessor;
        this.level = level;
        this.sectionDataSet = dataProcessor.getSubSet(parentSection, level);
    }

    public int getSectionGroupCount() {
        return sectionDataSet.size();
    }

    public String getAssetsName(int position) {
        return this.sectionDataSet.get(position).assetsTypeName;
    }

    public int getAssetsId(int position) {
        return this.sectionDataSet.get(position).assetsId;
    }

    @Override
    public int getGroupCount() {
        Log.d("AssetsFragmentAdapter", "getGroupCount: " + getSectionGroupCount() + ", Level: " + level);
        return getSectionGroupCount();
    }

    @Override
    public int getChildrenCount(int i) {
        String assetsName = getAssetsName(i);
        List<AssetsFragmentDataCarrier> childList = dataProcessor.getSubSet(assetsName, level + 1);

        Log.d("AssetsFragmentAdapter", "Child count at " + i + ": " + childList.size() + ", Level: " + level);
        return childList.size();
    }

    @Override
    public Object getGroup(int i) {
        return i;
//        return sectionDataSet.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String assetsTypeName = getAssetsName(i);
        List<AssetsFragmentDataCarrier> carriers = dataProcessor.getSubSet(assetsTypeName, level + 1);
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
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
        Log.d("AssetsFragmentAdapter", "get group at : " + position + ", Level: " + level);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int childrenCount = getChildrenCount(position);
//        if (childrenCount == 0) {
//            convertView = inflater.inflate(R.layout.assets_list_row_item, null);
//            TextView textView = convertView.findViewById(R.id.rowItemText);
//            textView.setText(this.getAssetsName(position));
//        } else {
//            if (level == 0){
                convertView = inflater.inflate(R.layout.assets_list_row_first, null);
                TextView textView = convertView.findViewById(R.id.rowParentText);
                textView.setText(this.sectionDataSet.get(position).assetsTypeName);
//            } else if (level == 1){
//                convertView = inflater.inflate(R.layout.assets_list_row_second, null);
//                TextView textView = convertView.findViewById(R.id.rowSecondText);
//                textView.setText(this.sectionDataSet.get(position).assetsTypeName);
//            } else if (level == 2){
//                convertView = inflater.inflate(R.layout.assets_list_row_third, null);
//                TextView textView = convertView.findViewById(R.id.rowThirdText);
//                textView.setText(this.sectionDataSet.get(position).assetsTypeName);
//            } else {
//                convertView = inflater.inflate(R.layout.assets_list_row_second, null);
//                TextView textView = convertView.findViewById(R.id.rowSecondText);
//                textView.setText(this.sectionDataSet.get(position).assetsTypeName);
//            }
//        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("AssetsFragmentAdapter", "Get Child at group: " + groupPosition + "child position: " + childPosition + ", Level: " + level);
        final AssetsFragmentDataCarrier sectionData = sectionDataSet.get(groupPosition);
        List<AssetsFragmentDataCarrier> children = dataProcessor.getSubSet(sectionData.assetsTypeName, level + 1);
        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.assets_list_row_second, null);
//            TextView textView = (TextView) convertView.findViewById(R.id.rowSecondText);
//            textView.setText(this.sectionDataSet.get(childPosition).assetsTypeName);
//            return convertView;

            convertView = inflater.inflate(R.layout.assets_list_row_third, null);
            TextView textView = convertView.findViewById(R.id.rowThirdText);
            textView.setText(this.sectionDataSet.get(childPosition).assetsTypeName);
            return convertView;
        } else {
            final AssetsSecondLevelExpandableListView secondLevelExpandableListView = new AssetsSecondLevelExpandableListView(context);
            AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(sectionDataSet, dataProcessor, level + 1);
            secondLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, dataProcessor, level + 1, sectionData.assetsTypeName));
            secondLevelExpandableListView.setOnChildClickListener(listener);
            secondLevelExpandableListView.setGroupIndicator(null);
//            secondLevelExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//                int previousGroup = -1;
//                @Override
//                public void onGroupExpand(int groupPosition) {
//
//                    if (groupPosition != previousGroup){
//                        secondLevelExpandableListView.collapseGroup(previousGroup);
//                    }
//                    previousGroup = groupPosition;
//                }
//            });
            return secondLevelExpandableListView;
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
