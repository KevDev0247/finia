package protect.FinanceLord.TransactionViewingUtils;

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

import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;

public class TransactionListAdapter extends ArrayAdapter<Transactions> {

    private Context context;
    private List<BudgetTypesDataModel> dataModels;

    TransactionListAdapter(@NonNull Context context, List<Transactions> transactions, List<BudgetTypesDataModel> dataModels) {
        super(context, 0, transactions);

        this.context = context;
        this.dataModels = dataModels;
    }

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

                for (BudgetTypesDataModel dataModel : dataModels) {
                    if (dataModel.typeId == transaction.getTransactionCategoryId()) {
                        itemCategory.setText(dataModel.typeName);
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

                for (BudgetTypesDataModel dataModel : dataModels) {
                    if (dataModel.typeId == transaction.getTransactionCategoryId()) {
                        itemCategory.setText(dataModel.typeName);
                    }
                }

                return convertView;
            }
        }

        return null;
    }
}
