package protect.FinanceLord.NetWorthReportTemplateUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import protect.FinanceLord.R;

public class ReportListAdapter extends ArrayAdapter<NetWorthItemsDataModel> {

    public ReportListAdapter(Context context, ArrayList<NetWorthItemsDataModel> dataSources) {
        super(context, 0, dataSources);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetWorthItemsDataModel dataSource = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.net_worth_report_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.item_name);
        TextView itemValue = convertView.findViewById(R.id.item_value);
        TextView difference = convertView.findViewById(R.id.difference);

        itemName.setText(dataSource.itemName);
        itemValue.setText(String.valueOf(dataSource.itemValue));
        difference.setText(String.valueOf(dataSource.difference));

        return convertView;
    }
}
