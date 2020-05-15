package protect.FinanceLord.NetWorthReportViewingUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import protect.FinanceLord.R;

public class ReportListAdapter extends ArrayAdapter<NetWorthItemsDataModel> {

    String fragmentName;

    public ReportListAdapter(Context context, ArrayList<NetWorthItemsDataModel> dataSources, String fragmentName) {
        super(context, 0, dataSources);
        this.fragmentName = fragmentName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetWorthItemsDataModel dataSource = getItem(position);

        if (fragmentName.equals(getContext().getString(R.string.report_assets_fragment_name))) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.assets_report_item, null, false);
            }

            TextView assetName = convertView.findViewById(R.id.asset_item_name);
            TextView assetValue = convertView.findViewById(R.id.asset_item_value);
            TextView assetDifferenceSymbol = convertView.findViewById(R.id.asset_symbol);
            TextView assetDifference = convertView.findViewById(R.id.asset_difference);

            assetName.setText(dataSource.itemName);
            assetValue.setText(dataSource.itemValue);
            assetDifference.setText(dataSource.difference);

            if (dataSource.difference.equals(getContext().getString(R.string.no_data_initialization))){
                assetDifferenceSymbol.setText("");

            } else if (Float.parseFloat(dataSource.difference) > 0) {
                View differenceBlockView = convertView.findViewById(R.id.asset_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.ic_net_increase);
                assetDifferenceSymbol.setText(R.string.positive_symbol);

            } else if (Float.parseFloat(dataSource.difference) < 0){
                View differenceBlockView = convertView.findViewById(R.id.asset_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.ic_net_decrease);
                assetDifferenceSymbol.setText(R.string.negative_symbol);

            } else if (Float.parseFloat(dataSource.difference) == 0){
                assetDifferenceSymbol.setText("");
            }

            return convertView;

        } else if (fragmentName.equals(getContext().getString(R.string.report_liabilities_fragment_name))) {

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.liabilities_report_item, null, false);
            }

            TextView itemName = convertView.findViewById(R.id.liabilities_item_name);
            TextView itemValue = convertView.findViewById(R.id.liabilities_item_value);
            TextView difference = convertView.findViewById(R.id.liabilities_item_difference);

            itemName.setText(dataSource.itemName);
            itemValue.setText(String.valueOf(dataSource.itemValue));
            difference.setText(String.valueOf(dataSource.difference));

            return convertView;
        }

        return null;
    }
}
