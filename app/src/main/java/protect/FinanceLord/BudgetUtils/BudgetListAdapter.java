package protect.FinanceLord.BudgetUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.R;

public class BudgetListAdapter extends ArrayAdapter<BudgetsValue> {

    private List<BudgetsValue> budgetsValues;
    private List<BudgetsType> budgetsTypes;

    public BudgetListAdapter(@NonNull Context context, List<BudgetsValue> budgetsValues, List<BudgetsType> budgetsTypes) {
        super(context, 0, budgetsValues);
        this.budgetsValues = budgetsValues;
        this.budgetsTypes = budgetsTypes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BudgetsValue budgetsValue = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budgets_item, null, false);
        }

        TextView budgetName = convertView.findViewById(R.id.budget_item_name);
        TextView budgetStatus = convertView.findViewById(R.id.budget_item_status);
        ProgressBar budgetProgress = convertView.findViewById(R.id.budget_progress_bar);

        for (BudgetsType budgetsType : budgetsTypes) {
            if (budgetsType.getBudgetsCategoryId() == budgetsValue.getBudgetsCategoryId()) {
                budgetName.setText(budgetsType.getBudgetsName());
            }
        }


        return convertView;
    }
}
