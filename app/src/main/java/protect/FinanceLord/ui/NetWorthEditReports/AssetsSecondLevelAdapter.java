package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import protect.FinanceLord.R;

class AssetsSecondLevelAdapter extends BaseExpandableListAdapter {
    private Context context;

    List<List<String>> AssetsList;
    String[] headers;

    public AssetsSecondLevelAdapter(Context context, List<List<String>> AssetsList, String[] headers){
        this.context = context;
        this.AssetsList = AssetsList;
        this.headers = headers;
    }

    @Override
    public int getGroupCount() {
        return headers.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String[] children = AssetsList.get(groupPosition).toArray(new String[0]);
        return children.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String[] childData;
        childData = AssetsList.get(groupPosition).toArray(new String[0]);
        return childData[childPosition];
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.assets_list_row_second, null);
        TextView textView = convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        textView.setText(groupText);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.assets_list_row_third, null);
        TextView textView = convertView.findViewById(R.id.rowThirdText);
        String[] childArray = AssetsList.get(groupPosition).toArray(new String[0]);
        String text = childArray[childPosition];
        textView.setText(text);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int i1) {
        return true;
    }
}
