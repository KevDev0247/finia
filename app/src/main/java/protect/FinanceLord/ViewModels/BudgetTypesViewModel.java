package protect.FinanceLord.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;

public class BudgetTypesViewModel extends ViewModel {
    private MutableLiveData<List<BudgetsType>> categoryLabels = new MutableLiveData<>();
    private MutableLiveData<List<BudgetTypesDataModel>> dataModels = new MutableLiveData<>();

    public void pushToBudgetTypes(List<BudgetsType> newBudgetTypes) {
        categoryLabels.setValue(newBudgetTypes);
    }

    public void pushToDataModels(List<BudgetTypesDataModel> newData) {
        dataModels.setValue(newData);
    }

    public MutableLiveData<List<BudgetsType>> getCategoryLabels() {
        return categoryLabels;
    }

    public MutableLiveData<List<BudgetTypesDataModel>> getBudgetsDataModels() {
        return dataModels;
    }
}
