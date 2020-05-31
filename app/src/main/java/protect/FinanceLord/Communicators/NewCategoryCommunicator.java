package protect.FinanceLord.Communicators;

import android.os.Parcelable;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.TransactionEditingUtils.BudgetTypesDataModel;

public interface NewCategoryCommunicator extends Parcelable {
    void budgetDataModels(List<BudgetTypesDataModel> dataModels);

    void budgetTypes(List<BudgetsType> budgetsTypes);
}
