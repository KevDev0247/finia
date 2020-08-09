package protect.Finia.SpendingModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import protect.Finia.R;

/**
 * The list adapter to deliver the data of spending of each month to the UI.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class SpendingListAdapter extends ArrayAdapter<MonthlyTotalSpending> {

    public SpendingListAdapter(@NonNull Context context, List<MonthlyTotalSpending> monthlyTotalSpendingList) {
        super(context,0, monthlyTotalSpendingList);
    }

    /**
     * Create and return the view for each item in the list.
     * The method first retrieve the data source of the current item, which contains the information to be displayed.
     * Then the method set the content of the view if it is not initialized.
     * The data from previous spending report is retrieved to calculate the difference of the spending.
     * Next, all the widgets on the UI are initialized.
     * Lastly, the data is loaded on to the widgets.
     * The color of difference block is also determined here by whether the difference is positive.
     *
     * @param position the position of the current item in the list
     * @param convertView the view class of this item
     * @param parent the ViewGroup of this list.
     * @return the view of the current report item.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MonthlyTotalSpending currentTotalSpending = getItem(position);
        MonthlyTotalSpending previousTotalSpending = null;
        float difference = 0;
        if (position != 0) {
            previousTotalSpending = getItem(position - 1);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spending_item, null, false);
        }

        TextView month = convertView.findViewById(R.id.spending_time);
        TextView spendingValue = convertView.findViewById(R.id.spending_total_value);
        TextView spendingDifference = convertView.findViewById(R.id.spending_difference);
        TextView spendingDifferenceSymbol = convertView.findViewById(R.id.spending_difference_symbol);
        LinearLayout differenceBlockView =  convertView.findViewById(R.id.spending_item_difference_block);

        month.setText(currentTotalSpending.month);
        spendingValue.setText(String.valueOf(currentTotalSpending.categoryTotal));
        spendingDifference.setText(getContext().getString(R.string.no_data_message));
        if (previousTotalSpending != null) {
            difference = currentTotalSpending.categoryTotal - previousTotalSpending.categoryTotal;
            spendingDifference.setText(String.valueOf(difference));
        }

        if (difference == 0) {
            spendingDifferenceSymbol.setText("");
            differenceBlockView.setBackgroundResource(R.drawable.net_neutral);
        } else if (difference > 0) {
            spendingDifferenceSymbol.setText("");
            differenceBlockView.setBackgroundResource(R.drawable.net_decrease);
        } else if (difference < 0) {
            spendingDifferenceSymbol.setText("");
            differenceBlockView.setBackgroundResource(R.drawable.net_increase);
        }

        return convertView;
    }
}
