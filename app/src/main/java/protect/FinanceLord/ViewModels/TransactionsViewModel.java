package protect.FinanceLord.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import protect.FinanceLord.Database.Transactions;

/**
 * The view model to store and manage transactions and share them between fragments, classes, and activities.
 * It will monitor the change in the data stored and push or retrieve new data from the view model.
 * The data will be stored in Mutable Live Data, a data holder class that can be observed within a given lifecycle
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class TransactionsViewModel extends ViewModel {
    private MutableLiveData<List<Transactions>> groupedTransactions = new MutableLiveData<>();

    public void pushToTransactionGroup(List<Transactions> transactions) {
        groupedTransactions.setValue(transactions);
    }

    public LiveData<List<Transactions>> getGroupedTransactions() {
        return groupedTransactions;
    }
}
