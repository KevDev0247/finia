package protect.FinanceLord.NetWorthEditReportsUtils.FragmentsUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import protect.FinanceLord.NetWorthDataTerminal.DataProcessor_Liabilities;
import protect.FinanceLord.NetWorthDataTerminal.DataCarrier_Liabilities;
import protect.FinanceLord.R;

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
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (level == 1){
            convertView = inflater.inflate(R.layout.liabilities_list_row_second,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowSecondText);
            textView.setText(sectionDataSet.get(position).liabilitiesTypeName);
        } else if (level == 2){
            convertView = inflater.inflate(R.layout.liabilities_list_row_item,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowItemText);
            textView.setText(sectionDataSet.get(position).liabilitiesTypeName);

            DataCarrier_Liabilities dataCarrier = this.sectionDataSet.get(position);
            EditText editText = convertView.findViewById(R.id.liabilitiesValueInput);
        }

        return convertView;
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
