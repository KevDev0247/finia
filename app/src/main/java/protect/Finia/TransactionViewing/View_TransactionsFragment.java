package protect.Finia.TransactionViewing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import protect.Finia.Database.BudgetsType;
import protect.Finia.Database.Transactions;
import protect.Finia.R;
import protect.Finia.Activities.TransactionActivity;
import protect.Finia.Activities.TransactionEditActivity;
import protect.Finia.ViewModels.BudgetTypesViewModel;
import protect.Finia.ViewModels.TransactionsViewModel;

/**
 * The class for the fragment to display transactions.
 * Note that this fragment is reusable. It is used for both expense and revenue sections.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class View_TransactionsFragment extends Fragment {

    private TransactionListAdapter revenuesAdapter;
    private TransactionListAdapter expensesAdapter;
    private TransactionActivity transactionActivity;
    private LinearLayout emptyMessageField;

    private List<Transactions> transactions;
    private List<BudgetsType> budgetsTypes;
    private List<Transactions> revenuesList = new ArrayList<>();
    private List<Transactions> expensesList = new ArrayList<>();

    private static final int MAIN_ACTIVITY_REQUEST_CODE = 1000;
    private String fragmentTag;

    public View_TransactionsFragment(List<Transactions> transactions, List<BudgetsType> budgetsTypes, String fragmentTag) {
        this.transactions = transactions;
        this.budgetsTypes = budgetsTypes;
        this.fragmentTag = fragmentTag;
    }

    /**
     * Attach the fragment to the activity it belongs to.
     * In this method, the instance of the activity the fragment belongs to will be retrieved to set up the view model.
     *
     * @param context the context of this fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TransactionActivity) {
            transactionActivity = (TransactionActivity) context;
        }
    }

    /**
     * Create the view of the fragment.
     * The method will first determine whether to create a revenues fragment or a expenses fragment based on the fragment tag.
     * Then, the list of transactions and the view model will be set up.
     *
     * @param inflater the Android System Services that is responsible for taking the XML files that define a layout, and converting them into View objects
     * @param container the container of the group of views.
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     * @return the view of the fragment.
     */
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

    /**
     * Set up the list by retrieve the data and set up the adapter.
     * An onItemClickListener is also added to each item to navigate the user to edit sheet when clicked.
     * The data of the item will be stored in the intent and transferred to the edit activity
     *
     * @param revenuesFragmentView the view of the revenues fragment.
     */
    private void setUpRevenuesListView(View revenuesFragmentView) {
        ListView revenuesListView = revenuesFragmentView.findViewById(R.id.transactions_list);
        emptyMessageField = revenuesFragmentView.findViewById(R.id.transaction_initialization_message);
        for (Transactions transaction : transactions) {
            if (transaction.getTransactionValue() > 0) {
                Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                revenuesList.add(transaction);
            }
        }

        setUpRevenuesEmptyMessage();

        revenuesAdapter = new TransactionListAdapter(getContext(), revenuesList, budgetsTypes);
        revenuesListView.setAdapter(revenuesAdapter);
        revenuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transactions revenue = revenuesList.get(position);

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

    /**
     * Set up the list by retrieve the data and set up the adapter.
     * An onItemClickListener is also added to each item to navigate the user to edit sheet when clicked.
     * The data of the item will be stored in the intent and transferred to the edit activity
     *
     * @param expensesFragmentView the view of the expenses fragment.
     */
    private void setUpExpensesListView(View expensesFragmentView) {
        ListView expensesListView = expensesFragmentView.findViewById(R.id.transactions_list);
        emptyMessageField = expensesFragmentView.findViewById(R.id.transaction_initialization_message);
        for (Transactions transaction : transactions) {
            if (transaction.getTransactionValue() < 0) {
                Log.d(fragmentTag, "the item is " + transaction.getTransactionName() + " the value is " + transaction.getTransactionValue());
                expensesList.add(transaction);
            }
        }

        setUpExpensesEmptyMessage();

        expensesAdapter = new TransactionListAdapter(getContext(), expensesList, budgetsTypes);
        expensesListView.setAdapter(expensesAdapter);
        expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transactions expense = expensesList.get(position);

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

    /**
     * Set up the View Models for revenues fragment to detect and communicate the change in data source.
     * The budgetTypesViewModel will monitor the change of categories when a new category is created
     * The transactionsViewModel will monitor the change of transactions when a new transactions is inserted into the database
     * These view models will notify the adapter when the list of data has changed when a change is observed.
     *
     * @see BudgetTypesViewModel
     * @see TransactionsViewModel
     */
    private void setUpRevenuesViewModels() {
        BudgetTypesViewModel budgetTypesViewModel = ViewModelProviders.of(transactionActivity).get(BudgetTypesViewModel.class);
        budgetTypesViewModel.getCategoryLabels().observe(this, new Observer<List<BudgetsType>>() {
            @Override
            public void onChanged(List<BudgetsType> newBudgetsTypes) {
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

    /**
     * Set up the View Models for expenses fragment to detect and communicate the change in data source.
     * The budgetTypesViewModel will monitor the change of categories when a new category is created
     * The transactionsViewModel will monitor the change of transactions when a new transactions is inserted into the database
     * These view models will notify the adapter when the list of data has changed when a change is observed.
     *
     * @see BudgetTypesViewModel
     * @see TransactionsViewModel
     */
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

    /**
     * Set up the empty message based on the status of the revenuesList.
     * If the revenuesList is empty, the empty message will be visible.
     * If the revenuesList has items, the empty message will be invisible.
     */
    public void setUpRevenuesEmptyMessage() {
        if (revenuesList.size() != 0) {
            emptyMessageField.setVisibility(View.GONE);
        } else {
            emptyMessageField.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set up the empty message based on the status of the expensesList.
     * If the expensesList is empty, the empty message will be visible.
     * If the expensesList has items, the empty message will be invisible.
     */
    public void setUpExpensesEmptyMessage() {
        if (expensesList.size() != 0) {
            emptyMessageField.setVisibility(View.GONE);
        } else {
            emptyMessageField.setVisibility(View.VISIBLE);
        }
    }
}
