package protect.FinanceLord.TransactionViewingUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionEditActivity;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;

public class View_TransactionsFragment extends Fragment {

    private List<Transactions> transactions;
    private List<BudgetTypesDataModel> dataModels;
    private String fragmentTag;

    public View_TransactionsFragment(List<Transactions> transactions, List<BudgetTypesDataModel> dataModels, String fragmentTag) {
        this.transactions = transactions;
        this.dataModels = dataModels;
        this.fragmentTag = fragmentTag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentTag.equals(getString(R.string.view_revenues_fragment_key))) {
            View revenuesFragmentView = inflater.inflate(R.layout.fragment_view_transactions, null);
            ListView revenuesListView = revenuesFragmentView.findViewById(R.id.transactions_list);

            final List<Transactions> revenues = new ArrayList<>();

            for (Transactions transaction : transactions) {
                if (transaction.getTransactionValue() > 0) {
                    Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                    revenues.add(transaction);
                }
            }

            TransactionListAdapter adapter = new TransactionListAdapter(getContext(), revenues, dataModels);
            revenuesListView.setAdapter(adapter);

            revenuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Transactions revenue = revenues.get(position);

                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.transaction_id_key), revenue.getTransactionId());
                    intent.putExtra(getString(R.string.transaction_name_key), revenue.getTransactionName());
                    intent.putExtra(getString(R.string.transaction_value_key), revenue.getTransactionValue());
                    intent.putExtra(getString(R.string.transaction_category_key), revenue.getTransactionCategoryId());
                    intent.putExtra(getString(R.string.transaction_comments_key), revenue.getTransactionComments());
                    intent.putExtra(getString(R.string.transaction_date_key), revenue.getDate());
                    intent.putExtra(getString(R.string.transaction_fragment_key), fragmentTag);
                    intent.setClass(getContext(), TransactionEditActivity.class);
                    startActivity(intent);
                }
            });

            return revenuesFragmentView;

        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
            View expensesFragmentView = inflater.inflate(R.layout.fragment_view_transactions, null);
            ListView expensesListView = expensesFragmentView.findViewById(R.id.transactions_list);

            final List<Transactions> expenses = new ArrayList<>();

            for (Transactions transaction : transactions) {
                if (transaction.getTransactionValue() < 0) {
                    Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                    expenses.add(transaction);
                }
            }

            TransactionListAdapter adapter = new TransactionListAdapter(getContext(), expenses, dataModels);
            expensesListView.setAdapter(adapter);

            expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Transactions expense = expenses.get(position);

                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.transaction_id_key), expense.getTransactionId());
                    intent.putExtra(getString(R.string.transaction_name_key), expense.getTransactionName());
                    intent.putExtra(getString(R.string.transaction_value_key), expense.getTransactionValue());
                    intent.putExtra(getString(R.string.transaction_category_key), expense.getTransactionCategoryId());
                    intent.putExtra(getString(R.string.transaction_comments_key), expense.getTransactionComments());
                    intent.putExtra(getString(R.string.transaction_date_key), expense.getDate());
                    intent.putExtra(getString(R.string.transaction_fragment_key), fragmentTag);
                    intent.setClass(getContext(), TransactionEditActivity.class);
                    startActivity(intent);
                }
            });

            return expensesFragmentView;
        }

        return null;
    }
}
