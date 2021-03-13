package protect.Finia.networth.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import protect.Finia.R;

/**
 * The adapter for list of net worth items on the report sheet.
 * Each report item acts as a link to the report sheet.
 * Each report item contains the data of the report, the net worth value,
 * and the difference of net worth compare to the last period.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/04/24
 */
public class PastReportsAdapter extends ArrayAdapter<ReportItemsDataModel> {

    public PastReportsAdapter(@NonNull Context context, List<ReportItemsDataModel> dataModels) {
        super(context, 0, dataModels);
    }

    /**
     * Create and return the view for each item in the list.
     * The method first retrieve the data source of the current item, which contains the information to be displayed.
     * Then the method set the content of the view if it is not initialized.
     * Next, all the widgets on the UI are initialized.
     * Lastly, the data is displayed on to the widgets.
     * The color of difference block is also determined here by whether the difference is positive.
     *
     * @param position the position of the current item in the list
     * @param convertView the view class of this item
     * @param parent the ViewGroup of this list
     * @return the view of the report item in the past report list.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportItemsDataModel dataSource = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.past_reports_item, parent, false);
        }

        TextView netWorthTime = convertView.findViewById(R.id.net_worth_time);
        TextView netWorthValue = convertView.findViewById(R.id.net_worth_value);
        TextView netWorthDifferenceSymbol = convertView.findViewById(R.id.net_worth_difference_symbol);
        TextView netWorthDifference = convertView.findViewById(R.id.net_worth_difference);
        View differenceBlockView = convertView.findViewById(R.id.past_report_item_difference_block);

        netWorthTime.setText(dataSource.time);
        netWorthValue.setText(String.valueOf(dataSource.netWorthValue));
        netWorthDifference.setText(dataSource.difference);

        if (dataSource.difference.equals(getContext().getString(R.string.no_data_message))){
            netWorthDifferenceSymbol.setText("");
            differenceBlockView.setBackgroundResource(R.drawable.net_neutral);

        } else if (Float.parseFloat(dataSource.difference) > 0) {
            differenceBlockView.setBackgroundResource(R.drawable.net_increase);
            netWorthDifferenceSymbol.setText(R.string.positive_symbol);

        } else if (Float.parseFloat(dataSource.difference) < 0) {
            differenceBlockView.setBackgroundResource(R.drawable.net_decrease);
            netWorthDifferenceSymbol.setText(R.string.negative_symbol);

        } else if (Float.parseFloat(dataSource.difference) == 0) {
            netWorthDifferenceSymbol.setText("");
            differenceBlockView.setBackgroundResource(R.drawable.net_neutral);
        }

        return convertView;
    }
}
