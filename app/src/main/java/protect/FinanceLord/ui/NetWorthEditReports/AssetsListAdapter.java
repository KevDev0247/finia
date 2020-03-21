package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import protect.FinanceLord.R;

class AssetsListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> assetsCategory;
    HashMap<String,List<String>> assetsList;

    public AssetsListAdapter (Context context, List<String> assetsCategory, HashMap<String,List<String>> assetslist){
        this.context = context;
        this.assetsCategory = assetsCategory;
        this.assetsList = assetslist;
    }

    @Override
    public int getGroupCount() {
        return assetsCategory.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.assetsList.get(this.assetsCategory.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.assetsCategory.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.assetsList.get(this.assetsCategory.get(groupPosition));
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
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExtended, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.asset_category, null);
        }

        TextView textView = convertView.findViewById(R.id.assets_list_parent);
        textView.setText(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.asset_item, null);
        }

        TextView textView = convertView.findViewById(R.id.assets_child);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int i1) {
        return true;
    }
}
