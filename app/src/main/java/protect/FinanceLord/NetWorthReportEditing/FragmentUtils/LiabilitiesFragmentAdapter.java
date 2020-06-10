package protect.FinanceLord.NetWorthReportEditing.FragmentUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import protect.FinanceLord.Database.LiabilitiesValue;
import protect.FinanceLord.NetWorthDataStructure.ValueTreeProcessor_Liabilities;
import protect.FinanceLord.NetWorthDataStructure.NodeContainer_Liabilities;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeProcessor_Liabilities;
import protect.FinanceLord.NetWorthReportEditing.NetWorthExpandableListView;
import protect.FinanceLord.R;

public class LiabilitiesFragmentAdapter extends BaseExpandableListAdapter {

    private ValueTreeProcessor_Liabilities dataProcessor;
    private TypeTreeProcessor_Liabilities typeProcessor;
    private List<NodeContainer_Liabilities> sectionDataSet;
    private int level;
    private Context context;

    public LiabilitiesFragmentAdapter(Context context, ValueTreeProcessor_Liabilities dataProcessor, TypeTreeProcessor_Liabilities typeProcessor, int level, String parentSection) {
        this.context = context;
        this.dataProcessor = dataProcessor;
        this.typeProcessor = typeProcessor;
        this.level = level;
        this.sectionDataSet = typeProcessor.getSubGroup(parentSection, level);
    }

    public String getLiabilitiesName(int position) {
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
        List<NodeContainer_Liabilities> carriers = typeProcessor.getSubGroup(liabilitiesTypeName, level + 1);
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
        if (level == 1) {
            convertView = inflater.inflate(R.layout.liabilities_list_row_first,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowFirstText);
            textView.setText(sectionDataSet.get(position).liabilitiesTypeName);

        } else if (level == 2) {
            convertView = inflater.inflate(R.layout.liabilities_list_item,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowThirdText);
            textView.setText(sectionDataSet.get(position).liabilitiesTypeName);

            NodeContainer_Liabilities dataCarrier = this.sectionDataSet.get(position);
            EditText editText = convertView.findViewById(R.id.liabilitiesValueInput);

            LiabilitiesValue liabilitiesValue = dataProcessor.getLiabilityValue(dataCarrier.liabilitiesId);
            if (liabilitiesValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(liabilitiesValue.getLiabilitiesValue());
                editText.setText(strValue);
            }

            this.addTextListener(editText, dataCarrier);
        }
        return convertView;
    }

    void addTextListener(EditText editText, final NodeContainer_Liabilities dataCarrier){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)) {
                    String numberStr = text.replace(",","");
                    final float liabilityValue = Float.parseFloat(numberStr);

                    dataProcessor.setLiabilityValue(dataCarrier.liabilitiesId, liabilityValue);
                    Log.d("LFragmentAdapter", "value changed: " + text + ", float value: " + liabilityValue);

                } else {
                    String numberStr = "0.00";
                    final float liabilityValue = Float.parseFloat(numberStr);

                    dataProcessor.setLiabilityValue(dataCarrier.liabilitiesId, liabilityValue);
                    Log.d("LFragmentAdapter", "value empty, set to 0 " + ", float value: " + liabilityValue);
                }
            }
        });
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final NodeContainer_Liabilities sectionData = sectionDataSet.get(groupPosition);
        List<NodeContainer_Liabilities> children = typeProcessor.getSubGroup(sectionData.liabilitiesTypeName, level + 1);

        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.liabilities_list_row_first, null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowFirstText);
            textView.setText(this.sectionDataSet.get(childPosition).liabilitiesTypeName);
            return convertView;

        } else {
            final NetWorthExpandableListView nextLevelExpandableListView = new NetWorthExpandableListView(context);
            LiabilitiesFragmentChildViewClickListener listener = new LiabilitiesFragmentChildViewClickListener(sectionDataSet, typeProcessor,level + 1);
            nextLevelExpandableListView.setAdapter(new LiabilitiesFragmentAdapter(context, dataProcessor, typeProcessor,level + 1, sectionData.liabilitiesTypeName));
            nextLevelExpandableListView.setOnChildClickListener(listener);
            nextLevelExpandableListView.setDivider(null);

            if (level == 1){
                nextLevelExpandableListView.setGroupIndicator(null);
                nextLevelExpandableListView.setDividerHeight(10);
            }
            nextLevelExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousGroup){
                        nextLevelExpandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                }
            });

            return nextLevelExpandableListView;
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
