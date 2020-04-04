package protect.FinanceLord.AssetsFragmentUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
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

import protect.FinanceLord.Database.AssetsValue;
import protect.FinanceLord.Database.AssetsValueDao;
import protect.FinanceLord.Database.FinanceLordDatabase;
import protect.FinanceLord.R;
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

    public CharSequence getAssetsValue(int assetsId){
        for (AssetsValue assetsValue: this.assetsValues){
            if (assetsValue.getAssetsId() == assetsId){
                return String.valueOf(assetsValue.getAssetsValue()) ;
            }
        }
        return null;
    }

    @Override
    public int getGroupCount() {
        return getSectionGroupCount();
    }

    @Override
    public int getChildrenCount(int position) {
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
            convertView = inflater.inflate(R.layout.assets_list_row_third, null);
            TextView textView = convertView.findViewById(R.id.rowThirdText);
            textView.setText(this.sectionDataSet.get(position).assetsTypeName);

            final AssetsFragmentDataCarrier dataCarrier = this.sectionDataSet.get(position);
            EditText editText = convertView.findViewById(R.id.assetsValueInput);
            editText.setText(getAssetsValue(dataCarrier.assetsId));
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (TextUtils.isEmpty(charSequence)){
                        final float assetValue = Float.valueOf(charSequence.toString());
                    }
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            FinanceLordDatabase database = FinanceLordDatabase.getInstance(context);
                            AssetsValueDao assetsValueDao = database.assetsValueDao();
                            AssetsValue assetsValue = new AssetsValue();
                            if (getAssetsValue(dataCarrier.assetsId) == null){
                                assetsValue.setAssetsId(dataCarrier.assetsId);
                                assetsValue.setAssetsValue(assetValue);
                                assetsValue.setDate(new Date().getTime());
                                assetsValueDao.insertAssetValue(assetsValue);
                            } else {
                                assetsValue.setAssetsValue(assetValue);
                                assetsValueDao.updateAssetValue(assetsValue);
                            }
                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });
        }
        return convertView;
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
            secondLevelExpandableListView.setAdapter(new AssetsFragmentAdapter(context, dataProcessor, this.assetsValues,level + 1, sectionData.assetsTypeName));
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