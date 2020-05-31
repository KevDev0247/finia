package protect.FinanceLord.Communicators;

import android.os.Parcelable;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;

public interface NewCategoryCommunicator extends Parcelable {
    void budgetTypes(List<BudgetsType> budgetsTypes);
}
