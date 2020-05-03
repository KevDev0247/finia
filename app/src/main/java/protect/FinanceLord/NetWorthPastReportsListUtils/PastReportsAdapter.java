package protect.FinanceLord.NetWorthPastReportsListUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.R;

public class PastReportsAdapter extends ArrayAdapter<ReportItemsDataModel> {

    public PastReportsAdapter(@NonNull Context context, List<ReportItemsDataModel> dataModels) {
        super(context, 0, dataModels);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportItemsDataModel dataSource = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.past_reports_item, parent, false);
        }

        TextView netWorthTime = convertView.findViewById(R.id.net_worth_time);
        TextView netWorthValue = convertView.findViewById(R.id.net_worth_value);
        TextView netWorthDifference = convertView.findViewById(R.id.net_worth_difference);

        netWorthTime.setText(dataSource.time);
        netWorthValue.setText(String.valueOf(dataSource.netWorthValue));
        netWorthDifference.setText(String.valueOf(dataSource.difference));

        return convertView;
    }
}
