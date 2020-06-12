package protect.FinanceLord.NetWorthReportViewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import protect.FinanceLord.R;

/**
 * The list adapter for the report sheets
 * Notice this adapter is reusable. It is used by both Assets and Liabilities.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class ReportListAdapter extends ArrayAdapter<NetWorthItemsDataModel> {

    private String fragmentTag;

    ReportListAdapter(Context context, ArrayList<NetWorthItemsDataModel> dataSources, String fragmentTag) {
        super(context, 0, dataSources);
        this.fragmentTag = fragmentTag;
    }

    /**
     * Create the view of each item.
     * The method will decide whether the item is an asset or a liability
     * with the fragment tag that is inputted through the constructor.
     * Then the adapter will load the data from the data source to the UI widgets.
     * It will also prepare the rest of the UI for display by set the sign and the color of the difference block.
     *
     * @author Owner  Kevin Zhijun Wang
     * @param position the position of the item
     * @param convertView the view of the current item
     * @param parent the group of views.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetWorthItemsDataModel dataSource = getItem(position);

        if (fragmentTag.equals(getContext().getString(R.string.report_assets_fragment_key))) {
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

            if (dataSource.difference.equals(getContext().getString(R.string.no_data_initialization))) {
                assetDifferenceSymbol.setText("");

            } else if (Float.parseFloat(dataSource.difference) > 0) {
                View differenceBlockView = convertView.findViewById(R.id.asset_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.net_increase);
                assetDifferenceSymbol.setText(R.string.positive_symbol);

            } else if (Float.parseFloat(dataSource.difference) < 0) {
                View differenceBlockView = convertView.findViewById(R.id.asset_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.net_decrease);
                assetDifferenceSymbol.setText(R.string.negative_symbol);

            } else if (Float.parseFloat(dataSource.difference) == 0) {
                assetDifferenceSymbol.setText("");
            }

            return convertView;

        } else if (fragmentTag.equals(getContext().getString(R.string.report_liabilities_fragment_key))) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.liabilities_report_item, null, false);
            }

            TextView liabilityItemName = convertView.findViewById(R.id.liability_item_name);
            TextView liabilityItemValue = convertView.findViewById(R.id.liability_item_value);
            TextView liabilityDifferenceSymbol = convertView.findViewById(R.id.liability_symbol);
            TextView liabilityDifference = convertView.findViewById(R.id.liability_item_difference);

            liabilityItemName.setText(dataSource.itemName);
            liabilityItemValue.setText(dataSource.itemValue);
            liabilityDifference.setText(String.valueOf(dataSource.difference));

            if (dataSource.difference.equals(getContext().getString(R.string.no_data_initialization))) {
                liabilityDifferenceSymbol.setText("");

            } else if (Float.parseFloat(dataSource.difference) > 0) {
                View differenceBlockView = convertView.findViewById(R.id.liability_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.net_increase);
                liabilityDifferenceSymbol.setText(R.string.positive_symbol);

            } else if (Float.parseFloat(dataSource.difference) < 0) {
                View differenceBlockView = convertView.findViewById(R.id.liability_item_difference_block);
                differenceBlockView.setBackgroundResource(R.drawable.net_decrease);
                liabilityDifferenceSymbol.setText(R.string.negative_symbol);

            } else if (Float.parseFloat(dataSource.difference) == 0) {
                liabilityDifferenceSymbol.setText("");
            }

            return convertView;
        }

        return null;
    }
}
