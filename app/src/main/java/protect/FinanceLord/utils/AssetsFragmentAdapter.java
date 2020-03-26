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
        return getSectionGroupCount();
    }

    @Override
    public int getChildrenCount(int i) {
        String assetsTypeName = getAssetsName(i);
        List<AssetsFragmentDataCarrier> carriers = dataProcessor.getSubSet(assetsTypeName, level + 1);
        return carriers.size();
    }

    @Override
    public Object getGroup(int i) {
        return sectionDataSet.get(i);
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.assets_list_row_first, null);
        TextView textView = (TextView) convertView.findViewById(R.id.rowParentText);
        textView.setText(this.sectionDataSet.get(position).assetsTypeName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final AssetsFragmentDataCarrier sectionData = sectionDataSet.get(groupPosition);
        List<AssetsFragmentDataCarrier> children = dataProcessor.getSubSet(sectionData.assetsTypeName, level + 1);
        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assets_list_row_first, null);
            TextView textView = (TextView) convertView.findViewById(R.id.rowParentText);
            textView.setText(this.sectionDataSet.get(childPosition).assetsTypeName);

            return convertView;
        } else {
            final AssetsSecondLevelExpandableListView secondLevelExpandableListView = new AssetsSecondLevelExpandableListView(context);
            AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(sectionDataSet, dataProcessor, level + 1);
            secondLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, dataProcessor, level + 1, sectionData.assetsTypeName));
            secondLevelExpandableListView.setOnChildClickListener(listener);
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
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
