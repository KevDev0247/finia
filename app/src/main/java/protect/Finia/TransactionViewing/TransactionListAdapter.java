package protect.Finia.TransactionViewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import protect.Finia.Database.BudgetsType;
import protect.Finia.Database.Transactions;
import protect.Finia.R;

/**
 * The list adapter to deliver the data of each transaction to the UI.
 * The TransactionListAdapter are used by both expenses and revenues.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionListAdapter extends ArrayAdapter<Transactions> {

    private Context context;
    private List<BudgetsType> budgetsTypes;

    TransactionListAdapter(@NonNull Context context, List<Transactions> transactions, List<BudgetsType> budgetsTypes) {
        super(context, 0, transactions);
        this.context = context;
        this.budgetsTypes = budgetsTypes;
    }

    /**
     * Create and return the view for each item in the list.
     * The method first retrieve the data source of the current item, which contains the information to be displayed.
     * The method also determine whether the current item is a expense or a revenue item.
     * Then the method set the content of the view if it is not initialized.
     * Next, all the widgets on the UI are initialized.
     * Lastly, the data is loaded on to the widgets.
     * The color of difference block is also determined here by whether the difference is positive.
     *
     * @param position the position of the current item in the list
     * @param convertView the view class of this item
     * @param parent the ViewGroup of this list.
     * @return the view of the current transaction item.
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Transactions transaction = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);

        if (transaction != null) {
            if (transaction.getTransactionValue() > 0) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, null, false);
                }

                LinearLayout itemLeftEdge = convertView.findViewById(R.id.transaction_left_edge);
                TextView itemName = convertView.findViewById(R.id.transaction_item_name);
                TextView itemValue = convertView.findViewById(R.id.transaction_item_value);
                TextView itemCategory = convertView.findViewById(R.id.transaction_item_category);
                TextView itemDate = convertView.findViewById(R.id.transaction_item_date);

                itemLeftEdge.setBackground(context.getDrawable(R.drawable.expenses_left_edge));
                itemName.setText(transaction.getTransactionName());
                itemValue.setText(String.valueOf(transaction.getTransactionValue()));
                itemDate.setText(dateFormat.format(transaction.getDate()));

                for (BudgetsType budgetsType : budgetsTypes) {
                    if (budgetsType.getBudgetsCategoryId() == transaction.getTransactionCategoryId()) {
                        itemCategory.setText(budgetsType.getBudgetsName());
                    }
                }

                return convertView;

            } else if (transaction.getTransactionValue() < 0) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, null, false);
                }

                LinearLayout itemLeftEdge = convertView.findViewById(R.id.transaction_left_edge);
                TextView itemName = convertView.findViewById(R.id.transaction_item_name);
                TextView itemValue = convertView.findViewById(R.id.transaction_item_value);
                TextView itemCategory = convertView.findViewById(R.id.transaction_item_category);
                TextView itemDate = convertView.findViewById(R.id.transaction_item_date);

                itemLeftEdge.setBackground(context.getDrawable(R.drawable.revenues_left_edge));
                itemName.setText(transaction.getTransactionName());
                itemValue.setText(String.valueOf(transaction.getTransactionValue()));
                itemDate.setText(dateFormat.format(transaction.getDate()));

                for (BudgetsType budgetsType : budgetsTypes) {
                    if (budgetsType.getBudgetsCategoryId() == transaction.getTransactionCategoryId()) {
                        itemCategory.setText(budgetsType.getBudgetsName());
                    }
                }

                return convertView;
            }
        }

        return super.getView(position, convertView, parent);
    }
}
