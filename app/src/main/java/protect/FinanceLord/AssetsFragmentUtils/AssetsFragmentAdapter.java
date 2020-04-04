package protect.FinanceLord.AssetsFragmentUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import protect.FinanceLord.Database.AssetsTypeDao;
import protect.FinanceLord.Database.AssetsTypeQuery;
import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;
import protect.FinanceLord.ui.NetWorthEditReports.AssetsFragment;
import protect.FinanceLord.ui.NetWorthEditReports.AssetsSecondLevelExpandableListView;

public class AssetsFragmentAdapter extends BaseExpandableListAdapter {

    private AssetsFragmentDataProcessor dataProcessor;
    private List<AssetsFragmentDataCarrier> sectionDataSet;
    private List<AssetsValue> assetsValues;

    private int level;
    private Context context;

    public AssetsFragmentAdapter(Context context, AssetsFragmentDataProcessor dataProcessor, List<AssetsValue> assetsValues, int level, String parentSection) {
        this.context = context;
        this.dataProcessor = dataProcessor;
        this.assetsValues = assetsValues;
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
        if (level == 0){
            convertView = inflater.inflate(R.layout.assets_list_row_first, null);
            TextView textView = convertView.findViewById(R.id.rowParentText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 1){
            convertView = inflater.inflate(R.layout.assets_list_row_second, null);
            TextView textView = convertView.findViewById(R.id.rowSecondText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 2 && sectionDataSet.get(position).assetsId != 29
                              && sectionDataSet.get(position).assetsId != 30
                              && sectionDataSet.get(position).assetsId != 31){
            convertView = inflater.inflate(R.layout.assets_list_row_third, null);
            TextView textView = convertView.findViewById(R.id.rowThirdText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 2) {
            convertView = inflater.inflate(R.layout.assets_list_row_second_category, null);
            TextView textView = convertView.findViewById(R.id.rowSecondCategoryText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
        } else if (level == 3){
            final AssetsFragmentDataCarrier dataCarrier = this.sectionDataSet.get(position);
            convertView = inflater.inflate(R.layout.assets_list_row_third, null);
            TextView textView = convertView.findViewById(R.id.rowThirdText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);
            editText.setText(this.getAssetsId(dataCarrier.assetsId));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final float assetsValue = Float.valueOf(s.toString());
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            FinanceLordDatabase database = FinanceLordDatabase.getInstance(AssetsFragmentAdapter.this.context);
                            AssetsValueDao assetsValueDao = database.assetsValueDao();
                            AssetsValue value = new AssetsValue();
                            value.setAssetsId(dataCarrier.assetsId);
                            value.setAssetsValue(assetsValue);
                            value.setDate(new Date().getTime());
                            assetsValueDao.insertAssetValue(value);
                        }
                    });

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return convertView;
    }

    float getAssetsValue(int assetsId) {
        for (AssetsValue assetsValue: this.assetsValues) {
            if (assetsValue.getAssetsId() == assetsId) {
                return assetsValue.getAssetsValue();
            }
        }
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final AssetsFragmentDataCarrier sectionData = sectionDataSet.get(groupPosition);
        List<AssetsFragmentDataCarrier> children = dataProcessor.getSubSet(sectionData.assetsTypeName, level + 1);
        if (children.size() == 0) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.assets_list_row_first, null);
            TextView textView = convertView.findViewById(R.id.rowParentText);
            textView.setText(this.sectionDataSet.get(childPosition).assetsTypeName);
            return convertView;
        } else{
            final AssetsSecondLevelExpandableListView secondLevelExpandableListView = new AssetsSecondLevelExpandableListView(context);
            AssetsFragmentChildViewClickListener listener = new AssetsFragmentChildViewClickListener(sectionDataSet, dataProcessor, level + 1);
            secondLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, dataProcessor, this.assetsValues, level + 1, sectionData.assetsTypeName));
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