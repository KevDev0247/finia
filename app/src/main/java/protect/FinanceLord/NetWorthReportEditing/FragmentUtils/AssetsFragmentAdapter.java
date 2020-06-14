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

import protect.FinanceLord.NetWorthDataStructure.NodeContainer_Assets;
import protect.FinanceLord.NetWorthDataStructure.ValueTreeProcessor_Assets;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.NetWorthDataStructure.TypeTreeProcessor_Assets;
import protect.FinanceLord.NetWorthReportEditing.NetWorthExpandableListView;
import protect.FinanceLord.R;

/**
 * The adapter for the Edit_AssetsFragment.
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
public class AssetsFragmentAdapter extends BaseExpandableListAdapter {

    private ValueTreeProcessor_Assets valueTreeProcessor;
    private TypeTreeProcessor_Assets typeTreeProcessor;
    private List<NodeContainer_Assets> currentLevelNodesContainers;
    private Context context;
    private int level;

    public AssetsFragmentAdapter(Context context, ValueTreeProcessor_Assets valueTreeProcessor, TypeTreeProcessor_Assets typeTreeProcessor, int level, String parentNodeName) {
        this.context = context;
        this.valueTreeProcessor = valueTreeProcessor;
        this.typeTreeProcessor = typeTreeProcessor;
        this.currentLevelNodesContainers = typeTreeProcessor.getSubGroup(parentNodeName, level);
        this.level = level;
    }

    /**
     * Get the current asset item's name
     *
     * @param position the instance of commit button.
     * @return name of the asset
     */
    private String getAssetsName(int position) {
        return this.currentLevelNodesContainers.get(position).assetsTypeName;
    }

    /**
     * Get the count of the group of nodes.
     *
     * @return number of node containers in the group
     */
    @Override
    public int getGroupCount() {
        return currentLevelNodesContainers.size();
    }

    /**
     * Gets the number of children in a specified group.
     *
     * @return number of node containers in the group
     */
    @Override
    public int getChildrenCount(int i1) {
        return 1;
    }

    /**
     * Gets the data associated with the given group.
     *
     * @return the group selected
     */
    @Override
    public Object getGroup(int i) {
        return currentLevelNodesContainers.get(i);
    }

    /**
     * Gets the current child node container of a parent node.
     *
     * @return current child node container of a parent node.
     */
    @Override
    public Object getChild(int i, int i1) {
        String assetsTypeName = getAssetsName(i);
        List<NodeContainer_Assets> carriers = typeTreeProcessor.getSubGroup(assetsTypeName, level + 1);
        return carriers.get(i1);
    }

    /**
     * Gets the the id of the group.
     *
     * @return Id of the group.
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * Gets the the id of the group.
     *
     * @return Id of the group.
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to the underlying data.
     *
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
            convertView = inflater.inflate(R.layout.assets_list_row_first_category, null);
            TextView textView = convertView.findViewById(R.id.assetsRowParentText);
            textView.setText(this.currentLevelNodesContainers.get(position).assetsTypeName);

        } else if (level == 2 && currentLevelNodesContainers.get(position).assetsTypeId != 29
                              && currentLevelNodesContainers.get(position).assetsTypeId != 30
                              && currentLevelNodesContainers.get(position).assetsTypeId != 31) {
            convertView = inflater.inflate(R.layout.assets_list_items, null);
            TextView textView = convertView.findViewById(R.id.assetsRowThirdText);
            textView.setText(this.currentLevelNodesContainers.get(position).assetsTypeName);

            NodeContainer_Assets nodeContainer = this.currentLevelNodesContainers.get(position);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);

            AssetsValue assetsValue = valueTreeProcessor.getAssetValue(nodeContainer.assetsTypeId);

            if (assetsValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(assetsValue.getAssetsValue());
                editText.setText(strValue);
            }

            this.addTextListener(editText, nodeContainer);

        } else if (level == 2) {
            convertView = inflater.inflate(R.layout.assets_list_row_second_category, null);
            TextView textView = convertView.findViewById(R.id.assetsRowSecondCategoryText);
            textView.setText(this.currentLevelNodesContainers.get(position).assetsTypeName);

        } else if (level == 3) {
            convertView = inflater.inflate(R.layout.assets_list_items, null);
            TextView textView = convertView.findViewById(R.id.assetsRowThirdText);
            textView.setText(this.currentLevelNodesContainers.get(position).assetsTypeName);

            NodeContainer_Assets nodeContainer = this.currentLevelNodesContainers.get(position);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);

            AssetsValue assetsValue = valueTreeProcessor.getAssetValue(nodeContainer.assetsTypeId);
            if (assetsValue != null) {
                DecimalFormat decimalFormat = new DecimalFormat();
                String strValue = decimalFormat.format(assetsValue.getAssetsValue());
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
     * @param editText the input box for the current item.
     * @param nodeContainer the node container that contain the information of each node.
     */
    private void addTextListener(EditText editText, final NodeContainer_Assets nodeContainer) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)){
                    String numberStr = text.replace(",", "");
                    final float assetValue = Float.parseFloat(numberStr);

                    valueTreeProcessor.setAssetValue(nodeContainer.assetsTypeId, assetValue);
                    Log.d("AFragmentAdapter", "Value changed: " + text + ", float value: " + assetValue);

                } else {
                    String numberStr = "0.00";
                    final float assetsValue = Float.parseFloat(numberStr);

                    valueTreeProcessor.setAssetValue(nodeContainer.assetsTypeId, assetsValue);
                    Log.d("AFragmentAdapter","Value empty, set to 0 " + ", float value: " + assetsValue);
                }
            }
        });
    }

    /**
     * Get the child view of the group.
     * The method will set up the nested expandable list in a particular category item.
     *
     * @param groupPosition the position of the group.
     * @param childPosition the position of the child item.
     * @param isExpanded indicate whether the group is expanded.
     * @param convertView the view of the child item
     * @param parent group of parent items
     * @return the view of the child
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final NodeContainer_Assets sectionData = currentLevelNodesContainers.get(groupPosition);
        List<NodeContainer_Assets> children = typeTreeProcessor.getSubGroup(sectionData.assetsTypeName, level + 1);

        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assets_list_row_first_category, null);
            TextView textView = convertView.findViewById(R.id.assetsRowParentText);
            textView.setText(this.currentLevelNodesContainers.get(childPosition).assetsTypeName);
            return convertView;

        } else {
            final NetWorthExpandableListView nextLevelExpandableListView = new NetWorthExpandableListView(context);
            AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(currentLevelNodesContainers, typeTreeProcessor, level + 1);
            nextLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, valueTreeProcessor, typeTreeProcessor,level + 1, sectionData.assetsTypeName));
            nextLevelExpandableListView.setOnChildClickListener(listener);
            nextLevelExpandableListView.setDivider(null);

            if (sectionData.assetsTypeId == 29 || sectionData.assetsTypeId == 30 || sectionData.assetsTypeId == 31 || sectionData.assetsTypeId == 32 || sectionData.assetsTypeId == 34){
                nextLevelExpandableListView.setGroupIndicator(null);
                nextLevelExpandableListView.setDividerHeight(10);
            } else {
                nextLevelExpandableListView.setDividerHeight(20);
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
     * @param i groupPosition
     * @param i1 childPosition
     * @return whether the child is selectable
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}