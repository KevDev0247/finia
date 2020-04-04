package protect.FinanceLord.AssetsFragmentUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import protect.FinanceLord.R;
import protect.FinanceLord.ui.NetWorthEditReports.AssetsSecondLevelExpandableListView;

class AssetsFragmentSpecialAdapter extends BaseExpandableListAdapter {

    private Context context;
    private AssetsFragmentDataProcessor dataProcessor;
    private List<AssetsFragmentDataCarrier> sectionDataSet;
    private int level;

    AssetsFragmentSpecialAdapter (Context context, AssetsFragmentDataProcessor dataProcessor, int level, String parentSection){
        this.context =context;
        this.dataProcessor = dataProcessor;
        this.level = level;
        this.sectionDataSet = dataProcessor.getSubSet(parentSection, level);
    }

    public int getSectionGroupCount() {
        return sectionDataSet.size();
    }

    @Override
    public int getGroupCount() {
        return getSectionGroupCount();
    }

    public String getAssetsName(int position) {
        return this.sectionDataSet.get(position).assetsTypeName;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
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
        return true;
    }

    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.assets_list_row_second, null);
        convertView = inflater.inflate(R.layout.assets_list_row_third, null);
        TextView textView = convertView.findViewById(R.id.rowThirdText);
        textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        final AssetsFragmentDataCarrier sectionData = sectionDataSet.get(groupPosition);
        List<AssetsFragmentDataCarrier> children = dataProcessor.getSubSet(sectionData.assetsTypeName, 3);
        final AssetsSecondLevelExpandableListView secondLevelExpandableListView = new AssetsSecondLevelExpandableListView(context);
        AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(sectionDataSet, dataProcessor, 3);
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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
