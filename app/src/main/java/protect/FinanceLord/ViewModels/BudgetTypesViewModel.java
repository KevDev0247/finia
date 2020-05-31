package protect.FinanceLord.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;

public class BudgetTypesViewModel extends ViewModel {
    private MutableLiveData<List<BudgetsType>> categoryLabels = new MutableLiveData<>();

    public void pushToBudgetTypes(List<BudgetsType> newBudgetTypes) {
        categoryLabels.setValue(newBudgetTypes);
    }

    public MutableLiveData<List<BudgetsType>> getCategoryLabels() {
        return categoryLabels;
    }
}
