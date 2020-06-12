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

/**
 * The adapter for the Edit_LiabilitiesFragment.
 * The adapter will carry the data from a source and deliver it to the UI widgets.
 * The source refers to the data source in the value tree processor.
 * Note that the carrier of the data in this adapter is the node carrier, which store the
 * information about each node in the value tree and record the level of the node.
 * This adapter will process the data in different level and deliver the group of data
 * to the right parent and under the right level.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class LiabilitiesFragmentAdapter extends BaseExpandableListAdapter {

    private ValueTreeProcessor_Liabilities valueTreeProcessor;
    private TypeTreeProcessor_Liabilities typeTreeProcessor;
    private List<NodeContainer_Liabilities> currentLevelNodeContainers;
    private Context context;
    private int level;

    public LiabilitiesFragmentAdapter(Context context, ValueTreeProcessor_Liabilities valueTreeProcessor, TypeTreeProcessor_Liabilities typeTreeProcessor, int level, String parentNodeName) {
        this.context = context;
        this.valueTreeProcessor = valueTreeProcessor;
        this.typeTreeProcessor = typeTreeProcessor;
        this.currentLevelNodeContainers = typeTreeProcessor.getSubGroup(parentNodeName, level);
        this.level = level;
    }

    /**
     * Get the current asset item's name
     *
     * @author Owner  Kevin Zhijun Wang
     * @param position the instance of commit button.
     * @return name of the asset
     */
    private String getLiabilitiesName(int position) {
        return currentLevelNodeContainers.get(position).liabilitiesTypeName;
    }

    /**
     * Get the count of the group of nodes.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return number of node containers in the group
     */
    @Override
    public int getGroupCount() {
        return currentLevelNodeContainers.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return number of node containers in the group
     */
    @Override
    public int getChildrenCount(int i1) {
        return 1;
    }

    /**
     * Gets the data associated with the given group.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return the group selected
     */
    @Override
    public Object getGroup(int i) {
        return currentLevelNodeContainers.get(i);
    }

    /**
     * Gets the current child node container of a parent node.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return current child node container of a parent node.
     */
    @Override
    public Object getChild(int i, int i1) {
        String liabilitiesTypeName = getLiabilitiesName(i);
        List<NodeContainer_Liabilities> carriers = typeTreeProcessor.getSubGroup(liabilitiesTypeName, level + 1);
        return carriers.get(i1);
    }

    /**
     * Gets the the id of the group.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return Id of the group.
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * Gets the the id of the group.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return Id of the group.
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the underlying data.
     *
     * @author Owner  Kevin Zhijun Wang
     * @return Boolean.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Create the view of a group.
     * The method will determine the level of the node by checking the level of each node.
     * Each item's data will be delivered to the right position with the help of processed data from the tree structure.
     * Also, the input fields will be related to the right type using the processed data from the tree processor
     * in order for the adapter to retrieve the right data for the right type.
     * Then, a text listener will be set to retrieve the data from the input field and insert into the tree processor's data source
     * to prepare for calculation and insertion into the database.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param position the position of the current item.
     * @param b indicate whether the group is expanded.
     * @param convertView the view object of the item.
     * @param viewGroup the group of item view.
     * @return View of a group.
     */
    @Override
    public View getGroupView(int position, boolean b, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (level == 1) {
            convertView = inflater.inflate(R.layout.liabilities_list_row_first,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowFirstText);
            textView.setText(currentLevelNodeContainers.get(position).liabilitiesTypeName);

        } else if (level == 2) {
            convertView = inflater.inflate(R.layout.liabilities_list_item,null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowThirdText);
            textView.setText(currentLevelNodeContainers.get(position).liabilitiesTypeName);

            NodeContainer_Liabilities nodeContainer = this.currentLevelNodeContainers.get(position);
            EditText editText = convertView.findViewById(R.id.liabilitiesValueInput);

            LiabilitiesValue liabilitiesValue = valueTreeProcessor.getLiabilityValue(nodeContainer.liabilitiesId);
            if (liabilitiesValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(liabilitiesValue.getLiabilitiesValue());
                editText.setText(strValue);
            }

            this.addTextListener(editText, nodeContainer);
        }
        return convertView;
    }

    /**
     * Retrieve the data from the input box and prepare for insertion.
     * First, the method will detect the change in the input box record the value.
     * Then, the method will reset the value the node in the tree, which is stored in processor data source.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param editText the input box for the current item.
     * @param nodeContainer the node container that contain the information of each node.
     */
    private void addTextListener(EditText editText, final NodeContainer_Liabilities nodeContainer){
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

                    valueTreeProcessor.setLiabilityValue(nodeContainer.liabilitiesId, liabilityValue);
                    Log.d("LFragmentAdapter", "value changed: " + text + ", float value: " + liabilityValue);

                } else {
                    String numberStr = "0.00";
                    final float liabilityValue = Float.parseFloat(numberStr);

                    valueTreeProcessor.setLiabilityValue(nodeContainer.liabilitiesId, liabilityValue);
                    Log.d("LFragmentAdapter", "value empty, set to 0 " + ", float value: " + liabilityValue);
                }
            }
        });
    }

    /**
     * Get the child view of the group.
     * The method will set up the nested expandable list in a particular category item.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param groupPosition the position of the group.
     * @param childPosition the position of the child item.
     * @param isExpanded indicate whether the group is expanded.
     * @param convertView the view of the child item
     * @param parent group of parent items
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final NodeContainer_Liabilities sectionData = currentLevelNodeContainers.get(groupPosition);
        List<NodeContainer_Liabilities> children = typeTreeProcessor.getSubGroup(sectionData.liabilitiesTypeName, level + 1);

        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.liabilities_list_row_first, null);
            TextView textView = convertView.findViewById(R.id.liabilitiesRowFirstText);
            textView.setText(this.currentLevelNodeContainers.get(childPosition).liabilitiesTypeName);
            return convertView;

        } else {
            final NetWorthExpandableListView nextLevelExpandableListView = new NetWorthExpandableListView(context);
            LiabilitiesFragmentChildViewClickListener listener = new LiabilitiesFragmentChildViewClickListener(currentLevelNodeContainers, typeTreeProcessor,level + 1);
            nextLevelExpandableListView.setAdapter(new LiabilitiesFragmentAdapter(context, valueTreeProcessor, typeTreeProcessor,level + 1, sectionData.liabilitiesTypeName));
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

    /**
     * Determine whether the child at the specified position is selectable
     *
     * @author Owner  Kevin Zhijun Wang
     * @param i groupPosition
     * @param i1 childPosition
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
