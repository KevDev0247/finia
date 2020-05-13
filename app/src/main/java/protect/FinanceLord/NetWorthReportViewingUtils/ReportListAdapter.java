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

            TextView itemName = convertView.findViewById(R.id.assets_item_name);
            TextView itemValue = convertView.findViewById(R.id.assets_item_value);
            TextView difference = convertView.findViewById(R.id.assets_difference);

            itemName.setText(dataSource.itemName);
            itemValue.setText(String.valueOf(dataSource.itemValue));
            difference.setText(String.valueOf(dataSource.difference));

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
