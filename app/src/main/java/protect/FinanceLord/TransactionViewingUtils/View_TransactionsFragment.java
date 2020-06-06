package protect.FinanceLord.TransactionViewingUtils;

import android.content.Context;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.Transactions;
import protect.FinanceLord.R;
import protect.FinanceLord.TransactionActivity;
import protect.FinanceLord.TransactionEditActivity;
import protect.FinanceLord.ViewModels.BudgetTypesViewModel;
import protect.FinanceLord.ViewModels.TransactionsViewModel;

public class View_TransactionsFragment extends Fragment {

    private TransactionListAdapter revenuesAdapter;
    private TransactionListAdapter expensesAdapter;
    private TransactionActivity transactionActivity;

    private List<Transactions> transactions;
    private List<BudgetsType> budgetsTypes;
    private List<Transactions> adapterRevenuesList = new ArrayList<>();
    private List<Transactions> adapterExpensesList = new ArrayList<>();

    static final int MAIN_ACTIVITY_REQUEST_CODE = 1000;
    private String fragmentTag;

    public View_TransactionsFragment(List<Transactions> transactions, List<BudgetsType> budgetsTypes, String fragmentTag) {
        this.transactions = transactions;
        this.budgetsTypes = budgetsTypes;
        this.fragmentTag = fragmentTag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TransactionActivity) {
            transactionActivity = (TransactionActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentTag.equals(getString(R.string.revenues_fragment_key))) {
            View revenuesFragmentView = inflater.inflate(R.layout.fragment_view_transactions, null);

            setUpRevenuesListView(revenuesFragmentView);

            setUpRevenuesViewModels();

            return revenuesFragmentView;

        } else if (fragmentTag.equals(getString(R.string.expenses_fragments_key))) {
            View expensesFragmentView = inflater.inflate(R.layout.fragment_view_transactions, null);

            setUpExpensesListView(expensesFragmentView);

            setUpExpensesViewModels();

            return expensesFragmentView;
        }

        return null;
    }

    private void setUpRevenuesListView(View revenuesFragmentView) {
        ListView revenuesListView = revenuesFragmentView.findViewById(R.id.transactions_list);

        for (Transactions transaction : transactions) {
            if (transaction.getTransactionValue() > 0) {
                Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                adapterRevenuesList.add(transaction);
            }
        }

        revenuesAdapter = new TransactionListAdapter(getContext(), adapterRevenuesList, budgetsTypes);
        revenuesListView.setAdapter(revenuesAdapter);
        revenuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transactions revenue = adapterRevenuesList.get(position);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.transaction_id_key), revenue.getTransactionId());
                intent.putExtra(getString(R.string.transaction_name_key), revenue.getTransactionName());
                intent.putExtra(getString(R.string.transaction_value_key), revenue.getTransactionValue());
                intent.putExtra(getString(R.string.transaction_category_key), revenue.getTransactionCategoryId());
                intent.putExtra(getString(R.string.transaction_comments_key), revenue.getTransactionComments());
                intent.putExtra(getString(R.string.transaction_date_key), revenue.getDate());
                intent.putExtra(getString(R.string.transaction_fragment_key), fragmentTag);
                intent.setClass(getContext(), TransactionEditActivity.class);
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void setUpExpensesListView(View expensesFragmentView) {
        ListView expensesListView = expensesFragmentView.findViewById(R.id.transactions_list);

        for (Transactions transaction : transactions) {
            if (transaction.getTransactionValue() < 0) {
                Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                adapterExpensesList.add(transaction);
            }
        }

        expensesAdapter = new TransactionListAdapter(getContext(), adapterExpensesList, budgetsTypes);
        expensesListView.setAdapter(expensesAdapter);
        expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transactions expense = adapterExpensesList.get(position);

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.transaction_id_key), expense.getTransactionId());
                intent.putExtra(getString(R.string.transaction_name_key), expense.getTransactionName());
                intent.putExtra(getString(R.string.transaction_value_key), expense.getTransactionValue());
                intent.putExtra(getString(R.string.transaction_category_key), expense.getTransactionCategoryId());
                intent.putExtra(getString(R.string.transaction_comments_key), expense.getTransactionComments());
                intent.putExtra(getString(R.string.transaction_date_key), expense.getDate());
                intent.putExtra(getString(R.string.transaction_fragment_key), fragmentTag);
                intent.setClass(getContext(), TransactionEditActivity.class);
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void setUpRevenuesViewModels() {
        BudgetTypesViewModel budgetTypesViewModel = ViewModelProviders.of(transactionActivity).get(BudgetTypesViewModel.class);
        budgetTypesViewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
                // budget types won't update when new categories where created when EDITING, not ADDING
                Log.d(fragmentTag, " budget types data has changed");
                budgetsTypes.clear();
                budgetsTypes.addAll(newBudgetsTypes);
                revenuesAdapter.notifyDataSetChanged();
            }
        });

        TransactionsViewModel transactionsViewModel = ViewModelProviders.of(transactionActivity).get(TransactionsViewModel.class);
        transactionsViewModel.getGroupedTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                revenuesAdapter.clear();
                for (Transactions transaction : transactions) {
                    if (transaction.getTransactionValue() > 0) {
                        Log.d(fragmentTag, "the item is " + transaction.getTransactionName() +
                                " the value is " + transaction.getTransactionValue() +
                                " the category is " + transaction.getTransactionCategoryId());
                        revenuesAdapter.add(transaction);
                    }
                }
                revenuesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpExpensesViewModels() {
        BudgetTypesViewModel budgetTypesViewModel = ViewModelProviders.of(transactionActivity).get(BudgetTypesViewModel.class);
        budgetTypesViewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
                Log.d(fragmentTag, " budget types data has changed");
                budgetsTypes.clear();
                budgetsTypes.addAll(newBudgetsTypes);
                expensesAdapter.notifyDataSetChanged();
            }
        });

        TransactionsViewModel transactionsViewModel = ViewModelProviders.of(transactionActivity).get(TransactionsViewModel.class);
        transactionsViewModel.getGroupedTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transactions>>() {
            @Override
            public void onChanged(List<Transactions> transactions) {
                expensesAdapter.clear();
                for (Transactions transaction : transactions) {
                    if (transaction.getTransactionValue() < 0) {
                        Log.d(fragmentTag, " the item is " + transaction.getTransactionName() +
                                " the value is " + transaction.getTransactionValue() +
                                " the category is " + transaction.getTransactionCategoryId());
                        expensesAdapter.add(transaction);
                    }
                }
                expensesAdapter.notifyDataSetChanged();
            }
        });
    }
}
