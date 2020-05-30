package protect.FinanceLord.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import protect.FinanceLord.Database.Transactions;

public class TransactionsViewModel extends ViewModel {
    private MutableLiveData<List<Transactions>> groupedTransactions = new MutableLiveData<>();

    public void pushToTransactionGroup(List<Transactions> transactions) {
        groupedTransactions.setValue(transactions);
    }

    public LiveData<List<Transactions>> getGroupedTransactions() {
        return groupedTransactions;
    }
}
