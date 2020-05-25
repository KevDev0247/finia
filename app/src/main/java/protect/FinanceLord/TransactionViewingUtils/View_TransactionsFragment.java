package protect.FinanceLord.TransactionViewingUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;

public class View_TransactionsFragment extends Fragment {

    private List<Transactions> transactions;
    private List<BudgetTypesDataModel> dataModels;
    private String fragmentTag;

    private static String TAG = "View_TransactionsFragment";

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

            List<Transactions> revenues = new ArrayList<>();

            for (Transactions transaction : transactions) {
                if (transaction.getTransactionValue() > 0) {
                    Log.d(TAG, "the value is " + transaction.getTransactionValue());
                    revenues.add(transaction);
                }
            }

            TransactionListAdapter adapter = new TransactionListAdapter(getContext(), revenues, dataModels);
            revenuesListView.setAdapter(adapter);

            return revenuesFragmentView;

        } else if (fragmentTag.equals(getString(R.string.view_expenses_fragment_key))) {
            View expensesFragmentView = inflater.inflate(R.layout.fragment_view_transactions, null);
            ListView expensesListView = expensesFragmentView.findViewById(R.id.transactions_list);

            List<Transactions> expenses = new ArrayList<>();

            for (Transactions transaction : transactions) {
                if (transaction.getTransactionValue() < 0) {
                    Log.d(TAG, "the value is " + transaction.getTransactionValue());
                    expenses.add(transaction);
                }
            }

            TransactionListAdapter adapter = new TransactionListAdapter(getContext(), expenses, dataModels);
            expensesListView.setAdapter(adapter);

            return expensesFragmentView;
        }

        return null;
    }
}
